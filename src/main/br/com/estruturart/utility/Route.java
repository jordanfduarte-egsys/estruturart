package br.com.estruturart.utility;

public class Route
{
    private String controller;
    private String action;
    private String actionName;

    public String getController()
    {
        return controller;
    }
    public void setController(String controller)
    {
        this.controller = controller;
    }
    public String getAction()
    {
        return action;
    }
    public void setAction(String action)
    {
        this.action = action;
        setActionName(action.replaceAll("Action", ""));
    }
    public String getActionName()
    {
        return this.actionName;
    }
    public void setActionName(String actionName)
    {
        this.actionName = actionName;
    }
}
