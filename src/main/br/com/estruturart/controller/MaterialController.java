package br.com.estruturart.controller;

import java.util.List;

import javax.servlet.annotation.WebServlet;
import javax.ws.rs.HttpMethod;

import br.com.estruturart.model.TbFornecedor;
import br.com.estruturart.model.TbMaterial;
import br.com.estruturart.model.TbStatusMaterial;
import br.com.estruturart.model.TbUnidadeMedida;
import br.com.estruturart.persistency.Fornecedor;
import br.com.estruturart.persistency.Material;
import br.com.estruturart.persistency.StatusMaterial;
import br.com.estruturart.persistency.UnidadeMedida;
import br.com.estruturart.utility.Exception1001;
import br.com.estruturart.utility.FlashMessenger;
import br.com.estruturart.utility.JsonModel;
import br.com.estruturart.utility.Paginator;

/**
 * Servlet implementation class Auth
 */
@WebServlet(name = "material", urlPatterns = { "/material", "/material/cadastro", "/material/index/page/*",
        "/material/editar/id/*", "/material/buscar-modelo", "/material/alterar-status" })
public class MaterialController extends AbstractServlet {
    private static final long serialVersionUID = -4214231788151587849L;

    public void indexAction() throws Exception {
        int page = Integer.parseInt(this.getParamOr("page", "1"));
        int offset = 10;
        System.out.println("PAGINA ATUAL: " + Integer.parseInt(this.getParamOr("page", "1")));
        Material modelMaterial = new Material();
        Paginator paginator = modelMaterial.findAllPaginated(page, offset, this.postFilter());

        paginator.setLink(getServletContext().getInitParameter("source").toString() + "material/index/page/{id}");
        System.out.println("TOTAL MATERIL: " + paginator.getIterator().size());
        this.getRequest().setAttribute("paginator", paginator);
        this.getRequest().setAttribute("filter", this.postFilter());
    }

    public void cadastroAction() throws Exception {
        int fkMaterial = Integer.parseInt(this.getParamOr("id", "0"));

        Material materialModel = new Material();
        Fornecedor fornecedorModel = new Fornecedor();
        UnidadeMedida unidadeMedidaModel = new UnidadeMedida();
        StatusMaterial statusMaterialModel = new StatusMaterial();
        TbMaterial material = new TbMaterial();

        List<TbFornecedor> fornecedores = fornecedorModel.findAll();
        List<TbStatusMaterial> statusMaterias = statusMaterialModel.findAll();
        List<TbUnidadeMedida> unidadeMedidas = unidadeMedidaModel.findAll();

        try {
            if (fkMaterial != 0) {
                material = materialModel.getMaterialById(fkMaterial);
                if (material.getId() == 0) {
                    throw new Exception1001("Material informado inválido!");
                }
            }

            if (this.getMethod().equals(HttpMethod.POST)) {
                String precoStr = this.getRequest().getParameter("preco").replace(".", "").replace(",", ".");
                material.setDescricao(this.getRequest().getParameter("descricao"));
                material.setMateriaPrima(Integer.parseInt(this.getRequest().getParameter("materia_prima")));
                material.setStatusMaterialId(Integer.parseInt(this.getRequest().getParameter("status_material_id")));
                material.setUnidadeMedidaId(Integer.parseInt(this.getRequest().getParameter("unidade_medida_id")));
                material.setFornecedorId(Integer.parseInt(this.getRequest().getParameter("fornecedor_id")));
                material.setQuantidade(Integer.parseInt(this.getRequest().getParameter("quantidade")));
                material.setPreco(Float.parseFloat(precoStr.equals("") ? "0" : precoStr));

                if (material.isValid()) {
                    System.out.println("Descricao: " + material.getDescricao());
                    System.out.println("MAteria Prima: " + material.getMateriaPrima());
                    System.out.println("Status MAterial: " + material.getStatusMaterialId());
                    System.out.println("Unidade Medida: " + material.getUnidadeMedidaId());
                    System.out.println("Fornecedor: " + material.getFornecedorId());
                    System.out.println("Quantidade: " + material.getQuantidade());
                    System.out.println("Preco: " + material.getPreco());

                    if (fkMaterial != 0) {
                        materialModel.update(material);
                    } else {
                        System.out.println("INSERINDO ");
                        materialModel.insert(material);
                    }

                    getFlashMessenger().setType(FlashMessenger.SUCCESS).add("Material salvo com sucesso!");
                    redirect("material");
                }
            }
        } catch (Exception1001 ee) {
            getFlashMessenger().setType(FlashMessenger.ERROR).add(ee.getMessage());
            redirect("material");
        } catch (Exception e) {
            getLogErrorService().createLog(e);
            getFlashMessenger().setType(FlashMessenger.ERROR).add("Ocorreu um erro ao salvar o material!");
        }

        this.getRequest().setAttribute("material", material);
        this.getRequest().setAttribute("fornecedores", fornecedores);
        this.getRequest().setAttribute("unidadeMedidas", unidadeMedidas);
        this.getRequest().setAttribute("statusMaterial", statusMaterias);
    }

    public void editarAction() throws Exception {
        this.getRoute().setAction("cadastroAction");
        this.cadastroAction();
    }

    public void buscarModeloAction() throws Exception {
        Material material = new Material();
        JsonModel jsonModel = new JsonModel();
        jsonModel.setList(material.findMaterialByNomeList(this.getRequest().getParameter("nome")));

        setRequestXhtmlHttpRequest(jsonModel);
    }

    public void alterarStatusAction() throws Exception
    {
        int id = Integer.parseInt(this.getRequest().getParameter("id"));
        int status = Integer.parseInt(this.getRequest().getParameter("status"));

        Material materialModel = new Material();
        JsonModel jsonModel = new JsonModel();

        TbMaterial material = materialModel.getMaterialById(id);
        material.setStatusMaterialId(status);
        if (materialModel.updateStatus(material) > 0) {
            jsonModel.setStatus(true);
            jsonModel.setMessage("Status alterado com sucesso!");
        } else {
            jsonModel.setStatus(false);
            jsonModel.setMessage("Ocorreu um erro ao alterar o status do material!");
        }

        setRequestXhtmlHttpRequest(jsonModel);
    }
}
