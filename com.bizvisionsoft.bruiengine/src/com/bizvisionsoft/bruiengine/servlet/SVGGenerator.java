package com.bizvisionsoft.bruiengine.servlet;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class SVGGenerator extends HttpServlet {

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		doPost(req, resp);
	}

	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		String type = request.getParameter("type");
		String svg = null;
		if ("progress".equals(type)) {// 生成环形进度
			svg = generateCicleProgress(request);
		} else {// 生成圆圈文字在中间的SVG图片
			svg = generateColorfulCicleBGText(request);
		}
		byte[] bs = svg.getBytes("utf-8");
		response.setContentLength(bs.length);
		response.setContentType("image/svg+xml;charset=UTF-8");
		response.addHeader("Accept-Ranges", "bytes");
		ServletOutputStream os = response.getOutputStream();
		os.write(bs);
		os.close();
	}

	private String generateCicleProgress(HttpServletRequest request) {
		String _percent = request.getParameter("percent");
		float percent = Float.parseFloat(_percent);
		double perimeter = Math.PI * 2 * 170;
		String strokeDasharray = perimeter * percent + " " + perimeter * (1 - percent);
		String bgColor = request.getParameter("bgColor");
		String fgColor = request.getParameter("fgColor");
		StringBuffer sb = new StringBuffer();
		sb.append("<?xml version=\"1.0\" encoding=\"UTF-8\"?>");
		sb.append(
				"<svg version=\"1.1\" id=\"layer_1\" xmlns=\"http://www.w3.org/2000/svg\" xmlns:xlink=\"http://www.w3.org/1999/xlink\" x=\"0px\" y=\"0px\" width=\"440\" height=\"440\" viewBox=\"0 0 440 440\" enable-background=\"new 0 0 440 440\" xml:space=\"preserve\">\r\n");
//		sb.append("<text style=\"text-anchor: middle;fill:black;font-weight:200;font-family:'Roboto',Helvetica,Arial,sans-serif;\" font-size=\"180\" x=\"200\" y=\"280\" width=\"440\" height=\"440\">"
//				+ (int)(100 * percent) + "</text>");
//		sb.append("<text style=\"text-anchor: middle;fill:black;font-weight:200;font-family:'Roboto',Helvetica,Arial,sans-serif;\" font-size=\"80\" x=\"315\" y=\"210\" width=\"440\" height=\"440\">%</text>");

		sb.append("<text style=\"text-anchor: middle;fill:black;font-weight:200;font-family:'Roboto',Helvetica,Arial,sans-serif;\" x=\"220\" y=\"280\" width=\"440\" height=\"440\">"
				+"<tspan font-size=\"180\">"+ (int)(100 * percent) +"</tspan><tspan font-size=\"80\">%</tspan></text>");
		
		sb.append("<circle cx=\"220\" cy=\"220\" r=\"170\" stroke-width=\"40\" stroke=\"#" + bgColor
				+ "\" fill=\"none\"></circle>");
		sb.append("<circle id=\"c1\" cx=\"220\" cy=\"220\" r=\"170\" stroke-linecap=\"round\" stroke-width=\"40\" stroke=\"#" + fgColor
				+ "\" fill=\"none\" transform=\"matrix(0,-1,1,0,0,440)\" stroke-dasharray=\"" + strokeDasharray
				+ "\"></circle>");
		sb.append("</svg>");
		return sb.toString();
	}

	private String generateColorfulCicleBGText(HttpServletRequest request) {
		String text = request.getParameter("text");
		String color = request.getParameter("color");
		String svg = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>"
				+ "<svg version=\"1.1\" id=\"layer_1\" xmlns=\"http://www.w3.org/2000/svg\" xmlns:xlink=\"http://www.w3.org/1999/xlink\" x=\"0px\" y=\"0px\" width=\"100px\" height=\"100px\" viewBox=\"0 0 100 100\" enable-background=\"new 0 0 100 100\" xml:space=\"preserve\">\r\n"
				+ "<text x=\"50\" y=\"55\"  text-anchor=\"middle\" dominant-baseline=\"middle\" fill=\"#" + color
				+ "\" font-size=\"40\">" + text + "</text>\r\n" + "</svg>";
		return svg;
	}

}
