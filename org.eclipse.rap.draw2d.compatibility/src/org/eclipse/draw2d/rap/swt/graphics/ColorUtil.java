package org.eclipse.draw2d.rap.swt.graphics;

import java.text.MessageFormat;

import org.eclipse.jface.resource.ColorRegistry;
import org.eclipse.jface.resource.JFaceResources;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.RGB;


public class ColorUtil {

  private final static String COLOR_KEY = "DRAW2D_{0}_{1}_{2}"; //$NON-NLS-1$

  public static Color getColor( int red, int green, int blue ) {
    String key = MessageFormat.format( COLOR_KEY, red, green, blue );
    ColorRegistry reg = JFaceResources.getColorRegistry();
    Color color = reg.get( key );
    if( color == null ) {
      reg.put( key, new RGB( red, green, blue ) );
      color = reg.get( key );
    }
    return color;
  }

  public static Color getColor( int rgb ) {
    return getColor( rgb, rgb, rgb );
  }
}
