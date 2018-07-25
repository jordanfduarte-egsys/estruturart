package br.com.estruturart.controller;

import java.io.File;
import java.io.FileInputStream;
import java.io.OutputStream;

import javax.servlet.ServletContext;
import javax.servlet.annotation.WebServlet;

@WebServlet(name = "image", urlPatterns = { "/image" })
public class ImageController extends AbstractServlet {
    private static final long serialVersionUID = -4214231788197597839L;

    public void indexAction() throws Exception {
        setNoRender(true);
        String path = this.getRequest().getParameter("path");
        ServletContext cntx = this.getRequest().getServletContext();
        String sourceFilder = getServletContext().getInitParameter("folderUpload");

        String filename = sourceFilder + path.replace("/files", "");
        String mime = cntx.getMimeType(filename);
        System.out.println("MIME " + mime);
        if (mime == null) {
            filename = cntx.getRealPath("/files/sem-foto.jpg");
            mime = cntx.getMimeType(filename);
        }
        System.out.println("LINK" + filename);
        getResponse().setContentType(mime);
        File file = new File(filename);
        getResponse().setContentLength((int) file.length());

        FileInputStream in = new FileInputStream(file);
        OutputStream out = getResponse().getOutputStream();

        byte[] buf = new byte[1024];
        int count = 0;
        while ((count = in.read(buf)) >= 0) {
            out.write(buf, 0, count);
        }

        out.close();
        in.close();
    }
}
