package br.com.estruturart.persistency;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import br.com.estruturart.model.TbStatusPedido;

public class StatusPedido extends AbstractPersistency
{
    public List<TbStatusPedido> findAll() throws SQLException
    {
        Connection conn = ConnectionManager.getConnection();

        String sql = String.format("SELECT STATUS_PEDIDO.* FROM STATUS_PEDIDO order by nome ASC");

        PreparedStatement ps = conn.prepareStatement(sql);
        ResultSet rs = ps.executeQuery();

        List<TbStatusPedido> statusList = new ArrayList<TbStatusPedido>();
        while (rs.next()) {
            TbStatusPedido statusPedido = new TbStatusPedido();
            statusPedido.setId(rs.getInt("id"));
            statusPedido.setNome(rs.getString("nome"));

            statusList.add(statusPedido);
        }

        conn.close();
        return statusList;
    }

    public TbStatusPedido findById(int fkStatus) throws SQLException
    {
        Connection conn = ConnectionManager.getConnection();

        String sql = String.format("SELECT STATUS_PEDIDO.* FROM STATUS_PEDIDO WHERE id = %d", fkStatus);

        PreparedStatement ps = conn.prepareStatement(sql);
        ResultSet rs = ps.executeQuery();
        TbStatusPedido statusPedido = new TbStatusPedido();
        if (rs.next()) {
            statusPedido.setId(rs.getInt("id"));
            statusPedido.setNome(rs.getString("nome"));
        }

        conn.close();
        return statusPedido;
    }
}
