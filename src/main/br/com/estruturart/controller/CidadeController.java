package br.com.estruturart.controller;

import javax.servlet.ServletContext;
import javax.servlet.annotation.WebServlet;
import br.com.estruturart.utility.JsonModel;
import br.com.estruturart.persistency.Cidade;

@WebServlet(name = "cidade", urlPatterns = { "/find-cidade" })
public class CidadeController extends AbstractServlet {
    private static final long serialVersionUID = -4214245788197597839L;

    public void findCidadeAction() throws Exception {
        setNoRender(true);
        String estadoId = getRequest().getParameter("estado_id");

        JsonModel jsonModel = new JsonModel();
        Cidade modelCidade = new Cidade();

        jsonModel.setList(modelCidade.findCidadeByEstado(estadoId));
        setRequestXhtmlHttpRequest(jsonModel);
    }
}
