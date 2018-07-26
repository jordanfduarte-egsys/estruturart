package br.com.estruturart.controller;

import javax.servlet.annotation.WebServlet;
import javax.ws.rs.HttpMethod;
import br.com.estruturart.model.TbPedido;

/**
 * Servlet implementation class Pedido
 */
@WebServlet(name = "pedido", urlPatterns = { "/pedido", "/pedido/cadastro", "/pedido/index/page/*", "pedido/editar/id/*"})
public class PedidoController extends AbstractServlet
{
    private static final long serialVersionUID = -4219231788151597849L;

    public void indexAction() throws Exception
    {

    }

    public void cadastroAction() throws Exception
    {

    }

    public void editarAction() throws Exception
    {
        this.getRoute().setAction("cadastroAction");
        this.cadastroAction();
    }
}
