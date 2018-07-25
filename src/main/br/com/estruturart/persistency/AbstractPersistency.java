package br.com.estruturart.persistency;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class AbstractPersistency {

    public int findTotalRows(String sql) throws SQLException
    {
        Connection conn = ConnectionManager.getConnection();
        System.out.println(sql.replace("{columns}", "COUNT(1)").replace("{limit}", ""));
        PreparedStatement ps = conn.prepareStatement(sql.replace("{columns}", "COUNT(1)").replace("{limit}", ""));

        ResultSet rs = ps.executeQuery();

        int total = 0;
        if (rs.next()) {
            total = rs.getInt(1);
        }

        conn.close();
        return total;
    }
}
