package br.com.estruturart.service;

import java.io.*;
import java.util.*;
import javax.servlet.*;
import javax.servlet.http.*;
import javax.mail.*;
import javax.mail.internet.*;
import javax.activation.*;
import br.com.estruturart.service.LogErrorService;

public class SendEmailService
{
    private HttpServletRequest request;
    private HttpServletResponse response;
    private String subject;
    private String html;
    private String to;
    private String from;
    private String host;
    private LogErrorService logService;

    public void SendMailService(HttpServletRequest request, HttpServletResponse response)
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

        // Get system properties
        Properties properties = System.getProperties();

        // Setup mail server
        properties.setProperty("mail.smtp.host", host);

        // Get the default Session object.
        Session session = Session.getDefaultInstance(properties);

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
}