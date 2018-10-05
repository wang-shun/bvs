/*******************************************************************************
 * Copyright (c) 2005, 2006 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 ******************************************************************************/

package com.bizvisionsoft.bruicommons.model;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;

import com.bizvisionsoft.annotations.AUtil;

public class ModelObject implements Cloneable {

	private final transient PropertyChangeSupport propertyChangeSupport = new PropertyChangeSupport(this);

	public void addPropertyChangeListener(final PropertyChangeListener listener) {
		propertyChangeSupport.addPropertyChangeListener(listener);
	}

	public void addPropertyChangeListener(final String propertyName, final PropertyChangeListener listener) {
		propertyChangeSupport.addPropertyChangeListener(propertyName, listener);
	}

	public void removePropertyChangeListener(final PropertyChangeListener listener) {
		propertyChangeSupport.removePropertyChangeListener(listener);
	}

	public void removePropertyChangeListener(final String propertyName, final PropertyChangeListener listener) {
		propertyChangeSupport.removePropertyChangeListener(propertyName, listener);
	}

	protected void firePropertyChange(final String propertyName, final Object oldValue, final Object newValue) {
		propertyChangeSupport.firePropertyChange(propertyName, oldValue, newValue);
	}

	protected void firePropertyChange(final String propertyName, final int oldValue, final int newValue) {
		propertyChangeSupport.firePropertyChange(propertyName, oldValue, newValue);
	}

	protected void firePropertyChange(final String propertyName, final boolean oldValue, final boolean newValue) {
		propertyChangeSupport.firePropertyChange(propertyName, oldValue, newValue);
	}

	public boolean hasListeners(String propertyName,PropertyChangeListener listener) {
		PropertyChangeListener[] listeners = propertyChangeSupport.getPropertyChangeListeners(propertyName);
		for (int i = 0; i < listeners.length; i++) {
			if(listeners[i].equals(listener)) {
				return true;
			}
		}
		return false;
	}
	
	@Override
	public ModelObject clone()  {
		return AUtil.deepCopy(this);
	}

}
