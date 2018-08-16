package br.com.estruturart.controller;

import javax.servlet.annotation.WebServlet;
import javax.ws.rs.HttpMethod;

import br.com.estruturart.model.TbParametro;
import br.com.estruturart.persistency.Parametro;
import br.com.estruturart.utility.FlashMessenger;

/**
 * Servlet implementation class Parametro
 */
@WebServlet(name = "parametro", urlPatterns = { "/parametro" })
public class ParametroController extends AbstractServlet
{
    private static final long serialVersionUID = -4111231788151587849L;

    public void indexAction() throws Exception
    {
        Parametro modelParametro = new Parametro();
        TbParametro parametro = modelParametro.findParametros();
        
        if (this.getMethod().equals(HttpMethod.POST)) {
            parametro.setTalCoisa();

            modelParametro.update(parametro);
            getFlashMessenger().setType(FlashMessenger.SUCCESS).add("Parametro salvo com sucesso!");
            redirect("parametro");
        }
        this.getRequest().setAttribute("parametro", parametro);
    }
}

