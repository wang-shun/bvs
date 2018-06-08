package com.bizvisionsoft.bruiengine.servlet;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.URLDecoder;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.bizvisionsoft.bruiengine.util.Util;

public class Download extends HttpServlet {

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		doPost(req, resp);
	}

	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		String id = request.getParameter("id");
		String path = URLDecoder.decode(id, "utf-8");
//		path = Util.decompress(id);
		String sid = request.getSession().getId();

		File file = new File(path);
		if (!file.isFile()) {
			response.sendError(HttpServletResponse.SC_NOT_FOUND, "请求的资源并不存在。");
			return;
		}
		String sessionId = file.getParentFile().getName().split("_")[2].toUpperCase();
		if (!sid.toUpperCase().equals(sessionId)) {
			response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "试图访问未经许可的资源。");
			return;
		}

		response.setHeader("Content-Disposition",
				"attachment; filename=\"" + new String(file.getName().getBytes(), "ISO8859-1") + "\"");
		response.setContentType(Util.getContentType(file, null));
		Util.copyStream(new FileInputStream(file), response.getOutputStream(), true);
	}

}
