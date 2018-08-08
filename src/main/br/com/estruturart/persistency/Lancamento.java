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

public class Lancamento extends AbstractPersistency
{
    public int insert(TbLancamento lancamento) throws SQLException
    {
        Connection conn = null;
        if (isConnection()) {
            conn = getConnection();
        } else {
            conn = ConnectionManager.getConnection();
        }

        String sql = String.format(
            "INSERT INTO LANCAMENTO (preco, preco_pintura, descricao, desconto, usuario_id, pedido_itens_id)"
            + " VALUES ('%s', '%s', '%s', '%s', %d, %d)",
            lancamento.getPreco(), lancamento.getPrecoPintura(), lancamento.getDescricao(),
            lancamento.getDesconto(), lancamento.getUsuarioId(), lancamento.getPedidoItensId(),
            lancamento.getUsuarioId()
        );

        PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
        ps.execute();
        System.out.println(sql);

        ResultSet rs = ps.getGeneratedKeys();
        lancamento.setId(rs.next() ? rs.getInt(1) : 0);

        if (!isConnection()) {
            conn.close();
        }

        return lancamento.getId();
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
            lans.add(lan);
        }

        return lans;
    }
}
