package br.com.estruturart.model;

import java.util.Date;

public class TbPedidoItem
{
    private int id = 0;
    private float largura;
    private float altura;
    private Date dataInclusao;
    private int quantidade;
    private int statusItemId;
    private int pedidoId;
    private int modeloId;

    public static final int ATIVO = 1;
    public static final int CANCELADO = 2;

    public int getId()
    {
        return this.id;
    }

    public void setId(int id)
    {
        this.id = id;
    }

    public float getLargura()
    {
        return this.largura;
    }

    public void setLargura(float largura)
    {
        this.largura = largura;
    }

    public float getAltura()
    {
        return this.altura;
    }

    public void setAltura(float altura)
    {
        this.altura = altura;
    }

    public Date getDataInclusao()
    {
        return this.dataInclusao;
    }

    public void setDataInclusao(Date dataInclusao)
    {
        this.dataInclusao = dataInclusao;
    }

    public int getQuantidade()
    {
        return this.quantidade;
    }

    public void setQuantidade(int quantidade)
    {
        this.quantidade = quantidade;
    }

    public int getStatusItemId()
    {
        return this.statusItemId;
    }

    public void setStatusItemId(int statusItemId)
    {
        this.statusItemId = statusItemId;
    }

    public int getPedidoId()
    {
        return this.pedidoId;
    }

    public void setPedidoId(int pedidoId)
    {
        this.pedidoId = pedidoId;
    }

    public int getModeloId()
    {
        return this.modeloId;
    }

    public void setModeloId(int modeloId)
    {
        this.modeloId = modeloId;
    }
}
