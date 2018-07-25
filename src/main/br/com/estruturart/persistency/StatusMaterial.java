package br.com.estruturart.persistency;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import br.com.estruturart.model.TbStatusMaterial;

public class StatusMaterial extends AbstractPersistency
{

    public List<TbStatusMaterial> findAll() throws SQLException
    {
        Connection conn = ConnectionManager.getConnection();

        String sql = String.format("SELECT STATUS_MATERIAL.* FROM STATUS_MATERIAL order by descricao ASC");

        PreparedStatement ps = conn.prepareStatement(sql);
        ResultSet rs = ps.executeQuery();

        List<TbStatusMaterial> statusList = new ArrayList<TbStatusMaterial>();
        while (rs.next()) {
            TbStatusMaterial statusMaterial = new TbStatusMaterial();
            statusMaterial.setId(rs.getInt("id"));
            statusMaterial.setDescricao(rs.getString("descricao"));

            statusList.add(statusMaterial);
        }

        conn.close();
        return statusList;
    }
}
