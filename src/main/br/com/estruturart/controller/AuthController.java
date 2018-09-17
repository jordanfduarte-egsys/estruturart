package br.com.estruturart.controller;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpSession;
import javax.ws.rs.HttpMethod;

import br.com.estruturart.model.TbUsuario;
import br.com.estruturart.model.TbPerfil;
import br.com.estruturart.persistency.Usuario;

/**
 * Servlet implementation class Auth
 */
@WebServlet(name = "auth", urlPatterns = { "/auth", "/auth/index/logout" })
public class AuthController extends AbstractServlet {
    private static final long serialVersionUID = -4214231788151597837L;

    public void indexAction() throws Exception {
        System.out.println("CHEGO");

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

                System.out.println("Nome: " + name);
                System.out.println("Pass: " + pass);
                TbUsuario usuario = modelUsuario.getUsuario(name, pass);
                if (usuario.getId() > 0) {
                    if (usuario.getStatusUsuarioId() == 1) {
                        if (usuario.getPerfilId() == TbPerfil.FUNCIONARIO) {
                            HttpSession session = this.getSession();

                            session.setAttribute("usuario", usuario);
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
        }

        this.getRequest().setAttribute("msgUsuario", msgUsuario);
        this.getRequest().setAttribute("msgPassword", msgPassword);
        this.getRequest().setAttribute("name", name);
        this.getRequest().setAttribute("pass", pass);
    }

    public void logoutAction() throws Exception {
        if (this.getSession().getAttribute("usuario") instanceof TbUsuario) {
            System.out.println("TROCO O USUAIRO");
            this.getSession().setAttribute("usuario", null);
        }

        this.redirect("auth");
    }

    public void flashMessagesAction() {
        this.setNoRender(true);
        this.getFlashMessenger().clear();

    }
}
