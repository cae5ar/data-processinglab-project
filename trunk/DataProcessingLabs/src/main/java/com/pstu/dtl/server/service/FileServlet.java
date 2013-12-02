package com.pstu.dtl.server.service;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.fileupload.FileItemIterator;
import org.apache.commons.fileupload.FileItemStream;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import com.pstu.dtl.server.dao.FileDao;
import com.pstu.dtl.server.domain.File;

@Transactional
@Controller
public class FileServlet {

    private static final String FSEP = ":fsep;";
    private static final String PSEP = ":psep;";

    @Autowired
    private FileDao fileDao;

    private String XSD_FILE = "/schemas/last_version_template.xsd";
     
    @RequestMapping(value = "/addfile", method = {RequestMethod.POST})
    public ModelAndView handleAddFile(HttpServletRequest req, HttpServletResponse res) throws Exception {
        String result = "";

        if (ServletFileUpload.isMultipartContent(req)) {
            try {
                ServletFileUpload upload = new ServletFileUpload();
                FileItemIterator iterator = upload.getItemIterator(req);
                while (iterator.hasNext()) {
                    FileItemStream item = iterator.next();
                    if (!item.isFormField()) {
                        File file = storeFile(item);
                        fileDao.persist(file);
                        result += file.getId() + PSEP + file.getName() + PSEP + file.getSize() + FSEP;
                    }
                }

                return setResponceMessage(res, HttpServletResponse.SC_OK, result);
            }
            catch (Exception e) {
                return setResponceMessage(res, HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Не удалось сохранить файл");
            }
        }

        return null;
    }

    @RequestMapping(value = "/getfile/*", method = {RequestMethod.GET})
    public ModelAndView handleGetFile(@RequestParam("id") Long id, HttpServletRequest req, HttpServletResponse res)
            throws Exception {

        File fileData = fileDao.findById(id);
        if (fileData == null) // Файл не найден
            return setResponceMessage(res, HttpServletResponse.SC_NOT_FOUND, "Запрашиваемый файл не найден");
        // Файл найден
        res.setContentType(fileData.guessContentType());
        res.setHeader("Content-Disposition", "inline;");
        res.setContentLength(fileData.getContent().length);
        res.getOutputStream().write(fileData.getContent());
        res.setCharacterEncoding("UTF-8");
        return null;
    }
    
    @RequestMapping(value = "/getxsdtemplate/*", method = {RequestMethod.GET})
    public ModelAndView handleGetXSDTemplate(HttpServletRequest req, HttpServletResponse res)
    throws Exception {
        ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
        InputStream input = classLoader.getResourceAsStream(XSD_FILE);
        res.setContentType("text/xml");
        res.setHeader("Content-Disposition", "inline;");
        res.setCharacterEncoding("UTF-8");
        res.setContentLength(input.available());
        ServletOutputStream output = res.getOutputStream();
        IOUtils.copy(input, output);
        return null;
    }

    private File storeFile(FileItemStream item) throws Exception {
        InputStream input = item.openStream();
        ByteArrayOutputStream output = new ByteArrayOutputStream();
        IOUtils.copy(input, output);
        File file = new File();
        file.setName(getOnlyName(item.getName()));
        byte[] arr = output.toByteArray();
        file.setSize(new Long(arr.length));
        file.setContent(arr);
        return file;
    }

    private String getOnlyName(String path) {
        try {
            path = new String(path.getBytes(), "UTF-8");
        }
        catch (Exception e) {}
        int i = path.lastIndexOf("\\") + 1;
        return i < 0 ? path : path.substring(i);
    }

    private ModelAndView setResponceMessage(HttpServletResponse res, int statusCode, String text) throws IOException {
        res.setCharacterEncoding("UTF-8");
        res.setContentType("text/HTML");
        res.setStatus(statusCode);
        res.getWriter().print(text);
        return null;
    }
}
