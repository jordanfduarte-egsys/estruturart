package br.com.estruturart.utility;

import java.util.ArrayList;
import javax.servlet.http.HttpSession;

public class FlashMessenger
{
    private String type;
    private boolean isShow = false;
    private HttpSession session;

    public static String SUCCESS = "success";
    public static String ERROR = "danger";
    public static String INFO = "primary";

    public FlashMessenger(HttpSession session)
    {
        this.session = session;

        if (this.session.getAttribute("flashMessenger") == null) {
            this.session.setAttribute("flashMessenger", new ArrayList<String>());
        }

        this.type = FlashMessenger.SUCCESS;
    }

    public FlashMessenger add(String message)
    {
        if (message.equals("")) {
            return this;
        }

        ArrayList<String> messagesAux = (ArrayList<String>) this.session.getAttribute("flashMessenger");
        messagesAux.add(message);
        return this;
    }

    /**
     * @return the type
     */
    public String getType()
    {
        return type;
    }

    /**
     * @param type
     *            the type to set
     */
    public FlashMessenger setType(String type)
    {
        this.type = type;
        return this;
    }

    public String getMessages()
    {
        int count = 0;

        StringBuffer buffer = new StringBuffer();

        ArrayList<String> messagesAux = (ArrayList<String>) this.session.getAttribute("flashMessenger");
        for (String message : messagesAux) {
            if (count > 0) {
                buffer.append("<br/>" + message);
            } else {
                buffer.append(message);
            }
            count++;
        }

        System.out.println("MESSAGE: " + buffer.toString());

        this.isShow = true;
        return buffer.toString();
    }

    public void clear()
    {
        this.session.setAttribute("flashMessenger", new ArrayList<String>());
        this.isShow = false;
    }

    public boolean isMessages()
    {
        ArrayList<String> messagesAux = (ArrayList<String>) this.session.getAttribute("flashMessenger");
        if (messagesAux.size() > 0)
            return true;
        else
            return false;
    }

    /**
     * @return the isShow
     */
    public boolean isShow()
    {
        return isShow;
    }

    /**
     * @param isShow
     *            the isShow to set
     */
    public void setShow(boolean isShow)
    {
        this.isShow = isShow;
    }
}
