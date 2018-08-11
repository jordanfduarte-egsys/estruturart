package br.com.estruturart.utility;

import java.util.ArrayList;
import java.util.List;

public class ParamRequestManager
{
    private List<RouteParam> routeParams;

    public ParamRequestManager()
    {
        this.routeParams = new ArrayList<RouteParam>();
    }

    public boolean hasParam(String name)
    {
        List<RouteParam> filter = routeParams;
        boolean isRouteParam = false;
        for (RouteParam r : filter) {
            if (r.getName().equals(name) && !r.getValue().equals("")) {
                isRouteParam = true;
                break;
            }
        }

        return isRouteParam;
    }

    public String getParamOr(String name, String or)
    {
        List<RouteParam> filter = routeParams;
        String search = "";
        for (RouteParam r : filter) {
            if (r.getName().equals(name) && !r.getValue().equals("")) {
                search = r.getValue();
                break;
            }
        }

        return search.equals("") ? or : search;
    }

    public String getParam(String name)
    {
        List<RouteParam> filter = routeParams;
        String search = "";
        for (RouteParam r : filter) {
            if (r.getName().equals(name)) {
                search = r.getValue();
                break;
            }
        }

        return search;
    }

    public void add(RouteParam r)
    {
        this.routeParams.add(r);
    }

    public void clear()
    {
        this.routeParams.clear();
    }

    public List<RouteParam> getAll()
    {
        return this.routeParams;
    }
}
