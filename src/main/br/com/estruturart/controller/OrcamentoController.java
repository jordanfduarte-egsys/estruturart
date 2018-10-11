package br.com.estruturart.controller;

import java.util.List;

import javax.servlet.annotation.WebServlet;
import javax.ws.rs.HttpMethod;

import java.util.Enumeration;
import java.lang.IndexOutOfBoundsException;
import br.com.estruturart.model.TbFornecedor;
import br.com.estruturart.model.TbMaterial;
import br.com.estruturart.model.TbStatusMaterial;
import br.com.estruturart.model.TbUnidadeMedida;
import br.com.estruturart.model.TbCidade;
import br.com.estruturart.model.TbEstado;
import br.com.estruturart.model.TbModelo;
import br.com.estruturart.model.Orcamento;
import br.com.estruturart.model.TbUsuario;
import br.com.estruturart.model.TbEndereco;
import br.com.estruturart.model.TbPerfil;
import br.com.estruturart.persistency.Fornecedor;
import br.com.estruturart.persistency.Material;
import br.com.estruturart.persistency.ConnectionManager;
import br.com.estruturart.persistency.Modelo;
import br.com.estruturart.persistency.StatusMaterial;
import br.com.estruturart.persistency.UnidadeMedida;
import br.com.estruturart.utility.Exception1001;
import br.com.estruturart.utility.FlashMessenger;
import br.com.estruturart.utility.JsonModel;
import br.com.estruturart.utility.Paginator;
import br.com.estruturart.persistency.Estado;
import br.com.estruturart.persistency.Cidade;
import br.com.estruturart.persistency.Perfil;
import br.com.estruturart.model.TbEstado;
import br.com.estruturart.utility.Util;
import java.util.ArrayList;
import java.util.List;
import javax.ws.rs.HttpMethod;
import br.com.estruturart.service.FinalizarOrcamento;
import java.sql.Connection;

/**
 * Servlet implementation class Orcamento
 */
@WebServlet(name = "orcamento", urlPatterns = { "/orcamento", "/orcamento/etapa1", "/orcamento/etapa2", "/orcamento/etapa3" })
public class OrcamentoController extends AbstractServlet {
    private static final long serialVersionUID = -4214231188151587849L;

    public void indexAction() throws Exception
    {
        String rota = "orcamento/etapa1";
        if (getSession().getAttribute("orcamento") instanceof Orcamento) {
            Orcamento orcamento = (Orcamento) getSession().getAttribute("orcamento");
            if (orcamento.isValidEtapa1() && orcamento.isValidEtapa2()) {
                rota = "orcamento/etapa3";
            } else if (orcamento.isValidEtapa1()) {
                rota = "orcamento/etapa2";
            } else {
                rota = "orcamento/etapa1";
            }
        } else {
            getSession().setAttribute("orcamento", new Orcamento());
        }

        redirect(rota);
    }

    public void etapa1Action() throws Exception
    {
        Estado modelEstado = new Estado();
        Cidade modelCidade = new Cidade();
        Perfil perfilModel = new Perfil();
        Orcamento orcamento = (Orcamento) getSession().getAttribute("orcamento");

        List<TbCidade> cidades = new ArrayList<TbCidade>();
        List<TbEstado> estados = modelEstado.findEstados();

        if (this.getMethod().equals(HttpMethod.POST)) {
            System.out.println("POST MESMO ?");
            TbUsuario usuario = new TbUsuario();
            TbEndereco endereco = new TbEndereco();
            String tipoPessoa = "2";
            if (this.getRequest().getParameter("cpf_cnpj").replaceAll("[^0-9]", "").length() == 11)  {
                tipoPessoa = "1";
            }

            usuario.setId(Integer.parseInt(this.getParameterOrValue("usuario_id", "0")));
            usuario.setNome(this.getParameterOrValue("nome_completo", ""));
            usuario.setCpfCnpj(this.getParameterOrValue("cpf_cnpj", ""));
            usuario.setEmail(this.getParameterOrValue("email", ""));
            usuario.setRgIncricaoEstadual(this.getParameterOrValue("rg_inscricao_estadual", ""));
            usuario.setTelefone(this.getParameterOrValue("telefone", ""));
            usuario.setPerfilId(TbPerfil.CLIENTE);
            usuario.setTipoPessoa(tipoPessoa);
            usuario.setPerfil(perfilModel.findRow(TbPerfil.CLIENTE));

            endereco.setCep(this.getParameterOrValue("cep", "").replace("-", ""));
            endereco.setLogradouro(this.getParameterOrValue("logradouro", ""));
            endereco.setBairro(this.getParameterOrValue("bairro", ""));
            endereco.setNumero(this.getParameterOrValue("numero", ""));
            endereco.setCidadeId(Integer.parseInt(this.getParameterOrValue("cidade_id", "0")));
            endereco.setEstadoId(Integer.parseInt(this.getParameterOrValue("estado_id", "0")));

            orcamento.setUsuario(usuario);
            orcamento.setEndereco(endereco);

            System.out.println("______________________");
            System.out.println("Cidade ID: " + this.getParameterOrValue("estado_id", "0"));
            System.out.println("______________________");
            System.out.println("______________________");

            if (orcamento.isValid(Orcamento.ETAPA1)) {
                redirect("orcamento/etapa2");
            }
        }

        if (orcamento.getEndereco().getEstadoId() > 0) {
            cidades = modelCidade.findCidadeByEstado(String.valueOf(orcamento.getEndereco().getEstadoId()), true);
        }

        this.getRequest().setAttribute("estados", estados);
        this.getRequest().setAttribute("cidades", cidades);
        this.getRequest().setAttribute("orcamento", orcamento);
    }

    public void etapa2Action() throws Exception
    {
        Orcamento orcamento = (Orcamento) getSession().getAttribute("orcamento");
        Modelo modelModelo = new Modelo();

        if (this.getMethod().equals(HttpMethod.POST)) {
            Enumeration<String> parameterNames = getRequest().getParameterNames();
            List<Integer> listIndex = new ArrayList<Integer>();
            System.out.println("---------------------------------------");
            System.out.println("---------------------------------------");
            while (parameterNames.hasMoreElements()) {
                String indexStr = parameterNames.nextElement().replaceAll("[^0-9]", "");
                int index = indexStr.equals("") ? -1 : Integer.parseInt(indexStr);

                if (index > -1) {
                    try {
                        listIndex.get(index);
                    } catch (IndexOutOfBoundsException e ) {
                        listIndex.add(index);
                    }
                }

                System.out.println("INDEX: " + index);
            }
            System.out.println("---------------------------------------");
            System.out.println("---------------------------------------");
            System.out.println("TOTAL itens: " + orcamento.getModelos().size());
            orcamento.getModelos().clear();
            for (int index : listIndex) {
                TbModelo modelo = new TbModelo();
                String paramId = String.format("item[%s]id", index);
                String paramAltura = String.format("item[%s]altura", index);
                String paramLargura = String.format("item[%s]largura", index);
                String paramQtd = String.format("item[%s]quantidade", index);
                String paramIsPintura = String.format("item[%s]isPintura", index);
                Integer id = Integer.parseInt(getParameterOrValue(paramId, "0"));

                if (id > 0) {
                    System.out.println("PRODUTO: " + id);
                    modelo = modelModelo.getModeloById(id);
                    modelo.setIndex(index);

                    modelo.setLarguraNova(Float.parseFloat(getParameterOrValue(paramLargura, String.valueOf(modelo.getLarguraPadrao()))));
                    modelo.setAlturaNova(Float.parseFloat(getParameterOrValue(paramAltura, String.valueOf(modelo.getAlturaPadrao()))));
                    modelo.setQuantidadeCompra(Integer.parseInt(getParameterOrValue(paramQtd, "1")));
                    modelo.setIsPintura(Integer.parseInt(getParameterOrValue(paramIsPintura, "0")) != 0);

                    System.out.println(
                        "Modelo Add ID: " + id +
                        ", LARGURA: " + modelo.getLarguraNova() +
                        ", ALTURA: " + modelo.getAlturaNova() +
                        ", QTD: " + modelo.getQuantidadeCompra() +
                        ",  PINTURA :" + modelo.isPintura()
                    );

                    orcamento.getModelos().add(modelo);
                }
            }
            System.out.println("TOTAL itens: " + orcamento.getModelos().size());
            System.out.println("---------------------------------------");
            System.out.println("---------------------------------------");

            if (orcamento.isValid(Orcamento.ETAPA2)) {
                redirect("orcamento/etapa3");
            }
        } else {
            if (!orcamento.isValidEtapa1()) {
                redirect("orcamento/etapa1");
            }
        }

        this.getRequest().setAttribute("orcamento", orcamento);
    }

    public void etapa3Action() throws Exception
    {
        Orcamento orcamento = (Orcamento) getSession().getAttribute("orcamento");
        if (this.getMethod().equals(HttpMethod.POST)) {
            String dataStr = getParameterOrValue("prev_entrega", "");

            orcamento.setPrevEntrega(null);
            if (Util.isDateValid(dataStr)) {
                orcamento.setPrevEntrega(Util.toDate(dataStr));
            }

            float valorMaoObra = Float.parseFloat(
                (
                    getRequest().getParameter("mao_obra").equals("")
                    ? "0"
                    : getRequest().getParameter("mao_obra").replace(".", "").replace(",", ".")
                )
            );

            orcamento.setDesconto(Float.parseFloat(getParameterOrValue("desconto", "0").replace(",", ".")));
            orcamento.setValorMaoObra(valorMaoObra);
            orcamento.setObservacao(getParameterOrValue("observacao", ""));

            System.out.println("========================================");
            System.out.println("========================================");
            System.out.println("========================================");
            System.out.println("Desconot: " + getParameterOrValue("desconto", "0"));
            System.out.println("Mao Obra: " + valorMaoObra);
            System.out.println("Obs: " + getParameterOrValue("observacao", ""));
            System.out.println("Entrega: " + getParameterOrValue("prev_entrega", ""));
            System.out.println("========================================");
            System.out.println("========================================");
            System.out.println("========================================");

            if (orcamento.isValid(Orcamento.ETAPA3)) {
                int usuarioId = ((TbUsuario) getSession().getAttribute("usuario")).getId();
                Connection conn = ConnectionManager.getConnection();
                conn.setAutoCommit(false);

                try {
                    orcamento.setUsuarioLog(usuarioId);
                    FinalizarOrcamento finalizarService = new FinalizarOrcamento(
                        getRequest(),
                        getResponse(),
                        orcamento
                    );
                    boolean isOrcamento = !getParameterOrValue("is_orcamento", "0").equals("0");
                    finalizarService.setIsOrcamento(isOrcamento);
                    finalizarService.setConnection(conn);
                    finalizarService.salvar();
                    conn.commit();

                    if (isOrcamento) {
                        getFlashMessenger().setType(FlashMessenger.SUCCESS)
                            .add("Orçamento criado com sucesso!");
                    } else {
                        getFlashMessenger().setType(FlashMessenger.SUCCESS)
                            .add("Pedido criado com sucesso!");
                    }

                    redirect("pedido/visualizar/id/" + finalizarService.getId());
                } catch (java.sql.SQLException e) {
                    conn.rollback();
                    conn.close();
                    getLogErrorService().createLog(e);
                    getFlashMessenger().setType(FlashMessenger.ERROR)
                        .add("Ocorreu um erro ao criar o orçamento. Verifique!");
                } catch (Exception e) {
                    conn.rollback();
                    conn.close();
                    getLogErrorService().createLog(e);
                    getFlashMessenger().setType(FlashMessenger.ERROR)
                        .add("Ocorreu um erro ao criar o orçamento. Verifique!");
                }
            }
        } else {
            if (!orcamento.isValidEtapa1()) {
                redirect("orcamento/etapa1");
            } else if (!orcamento.isValidEtapa2()) {
                redirect("orcamento/etapa2");
            }
        }

        this.getRequest().setAttribute("orcamento", orcamento);
    }
}
