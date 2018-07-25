package br.com.estruturart.controller;

import java.io.File;
import java.io.FileInputStream;
import java.io.OutputStream;

import javax.servlet.ServletContext;
import javax.servlet.annotation.WebServlet;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import br.com.estruturart.utility.JsonModel;
import br.com.estruturart.persistency.Cidade;
import br.com.estruturart.model.CepModel;
import com.google.gson.Gson;
import java.net.URL;

@WebServlet(name = "webservice", urlPatterns = { "/find-cep" })
public class WebserviceController extends AbstractServlet {
    private static final long serialVersionUID = -4214231788197597839L;

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
