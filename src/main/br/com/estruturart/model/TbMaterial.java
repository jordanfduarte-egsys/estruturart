package br.com.estruturart.model;

import java.sql.Date;
import java.sql.SQLException;
import java.text.DecimalFormat;

import br.com.estruturart.persistency.Material;
import br.com.estruturart.utility.RouteParam;

public class TbMaterial extends AbstractModel
{
    private Integer id = 0;
    private String descricao = "";
    private Date dataInclusao;
    private int materiaPrima;
    private float preco = 0;
    private String precoBr = "0,00";
    private int quantidade = 1;
    private String descricaoFiltro;

    private int statusMaterialId;
    private int unidadeMedidaId;
    private int fornecedorId;

    private TbStatusMaterial statusMaterial;
    private TbUnidadeMedida unidadeMedida;
    private TbFornecedor fornecedor;

    public TbMaterial()
    {
        statusMaterial = new TbStatusMaterial();
        unidadeMedida = new TbUnidadeMedida();
        fornecedor = new TbFornecedor();
    }

    public Integer getId()
    {
        return id;
    }

    public void setId(Integer id)
    {
        this.id = id;
    }

    public String getDescricao()
    {
        return descricao;
    }

    public void setDescricao(String descricao)
    {
        this.descricao = descricao;
    }

    public Date getDataInclusao()
    {
        return dataInclusao;
    }

    public void setDataInclusao(Date dataInclusao)
    {
        this.dataInclusao = dataInclusao;
    }

    public int getMateriaPrima()
    {
        return materiaPrima;
    }

    public void setMateriaPrima(int materiaPrima)
    {
        this.materiaPrima = materiaPrima;
    }

    public int getStatusMaterialId()
    {
        return statusMaterialId;
    }

    public void setStatusMaterialId(int statusMaterialId)
    {
        this.statusMaterialId = statusMaterialId;
    }

    public int getUnidadeMedidaId()
    {
        return unidadeMedidaId;
    }

    public void setUnidadeMedidaId(int unidadeMedidaId)
    {
        this.unidadeMedidaId = unidadeMedidaId;
    }

    public int getFornecedorId()
    {
        return fornecedorId;
    }

    public void setFornecedorId(int fornecedorId)
    {
        this.fornecedorId = fornecedorId;
    }

    public String getDateFormat(String format)
    {
        return this.getSimpleDateFormat(format).format(this.getDataInclusao());
    }

    @Override
    public boolean isValid() throws SQLException
    {
        boolean isValid = true;

        if (this.getDescricao().equals("")) {
            this.getValidation().add(new RouteParam("descricao", "Informe a descrição!"));
            isValid = false;
        }

        if (this.getPreco() <= 0) {
            this.getValidation().add(new RouteParam("preco", "Informe o preço!"));
            isValid = false;
        }

        if (this.getQuantidade() <= 0) {
            this.getValidation().add(new RouteParam("quantidade", "Informe a quantidade!"));
            isValid = false;
        }

        if (this.getStatusMaterialId() == 0) {
            this.getValidation().add(new RouteParam("status_material_id", "Informe o status do material!"));
            isValid = false;
        }

        if (this.getUnidadeMedidaId() == 0) {
            this.getValidation().add(new RouteParam("unidade_medida_id", "Informe a unidadade de medida!"));
            isValid = false;
        }

        if (this.getFornecedorId() == 0) {
            this.getValidation().add(new RouteParam("fornecedor_id", "Informe o fornecedor!"));
            isValid = false;
        }
        Material material = new Material();
        if (material.findMaterialByNome(this.getDescricao(), this.getId(), this.getFornecedorId())) {
            this.getValidation().add(new RouteParam("descricao", "Material já cadastrado para esse fornecedor!"));
            isValid = false;
        }

        return isValid;
    }

    public TbStatusMaterial getStatusMaterial()
    {
        return statusMaterial;
    }

    public void setStatusMaterial(TbStatusMaterial statusMaterial)
    {
        this.statusMaterial = statusMaterial;
    }

    public TbUnidadeMedida getUnidadeMedida()
    {
        return unidadeMedida;
    }

    public void setUnidadeMedida(TbUnidadeMedida unidadeMedida)
    {
        this.unidadeMedida = unidadeMedida;
    }

    public TbFornecedor getFornecedor()
    {
        return fornecedor;
    }

    public void setFornecedor(TbFornecedor fornecedor)
    {
        this.fornecedor = fornecedor;
    }

    public float getPreco()
    {
        return preco;
    }

    public void setPreco(float preco)
    {
        this.preco = preco;
        this.getPrecoBR();
    }

    public int getQuantidade()
    {
        return quantidade;
    }

    public void setQuantidade(int quantidade)
    {
        this.quantidade = quantidade;
    }

    public String getPrecoBR() {
        DecimalFormat twoPlaces = new DecimalFormat("0.00");
        return this.precoBr = twoPlaces.format((this.preco));
    }

    /**
     * @return the descricaoFiltro
     */
    public String getDescricaoFiltro()
    {
        return descricaoFiltro;
    }

    /**
     * @param descricaoFiltro the descricaoFiltro to set
     */
    public void setDescricaoFiltro(String descricaoFiltro)
    {
        this.descricaoFiltro = descricaoFiltro;
    }
}
