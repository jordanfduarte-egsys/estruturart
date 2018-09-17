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
import br.com.estruturart.persistency.Usuario;
import br.com.estruturart.model.CepModel;
import br.com.estruturart.persistency.Estado;
import br.com.estruturart.model.TbUsuario;
import br.com.estruturart.model.TbEstado;
import java.util.List;
import com.google.gson.Gson;
import java.net.URL;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

@WebServlet(name = "webservice", urlPatterns = { "/find-cep", "/find-usuario" })
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

    public void findUsuarioAction() throws Exception
    {
        setNoRender(true);
        String usuarioStr = getRequest().getParameter("email");
        String senhaStr = getRequest().getParameter("senha");

        Map m= getRequest().getParameterMap();
        Set s = m.entrySet();
        Iterator it = s.iterator();

        while(it.hasNext()){
            Map.Entry<String,String[]> entry = (Map.Entry<String,String[]>)it.next();

            String key             = entry.getKey();
            String[] value         = entry.getValue();

            System.out.println("Key is "+key+"<br>");
            if(value.length>1){
                for (int i = 0; i < value.length; i++) {
                    System.out.println("<li>" + value[i].toString() + "</li><br>");
                }
            } else {
                System.out.println("Value is "+value[0].toString()+"<br>");
            }

            System.out.println("-------------------<br>");
        }

        TbUsuario usuarioEntity = new TbUsuario();
        Usuario usuario = new Usuario();

        System.out.println("Usuario: "  + usuarioStr);
        System.out.println("Senha: "  + senhaStr);
        try {
            usuarioEntity = usuario.findUsuarioByUsuarioSenha(usuarioStr, senhaStr);
        } catch (Exception e) {
            usuarioEntity.setMessage(e.getMessage());
        }

        setRequestXhtmlHttpRequestModel(usuarioEntity);
    }

    public void findCepObjectAction() throws Exception
    {
        setNoRender(true);
        String cep = getRequest().getParameter("cep");

        BufferedReader reader;
        Cidade modelCidade = new Cidade();
        CepModel cepModel = new CepModel();
        try {
            URL url = new URL(String.format("http://viacep.com.br/ws/%s/json", cep));
            reader = new BufferedReader(new InputStreamReader(url.openStream(), "UTF-8"));
            String jsonStr = "";
            for (String line; (line = reader.readLine()) != null; ) {
                jsonStr += line;
            }
            reader.close();

            Gson gson = new Gson();
            cepModel = gson.fromJson(jsonStr, CepModel.class);
            cepModel.setCidade(modelCidade.findCidadeByName(cepModel.getLocalidade()));

            if (cepModel.getCep().equals(cep)) {
                cepModel.setId(1);
            }
        } catch (IOException e) {
            cepModel.setMessage(e.getMessage());
        }

        setRequestXhtmlHttpRequestModel(cepModel);
    }

    public void findEstadosAction() throws Exception
    {
        setNoRender(true);
        Estado modelEstado = new Estado();
        setRequestXhtmlHttpRequestList(modelEstado.findEstados());
    }

    public void findCidadesAction() throws Exception
    {
        setNoRender(true);
        String estadoId = getRequest().getParameter("estado_id");
        Cidade modelCidade = new Cidade();
        setRequestXhtmlHttpRequestList(modelCidade.findCidadeByEstado(estadoId));
    }

    public void findCpfCnpjAction() throws Exception
    {
        setNoRender(true);
        Usuario usuario = new Usuario();
        setRequestXhtmlHttpRequestModel(usuario.findUsuarioByCpjCNpj(
            this.getRequest().getParameter("cpf_cnpj")
        ));
    }
}
