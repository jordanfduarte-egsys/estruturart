package br.com.estruturart.persistency;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
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
}
