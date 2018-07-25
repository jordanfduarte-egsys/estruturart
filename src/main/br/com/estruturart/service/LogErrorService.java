package br.com.estruturart.service;


import java.io.PrintWriter;
import java.io.StringWriter;
import java.sql.SQLException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import br.com.estruturart.model.TbUsuario;
import br.com.estruturart.persistency.LogError;

public class LogErrorService
{
    private HttpServletRequest request;
    private HttpServletResponse response;

    public LogErrorService(HttpServletRequest request, HttpServletResponse response)
    {
        this.request = request;
        this.response = response;
    }

    public void createLog(Exception e) throws SQLException
    {
        StringWriter sw = new StringWriter();
        PrintWriter pw = new PrintWriter(sw);
        e.printStackTrace(pw);
        String sStackTrace = sw.toString();
        int usuarioId = 0;

        TbUsuario usuario = null;
        if (this.request.getSession().getAttribute("usuario") instanceof TbUsuario) {
            usuario = (TbUsuario) this.request.getSession().getAttribute("usuario");
            usuarioId = usuario.getId();
        }

        String message = String.format(
            "Exception: %s \n Trace: %s \n Url: %s \n Auth: %s", e.getMessage(), sStackTrace,
            this.request.getRequestURI().toString(), usuarioId
        );

        LogError modelLogError = new LogError();
        modelLogError.insert(message);
    }
}
