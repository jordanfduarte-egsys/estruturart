package br.com.estruturart.controller;

import javax.servlet.annotation.WebServlet;
import javax.ws.rs.HttpMethod;

import br.com.estruturart.model.TbFornecedor;
import br.com.estruturart.persistency.Fornecedor;
import br.com.estruturart.utility.Exception1001;
import br.com.estruturart.utility.FlashMessenger;
import br.com.estruturart.utility.Paginator;

/**
 * Servlet implementation class Auth
 */
@WebServlet(name = "fornecedor", urlPatterns = { "/fornecedor", "/fornecedor/cadastro", "/fornecedor/index/page/*",
        "/fornecedor/editar/id/*" })
public class FornecedorController extends AbstractServlet {
    private static final long serialVersionUID = -4214231788151587849L;

    public void indexAction() throws Exception {
        int page = Integer.parseInt(this.getParamOr("page", "1"));
        int offset = 10;
        System.out.println("PAGINA ATUAL: " + Integer.parseInt(this.getParamOr("page", "1")));
        Fornecedor modelFornecedor = new Fornecedor();
        Paginator paginator = modelFornecedor.findAllPaginated(page, offset, this.postFilter());

        paginator.setLink(getServletContext().getInitParameter("source").toString() + "fornecedor/index/page/{id}");
        System.out.println("TOTAL FORNECEDOR: " + paginator.getIterator().size());
        this.getRequest().setAttribute("paginator", paginator);
        this.getRequest().setAttribute("filter", this.postFilter());
    }

    public void cadastroAction() throws Exception {
        int fkFornecedor = Integer.parseInt(this.getParamOr("id", "0"));

        Fornecedor fornecedorModel = new Fornecedor();
        TbFornecedor fornecedor = new TbFornecedor();

        try {
            if (fkFornecedor != 0) {
                fornecedor = fornecedorModel.getFornecedorById(fkFornecedor);
                if (fornecedor.getId() == 0) {
                    throw new Exception1001("Fornecedor informado invalido!");
                }
            }

            if (this.getMethod().equals(HttpMethod.POST)) {
                fornecedor.setNome(this.getRequest().getParameter("nome"));
                fornecedor.setTelefone(this.getRequest().getParameter("telefone"));

                if (fornecedor.isValid()) {
                    if (fkFornecedor != 0) {
                        fornecedorModel.update(fornecedor);
                    } else {
                        fornecedorModel.insert(fornecedor);
                    }

                    getFlashMessenger().setType(FlashMessenger.SUCCESS).add("Fornecedor salvo com sucesso!");
                    redirect("fornecedor");
                }
            }
        } catch (Exception1001 ee) {
            getFlashMessenger().setType(FlashMessenger.ERROR).add(ee.getMessage());
            redirect("fornecedor");
        } catch (Exception e) {
            getLogErrorService().createLog(e);
            getFlashMessenger().setType(FlashMessenger.ERROR).add("Ocorreu um erro ao salvar o fornecedor!");
        }

        this.getRequest().setAttribute("fornecedor", fornecedor);
    }

    public void editarAction() throws Exception {
        this.getRoute().setAction("cadastroAction");
        this.cadastroAction();
    }
}
