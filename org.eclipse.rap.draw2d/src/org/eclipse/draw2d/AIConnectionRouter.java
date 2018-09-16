package org.eclipse.draw2d;

import java.util.List;

import org.eclipse.core.runtime.Assert;
import org.eclipse.draw2d.AbstractRouter;
import org.eclipse.draw2d.Connection;
import org.eclipse.draw2d.ConnectionAnchor;
import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.geometry.Point;
import org.eclipse.draw2d.geometry.PointList;
import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.draw2d.geometry.Vector;

/**
 * 具备自动避开节点的能力, 尽可能绕开的最短路径
 * 
 * @author dzh
 * @version 1.0 2011-12-16 上午10:33:38
 */
public class AIConnectionRouter extends AbstractRouter {

	private IFigure content;

	private static final int DEFAULT_SPACE = 10;

	private PointList points;

	private int space; /* 离障碍物的间隙 */

	private Vector endDirection;

	private Point endPoint;

	private static final Vector UP = new Vector(0, -1),
			DOWN = new Vector(0, 1), LEFT = new Vector(-1, 0),
			RIGHT = new Vector(1, 0);

	public AIConnectionRouter(IFigure content) {
		this(content, DEFAULT_SPACE);
	}

	public AIConnectionRouter(IFigure content, int space) {
		Assert.isNotNull(content);
		Assert.isLegal(space > 0, "Space must be gt zero.");

		this.content = content;
		this.space = space;
		this.points = new PointList();
	}

	private boolean validConn(Connection conn) {
		if ((conn.getSourceAnchor() == null)
				|| (conn.getTargetAnchor() == null))
			return false;

		// if (conn.getSourceAnchor().getOwner() == null
		// || conn.getTargetAnchor().getOwner() == null)
		// return false;

		return true;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.draw2d.ConnectionRouter#route(org.eclipse.draw2d.Connection)
	 */
	@Override
	public void route(Connection conn) {
		// testChildren(conn);
		Point startPoint = getStartPoint(conn).getCopy();
		// logger.debug("startPoint: " + startPoint);
		conn.translateToRelative(startPoint);
		// logger.debug("relativeStartPoint: " + startPoint);
		points.addPoint(startPoint);

		endPoint = getEndPoint(conn).getCopy();
		// logger.debug("endPoint: " + endPoint);
		conn.translateToRelative(endPoint);

		if (validConn(conn)) {
			Vector sdirection = getStartDirection(conn);
			// logger.debug("startDirection: " + direction);
			endDirection = getEndDirection(conn);

			// logger.debug("endDirection: " + endDirection);
			processPoints(startPoint, sdirection, null);
		}

		points.addPoint(endPoint);
		conn.setPoints(points.getCopy());

		endDirection = null;
		endPoint = null;
		points.removeAllPoints();
	}

	private void processPoints(Point startPoint, Vector direction,
			Rectangle parallelObs) {
		if (direction == UP)
			processUp(startPoint, parallelObs);
		else if (direction == DOWN)
			processDown(startPoint, parallelObs);
		else if (direction == LEFT)
			processLeft(startPoint, parallelObs);
		else
			processRight(startPoint, parallelObs);
	}

	private void processRight(Point startPoint, Rectangle parallelObs) {
		Point newStartPoint = new Point(startPoint);

		int min_xd = 0;
		Rectangle obstracle = null;
		@SuppressWarnings("unchecked")
		List<IFigure> list = content.getChildren();
		for (IFigure f : list) {
			Rectangle fr = f.getBounds();
			if (containPoint(fr, endPoint) || containPoint(fr, startPoint)
					|| fr.x > endPoint.x)
				continue;

			int xd = fr.x - startPoint.x;
			if (xd > 0 && fr.y <= startPoint.y
					&& (fr.y + fr.height) >= startPoint.y) {
				if (xd < min_xd || min_xd == 0) {
					min_xd = xd;
					obstracle = fr;
				}
			}

		}
		if (min_xd == 0) { // no obstacles
			if (parallelObs == null) {
				if (newStartPoint.y == endPoint.y)
					return;

			} else {
				if (newStartPoint.x < parallelObs.x)
					newStartPoint.x -= (parallelObs.x - newStartPoint.x) / 2;
			}

			if (newStartPoint.x < endPoint.x) {
				if (isVertical(endDirection, RIGHT))
					newStartPoint.x = endPoint.x;
				else {
					if (endDirection.equals(RIGHT)) {
						newStartPoint.x = endPoint.x + space;
					} else
						newStartPoint.x += (endPoint.x - newStartPoint.x) / 2;
				}
			}
		} else {
			int x = newStartPoint.x + min_xd - space;
			if (x < newStartPoint.x)
				x = newStartPoint.x + min_xd / 2;
			newStartPoint.x = x;
		}
		if (parallelObs != null) {
			if (newStartPoint.x >= parallelObs.x
					&& newStartPoint.x <= parallelObs.x + parallelObs.width)
				newStartPoint.x = parallelObs.x + parallelObs.width + space;
		}
		if (!newStartPoint.equals(startPoint))
			points.addPoint(newStartPoint);

		// next
		Vector newDirection = UP;
		if (obstracle == null) {
			if (endPoint.y > newStartPoint.y)
				newDirection = DOWN;
		} else {
			if (endPoint.y > obstracle.y)
				newDirection = DOWN;
		}

		processPoints(newStartPoint, newDirection, obstracle);
	}

	private void processLeft(Point startPoint, Rectangle parallelObs) {
		Point newStartPoint = new Point(startPoint);

		int min_xd = 0;
		Rectangle obstracle = null;
		@SuppressWarnings("unchecked")
		List<IFigure> list = content.getChildren();
		for (IFigure f : list) {
			Rectangle fr = f.getBounds();
			if (containPoint(fr, endPoint) || containPoint(fr, startPoint)
					|| (fr.x + fr.width) <= endPoint.x)
				continue;

			int xd = startPoint.x - fr.x - fr.width;
			if (xd > 0 && fr.y <= startPoint.y
					&& (fr.y + fr.height) >= startPoint.y) {
				if (xd < min_xd || min_xd == 0) {
					min_xd = xd;
					obstracle = fr;
				}
			}
		}
		if (min_xd == 0) { // no obstacles
			// not need bend point
			if (parallelObs == null) {
				if (newStartPoint.y == endPoint.y)
					return;
			} else {
				if (newStartPoint.x > parallelObs.x + parallelObs.width)
					newStartPoint.x -= (newStartPoint.x - parallelObs.x - parallelObs.width) / 2;
			}

			if (newStartPoint.x > endPoint.x) {
				if (isVertical(endDirection, LEFT))
					newStartPoint.x = endPoint.x;
				else {
					if (endDirection.equals(LEFT)) {
						newStartPoint.x = endPoint.x - space;
					} else
						newStartPoint.x += (newStartPoint.x - endPoint.x) / 2;
				}
			}
		} else {
			int x = newStartPoint.x + min_xd - space;
			if (x < newStartPoint.x)
				x = newStartPoint.x + min_xd / 2;
			newStartPoint.x = x;
		}
		if (parallelObs != null) {
			if (newStartPoint.x >= parallelObs.x
					&& newStartPoint.x <= (parallelObs.x + parallelObs.width)) {
				newStartPoint.x = parallelObs.x - space;
			}
		}
		if (!newStartPoint.equals(startPoint))
			points.addPoint(newStartPoint);

		// next row point
		Vector newDirection = UP;
		if (obstracle == null) {
			if (endPoint.y > newStartPoint.y)
				newDirection = DOWN;
		} else {
			if (endPoint.y >= obstracle.y)
				newDirection = DOWN;
		}

		processPoints(newStartPoint, newDirection, obstracle);
	}

	private void processDown(Point startPoint, Rectangle parallelObs) {
		Point newStartPoint = new Point(startPoint);

		int min_yd = 0;
		Rectangle obstracle = null;
		@SuppressWarnings("unchecked")
		List<IFigure> list = content.getChildren();
		for (IFigure f : list) {
			Rectangle fr = f.getBounds();
			// logger.debug(fr.width);
			if (containPoint(fr, endPoint) || containPoint(fr, startPoint)
					|| fr.y > endPoint.y)
				continue;

			int yd = fr.y - startPoint.y;
			if (yd > 0 && fr.x <= startPoint.x
					&& (fr.x + fr.width) >= startPoint.x) {
				if (yd < min_yd || min_yd == 0) {
					min_yd = yd;
					obstracle = fr;
				}
			}
		}
		if (min_yd == 0) { // no obstacles
			// not need bend point
			if (parallelObs == null) {
				if (newStartPoint.x == endPoint.x)
					return;
			} else {
				if (parallelObs.y > startPoint.y)
					newStartPoint.y += (parallelObs.y - startPoint.y) / 2;
			}
			if (newStartPoint.y < endPoint.y) {
				if (isVertical(endDirection, DOWN)) {
					newStartPoint.y = endPoint.y; // TODO avoid itself
				} else {
					if (endDirection.equals(DOWN))
						newStartPoint.y = startPoint.y + space;
					else
						newStartPoint.y += (endPoint.y - newStartPoint.y) / 2;
				}
			}
		} else {
			int y = newStartPoint.y + min_yd - space;
			if (y < newStartPoint.y)
				y = newStartPoint.y + min_yd / 2;
			newStartPoint.y = y;
		}

		if (parallelObs != null) {
			if (newStartPoint.y > parallelObs.y
					&& newStartPoint.y < parallelObs.y + parallelObs.height)
				newStartPoint.y = parallelObs.y + parallelObs.height + space;
		}
		if (!newStartPoint.equals(startPoint))
			points.addPoint(newStartPoint);
		// logger.debug("point:" + newStartPoint.toString());

		// next row point
		Vector newDirection = LEFT;
		if (obstracle == null) {
			if (endPoint.x > newStartPoint.x)
				newDirection = RIGHT;
		} else {
			if (endPoint.x > (obstracle.x + obstracle.width))
				newDirection = RIGHT;
		}

		processPoints(newStartPoint, newDirection, obstracle);
	}

	boolean isVertical(Vector v1, Vector v2) {
		double val = v1.x * v2.x + v1.y * v2.y;
		if (val == 0)
			return true;
		return false;
	}

	boolean containPoint(Rectangle r, Point p) {
		return p.x >= r.x && p.x <= r.x + r.width && p.y >= r.y
				&& p.y <= r.y + r.height;
	}

	private void processUp(Point startPoint, Rectangle parallelObs) {
		Point newStartPoint = new Point(startPoint);

		int min_yd = 0;
		Rectangle obstracle = null;
		@SuppressWarnings("unchecked")
		List<IFigure> list = content.getChildren();
		for (IFigure f : list) {
			Rectangle fr = f.getBounds();
			if (containPoint(fr, endPoint) || containPoint(fr, startPoint)
					|| (fr.y + fr.height) <= endPoint.y)
				continue;

			int yd = startPoint.y - fr.y - fr.height;
			if (yd > 0 && fr.x <= startPoint.x
					&& (fr.x + fr.width) >= startPoint.x) {
				if (yd < min_yd || min_yd == 0) {
					min_yd = yd;
					obstracle = fr;
				}
			}
		}
		if (min_yd == 0) { // no obstacles
			// not need bend point
			if (parallelObs == null) {
				if (newStartPoint.x == endPoint.x)
					return;
			} else {
				if (newStartPoint.y > parallelObs.y + parallelObs.height)
					newStartPoint.y -= (newStartPoint.y - parallelObs.y - parallelObs.height) / 2;
			}
			if (newStartPoint.y > endPoint.y) {
				if (isVertical(endDirection, UP))
					newStartPoint.y = endPoint.y;
				else {
					if (endDirection.equals(UP)) {
						newStartPoint.y = endPoint.y - space;
					} else
						newStartPoint.y -= (newStartPoint.y - endPoint.y) / 2;
				}
			}
		} else {
			int y = newStartPoint.y - min_yd + space;
			if (y > newStartPoint.y)
				y = newStartPoint.y - min_yd / 2;
			newStartPoint.y = y;
		}
		if (parallelObs != null) {
			if (newStartPoint.y >= parallelObs.y
					&& newStartPoint.y <= parallelObs.y + parallelObs.height)
				newStartPoint.y = parallelObs.y - space;
		}
		if (!newStartPoint.equals(startPoint))
			points.addPoint(newStartPoint);

		// next row point
		Vector newDirection = LEFT;
		if (obstracle == null) {
			if (endPoint.x > newStartPoint.x)
				newDirection = RIGHT;
		} else {
			if (endPoint.x >= obstracle.x)
				newDirection = RIGHT;
		}

		processPoints(newStartPoint, newDirection, obstracle);
	}

	protected Vector getDirection(Rectangle r, Point p) {
		int i, distance = Math.abs(r.x - p.x);
		Vector direction;

		direction = LEFT;

		i = Math.abs(r.y - p.y);
		if (i <= distance) {
			distance = i;
			direction = UP;
		}

		i = Math.abs(r.bottom() - p.y);
		if (i <= distance) {
			distance = i;
			direction = DOWN;
		}

		i = Math.abs(r.right() - p.x);
		if (i < distance) {
			distance = i;
			direction = RIGHT;
		}

		return direction;
	}

	protected Vector getStartDirection(Connection conn) {
		ConnectionAnchor anchor = conn.getSourceAnchor();
		Point p = getStartPoint(conn);
		Rectangle rect;
		if (anchor.getOwner() == null)
			rect = new Rectangle(p.x - 1, p.y - 1, 2, 2);
		else {
			rect = conn.getSourceAnchor().getOwner().getBounds().getCopy();
			conn.getSourceAnchor().getOwner().translateToAbsolute(rect);
		}
		return getDirection(rect, p);
	}

	protected Vector getEndDirection(Connection conn) {
		ConnectionAnchor anchor = conn.getTargetAnchor();
		Point p = getEndPoint(conn);
		Rectangle rect;
		if (anchor.getOwner() == null)
			rect = new Rectangle(p.x - 1, p.y - 1, 2, 2);
		else {
			rect = conn.getTargetAnchor().getOwner().getBounds().getCopy();
			conn.getTargetAnchor().getOwner().translateToAbsolute(rect);
		}
		return getDirection(rect, p);
	}

	// private IFigure getSourceOwner(Connection conn) {
	// return conn.getSourceAnchor().getOwner();
	// }
	//
	// private IFigure getTargetOwner(Connection conn) {
	// return conn.getTargetAnchor().getOwner();
	// }

	// private void testChildren(Connection conn) {
	// List<Figure> list = content.getChildren();
	// for (Figure f : list) {
	// Rectangle r = f.getBounds();
	// // conn.translateToRelative(r.getCopy());
	// Rectangle rc = r.getCopy();
	// content.translateToRelative(rc.getCopy());
	// logger.debug(rc.toString());
	// // if(f instanceof HandleBounds){
	// //
	// // }
	// }
	// logger.debug("\n");
	// }

}