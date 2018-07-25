package br.com.estruturart.persistency;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import br.com.estruturart.model.TbUnidadeMedida;

public class UnidadeMedida extends AbstractPersistency
{
    public List<TbUnidadeMedida> findAll() throws SQLException
    {
        Connection conn = ConnectionManager.getConnection();
        String sql = String.format("SELECT UNIDADE_MEDIDA.* FROM UNIDADE_MEDIDA order by nome ASC");

        PreparedStatement ps = conn.prepareStatement(sql);
        ResultSet rs = ps.executeQuery();

        List<TbUnidadeMedida> unidadesMedida = new ArrayList<TbUnidadeMedida>();
        while (rs.next()) {
            TbUnidadeMedida uMedidade = new TbUnidadeMedida();
            uMedidade.setId(rs.getInt("id"));
            uMedidade.setNome(rs.getString("nome"));

            unidadesMedida.add(uMedidade);
        }

        conn.close();
        return unidadesMedida;
    }
}
