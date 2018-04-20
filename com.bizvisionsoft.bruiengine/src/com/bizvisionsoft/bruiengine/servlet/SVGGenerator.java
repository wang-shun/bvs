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
		String text = request.getParameter("text");
		String color = request.getParameter("color");

		String svg =
				"<svg xmlns=\"http://www.w3.org/2000/svg\" xmlns:xlink=\"http://www.w3.org/1999/xlink\" x=\"0px\" y=\"0px\"\r\n"
						+ "	 width=\"100px\" height=\"100px\" viewBox=\"0 0 100 100\" enable-background=\"new 0 0 100 100\" xml:space=\"preserve\">\r\n"
						+ "<text transform=\"matrix(1.0463 0 0 1 18.3418 62.9326)\" fill=\"#" + color
						+ "\" font-family=\"'TimesNewRomanPSMT'\" font-size=\"41.8704\">" + text + "</text>\r\n"
						+ "</svg>";

		response.setContentType("image/svg");
		ServletOutputStream os = response.getOutputStream();
		os.write(svg.getBytes());
		os.close();
	}

}
