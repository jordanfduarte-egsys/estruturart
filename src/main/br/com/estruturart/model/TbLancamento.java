package br.com.estruturart.model;

import java.util.Date;
import br.com.estruturart.utility.StringUtilsPad;
import java.text.SimpleDateFormat;

public class TbLancamento extends AbstractModel
{
    private int id = 0;
    private String idString = "";
    private float totalMaisPintura = 0;
    private float preco;
    private float precoPintura;
    private Date dataInclusao;
    private String dataInclusaoString;
    private String descricao;
    private float desconto;
    private int usuarioId;
    private int pedidoItensId;

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

    public boolean isValid() { return true; }
}
