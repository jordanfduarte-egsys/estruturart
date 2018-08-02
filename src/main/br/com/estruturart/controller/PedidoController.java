package br.com.estruturart.controller;

import javax.servlet.annotation.WebServlet;
import javax.ws.rs.HttpMethod;
import br.com.estruturart.model.TbPedido;
import br.com.estruturart.model.TbStatusPedido;
import br.com.estruturart.utility.ParamRequestManager;
import br.com.estruturart.utility.PedidoJsonModel;
import br.com.estruturart.utility.Util;
import br.com.estruturart.persistency.Pedido;
import br.com.estruturart.persistency.StatusPedido;
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

        List<TbStatusPedido> statusPedido = statusPedidoModel.findAll();
        TbPedido pedido = pedidoModel.findPedidoVisualizacao(id);

        getRequest().setAttribute("pedido", pedido);
        getRequest().setAttribute("statusPedido", statusPedido);
    }

    public void editarAction() throws Exception
    {
        //this.getRoute().setAction("cadastroAction");
        //this.cadastroAction();
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
}
