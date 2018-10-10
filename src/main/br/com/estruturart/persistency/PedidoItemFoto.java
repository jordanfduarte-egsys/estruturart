package br.com.estruturart.persistency;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Date;
import com.mysql.jdbc.Statement;
import br.com.estruturart.model.TbPedidoItemFoto;

public class PedidoItemFoto extends AbstractPersistency
{
    public List<TbPedidoItemFoto> findItemFoto(int itemId) throws java.sql.SQLException
    {
        Connection conn = ConnectionManager.getConnection();
        String sql = String.format(
            "SELECT f.* FROM pedido_itens_fotos f WHERE f.pedido_itens_id = %d ORDER BY data_inclusao", itemId
        );

        PreparedStatement ps = conn.prepareStatement(sql);
        ResultSet rs = ps.executeQuery();
        List<TbPedidoItemFoto> fotos = new ArrayList<TbPedidoItemFoto>();
        while (rs.next()) {
            TbPedidoItemFoto foto = new TbPedidoItemFoto();

            foto.setId(rs.getInt("id"));
            foto.setPedidoItensId(rs.getInt("pedido_itens_id"));
            foto.setDataInclusao(new Date(rs.getTimestamp("data_inclusao").getTime()));
            foto.setObservacao(rs.getString("observacao"));
            foto.setCaminhoArquivo(rs.getString("caminho_arquivo"));
            fotos.add(foto);
        }

        return fotos;
    }

    public int insert(TbPedidoItemFoto itemFoto) throws java.sql.SQLException
    {
        Connection conn = null;
        if (isConnection()) {
            conn = getConnection();
        } else {
            conn = ConnectionManager.getConnection();
        }

        String sql = String.format(
            "INSERT INTO PEDIDO_ITENS_FOTOS (caminho_arquivo, observacao, pedido_itens_id)"
            + " VALUES ('%s', '%s', %d)",
            itemFoto.getCaminhoArquivo(), itemFoto.getObservacao(), itemFoto.getPedidoItensId()
        );

        PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
        ps.execute();
        System.out.println(sql);

        ResultSet rs = ps.getGeneratedKeys();
        itemFoto.setId(rs.next() ? rs.getInt(1) : 0);

        if (!isConnection()) {
            conn.close();
        }

        return itemFoto.getId();
    }

    public List<TbPedidoItemFoto> findFotos(int itemId, int idFotoId) throws java.sql.SQLException
    {
        Connection conn = ConnectionManager.getConnection();
        String sql = String.format(
            "SELECT f.* FROM pedido_itens_fotos f WHERE f.pedido_itens_id = %d ORDER BY data_inclusao DESC LIMIT 4", itemId
        );

        if (idFotoId > 0) {
            sql = String.format(
                "SELECT f.* FROM pedido_itens_fotos f WHERE f.pedido_itens_id = %d AND id < %d ORDER BY data_inclusao DESC LIMIT 4", itemId, idFotoId
            );
        }
System.out.println("SQL: " + sql);
        PreparedStatement ps = conn.prepareStatement(sql);
        ResultSet rs = ps.executeQuery();
        List<TbPedidoItemFoto> fotos = new ArrayList<TbPedidoItemFoto>();
        while (rs.next()) {
            TbPedidoItemFoto foto = new TbPedidoItemFoto();

            foto.setId(rs.getInt("id"));
            foto.setPedidoItensId(rs.getInt("pedido_itens_id"));
            foto.setDataInclusao(new Date(rs.getTimestamp("data_inclusao").getTime()));
            foto.setObservacao(rs.getString("observacao"));
            foto.setCaminhoArquivo(rs.getString("caminho_arquivo"));
            fotos.add(foto);
        }

        return fotos;
    }
}