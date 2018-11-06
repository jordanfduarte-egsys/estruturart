package br.com.estruturart.persistency;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Date;
import com.mysql.jdbc.Statement;
import br.com.estruturart.model.TbLogPedido;
import br.com.estruturart.model.TbUsuario;
import br.com.estruturart.model.TbStatusPedido;

public class LogPedido extends AbstractPersistency
{
    public int insert(int statusPedidoId, int usuarioId, int pedidoId) throws SQLException
    {
        Connection conn = null;
        if (isConnection()) {
            conn = getConnection();
        } else {
            conn = ConnectionManager.getConnection();
        }

        String sql = String.format(
            "INSERT INTO LOG_PEDIDO (status_pedido_id, usuario_id, pedido_id)"
            + " VALUES (%d, %d, %d)",
            statusPedidoId, usuarioId, pedidoId
        );

        PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
        ps.execute();


        ResultSet rs = ps.getGeneratedKeys();
        int id = (rs.next() ? rs.getInt(1) : 0);

        if (!isConnection()) {
            conn.close();
        }

        return id;
    }

    public List<TbLogPedido> findLogPedido(int fkPedido) throws SQLException
    {
        Connection conn = null;
        if (isConnection()) {
            conn = getConnection();
        } else {
            conn = ConnectionManager.getConnection();
        }

        String sql = String.format(
            "SELECT l.*, p.nome as status_nome, u.nome as nome_usuario FROM log_pedido l "
            + " INNER JOIN STATUS_PEDIDO p ON l.status_pedido_id = p.id INNER JOIN USUARIO u ON l.usuario_id = u.id "
            + " WHERE pedido_id = %d ORDER BY l.data_inclusao",
            fkPedido
        );

        PreparedStatement ps = conn.prepareStatement(sql);
        ResultSet rs = ps.executeQuery();
        List<TbLogPedido> logs = new ArrayList<TbLogPedido>();
        while (rs.next()) {
            TbLogPedido log = new TbLogPedido();
            TbUsuario usuario = new TbUsuario();
            TbStatusPedido status = new TbStatusPedido();
            log.setId(rs.getInt("id"));
            log.setStatusPedidoId(rs.getInt("status_pedido_id"));
            log.setUsuarioId(rs.getInt("usuario_id"));
            log.setDataInclusao(new Date(rs.getTimestamp("data_inclusao").getTime()));
            log.setPedidoId(rs.getInt("pedido_id"));

            usuario.setId(rs.getInt("usuario_id"));
            usuario.setNome(rs.getString("nome_usuario"));

            status.setId(rs.getInt("status_pedido_id"));
            status.setNome(rs.getString("status_nome"));

            log.setUsuario(usuario);
            log.setStatusPedido(status);

            logs.add(log);
        }

        conn.close();
        return logs;
    }
}
