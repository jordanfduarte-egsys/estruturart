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

public class Cidade extends AbstractPersistency {
    public TbCidade findCidadeByName(String nome) throws Exception {
        Connection conn = ConnectionManager.getConnection();
        PreparedStatement ps = conn
                .prepareStatement("SELECT CIDADE.*, ESTADO.nome as nome_estado FROM CIDADE INNER JOIN ESTADO ON CIDADE.estado_id = ESTADO.id WHERE CIDADE.NOME LIKE \"%" + nome + "%\" ");

        ResultSet rs = ps.executeQuery();
        TbCidade cidade = new TbCidade();
        if (rs.next()) {
            TbEstado estado = new TbEstado();
            cidade.setId(rs.getInt("id"));
            cidade.setEstadoid(rs.getInt("estado_id"));
            cidade.setUf(rs.getString("uf"));
            cidade.setNome(rs.getString("nome"));

            estado.setId(rs.getInt("estado_id"));
            estado.setNome(rs.getString("nome_estado"));
            estado.setUf(rs.getString("uf"));
            cidade.setEstado(estado);
        }

        conn.close();
        return cidade;
    }

    public List<TbCidade> findCidadeByEstado(String estadoId, boolean selecione) throws Exception {
        Connection conn = ConnectionManager.getConnection();
        PreparedStatement ps = conn
                .prepareStatement("SELECT CIDADE.*, ESTADO.nome as nome_estado FROM CIDADE INNER JOIN ESTADO ON CIDADE.estado_id = ESTADO.id WHERE CIDADE.estado_id = \"" + estadoId + "\" ");

        ResultSet rs = ps.executeQuery();
        List<TbCidade> cidades = new ArrayList<TbCidade>();
        TbCidade cidade = new TbCidade();
        if (selecione) {
            cidade.setId(0);
            cidade.setNome("Selecione!");
            cidades.add(cidade);
        }

        while (rs.next()) {
            cidade = new TbCidade();
            TbEstado estado = new TbEstado();
            cidade.setId(rs.getInt("id"));
            cidade.setEstadoid(rs.getInt("estado_id"));
            cidade.setUf(rs.getString("uf"));
            cidade.setNome(rs.getString("nome"));

            estado.setId(rs.getInt("estado_id"));
            estado.setNome(rs.getString("nome_estado"));
            estado.setUf(rs.getString("uf"));
            cidade.setEstado(estado);
            cidades.add(cidade);
        }

        conn.close();
        return cidades;
    }
}