package br.com.estruturart.model;

import java.util.Date;
import java.util.ArrayList;
import java.util.List;
import br.com.estruturart.model.TbLancamento;
import br.com.estruturart.model.TbModelo;
import br.com.estruturart.model.TbStatusItem;
import br.com.estruturart.utility.StringUtilsPad;

public class TbPedidoItem extends AbstractModel
{
    private int id = 0;
    private float largura;
    private float altura;
    private Date dataInclusao;
    private int quantidade;
    private int statusItemId;
    private int pedidoId;
    private int modeloId;
    private float desconto;
    private List<TbLancamento> lancamentos = new ArrayList<TbLancamento>();
    private TbModelo modelo = new TbModelo();
    private TbStatusItem statusItem = new TbStatusItem();

    public static final int ATIVO = 1;
    public static final int CANCELADO = 2;

    public TbPedidoItem()
    {
        lancamentos = new ArrayList<TbLancamento>();
        modelo = new TbModelo();
        statusItem = new TbStatusItem();
    }

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

    public float getDesconto()
    {
        return this.desconto;
    }

    public void setDesconto(float desconto)
    {
        this.desconto = desconto;
    }

    public TbModelo getModelo()
    {
        return this.modelo;
    }

    public void setModelo(TbModelo modelo)
    {
        this.modelo = modelo;
    }

    public TbStatusItem getStatusItem()
    {
        return this.statusItem;
    }

    public void setStatusItem(TbStatusItem statusItem)
    {
        this.statusItem = statusItem;
    }

    public List<TbLancamento> getLancamentos()
    {
        return this.lancamentos;
    }

    public void setLancamentos(List<TbLancamento> lancamentos)
    {
        this.lancamentos = lancamentos;
    }

    public String getIdString()
    {
        return StringUtilsPad.padLeft(String.valueOf(getId()), 5, "0");
    }

    public String getPrecoItemMaisPintura()
    {
        float total = 0;
        if (getStatusItemId() == 1) {
            boolean isCalcQtd = false;
            for (TbLancamento lancamento: getLancamentos()) {
                if (!isCalcQtd) {
                    total += (lancamento.getPreco() * this.getQuantidade()) + lancamento.getPrecoPintura();
                    isCalcQtd = true;
                } else {
                    total += (lancamento.getPreco()) + lancamento.getPrecoPintura();
                }
            }
        }
        return formatMoney(total);
    }

    public float getPrecoItemMaisPinturaFloat()
    {
        float total = 0;
        if (getStatusItemId() == 1) {
            boolean isCalcQtd = false;
            for (TbLancamento lancamento: getLancamentos()) {
                if (!isCalcQtd) {
                    total += (lancamento.getPreco() * this.getQuantidade()) + lancamento.getPrecoPintura();
                    isCalcQtd = true;
                } else {
                    total += (lancamento.getPreco()) + lancamento.getPrecoPintura();
                }
            }
        }
        return total;
    }

    public String getPrecoItem()
    {
        float total = 0;
        for (TbLancamento lancamento: getLancamentos()) {
            total += (lancamento.getPreco() * this.getQuantidade());
        }
        return formatMoney(total);
    }

    public String getSomaPrecoPintura()
    {
        float total = 0;
        for (TbLancamento lancamento : getLancamentos()) {
            total += lancamento.getPrecoPintura();
        }
        return formatMoney(total);
    }

    public boolean getIsPintura()
    {
        for (TbLancamento lancamento: getLancamentos()) {
            if (lancamento.getPrecoPintura() > 0) {
                return true;
            }
        }

        return false;
    }

    public boolean isValid() { return true; }
}
