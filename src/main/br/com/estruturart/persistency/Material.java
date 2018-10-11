package br.com.estruturart.persistency;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.mysql.jdbc.Statement;

import br.com.estruturart.model.TbFornecedor;
import br.com.estruturart.model.TbMaterial;
import br.com.estruturart.model.TbStatusMaterial;
import br.com.estruturart.model.TbUnidadeMedida;
import br.com.estruturart.utility.Paginator;
import br.com.estruturart.utility.ParamRequestManager;

public class Material extends AbstractPersistency
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
        if (filter.hasParam("descricao")) {
            sqlNome = " AND MATERIAL.descricao LIKE \"%" + filter.getParam("descricao") + "%\" ";
        }

        String columns = String.format(
            "MATERIAL.*, UNIDADE_MEDIDA.nome as nome_unidade_medida, STATUS_MATERIAL.descricao as descricao_status, FORNECEDOR.nome as nome_fornecedor, FORNECEDOR.status as status_fornecedor"
        );
        String limit = String.format("LIMIT %d, %d", pageAux, offset);

        String sql = String.format("SELECT {columns} FROM MATERIAL"
            + " INNER JOIN STATUS_MATERIAL ON MATERIAL.status_material_id = STATUS_MATERIAL.id LEFT JOIN UNIDADE_MEDIDA ON MATERIAL.unidade_medida_id = UNIDADE_MEDIDA.id INNER JOIN FORNECEDOR ON MATERIAL.fornecedor_id = FORNECEDOR.id WHERE 1 %s "
            + " order by MATERIAL.id DESC {limit}",
            sqlNome
        );

        PreparedStatement ps = conn.prepareStatement(sql.replace("{columns}", columns).replace("{limit}", limit));
        System.out.println(sql);
        System.out.println(sqlNome);

        ResultSet rs = ps.executeQuery();

        List<TbMaterial> materiais = new ArrayList<TbMaterial>();
        while (rs.next()) {
            TbMaterial material = new TbMaterial();
            material.setId(rs.getInt("id"));
            material.setDescricao(rs.getString("descricao"));
            material.setMateriaPrima(rs.getInt("materia_prima"));
            material.setDataInclusao(rs.getDate("data_inclusao"));
            material.setStatusMaterialId(rs.getInt("status_material_id"));
            material.setUnidadeMedidaId(rs.getInt("unidade_medida_id"));
            material.setFornecedorId(rs.getInt("fornecedor_id"));
            material.setPreco(rs.getFloat("preco"));
            material.setQuantidade(rs.getInt("quantidade"));

            TbStatusMaterial statusMaterial = new TbStatusMaterial();
            statusMaterial.setId(rs.getInt("status_material_id"));
            statusMaterial.setDescricao(rs.getString("descricao_status"));
            material.setStatusMaterial(statusMaterial);

            TbUnidadeMedida unidadeMedida = new TbUnidadeMedida();
            unidadeMedida.setId(rs.getInt("unidade_medida_id"));
            unidadeMedida.setNome(rs.getString("nome_unidade_medida"));
            material.setUnidadeMedida(unidadeMedida);

            TbFornecedor fornecedor = new TbFornecedor();
            fornecedor.setId(rs.getInt("fornecedor_id"));
            fornecedor.setNome(rs.getString("nome_fornecedor"));
            fornecedor.setStatus(rs.getInt("status_fornecedor"));
            material.setFornecedor(fornecedor);

            materiais.add(material);
        }

        conn.close();
        return new Paginator(materiais, this.findTotalRows(sql), offset, page);
    }

    public TbMaterial getMaterialById(int fkMaterial) throws Exception
    {
        Connection conn = ConnectionManager.getConnection();
        PreparedStatement ps = conn
            .prepareStatement(
                "SELECT MATERIAL.*, UNIDADE_MEDIDA.nome as nome_unidade_medida, STATUS_MATERIAL.descricao as descricao_status, FORNECEDOR.nome as nome_fornecedor, FORNECEDOR.status as status_fornecedor FROM MATERIAL "
                + " INNER JOIN STATUS_MATERIAL ON MATERIAL.status_material_id = STATUS_MATERIAL.id LEFT JOIN UNIDADE_MEDIDA ON MATERIAL.unidade_medida_id = UNIDADE_MEDIDA.id INNER JOIN FORNECEDOR ON MATERIAL.fornecedor_id = FORNECEDOR.id"
                + " WHERE MATERIAL.id = ?"
            );

        ps.setInt(1, fkMaterial);
        ResultSet rs = ps.executeQuery();
        TbMaterial material = new TbMaterial();
        if (rs.next()) {
            material.setId(rs.getInt("id"));
            material.setDescricao(rs.getString("descricao"));
            material.setMateriaPrima(rs.getInt("materia_prima"));
            material.setDataInclusao(rs.getDate("data_inclusao"));
            material.setStatusMaterialId(rs.getInt("status_material_id"));
            material.setUnidadeMedidaId(rs.getInt("unidade_medida_id"));
            material.setFornecedorId(rs.getInt("fornecedor_id"));
            material.setPreco(rs.getFloat("preco"));
            material.setQuantidade(rs.getInt("quantidade"));

            TbStatusMaterial statusMaterial = new TbStatusMaterial();
            statusMaterial.setId(rs.getInt("status_material_id"));
            statusMaterial.setDescricao(rs.getString("descricao_status"));
            material.setStatusMaterial(statusMaterial);

            TbUnidadeMedida unidadeMedida = new TbUnidadeMedida();
            unidadeMedida.setId(rs.getInt("unidade_medida_id"));
            unidadeMedida.setNome(rs.getString("nome_unidade_medida"));
            material.setUnidadeMedida(unidadeMedida);

            TbFornecedor fornecedor = new TbFornecedor();
            fornecedor.setId(rs.getInt("fornecedor_id"));
            fornecedor.setNome(rs.getString("nome_fornecedor"));
            fornecedor.setStatus(rs.getInt("status_fornecedor"));
            material.setFornecedor(fornecedor);
        }

        conn.close();
        return material;
    }

    public int insert(TbMaterial material) throws SQLException
    {
        Connection conn = ConnectionManager.getConnection();
        String sql = String.format(
                "INSERT INTO MATERIAL (descricao, materia_prima, status_material_id, unidade_medida_id, fornecedor_id, preco, quantidade)"
                        + " VALUES ('%s', %d, %d, %d, %d, '%s', %d)",
                material.getDescricao(), material.getMateriaPrima(), material.getStatusMaterialId(),
                material.getUnidadeMedidaId(), material.getFornecedorId(), material.getPreco(),
                material.getQuantidade());

        PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
        ps.execute();
        System.out.println(sql);

        ResultSet rs = ps.getGeneratedKeys();
        material.setId(rs.next() ? rs.getInt(1) : 0);

        conn.close();
        return material.getId();
    }

    public int update(TbMaterial material) throws SQLException
    {
        Connection conn = ConnectionManager.getConnection();
        String sql = String.format(
                "UPDATE MATERIAL SET descricao = '%s', materia_prima = %d, status_material_id = %d, unidade_medida_id = %d, fornecedor_id = %d, preco = '%s', quantidade = %d "
                        + "WHERE id = %d",
                material.getDescricao(), material.getMateriaPrima(), material.getStatusMaterialId(),
                material.getUnidadeMedidaId(), material.getFornecedorId(), material.getPreco(),
                material.getQuantidade(), material.getId());

        PreparedStatement ps = conn.prepareStatement(sql);
        int rows = ps.executeUpdate();


        conn.close();
        return rows;
    }

    public boolean findMaterialByNome(String nome, int fkMAterialEdicao, int fkFornecedor) throws SQLException
    {
        Connection conn = ConnectionManager.getConnection();

        PreparedStatement ps = conn
            .prepareStatement("SELECT * FROM MATERIAL WHERE LOWER(descricao) = LOWER(?) AND fornecedor_id = ?");
        ps.setString(1, nome);
        ps.setInt(2, fkFornecedor);
        System.out.println("EDICAO" + fkMAterialEdicao);
        if (fkMAterialEdicao != 0) {
            ps = conn.prepareStatement(
                    "SELECT * FROM MATERIAL WHERE LOWER(descricao) = LOWER(?) AND id <> ? AND fornecedor_id = ?");

            ps.setString(1, nome);
            ps.setInt(2, fkMAterialEdicao);
            ps.setInt(3, fkFornecedor);
        }

        ResultSet rs = ps.executeQuery();
        if (rs.next()) {
            return true;
        }

        conn.close();
        return false;
    }

    public List<TbMaterial> findMaterialByNomeList(String nome) throws SQLException
    {
        Connection conn = ConnectionManager.getConnection();
        List<TbMaterial> materiaisList = new ArrayList<TbMaterial>();

        String sql = String.format(
            "SELECT m.*, STATUS_MATERIAL.descricao as descricao_status, UNIDADE_MEDIDA.nome as nome_unidade_medida, FORNECEDOR.nome as nome_fornecedor, FORNECEDOR.status as status_fornecedor"
            + " FROM MATERIAL m"
            + " INNER JOIN STATUS_MATERIAL ON m.status_material_id = STATUS_MATERIAL.id "
            + " LEFT JOIN UNIDADE_MEDIDA ON m.unidade_medida_id = UNIDADE_MEDIDA.id "
            + " INNER JOIN FORNECEDOR ON m.fornecedor_id = FORNECEDOR.id WHERE LOWER(m.descricao) LIKE \"%s%%\" AND m.status_material_id = 1 ORDER BY m.descricao ASC LIMIT 30",
            nome
        );

        System.out.println("SQL: " + sql);
        PreparedStatement ps = conn.prepareStatement(sql);
        ResultSet rs = ps.executeQuery();

        while (rs.next()) {
            TbMaterial material = new TbMaterial();
            material.setId(rs.getInt("id"));
            material.setDescricao(rs.getString("descricao"));
            material.setMateriaPrima(rs.getInt("materia_prima"));
            material.setDataInclusao(rs.getDate("data_inclusao"));
            material.setStatusMaterialId(rs.getInt("status_material_id"));
            material.setUnidadeMedidaId(rs.getInt("unidade_medida_id"));
            material.setFornecedorId(rs.getInt("fornecedor_id"));
            material.setPreco(rs.getFloat("preco"));
            material.setQuantidade(rs.getInt("quantidade"));
            material.setDescricaoFiltro(String.format("%s - Fornecedor %s - Quantidade - %s", rs.getString("descricao"),
                    rs.getString("nome_fornecedor"), rs.getString("quantidade")));

            TbStatusMaterial statusMaterial = new TbStatusMaterial();
            statusMaterial.setId(rs.getInt("status_material_id"));
            statusMaterial.setDescricao(rs.getString("descricao_status"));
            material.setStatusMaterial(statusMaterial);

            TbUnidadeMedida unidadeMedida = new TbUnidadeMedida();
            unidadeMedida.setId(rs.getInt("unidade_medida_id"));
            unidadeMedida.setNome(rs.getString("nome_unidade_medida"));
            material.setUnidadeMedida(unidadeMedida);

            TbFornecedor fornecedor = new TbFornecedor();
            fornecedor.setId(rs.getInt("fornecedor_id"));
            fornecedor.setNome(rs.getString("nome_fornecedor"));
            fornecedor.setStatus(rs.getInt("status_fornecedor"));
            material.setFornecedor(fornecedor);

            materiaisList.add(material);

        }

        conn.close();
        return materiaisList;
    }


    public List<TbMaterial> findAll() throws SQLException
    {
        Connection conn = ConnectionManager.getConnection();
        PreparedStatement ps = conn.prepareStatement("SELECT * FROM MATERIAL ORDER BY descricao");
        ResultSet rs = ps.executeQuery();
        List<TbMaterial> materiaisList = new ArrayList<TbMaterial>();
        while (rs.next()) {
            TbMaterial material = new TbMaterial();
            material.setId(rs.getInt("id"));
            material.setDescricao(rs.getString("descricao"));
            material.setMateriaPrima(rs.getInt("materia_prima"));
            material.setDataInclusao(rs.getDate("data_inclusao"));
            material.setStatusMaterialId(rs.getInt("status_material_id"));
            material.setUnidadeMedidaId(rs.getInt("unidade_medida_id"));
            material.setFornecedorId(rs.getInt("fornecedor_id"));
            material.setPreco(rs.getFloat("preco"));
            material.setQuantidade(rs.getInt("quantidade"));
            materiaisList.add(material);
        }
        conn.close();
        return materiaisList;
    }
}
