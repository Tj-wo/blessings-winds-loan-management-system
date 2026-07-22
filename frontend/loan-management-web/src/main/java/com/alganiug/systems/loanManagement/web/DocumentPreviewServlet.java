package com.alganiug.systems.loanManagement.web;

import com.alganiug.systems.loanManagement.core.services.employee.DocumentService;
import com.alganiug.systems.loanManagement.models.employee.Document;
import com.alganiug.systems.loanManagement.models.security.User;
import org.springframework.web.context.support.WebApplicationContextUtils;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.Optional;
import java.util.UUID;

public class DocumentPreviewServlet extends HttpServlet {
    private DocumentService documentService;

    @Override public void init() throws ServletException {
        documentService = WebApplicationContextUtils.getRequiredWebApplicationContext(getServletContext())
                .getBean(DocumentService.class);
    }

    @Override protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        User viewer = request.getSession(false) == null ? null
                : (User) request.getSession(false).getAttribute("loggedInUser");
        String path = request.getPathInfo();
        if (viewer == null || path == null || path.length() < 2) {
            response.sendError(HttpServletResponse.SC_UNAUTHORIZED); return;
        }
        final UUID id;
        try { id = UUID.fromString(path.substring(1)); }
        catch (IllegalArgumentException exception) { response.sendError(HttpServletResponse.SC_NOT_FOUND); return; }
        Optional<Document> result = documentService.getDocumentForPreview(id, viewer);
        if (!result.isPresent()) { response.sendError(HttpServletResponse.SC_FORBIDDEN); return; }
        Document document = result.get();
        response.setContentType(document.getMimeType());
        response.setContentLengthLong(document.getContentSize());
        response.setHeader("X-Content-Type-Options", "nosniff");
        String filename = URLEncoder.encode(document.getOriginalFilename(), StandardCharsets.UTF_8.name()).replace("+", "%20");
        response.setHeader("Content-Disposition", "inline; filename*=UTF-8''" + filename);
        response.getOutputStream().write(document.getFileContent());
    }
}