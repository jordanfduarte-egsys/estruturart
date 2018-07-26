package br.com.estruturart.model;

import java.util.Date;

public class TbPedido extends AbstractModel
{
    private int id = 0;
    private String caminhoArquivoNotaFiscal;
    private Date dataInclusao;
    private Date dataPrevisaoInstalacao;
    private float valorTotal;
    private float valorMaoObra;
    private int pedidoPago = 0;
    private float descontoGeral = 0;
    private String observacao;
    private int usuarioId;
    private int statusPedidoId;

    public static final int PEDIDO_PENDENTE = 1;
    public static final int ORCAMENTO_PENDENTE = 2;
    public static final int PRODUCAO = 3;
    public static final int PEDIDO_PAGO = 4;
    public static final int INSTALACAO = 5;
    public static final int FINALIZADO = 6;
    public static final int CANCELADO = 7;

    public int getId()
    {
        return this.id;
    }

    public void setId(int id)
    {
        this.id = id;
    }

    public String getCaminhoArquivoNotaFiscal()
    {
        return this.caminhoArquivoNotaFiscal;
    }

    public void setCaminhoArquivoNotaFiscal(String caminhoArquivoNotaFiscal)
    {
        this.caminhoArquivoNotaFiscal = caminhoArquivoNotaFiscal;
    }

    public Date getDataInclusao()
    {
        return this.dataInclusao;
    }

    public void setDataInclusao(Date dataInclusao)
    {
        this.dataInclusao = dataInclusao;
    }

    public Date getDataPrevisaoInstalacao()
    {
        return this.dataPrevisaoInstalacao;
    }

    public void setDataPrevisaoInstalacao(Date dataPrevisaoInstalacao)
    {
        this.dataPrevisaoInstalacao = dataPrevisaoInstalacao;
    }

    public float getValorTotal()
    {
        return this.valorTotal;
    }

    public void setValorTotal(float valorTotal)
    {
        this.valorTotal = valorTotal;
    }

    public float getValorMaoObra()
    {
        return this.valorMaoObra;
    }

    public void setValorMaoObra(float valorMaoObra)
    {
        this.valorMaoObra = valorMaoObra;
    }

    public int getPedidoPago()
    {
        return this.pedidoPago;
    }

    public void setPedidoPago(int pedidoPago)
    {
        this.pedidoPago = pedidoPago;
    }

    public String getObservacao()
    {
        return this.observacao;
    }

    public void setObservacao(String observacao)
    {
        this.observacao = observacao;
    }

    public int getUsuarioId()
    {
        return this.usuarioId;
    }

    public void setUsuarioId(int usuarioId)
    {
        this.usuarioId = usuarioId;
    }

    public int getStatusPedidoId()
    {
        return this.statusPedidoId;
    }

    public void setStatusPedidoId(int statusPedidoId)
    {
        this.statusPedidoId = statusPedidoId;
    }

    public float getDescontoGeral()
    {
        return this.descontoGeral;
    }

    public void setDescontoGeral(float descontoGeral)
    {
        this.descontoGeral = descontoGeral;
    }

    public boolean isValid() { return true; }
}
