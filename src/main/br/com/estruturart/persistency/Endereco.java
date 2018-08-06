package br.com.estruturart.persistency;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import com.mysql.jdbc.Statement;
import br.com.estruturart.model.TbEndereco;

public class Endereco extends AbstractPersistency
{
    public int insert(TbEndereco endereco) throws SQLException
    {
        Connection conn = null;
        if (isConnection()) {
            conn = getConnection();
        } else {
            conn = ConnectionManager.getConnection();
        }

        String sql = String.format(
            "INSERT INTO ENDERECO (cep, logradouro, bairro, numero, complemento, cidade_id, usuario_id, pedido_id)"
            + " VALUES ('%s', '%s', '%s', '%s', '%s', %d, %d, %d)",
            endereco.getCep(), endereco.getLogradouro(), endereco.getBairro(), endereco.getNumero(),
            endereco.getComplemento(), endereco.getCidadeId(), endereco.getUsuarioId(), endereco.getPedidoId()
        );

        PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
        ps.execute();
        System.out.println(sql);

        ResultSet rs = ps.getGeneratedKeys();
        endereco.setId(rs.next() ? rs.getInt(1) : 0);

        if (!isConnection()) {
            conn.close();
        }

        return endereco.getId();
    }

    public void update(TbEndereco endereco) throws SQLException
    {
        Connection conn = null;
        if (isConnection()) {
            conn = getConnection();
        } else {
            conn = ConnectionManager.getConnection();
        }

        String sql = String.format(
            "UPDATE ENDERECO SET cep = '%s', logradouro = '%s', bairro = '%s', numero = '%s', complemento = '%s', cidade_id = %d"
            + " WHERE pedido_id = %d",
            endereco.getCep(), endereco.getLogradouro(), endereco.getBairro(), endereco.getNumero(),
            endereco.getComplemento(), endereco.getCidadeId(), endereco.getPedidoId()
        );

        PreparedStatement ps = conn.prepareStatement(sql);
        ps.execute();
        System.out.println(sql);

        if (!isConnection()) {
            conn.close();
        }
    }
}
