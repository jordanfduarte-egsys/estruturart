package br.com.estruturart.controller;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.InvocationTargetException;
import java.util.Enumeration;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import br.com.estruturart.utility.IJsonModel;

import com.google.gson.Gson;

import br.com.estruturart.model.TbUsuario;
import br.com.estruturart.service.LogErrorService;
import br.com.estruturart.utility.FlashMessenger;
import br.com.estruturart.utility.JsonModel;
import br.com.estruturart.utility.ParamRequestManager;
import br.com.estruturart.utility.Route;
import br.com.estruturart.utility.RouteParam;

public class AbstractServlet extends HttpServlet {

    private static final long serialVersionUID = -6797310465351284411L;

    private HttpServletRequest request;
    private HttpServletResponse response;

    private Route route;
    private ParamRequestManager params;
    private FlashMessenger flashMessenger;
    private LogErrorService logErrorService;
    private boolean noRender = false;

    /**
     * @throws IOException
     * @see HttpServlet#HttpServlet()
     */
    public AbstractServlet() {
        super();
    }

    /**
     * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
     *      response)
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        this.setResponse(response);
        this.setRequest(request);

        this.route = new Route();
        this.params = new ParamRequestManager();
        setLogErrorService(new LogErrorService(request, response));
        setFlashMessenger(new FlashMessenger(this.getSession()));

        this.getRequest().setCharacterEncoding("UTF-8");
        this.getResponse().setContentType("text/html;charset=UTF-8");

        String[] methodParam = request.getServletPath().split("/");
        String method = "index";

        System.out.println("+====================================");
        for (String m : methodParam) {
            System.out.println("M: " + m);
        }
        System.out.println("+====================================");

        if (methodParam.length > 2) {
            method = methodParam[2];

            String uri = request.getRequestURI().toString().replace(getServletContext().getInitParameter("source"), "");

            RouteParam routeParam = new RouteParam();
            boolean isKey = true;

            for (String param : uri.split("/")) {
                System.out.println("P: " + param);
                System.out.println("Completa: " + uri);

                if (isKey) {
                    routeParam.setName(param);
                    isKey = false;
                } else {
                    routeParam.setValue(param);
                    this.params.add(routeParam);
                    routeParam = new RouteParam();
                    isKey = true;
                }

            }
        }

        method = camelcasify(method);
        method = method + "Action";

        try {
            this.route.setAction(method);
            this.route.setController(this.getClass().getSimpleName().toLowerCase().replace("controller", ""));
            System.out.println("ABSTRACT CHAMANDO ACTION: " + method + "USUARIO LOGADO: "
                    + (this.getSession().getAttribute("usuario") instanceof TbUsuario));
            System.out.println("COntroller: " + this.route.getController());

            // @todo remover
            if ((this.getSession().getAttribute("usuario") instanceof TbUsuario) == false
                    && !(method.equals("indexAction") && this.route.getController().toString().equals("auth"))) {
                TbUsuario u = new TbUsuario();
                u.setId(1);
                u.setCodigo("DKSI");
                u.setCpfCnpj("08142781913");
                u.setEmail("jordan.duarte.pr@gmail.com");
                u.setNome("Jordan");
                u.setPerfilId(1);
                u.setRgIncricaoEstadual("dkskdksd");
                u.setSenha("kdoskodkoskods");
                u.setStatusUsuarioId(1);
                u.setTelefone("82833383");
                u.setTipoPessoa("1");

                this.getSession().setAttribute("usuario", u);
                System.out.println("Atribuiu um usuario MANUALMENTE");
            }

            if (!(this.getSession().getAttribute("usuario") instanceof TbUsuario)
                    && !(method.equals("indexAction") && this.route.getController().toString().equals("auth"))) {
                this.redirect("auth");
            } else if (this.getSession().getAttribute("usuario") instanceof TbUsuario && method.equals("indexAction")
                    && this.route.getController().toString().equals("auth")) {
                this.redirect("home");
            } else {
                System.out.println("ANTES DE CHAMAR");
                this.getClass().getMethod(method).invoke(this, null);
                System.out.println("AJAX? " + isXhtmlHttpRequest());
                if (!this.noRender && !getResponse().isCommitted()) {
                    this.view();
                }

                this.noRender = false;
            }
        } catch (IllegalArgumentException e) {
            error(e);
        } catch (NoSuchMethodException e) {
            error(e);
        } catch (SecurityException e) {
            error(e);
        } catch (IllegalAccessException e1) {
            error(e1);
        } catch (InvocationTargetException e) {
            error(e);
        } catch (Exception e) {
            error(e);
        }
    }

    /**
     * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse
     *      response)
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        doGet(request, response);
    }

    public HttpServletRequest getRequest() {
        return request;
    }

    public void setRequest(HttpServletRequest request) {
        this.request = request;
    }

    public HttpServletResponse getResponse() {
        return response;
    }

    public void setResponse(HttpServletResponse response) {
        this.response = response;
    }

    public void view() throws ServletException, IOException {
        try {
            this.prePopulateView();
            this.getRequest().getRequestDispatcher("/WEB-INF/view/layout/layout.jsp").forward(this.getRequest(),
                    this.getResponse());
        } catch (ServletException e) {
            error(e);
        } catch (IOException e) {
            error(e);
        }
    }

    protected void error(final Exception e) throws ServletException, IOException {
        StringWriter sw = new StringWriter();
        PrintWriter pw = new PrintWriter(sw);
        e.printStackTrace(pw);
        String sStackTrace = sw.toString();
        System.out.println("EXCEPTION O QUE ?" + sStackTrace);
        this.route.setAction("error");
        this.route.setController("error");

        this.prePopulateView();
        this.getRequest().setAttribute("e", e);
        this.getRequest().setAttribute("printStackTrace", sStackTrace);

        this.getRequest().getRequestDispatcher("/WEB-INF/view/layout/layout.jsp").forward(this.getRequest(),
                this.getResponse());
    }

    private void prePopulateView() throws UnsupportedEncodingException {
        this.getRequest().setAttribute("auth", this.getSession().getAttribute("usuario"));
        this.getRequest().setAttribute("isAuth", this.getSession().getAttribute("usuario") instanceof TbUsuario);
        this.getRequest().setAttribute("nomeEmpresa", getServletContext().getInitParameter("nomeEmpresa"));
        this.getRequest().setAttribute("source", getServletContext().getInitParameter("source"));
        this.getRequest().setAttribute("route", this.route);
        this.getRequest().setAttribute("flashMessenger", flashMessenger);
    }

    public String getMethod() {
        return this.getRequest().getMethod();
    }

    public HttpSession getSession() {
        return this.getRequest().getSession();
    }

    public void redirect(String url) throws IOException {
        this.getResponse().sendRedirect(getServletContext().getInitParameter("source").toString() + url);

    }

    public boolean hasParam(String name) {
        return this.params.hasParam(name);
    }

    public String getParam(String name) {
        return this.params.getParam(name);
    }

    public String getParamOr(String parameter, String or) {
        return this.params.getParamOr(parameter, or);
    }

    public ParamRequestManager postFilter() throws UnsupportedEncodingException {
        ParamRequestManager routeParams = new ParamRequestManager();
        Enumeration<String> parameterNames = this.getRequest().getParameterNames();

        while (parameterNames.hasMoreElements()) {
            String paramName = parameterNames.nextElement();
            RouteParam routeParam = new RouteParam();
            routeParam.setName(paramName);

            String[] paramValues = this.getRequest().getParameterValues(paramName);
            for (int i = 0; i < paramValues.length; i++) {
                String paramValue = paramValues[i];
                routeParam.setValue(paramValue); // @TODO melhorar para array RECURSIVO
            }

            routeParams.add(routeParam);
        }

        return routeParams;
    }

    /**
     * @return the flashMessenger
     */
    public FlashMessenger getFlashMessenger() {
        return flashMessenger;
    }

    /**
     * @param flashMessenger
     *            the flashMessenger to set
     */
    public void setFlashMessenger(FlashMessenger flashMessenger) {
        this.flashMessenger = flashMessenger;
    }

    /**
     * @return the logErrorService
     */
    public LogErrorService getLogErrorService() {
        return logErrorService;
    }

    /**
     * @param logErrorService
     *            the logErrorService to set
     */
    public void setLogErrorService(LogErrorService logErrorService) {
        this.logErrorService = logErrorService;
    }

    public Route getRoute() {
        return this.route;
    }

    public void setNoRender(boolean isRender) {
        this.noRender = isRender;
    }

    public static String camelcasify(String in) {
        StringBuilder sb = new StringBuilder();
        boolean capitalizeNext = false;
        for (char c : in.toCharArray()) {
            if (c == '-') {
                capitalizeNext = true;
            } else {
                if (capitalizeNext) {
                    sb.append(Character.toUpperCase(c));
                    capitalizeNext = false;
                } else {
                    sb.append(c);
                }
            }
        }

        return sb.toString();
    }

    public String toJson(IJsonModel jsonModel) {
        Gson gson = new Gson();
        String json = gson.toJson(jsonModel);
        return json;
    }

    private boolean isXhtmlHttpRequest() {
        System.out.println("VAMOS VER ? " + getResponse().getContentType());
        return getResponse().getContentType().equals("application/json");
    }

    public void setRequestXhtmlHttpRequest(IJsonModel jsonModel) throws IOException {
        setNoRender(true);
        getResponse().setContentType("application/json");
        getResponse().setHeader("Content-Disposition", "inline");
        PrintWriter out = getResponse().getWriter();
        out.print(this.toJson(jsonModel));
        out.flush();
    }

    public boolean isRequestImage() {

        return this.getRequest().getContentType().matches("(.*)image(.*)");
    }

    public boolean isNullOrBlank(final String s) {
        return s == null || s.trim().length() == 0;
    }

    public String getParameterOrValue(String parameterName, String or) {
        boolean isEmpty = isNullOrBlank(request.getParameter(parameterName));
        String value = or;
        if (!isEmpty) {
            value = request.getParameter(parameterName);
        }

        return value;
    }
}
