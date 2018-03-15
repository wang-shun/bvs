/*******************************************************************************
 * Copyright (c) 2000, 2003 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 ******************************************************************************/
package org.eclipse.swt.events;

import org.eclipse.swt.internal.SWTEventListener;

/**
 * Classes which implement this interface provide methods that deal with the
 * events that are generated by moving and resizing controls.
 * <p>
 * After creating an instance of a class that implements this interface it can
 * be added to a control using the <code>addControlListener</code> method and
 * removed using the <code>removeControlListener</code> method. When a control
 * is moved or resized, the appropriate method will be invoked.
 * </p>
 * 
 * @see ControlAdapter
 * @see ControlEvent
 */
public interface ControlListener extends SWTEventListener {

  /**
   * Sent when the location (x, y) of a control changes relative to its parent
   * (or relative to the display, for <code>Shell</code>s).
   * 
   * @param e an event containing information about the move
   */
  void controlMoved( ControlEvent e );

  /**
   * Sent when the size (width, height) of a control changes.
   * 
   * @param e an event containing information about the resize
   */
  void controlResized( ControlEvent e );
}
