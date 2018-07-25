package br.com.estruturart.controller;

import java.util.ArrayList;
import java.util.List;
import javax.servlet.annotation.WebServlet;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileUploadBase.FileSizeLimitExceededException;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;

import br.com.estruturart.model.TbMaterial;
import br.com.estruturart.model.TbModelo;
import br.com.estruturart.model.TbStatusModelo;
import br.com.estruturart.persistency.Material;
import br.com.estruturart.persistency.Modelo;
import br.com.estruturart.persistency.StatusModelo;
import br.com.estruturart.service.UploadService;
import br.com.estruturart.utility.Exception1001;
import br.com.estruturart.utility.FlashMessenger;
import br.com.estruturart.utility.Paginator;
import br.com.estruturart.utility.RouteParam;
import br.com.estruturart.utility.JsonModel;

/**
 * Servlet implementation class Auth
 */
@WebServlet(name = "modelo", urlPatterns = { "/modelo", "/modelo/cadastro", "/modelo/index/page/*", "/modelo/editar/id/*", "/modelo/buscar-modelo" })
public class ModeloController extends AbstractServlet
{
    private static final long serialVersionUID = -4214231458151587849L;

    public void indexAction() throws Exception
    {
        int page = Integer.parseInt(this.getParamOr("page", "1"));
        int offset = 10;
        System.out.println("PAGINA ATUAL: " + Integer.parseInt(this.getParamOr("page", "1")));
        Modelo modeloModel = new Modelo();
        Paginator paginator = modeloModel.findAllPaginated(page, offset, this.postFilter());

        paginator.setLink(getServletContext().getInitParameter("source").toString() + "modelo/index/page/{id}");
        System.out.println("TOTAL MODELO: " + paginator.getIterator().size());
        this.getRequest().setAttribute("paginator", paginator);
        this.getRequest().setAttribute("filter", this.postFilter());
    }

    public void cadastroAction() throws Exception
    {
        int fkModelo = Integer.parseInt(this.getParamOr("id", "0"));

        Modelo modeloModel = new Modelo();
        Material materialModel = new Material();
        TbModelo modelo = new TbModelo();
        StatusModelo modelStatusModelo = new StatusModelo();
        int widthModelo = Integer.parseInt(getServletContext().getInitParameter("widthModelo"));
        int heigthModelo = Integer.parseInt(getServletContext().getInitParameter("heigthModelo"));
        String extensoes = getServletContext().getInitParameter("extensoesImagem");

        List<TbStatusModelo> statusModelo = modelStatusModelo.findAll();
        try {
            if (fkModelo != 0) {
                modelo = modeloModel.getModeloById(fkModelo);
                if (modelo.getId() == 0) {
                    throw new Exception1001("Modelo informado inválido!");
                }
            }

            if (ServletFileUpload.isMultipartContent(getRequest())) {
                try {
                    List<TbMaterial> materiais = new ArrayList<TbMaterial>();
                    ServletFileUpload upload = new ServletFileUpload();
                    upload.setFileSizeMax(UploadService.MEMORY_THRESHOLD);
                    upload.setFileItemFactory(new DiskFileItemFactory());
                    List<FileItem> formItems = upload.parseRequest(getRequest());
                    modelo.clearMateriais();
                    for (FileItem item : formItems) {
                        String fieldname = item.getFieldName();
                        String fieldvalue = item.getString();
                        System.out.println("++++++++++++++++++++++");
                        System.out.println("FILED: " + fieldname);
                        System.out.println("FILED V: " + fieldvalue);
                        System.out.println("+++++++++++++++++++++");

                        if (item.isFormField()) {
                            if (fieldname.equals("nome")) {
                                modelo.setNome(fieldvalue);
                            }

                            if (fieldname.equals("descricao")) {
                                modelo.setDescricao(fieldvalue);
                            }

                            if (fieldname.equals("largura_padrao")) {
                                modelo.setLarguraPadrao(
                                        Float.parseFloat((fieldvalue.equals("") ? "0" : fieldvalue.replace(",", "."))));
                            }

                            if (fieldname.equals("altura_padrao")) {
                                modelo.setAlturaPadrao(
                                        Float.parseFloat((fieldvalue.equals("") ? "0" : fieldvalue.replace(",", "."))));
                            }

                            if (fieldname.equals("preco_pintura")) {
                                modelo.setPrecoPintura(Float.parseFloat(
                                        (fieldvalue.equals("") ? "0" : fieldvalue.replace(".", "").replace(",", "."))));
                            }

                            if (fieldname.equals("porcentagem_acrescimo")) {
                                modelo.setPorcentagemAcrescimo(
                                        Integer.parseInt((fieldvalue.equals("") ? "0" : fieldvalue)));
                            }

                            if (fieldname.equals("qtd_dias_producao")) {
                                modelo.setQtdDiasProducao(Integer.parseInt((fieldvalue.equals("") ? "0" : fieldvalue)));
                            }

                            if (fieldname.equals("qtd_dias_producao")) {
                                modelo.setQtdDiasProducao(Integer.parseInt((fieldvalue.equals("") ? "0" : fieldvalue)));
                            }

                            if (fieldname.equals("status_modelo_id")) {
                                modelo.setStatusModeloId(Integer.parseInt((fieldvalue.equals("") ? "0" : fieldvalue)));
                            }

                            if (fieldname.equals("materiais[]")) {
                                TbMaterial material = new TbMaterial();
                                material.setId(Integer.valueOf(fieldvalue));
                                materiais.add(material);
                            }
                        } else {
                            modelo.setFileItem(item);
                            System.out.println("IMG OK");
                        }
                    }

                    for (TbMaterial material : materiais) {
                        TbMaterial materialAux = materialModel.getMaterialById(material.getId());
                        if (materialAux.getId() != 0) {
                            modelo.setMateriais(materialAux);
                        }
                    }

                    if (modelo.isValid()) {
                        UploadService uploadService = new UploadService(this.getRequest());
                        uploadService.setExtensoes(extensoes.split(","));
                        uploadService.setFileItem(modelo.getFileItem());

                        String imagem = modelo.getImagem();
                        String sourceFilder = getServletContext().getInitParameter("folderUpload");
                        System.out.println("IMAGEM: " + imagem);
                        System.out.println("FOLDER: " + sourceFilder);
                        boolean isContinue = true;
                        if (modelo.getId() == 0) {
                            imagem = uploadService.process(sourceFilder, widthModelo, heigthModelo, null);

                            if (imagem.equals("")) {
                                modelo.getValidation().add(new RouteParam("imagem", uploadService.getMessageErro()));
                                isContinue = false;
                            }
                        } else if (modelo.getId() != 0 && uploadService.isUploadImagem()) {
                            imagem = uploadService.process(sourceFilder, widthModelo, heigthModelo, modelo.getImagem());
                            System.out.println("UPLOAD NA EDICAO: " + uploadService.isUploadImagem());
                            if (imagem.equals("")) {
                                modelo.getValidation().add(new RouteParam("imagem", uploadService.getMessageErro()));
                                isContinue = false;
                            }
                        }
                        System.out.println("IMAGEM2: " + imagem);

                        modelo.setImagem(imagem);

                        if (isContinue) {
                            if (fkModelo != 0) {
                                modeloModel.update(modelo);
                            } else {
                                modeloModel.insert(modelo);
                            }

                            getFlashMessenger().setType(FlashMessenger.SUCCESS).add("Modelo salvo com sucesso!");
                            redirect("modelo");
                        }
                    }
                } catch (FileSizeLimitExceededException e) {
                    modelo.getValidation()
                            .add(new RouteParam("imagem",
                                    String.format("Tamanho do arquivo de upload deve ser menor que 2MB.")));
                }
            }
        } catch (NumberFormatException e) {
            getFlashMessenger().setType(FlashMessenger.ERROR).add("Informe um campo númerico válido");
        } catch (Exception1001 ee) {
            getFlashMessenger().setType(FlashMessenger.ERROR).add(ee.getMessage());
            redirect("modelo");
        } catch (Exception e) {
            getLogErrorService().createLog(e);
            getFlashMessenger().setType(FlashMessenger.ERROR).add("Ocorreu um erro ao salvar o modelo!");
        }

        this.getRequest().setAttribute("modelo", modelo);
        this.getRequest().setAttribute("statusModelo", statusModelo);
        this.getRequest().setAttribute("widthModelo", widthModelo);
        this.getRequest().setAttribute("heigthModelo", heigthModelo);
        this.getRequest().setAttribute("extensoes", extensoes);
    }

    public void editarAction() throws Exception
    {
        this.getRoute().setAction("cadastroAction");
        this.cadastroAction();
    }

    public void buscarModeloAction() throws Exception
    {
        Modelo modelo = new Modelo();
        JsonModel jsonModel = new JsonModel();
        jsonModel.setList(modelo.findModeloByNomeList(this.getParameterOrValue("nome", ""), this.getParameterOrValue("id", "0")));

        setRequestXhtmlHttpRequest(jsonModel);
    }
}
