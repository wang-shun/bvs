package com.bizvisionsoft.bruiengine.ui;

import java.io.Serializable;

import org.eclipse.jface.util.Geometry;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Monitor;
import org.eclipse.swt.widgets.Shell;

public abstract class Part implements Serializable {

	private int shellStyle = SWT.NO_TRIM | SWT.ON_TOP;

	private Shell shell;

	private int returnCode = 0;

	private boolean block = false;

	private Shell parentShell;

	protected Part(Shell parentShell) {
		this.parentShell = parentShell;
	}

	public boolean close() {
		if (shell == null || shell.isDisposed()) {
			return true;
		}

		shell.dispose();
		shell = null;
		return true;
	}

	protected void configureShell(Shell newShell) {
	}

	protected void constrainShellSize() {
		Rectangle bounds = shell.getBounds();
		Rectangle constrained = getConstrainedShellBounds(bounds);
		if (!bounds.equals(constrained)) {
			shell.setBounds(constrained);
		}
	}

	public void create() {
		shell = createShell();
		createContents(shell);
		if (!shell.getFullScreen()) {
			initializeBounds();
		}
	}

	protected abstract void createContents(Composite parent);

	/**
	 * Creates and returns this window's shell.
	 * <p>
	 * This method creates a new shell and configures it using
	 * <code>configureShell</code>. Subclasses should override
	 * <code>configureShell</code> if the shell needs to be customized.
	 * </p>
	 * 
	 * @return the shell
	 */
	protected final Shell createShell() {
		// CreateRoot the shell
		Shell newShell = new Shell(parentShell, getShellStyle());
		newShell.setBackgroundMode(SWT.INHERIT_DEFAULT);
		// Set the layout
		configureShell(newShell);

		return newShell;
	}

	protected Point getInitialLocation(Point initialSize) {
		Composite parent = shell.getParent();

		Monitor monitor = shell.getDisplay().getPrimaryMonitor();
		if (parent != null) {
			monitor = parent.getMonitor();
		}

		Rectangle monitorBounds = monitor.getClientArea();

		Point centerPoint;
		if (parent != null) {
			centerPoint = Geometry.centerPoint(parent.getBounds());
		} else {
			centerPoint = Geometry.centerPoint(monitorBounds);
		}

		return new Point(centerPoint.x - initialSize.x / 2, centerPoint.y - initialSize.y / 2);
	}

	protected Point getInitialSize() {
		return shell.computeSize(SWT.DEFAULT, SWT.DEFAULT, true);
	}

	public int getReturnCode() {
		return returnCode;
	}

	public Shell getShell() {
		return shell;
	}

	protected int getShellStyle() {
		return shellStyle;
	}

	/**
	 * Initializes the location and size of this window's SWT shell after it has
	 * been created.
	 * <p>
	 * This framework method is called by the <code>create</code> framework method.
	 * The default implementation calls <code>getInitialSize</code> and
	 * <code>getInitialLocation</code> and passes the results to
	 * <code>Shell.setBounds</code>. This is only done if the bounds of the shell
	 * have not already been modified. Subclasses may extend or reimplement.
	 * </p>
	 */
	protected void initializeBounds() {
		Point size = getInitialSize();
		Point location = getInitialLocation(size);
		shell.setBounds(getConstrainedShellBounds(new Rectangle(location.x, location.y, size.x, size.y)));
	}

	/**
	 * Opens this window, creating it first if it has not yet been created.
	 * <p>
	 * If this window has been configured to block on open (
	 * <code>setBlockOnOpen</code>), this method waits until the window is closed by
	 * the end user, and then it returns the window's return code; otherwise, this
	 * method returns immediately. A window's return codes are window-specific,
	 * although two standard return codes are predefined: <code>OK</code> and
	 * <code>CANCEL</code>.
	 * </p>
	 * 
	 * @return the return code
	 * 
	 * @see #create()
	 */
	public int open() {

		if (shell == null || shell.isDisposed()) {
			shell = null;
			// create the window
			create();
		}

		// limit the shell size to the display size
		constrainShellSize();

		// open the window
		shell.open();

		// run the event loop if specified
		if (block) {
			runEventLoop(shell);
		}

		return returnCode;
	}

	/**
	 * Runs the event loop for the given shell.
	 * 
	 * @param loopShell
	 *            the shell
	 */
	private void runEventLoop(Shell loopShell) {

		// Use the display provided by the shell if possible
		Display display;
		if (shell == null) {
			display = Display.getCurrent();
		} else {
			display = loopShell.getDisplay();
		}

		while (loopShell != null && !loopShell.isDisposed()) {
			try {
				if (!display.readAndDispatch()) {
					display.sleep();
				}
			} catch (Throwable e) {
				// RAP [if] Session scoped exceptionHandler
				// exceptionHandler.handleException(e);
			}
		}
		if (!display.isDisposed())
			display.update();
	}

	/**
	 * Sets whether the <code>open</code> method should block until the window
	 * closes.
	 * 
	 * @param shouldBlock
	 *            <code>true</code> if the <code>open</code> method should not
	 *            return until the window closes, and <code>false</code> if the
	 *            <code>open</code> method should return immediately
	 */
	public void setBlockOnOpen(boolean shouldBlock) {
		block = shouldBlock;
	}

	protected void setReturnCode(int code) {
		returnCode = code;
	}

	/**
	 * Returns the monitor whose client area contains the given point. If no monitor
	 * contains the point, returns the monitor that is closest to the point. If this
	 * is ever made public, it should be moved into a separate utility class.
	 * 
	 * @param toSearch
	 *            point to find (display coordinates)
	 * @param toFind
	 *            point to find (display coordinates)
	 * @return the montor closest to the given point
	 */
	private static Monitor getClosestMonitor(Display toSearch, Point toFind) {
		int closest = Integer.MAX_VALUE;

		Monitor[] monitors = toSearch.getMonitors();
		Monitor result = monitors[0];

		for (int idx = 0; idx < monitors.length; idx++) {
			Monitor current = monitors[idx];

			Rectangle clientArea = current.getClientArea();

			if (clientArea.contains(toFind)) {
				return current;
			}

			int distance = Geometry.distanceSquared(Geometry.centerPoint(clientArea), toFind);
			if (distance < closest) {
				closest = distance;
				result = current;
			}
		}

		return result;
	}

	/**
	 * Given the desired position of the window, this method returns an adjusted
	 * position such that the window is no larger than its monitor, and does not
	 * extend beyond the edge of the monitor. This is used for computing the initial
	 * window position, and subclasses can use this as a utility method if they want
	 * to limit the region in which the window may be moved.
	 * 
	 * @param preferredSize
	 *            the preferred position of the window
	 * @return a rectangle as close as possible to preferredSize that does not
	 *         extend outside the monitor
	 * 
	 * @since 1.0
	 */
	protected Rectangle getConstrainedShellBounds(Rectangle preferredSize) {
		Rectangle result = new Rectangle(preferredSize.x, preferredSize.y, preferredSize.width, preferredSize.height);

		Monitor mon = getClosestMonitor(getShell().getDisplay(), Geometry.centerPoint(result));

		Rectangle bounds = mon.getClientArea();

		if (result.height > bounds.height) {
			result.height = bounds.height;
		}

		if (result.width > bounds.width) {
			result.width = bounds.width;
		}

		result.x = Math.max(bounds.x, Math.min(result.x, bounds.x + bounds.width - result.width));
		result.y = Math.max(bounds.y, Math.min(result.y, bounds.y + bounds.height - result.height));

		return result;
	}

	/**
	 * Sets the shell style bits. This method has no effect after the shell is
	 * created.
	 * <p>
	 * The shell style bits are used by the framework method
	 * <code>createShell</code> when creating this window's shell.
	 * </p>
	 * 
	 * @param newShellStyle
	 *            the new shell style bits
	 */
	protected void setShellStyle(int newShellStyle) {
		shellStyle = newShellStyle;
	}

	public boolean isDisposed() {
		return shell.isDisposed();
	}

	public void dispose() {
		shell.dispose();
	}


}
