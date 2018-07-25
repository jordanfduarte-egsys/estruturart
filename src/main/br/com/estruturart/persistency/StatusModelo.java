package br.com.estruturart.persistency;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import br.com.estruturart.model.TbStatusModelo;

public class StatusModelo extends AbstractPersistency
{
    public List<TbStatusModelo> findAll() throws SQLException
    {
        Connection conn = ConnectionManager.getConnection();

        String sql = String.format("SELECT STATUS_MODELO.* FROM STATUS_MODELO order by nome ASC");

        PreparedStatement ps = conn.prepareStatement(sql);
        ResultSet rs = ps.executeQuery();

        List<TbStatusModelo> statusList = new ArrayList<TbStatusModelo>();
        while (rs.next()) {
            TbStatusModelo statusModelo = new TbStatusModelo();
            statusModelo.setId(rs.getInt("id"));
            statusModelo.setNome(rs.getString("nome"));

            statusList.add(statusModelo);
        }

        conn.close();
        return statusList;
    }
}
