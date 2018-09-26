package br.com.estruturart.utility;

import java.util.List;

public class JsonModel implements IJsonModel
{
    private boolean status = true;
    private String message;
    private Integer id;
    private List<?> list;
    private Object object;

    public boolean isStatus()
    {
        return status;
    }

    public void setStatus(boolean status)
    {
        this.status = status;
    }

    public String getMessage()
    {
        return message;
    }

    public void setMessage(String message)
    {
        this.message = message;
    }

    public List<?> getList()
    {
        return list;
    }

    public void setList(List<?> list)
    {
        this.list = list;
    }

    public Object getObject()
    {
        return object;
    }

    public void setObject(Object object)
    {
        this.object = object;
    }

    public void setId(Integer id)
    {
        this.id = id;
    }

    public Integer getId()
    {
        return this.id;
    }
}
