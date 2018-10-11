package br.com.estruturart.controller;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.OutputStream;

import javax.servlet.ServletContext;
import javax.servlet.annotation.WebServlet;
import javax.ws.rs.HttpMethod;
import javax.xml.bind.DatatypeConverter;
import br.com.estruturart.service.FinalizarOrcamento;
import java.util.Date;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import br.com.estruturart.utility.JsonModel;
import br.com.estruturart.persistency.Cidade;
import br.com.estruturart.persistency.PedidoItemFoto;
import br.com.estruturart.persistency.Usuario;
import br.com.estruturart.persistency.Modelo;
import br.com.estruturart.model.CepModel;
import br.com.estruturart.persistency.Estado;
import br.com.estruturart.persistency.Pedido;
import br.com.estruturart.model.TbUsuario;
import br.com.estruturart.model.TbEstado;
import br.com.estruturart.model.TbModelo;
import br.com.estruturart.model.TbPedido;
import br.com.estruturart.utility.ParamRequestManager;
import br.com.estruturart.model.Orcamento;
import br.com.estruturart.utility.GsonDeserializeExclusion;
import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.disk.DiskFileItem;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.io.IOUtils;

import br.com.estruturart.model.TbPedidoItem;
import br.com.estruturart.model.TbPedidoItemFoto;
import br.com.estruturart.persistency.PedidoItem;
import br.com.estruturart.persistency.PedidoItemFoto;
import br.com.estruturart.service.UploadService;
import java.util.List;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.sql.Connection;
import br.com.estruturart.persistency.ConnectionManager;

import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Base64;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

@WebServlet(name = "webservice", urlPatterns = { "/find-cep", "/find-usuario", "/find-cep-object",
    "/find-estados", "/find-cidades", "/find-cpf-cnpj", "/buscar-modelo", "/salvar-orcamento", "/find-pedidos", "/detalhe-pedido", "/nova-foto-camera-item", "/find-imagem" })
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
System.out.println("CEP: " + cep);
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

            if (cepModel.getCep().replaceAll("-", "").equals(cep)) {
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
        setRequestXhtmlHttpRequestList(modelCidade.findCidadeByEstado(estadoId, false));
    }

    public void findCpfCnpjAction() throws Exception
    {
        setNoRender(true);
        Usuario usuario = new Usuario();
        setRequestXhtmlHttpRequestModel(usuario.findUsuarioByCpjCNpj(
            this.getRequest().getParameter("cpf_cnpj")
        ));
    }

    public void buscarModeloAction() throws Exception
    {
        setNoRender(true);
        Modelo modeloModel = new Modelo();
        List<TbModelo> list = modeloModel.findModeloByNomeList(
            this.getParameterOrValue("nome", ""), this.getParameterOrValue("id", "0")
        );

        String separator = File.separator;
        for (TbModelo modelo : list) {
            String path = separator + "files" + separator + "modelos" + separator + modelo.getImagem();
            ServletContext cntx = this.getRequest().getServletContext();
            String sourceFilder = getServletContext().getInitParameter("folderUpload");

            String filename = sourceFilder + path.replace( separator + "files", "");
            String mime = cntx.getMimeType(filename);
            System.out.println("MIME " + mime);
            if (mime == null) {
                filename = cntx.getRealPath(separator + "files" + separator + "sem-foto.jpg");
                mime = cntx.getMimeType(filename);
            }

            System.out.println("LINK" + filename);
            getResponse().setContentType(mime);
            File file = new File(filename);
            if (!file.exists()) continue;
            FileInputStream in = new FileInputStream(file);

            byte imageData[] = new byte[(int) file.length()];
            in.read(imageData);
            modelo.setBase64Image(Base64.getEncoder().encodeToString(imageData));
        }

        setRequestXhtmlHttpRequestList(list);
    }

    public void salvarOrcamentoAction() throws Exception
    {
        String message = "";
        boolean status = false;
        Integer id = 0;
        if (this.getMethod().equals(HttpMethod.POST)) {
            System.out.println("\n\nJSON: "  + getRequest().getParameter("orcamento"));
            System.out.println("\n\nISORCAMENTO: "  + getParameterOrValue("is_orcamento", "0").equals("0"));

            Gson gson = new GsonBuilder()
                .addDeserializationExclusionStrategy(new GsonDeserializeExclusion())
                .setDateFormat("MMM d, yyyy")
                .create();

            Orcamento orcamento = (Orcamento)gson.fromJson(getRequest().getParameter("orcamento"), Orcamento.class);




            Connection conn = ConnectionManager.getConnection();
            conn.setAutoCommit(false);

            try {
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
                    message = "Orçamento criado com sucesso!";
                } else {
                    message = "Pedido criado com sucesso!";
                }

                status = true;
                id = finalizarService.getId();
            } catch (java.sql.SQLException e) {
                conn.rollback();
                conn.close();
                getLogErrorService().createLog(e);
                message = "Ocorreu um erro ao criar o orçamento. Verifique!";
            } catch (Exception e) {
                conn.rollback();
                conn.close();
                getLogErrorService().createLog(e);
                message = "Ocorreu um erro ao criar o orçamento. Verifique!";
            }
        }

        JsonModel jsonModel = new JsonModel();
        jsonModel.setId(id);
        jsonModel.setMessage(message);
        jsonModel.setStatus(status);

        setRequestXhtmlHttpRequest(jsonModel);
    }

    public void findPedidosAction() throws Exception
    {
        ParamRequestManager params = this.postFilter();
        Pedido modelPedido = new Pedido();
        List<TbPedido> pedidos = modelPedido.findByRequestManager(params, "data");
        setRequestXhtmlHttpRequestList(pedidos);
    }

    public void detalhePedidoAction() throws Exception
    {
        int id = Integer.parseInt(this.getRequest().getParameter("id"));
        Pedido pedidoModel = new Pedido();
        TbPedido pedido = pedidoModel.findPedidoVisualizacao(id);

        System.out.println("\n\n\n PEDIDO");

        Gson gson = new Gson();
        System.out.println(gson.toJson(pedido));
        System.out.println("\n\n\n PEDIDO ");

        setRequestXhtmlHttpRequestModel(pedido);
    }

    public void novaFotoCameraItemAction() throws Exception
    {
        int idItem = Integer.parseInt(getRequest().getParameter("id"));
        String imageBase64 = getRequest().getParameter("base64");
        String extension = getRequest().getParameter("format");
        String obs = getRequest().getParameter("observacao");

        PedidoItem pedidoItem = new PedidoItem();
        TbPedidoItem item = pedidoItem.findPedidoByItem(idItem);
        JsonModel jsonModel = new JsonModel();

        if (item.getId() > 0) {
            String contextType = "image/jpg";
            switch (extension) {
                case "jpg":
                    contextType = "image/jpg";
                    break;
                case "jpeg":
                    contextType = "image/jpeg";
                    break;
                default:
                    contextType = "image/png";
                    break;
            }

            System.out.println("\n\nID: " + idItem);
            System.out.println("EXT: " + extension);
            System.out.println("OBS: " + obs);

            String sourceFilder = getServletContext().getInitParameter("folderUpload");
            int widthModelo = Integer.parseInt(getServletContext().getInitParameter("widthModelo"));
            int heigthModelo = Integer.parseInt(getServletContext().getInitParameter("heigthModelo"));
            String extensoes = getServletContext().getInitParameter("extensoesImagem");
            String separator = File.separator;
            String path = sourceFilder + separator + String.valueOf(new Date().getTime()) + "." + extension;
            File file = new File(path);
            file.setReadable(true, false);
            file.setExecutable(true, false);
            file.setWritable(true, false);
            try {
                org.apache.commons.codec.binary.Base32 b2 = new org.apache.commons.codec.binary.Base32();
                byte[] data = b2.decode(imageBase64);
                FileOutputStream out = new FileOutputStream(file);
                OutputStream outputStream = new BufferedOutputStream(
                    out
                );

                outputStream.write(data);
                outputStream.flush();
                outputStream.close();

                out.flush();
                out.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            FileInputStream inpuStream = new FileInputStream(file);
            UploadService uploadService = new UploadService(this.getRequest());
            uploadService.setExtensoes(extensoes.split(","));
            uploadService.setFolder(separator + "item" + separator + item.getId() + separator);
            System.out.println("NOME ANTIGO: " + file.getName());
            InputStream fileContent = uploadService.redimencionar(inpuStream, 800, 600);
            String imagem = uploadService.savePosRedimencionamento(
                file.getName(),
                extension,
                sourceFilder,
                null,
                fileContent
            );
            inpuStream.close();
            fileContent.close();
            PedidoItemFoto pedidoItemFoto = new PedidoItemFoto();
            TbPedidoItemFoto itemFoto = new TbPedidoItemFoto();
            if (!imagem.equals("")) {
                itemFoto.setCaminhoArquivo(imagem);
                itemFoto.setObservacao(obs);
                itemFoto.setPedidoItensId(item.getId());
                pedidoItemFoto.insert(itemFoto);

                jsonModel.setMessage("Foto enviada com sucesso!");
                jsonModel.setStatus(true);

                System.out.println("CAMINHO : " + path);
                File file2 = new File(path);

                file2.setReadable(true, false);
                file2.setExecutable(true, false);
                file2.setWritable(true, false);
                boolean isDeleted = file2.delete();
                System.out.println("REMOVIDO : " + isDeleted);
            } else {
                jsonModel.setMessage("Ocorreu um erro ao enviar a foto!");
                jsonModel.setStatus(false);
            }
        } else {
            jsonModel.setMessage("Item não encontrado!");
            jsonModel.setStatus(false);
        }

        setRequestXhtmlHttpRequest(jsonModel);
    }

    public void findImagemAction() throws Exception
    {
        int idItem = Integer.parseInt(getRequest().getParameter("id"));
        int idUltimaFoto = Integer.parseInt(getRequest().getParameter("idmaisquatro"));
        PedidoItemFoto modelFoto = new PedidoItemFoto();

        List<TbPedidoItemFoto> fotos = modelFoto.findFotos(idItem, idUltimaFoto);
        String separator = File.separator;
        ServletContext cntx = this.getRequest().getServletContext();
        String sourceFilder = getServletContext().getInitParameter("folderUpload");

        for (TbPedidoItemFoto foto : fotos) {
            String path = separator + "item" + separator + foto.getPedidoItensId() + separator + foto.getCaminhoArquivo();

            String filename = sourceFilder + path;
            String mime = cntx.getMimeType(filename);
            System.out.println("MIME " + mime);
            if (mime == null) {
                fotos.remove(foto);
                continue;
            }

            System.out.println("LINK" + filename);
            getResponse().setContentType(mime);
            File file = new File(filename);
            if (!file.exists()) {
                fotos.remove(foto);
                continue;
            }

            FileInputStream in = new FileInputStream(file);

            byte imageData[] = new byte[(int) file.length()];
            in.read(imageData);
            foto.setBase64Imagem(Base64.getEncoder().encodeToString(imageData));
        }
        setRequestXhtmlHttpRequestList(fotos);
    }
}
