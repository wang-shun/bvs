package com.bizvisionsoft.bruiengine.assembly.field;

import static org.eclipse.swt.internal.widgets.LayoutUtil.createButtonLayoutData;
import static org.eclipse.swt.internal.widgets.LayoutUtil.createFillData;
import static org.eclipse.swt.internal.widgets.LayoutUtil.createGridLayout;
import static org.eclipse.swt.internal.widgets.LayoutUtil.createHorizontalFillData;

import java.util.List;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

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

import com.bizvisionsoft.service.model.RemoteFile;

public class MultiFileField extends EditorField {

	private final ServerPushSession pushSession;
	private ThreadPoolExecutor singleThreadExecutor;
	private Display display;
	private ScrolledComposite uploadsScroller;
	private Button deleteBtn;
	// private Label spacer;
	private UploadPanel placeHolder;
	private ProgressCollector progressCollector;
	private ClientFile[] clientFiles;
	private String[] filterExtensions;
	private long sizeLimit = -1;
	private long timeLimit = -1;

	private List<RemoteFile> value;

	public MultiFileField() {
		pushSession = new ServerPushSession();
	}

	@Override
	protected Control createControl(Composite parent) {
		display = parent.getDisplay();
		Composite pane = new Composite(parent, SWT.BORDER);
		pane.setLayout(createGridLayout(1, 0, 0));
		createContentArea(pane);
		createButtonsArea(pane);
		prepareOpen();
		return pane;
	}

	private void createButtonsArea(Composite parent) {
		Composite buttonsArea = new Composite(parent, SWT.NONE);
		buttonsArea.setLayout(createGridLayout(3, 8, 8));
		buttonsArea.setLayoutData(createHorizontalFillData());
		createFileUpload(buttonsArea, "添加");
		deleteBtn = createButton(buttonsArea, "删除");
		parent.getShell().setDefaultButton(deleteBtn);
		deleteBtn.addListener(SWT.Selection, e -> {
		});
		Button clearBtn = createButton(buttonsArea, "清空");
		clearBtn.addListener(SWT.Selection, e -> {
		});
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
		placeHolder = createPlaceHolder(scrolledContent);
	}

	private UploadPanel createPlaceHolder(Composite parent) {
		String text = "选择文件上传或拖放文件到此处";
		UploadPanel panel = new UploadPanel(parent, new String[] { text }, true);
		panel.setData(RWT.CUSTOM_VARIANT, "inline");
		panel.setLayoutData(createHorizontalFillData());
		return panel;
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

	private void prepareOpen() {
		pushSession.start();
		singleThreadExecutor = createSingleThreadExecutor();
		if (clientFiles != null && clientFiles.length > 0) {
			handleFileDrop(clientFiles);
		}
	}

	private void handleFileDrop(ClientFile[] clientFiles) {
		placeHolder.dispose();
		UploadPanel uploadPanel = createUploadPanel(getFileNames(clientFiles));
		updateScrolledComposite();
		Uploader uploader = new UploaderService(clientFiles);
		FileUploadHandler handler = new FileUploadHandler(new DiskFileUploadReceiver());
		handler.setMaxFileSize(sizeLimit);
		handler.setUploadTimeLimit(timeLimit);
		FileUploadRunnable uploadRunnable = new FileUploadRunnable(uploadPanel, progressCollector, uploader, handler);
		singleThreadExecutor.execute(uploadRunnable);
	}

	private void handleFileUploadSelection(FileUpload fileUpload) {
		placeHolder.dispose();
		UploadPanel uploadPanel = createUploadPanel(fileUpload.getFileNames());
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

	private UploadPanel createUploadPanel(String[] fileNames) {
		Composite parent = (Composite) uploadsScroller.getContent();
		UploadPanel uploadPanel = new UploadPanel(parent, fileNames, true);
		uploadPanel.setLayoutData(createHorizontalFillData());
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
		presentation();
	}

	private void presentation() {
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

	@Override
	protected void saveBefore() throws Exception {
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
			if (getQueue().size() == 0) {
				setButtonEnabled(true);
			}
		}

	}

}
