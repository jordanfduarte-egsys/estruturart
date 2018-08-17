package br.com.estruturart.service;

import java.io.*;
import java.util.*;
import javax.servlet.*;
import javax.servlet.http.*;
import javax.mail.*;
import javax.mail.internet.*;
import javax.activation.*;
import br.com.estruturart.service.LogErrorService;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class SendEmailService extends Authenticator
{
    private HttpServletRequest request;
    private HttpServletResponse response;
    private String subject;
    private String html;
    private String to;
    private String from;
    private String host;
    private final String usuario;
    private final String senha;
    private LogErrorService logService;

    public SendEmailService(HttpServletRequest request, HttpServletResponse response)
    {
        this.request = request;
        this.response = response;
        this.logService = new LogErrorService(request, response);
    }

    public void send() throws ServletException, IOException, java.sql.SQLException
    {
        // Recipient's email ID needs to be mentioned.
        String to = this.to;

        // Sender's email ID needs to be mentioned
        String from = this.from;

        // Assuming you are sending email from localhost
        String host = this.host;
        
        // Porta Gmail
        String port = "465";

        // Get system properties
        Properties props = new Properties();
        
        /** Parâmetros de conexão com servidor Gmail */
        props.put("mail.smtp.host", host);
        props.put("mail.smtp.socketFactory.port", port);
        props.put("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.port", port);

        // @see https://www.devmedia.com.br/enviando-email-com-javamail-utilizando-gmail/18034
        Session session = Session.getDefaultInstance(props, this);
        
        // Ativa Debug para sessão
        session.setDebug(true);
        try {
            // Create a default MimeMessage object.
            MimeMessage message = new MimeMessage(session);

            // Set From: header field of the header.
            message.setFrom(new InternetAddress(from));

            // Set To: header field of the header.
            message.addRecipient(Message.RecipientType.TO, new InternetAddress(to));
            // Set Subject: header field
            message.setSubject(this.subject);

            // Send the actual HTML message, as big as you like
            message.setContent(this.html, "text/plain");

            // Send message
            Transport.send(message);
        } catch (MessagingException mex) {
           this.logService.createLog(mex);
        } catch (Exception e) {
            this.logService.createLog(e);
        }
    }

    public String getSubject()
    {
        return this.subject;
    }

    public void setSubject(String subject)
    {
        this.subject = subject;
    }

    public String getHtml()
    {
        return this.html;
    }

    public void setHtml(String html)
    {
        this.html = html;
    }

    public String getTo()
    {
        return this.to;
    }

    public void setTo(String to)
    {
        this.to = to;
    }

    public String getFrom()
    {
        return this.from;
    }

    public void setFrom(String from)
    {
        this.from = from;
    }

    public String getHost()
    {
        return this.host;
    }

    public void setHost(String host)
    {
        this.host = host;
    }
    
    public void setUsuario(String usuario)
    {
        this.usuario = usuario;
    }
    
    public void setSenha(String senha)
    {
        this.senha = senha;
    }
    
    @Override
    protected PasswordAuthentication getPasswordAuthentication()
    {
         return new PasswordAuthentication(this.usuario, this.senha);
    }
}
