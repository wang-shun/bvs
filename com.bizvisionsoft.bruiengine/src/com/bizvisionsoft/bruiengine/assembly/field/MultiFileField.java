package com.bizvisionsoft.bruiengine.assembly.field;

import static org.eclipse.swt.internal.widgets.LayoutUtil.createButtonLayoutData;
import static org.eclipse.swt.internal.widgets.LayoutUtil.createFillData;
import static org.eclipse.swt.internal.widgets.LayoutUtil.createGridLayout;
import static org.eclipse.swt.internal.widgets.LayoutUtil.createHorizontalFillData;

import java.io.File;
import java.io.FileInputStream;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import org.bson.Document;
import org.eclipse.rap.fileupload.DiskFileUploadReceiver;
import org.eclipse.rap.fileupload.FileUploadHandler;
import org.eclipse.rap.rwt.RWT;
import org.eclipse.rap.rwt.client.ClientFile;
import org.eclipse.rap.rwt.dnd.ClientFileTransfer;
import org.eclipse.rap.rwt.service.ServerPushSession;
import org.eclipse.rap.rwt.widgets.FileUpload;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.ScrolledComposite;
import org.eclipse.swt.dnd.DND;
import org.eclipse.swt.dnd.DropTarget;
import org.eclipse.swt.dnd.DropTargetAdapter;
import org.eclipse.swt.dnd.DropTargetEvent;
import org.eclipse.swt.dnd.Transfer;
import org.eclipse.swt.events.ControlAdapter;
import org.eclipse.swt.events.ControlEvent;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.internal.widgets.FileUploadRunnable;
import org.eclipse.swt.internal.widgets.ProgressCollector;
import org.eclipse.swt.internal.widgets.UploadPanel;
import org.eclipse.swt.internal.widgets.Uploader;
import org.eclipse.swt.internal.widgets.UploaderService;
import org.eclipse.swt.internal.widgets.UploaderWidget;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;

import com.bizvisionsoft.bruiengine.Brui;
import com.bizvisionsoft.service.FileService;
import com.bizvisionsoft.service.ServicesLoader;
import com.bizvisionsoft.service.model.RemoteFile;
import com.bizvisionsoft.service.tools.Check;
import com.bizvisionsoft.service.tools.FileTools;
import com.bizvisionsoft.serviceconsumer.Services;
import com.mongodb.BasicDBList;

public class MultiFileField extends EditorField {

	private final ServerPushSession pushSession;
	private ThreadPoolExecutor singleThreadExecutor;
	private Display display;
	private ScrolledComposite uploadsScroller;
	private ProgressCollector progressCollector;
	private String[] filterExtensions;
	private long sizeLimit = -1;
	private long timeLimit = -1;

	private List<RemoteFile> value;

	private List<UploadPanel> uploadPanels;

	public MultiFileField() {
		pushSession = new ServerPushSession();
		uploadPanels = new ArrayList<UploadPanel>();
	}
	
	@Override
	protected boolean isVertivalLayout() {
		return true;
	}

	@Override
	protected Control createControl(Composite parent) {
		// 基本的设置
		int maxFileSizeSetting = fieldConfig.getMaxFileSize();
		if (maxFileSizeSetting != 0)
			sizeLimit = 1024l * 1024 * maxFileSizeSetting;
		int timeLimitSetting = fieldConfig.getTimeLimit();
		if (timeLimitSetting != 0)
			this.timeLimit = 1000l * timeLimitSetting;
		String exts = fieldConfig.getFileFilerExts();
		if (exts != null)
			filterExtensions = exts.split(",");

		// 创建UI
		display = parent.getDisplay();
		Composite pane = new Composite(parent, SWT.BORDER);
		pane.setLayout(createGridLayout(1, 0, 0));
		createContentArea(pane);
		if (!isReadOnly()) {
			createButtonsArea(pane);
		}

		pushSession.start();
		singleThreadExecutor = createSingleThreadExecutor();
		return pane;
	}

	private void createButtonsArea(Composite parent) {
		Composite buttonsArea = new Composite(parent, SWT.NONE);
		buttonsArea.setLayout(createGridLayout(1, 8, 8));
		buttonsArea.setLayoutData(createHorizontalFillData());
		createFileUpload(buttonsArea, "添加");
	}

	@Override
	protected Object getControlLayoutData() {
		GridData gd = (GridData) super.getControlLayoutData();
		gd.heightHint = 198;
		return gd;
	}

	protected FileUpload createFileUpload(Composite parent, String text) {
		FileUpload fileUpload = new FileUpload(parent, SWT.MULTI);
		fileUpload.setText(text);
		GridData gridData = createButtonLayoutData(fileUpload);
		fileUpload.setLayoutData(gridData);
		gridData.heightHint = 28;
		gridData.widthHint = 64;
		fileUpload.setData(RWT.CUSTOM_VARIANT, "inbox");
		if (filterExtensions != null) {
			fileUpload.setFilterExtensions(filterExtensions);
		}
		fileUpload.addListener(SWT.Selection, new Listener() {
			@Override
			public void handleEvent(Event event) {
				handleFileUploadSelection((FileUpload) event.widget);
			}
		});
		fileUpload.moveAbove(null);
		return fileUpload;
	}

	protected Button createButton(Composite parent, String text) {
		Button button = new Button(parent, SWT.PUSH);
		button.setText(text);
		GridData layoutData = createButtonLayoutData(button);
		layoutData.heightHint = 28;
		layoutData.widthHint = 64;
		button.setData(RWT.CUSTOM_VARIANT, "inbox");
		button.setLayoutData(layoutData);
		return button;
	}

	private void createContentArea(Composite parent) {
		Composite dialogArea = new Composite(parent, SWT.NONE);
		dialogArea.setLayoutData(createFillData());
		dialogArea.setLayout(createGridLayout(1, 0, 0));
		createUploadsArea(dialogArea);
		createProgressArea(dialogArea);
		createDropTarget(dialogArea);
	}

	private void createUploadsArea(Composite parent) {
		uploadsScroller = new ScrolledComposite(parent, SWT.V_SCROLL);
		uploadsScroller.setLayoutData(createFillData());
		uploadsScroller.setExpandHorizontal(true);
		uploadsScroller.setExpandVertical(true);
		Composite scrolledContent = new Composite(uploadsScroller, SWT.NONE);
		scrolledContent.setLayout(createGridLayout(1, 0, 0));
		uploadsScroller.setContent(scrolledContent);
		uploadsScroller.addControlListener(new ControlAdapter() {
			@Override
			public void controlResized(ControlEvent event) {
				updateScrolledComposite();
			}
		});
	}

	private void createProgressArea(Composite parent) {
		progressCollector = new ProgressCollector(parent);
		progressCollector.setLayoutData(createHorizontalFillData());
	}

	private void createDropTarget(Control control) {
		DropTarget dropTarget = new DropTarget(control, DND.DROP_MOVE | DND.DROP_COPY);
		dropTarget.setTransfer(new Transfer[] { ClientFileTransfer.getInstance() });
		dropTarget.addDropListener(new DropTargetAdapter() {
			@Override
			public void dropAccept(DropTargetEvent event) {
				if (!ClientFileTransfer.getInstance().isSupportedType(event.currentDataType)) {
					event.detail = DND.DROP_NONE;
				}
			}

			@Override
			public void drop(DropTargetEvent event) {
				handleFileDrop((ClientFile[]) event.data);
			}
		});
	}

	private void handleFileDrop(ClientFile[] clientFiles) {
		UploadPanel uploadPanel = createUploadPanel(getFileNames(clientFiles), null);
		updateScrolledComposite();
		Uploader uploader = new UploaderService(clientFiles);
		FileUploadHandler handler = new FileUploadHandler(new DiskFileUploadReceiver());
		handler.setMaxFileSize(sizeLimit);
		handler.setUploadTimeLimit(timeLimit);
		FileUploadRunnable uploadRunnable = new FileUploadRunnable(uploadPanel, progressCollector, uploader, handler);
		singleThreadExecutor.execute(uploadRunnable);
	}

	private void handleFileUploadSelection(FileUpload fileUpload) {
		UploadPanel uploadPanel = createUploadPanel(fileUpload.getFileNames(), null);
		updateScrolledComposite();
		updateButtonsArea(fileUpload);
		Uploader uploader = new UploaderWidget(fileUpload);
		FileUploadHandler handler = new FileUploadHandler(new DiskFileUploadReceiver());
		handler.setMaxFileSize(sizeLimit);
		handler.setUploadTimeLimit(timeLimit);
		FileUploadRunnable uploadRunnable = new FileUploadRunnable(uploadPanel, progressCollector, uploader, handler);
		singleThreadExecutor.execute(uploadRunnable);
	}

	private void updateButtonsArea(FileUpload fileUpload) {
		Composite buttonsArea = fileUpload.getParent();
		hideControl(fileUpload);
		createFileUpload(buttonsArea, "添加");
		buttonsArea.layout();
	}

	private UploadPanel createUploadPanel(String[] fileNames, List<RemoteFile> value) {
		Composite parent = (Composite) uploadsScroller.getContent();
		UploadPanel uploadPanel = new UploadPanel(parent, Arrays.asList(fileNames), true, value, isReadOnly());
		uploadPanel.setLayoutData(createHorizontalFillData());
		uploadPanel.setURLMaker(f -> {
			String url;
			try {
				url = "/bvs/fs?id=" + URLEncoder.encode(f.getPath(), "utf-8");
			} catch (UnsupportedEncodingException e) {
				url = "";
			}
			return "<a style='color:#4a4a4a;' target='_blank' href='" + url + "'>" + f.getName() + "</a>";
		});

		uploadPanels.add(uploadPanel);
		return uploadPanel;
	}

	private void updateScrolledComposite() {
		Composite content = (Composite) uploadsScroller.getContent();
		for (int i = 0; i < 2; i++) { // workaround for bug 414868
			Rectangle clientArea = uploadsScroller.getBounds();
			Point minSize = content.computeSize(clientArea.width, SWT.DEFAULT);
			uploadsScroller.setMinSize(minSize);
		}
		uploadsScroller.setOrigin(0, 10000);
		content.layout();
	}

	@Override
	protected void dispose() {
		pushSession.stop();
		super.dispose();
	}

	@SuppressWarnings("unchecked")
	@Override
	public void setValue(Object value) {
		this.value = (List<RemoteFile>) value;
		// 添加uploadpanel
		if (this.value != null && this.value.size() > 0) {
			String[] items = new String[this.value.size()];
			for (int i = 0; i < this.value.size(); i++) {
				RemoteFile remoteFile = this.value.get(i);
				String url = remoteFile.getURL(ServicesLoader.url);
				items[i] = "<a style='color:#4a4a4a;' target='_blank' href='" + url + "'>" + remoteFile.name + "</a>";
			}
			createUploadPanel(items, this.value);
		}
	}

	@Override
	public Object getValue() {
		return value;
	}

	@Override
	protected void check(boolean saveCheck) throws Exception {
		// 必填检查
		if (saveCheck && (fieldConfig.isRequired() && (value == null || value.isEmpty()))) {
			throw new Exception(fieldConfig.getFieldText() + "必填。");
		}
		// 类型检查

	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Override
	protected void saveBefore() throws Exception {
		List<RemoteFile> newValue = new ArrayList<RemoteFile>();
		for (Iterator<UploadPanel> iterator = uploadPanels.iterator(); iterator.hasNext();) {
			UploadPanel uploadPanel = iterator.next();
			List rfs = uploadPanel.getRemotesFiles();
			if (rfs != null) {
				newValue.addAll(rfs);
			}
			List<File> lfs = uploadPanel.getLocalFiles();
			if (lfs != null) {
				for (int i = 0; i < lfs.size(); i++) {
					File file = lfs.get(i);
					String contentType = FileTools.getContentType(file, null);
					String uploadBy = Brui.sessionManager.getUser().getUserId();
					String fileNamespace = fieldConfig.getFileNamespace();
					if(Check.isNotAssigned(fileNamespace)) {
						throw new RuntimeException("未设置文件保存的名称空间，字段："+fieldConfig.getName());
					}
					RemoteFile rf = Services.get(FileService.class).upload(new FileInputStream(file), file.getName(),
							fileNamespace, contentType, uploadBy);
					newValue.add(rf);
				}
			}
		}

		if (value != null) {
			List<RemoteFile> needToDelete = new ArrayList<RemoteFile>();
			needToDelete.addAll(value);
			needToDelete.removeAll(newValue);
			needToDelete.forEach(rf -> {
				Services.get(FileService.class).delete(rf._id.toString(), rf.namepace);
			});

			value.clear();
			value.addAll(newValue);
		} else {
			value = newValue;
		}

		super.saveBefore();
	}

	private void setButtonEnabled(final boolean enabled) {
		if (!display.isDisposed()) {
			display.asyncExec(new Runnable() {
				@Override
				public void run() {
					// if (!okButton.isDisposed()) {
					// okButton.setEnabled(enabled);
					// }
				}
			});
		}
	}

	private static String[] getFileNames(ClientFile[] clientFiles) {
		String[] fileNames = new String[clientFiles.length];
		for (int i = 0; i < fileNames.length; i++) {
			fileNames[i] = clientFiles[i].getName();
		}
		return fileNames;
	}

	private static void hideControl(Control control) {
		if (control != null) {
			GridData layoutData = (GridData) control.getLayoutData();
			layoutData.exclude = true;
			control.setVisible(false);
		}
	}

	private ThreadPoolExecutor createSingleThreadExecutor() {
		return new SingleThreadExecutor();
	}

	private final class SingleThreadExecutor extends ThreadPoolExecutor {

		public SingleThreadExecutor() {
			super(1, 1, 0L, TimeUnit.MILLISECONDS, new LinkedBlockingQueue<Runnable>());
		}

		@Override
		protected void beforeExecute(Thread thread, Runnable runnable) {
			setButtonEnabled(false);
		}

		@Override
		protected void afterExecute(Runnable runnable, Throwable throwable) {
			if (getQueue().size() == 0)
				setButtonEnabled(true);
		}

	}
	
	@Override
	protected Object decodeValue_DBObject(Object value) {
		BasicDBList result = new BasicDBList();
		this.value.forEach(rf-> result.add(rf.encodeBson()));
		return result;
	}
	
	@Override
	protected Object decodeValue_Document(Object value) {
		ArrayList<Document> result = new ArrayList<>();
		this.value.forEach(rf-> result.add(rf.encodeDocument()));
		return result;
	}

}
