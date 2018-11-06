package br.com.estruturart.controller;

import java.util.Base64;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpSession;
import javax.ws.rs.HttpMethod;

import com.google.gson.Gson;

import br.com.estruturart.model.TbUsuario;
import br.com.estruturart.model.TbPerfil;
import br.com.estruturart.persistency.Usuario;

/**
 * Servlet implementation class Auth
 */
@WebServlet(name = "auth", urlPatterns = { "/auth", "/auth/index/logout", "/auth/recuperar" })
public class AuthController extends AbstractServlet {
    private static final long serialVersionUID = -4214231788151597837L;

    public void indexAction() throws Exception {


        String name = null;
        String pass = null;
        String msgUsuario = null;
        String msgPassword = null;

        if (this.getMethod().equals(HttpMethod.POST)) {
            name = this.getRequest().getParameter("name");
            pass = this.getRequest().getParameter("pass");

            if (name == null || name.equals("")) {
                msgUsuario = "Informe o e-mail do usuário!";
            }

            if (pass == null || pass.isEmpty()) {
                msgPassword = "Informe a senha de acesso!";
            }

            if (msgUsuario == null && msgPassword == null) {
                Usuario modelUsuario = new Usuario();

                TbUsuario usuario = modelUsuario.getUsuario(name, pass);
                if (usuario.getId() > 0) {
                    if (usuario.getStatusUsuarioId() == 1) {
                        if (usuario.getPerfilId() == TbPerfil.FUNCIONARIO) {
                            HttpSession session = this.getSession();

                            session.setAttribute("usuario", usuario);
                            if (!getParameterOrValue("remember", "0").equals("0")) {
                                Gson gson = new Gson();
                                Cookie cookie = new Cookie("login", Base64.getEncoder().encodeToString(gson.toJson(usuario).getBytes()));
                                //cookie.setSecure(false);
                                //cookie.setVersion(0);
                                cookie.setMaxAge(60*60*24); // 24 hour
                                getResponse().addCookie(cookie);
                            }
                            this.redirect("home");
                        } else {
                            msgUsuario = "Perfil inválido. Verifique!";
                        }
                    } else {
                        msgUsuario = "Usuário bloqueado!";
                    }
                } else {
                    msgUsuario = "E-mail ou senha incorreto!";
                }
            } else {
                this.getRequest().setAttribute("msgUsuario", msgUsuario);
                this.getRequest().setAttribute("msgPassword", msgPassword);
            }
        } else {
            Cookie c = getCookie(getRequest(), "login");

            if (c != null) {
                String jsonLogin = c.getValue();
                if (!jsonLogin.isEmpty()) {
                    try {
                        Gson gson = new Gson();
                        TbUsuario usuario = (TbUsuario)gson.fromJson(new String(Base64.getDecoder().decode(jsonLogin)), TbUsuario.class);
                        if (usuario.getId() > 0) {
                            if (usuario.getStatusUsuarioId() == 1) {
                                if (usuario.getPerfilId() == TbPerfil.FUNCIONARIO) {

                                    HttpSession session = this.getSession();
                                    session.setAttribute("usuario", usuario);
                                    this.redirect("home");
                                }
                            }
                        }
                    } catch (Exception e) {

                    }
                }
            }
        }

        this.getRequest().setAttribute("msgUsuario", msgUsuario);
        this.getRequest().setAttribute("msgPassword", msgPassword);
        this.getRequest().setAttribute("name", name);
        this.getRequest().setAttribute("pass", pass);
    }

    public void logoutAction() throws Exception {
        if (this.getSession().getAttribute("usuario") instanceof TbUsuario) {


            Cookie c = getCookie(getRequest(), "login");

            if (c != null) {
                String jsonLogin = c.getValue();
                if (!jsonLogin.isEmpty()) {

                    c.setMaxAge(0);
                    c.setValue(null);
                    c.setPath("/sistemateste");
                    c.setDomain("");
                    c.setComment("Alterando");
                    this.getResponse().addCookie(c);
                }
            }

            this.getSession().setAttribute("usuario", null);
            this.getSession().invalidate();
        }

        setNoRender(true);
        getRequest().setAttribute("source", getServletContext().getInitParameter("source"));
        getRequest().getRequestDispatcher("/WEB-INF/view/auth/logout.jsp")
            .forward(this.getRequest(), this.getResponse());
        //this.redirect("auth");
    }

    public void flashMessagesAction() {
        this.setNoRender(true);
        this.getFlashMessenger().clear();

    }

    public void recuperarAction() {
        String name = null;
        name = this.getRequest().getParameter("name");
    }
}
