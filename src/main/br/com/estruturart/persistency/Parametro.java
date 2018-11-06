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

        String sql = String.format("SELECT * FROM CONFIGURACAO");





        PreparedStatement ps = conn.prepareStatement(sql);
        ResultSet rs = ps.executeQuery();
        TbParametro parametro = new TbParametro();
        while (rs.next()) {
            switch (rs.getString("field")) {
                case "id":
                    parametro.setId(rs.getInt("id")); break;
                case "logradouro":
                    parametro.setLogradouro(rs.getString("value")); break;
                case "numero":
                    parametro.setNumero(rs.getString("value")); break;
                case "bairro":
                    parametro.setBairro(rs.getString("value")); break;
                case "complemento":
                    parametro.setComplemento(rs.getString("value")); break;
                case "cep":
                    parametro.setCep(rs.getString("value")); break;
                case "cidade":
                    parametro.setCidade(rs.getString("value")); break;
                case "uf":
                    parametro.setUf(rs.getString("value")); break;
                case "host_mail":
                    parametro.setHostMail(rs.getString("value")); break;
                case "host":
                    parametro.setHost(rs.getString("value")); break;
                case "usuario":
                    parametro.setUsuario(rs.getString("value")); break;
                case "senha":
                    parametro.setSenha(rs.getString("value")); break;
                case "from":
                    parametro.setFrom(rs.getString("value")); break;
            }
        }

        conn.close();
        return parametro;
    }

    public void update(TbParametro parametro) throws SQLException
    {
        Connection conn = null;
        if (isConnection()) {
            conn = getConnection();
        } else {
            conn = ConnectionManager.getConnection();
        }

        String sql = String.format("UPDATE configuracao SET value = '%s' WHERE field = 'logradouro'", parametro.getLogradouro());
        PreparedStatement ps = conn.prepareStatement(sql);
        ps.execute();

        sql = String.format("UPDATE configuracao SET value = '%s' WHERE field = 'numero'", parametro.getNumero());
        ps = conn.prepareStatement(sql);
        ps.execute();

        sql = String.format("UPDATE configuracao SET value = '%s' WHERE field = 'bairro'", parametro.getBairro());
        ps = conn.prepareStatement(sql);
        ps.execute();

        sql = String.format("UPDATE configuracao SET value = '%s' WHERE field = 'cep'", parametro.getCep());
        ps = conn.prepareStatement(sql);
        ps.execute();

        sql = String.format("UPDATE configuracao SET value = '%s' WHERE field = 'cidade'", parametro.getCidade());
        ps = conn.prepareStatement(sql);
        ps.execute();

        sql = String.format("UPDATE configuracao SET value = '%s' WHERE field = 'uf'", parametro.getUf());
        ps = conn.prepareStatement(sql);
        ps.execute();

        sql = String.format("UPDATE configuracao SET value = '%s' WHERE field = 'host_mail'", parametro.getHostMail());
        ps = conn.prepareStatement(sql);
        ps.execute();

        sql = String.format("UPDATE configuracao SET value = '%s' WHERE field = 'host'", parametro.getHost());
        ps = conn.prepareStatement(sql);
        ps.execute();

        sql = String.format("UPDATE configuracao SET value = '%s' WHERE field = 'usuario'", parametro.getUsuario());
        ps = conn.prepareStatement(sql);
        ps.execute();

        sql = String.format("UPDATE configuracao SET value = '%s' WHERE field = 'senha'", parametro.getSenha());
        ps = conn.prepareStatement(sql);
        ps.execute();

        sql = String.format("UPDATE configuracao SET value = '%s' WHERE field = 'from'", parametro.getFrom());
        ps = conn.prepareStatement(sql);
        ps.execute();

        sql = String.format("UPDATE configuracao SET value = '%s' WHERE field = 'complemento'", parametro.getComplemento());
        ps = conn.prepareStatement(sql);
        ps.execute();

        if (!isConnection()) {
            conn.close();
        }
    }
}
