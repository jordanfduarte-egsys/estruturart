package br.com.estruturart.persistency;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.mysql.jdbc.Statement;

import br.com.estruturart.model.TbCidade;
import br.com.estruturart.model.TbEstado;

public class Estado extends AbstractPersistency
{
    public List<TbEstado> findEstados() throws Exception
    {
        Connection conn = ConnectionManager.getConnection();
        PreparedStatement ps = conn
                .prepareStatement("SELECT ESTADO.* FROM ESTADO");

        ResultSet rs = ps.executeQuery();
        List<TbEstado> estados = new ArrayList<TbEstado>();
        while (rs.next()) {
            TbEstado estado = new TbEstado();
            estado.setId(rs.getInt("id"));
            estado.setNome(rs.getString("nome"));
            estado.setUf(rs.getString("uf"));
            estados.add(estado);
        }

        conn.close();
        return estados;
    }
}