package br.com.estruturart.persistency;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import com.mysql.jdbc.Statement;

public class LogError
{
    public void insert(String message) throws SQLException
    {
        Connection conn = ConnectionManager.getConnection();
        String sql = String.format("INSERT INTO log_error (descricao) " + " VALUES (\"%s\")", message);

        PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
        ps.executeUpdate();

        conn.close();
    }
}
