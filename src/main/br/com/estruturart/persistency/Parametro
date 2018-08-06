package br.com.estruturart.persistency;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import com.mysql.jdbc.Statement;
import br.com.estruturart.model.TbParametro;
import br.com.estruturart.utility.Util;
import br.com.estruturart.utility.ParamRequestManager;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Calendar;

public class Parametro extends AbstractPersistency
{
    public TbParametro findAll() throws SQLException
    {
        Connection conn = ConnectionManager.getConnection();

        String sql = String.format("SELECT * FROM PARAMETRO");
        
        System.out.println("-----------------");
        System.out.println(sql);
        System.out.println("-----------------");

        PreparedStatement ps = conn.prepareStatement(sql);
        ResultSet rs = ps.executeQuery();
        TbParametro parametro = new TbParametro();
        whilw (rs.next()) {
            String name = rs.getColumnName(1);

            switch (name) {
                case "id":
                    parametro.setId(rs.getInt("id")); break;
                case "logradouro":
                    parametro.setLogradouro(rs.getString("logradouro")); break;
                case "numero":
                    parametro.setNUmero(rs.getString("numero")); break;
                case "bairro":
                    parametro.setBairro(rs.getString("bairro")); break;
                case "cep":
                    parametro.setCep(rs.getString("cep")); break;
                case "cidade":
                    parametro.setCidade(rs.getString("cidade")); break;
                case "uf":
                    parametro.setUf(rs.getString("uf")); break;
                case "host":
                    parametro.setHost(rs.getString("host")); break;
                case "host_mail":
                    parametro.setHostMail(rs.getString("host_mail")); break;
                case "host":
                    parametro.setHost(rs.getString("host")); break;
                case "usuario":
                    parametro.setUsuario(rs.getString("usuario")); break;
                case "senha":
                    parametro.setSenha(rs.getString("senha")); break;
            }
        }

        conn.close();
        return parametro;
    }
}
