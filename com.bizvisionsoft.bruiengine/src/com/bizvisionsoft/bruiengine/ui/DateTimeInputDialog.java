package com.bizvisionsoft.bruiengine.ui;

import java.util.Date;
import java.util.function.Function;

import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.jface.resource.StringConverter;
import org.eclipse.rap.rwt.RWT;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;

import com.bizivisionsoft.widgets.datetime.DateTime;
import com.bizivisionsoft.widgets.datetime.DateTimeSetting;

public class DateTimeInputDialog extends Dialog {
	/**
	 * The title of the dialog.
	 */
	private String title;

	/**
	 * The message to display, or <code>null</code> if none.
	 */
	private String message;

	/**
	 * The input value; the empty string by default.
	 */
	private Date value;// $NON-NLS-1$

	/**
	 * The input validator, or <code>null</code> if none.
	 */
	private Function<Date, String> validator;

	/**
	 * Ok button widget.
	 */
	private Button okButton;

	/**
	 * Input text widget.
	 */
	private DateTime text;

	/**
	 * Error message label widget.
	 */
	private Text errorMessageText;

	/**
	 * Error message string.
	 */
	private String errorMessage;

	private DateTimeSetting dateSetting = DateTimeSetting.date();

	/**
	 * Creates an input dialog with OK and Cancel buttons. Note that the dialog will
	 * have no visual representation (no widgets) until it is told to open.
	 * <p>
	 * Note that the <code>open</code> method blocks for input dialogs.
	 * </p>
	 * 
	 * @param parentShell
	 *            the parent shell, or <code>null</code> to create a top-level shell
	 * @param dialogTitle
	 *            the dialog title, or <code>null</code> if none
	 * @param dialogMessage
	 *            the dialog message, or <code>null</code> if none
	 * @param initialValue
	 *            the initial input value, or <code>null</code> if none (equivalent
	 *            to the empty string)
	 * @param validator
	 *            an input validator, or <code>null</code> if none
	 */
	public DateTimeInputDialog(Shell parentShell, String dialogTitle, String dialogMessage, Date initialValue,
			Function<Date, String> validator) {
		super(parentShell);
		this.title = dialogTitle;
		message = dialogMessage;
		value = initialValue;
		this.validator = validator;
	}

	/*
	 * (non-Javadoc) Method declared on Dialog.
	 */
	protected void buttonPressed(int buttonId) {
		if (buttonId == IDialogConstants.OK_ID) {
			value = text.getDate();
		} else {
			value = null;
		}
		super.buttonPressed(buttonId);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.jface.window.Window#configureShell(org.eclipse.swt.widgets.Shell)
	 */
	protected void configureShell(Shell shell) {
		super.configureShell(shell);
		if (title != null) {
			shell.setText(title);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.jface.dialogs.Dialog#createButtonsForButtonBar(org.eclipse.swt.
	 * widgets.Composite)
	 */
	protected void createButtonsForButtonBar(Composite parent) {
		// create OK and Cancel buttons by default
		createButton(parent, IDialogConstants.CANCEL_ID, IDialogConstants.get().CANCEL_LABEL, false)
				.setData(RWT.CUSTOM_VARIANT, "warning");
		// do this here because setting the text will set enablement on the ok
		// button
		okButton = createButton(parent, IDialogConstants.OK_ID, IDialogConstants.get().OK_LABEL, true);
		okButton.setData(RWT.CUSTOM_VARIANT, "normal");
		text.setFocus();
		if (value != null) {
			text.setDate(value);
		}
	}

	/*
	 * (non-Javadoc) Method declared on Dialog.
	 */
	protected Control createDialogArea(Composite parent) {
		// create composite
		Composite composite = (Composite) super.createDialogArea(parent);
		// create message
		if (message != null) {
			Label label = new Label(composite, SWT.WRAP);
			label.setText(message);
			GridData data = new GridData(GridData.GRAB_HORIZONTAL | GridData.GRAB_VERTICAL
					| GridData.HORIZONTAL_ALIGN_FILL | GridData.VERTICAL_ALIGN_CENTER);
			data.widthHint = 400;
			label.setLayoutData(data);
			label.setFont(parent.getFont());
		}
		text = new DateTime(composite, getDateSetting());
		text.setLayoutData(new GridData(GridData.GRAB_HORIZONTAL | GridData.HORIZONTAL_ALIGN_FILL));
		text.addListener(SWT.Modify, e -> {
			validateInput();
		});
		errorMessageText = new Text(composite, SWT.READ_ONLY | SWT.WRAP);
		errorMessageText.setLayoutData(new GridData(GridData.GRAB_HORIZONTAL | GridData.HORIZONTAL_ALIGN_FILL));
		errorMessageText.setBackground(errorMessageText.getDisplay().getSystemColor(SWT.COLOR_WIDGET_BACKGROUND));
		// Set the error message text
		// See https://bugs.eclipse.org/bugs/show_bug.cgi?id=66292
		setErrorMessage(errorMessage);

		applyDialogFont(composite);
		return composite;
	}

	/**
	 * Returns the error message label.
	 * 
	 * @return the error message label
	 * @deprecated use setErrorMessage(String) instead
	 */
	protected Label getErrorMessageLabel() {
		return null;
	}

	/**
	 * Returns the ok button.
	 * 
	 * @return the ok button
	 */
	protected Button getOkButton() {
		return okButton;
	}

	/**
	 * Returns the text area.
	 * 
	 * @return the text area
	 */
	protected DateTime getText() {
		return text;
	}

	/**
	 * Returns the validator.
	 * 
	 * @return the validator
	 */
	protected Function<Date, String> getValidator() {
		return validator;
	}

	/**
	 * Returns the string typed into this input dialog.
	 * 
	 * @return the input string
	 */
	public Date getValue() {
		return value;
	}

	/**
	 * Validates the input.
	 * <p>
	 * The default implementation of this framework method delegates the request to
	 * the supplied input validator object; if it finds the input invalid, the error
	 * message is displayed in the dialog's message line. This hook method is called
	 * whenever the text changes in the input field.
	 * </p>
	 */
	protected void validateInput() {
		String errorMessage = null;
		if (validator != null) {
			errorMessage = validator.apply(text.getDate());
		}
		// Bug 16256: important not to treat "" (blank error) the same as null
		// (no error)
		setErrorMessage(errorMessage);
	}

	/**
	 * Sets or clears the error message. If not <code>null</code>, the OK button is
	 * disabled.
	 * 
	 * @param errorMessage
	 *            the error message, or <code>null</code> to clear
	 */
	public void setErrorMessage(String errorMessage) {
		this.errorMessage = errorMessage;
		if (errorMessageText != null && !errorMessageText.isDisposed()) {
			errorMessageText.setText(errorMessage == null ? " \n " : errorMessage); //$NON-NLS-1$
			// Disable the error message text control if there is no error, or
			// no error text (empty or whitespace only). Hide it also to avoid
			// color change.
			// See https://bugs.eclipse.org/bugs/show_bug.cgi?id=130281
			boolean hasError = errorMessage != null && (StringConverter.removeWhiteSpaces(errorMessage)).length() > 0;
			errorMessageText.setEnabled(hasError);
			errorMessageText.setVisible(hasError);
			errorMessageText.getParent().update();
			// Access the ok button by id, in case clients have overridden button creation.
			// See https://bugs.eclipse.org/bugs/show_bug.cgi?id=113643
			Control button = getButton(IDialogConstants.OK_ID);
			if (button != null) {
				button.setEnabled(errorMessage == null);
			}
		}
	}

	public DateTimeSetting getDateSetting() {
		return dateSetting;
	}

	public DateTimeInputDialog setDateSetting(DateTimeSetting dateSetting) {
		this.dateSetting = dateSetting;
		return this;
	}
}