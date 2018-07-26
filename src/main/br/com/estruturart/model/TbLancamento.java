package br.com.estruturart.model;

import java.util.Date;

public class TbLancamento extends AbstractModel
{
    private int id = 0;
    private float preco;
    private float precoPintura;
    private Date dataInclusao;
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
    }

    public float getPrecoPintura()
    {
        return this.precoPintura;
    }

    public void setPrecoPintura(float precoPintura)
    {
        this.precoPintura = precoPintura;
    }

    public Date getDataInclusao()
    {
        return this.dataInclusao;
    }

    public void setDataInclusao(Date dataInclusao)
    {
        this.dataInclusao = dataInclusao;
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
    }

    public boolean isValid() { return true; }
}
