package br.com.estruturart.controller;

import javax.servlet.annotation.WebServlet;
import javax.ws.rs.HttpMethod;

import br.com.estruturart.model.TbUsuario;
import br.com.estruturart.persistency.Perfil;
import br.com.estruturart.persistency.Usuario;
import br.com.estruturart.utility.Exception1001;
import br.com.estruturart.utility.FlashMessenger;
import br.com.estruturart.utility.Paginator;
import br.com.estruturart.utility.JsonModel;

/**
 * Servlet implementation class Auth
 */
@WebServlet(name = "usuario", urlPatterns = { "/usuario", "/usuario/cadastro", "/usuario/index/page/*",
        "usuario/editar/id/*", "/usuario/find-cpf-cnpj" })
public class UsuarioController extends AbstractServlet {
    private static final long serialVersionUID = -4214231788151597849L;

    public void indexAction() throws Exception {
        int page = Integer.parseInt(this.getParamOr("page", "1"));
        int offset = 10;
        System.out.println("PAGINA ATUAL: " + Integer.parseInt(this.getParamOr("page", "1")));
        Usuario modelUsuario = new Usuario();
        Paginator paginator = modelUsuario.findAllPaginated(page, offset, this.postFilter());

        paginator.setLink(getServletContext().getInitParameter("source").toString() + "usuario/index/page/{id}");

        boolean isFiltroAvancado = false;
        if (this.postFilter().hasParam("busca_nome") || this.postFilter().hasParam("cpf_cnpj")
                || this.postFilter().hasParam("rg_incricao_estadual") || this.postFilter().hasParam("email")) {
            isFiltroAvancado = true;
        }

        this.getRequest().setAttribute("paginator", paginator);
        this.getRequest().setAttribute("filter", this.postFilter());
        this.getRequest().setAttribute("isFiltroAvancado", isFiltroAvancado);
    }

    public void cadastroAction() throws Exception {
        int fkUsuario = Integer.parseInt(this.getParamOr("id", "0"));

        Usuario usuarioModel = new Usuario();
        Perfil perfilModel = new Perfil();
        TbUsuario usuario = new TbUsuario();

        try {
            if (fkUsuario != 0) {
                usuario = usuarioModel.getUsuarioById(fkUsuario);
                if (usuario.getId() == 0) {
                    throw new Exception1001("Usuario informado inv?lido!");
                }
            }

            usuario.setPerfilAll(perfilModel.findPerfisAll());

            if (this.getMethod().equals(HttpMethod.POST)) {
                usuario.setNome(this.getRequest().getParameter("nome"));
                usuario.setCpfCnpj(this.getRequest().getParameter("cpf_cnpj"));
                usuario.setEmail(this.getRequest().getParameter("email"));
                usuario.setRgIncricaoEstadual(this.getRequest().getParameter("rg_inscricao_estadual"));
                usuario.setTelefone(this.getRequest().getParameter("telefone"));
                usuario.setPerfilId(Integer.valueOf(this.getRequest().getParameter("perfil_id")));
                usuario.setSenha(this.getRequest().getParameter("senha"));
                usuario.setTipoPessoa(this.getRequest().getParameter("tipo_pessoa"));
                usuario.setPerfil(perfilModel.findRow(usuario.getPerfilId()));

                if (usuario.isValid()) {
                    if (fkUsuario != 0) {
                        usuarioModel.update(usuario);
                    } else {
                        usuarioModel.insert(usuario);
                    }

                    getFlashMessenger().setType(FlashMessenger.SUCCESS).add("Usu?rio salvo com sucesso!");
                    redirect("usuario");
                }
            }
        } catch (Exception1001 e) {
            getFlashMessenger().setType(FlashMessenger.ERROR).add(e.getMessage());
            redirect("usuario");
        } catch (Exception e) {
            getLogErrorService().createLog(e);
            getFlashMessenger().setType(FlashMessenger.ERROR).add("Ocorreu um erro ao salvar o usu?rio!");
        }

        this.getRequest().setAttribute("usuario", usuario);
    }

    public void editarAction() throws Exception {
        this.getRoute().setAction("cadastroAction");
        this.cadastroAction();
    }

    public void findCpfCnpjAction() throws Exception {
        Usuario usuario = new Usuario();
        JsonModel jsonModel = new JsonModel();
        jsonModel.setObject(usuario.findUsuarioByCpjCNpj(this.getRequest().getParameter("cpf_cnpj")));

        setRequestXhtmlHttpRequest(jsonModel);
    }
}
