/*******************************************************************************
 * Copyright (c) 2013, 2017 EclipseSource and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    EclipseSource - initial API and implementation
 ******************************************************************************/
package org.eclipse.swt.internal.widgets;

import static org.eclipse.swt.internal.Compatibility.getMessage;
import static org.eclipse.swt.internal.widgets.LayoutUtil.createGridLayout;
import static org.eclipse.swt.internal.widgets.LayoutUtil.createHorizontalFillData;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

import org.eclipse.rap.fileupload.UploadSizeLimitExceededException;
import org.eclipse.rap.fileupload.UploadTimeLimitExceededException;
import org.eclipse.rap.rwt.RWT;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.internal.widgets.FileUploadRunnable.State;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;

public class UploadPanel extends Composite {

	private static long KB = 1000;
	private static long MB = 1000 * 1000;
	private static long SEC = 1000;
	private static long MIN = 60 * 1000;

	private Image emptyIcon;
	private Image waitingIcon;
	private Image uploadingIcon;
	private Image finishedIcon;
	private Image failedIcon;

	private final List<Label> icons;
	private final List<Label> names;

	private final List<String> fileNames;
	private final List<Button> removeBtns;
	private final List<Composite> containers;

	private List<Object> remotesFiles;
	private List<File> localFiles;

	private boolean embed;
	private Function<File, String> maker;
	private boolean readOnly;

	public UploadPanel(Composite parent, List<String> fileNames, boolean embed, List<?> remotesFiles,
			boolean readOnly) {
		super(parent, SWT.NONE);
		this.readOnly = readOnly;
		this.fileNames = new ArrayList<>();
		this.fileNames.addAll(fileNames);
		this.embed = embed;
		this.remotesFiles = new ArrayList<>();
		if (remotesFiles != null)
			this.remotesFiles.addAll(remotesFiles);
		initImages();
		setLayout(createGridLayout(1, 0, embed ? 0 : 4));
		icons = new ArrayList<>();
		names = new ArrayList<>();
		removeBtns = new ArrayList<>();
		containers = new ArrayList<>();
		createChildren();
	}

	public UploadPanel(Composite parent, List<String> fileNames) {
		this(parent, fileNames, false, null, false);
	}

	private void initImages() {
		Display display = getDisplay();
		emptyIcon = ImageUtil.getImage(display, "empty.png");
		waitingIcon = ImageUtil.getImage(display, "waiting.png");
		uploadingIcon = ImageUtil.getImage(display, "uploading.png");
		finishedIcon = ImageUtil.getImage(display, "finished.png");
		failedIcon = ImageUtil.getImage(display, "failed.png");
	}

	public void updateFiles(List<File> files) {
		this.localFiles = new ArrayList<File>();
		this.localFiles.addAll(files);
		if (maker != null) {
			for (int i = 0; i < files.size(); i++) {
				names.get(i).setText(maker.apply(files.get(i)));
				removeBtns.get(i).setVisible(true);
			}
			layout();
		}
	}

	public void setURLMaker(Function<File, String> func) {
		this.maker = func;
	}

	private void createChildren() {
		for (int index = 0; index < fileNames.size(); index++) {
			Composite container = new Composite(this, SWT.BORDER);
			containers.add(container);
			if (embed) {
				container.setData(RWT.CUSTOM_VARIANT, "inline");
			}
			GridLayout layout = createContainerLayout();
			layout.numColumns = embed && !readOnly ? 3 : 2;
			container.setLayout(layout);
			container.setLayoutData(createHorizontalFillData());
			Label icon = new Label(container, SWT.NONE);
			icon.setImage(remotesFiles != null ? finishedIcon : emptyIcon);
			icons.add(icon);
			Label name = new Label(container, SWT.NONE);
			name.setLayoutData(createHorizontalFillData());
			if (embed) {
				name.setData(RWT.MARKUP_ENABLED, Boolean.TRUE);
				name.setData(MarkupValidator.MARKUP_VALIDATION_DISABLED, Boolean.TRUE);
			}
			name.setText(fileNames.get(index));
			names.add(name);
			if (embed && !readOnly) {
				Button btn = new Button(container, SWT.PUSH);
				btn.setText("É¾³ý");
				removeBtns.add(btn);
				GridData gd = new GridData(SWT.FILL, SWT.FILL, false, false);
				gd.widthHint = 56;
				gd.heightHint = 24;
				btn.setLayoutData(gd);
				btn.setVisible(remotesFiles != null);
				btn.addListener(SWT.Selection, e -> {
					removeItem(container);
				});
			}
		}
	}

	private void removeItem(Composite container) {
		int idx = containers.indexOf(container);
		containers.remove(idx);
		icons.remove(idx);
		removeBtns.remove(idx);
		names.remove(idx);
		fileNames.remove(idx);
		if (remotesFiles != null && remotesFiles.size() > idx) {
			remotesFiles.remove(idx);
		}
		if (localFiles != null && localFiles.size() > idx) {
			localFiles.remove(idx);
		}
		container.dispose();
		layout();
	}

	private static GridLayout createContainerLayout() {
		GridLayout layout = new GridLayout(3, false);
		layout.verticalSpacing = 0;
		return layout;
	}

	void updateIcons(State state) {
		for (Label icon : icons) {
			if (!icon.isDisposed()) {
				icon.setImage(getImage(state));
			}
		}
	}

	void updateToolTips(Exception exception) {
		for (Label icon : icons) {
			if (!icon.isDisposed()) {
				icon.setToolTipText(getToolTip(exception));
			}
		}
	}

	private Image getImage(State state) {
		Image image = emptyIcon;
		if (state.equals(State.WAITING)) {
			image = waitingIcon;
		} else if (state.equals(State.UPLOADING)) {
			image = uploadingIcon;
		} else if (state.equals(State.FINISHED)) {
			image = finishedIcon;
		} else if (state.equals(State.FAILED)) {
			image = failedIcon;
		}
		return image;
	}

	private String getToolTip(Exception exception) {
		if (exception instanceof UploadSizeLimitExceededException) {
			long size = ((UploadSizeLimitExceededException) exception).getSizeLimit();
			String key = "SWT_UploadFailed_SizeLimitExceeded";
			key += icons.size() == 1 ? "_Single" : "_Multi";
			return getMessage(key, new Object[] { formatSize(size) });
		} else if (exception instanceof UploadTimeLimitExceededException) {
			long time = ((UploadTimeLimitExceededException) exception).getTimeLimit();
			return getMessage("SWT_UploadFailed_TimeLimitExceeded", new Object[] { formatTime(time) });
		} else if (exception != null) {
			return SWT.getMessage("SWT_UploadFailed");
		}
		return null;
	}

	private static String formatSize(long size) {
		if (size >= MB) {
			return Math.round(size / MB) + " MB";
		} else if (size >= KB) {
			return Math.round(size / KB) + " kB";
		}
		return size + " B";
	}

	private static String formatTime(long time) {
		if (time >= MIN) {
			return Math.round(time / MIN) + " min";
		} else if (time >= SEC) {
			return Math.round(time / SEC) + " sec";
		}
		return time + " milliseconds";
	}

	public List<Object> getRemotesFiles() {
		return remotesFiles;
	}

	public List<File> getLocalFiles() {
		return localFiles;
	}

}
