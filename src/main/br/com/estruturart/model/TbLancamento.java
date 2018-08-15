package br.com.estruturart.model;

import java.util.Date;
import br.com.estruturart.utility.StringUtilsPad;
import java.text.SimpleDateFormat;
import br.com.estruturart.model.TbMaterial;
import br.com.estruturart.model.TbPedidoItem;
import br.com.estruturart.utility.RouteParam;

public class TbLancamento extends AbstractModel
{
    private int id = 0;
    private String idString = "";
    private float totalMaisPintura = 0;
    private float preco = 0;
    private float precoPintura = 0;
    private Date dataInclusao;
    private String dataInclusaoString;
    private String descricao = "";
    private float desconto = 0;
    private int usuarioId;
    private int pedidoItensId = 0;
    private int materialId = 0;
    private TbMaterial material = new TbMaterial();
    private TbPedidoItem pedidoItem = new TbPedidoItem();

    public int getId()
    {
        return this.id;
    }

    public void setId(int id)
    {
        this.id = id;
    }

    public float getPreco()
    {
        return this.preco;
    }

    public String getPrecoString()
    {
        return formatMoney(this.preco);
    }

    public void setPreco(float preco)
    {
        this.preco = preco;
        this.totalMaisPintura += preco;
    }

    public float getPrecoPintura()
    {
        return this.precoPintura;
    }

    public void setPrecoPintura(float precoPintura)
    {
        this.precoPintura = precoPintura;
        this.totalMaisPintura += precoPintura;
    }

    public Date getDataInclusao()
    {
        return this.dataInclusao;
    }

    public void setDataInclusao(Date dataInclusao)
    {
        this.dataInclusao = dataInclusao;
        SimpleDateFormat df = new SimpleDateFormat("yyy-MM-dd HH:mm:ss");
        this.dataInclusaoString = df.format(getDataInclusao().getTime());
    }

    public String getDescricao()
    {
        return this.descricao;
    }

    public void setDescricao(String descricao)
    {
        this.descricao = descricao;
    }

    public float getDesconto()
    {
        return this.desconto;
    }

    public void setDesconto(float desconto)
    {
        this.desconto = desconto;
    }

    public int getUsuarioId()
    {
        return this.usuarioId;
    }

    public void setUsuarioId(int usuarioId)
    {
        this.usuarioId = usuarioId;
    }

    public int getPedidoItensId()
    {
        return this.pedidoItensId;
    }

    public void setPedidoItensId(int pedidoItensId)
    {
        this.pedidoItensId = pedidoItensId;
        this.idString = StringUtilsPad.padLeft(String.valueOf(pedidoItensId), 5, "0");
    }

    public int getMaterialId()
    {
        return this.materialId;
    }

    public void setMaterialId(int materialId)
    {
        this.materialId = materialId;
    }

    public TbMaterial getMaterial()
    {
        return this.material;
    }

    public void setMaterial(TbMaterial material)
    {
        this.material = material;
    }

    public TbPedidoItem getPedidoItem()
    {
        return this.pedidoItem;
    }

    public void setPedidoItem(TbPedidoItem pedidoItem)
    {
        this.pedidoItem = pedidoItem;
    }

    public boolean isValid()
    {
        boolean isValid = true;
        if (getPedidoItensId() == 0) {
            if (getMaterialId() == 0) {
                this.getValidation().add(new RouteParam("material_id", "Informe o material!"));
                isValid = false;
            }
        }

        if (getPreco() <= 0.0) {
            this.getValidation().add(new RouteParam("preco", "Informe o valor do lançamento!"));
            isValid = false;
        }

        return isValid;
    }
}
