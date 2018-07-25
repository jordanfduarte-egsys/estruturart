package br.com.estruturart.utility;

public class RouteParam
{
    private String name = "";
    private String value = "";

    public RouteParam(String nome, String value)
    {
        this.name = nome;
        this.value = value;
    }

    public RouteParam()
    {

    }

    /**
     * @return the name
     */
    public String getName()
    {
        return name;
    }
    /**
     * @param name the name to set
     */
    public void setName(String name)
    {
        this.name = name;
    }
    /**
     * @return the value
     */
    public String getValue()
    {
        return value;
    }
    /**
     * @param value the value to set
     */
    public void setValue(String value)
    {
        this.value = value;
    }
}
