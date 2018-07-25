package br.com.estruturart.persistency;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ConnectionManager {

    public static Connection getConnection() throws SQLException
    {
        try {
            Class.forName("com.mysql.jdbc.Driver");
            Connection con = DriverManager.getConnection(
                 "jdbc:mysql://localhost:3306/estruturart?useSSL=false", "root", ""
            );

            con.setAutoCommit(true);
            return con;
         } catch(Exception e) {
            System.out.println("Erro na conexao!!!!" + e.getMessage());
            return null;
         }
    }
}
