package br.com.estruturart.persistency;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Date;
import com.mysql.jdbc.Statement;
import br.com.estruturart.model.TbPedidoItem;
import br.com.estruturart.model.TbModelo;
import br.com.estruturart.model.TbUsuario;
import br.com.estruturart.model.TbPedido;
import br.com.estruturart.model.TbStatusItem;
import br.com.estruturart.persistency.Lancamento;
import br.com.estruturart.persistency.Modelo;

public class PedidoItem extends AbstractPersistency
{
    public int insert(TbPedidoItem item) throws SQLException
    {
        Connection conn = null;
        if (isConnection()) {
            conn = getConnection();
        } else {
            conn = ConnectionManager.getConnection();
        }

        String sql = String.format(
            "INSERT INTO PEDIDO_ITENS (largura, altura, quantidade, status_item_id, pedido_id, modelo_id)"
            + " VALUES ('%s', '%s', %d, %d, %d, %d)",
            item.getLargura(), item.getAltura(), item.getQuantidade(),
            item.getStatusItemId(), item.getPedidoId(), item.getModeloId()
        );

        PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
        ps.execute();
        System.out.println(sql);

        ResultSet rs = ps.getGeneratedKeys();
        item.setId(rs.next() ? rs.getInt(1) : 0);

        if (!isConnection()) {
            conn.close();
        }

        return item.getId();
    }

    public void update(TbPedidoItem item) throws SQLException
    {
        Connection conn = null;
        if (isConnection()) {
            conn = getConnection();
        } else {
            conn = ConnectionManager.getConnection();
        }

        String sql = String.format(
            "UPDATE PEDIDO_ITENS SET status_item_id = %d"
            + " WHERE id = %d",
            item.getStatusItemId(), item.getId()
        );

        PreparedStatement ps = conn.prepareStatement(sql);
        ps.execute();
        System.out.println(sql);

        if (!isConnection()) {
            conn.close();
        }
    }

    public List<TbPedidoItem> findItensByPedido(int pedidoId, boolean isConsultarMateriais) throws java.sql.SQLException
    {
        Connection conn = ConnectionManager.getConnection();

        String sql = String.format(
            "SELECT pi.*, m.nome, m.descricao, m.largura_padrao, m.altura_padrao, m.imagem, "
            + " m.preco_pintura, m.porcentagem_acrescimo, m.qtd_dias_producao, s.nome as nome_status_item FROM pedido_itens pi"
            + " INNER JOIN modelo m ON pi.modelo_id = m.id INNER JOIN status_item s "
            + " ON pi.status_item_id = s.id WHERE pedido_id = %d ORDER BY pi.id ASC", pedidoId
        );

        PreparedStatement ps = conn.prepareStatement(sql);
        ResultSet rs = ps.executeQuery();
        List<TbPedidoItem> itens = new ArrayList<TbPedidoItem>();
        Lancamento modelLancamento = new Lancamento();
        Modelo modeloModel = new Modelo();
        while (rs.next()) {
            TbPedidoItem item = new TbPedidoItem();
            TbModelo modelo = new TbModelo();
            TbStatusItem statusItem = new TbStatusItem();

            item.setId(rs.getInt("id"));
            item.setLargura(rs.getFloat("largura"));
            item.setAltura(rs.getFloat("altura"));
            item.setDataInclusao(new Date(rs.getDate("data_inclusao").getTime()));
            item.setQuantidade(rs.getInt("quantidade"));
            item.setStatusItemId(rs.getInt("status_item_id"));
            item.setPedidoId(rs.getInt("pedido_id"));
            item.setModeloId(rs.getInt("modelo_id"));

            modelo.setId(rs.getInt("modelo_id"));
            modelo.setNome(rs.getString("nome"));
            modelo.setDescricao(rs.getString("descricao"));
            modelo.setLarguraPadrao(rs.getFloat("largura_padrao"));
            modelo.setAlturaPadrao(rs.getFloat("altura_padrao"));
            modelo.setImagem(rs.getString("imagem"));
            modelo.setPrecoPintura(rs.getFloat("preco_pintura"));
            modelo.setPorcentagemAcrescimo(rs.getFloat("porcentagem_acrescimo"));
            modelo.setQtdDiasProducao(rs.getInt("qtd_dias_producao"));

            statusItem.setId(rs.getInt("status_item_id"));
            statusItem.setNome(rs.getString("nome_status_item"));

            if (isConsultarMateriais) {
                modelo.setMateriais(modeloModel.findMaterialByModelo(rs.getInt("modelo_id")));
            }
            item.setModelo(modelo);
            item.setStatusItem(statusItem);
            item.setLancamentos(modelLancamento.findLancamentosByItem(rs.getInt("id")));
            itens.add(item);
        }

        return itens;
    }

    public TbPedidoItem findPedidoByItem(int itemId) throws java.sql.SQLException
    {
        Connection conn = ConnectionManager.getConnection();

        String sql = String.format(
            "SELECT i.*, p.usuario_id, p.valor_total FROM pedido_itens i INNER JOIN pedido p ON "
            + " i.pedido_id = p.id INNER JOIN usuario u ON p.usuario_id = u.id WHERE i.id = %d",
            itemId
        );

        PreparedStatement ps = conn.prepareStatement(sql);
        ResultSet rs = ps.executeQuery();
        TbPedidoItem item = new TbPedidoItem();
        if (rs.next()) {
            TbUsuario usuario = new TbUsuario();
            TbPedido pedido = new TbPedido();
            item.setId(rs.getInt("id"));
            item.setLargura(rs.getFloat("largura"));
            item.setAltura(rs.getFloat("altura"));
            item.setDataInclusao(rs.getDate("data_inclusao"));
            item.setQuantidade(rs.getInt("quantidade"));
            item.setStatusItemId(rs.getInt("status_item_id"));
            item.setPedidoId(rs.getInt("pedido_id"));
            item.setModeloId(rs.getInt("modelo_id"));

            pedido.setId(rs.getInt("pedido_id"));
            pedido.setValorTotal(rs.getFloat("valor_total"));
            usuario.setId(rs.getInt("usuario_id"));
            pedido.setUsuario(usuario);
            item.setPedido(pedido);

        }

        return item;
    }
}
