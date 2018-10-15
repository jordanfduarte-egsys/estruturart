package br.com.estruturart.persistency;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Date;

import com.mysql.jdbc.Statement;

import br.com.estruturart.model.TbFornecedor;
import br.com.estruturart.model.TbMaterial;
import br.com.estruturart.model.TbModelo;
import br.com.estruturart.model.TbStatusMaterial;
import br.com.estruturart.model.TbStatusModelo;
import br.com.estruturart.model.TbUnidadeMedida;
import br.com.estruturart.utility.Paginator;
import br.com.estruturart.utility.ParamRequestManager;

public class Modelo extends AbstractPersistency
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
            sqlNome = " AND m.nome LIKE \"%" + filter.getParam("nome") + "%\" ";
        }

        String subQueryPreco = String.format("(" +
            " SELECT" +
            "     SUM(IF(mat.materia_prima = 1, mat.preco, 0)) + (" +
            "         IF (" +
            "             m.preco_pintura IS NULL || m.preco_pintura = ''," +
            "             0," +
            "             m.preco_pintura" +
            "         )" +
            "     )" +
            "     +" +
            "     (" +
            "             (SUM(IF(mat.materia_prima = 1, mat.preco, 0))" +
            "             * m.porcentagem_acrescimo) / 100" +
            "     )" +
            " FROM modelo_material mm INNER JOIN material mat ON mm.material_id = mat.id WHERE m.id = mm.modelo_id) as preco_total"
        );
        String columns = String.format("m.*, STATUS_MODELO.NOME AS status_modelo, %s", subQueryPreco);
        String limit = String.format("LIMIT %d, %d", pageAux, offset);

        String sql = String.format("SELECT {columns} FROM MODELO m"
                + " INNER JOIN STATUS_MODELO ON m.status_modelo_id = STATUS_MODELO.id WHERE 1 %s "
                + " order by m.id DESC {limit}",
                sqlNome);
        System.out.println("SQL: " + sql.replace("{columns}", columns).replace("{limit}", limit));
        PreparedStatement ps = conn.prepareStatement(sql.replace("{columns}", columns).replace("{limit}", limit));
        System.out.println(sql);
        System.out.println(sqlNome);

        ResultSet rs = ps.executeQuery();

        List<TbModelo> modelos = new ArrayList<TbModelo>();
        while (rs.next()) {
            TbModelo modelo = new TbModelo();
            modelo.setId(rs.getInt("id"));
            modelo.setNome(rs.getString("nome"));
            modelo.setDescricao(rs.getString("descricao"));
            modelo.setLarguraPadrao(rs.getFloat("largura_padrao"));
            modelo.setAlturaPadrao(rs.getFloat("altura_padrao"));
            modelo.setImagem(rs.getString("imagem"));
            modelo.setPrecoPintura(rs.getFloat("preco_pintura"));
            modelo.setPorcentagemAcrescimo(rs.getFloat("porcentagem_acrescimo"));
            modelo.setQtdDiasProducao(rs.getInt("qtd_dias_producao"));
            modelo.setStatusModeloId(rs.getInt("status_modelo_id"));
            modelo.setDataInclusao(rs.getDate("data_inclusao"));
            modelo.setPrecoTotalSubQuery(rs.getFloat("preco_total"));

            TbStatusModelo statusModelo = new TbStatusModelo();
            statusModelo.setId(rs.getInt("status_modelo_id"));
            statusModelo.setNome(rs.getString("status_modelo"));

            modelo.setStatusModelo(statusModelo);
            modelos.add(modelo);
        }

        conn.close();
        return new Paginator(modelos, this.findTotalRows(sql), offset, page);
    }

    public TbModelo getModeloById(int fkModelo) throws Exception
    {
        Connection conn = ConnectionManager.getConnection();
        PreparedStatement ps = conn
                .prepareStatement("SELECT MODELO.*, STATUS_MODELO.NOME AS status_modelo FROM MODELO"
                        + " INNER JOIN STATUS_MODELO ON MODELO.status_modelo_id = STATUS_MODELO.id"
                        + " WHERE MODELO.id = ?");

        ps.setInt(1, fkModelo);

        ResultSet rs = ps.executeQuery();
        TbModelo modelo = new TbModelo();
        if (rs.next()) {
            modelo.setId(rs.getInt("id"));
            modelo.setNome(rs.getString("nome"));
            modelo.setDescricao(rs.getString("descricao"));
            modelo.setLarguraPadrao(rs.getFloat("largura_padrao"));
            modelo.setAlturaPadrao(rs.getFloat("altura_padrao"));
            modelo.setImagem(rs.getString("imagem"));
            modelo.setPrecoPintura(rs.getFloat("preco_pintura"));
            modelo.setPorcentagemAcrescimo(rs.getFloat("porcentagem_acrescimo"));
            modelo.setQtdDiasProducao(rs.getInt("qtd_dias_producao"));
            modelo.setStatusModeloId(rs.getInt("status_modelo_id"));
            modelo.setDataInclusao(rs.getDate("data_inclusao"));

            TbStatusModelo statusModelo = new TbStatusModelo();
            statusModelo.setId(rs.getInt("status_modelo_id"));
            statusModelo.setNome(rs.getString("status_modelo"));

            modelo.setStatusModelo(statusModelo);
            modelo.setMateriais(this.findMaterialByModelo(rs.getInt("id")));
            modelo.getPrecoTotalQuantidadeString();
        }

        conn.close();
        return modelo;
    }

    public int insert(TbModelo modelo) throws SQLException
    {
        Connection conn = ConnectionManager.getConnection();
        String sql = String.format(
                "INSERT INTO MODELO (nome, descricao, largura_padrao, altura_padrao, imagem, preco_pintura, porcentagem_acrescimo, qtd_dias_producao, status_modelo_id)"
                        + " VALUES (\"%s\", \"%s\", \"%s\", \"%s\", \"%s\", \"%s\", \"%s\", %d, %d)",
                modelo.getNome(), modelo.getDescricao(), modelo.getLarguraPadrao(), modelo.getAlturaPadrao(), modelo.getImagem(), modelo.getPrecoPintura(), modelo.getPorcentagemAcrescimo(), modelo.getQtdDiasProducao(), modelo.getStatusModeloId()
        );

        PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
        ps.execute();
        System.out.println(sql);

        ResultSet rs = ps.getGeneratedKeys();
        modelo.setId(rs.next() ? rs.getInt(1) : 0);

        conn.close();

        inserirMateriaisModelo(modelo, true);
        return modelo.getId();
    }

    public int update(TbModelo modelo) throws SQLException {
        Connection conn = ConnectionManager.getConnection();
        String sql = String.format(
            "UPDATE MODELO SET nome = \"%s\", descricao = \"%s\", largura_padrao = \"%s\", altura_padrao = \"%s\", imagem = \"%s\", preco_pintura = \"%s\", porcentagem_acrescimo = \"%s\", qtd_dias_producao = %d, status_modelo_id = %d"
            + " WHERE id = %d",
            modelo.getNome(), modelo.getDescricao(), modelo.getLarguraPadrao(), modelo.getAlturaPadrao(),
            modelo.getImagem(), modelo.getPrecoPintura(), modelo.getPorcentagemAcrescimo(),
            modelo.getQtdDiasProducao(), modelo.getStatusModeloId(), modelo.getId()
        );

        PreparedStatement ps = conn.prepareStatement(sql);
        int rows = ps.executeUpdate();

        conn.close();

        inserirMateriaisModelo(modelo, false);
        return rows;
    }

    public int updateStatus(TbModelo modelo) throws SQLException {
        Connection conn = ConnectionManager.getConnection();
        String sql = String.format(
            "UPDATE MODELO SET status_modelo_id = %d WHERE id = %d",
            modelo.getStatusModeloId(), modelo.getId()
        );

        PreparedStatement ps = conn.prepareStatement(sql);
        int rows = ps.executeUpdate();

        conn.close();

        inserirMateriaisModelo(modelo, false);
        return rows;
    }

    public boolean findModeloByNome(String nome, int fkModeloEdicao) throws SQLException
    {
        Connection conn = ConnectionManager.getConnection();
        PreparedStatement ps = conn.prepareStatement("SELECT * FROM MODELO WHERE LOWER(nome) = LOWER(?)");
        ps.setString(1, nome);
        System.out.println("EDICAO" + fkModeloEdicao);
        if (fkModeloEdicao != 0) {
            ps = conn.prepareStatement("SELECT * FROM MODELO WHERE LOWER(nome) = LOWER(?) AND id <> ?");

            ps.setString(1, nome);
            ps.setInt(2, fkModeloEdicao);
        }

        ResultSet rs = ps.executeQuery();
        if (rs.next()) {
            return true;
        }

        conn.close();
        return false;
    }

    public List<TbMaterial> findMaterialByModelo(int fkModelo) throws SQLException
    {
        Connection conn = ConnectionManager.getConnection();

        List<TbMaterial> materiaisList = new ArrayList<TbMaterial>();

        String sql = String
            .format("SELECT m.*, STATUS_MATERIAL.descricao as descricao_status, UNIDADE_MEDIDA.nome as nome_unidade_medida, FORNECEDOR.nome as nome_fornecedor, FORNECEDOR.status as status_fornecedor"
            + " FROM MODELO_MATERIAL mm INNER JOIN MATERIAL m ON m.id = mm.material_id"
            + " INNER JOIN STATUS_MATERIAL ON m.status_material_id = STATUS_MATERIAL.id "
            + " LEFT JOIN UNIDADE_MEDIDA ON m.unidade_medida_id = UNIDADE_MEDIDA.id "
            + " INNER JOIN FORNECEDOR ON m.fornecedor_id = FORNECEDOR.id WHERE mm.modelo_id = %d",
            fkModelo
        );

        PreparedStatement ps = conn.prepareStatement(sql);
        ResultSet rs = ps.executeQuery();

        while (rs.next()) {
            TbMaterial material = new TbMaterial();
            material.setId(rs.getInt("id"));
            material.setDescricao(rs.getString("descricao"));
            material.setMateriaPrima(rs.getInt("materia_prima"));
            material.setDataInclusao(new Date(rs.getDate("data_inclusao").getTime()));
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

    public void inserirMateriaisModelo(TbModelo modelo, boolean isInsert) throws SQLException
    {
        Connection conn = ConnectionManager.getConnection();
        if (!isInsert) {
            PreparedStatement ps = conn.prepareStatement("DELETE FROM MODELO_MATERIAL WHERE modelo_id = ?");
            ps.setInt(1, modelo.getId());
            ps.executeUpdate();
            System.out.println("DELETANDO..." + modelo.getId());

        }

        for (TbMaterial material : modelo.getMateriais()) {
            String sql = String.format("INSERT INTO MODELO_MATERIAL (material_id, modelo_id) VALUES (%d, %d)",
                    material.getId(), modelo.getId());

            PreparedStatement ps2 = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            ps2.execute();
        }

        conn.close();
    }

    public List<TbModelo> findModeloByNomeList(String nome, String id) throws SQLException
    {
        Connection conn = ConnectionManager.getConnection();
        String sql =
            "SELECT MODELO.*, STATUS_MODELO.NOME AS status_modelo FROM MODELO"
            + " INNER JOIN STATUS_MODELO ON MODELO.status_modelo_id = STATUS_MODELO.id"
            + " WHERE MODELO.status_modelo_id = 1";

        if (!id.equals("0")) {
            sql += " AND MODELO.id = \"" + id + "\" ";
        } else {
            sql += " AND MODELO.nome LIKE \"%" + nome + "%\" ";
        }

        sql += " ORDER BY MODELO.nome ASC LIMIT 30";
        PreparedStatement ps = conn.prepareStatement(sql);

        ResultSet rs = ps.executeQuery();
        List<TbModelo> modelosList = new ArrayList<TbModelo>();
        while (rs.next()) {
            TbModelo modelo = new TbModelo();
            modelo.setId(rs.getInt("id"));
            modelo.setNome(rs.getString("nome"));
            modelo.setDescricao(rs.getString("descricao"));
            modelo.setLarguraPadrao(rs.getFloat("largura_padrao"));
            modelo.setAlturaPadrao(rs.getFloat("altura_padrao"));
            modelo.setImagem(rs.getString("imagem"));
            modelo.setPrecoPintura(rs.getFloat("preco_pintura"));
            modelo.setPorcentagemAcrescimo(rs.getFloat("porcentagem_acrescimo"));
            modelo.setQtdDiasProducao(rs.getInt("qtd_dias_producao"));
            modelo.setStatusModeloId(rs.getInt("status_modelo_id"));
            modelo.setDataInclusao(rs.getDate("data_inclusao"));

            TbStatusModelo statusModelo = new TbStatusModelo();
            statusModelo.setId(rs.getInt("status_modelo_id"));
            statusModelo.setNome(rs.getString("status_modelo"));

            modelo.setStatusModelo(statusModelo);
            modelo.setMateriais(this.findMaterialByModelo(rs.getInt("id")));
            modelo.getPrecoTotalQuantidadeString();
            modelosList.add(modelo);
        }

        return modelosList;
    }
}
