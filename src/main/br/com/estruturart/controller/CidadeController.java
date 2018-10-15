package br.com.estruturart.controller;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;

import javax.servlet.ServletContext;
import javax.servlet.annotation.WebServlet;

import com.google.gson.Gson;

import br.com.estruturart.utility.JsonModel;
import br.com.estruturart.persistency.Cidade;
import br.com.estruturart.model.CepModel;

@WebServlet(name = "cidade", urlPatterns = { "/find-cidade", "/find-cep" })
public class CidadeController extends AbstractServlet {
    private static final long serialVersionUID = -4214245788197597839L;

    public void findCidadeAction() throws Exception {
        setNoRender(true);
        String estadoId = getRequest().getParameter("estado_id");

        JsonModel jsonModel = new JsonModel();
        Cidade modelCidade = new Cidade();

        jsonModel.setList(modelCidade.findCidadeByEstado(estadoId, true));
        setRequestXhtmlHttpRequest(jsonModel);
    }

    public void findCepAction() throws Exception {
        setNoRender(true);
        String cep = getRequest().getParameter("cep");

        BufferedReader reader;
        JsonModel jsonModel = new JsonModel();
        Cidade modelCidade = new Cidade();
        try {
            URL url = new URL(String.format("http://viacep.com.br/ws/%s/json", cep));
            reader = new BufferedReader(new InputStreamReader(url.openStream(), "UTF-8"));
            String jsonStr = "";
            for (String line; (line = reader.readLine()) != null; ) {
                jsonStr += line;
            }
            reader.close();

            Gson gson = new Gson();
            CepModel cepModel = gson.fromJson(jsonStr, CepModel.class);

            cepModel.setCidade(modelCidade.findCidadeByName(cepModel.getLocalidade()));
            jsonModel.setObject(cepModel);
            // buscar cidade e estado
        } catch (IOException e) {
            jsonModel.setStatus(false);
            jsonModel.setMessage(e.getMessage());
        }

        setRequestXhtmlHttpRequest(jsonModel);
    }
}
