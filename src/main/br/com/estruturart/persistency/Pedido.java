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
import br.com.estruturart.utility.Util;
import br.com.estruturart.utility.ParamRequestManager;
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

    public List<TbPedido> findByRequestManager(ParamRequestManager params) throws SQLException
    {
        Connection conn = ConnectionManager.getConnection();

        boolean isFiltro = false;
        boolean filtroData = false;
        String args = "";
        if (params.hasParam("id") && !params.getParam("id").equals("")) {
            args += " AND p.id = '" + params.getParam("id") + "' ";
            isFiltro = true;
        }

        if (params.hasParam("nome") && !params.getParam("nome").equals("")) {
            args += " AND (p.nome LIKE '" + params.getParam("id") + "%' OR p.observacao LIKE '%" + params.getParam("id") + "%') ";
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
            if (!data.equals("")) {
                SimpleDateFormat df = new SimpleDateFormat("yyy-MM-dd");
                Date date = df.parse(dataStr);

                Calendar c = Calendar.getInstance();
                c.set(Calendar.DAY_OF_MONTH, c.getActualMinimum(Calendar.DAY_OF_MONTH));
                Date d1 = c.getTime();
                c.set(Calendar.DAY_OF_MONTH, c.getActualMaximum(Calendar.DAY_OF_MONTH));
                Date d2 = c.getTime();
                args += " AND (p.data_inclusao BETWEEN '" + df.format(d1) + " 00:00'  AND '" + df.format(d2) + " 23:59') ";
                isFiltro = filtroData = true;
            }
        }

        if (!filtroData && params.hasParam("data_ini") && !params.getParam("data_ini").equals("")) {
            String dataIni = Util.dataBrToEn(params.getParam("data_ini"));

            if (!dataIni.equals("")) {
                args += " AND (p.data_inclusao BETWEEN '" + dataIni + " 00:00'  AND '" + dataFim + " 23:59') ";
                isFiltro = filtroData = true;
            }
        }

        if (!filtroData) {
            Date d1 = new Date();
            Date d2 = new Date();
            SimpleDateFormat df = new SimpleDateFormat("yyy-MM-dd");
            Calendar c = Calendar.getInstance();

            c.set(Calendar.DAY_OF_MONTH, c.getActualMinimum(Calendar.DAY_OF_MONTH));
            d1 = c.getTime();
            c.set(Calendar.DAY_OF_MONTH, c.getActualMaximum(Calendar.DAY_OF_MONTH));
            d2 = c.getTime();
            args += " AND (p.data_inclusao BETWEEN '" + df.format(d1) + " 00:00'  AND '" + df.format(d2) + " 23:59') ";
        }

        String sql = String.format(
            "SELECT p.*, e.cep, e.logradouro, e.bairro, e.numero, e.complemento, e.cidade_id, e.usuario_id, e.id as endereco_id, u.nome as nome_usuario "
            + " FROM PEDIDO p INNER JOIN endereco e ON p.id = e.pedido_id "
            + " INNER JOIN USUARIO u ON p.usuario_id = u.id "
            + " WHERE 1 %s",
            args
        );

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
}
