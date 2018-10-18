package br.com.estruturart.persistency;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.mysql.jdbc.Statement;

import br.com.estruturart.model.TbPerfil;
import br.com.estruturart.model.TbStatusUsuario;
import br.com.estruturart.model.TbUsuario;
import br.com.estruturart.utility.Paginator;
import br.com.estruturart.utility.ParamRequestManager;

public class Usuario extends AbstractPersistency
{
    public TbUsuario getUsuario(String email, String senha) throws Exception
    {
        Connection conn = ConnectionManager.getConnection();
        PreparedStatement ps = conn.prepareStatement("SELECT * FROM USUARIO WHERE email = ? AND senha = MD5(?)");

        ps.setString(1, email);
        ps.setString(2, senha);

        ResultSet rs = ps.executeQuery();
        TbUsuario usuario = new TbUsuario();
        if (rs.next()) {
            usuario.setId(rs.getInt("id"));
            usuario.setTipoPessoa(rs.getString("tipo_pessoa"));
            usuario.setNome(rs.getString("nome"));
            usuario.setCpfCnpj(rs.getString("cpf_cnpj"));
            usuario.setRgIncricaoEstadual(rs.getString("rg_inscricao_estadual"));
            usuario.setEmail(rs.getString("email"));
            usuario.setTelefone(rs.getString("telefone"));
            usuario.setCodigo(rs.getString("codigo"));
            usuario.setSenha(rs.getString("senha"));
            usuario.setDataInclusao(rs.getDate("data_inclusao"));
            usuario.setPerfilId(rs.getInt("perfil_id"));
            usuario.setStatusUsuarioId(rs.getInt("status_usuario_id"));
        }

        conn.close();
        return usuario;
    }

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
        if (filter.hasParam("busca_nome")) {
            sqlNome = " AND nome LIKE \"%" + filter.getParam("busca_nome") + "%\" ";
        }

        if (filter.hasParam("busca_nome_rapido")) {
            sqlNome = " AND nome LIKE \"%" + filter.getParam("busca_nome_rapido") + "%\" ";
        }

        String sqlCpfCnpj = "";
        if (filter.hasParam("cpf_cnpj")) {
            sqlCpfCnpj = " AND cpf_cnpj LIKE \"" + filter.getParam("cpf_cnpj") + "%\" ";
        }

        String sqlRgIncricaoEstadual = "";
        if (filter.hasParam("rg_incricao_estadual")) {
            sqlRgIncricaoEstadual = " AND rg_inscricao_estadual LIKE \"" + filter.getParam("rg_incricao_estadual")
                    + "%\" ";
        }

        String sqlEmail = "";
        if (filter.hasParam("email")) {
            sqlEmail = " AND email LIKE \"%" + filter.getParam("email") + "%\" ";
        }

        String columns = String
                .format("USUARIO.*, perfil.descricao as desc_perfil, status_usuario.descricao as desc_status");
        String limit = String.format("LIMIT %d, %d", pageAux, offset);

        String sql = String.format(
            "SELECT {columns} FROM USUARIO" + " INNER JOIN perfil ON USUARIO.perfil_id = perfil.id"
            + " INNER JOIN status_usuario ON USUARIO.status_usuario_id = status_usuario.id"
            + " WHERE 1 %s %s %s %s " + " order by USUARIO.id DESC {limit}",
            sqlNome, sqlCpfCnpj, sqlRgIncricaoEstadual, sqlEmail
        );

        PreparedStatement ps = conn.prepareStatement(sql.replace("{columns}", columns).replace("{limit}", limit));
        System.out.println(sql);
        System.out.println(sqlNome);
        System.out.println(sqlCpfCnpj);
        System.out.println(sqlRgIncricaoEstadual);
        System.out.println(sqlEmail);

        ResultSet rs = ps.executeQuery();

        List<TbUsuario> usuarios = new ArrayList<TbUsuario>();
        while (rs.next()) {
            TbUsuario usuario = new TbUsuario();
            usuario.setId(rs.getInt("id"));
            usuario.setTipoPessoa(rs.getString("tipo_pessoa"));
            usuario.setNome(rs.getString("nome"));
            usuario.setCpfCnpj(rs.getString("cpf_cnpj"));
            usuario.setRgIncricaoEstadual(rs.getString("rg_inscricao_estadual"));
            usuario.setEmail(rs.getString("email"));
            usuario.setTelefone(rs.getString("telefone"));
            usuario.setCodigo(rs.getString("codigo"));
            usuario.setSenha(rs.getString("senha"));
            usuario.setDataInclusao(rs.getDate("data_inclusao"));
            usuario.setPerfilId(rs.getInt("perfil_id"));
            usuario.setStatusUsuarioId(rs.getInt("status_usuario_id"));

            TbPerfil perfil = new TbPerfil();
            perfil.setId(rs.getInt("perfil_id"));
            perfil.setDescricao(rs.getString("desc_perfil"));

            TbStatusUsuario status = new TbStatusUsuario();
            status.setId(rs.getInt("status_usuario_id"));
            status.setDescricao(rs.getString("desc_status"));

            usuario.setPerfil(perfil);
            usuario.setStatusUsuario(status);

            usuarios.add(usuario);
        }

        conn.close();
        return new Paginator(usuarios, this.findTotalRows(sql), offset, page);
    }

    public TbUsuario getUsuarioById(int fkUsuario) throws Exception
    {
        Connection conn = ConnectionManager.getConnection();
        PreparedStatement ps = conn
            .prepareStatement(
                "SELECT USUARIO.*, p.descricao as desc_perfil, s.descricao as desc_status FROM USUARIO "
                + "INNER JOIN PERFIL p ON USUARIO.perfil_id = p.id "
                + "INNER JOIN STATUS_USUARIO s ON USUARIO.status_usuario_id = s.id WHERE USUARIO.id = ?"
            );

        ps.setInt(1, fkUsuario);

        ResultSet rs = ps.executeQuery();
        TbUsuario usuario = new TbUsuario();
        if (rs.next()) {
            usuario.setId(rs.getInt("id"));
            usuario.setTipoPessoa(rs.getString("tipo_pessoa"));
            usuario.setNome(rs.getString("nome"));
            usuario.setCpfCnpj(rs.getString("cpf_cnpj"));
            usuario.setRgIncricaoEstadual(rs.getString("rg_inscricao_estadual"));
            usuario.setEmail(rs.getString("email"));
            usuario.setTelefone(rs.getString("telefone"));
            usuario.setCodigo(rs.getString("codigo"));
            usuario.setSenha(rs.getString("senha"));
            usuario.setDataInclusao(rs.getDate("data_inclusao"));
            usuario.setPerfilId(rs.getInt("perfil_id"));
            usuario.setStatusUsuarioId(rs.getInt("status_usuario_id"));

            TbPerfil perfil = new TbPerfil();
            perfil.setId(rs.getInt("perfil_id"));
            perfil.setDescricao(rs.getString("desc_perfil"));

            TbStatusUsuario status = new TbStatusUsuario();
            status.setId(rs.getInt("status_usuario_id"));
            status.setDescricao(rs.getString("desc_status"));

            usuario.setPerfil(perfil);
            usuario.setStatusUsuario(status);
        }

        conn.close();
        return usuario;
    }

    public int insert(TbUsuario usuario) throws SQLException
    {
        Connection conn = null;
        if (isConnection()) {
            conn = getConnection();
        } else {
            conn = ConnectionManager.getConnection();
        }

        String sql = String.format(
            "INSERT INTO USUARIO (tipo_pessoa, nome, cpf_cnpj, rg_inscricao_estadual, email, telefone, senha, perfil_id, status_usuario_id)"
            + " VALUES ('%s', '%s', '%s', '%s', '%s', '%s', MD5('%s'), %d, %d)",
            usuario.getTipoPessoa(), usuario.getNome(), usuario.getCpfCnpj(), usuario.getRgIncricaoEstadual(),
            usuario.getEmail(), usuario.getTelefone(), usuario.getSenha(), usuario.getPerfilId(), 2
        );
        if (usuario.getPerfilId() == 1) {
            sql = String.format(
                "INSERT INTO USUARIO (tipo_pessoa, nome, cpf_cnpj, rg_inscricao_estadual, email, telefone, senha, perfil_id, status_usuario_id)"
                + " VALUES ('%s', '%s', '%s', '%s', '%s', '%s', %d, %d)",
                usuario.getTipoPessoa(), usuario.getNome(), usuario.getCpfCnpj(), usuario.getRgIncricaoEstadual(),
                usuario.getEmail(), usuario.getTelefone(), usuario.getPerfilId(), 2
            );
        }

        PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
        ps.execute();
        System.out.println(sql);

        ResultSet rs = ps.getGeneratedKeys();
        usuario.setId(rs.next() ? rs.getInt(1) : 0);

        if (usuario.getId() != 0) {
            System.out.println("SALVO ?" + usuario.getId());

            sql = String.format("UPDATE USUARIO SET codigo = '%s' WHERE id = %d", usuario.generateCode(),
                    usuario.getId());
            ps = conn.prepareStatement(sql);
            ps.executeUpdate();
        }

        if (!isConnection()) {
            conn.close();
        }

        return usuario.getId();
    }

    public int updateSemSenha(TbUsuario usuario) throws SQLException
    {
        Connection conn = ConnectionManager.getConnection();
        String sql = String.format(
            "UPDATE USUARIO SET tipo_pessoa = '%s', nome = '%s', cpf_cnpj = '%s', rg_inscricao_estadual = '%s', email = '%s', telefone = '%s', perfil_id = %d, status_usuario_id = %d "
            + "WHERE id = %d",
            usuario.getTipoPessoa(), usuario.getNome(), usuario.getCpfCnpj(), usuario.getRgIncricaoEstadual(),
            usuario.getEmail(), usuario.getTelefone(), usuario.getPerfilId(), usuario.getStatusUsuarioId(), usuario.getId()
        );

        PreparedStatement ps = conn.prepareStatement(sql);
        int rows = ps.executeUpdate();

        conn.close();
        return rows;
    }

    public int update(TbUsuario usuario) throws SQLException
    {
        Connection conn = ConnectionManager.getConnection();
        String sql = String.format(
            "UPDATE USUARIO SET tipo_pessoa = '%s', nome = '%s', cpf_cnpj = '%s', rg_inscricao_estadual = '%s', email = '%s', telefone = '%s', senha = MD5('%s'), perfil_id = %d "
            + "WHERE id = %d",
            usuario.getTipoPessoa(), usuario.getNome(), usuario.getCpfCnpj(), usuario.getRgIncricaoEstadual(),
            usuario.getEmail(), usuario.getTelefone(), usuario.getSenha(), usuario.getPerfilId(), usuario.getId()
        );

        PreparedStatement ps = conn.prepareStatement(sql);
        int rows = ps.executeUpdate();

        conn.close();
        return rows;
    }

    public boolean findCpfCnpjExists(String cpfCnpj, int fkUsuarioEdicao) throws SQLException
    {
        Connection conn = ConnectionManager.getConnection();

        PreparedStatement ps = conn.prepareStatement("SELECT * FROM USUARIO WHERE cpf_cnpj = ?");
        ps.setString(1, cpfCnpj);
        System.out.println("EDICAO" + fkUsuarioEdicao);
        if (fkUsuarioEdicao != 0) {
            ps = conn.prepareStatement("SELECT * FROM USUARIO WHERE cpf_cnpj = ? AND id <> ?");

            ps.setString(1, cpfCnpj);
            ps.setInt(2, fkUsuarioEdicao);
        }

        ResultSet rs = ps.executeQuery();
        if (rs.next()) {
            return true;
        }

        conn.close();
        return false;
    }

    public boolean findEmailExists(String email, int fkUsuarioEdicao) throws SQLException
    {
        Connection conn = ConnectionManager.getConnection();

        PreparedStatement ps = conn.prepareStatement("SELECT * FROM USUARIO WHERE email = ?");
        ps.setString(1, email);

        if (fkUsuarioEdicao != 0) {
            ps = conn.prepareStatement("SELECT * FROM USUARIO WHERE email = ? AND id <> ?");

            ps.setString(1, email);
            ps.setInt(2, fkUsuarioEdicao);
        }

        ResultSet rs = ps.executeQuery();
        if (rs.next()) {
            return true;
        }

        conn.close();
        return false;
    }

    public TbUsuario findUsuarioByCpjCNpj(String cpfCnpj) throws SQLException
    {
        Connection conn = ConnectionManager.getConnection();

        PreparedStatement ps = conn.prepareStatement(
            "SELECT * FROM USUARIO WHERE cpf_cnpj LIKE \"%" + cpfCnpj + "%\" "
        );

        TbUsuario usuario = new TbUsuario();
        ResultSet rs = ps.executeQuery();
        if (rs.next()) {
            usuario.setId(rs.getInt("id"));
            usuario.setTipoPessoa(rs.getString("tipo_pessoa"));
            usuario.setNome(rs.getString("nome"));
            usuario.setCpfCnpj(rs.getString("cpf_cnpj"));
            usuario.setRgIncricaoEstadual(rs.getString("rg_inscricao_estadual"));
            usuario.setEmail(rs.getString("email"));
            usuario.setTelefone(rs.getString("telefone"));
            usuario.setCodigo(rs.getString("codigo"));
            usuario.setSenha(rs.getString("senha"));
            usuario.setDataInclusao(rs.getDate("data_inclusao"));
            usuario.setPerfilId(rs.getInt("perfil_id"));
            usuario.setStatusUsuarioId(rs.getInt("status_usuario_id"));
        }

        conn.close();
        return usuario;
    }

    public TbUsuario findUsuarioByUsuarioSenha(String usuarioStr, String senhaStr) throws SQLException
    {
        Connection conn = ConnectionManager.getConnection();

        PreparedStatement ps = conn.prepareStatement(
            "SELECT * FROM USUARIO WHERE (email = \"" + usuarioStr + "\" OR cpf_cnpj = \"" + usuarioStr +"\") "
            + " AND senha = MD5(\"" + senhaStr + "\") "
            + " AND perfil_id = 2 "
            + " AND senha IS NOT NULL "
            + " AND senha != ''"
        );

        TbUsuario usuario = new TbUsuario();
        ResultSet rs = ps.executeQuery();
        if (rs.next()) {
            usuario.setId(rs.getInt("id"));
            usuario.setTipoPessoa(rs.getString("tipo_pessoa"));
            usuario.setNome(rs.getString("nome"));
            usuario.setCpfCnpj(rs.getString("cpf_cnpj"));
            usuario.setRgIncricaoEstadual(rs.getString("rg_inscricao_estadual"));
            usuario.setEmail(rs.getString("email"));
            usuario.setTelefone(rs.getString("telefone"));
            usuario.setCodigo(rs.getString("codigo"));
            usuario.setSenha(rs.getString("senha"));
            usuario.setDataInclusao(rs.getDate("data_inclusao"));
            usuario.setPerfilId(rs.getInt("perfil_id"));
            usuario.setStatusUsuarioId(rs.getInt("status_usuario_id"));
        }

        conn.close();
        return usuario;
    }
}
