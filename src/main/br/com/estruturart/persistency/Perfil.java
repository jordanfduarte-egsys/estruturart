package br.com.estruturart.persistency;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import br.com.estruturart.model.TbPerfil;
import br.com.estruturart.model.TbUsuario;

public class Perfil extends AbstractPersistency
{
    public List<TbPerfil> findPerfisAll() throws SQLException
    {
        Connection conn = ConnectionManager.getConnection();

        String sql = String.format(
            "SELECT * FROM PERFIL"
             + " order by id DESC"
        );

        PreparedStatement ps = conn.prepareStatement(sql);
        ResultSet rs = ps.executeQuery();

        List<TbPerfil> perfis = new ArrayList<TbPerfil>();
        while (rs.next()) {
            TbPerfil perfil = new TbPerfil();
            perfil.setId(rs.getInt("id"));
            perfil.setDescricao(rs.getString("descricao"));
            perfis.add(perfil);
        }

        conn.close();
        return perfis;
    }

    public TbPerfil findRow(int fkPerfil) throws SQLException
    {
        Connection conn = ConnectionManager.getConnection();
        PreparedStatement ps = conn.prepareStatement(
            "SELECT * FROM PERFIL WHERE id = ?"
        );

        ps.setInt(1, fkPerfil);

        ResultSet rs = ps.executeQuery();
        TbPerfil perfil = new TbPerfil();
        if (rs.next()) {
            perfil.setId(rs.getInt("id"));
            perfil.setDescricao(rs.getString("descricao"));
        }

        conn.close();
        return perfil;
    }
}
