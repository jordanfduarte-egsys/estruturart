package br.com.estruturart.persistency;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import com.mysql.jdbc.Statement;
import br.com.estruturart.model.TbPedido;
import java.text.SimpleDateFormat;

public class Pedido extends AbstractPersistency
{
    public int insert(TbPedido pedido) throws SQLException
    {
        Connection conn = null;
        if (isConnection()) {
            conn = getConnection();
        } else {
            conn = ConnectionManager.getConnection();
        }

        SimpleDateFormat df = new SimpleDateFormat("yyy-MM-dd");
        String sql = String.format(
            "INSERT INTO PEDIDO (caminho_arquivo_nota_fiscal, data_previsao_instalacao, valor_total, valor_mao_obra, pedido_pago, observacao, usuario_id, status_pedido_id, desconto_geral)"
            + " VALUES ('%s', '%s', '%s', '%s', %d, '%s', %d, %d, '%s')",
            pedido.getCaminhoArquivoNotaFiscal(), df.format(pedido.getDataPrevisaoInstalacao().getTime()), pedido.getValorTotal(), pedido.getValorMaoObra(),
            pedido.getPedidoPago(), pedido.getObservacao(), pedido.getUsuarioId(), pedido.getStatusPedidoId(),
            pedido.getDescontoGeral()
        );

        PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
        ps.execute();
        System.out.println(sql);

        ResultSet rs = ps.getGeneratedKeys();
        pedido.setId(rs.next() ? rs.getInt(1) : 0);

        if (!isConnection()) {
            conn.close();
        }

        return pedido.getId();
    }
}
