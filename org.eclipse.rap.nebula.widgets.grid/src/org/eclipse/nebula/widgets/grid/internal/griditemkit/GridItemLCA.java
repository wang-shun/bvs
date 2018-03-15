/*******************************************************************************
 * Copyright (c) 2012, 2015 EclipseSource and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    EclipseSource - initial API and implementation
 ******************************************************************************/
package org.eclipse.nebula.widgets.grid.internal.griditemkit;

import static org.eclipse.rap.rwt.internal.lifecycle.WidgetLCAUtil.preserveProperty;
import static org.eclipse.rap.rwt.internal.lifecycle.WidgetLCAUtil.renderProperty;
import static org.eclipse.rap.rwt.internal.protocol.RemoteObjectFactory.createRemoteObject;
import static org.eclipse.rap.rwt.internal.protocol.RemoteObjectFactory.getRemoteObject;

import java.io.IOException;

import org.eclipse.nebula.widgets.grid.Grid;
import org.eclipse.nebula.widgets.grid.GridItem;
import org.eclipse.nebula.widgets.grid.internal.IGridAdapter;
import org.eclipse.nebula.widgets.grid.internal.IGridItemAdapter;
import org.eclipse.rap.rwt.internal.lifecycle.RemoteAdapter;
import org.eclipse.rap.rwt.internal.lifecycle.WidgetLCA;
import org.eclipse.rap.rwt.internal.lifecycle.WidgetLCAUtil;
import org.eclipse.rap.rwt.internal.lifecycle.WidgetUtil;
import org.eclipse.rap.rwt.internal.remote.RemoteObjectImpl;
import org.eclipse.rap.rwt.remote.RemoteObject;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.internal.widgets.IWidgetColorAdapter;
import org.eclipse.swt.internal.widgets.IWidgetFontAdapter;
import org.eclipse.swt.internal.widgets.WidgetRemoteAdapter;
import org.eclipse.swt.widgets.Widget;


@SuppressWarnings("restriction")
public class GridItemLCA extends WidgetLCA<GridItem> {

  public static final GridItemLCA INSTANCE = new GridItemLCA();

  private static final String TYPE = "rwt.widgets.GridItem";

  private static final String PROP_INDEX = "index";
  private static final String PROP_ITEM_COUNT = "itemCount";
  private static final String PROP_HEIGHT = "height";
  private static final String PROP_TEXTS = "texts";
  private static final String PROP_IMAGES = "images";
  private static final String PROP_CELL_BACKGROUNDS = "cellBackgrounds";
  private static final String PROP_CELL_FOREGROUNDS = "cellForegrounds";
  private static final String PROP_CELL_FONTS = "cellFonts";
  private static final String PROP_EXPANDED = "expanded";
  private static final String PROP_CELL_CHECKED = "cellChecked";
  private static final String PROP_CELL_GRAYED = "cellGrayed";
  private static final String PROP_CELL_CHECKABLE = "cellCheckable";
  private static final String PROP_CACHED = "cached";
  private static final String PROP_COLUMN_SPANS = "columnSpans";

  private static final int ZERO = 0;

  @Override
  public void renderInitialization( GridItem item ) throws IOException {
    RemoteObject remoteObject = createRemoteObject( item, TYPE );
    remoteObject.setHandler( new GridItemOperationHandler( item ) );
    remoteObject.set( "parent", WidgetUtil.getId( getParent( item ) ) );
  }

  @Override
  public void preserveValues( GridItem item ) {
    preserveProperty( item, PROP_INDEX, getItemIndex( item ) );
    preserveProperty( item, PROP_CACHED, isCached( item ) );
    if( isCached( item ) ) {
      preserveProperty( item, PROP_ITEM_COUNT, item.getItemCount() );
      preserveProperty( item, PROP_HEIGHT, item.getHeight() );
      preserveProperty( item, PROP_TEXTS, getTexts( item ) );
      preserveProperty( item, PROP_IMAGES, getImages( item ) );
      WidgetLCAUtil.preserveBackground( item, getUserBackground( item ) );
      WidgetLCAUtil.preserveForeground( item, getUserForeground( item ) );
      WidgetLCAUtil.preserveFont( item, getUserFont( item ) );
      preserveProperty( item, PROP_CELL_BACKGROUNDS, getCellBackgrounds( item ) );
      preserveProperty( item, PROP_CELL_FOREGROUNDS, getCellForegrounds( item ) );
      preserveProperty( item, PROP_CELL_FONTS, getCellFonts( item ) );
      preserveProperty( item, PROP_EXPANDED, item.isExpanded() );
      preserveProperty( item, PROP_CELL_CHECKED, getCellChecked( item ) );
      preserveProperty( item, PROP_CELL_GRAYED, getCellGrayed( item ) );
      preserveProperty( item, PROP_CELL_CHECKABLE, getCellCheckable( item ) );
      preserveProperty( item, PROP_COLUMN_SPANS, getColumnSpans( item ) );
    }
  }

  @Override
  public void renderChanges( final GridItem item ) throws IOException {
    renderProperty( item, PROP_INDEX, getItemIndex( item ), -1 );
    if( wasCleared( item ) ) {
      renderClear( item );
    } else if( isCached( item ) ) {
      preservingInitialized( item, new Runnable() {
        @Override
        public void run() {
          // items that were uncached and are now cached (materialized) are handled as if they were
          // just created (initialized = false)
          if( !wasCached( item ) ) {
            setInitialized( item, false );
          }
          renderProperties( item );
        }
      } );
    }
  }

  private static void renderClear( GridItem item ) {
    getRemoteObject( item ).call( "clear", null );
  }

  private static void renderProperties( GridItem item ) {
    WidgetLCAUtil.renderCustomVariant( item );
    WidgetLCAUtil.renderData( item );
    renderProperty( item, PROP_ITEM_COUNT, item.getItemCount(), ZERO );
    renderProperty( item, PROP_HEIGHT, item.getHeight(), item.getParent().getItemHeight() );
    renderProperty( item, PROP_TEXTS, getTexts( item ), null );
    renderProperty( item, PROP_IMAGES, getImages( item ), null );
    WidgetLCAUtil.renderBackground( item, getUserBackground( item ) );
    WidgetLCAUtil.renderForeground( item, getUserForeground( item ) );
    WidgetLCAUtil.renderFont( item, getUserFont( item ) );
    renderProperty( item, PROP_CELL_BACKGROUNDS, getCellBackgrounds( item ), null );
    renderProperty( item, PROP_CELL_FOREGROUNDS, getCellForegrounds( item ), null );
    renderProperty( item, PROP_CELL_FONTS, getCellFonts( item ), null );
    renderProperty( item, PROP_EXPANDED, item.isExpanded(), false );
    renderProperty( item, PROP_CELL_CHECKED, getCellChecked( item ), null );
    renderProperty( item, PROP_CELL_GRAYED, getCellGrayed( item ), null );
    renderProperty( item, PROP_CELL_CHECKABLE, getCellCheckable( item ), null );
    renderProperty( item, PROP_COLUMN_SPANS, getColumnSpans( item ), null );
  }

  @Override
  public void renderDispose( GridItem item ) throws IOException {
    RemoteObject remoteObject = getRemoteObject( item );
    if( !isParentDisposed( item ) ) {
      // The tree disposes the items itself on the client (faster)
      remoteObject.destroy();
    } else {
      ( ( RemoteObjectImpl )remoteObject ).markDestroyed();
    }
  }


  //////////////////
  // Helping methods

  private static boolean wasCleared( GridItem item ) {
    return !isCached( item ) && wasCached( item );
  }

  private static boolean isCached( GridItem item ) {
    return getGridItemAdapter( item ).isCached();
  }

  private static boolean wasCached( GridItem item ) {
    RemoteAdapter adapter = WidgetUtil.getAdapter( item );
    if( adapter.isInitialized() ) {
      return Boolean.TRUE.equals( adapter.getPreserved( PROP_CACHED ) );
    }
    return false;
  }

  private static Widget getParent( GridItem item ) {
    Widget result = item.getParent();
    GridItem parentItem = item.getParentItem();
    if( parentItem != null ) {
      result = item.getParentItem();
    }
    return result;
  }

  private static int getItemIndex( GridItem item ) {
    return getGridAdapter( item.getParent() ).getItemIndex( item );
  }

  private static boolean isParentDisposed( GridItem item ) {
    return getGridItemAdapter( item ).isParentDisposed();
  }

  private static String[] getTexts( GridItem item ) {
    return getGridItemAdapter( item ).getTexts();
  }

  private static Image[] getImages( GridItem item ) {
    return getGridItemAdapter( item ).getImages();
  }

  private static Color getUserBackground( GridItem item ) {
    return item.getAdapter( IWidgetColorAdapter.class ).getUserBackground();
  }

  private static Color getUserForeground( GridItem item ) {
    return item.getAdapter( IWidgetColorAdapter.class ).getUserForeground();
  }

  private static Font getUserFont( GridItem item ) {
    return item.getAdapter( IWidgetFontAdapter.class ).getUserFont();
  }

  private static Color[] getCellBackgrounds( GridItem item ) {
    return getGridItemAdapter( item ).getCellBackgrounds();
  }

  private static Color[] getCellForegrounds( GridItem item ) {
    return getGridItemAdapter( item ).getCellForegrounds();
  }

  private static Font[] getCellFonts( GridItem item ) {
    return getGridItemAdapter( item ).getCellFonts();
  }

  private static boolean[] getCellChecked( GridItem item ) {
    return getGridItemAdapter( item ).getCellChecked();
  }

  private static boolean[] getCellGrayed( GridItem item ) {
    return getGridItemAdapter( item ).getCellGrayed();
  }

  private static boolean[] getCellCheckable( GridItem item ) {
    return getGridItemAdapter( item ).getCellCheckable();
  }

  private static int[] getColumnSpans( GridItem item ) {
    return getGridItemAdapter( item ).getColumnSpans();
  }

  private static IGridAdapter getGridAdapter( Grid grid ) {
    return grid.getAdapter( IGridAdapter.class );
  }

  private static IGridItemAdapter getGridItemAdapter( GridItem item ) {
    return item.getAdapter( IGridItemAdapter.class );
  }

  private static void preservingInitialized( GridItem item, Runnable runnable ) {
    boolean initialized = WidgetUtil.getAdapter( item ).isInitialized();
    runnable.run();
    setInitialized( item, initialized );
  }

  private static void setInitialized( GridItem item, boolean initialized ) {
    WidgetRemoteAdapter adapter = ( WidgetRemoteAdapter )WidgetUtil.getAdapter( item );
    adapter.setInitialized( initialized );
  }

}
