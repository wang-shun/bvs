package com.bizvisionsoft.bruiengine.assembly;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.bson.Document;
import org.eclipse.core.runtime.Assert;
import org.eclipse.draw2d.AbsoluteBendpoint;
import org.eclipse.draw2d.Bendpoint;
import org.eclipse.draw2d.BendpointConnectionRouter;
import org.eclipse.draw2d.ColorConstants;
import org.eclipse.draw2d.Figure;
import org.eclipse.draw2d.FigureCanvas;
import org.eclipse.draw2d.Label;
import org.eclipse.draw2d.Panel;
import org.eclipse.draw2d.PolygonDecoration;
import org.eclipse.draw2d.PolylineConnection;
import org.eclipse.draw2d.PositionConstants;
import org.eclipse.draw2d.XYLayout;
import org.eclipse.draw2d.geometry.Dimension;
import org.eclipse.draw2d.geometry.Insets;
import org.eclipse.draw2d.geometry.Point;
import org.eclipse.draw2d.graph.DirectedGraph;
import org.eclipse.draw2d.graph.DirectedGraphLayout;
import org.eclipse.draw2d.graph.Edge;
import org.eclipse.draw2d.graph.EdgeList;
import org.eclipse.draw2d.graph.Node;
import org.eclipse.draw2d.graph.NodeList;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.ScrolledComposite;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;

import com.bizivisionsoft.widgets.util.Layer;
import com.bizvisionsoft.annotations.AUtil;
import com.bizvisionsoft.annotations.ui.common.CreateUI;
import com.bizvisionsoft.annotations.ui.common.EdgeProperty;
import com.bizvisionsoft.annotations.ui.common.Init;
import com.bizvisionsoft.annotations.ui.common.Inject;
import com.bizvisionsoft.annotations.ui.common.NodeProperty;
import com.bizvisionsoft.bruicommons.model.Assembly;
import com.bizvisionsoft.bruiengine.BruiDataSetEngine;
import com.bizvisionsoft.bruiengine.service.BruiAssemblyContext;
import com.bizvisionsoft.bruiengine.service.IBruiService;
import com.bizvisionsoft.bruiengine.util.BruiColors;
import com.bizvisionsoft.bruiengine.util.BruiColors.BruiColor;
import com.mongodb.BasicDBObject;

public class FlowPart {

	@Inject
	private IBruiService bruiService;

	private Assembly config;

	protected BruiDataSetEngine dataSetEngine;

	@Inject
	private BruiAssemblyContext context;

	private BasicDBObject filter;

	private FigureCanvas canvas;

	private ScrolledComposite sc;

	private DirectedGraph graph;

	public FlowPart() {
	}

	public FlowPart(Assembly gridConfig) {
		setConfig(gridConfig);
	}

	protected void setConfig(Assembly config) {
		this.config = config;
	}

	protected void setBruiService(IBruiService bruiService) {
		this.bruiService = bruiService;
	}

	protected void setContext(BruiAssemblyContext context) {
		this.context = context;
	}

	@Init
	protected void init() {
		// 注册数据集引擎
		dataSetEngine = BruiDataSetEngine.create(config, bruiService, context);

	}

	public BruiDataSetEngine getDataSetEngine() {
		return dataSetEngine;
	}

	@CreateUI
	public void createUI(Composite parent) {
		Composite panel;
		if (config.isHasTitlebar()) {
			panel = createSticker(parent);
		} else {
			panel = parent;
		}
		panel.setLayout(new FillLayout());
		createControl(panel);
		setViewerInput();
		Dimension size = graph.getLayoutSize();
		Insets insets = graph.getMargin();
		sc.setMinSize(size.width + insets.getWidth() , size.height + insets.getHeight());
	}

	private Composite createSticker(Composite parent) {
		StickerPart sticker = new StickerPart(config);
		sticker.context = context;
		sticker.service = bruiService;
		sticker.createUI(parent);
		return sticker.content;
	}

	protected FigureCanvas createControl(Composite parent) {
		sc = new ScrolledComposite(parent, SWT.H_SCROLL | SWT.V_SCROLL);
		canvas = new FigureCanvas(sc);
		canvas.setScrollBarVisibility(FigureCanvas.NEVER);
		sc.setExpandHorizontal(true);
		sc.setExpandVertical(true);
		sc.setContent(canvas);
		return canvas;
	}

	public void setViewerInput() {
		if (dataSetEngine != null) {
			List<?> nodes = null;
			List<?> links = null;
			try {
				Object object = dataSetEngine.query(filter, context, "node");
				if (object == null || !(object instanceof List<?>)) {
					throw new Exception("数据源node注解需返回List。");
				}
				nodes = (List<?>) object;

				object = dataSetEngine.query(filter, context, "link");
				if (object == null || !(object instanceof List<?>)) {
					throw new Exception("数据源link注解需返回List。");
				}
				links = (List<?>) object;

			} catch (Exception e) {
				Layer.message(e.getMessage(), Layer.ICON_CANCEL);
			}
			setViewerInput(nodes, links);
		} else {
			setViewerInput(null, null);
		}
	}

	public void setViewerInput(List<?> nodeData, List<?> linkData) {
		NodeList nodes = new NodeList();
		EdgeList edges = new EdgeList();

		nodeData.forEach(elem -> nodes.add(createNode(elem)));
		linkData.forEach(elem -> edges.add(createEdge(nodes, elem)));

		graph = new DirectedGraph();
		graph.setDirection(PositionConstants.EAST);
		graph.nodes = nodes;
		graph.edges = edges;

		new DirectedGraphLayout().visit(graph);
		Figure figure = buildGraph(graph);
		canvas.setContents(figure);
	}

	private Edge createEdge(NodeList nodes, Object elem) {
		Object _from = AUtil.readValue(elem, config.getName(), EdgeProperty.src.name(), "");
		Assert.isTrue(_from instanceof String, "src 节点id必须是非空字符串");

		Object _to = AUtil.readValue(elem, config.getName(), EdgeProperty.tgt.name(), "");
		Assert.isTrue(_to instanceof String, "tgt 节点id必须是非空字符串");

		Node from = nodes.stream().filter(n -> n.id.equals(_from)).findFirst().orElse(null);
		Node to = nodes.stream().filter(n -> n.id.equals(_to)).findFirst().orElse(null);
		Assert.isNotNull(from, "无法获得连线的起点");
		Assert.isNotNull(to, "无法获得连线的终点");

		Edge e = new Edge(from, to);

		Object data = AUtil.readValue(elem, config.getName(), EdgeProperty.data.name(), "");
		if (data instanceof Document) {
			e.data = data;
			return e;
		} else {
			Document doc = new Document();
			Arrays.asList(EdgeProperty.values())
					.forEach(np -> doc.put(np.name(), AUtil.readValue(elem, config.getName(), np.name(), null)));
			e.data = doc;
			return e;
		}
	}

	private Node createNode(Object elem) {
		Node node = new Node();
		Object value = AUtil.readValue(elem, config.getName(), NodeProperty.id.name(), null);
		Assert.isTrue(value instanceof String, "节点id必须是非空字符串");
		node.id = (String) value;

		value = AUtil.readValue(elem, config.getName(), NodeProperty.height.name(), "");
		if (value instanceof Integer)
			node.height = ((Integer) value).intValue();
		value = AUtil.readValue(elem, config.getName(), NodeProperty.width.name(), "");
		if (value instanceof Integer)
			node.width = ((Integer) value).intValue();

		Object data = AUtil.readValue(elem, config.getName(), NodeProperty.data.name(), "");
		if (data instanceof Document) {
			node.data = data;
			return node;
		} else {
			Document doc = new Document();
			Arrays.asList(NodeProperty.values())
					.forEach(np -> doc.put(np.name(), AUtil.readValue(elem, config.getName(), np.name(), null)));
			node.data = doc;
			return node;
		}
	}

	public Figure buildGraph(DirectedGraph graph) {
		Figure contents = new Panel();
		contents.setBackgroundColor(ColorConstants.white());
		contents.setLayoutManager(new XYLayout());

		for (int i = 0; i < graph.nodes.size(); i++) {
			Node node = graph.nodes.getNode(i);
			buildNodeFigure(contents, node);
		}

		for (int i = 0; i < graph.edges.size(); i++) {
			Edge edge = graph.edges.getEdge(i);
			buildEdgeFigure(contents, edge);
		}
		return contents;
	}

	private void buildEdgeFigure(Figure contents, Edge edge) {
		PolylineConnection conn = connection(contents, edge);
		conn.setForegroundColor(ColorConstants.gray());
		PolygonDecoration dec = new PolygonDecoration();
		conn.setTargetDecoration(dec);
		conn.setPoints(edge.getPoints());
		contents.add(conn);
	}

	private void buildNodeFigure(Figure contents, Node node) {
		Label label;
		label = new Label();

		Document data = (Document) node.data;

		Color color = Optional.ofNullable(getColor(data.get(NodeProperty.background.name())))
				.orElse(BruiColors.getColor(BruiColor.light_blue_500));
		label.setBackgroundColor(color);

		color = Optional.ofNullable(getColor(data.get(NodeProperty.foreground.name())))
				.orElse(Display.getCurrent().getSystemColor(SWT.COLOR_WHITE));
		label.setForegroundColor(color);

		label.setOpaque(true);

		label.setText((String) data.get(NodeProperty.text.name()));

		label.setLocation(new Point(node.x, node.y));
		label.setSize(new Dimension(node.width, node.height));
		contents.add(label);
	}

	private Color getColor(Object bgColor) {
		if (bgColor instanceof String) {
			Color color = BruiColors.getColorFromRGB((String) bgColor);
			return color;
		}
		return null;
	}

	private PolylineConnection connection(Figure contents, Edge e) {
		PolylineConnection conn = new PolylineConnection();
		conn.setConnectionRouter(new BendpointConnectionRouter());
		List<Bendpoint> bends = new ArrayList<Bendpoint>();
		NodeList nodes = e.vNodes;
		if (nodes != null) {
			for (int i = 0; i < nodes.size(); i++) {
				Node n = nodes.getNode(i);
				int x = n.x;
				int y = n.y;
				bends.add(new AbsoluteBendpoint(x, y));
				bends.add(new AbsoluteBendpoint(x, y + n.height));
			}
		}
		conn.setRoutingConstraint(bends);
		return conn;
	}

}
