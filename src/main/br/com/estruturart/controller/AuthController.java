package br.com.estruturart.controller;

import java.util.Base64;
import br.com.estruturart.utility.FlashMessenger;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpSession;
import javax.ws.rs.HttpMethod;
import br.com.estruturart.service.SendEmailService;
import java.io.FileReader;
import java.io.Reader;
import java.io.StringWriter;
import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import br.com.estruturart.model.TbParametro;
import br.com.estruturart.persistency.Parametro;
import com.google.gson.Gson;
import org.apache.commons.io.IOUtils;
import br.com.estruturart.model.TbUsuario;
import br.com.estruturart.model.TbPerfil;
import br.com.estruturart.persistency.Usuario;
import br.com.estruturart.utility.JsonModel;
import br.com.estruturart.utility.Util;

/**
 * Servlet implementation class Auth
 */
@WebServlet(name = "auth", urlPatterns = { "/auth", "/auth/index/logout", "/auth/recuperar", "/auth/recuperar-senha/h/*"})
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

    public void recuperarAction() throws Exception {
        String name = this.getRequest().getParameter("email");

        Usuario modelUsuario = new Usuario();
        Parametro modelParametros = new Parametro();
        TbUsuario usuario = modelUsuario.getUsuarioByEmail(name);
        String msgUsuario = "";
        boolean status = false;
        if (usuario.getId() > 0) {
            if (usuario.getStatusUsuarioId() == 1) {
                if (usuario.getPerfilId() == TbPerfil.FUNCIONARIO) {
                    String hash = convertStringToMd5(usuario.getCpfCnpj() + usuario.getCodigo() + usuario.getId());
                    modelUsuario.updateHash(hash, usuario.getId());

                    //=============================================================
                    SendEmailService emailService = new SendEmailService(getRequest(), getResponse());
                    TbParametro parametro = modelParametros.findAll();
                    emailService.setSubject("Recuperação de senha EstruturArt");
                    emailService.setTo(usuario.getEmail());
                    emailService.setFrom(parametro.getFrom());
                    emailService.setHost(parametro.getHostMail());
                    emailService.setUsuario(parametro.getUsuario());
                    emailService.setSenha(parametro.getSenha());

                    String sourceFilder = getServletContext().getInitParameter("folderUpload");
                    Reader input = new FileReader(sourceFilder + "/../WEB-INF/view/auth/mail-recuperacao.jsp");
                    StringWriter output = new StringWriter();
                    try {
                        IOUtils.copy(input, output);
                    } finally {
                        input.close();
                    }
                    String str = output.toString();
                    str = str.replaceAll("#nome_usuario", usuario.getNome());
                    str = str.replaceAll("#endereco", parametro.getLogradouro());
                    str = str.replaceAll("#cep", parametro.getCep());
                    str = str.replaceAll("#numero", parametro.getNumero());
                    str = str.replaceAll("#cidade", parametro.getCidade());
                    str = str.replaceAll("#uf", parametro.getUf());
                    str = str.replaceAll("#link", "http://" + parametro.getHost().replace("http://","").replace("https://","") + "/auth/recuperar-senha/h/" + hash);

                    emailService.setHtml(str);
                    emailService.send();
                    //=============================================================

                    msgUsuario = "Um e-mail foi enviado com o link para recuperação. Verifique!";
                    status = true;
                } else {
                    msgUsuario = "Perfil inválido. Verifique!";
                }
            } else {
                msgUsuario = "Usuário bloqueado!";
            }
        } else {
            msgUsuario = "E-mail incorreto!";
        }

        JsonModel jsonModel = new JsonModel();
        jsonModel.setMessage(msgUsuario);
        jsonModel.setStatus(status);
        setRequestXhtmlHttpRequest(jsonModel);
    }

    public void recuperarSenhaAction() throws Exception
    {
        String name = this.getParamOr("h", "none");
        Usuario modelUsuario = new Usuario();
        String message = null;
        String senha = "";
        TbUsuario usuario = modelUsuario.findUsuarioByCodigoRecuperacao(name);
        if (usuario.getId() > 0) {
            if (this.getSession().getAttribute("usuario") instanceof TbUsuario) {
                this.getSession().setAttribute("usuario", null);
                this.getSession().invalidate();

                this.redirect("auth/recuperar-senha/h/" + name);
            }

            if (this.getMethod().equals(HttpMethod.POST)) {
                senha = this.getRequest().getParameter("senha");
                if (Util.isPasswordValid(senha)) {
                    usuario.setSenha(senha);
                    modelUsuario.updateHash("", usuario.getId());
                    modelUsuario.updateSenha(usuario);

                    getFlashMessenger().setType(FlashMessenger.SUCCESS)
                        .add("Senha alterada com sucesso!");

                    this.redirect("auth");
                } else {
                    message = "Senha não corresponde a expecificação!";
                }
            }
        } else {
            this.redirect("auth");
        }

        this.getRequest().setAttribute("usuario", usuario);
        this.getRequest().setAttribute("senha", senha);
        this.getRequest().setAttribute("message", message);
    }

    private String convertStringToMd5(String valor)
    {
       MessageDigest mDigest;
       try {
          //Instanciamos o nosso HASH MD5, poderamos usar outro como
          //SHA, por exemplo, mas optamos por MD5.
         mDigest = MessageDigest.getInstance("MD5");

         //Convert a String valor para um array de bytes em MD5
         byte[] valorMD5 = mDigest.digest(valor.getBytes("UTF-8"));

         //Convertemos os bytes para hexadecimal, assim podemos salvar
         //no banco para posterior comparo se senhas
         StringBuffer sb = new StringBuffer();
         for (byte b : valorMD5) {
            sb.append(Integer.toHexString((b & 0xFF) |
            0x100).substring(1,3));
         }

         return sb.toString();
       } catch (NoSuchAlgorithmException e) {
         e.printStackTrace();
       } catch (UnsupportedEncodingException e) {
         e.printStackTrace();
       }

       return null;
    }
}
