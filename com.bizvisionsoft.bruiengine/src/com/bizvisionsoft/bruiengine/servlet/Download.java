package com.bizvisionsoft.bruiengine.servlet;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.URLDecoder;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.bizvisionsoft.service.tools.FileTools;

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
//		path = BsonTools.decompress(id);
		String sid = request.getSession().getId();

		File file = new File(path);
		if (!file.isFile()) {
			response.sendError(HttpServletResponse.SC_NOT_FOUND, "�������Դ�������ڡ�");
			return;
		}
		String sessionId = file.getParentFile().getName().split("_")[2].toUpperCase();
		if (!sid.toUpperCase().equals(sessionId)) {
			response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "��ͼ����δ����ɵ���Դ��");
			return;
		}

		response.setHeader("Content-Disposition",
				"attachment; filename=\"" + new String(file.getName().getBytes(), "ISO8859-1") + "\"");
		response.setContentType(FileTools.getContentType(file, null));
		FileTools.copyStream(new FileInputStream(file), response.getOutputStream(), true);
	}

}
