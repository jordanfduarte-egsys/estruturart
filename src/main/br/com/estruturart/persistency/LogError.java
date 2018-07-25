package br.com.estruturart.persistency;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import com.mysql.jdbc.Statement;

public class LogError
{
    public int insert(String message) throws SQLException
    {
        Connection conn = ConnectionManager.getConnection();
        String sql = String.format("INSERT INTO log_error (descricao) " + " VALUES (\"%s\")", message);

        PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
        ps.executeUpdate();

        conn.close();
        if (ps.getGeneratedKeys().next()) {
            return ps.getGeneratedKeys().getInt(1);
        }

        return 0;
    }
}
