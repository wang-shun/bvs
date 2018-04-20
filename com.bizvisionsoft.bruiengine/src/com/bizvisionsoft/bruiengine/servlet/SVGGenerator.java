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
		String svg ="<?xml version=\"1.0\" encoding=\"UTF-8\"?>"+
				"<svg version=\"1.1\" id=\"layer_1\" xmlns=\"http://www.w3.org/2000/svg\" xmlns:xlink=\"http://www.w3.org/1999/xlink\" x=\"0px\" y=\"0px\" width=\"100px\" height=\"100px\" viewBox=\"0 0 100 100\" enable-background=\"new 0 0 100 100\" xml:space=\"preserve\">\r\n" + 
				"<text x=\"50\" y=\"55\"  text-anchor=\"middle\" dominant-baseline=\"middle\" fill=\"#"+color+"\" font-size=\"40\">"+text+"</text>\r\n" + 
				"</svg>";
		byte[] bs = svg.getBytes("utf-8");
		response.setContentLength(bs.length);  
		response.setContentType("image/svg+xml;charset=UTF-8");  
        response.addHeader("Accept-Ranges","bytes"); 
		ServletOutputStream os = response.getOutputStream();
		os.write(bs);
		os.close();
	}

}
