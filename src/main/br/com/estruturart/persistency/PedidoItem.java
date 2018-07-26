package br.com.estruturart.persistency;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import com.mysql.jdbc.Statement;
import br.com.estruturart.model.TbPedidoItem;

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
}
