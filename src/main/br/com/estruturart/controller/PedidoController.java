package br.com.estruturart.controller;

import javax.servlet.annotation.WebServlet;
import javax.ws.rs.HttpMethod;
import br.com.estruturart.model.TbPedido;
import br.com.estruturart.utility.ParamRequestManager;
import br.com.estruturart.utility.PedidoJsonModel;
import br.com.estruturart.utility.Util;
import br.com.estruturart.persistency.Pedido;
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
        this.getRequest().setAttribute("isFiltroAvancado", isFiltroAvancado);
    }

    public void cadastroAction() throws Exception
    {

    }

    public void editarAction() throws Exception
    {
        this.getRoute().setAction("cadastroAction");
        this.cadastroAction();
    }

    public void buscarAction() throws Exception
    {
        ParamRequestManager params = this.postFilter();
        Pedido modelPedido = new Pedido();
        getSession().setAttribute("session_pedido", params);
        List<TbPedido> pedidos = modelPedido.findByRequestManager(params);
        String data = "";
        boolean isFiltro = false;

        if (params.hasParam("data_ini") && params.hasParam("data_fim") && !params.getParam("data_ini").equals("") && !params.getParam("data_fim").equals("")) {
            String dataIni = Util.dataBrToEn(params.getParam("data_ini"));
            String dataFim = Util.dataBrToEn(params.getParam("data_fim"));

            if (!dataIni.equals("") && !dataFim.equals("")) {
                data = dataIni;
                isFiltro = true;
            }
        }

        if (!isFiltro && params.hasParam("data_filtro") && !params.getParam("data_filtro").equals("")) {
            String dataValidate = Util.dataBrToEn(params.getParam("data_filtro"));
            if (!data.equals("")) {
                data = dataValidate;
                isFiltro = true;
            }
        }

        if (!isFiltro) {
            Date d1 = new Date();
            SimpleDateFormat df = new SimpleDateFormat("yyy-MM-dd");
            Calendar c = Calendar.getInstance();

            c.set(Calendar.DAY_OF_MONTH, c.getActualMinimum(Calendar.DAY_OF_MONTH));
            d1 = c.getTime();
            data = df.format(d1);
        }

        PedidoJsonModel jsonPedido = new PedidoJsonModel();
        jsonPedido.setList(pedidos);
        jsonPedido.setDate(data);

        setRequestXhtmlHttpRequest(jsonPedido);
    }
}
