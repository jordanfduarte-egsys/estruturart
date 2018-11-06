package br.com.estruturart.persistency;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.mysql.jdbc.Statement;

import br.com.estruturart.model.TbFornecedor;
import br.com.estruturart.utility.Paginator;
import br.com.estruturart.utility.ParamRequestManager;

public class Fornecedor extends AbstractPersistency
{
    public Paginator findAllPaginated(int page, int offset, ParamRequestManager filter) throws SQLException
    {
        Connection conn = ConnectionManager.getConnection();

        int pageAux = page;
        if (pageAux == 1) {
            pageAux = 0;
        } else {
            pageAux = pageAux * offset - offset;
        }

        String sqlNome = "";
        if (filter.hasParam("nome")) {
            sqlNome = " AND nome LIKE \"%" + filter.getParam("nome") + "%\" ";
        }

        String columns = String.format("FORNECEDOR.*");
        String limit = String.format("LIMIT %d, %d", pageAux, offset);

        String sql = String.format(
            "SELECT {columns} FROM FORNECEDOR" +
            " WHERE 1 %s " + " order by id DESC {limit}",
            sqlNome
        );

        PreparedStatement ps = conn.prepareStatement(sql.replace("{columns}", columns).replace("{limit}", limit));



        ResultSet rs = ps.executeQuery();

        List<TbFornecedor> fornecedores = new ArrayList<TbFornecedor>();
        while (rs.next()) {
            TbFornecedor fornecedor = new TbFornecedor();
            fornecedor.setId(rs.getInt("id"));
            fornecedor.setNome(rs.getString("nome"));
            fornecedor.setStatus(rs.getInt("status"));
            fornecedor.setTelefone(rs.getString("telefone"));
            fornecedor.setDataInclusao(rs.getDate("data_inclusao"));
            fornecedores.add(fornecedor);
        }

        conn.close();
        return new Paginator(fornecedores, this.findTotalRows(sql), offset, page);
    }

    public TbFornecedor getFornecedorById(int fkFornecedor) throws Exception
    {
        Connection conn = ConnectionManager.getConnection();
        PreparedStatement ps = conn
                .prepareStatement("SELECT FORNECEDOR.* FROM FORNECEDOR " + "WHERE FORNECEDOR.id = ?");

        ps.setInt(1, fkFornecedor);

        ResultSet rs = ps.executeQuery();
        TbFornecedor fornecedor = new TbFornecedor();
        if (rs.next()) {
            fornecedor.setId(rs.getInt("id"));
            fornecedor.setNome(rs.getString("nome"));
            fornecedor.setStatus(rs.getInt("status"));
            fornecedor.setTelefone(rs.getString("telefone"));
            fornecedor.setDataInclusao(rs.getDate("data_inclusao"));
        }

        conn.close();
        return fornecedor;
    }

    public int insert(TbFornecedor fornecedor) throws SQLException
    {
        Connection conn = ConnectionManager.getConnection();
        String sql = String.format(
            "INSERT INTO FORNECEDOR (nome, status, telefone)" + " VALUES ('%s', %d, '%s')",
            fornecedor.getNome(),
            1,
            fornecedor.getTelefone()
        );

        PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
        ps.execute();


        ResultSet rs = ps.getGeneratedKeys();
        fornecedor.setId(rs.next() ? rs.getInt(1) : 0);
        conn.close();
        return fornecedor.getId();
    }

    public int update(TbFornecedor fornecedor) throws SQLException
    {
        Connection conn = ConnectionManager.getConnection();
        String sql = String.format("UPDATE FORNECEDOR SET nome = '%s', telefone = '%s' WHERE id = %d",
            fornecedor.getNome(),
            fornecedor.getTelefone(),
            fornecedor.getId()
        );

        PreparedStatement ps = conn.prepareStatement(sql);
        int rows = ps.executeUpdate();

        conn.close();
        return rows;
    }

    public int updateStatus(TbFornecedor fornecedor) throws SQLException
    {
        Connection conn = ConnectionManager.getConnection();
        String sql = String.format("UPDATE FORNECEDOR SET nome = '%s', telefone = '%s', status = %d WHERE id = %d",
            fornecedor.getNome(),
            fornecedor.getTelefone(),
            fornecedor.getStatus(),
            fornecedor.getId()
        );

        PreparedStatement ps = conn.prepareStatement(sql);
        int rows = ps.executeUpdate();

        conn.close();
        return rows;
    }

    public boolean findFornecedorByNome(String nome, int fkFornecedorEdicao) throws SQLException
    {
        Connection conn = ConnectionManager.getConnection();

        PreparedStatement ps = conn.prepareStatement("SELECT * FROM FORNECEDOR WHERE LOWER(nome) = LOWER(?)");
        ps.setString(1, nome);

        if (fkFornecedorEdicao != 0) {
            ps = conn.prepareStatement("SELECT * FROM FORNECEDOR WHERE LOWER(nome) = LOWER(?) AND id <> ?");

            ps.setString(1, nome);
            ps.setInt(2, fkFornecedorEdicao);
        }

        ResultSet rs = ps.executeQuery();
        if (rs.next()) {
            return true;
        }

        conn.close();
        return false;
    }

    public List<TbFornecedor> findAll() throws SQLException
    {
        Connection conn = ConnectionManager.getConnection();

        String sql = String.format("SELECT FORNECEDOR.* FROM FORNECEDOR WHERE status = 1 order by nome ASC");

        PreparedStatement ps = conn.prepareStatement(sql);
        ResultSet rs = ps.executeQuery();

        List<TbFornecedor> fornecedores = new ArrayList<TbFornecedor>();
        while (rs.next()) {
            TbFornecedor fornecedor = new TbFornecedor();
            fornecedor.setId(rs.getInt("id"));
            fornecedor.setNome(rs.getString("nome"));
            fornecedor.setTelefone(rs.getString("telefone"));
            fornecedor.setStatus(rs.getInt("status"));
            fornecedor.setDataInclusao(rs.getDate("data_inclusao"));

            fornecedores.add(fornecedor);
        }

        conn.close();
        return fornecedores;
    }
}
