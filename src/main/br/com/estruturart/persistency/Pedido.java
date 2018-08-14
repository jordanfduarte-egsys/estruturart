package br.com.estruturart.persistency;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import com.mysql.jdbc.Statement;
import br.com.estruturart.model.TbPedido;
import br.com.estruturart.model.TbUsuario;
import br.com.estruturart.model.TbEndereco;
import br.com.estruturart.model.TbEstado;
import br.com.estruturart.model.TbCidade;
import br.com.estruturart.model.TbStatusPedido;
import br.com.estruturart.utility.Util;
import br.com.estruturart.utility.ParamRequestManager;
import br.com.estruturart.persistency.PedidoItem;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Calendar;

public class Pedido extends AbstractPersistency
{
    public int insert(TbPedido pedido) throws SQLException
    {
        Connection conn = null;
        if (isConnection()) {
            conn = getConnection();
        } else {
            conn = ConnectionManager.getConnection();
        }

        SimpleDateFormat df = new SimpleDateFormat("yyy-MM-dd");
        String sql = String.format(
            "INSERT INTO PEDIDO (caminho_arquivo_nota_fiscal, data_previsao_instalacao, valor_total, valor_mao_obra, pedido_pago, observacao, usuario_id, status_pedido_id, desconto_geral)"
            + " VALUES ('%s', '%s', '%s', '%s', %d, '%s', %d, %d, '%s')",
            pedido.getCaminhoArquivoNotaFiscal(), df.format(pedido.getDataPrevisaoInstalacao().getTime()), pedido.getValorTotal(), pedido.getValorMaoObra(),
            pedido.getPedidoPago(), pedido.getObservacao(), pedido.getUsuarioId(), pedido.getStatusPedidoId(),
            pedido.getDescontoGeral()
        );

        PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
        ps.execute();
        System.out.println(sql);

        ResultSet rs = ps.getGeneratedKeys();
        pedido.setId(rs.next() ? rs.getInt(1) : 0);

        if (!isConnection()) {
            conn.close();
        }

        return pedido.getId();
    }

    public void update(TbPedido pedido) throws SQLException
    {
        Connection conn = null;
        if (isConnection()) {
            conn = getConnection();
        } else {
            conn = ConnectionManager.getConnection();
        }

        SimpleDateFormat df = new SimpleDateFormat("yyy-MM-dd");
        String sql = String.format(
            "UPDATE PEDIDO SET data_previsao_instalacao = '%s', valor_total = '%s', observacao = '%s', desconto_geral = '%s', status_pedido_id = %d"
            + " WHERE id = %d",
            df.format(pedido.getDataPrevisaoInstalacao().getTime()), pedido.getValorTotal(), pedido.getObservacao(),
            pedido.getDescontoGeral(), pedido.getStatusPedidoId(), pedido.getId()
        );

        PreparedStatement ps = conn.prepareStatement(sql);
        ps.execute();
        System.out.println(sql);

        if (!isConnection()) {
            conn.close();
        }
    }

    public List<TbPedido> findByRequestManager(ParamRequestManager params) throws SQLException, java.text.ParseException
    {
        Connection conn = ConnectionManager.getConnection();
        SimpleDateFormat df = new SimpleDateFormat("yyy-MM-dd");
        boolean isFiltro = false;
        boolean filtroData = false;
        String args = "";
        if (params.hasParam("id") && !params.getParam("id").equals("")) {
            args += " AND p.id = '" + params.getParam("id") + "' ";
            isFiltro = true;
        }

        if (params.hasParam("nome") && !params.getParam("nome").equals("")) {
            args += " AND (u.nome LIKE '" + params.getParam("nome") + "%' OR u.email LIKE '%" + params.getParam("nome") + "%') ";
            isFiltro = true;
        }

        if (params.hasParam("status_id") && !params.getParam("status_id").equals("0")) {
            args += " AND p.status_pedido_id = '" + params.getParam("status_id") + "' ";
            isFiltro = true;
        }

        if (params.hasParam("cep_or_destinatario") && !params.getParam("cep_or_destinatario").equals("")) {
            args += " AND (e.cep LIKE '" + params.getParam("cep_or_destinatario") + "%' OR e.logradouro LIKE '" + params.getParam("cep_or_destinatario") + "%') ";
            isFiltro = true;
        }

        if (params.hasParam("data_filtro") && !params.getParam("data_filtro").equals("")) {
            String dataStr = Util.dataBrToEn(params.getParam("data_filtro"));
            if (!dataStr.equals("")) {
                System.out.println("DATA1: " + dataStr);
                Date date = df.parse(dataStr);
                Calendar c = Calendar.getInstance();
                c.setTime(date);
                c.set(Calendar.DAY_OF_MONTH, c.getActualMinimum(Calendar.DAY_OF_MONTH));
                Date d1 = c.getTime();
                c.set(Calendar.DAY_OF_MONTH, c.getActualMaximum(Calendar.DAY_OF_MONTH));
                Date d2 = c.getTime();
                args += " AND (p.data_previsao_instalacao BETWEEN '" + df.format(d1) + " 00:00'  AND '" + df.format(d2) + " 23:59') ";
                isFiltro = filtroData = true;
            }
        }

        System.out.println("DATA INI: " + params.hasParam("data_ini"));
        if (!filtroData && params.hasParam("data_ini") && !params.getParam("data_ini").equals("")) {
            String dataIni = Util.dataBrToEn(params.getParam("data_ini"));
            System.out.println("DATA INI: " + dataIni);
            if (!dataIni.equals("")) {
                Date date = df.parse(dataIni);
                Calendar c = Calendar.getInstance();
                c.setTime(date);
                Date d1 = c.getTime();
                c.set(Calendar.DAY_OF_MONTH, c.getActualMaximum(Calendar.DAY_OF_MONTH));
                Date d2 = c.getTime();
                args += " AND (p.data_previsao_instalacao BETWEEN '" + df.format(d1) + " 00:00'  AND '" + df.format(d2) + " 23:59') ";
                isFiltro = filtroData = true;
            }
        }

        if (!filtroData) {
            Date d1 = new Date();
            Date d2 = new Date();
            Calendar c = Calendar.getInstance();

            c.set(Calendar.DAY_OF_MONTH, c.getActualMinimum(Calendar.DAY_OF_MONTH));
            d1 = c.getTime();
            c.set(Calendar.DAY_OF_MONTH, c.getActualMaximum(Calendar.DAY_OF_MONTH));
            d2 = c.getTime();
            args += " AND (p.data_previsao_instalacao BETWEEN '" + df.format(d1) + " 00:00'  AND '" + df.format(d2) + " 23:59') ";
        }

        String sql = String.format(
            "SELECT p.*, e.cep, e.logradouro, e.bairro, e.numero, e.complemento, e.cidade_id, e.usuario_id, e.id as endereco_id, u.nome as nome_usuario "
            + " FROM PEDIDO p INNER JOIN endereco e ON p.id = e.pedido_id "
            + " INNER JOIN USUARIO u ON p.usuario_id = u.id "
            + " WHERE 1 %s",
            args
        );
        System.out.println("-----------------");
        System.out.println(sql);
        System.out.println("-----------------");
        PreparedStatement ps = conn.prepareStatement(sql);
        ResultSet rs = ps.executeQuery();
        List<TbPedido> pedidos = new ArrayList<TbPedido>();
        while (rs.next()) {
            TbPedido pedido = new TbPedido();
            TbEndereco endereco = new TbEndereco();
            TbUsuario usuario = new TbUsuario();

            pedido.setId(rs.getInt("id"));
            pedido.setCaminhoArquivoNotaFiscal(rs.getString("caminho_arquivo_nota_fiscal"));
            pedido.setDataInclusao(rs.getDate("data_inclusao"));
            pedido.setDataPrevisaoInstalacao(rs.getDate("data_previsao_instalacao"));
            pedido.setValorTotal(rs.getFloat("valor_total"));
            pedido.setValorMaoObra(rs.getFloat("valor_mao_obra"));
            pedido.setPedidoPago(rs.getInt("pedido_pago"));
            pedido.setObservacao(rs.getString("observacao"));
            pedido.setUsuarioId(rs.getInt("usuario_id"));
            pedido.setStatusPedidoId(rs.getInt("status_pedido_id"));
            pedido.setDescontoGeral(rs.getFloat("desconto_geral"));

            endereco.setId(rs.getInt("endereco_id"));
            endereco.setCep(rs.getString("cep"));
            endereco.setLogradouro(rs.getString("logradouro"));
            endereco.setBairro(rs.getString("bairro"));
            endereco.setComplemento(rs.getString("complemento"));
            endereco.setCidadeId(rs.getInt("cidade_id"));
            endereco.setUsuarioId(rs.getInt("usuario_id"));

            usuario.setId(rs.getInt("usuario_id"));
            usuario.setNome(rs.getString("nome_usuario"));

            pedido.setEndereco(endereco);
            pedido.setUsuario(usuario);
            pedido.processarInfoListagem();
            pedidos.add(pedido);
        }

        conn.close();
        return pedidos;
    }

    public TbPedido findPedidoVisualizacao(int id) throws java.sql.SQLException
    {
        Connection conn = ConnectionManager.getConnection();

        String sql = String.format(
            "SELECT p.*, c.nome as nome_cliente, c.cpf_cnpj, c.tipo_pessoa, e.cep, e.logradouro, e.bairro, e.complemento, e.numero, sp.nome as nome_status_pedido, "
            + " cid.nome as nome_cidade, est.nome as nome_estado, est.uf, est.id as estado_id, e.id as endereco_id, e.cidade_id FROM pedido p "
            + " INNER JOIN status_pedido sp ON p.status_pedido_id = sp.id"
            + " INNER JOIN usuario c ON p.usuario_id = c.id"
            + " INNER JOIN endereco e ON e.pedido_id = p.id"
            + " INNER JOIN cidade cid ON e.cidade_id = cid.id"
            + " INNER JOIN estado est ON cid.estado_id = est.id"
            + " WHERE p.id = %d",
            id
        );

        System.out.println("-----------------");
        System.out.println(sql);
        System.out.println("-----------------");


        PreparedStatement ps = conn.prepareStatement(sql);
        ResultSet rs = ps.executeQuery();

        TbPedido pedido = new TbPedido();
        PedidoItem itemModel = new PedidoItem();
        if (rs.next()) {
            TbUsuario usuario = new TbUsuario();
            TbEndereco endereco = new TbEndereco();
            TbEstado estado = new TbEstado();
            TbCidade cidade = new TbCidade();
            TbStatusPedido statusPedido = new TbStatusPedido();

            pedido.setId(rs.getInt("id"));
            pedido.setCaminhoArquivoNotaFiscal(rs.getString("caminho_arquivo_nota_fiscal"));
            pedido.setDataInclusao(rs.getDate("data_inclusao"));
            pedido.setDataPrevisaoInstalacao(rs.getDate("data_previsao_instalacao"));
            pedido.setValorTotal(rs.getFloat("valor_total"));
            pedido.setValorMaoObra(rs.getFloat("valor_mao_obra"));
            pedido.setPedidoPago(rs.getInt("pedido_pago"));
            pedido.setObservacao(rs.getString("observacao"));
            pedido.setUsuarioId(rs.getInt("usuario_id"));
            pedido.setStatusPedidoId(rs.getInt("status_pedido_id"));
            pedido.setDescontoGeral(rs.getFloat("desconto_geral"));

            statusPedido.setId(rs.getInt("status_pedido_id"));
            statusPedido.setNome(rs.getString("nome_status_pedido"));

            endereco.setId(rs.getInt("endereco_id"));
            endereco.setCep(rs.getString("cep"));
            endereco.setLogradouro(rs.getString("logradouro"));
            endereco.setBairro(rs.getString("bairro"));
            endereco.setComplemento(rs.getString("complemento"));
            endereco.setCidadeId(rs.getInt("cidade_id"));
            endereco.setNumero(rs.getString("numero"));
            endereco.setUsuarioId(rs.getInt("usuario_id"));
            endereco.setEstadoId(rs.getInt("estado_id"));

            cidade.setNome(rs.getString("nome_cidade"));
            cidade.setUf(rs.getString("uf"));
            cidade.setId(rs.getInt("cidade_id"));

            estado.setNome(rs.getString("nome_estado"));
            estado.setUf(rs.getString("uf"));
            estado.setId(rs.getInt("estado_id"));

            usuario.setId(rs.getInt("usuario_id"));
            usuario.setNome(rs.getString("nome_cliente"));
            usuario.setTipoPessoa(rs.getString("tipo_pessoa"));
            usuario.setCpfCnpj(rs.getString("cpf_cnpj"));

            pedido.setStatusPedido(statusPedido);
            cidade.setEstado(estado);
            endereco.setCidade(cidade);
            pedido.setEndereco(endereco);
            pedido.setUsuario(usuario);
            pedido.setItens(itemModel.findItensByPedido(rs.getInt("id"), false));
        }

        return pedido;
    }

    public TbPedido findPedidoVisualizacaoOrdemProducao(int id) throws java.sql.SQLException
    {
        Connection conn = ConnectionManager.getConnection();

        String sql = String.format(
            "SELECT p.*, c.nome as nome_cliente, c.cpf_cnpj, c.tipo_pessoa, e.cep, e.logradouro, e.bairro, e.complemento, e.numero, sp.nome as nome_status_pedido, "
            + " cid.nome as nome_cidade, est.nome as nome_estado, est.uf, est.id as estado_id, e.id as endereco_id, e.cidade_id FROM pedido p "
            + " INNER JOIN status_pedido sp ON p.status_pedido_id = sp.id"
            + " INNER JOIN usuario c ON p.usuario_id = c.id"
            + " INNER JOIN endereco e ON e.pedido_id = p.id"
            + " INNER JOIN cidade cid ON e.cidade_id = cid.id"
            + " INNER JOIN estado est ON cid.estado_id = est.id"
            + " WHERE p.id = %d",
            id
        );

        System.out.println("-----------------");
        System.out.println(sql);
        System.out.println("-----------------");


        PreparedStatement ps = conn.prepareStatement(sql);
        ResultSet rs = ps.executeQuery();

        TbPedido pedido = new TbPedido();
        PedidoItem itemModel = new PedidoItem();
        if (rs.next()) {
            TbUsuario usuario = new TbUsuario();
            TbEndereco endereco = new TbEndereco();
            TbEstado estado = new TbEstado();
            TbCidade cidade = new TbCidade();
            TbStatusPedido statusPedido = new TbStatusPedido();

            pedido.setId(rs.getInt("id"));
            pedido.setCaminhoArquivoNotaFiscal(rs.getString("caminho_arquivo_nota_fiscal"));
            pedido.setDataInclusao(rs.getDate("data_inclusao"));
            pedido.setDataPrevisaoInstalacao(rs.getDate("data_previsao_instalacao"));
            pedido.setValorTotal(rs.getFloat("valor_total"));
            pedido.setValorMaoObra(rs.getFloat("valor_mao_obra"));
            pedido.setPedidoPago(rs.getInt("pedido_pago"));
            pedido.setObservacao(rs.getString("observacao"));
            pedido.setUsuarioId(rs.getInt("usuario_id"));
            pedido.setStatusPedidoId(rs.getInt("status_pedido_id"));
            pedido.setDescontoGeral(rs.getFloat("desconto_geral"));

            statusPedido.setId(rs.getInt("status_pedido_id"));
            statusPedido.setNome(rs.getString("nome_status_pedido"));

            endereco.setId(rs.getInt("endereco_id"));
            endereco.setCep(rs.getString("cep"));
            endereco.setLogradouro(rs.getString("logradouro"));
            endereco.setBairro(rs.getString("bairro"));
            endereco.setComplemento(rs.getString("complemento"));
            endereco.setCidadeId(rs.getInt("cidade_id"));
            endereco.setNumero(rs.getString("numero"));
            endereco.setUsuarioId(rs.getInt("usuario_id"));
            endereco.setEstadoId(rs.getInt("estado_id"));

            cidade.setNome(rs.getString("nome_cidade"));
            cidade.setUf(rs.getString("uf"));
            cidade.setId(rs.getInt("cidade_id"));

            estado.setNome(rs.getString("nome_estado"));
            estado.setUf(rs.getString("uf"));
            estado.setId(rs.getInt("estado_id"));

            usuario.setId(rs.getInt("usuario_id"));
            usuario.setNome(rs.getString("nome_cliente"));
            usuario.setTipoPessoa(rs.getString("tipo_pessoa"));
            usuario.setCpfCnpj(rs.getString("cpf_cnpj"));

            pedido.setStatusPedido(statusPedido);
            cidade.setEstado(estado);
            endereco.setCidade(cidade);
            pedido.setEndereco(endereco);
            pedido.setUsuario(usuario);
            pedido.setItens(itemModel.findItensByPedido(rs.getInt("id"), true));
        }

        return pedido;
    }

    public TbPedido findById(int fkPedido) throws java.sql.SQLException
    {
        Connection conn = ConnectionManager.getConnection();

        String sql = String.format(
            "SELECT p.* FROM pedido p WHERE p.id = %d",
            fkPedido
        );

        System.out.println("-----------------");
        System.out.println(sql);
        System.out.println("-----------------");

        PreparedStatement ps = conn.prepareStatement(sql);
        ResultSet rs = ps.executeQuery();

        TbPedido pedido = new TbPedido();
        if (rs.next()) {
            pedido.setId(rs.getInt("id"));
            pedido.setCaminhoArquivoNotaFiscal(rs.getString("caminho_arquivo_nota_fiscal"));
            pedido.setDataInclusao(rs.getDate("data_inclusao"));
            pedido.setDataPrevisaoInstalacao(rs.getDate("data_previsao_instalacao"));
            pedido.setValorTotal(rs.getFloat("valor_total"));
            pedido.setValorMaoObra(rs.getFloat("valor_mao_obra"));
            pedido.setPedidoPago(rs.getInt("pedido_pago"));
            pedido.setObservacao(rs.getString("observacao"));
            pedido.setUsuarioId(rs.getInt("usuario_id"));
            pedido.setStatusPedidoId(rs.getInt("status_pedido_id"));
            pedido.setDescontoGeral(rs.getFloat("desconto_geral"));
        }

        return pedido;
    }
}
