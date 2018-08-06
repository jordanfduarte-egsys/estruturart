package br.com.estruturart.controller;

import javax.servlet.annotation.WebServlet;
import javax.ws.rs.HttpMethod;
import br.com.estruturart.model.TbPedido;
import br.com.estruturart.model.TbCidade;
import br.com.estruturart.model.TbEstado;
import br.com.estruturart.model.TbEndereco;
import br.com.estruturart.model.TbStatusItem;
import br.com.estruturart.model.TbPedidoItem;
import br.com.estruturart.model.TbLogPedido;
import br.com.estruturart.persistency.Parametro;
import br.com.estruturart.model.TbParametro;
import br.com.estruturart.model.TbUsuario;
import br.com.estruturart.model.TbStatusPedido;
import br.com.estruturart.utility.ParamRequestManager;
import br.com.estruturart.utility.PedidoJsonModel;
import br.com.estruturart.utility.JsonModel;
import br.com.estruturart.utility.Util;
import br.com.estruturart.utility.FlashMessenger;
import br.com.estruturart.service.SendEmailService;
import br.com.estruturart.persistency.Pedido;
import br.com.estruturart.persistency.PedidoItem;
import br.com.estruturart.persistency.Estado;
import br.com.estruturart.persistency.Cidade;
import br.com.estruturart.persistency.Endereco;
import br.com.estruturart.persistency.LogPedido;
import br.com.estruturart.persistency.StatusPedido;
import br.com.estruturart.utility.Util;
import org.apache.commons.io.IOUtils;
import java.io.FileReader;
import java.util.Enumeration;
import java.util.Date;
import java.util.Calendar;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

/**
 * Servlet implementation class Pedido
 */
@WebServlet(name = "pedido", urlPatterns = { "/pedido", "/pedido/cadastro", "/pedido/index/page/*", "pedido/editar/id/*"})
public class PedidoController extends AbstractServlet
{
    private static final long serialVersionUID = -4219231788151597849L;

    public void indexAction() throws Exception
    {
        StatusPedido statusPedidoModel = new StatusPedido();
        List<TbStatusPedido> statusPedido = statusPedidoModel.findAll();

        boolean isFiltroAvancado = false;
        ParamRequestManager postFilter = new ParamRequestManager();
        if (getSession().getAttribute("session_pedido") instanceof ParamRequestManager) {
            postFilter = (ParamRequestManager) getSession().getAttribute("session_pedido");
            if (
                postFilter.hasParam("nome") ||
                postFilter.hasParam("status") ||
                postFilter.hasParam("cep_or_destinatario") ||
                postFilter.hasParam("data_ini") ||
                postFilter.hasParam("data_fim")
            ) {
                isFiltroAvancado = true;
            }
        }

        this.getRequest().setAttribute("filter", postFilter);
        this.getRequest().setAttribute("status", statusPedido);
        this.getRequest().setAttribute("isFiltroAvancado", isFiltroAvancado);
    }

    public void visualizarAction() throws Exception
    {
        int id = Integer.parseInt(this.getParamOr("id", "0"));

        Pedido pedidoModel = new Pedido();
        StatusPedido statusPedidoModel = new StatusPedido();
        LogPedido modelLog = new LogPedido();

        List<TbStatusPedido> statusPedido = statusPedidoModel.findAll();
        List<TbLogPedido> logsPedido = modelLog.findLogPedido(id);
        TbPedido pedido = pedidoModel.findPedidoVisualizacao(id);

        getRequest().setAttribute("pedido", pedido);
        getRequest().setAttribute("statusPedido", statusPedido);
        getRequest().setAttribute("logs", logsPedido);
    }

    public void editarAction() throws Exception
    {
        int id = Integer.parseInt(this.getParamOr("id", "0"));

        Pedido pedidoModel = new Pedido();
        PedidoItem modelPedidoItem = new PedidoItem();
        Endereco enderecoModel = new Endereco();
        Estado modelEstado = new Estado();
        Cidade modelCidade = new Cidade();
        LogPedido logPedido = new LogPedido();
        StatusPedido statusPedidoModel = new StatusPedido();
        List<TbCidade> cidades = new ArrayList<TbCidade>();
        List<TbEstado> estados = modelEstado.findEstados();

        List<TbStatusPedido> statusPedido = statusPedidoModel.findAll();
        TbPedido pedido = pedidoModel.findPedidoVisualizacao(id);

        if (this.getMethod().equals(HttpMethod.POST)) {
            String dataStr = getParameterOrValue("prev_entrega", "");
            pedido.setObservacao(this.getParameterOrValue("observacao", ""));
            pedido.setDataPrevisaoInstalacao(null);
            if (Util.isDateValid(dataStr)) {
                pedido.setDataPrevisaoInstalacao(Util.toDate(dataStr));
            }

            pedido.getEndereco().setCep(this.getParameterOrValue("cep", "").replace("-", ""));
            pedido.getEndereco().setLogradouro(this.getParameterOrValue("logradouro", ""));
            pedido.getEndereco().setBairro(this.getParameterOrValue("bairro", ""));
            pedido.getEndereco().setNumero(this.getParameterOrValue("numero", ""));
            pedido.getEndereco().setCidadeId(Integer.parseInt(this.getParameterOrValue("cidade_id", "0")));
            pedido.getEndereco().setEstadoId(Integer.parseInt(this.getParameterOrValue("estado_id", "0")));
            pedido.getEndereco().setPedidoId(pedido.getId());
            // verificar se existe post de cancelamento de itens... canelar e extornar valor

            if (pedido.isValid() && pedido.getEndereco().isValid()) {
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
                }

                if (listIndex.size() > 0) {
                    // cancelar o pedido
                    if (listIndex.size() == pedido.getItensNaoCancelado()) {
                        pedido.setStatusPedidoId(TbStatusPedido.CANCELADO);
                        for (TbPedidoItem item : pedido.getItens()) {
                            item.setStatusItemId(TbPedidoItem.CANCELADO);
                            modelPedidoItem.update(item);
                        }
                    } else { // cancelar o item
                        float valorTotal = 0;
                        for (int itemCanc : listIndex) {
                            for (TbPedidoItem item : pedido.getItens()) {
                                if (item.getId() == itemCanc) {
                                    item.setStatusItemId(TbPedidoItem.CANCELADO);
                                    modelPedidoItem.update(item);
                                    valorTotal += item.getPrecoItemMaisPinturaFloat();
                                }
                            }
                        }

                        if (pedido.getDescontoGeral() > 0) {
                            pedido.setDescontoGeral((listIndex.size() * pedido.getDescontoGeral()) / pedido.getItensNaoCancelado());

                            valorTotal = (valorTotal - (valorTotal * pedido.getDescontoGeral()) / 100) + pedido.getValorMaoObra();
                        }

                        pedido.setValorTotal(valorTotal);
                    }
                }

                pedidoModel.update(pedido);
                enderecoModel.update(pedido.getEndereco());
                logPedido.insert(pedido.getStatusPedidoId(), pedido.getUsuario().getId(), pedido.getId());

                getFlashMessenger().setType(FlashMessenger.SUCCESS)
                            .add("Pedido alterado com sucesso!");

                redirect("pedido/visualizar/id/" + pedido.getId());
            }
        }

        cidades = modelCidade.findCidadeByEstado(String.valueOf(pedido.getEndereco().getEstadoId()));

        getRequest().setAttribute("pedido", pedido);
        getRequest().setAttribute("estados", estados);
        getRequest().setAttribute("cidades", cidades);
        getRequest().setAttribute("statusPedido", statusPedido);
    }

    public void buscarAction() throws Exception
    {
        ParamRequestManager params = this.postFilter();
        SimpleDateFormat df = new SimpleDateFormat("yyy-MM-dd");
        Pedido modelPedido = new Pedido();
        getSession().setAttribute("session_pedido", params);
        List<TbPedido> pedidos = modelPedido.findByRequestManager(params);
        String data = "";
        boolean isFiltro = false;

        if (pedidos.size() > 0) {
            if (
                (params.hasParam("id") && !params.getParam("id").equals("")) ||
                (params.hasParam("nome") && !params.getParam("nome").equals("")) ||
                (params.hasParam("status_id") && !params.getParam("status_id").equals("0")) ||
                (params.hasParam("cep_or_destinatario") && !params.getParam("cep_or_destinatario").equals(""))
            ) {
                data = df.format(pedidos.get(0).getDataPrevisaoInstalacao());
                isFiltro = true;
            }
        }

        if (params.hasParam("data_filtro") && !params.getParam("data_filtro").equals("")) {
            String dataValidate = Util.dataBrToEn(params.getParam("data_filtro"));
            if (!dataValidate.equals("")) {
                data = dataValidate;
                isFiltro = true;

                Date date = df.parse(dataValidate);
                Date compare = new Date();
                if (date.getMonth() == compare.getMonth() && date.getYear() == compare.getYear()) {
                    data = df.format(compare.getTime());

                }
            }
        }

        if (!isFiltro && params.hasParam("data_ini") && !params.getParam("data_ini").equals("")) {
            String dataIni = Util.dataBrToEn(params.getParam("data_ini"));

            if (!dataIni.equals("")) {
                data = dataIni;
                isFiltro = true;
            }
        }

        if (!isFiltro) {
            data = df.format(new Date());
        }

        PedidoJsonModel jsonPedido = new PedidoJsonModel();
        jsonPedido.setList(pedidos);
        jsonPedido.setDate(data);

        setRequestXhtmlHttpRequest(jsonPedido);
    }

    public void statusAction() throws Exception
    {
        int id = Integer.parseInt(this.getParamOr("id", "0"));
        int status = Integer.parseInt(this.getParamOr("status", "0"));

        Pedido modelPedido = new Pedido();
        Parametro modelParametros = new Parametro();
        StatusPedido statusPedidoModel = new StatusPedido();
        LogPedido logPedido = new LogPedido();
        JsonModel jsonModel = new JsonModel();
        SendEmailService emailService = new SendEmailService(getRequest(), getResponse());

        if (status > 0 && id > 0) {
            TbPedido pedido = modelPedido.findPedidoVisualizacao(id);
            TbParametro parametro = modelParametros.findAll();
            int usuarioId = ((TbUsuario) getSession().getAttribute("usuario")).getId();

            emailService.setSubject(parametro.getSubject());
            emailService.setTo(pedido.getUsuario().getEmail());
            emailService.setFrom(parametro.getTo());
            emailService.setHost(parametro.getHost());

            /**
            * http://commons.apache.org/proper/commons-io/
            * https://github.com/leemunroe/responsive-html-email-template
             */
            String str = IOUtils.toString(new FileReader("/WEB-INF/view/pedido/mail-status.jsp"), "utf-8");
            str.replaceAll("{{nome_usuario}}", pedido.getUsuario().getNome());
            str.replaceAll("{{id_pedido}}", pedido.getId());
            str.replaceAll("{{endereco}}", parametro.getLogradouro());
            str.replaceAll("{{numero}}", parametro.getNumero());
            str.replaceAll("{{cidade}}", parametro.getCidade());
            str.replaceAll("{{uf}}", parametro.getUf());
            
            switch (status) {
                case TbStatusPedido.PEDIDO_PENDENTE:
                case TbStatusPedido.ORCAMENTO_PENDENTE:
                case TbStatusPedido.PRODUCAO:
                    TbStatusPedido statusPedido = statusPedidoModel.findById(status);
                    str.replaceAll("{{status_pedido}}", statusPedido.getNome());

                    emailService.setHtml(str);
                    emailService.send();
                    pedido.setStatusPedidoId(status);
                    modelPedido.update(pedido);
                    logPedido.insert(status, usuarioId, pedido.getId());
                break;
                case TbStatusPedido.PEDIDO_PAGO:                    
                    pedido.setStatusPedidoId(status);
                    // @TODO gerar nota fiscal
                    modelPedido.update(pedido);
                    logPedido.insert(status, usuarioId, pedido.getId());

                    if (pedido.getStatusPedidoId() != TbStatusPedido.PRODUCAO) {
                        status = TbStatusPedido.PRODUCAO;
                        pedido.setStatusPedidoId(TbStatusPedido.PRODUCAO);
                        modelPedido.update(pedido);
                        logPedido.insert(TbStatusPedido.PRODUCAO, usuarioId, pedido.getId());
                    }

                    TbStatusPedido statusPedido = statusPedidoModel.findById(status);
                    str.replaceAll("{{status_pedido}}", statusPedido.getNome());
                    emailService.setHtml(str);
                    emailService.send();
                break;
                case TbStatusPedido.INSTALACAO:
                    TbStatusPedido statusPedido = statusPedidoModel.findById(status);
                    str.replaceAll("{{status_pedido}}", statusPedido.getNome());

                    pedido.setStatusPedidoId(status);
                    emailService.setHtml(str);
                    emailService.send();
                    modelPedido.update(pedido);
                    logPedido.insert(status, usuarioId, pedido.getId());
                break;
                case TbStatusPedido.FINALIZADO:
                    pedido.setStatusPedidoId(status);
                    modelPedido.update(pedido);
                    logPedido.insert(status, usuarioId, pedido.getId());
                break;
            }

            jsonModel.setStatus(true);
            jsonModel.setMessage("Status do pedido alterado com sucesso!");
        } else {
            jsonModel.setStatus(false);
            jsonModel.setMessage("Pedido ou status informado inválido. Verifique!");
        }

        setRequestXhtmlHttpRequest(jsonModel);
    }

    public void cancelarAction() throws Exception
    {
        int id = Integer.parseInt(this.getParamOr("id", "0"));

        Pedido modelPedido = new Pedido();
        Parametro modelParametros = new Parametro();
        LogPedido logPedido = new LogPedido();
        JsonModel jsonModel = new JsonModel();
        if (id > 0) {
            TbPedido pedido = modelPedido.findPedidoVisualizacao(id);
            int usuarioId = ((TbUsuario) getSession().getAttribute("usuario")).getId();

            pedido.setStatusPedidoId(TbStatusPedido.CANCELADO);
            logPedido.insert(pedido.getStatusPedidoId(), pedido.getUsuario().getId(), pedido.getId());

            SendEmailService emailService = new SendEmailService(getRequest(), getResponse());
            TbParametro parametro = modelParametros.findAll();

            String str = IOUtils.toString(new FileReader("/WEB-INF/view/pedido/mail-status.jsp"), "utf-8");
            str.replaceAll("{{nome_usuario}}", pedido.getUsuario().getNome());
            str.replaceAll("{{id_pedido}}", pedido.getId());
            str.replaceAll("{{endereco}}", parametro.getLogradouro());
            str.replaceAll("{{numero}}", parametro.getNumero());
            str.replaceAll("{{cidade}}", parametro.getCidade());
            str.replaceAll("{{uf}}", parametro.getUf());
            str.replaceAll("{{status_pedido}}", "Cancelado");            

            emailService.setSubject(parametro.getSubject());
            emailService.setTo(pedido.getUsuario().getEmail());
            emailService.setFrom(parametro.getTo());
            emailService.setHost(parametro.getHost());
            emailService.setHtml(str);
            emailService.send();

            jsonModel.setStatus(true);
            jsonModel.setMessage("Pedido cancelado com sucesso!");
        } else {
            jsonModel.setStatus(false);
            jsonModel.setMessage("Pedido informado inválido. Verifique!");
        }

        setRequestXhtmlHttpRequest(jsonModel);
    }
}
