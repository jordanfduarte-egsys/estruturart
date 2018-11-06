package br.com.estruturart.persistency;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Date;
import com.mysql.jdbc.Statement;
import br.com.estruturart.model.TbLancamento;
import br.com.estruturart.model.TbPedidoItem;
import br.com.estruturart.model.TbPedido;
import br.com.estruturart.model.TbMaterial;
import br.com.estruturart.utility.Paginator;
import br.com.estruturart.utility.ParamRequestManager;

public class Lancamento extends AbstractPersistency
{

    public Paginator findAllPaginated(int page, int offset, ParamRequestManager filter) throws SQLException
    {
        Connection conn = ConnectionManager.getConnection();

        int pageAux = page;
        if (pageAux == 1) {
            pageAux = 0;
        } else {
            pageAux = pageAux * offset - offset;
        }

        String sqlNome = "";
        if (filter.hasParam("busca")) {
            sqlNome = " AND (m2.descricao LIKE \"%" + filter.getParam("busca") + "%\" OR i.id = \"" + filter.getParam("busca") + "\") ";
        }

        String columns = String.format("l.*, m2.descricao as desc_material, i.pedido_id");
        String limit = String.format("LIMIT %d, %d", pageAux, offset);

        String sql = String.format(
            "SELECT {columns} FROM LANCAMENTO l" +
            " LEFT JOIN pedido_itens i ON l.pedido_itens_id = i.id" +
            " LEFT JOIN material m ON i.modelo_id = m.id" +
            " LEFT JOIN material m2 ON l.material_id = m2.id" +
            " WHERE 1 %s " + " order by l.data_inclusao DESC {limit}",
            sqlNome
        );

        PreparedStatement ps = conn.prepareStatement(sql.replace("{columns}", columns).replace("{limit}", limit));



        ResultSet rs = ps.executeQuery();

        List<TbLancamento> lancamentos = new ArrayList<TbLancamento>();
        while (rs.next()) {
            TbLancamento lancamento = new TbLancamento();
            TbMaterial material2 = new TbMaterial();
            TbPedidoItem item = new TbPedidoItem();

            lancamento.setId(rs.getInt("id"));
            lancamento.setPreco(rs.getFloat("preco"));
            lancamento.setPrecoPintura(rs.getFloat("preco_pintura"));
            lancamento.setDataInclusao(new Date(rs.getTimestamp("data_inclusao").getTime()));
            lancamento.setDescricao(rs.getString("descricao"));
            lancamento.setDesconto(rs.getFloat("desconto"));
            lancamento.setUsuarioId(rs.getInt("usuario_id"));
            lancamento.setPedidoItensId(rs.getInt("pedido_itens_id"));
            lancamento.setMaterialId(rs.getInt("material_id"));

            material2.setId(rs.getInt("material_id"));
            material2.setDescricao(rs.getString("desc_material"));
            item.setId(rs.getInt("pedido_itens_id"));
            item.setPedidoId(rs.getInt("pedido_id"));

            lancamento.setPedidoItem(item);
            lancamento.setMaterial(material2);
            lancamentos.add(lancamento);
        }

        conn.close();
        return new Paginator(lancamentos, this.findTotalRows(sql), offset, page);
    }

    public int insert(TbLancamento lancamento) throws SQLException
    {
        Connection conn = null;
        if (isConnection()) {
            conn = getConnection();
        } else {
            conn = ConnectionManager.getConnection();
        }

        String materialId = (lancamento.getMaterialId() > 0 ? String.valueOf(lancamento.getMaterialId()) : "null");
        String pedidoItem = (lancamento.getPedidoItensId() > 0 ? String.valueOf(lancamento.getPedidoItensId()) : "null");
        String sql = String.format(
            "INSERT INTO LANCAMENTO (preco, preco_pintura, descricao, desconto, usuario_id, pedido_itens_id, material_id)"
            + " VALUES ('%s', '%s', '%s', '%s', '%d', %s, %s)",
            lancamento.getPreco(), lancamento.getPrecoPintura(), lancamento.getDescricao(),
            lancamento.getDesconto(), lancamento.getUsuarioId(), pedidoItem, materialId
        );

        PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
        ps.execute();


        ResultSet rs = ps.getGeneratedKeys();
        lancamento.setId(rs.next() ? rs.getInt(1) : 0);

        if (!isConnection()) {
            conn.close();
        }

        return lancamento.getId();
    }

    public void update(TbLancamento lancamento) throws SQLException
    {
        Connection conn = null;
        if (isConnection()) {
            conn = getConnection();
        } else {
            conn = ConnectionManager.getConnection();
        }

        String materialId = (lancamento.getMaterialId() > 0 ? String.valueOf(lancamento.getMaterialId()) : "null");
        String pedidoItem = (lancamento.getPedidoItensId() > 0 ? String.valueOf(lancamento.getPedidoItensId()) : "null");
        String sql = String.format(
            "UPDATE LANCAMENTO SET preco = '%s', descricao = '%s', pedido_itens_id = %s, material_id = %s WHERE id = %d",
            lancamento.getPreco(), lancamento.getDescricao(), pedidoItem, materialId, lancamento.getId()
        );

        PreparedStatement ps = conn.prepareStatement(sql);
        ps.execute();


        if (!isConnection()) {
            conn.close();
        }
    }

    public List<TbLancamento> findLancamentosByItem(int itemId) throws java.sql.SQLException
    {
        Connection conn = ConnectionManager.getConnection();

        String sql = String.format("SELECT * FROM lancamento WHERE pedido_itens_id = %d ORDER BY data_inclusao", itemId);

        PreparedStatement ps = conn.prepareStatement(sql);
        ResultSet rs = ps.executeQuery();
        List<TbLancamento> lans = new ArrayList<TbLancamento>();
        while (rs.next()) {
            TbLancamento lan = new TbLancamento();

            lan.setId(rs.getInt("id"));
            lan.setPreco(rs.getFloat("preco"));
            lan.setPrecoPintura(rs.getFloat("preco_pintura"));
            lan.setDataInclusao(new Date(rs.getTimestamp("data_inclusao").getTime()));
            lan.setDescricao(rs.getString("descricao"));
            lan.setDesconto(rs.getFloat("desconto"));
            lan.setUsuarioId(rs.getInt("usuario_id"));
            lan.setPedidoItensId(rs.getInt("pedido_itens_id"));
            lan.setMaterialId(rs.getInt("material_id"));
            lans.add(lan);
        }

        return lans;
    }

    public TbLancamento getLancamentoById(int lancamentoId) throws java.sql.SQLException
    {
        Connection conn = ConnectionManager.getConnection();

        String sql = String.format("SELECT * FROM lancamento WHERE id = %d", lancamentoId);

        PreparedStatement ps = conn.prepareStatement(sql);
        ResultSet rs = ps.executeQuery();
        TbLancamento lan = new TbLancamento();
        if (rs.next()) {
            lan.setId(rs.getInt("id"));
            lan.setPreco(rs.getFloat("preco"));
            lan.setPrecoPintura(rs.getFloat("preco_pintura"));
            lan.setDataInclusao(new Date(rs.getTimestamp("data_inclusao").getTime()));
            lan.setDescricao(rs.getString("descricao"));
            lan.setDesconto(rs.getFloat("desconto"));
            lan.setUsuarioId(rs.getInt("usuario_id"));
            lan.setMaterialId(rs.getInt("material_id"));
            lan.setPedidoItensId(rs.getInt("pedido_itens_id"));
        }

        return lan;
    }

    public List<TbLancamento> findFluxoCaixa(String dataIni, String dataFim) throws java.sql.SQLException
    {
        Connection conn = ConnectionManager.getConnection();

        String sql = String.format(
            "SELECT l.*, m2.descricao as desc_material, i.pedido_id, p.data_inclusao as data_inclusao_pedido, p.data_previsao_instalacao" +
            " FROM lancamento l " +
            " LEFT JOIN pedido_itens i ON l.pedido_itens_id = i.id" +
            " LEFT JOIN pedido p ON i.pedido_id = p.id" +
            " LEFT JOIN material m ON i.modelo_id = m.id" +
            " LEFT JOIN material m2 ON l.material_id = m2.id" +
            " WHERE l.data_inclusao BETWEEN '%s 00:00:00' AND '%s 23:59:59' ORDER BY l.data_inclusao",
            dataIni,
            dataFim
        );

        PreparedStatement ps = conn.prepareStatement(sql);
        ResultSet rs = ps.executeQuery();
        List<TbLancamento> lans = new ArrayList<TbLancamento>();
        while (rs.next()) {
            TbLancamento lan = new TbLancamento();
            TbMaterial material2 = new TbMaterial();
            TbPedidoItem item = new TbPedidoItem();
            TbPedido pedido = new TbPedido();

            lan.setId(rs.getInt("id"));
            lan.setPreco(rs.getFloat("preco"));
            lan.setPrecoPintura(rs.getFloat("preco_pintura"));
            lan.setDataInclusao(new Date(rs.getTimestamp("data_inclusao").getTime()));
            lan.setDescricao(rs.getString("descricao"));
            lan.setDesconto(rs.getFloat("desconto"));
            lan.setUsuarioId(rs.getInt("usuario_id"));
            lan.setPedidoItensId(rs.getInt("pedido_itens_id"));
            lan.setMaterialId(rs.getInt("material_id"));

            material2.setId(rs.getInt("material_id"));
            material2.setDescricao(rs.getString("desc_material"));

            pedido.setId(rs.getInt("pedido_id"));
            pedido.setDataInclusao(rs.getDate("data_inclusao_pedido"));
            pedido.setDataPrevisaoInstalacao(rs.getDate("data_previsao_instalacao"));
            item.setId(rs.getInt("pedido_itens_id"));
            item.setPedidoId(rs.getInt("pedido_id"));
            item.setPedido(pedido);
            lan.setPedidoItem(item);
            lan.setMaterial(material2);
            lans.add(lan);
        }

        return lans;
    }

    public TbLancamento findFluxoCaixaTotais(String dataIni, String dataFim) throws java.sql.SQLException
    {
        Connection conn = ConnectionManager.getConnection();

        String sql = String.format(
            "SELECT SUM(IF (l.preco < 0, ABS(l.preco), 0)) as soma_empresa, SUM(IF (l.preco > 0, l.preco + IFNULL(l.preco_pintura, 0), 0)) as soma_lucro FROM lancamento l " +
            " LEFT JOIN pedido_itens i ON l.pedido_itens_id = i.id" +
            " LEFT JOIN material m ON i.modelo_id = m.id" +
            " LEFT JOIN material m2 ON l.material_id = m2.id" +
            " WHERE l.data_inclusao BETWEEN '%s 00:00:00' AND '%s 23:59:59' ORDER BY l.data_inclusao",
            dataIni,
            dataFim
        );

        PreparedStatement ps = conn.prepareStatement(sql);
        ResultSet rs = ps.executeQuery();
        TbLancamento lan = new TbLancamento();
        if (rs.next()) {
            lan.setSomaEmpresa(rs.getFloat("soma_empresa"));
            lan.setLucroEmpresa(rs.getFloat("soma_lucro"));
            lan.setDiferenca(rs.getFloat("soma_lucro") - rs.getFloat("soma_empresa"));
        }

        return lan;
    }
}
