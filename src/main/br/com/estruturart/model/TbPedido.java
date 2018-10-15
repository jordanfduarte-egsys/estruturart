package br.com.estruturart.model;

import java.util.Date;
import java.util.Calendar;
import java.text.SimpleDateFormat;
import br.com.estruturart.utility.StringUtilsPad;
import br.com.estruturart.model.TbPedidoItem;
import br.com.estruturart.model.TbStatusPedido;
import br.com.estruturart.utility.RouteParam;
import br.com.estruturart.model.TbLogPedido;
import java.util.ArrayList;
import java.util.List;

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
    private TbEndereco endereco;
    private TbUsuario usuario;
    private List<TbPedidoItem> itens;
    private List<TbLogPedido> logPedido;
    private TbStatusPedido statusPedido;

    private String url;
    private String title;
    private String start;
    private String color;

    public static final int PEDIDO_PENDENTE = 1;
    public static final int ORCAMENTO_PENDENTE = 2;
    public static final int PRODUCAO = 3;
    public static final int PEDIDO_PAGO = 4;
    public static final int INSTALACAO = 5;
    public static final int FINALIZADO = 6;
    public static final int CANCELADO = 7;

    public TbPedido()
    {
        itens = new ArrayList<TbPedidoItem>();
        logPedido = new ArrayList<TbLogPedido>();
        statusPedido = new TbStatusPedido();
    }

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

    public String getDescontoGeralString()
    {
        return formatMoney(this.descontoGeral);
    }

    public void setDescontoGeral(float descontoGeral)
    {
        this.descontoGeral = descontoGeral;
    }

    public TbEndereco getEndereco()
    {
        return this.endereco;
    }

    public void setEndereco(TbEndereco endereco)
    {
        this.endereco = endereco;
    }

    public void processarInfoListagem()
    {
        SimpleDateFormat df = new SimpleDateFormat("yyy-MM-dd");
        SimpleDateFormat df1 = new SimpleDateFormat("yyy-MM-dd'T'HH:mm:ss");

        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MILLISECOND, 0);
        Date dateWithoutTime = cal.getTime();

        this.url = String.format("{0}/visualizar/id/%s", getId());

        this.start = df1.format(getDataPrevisaoInstalacao().getTime());
        this.color = "#007bff";

        String status = "Pedido Pendente";
        if (getStatusPedidoId() != 7 && getStatusPedidoId() != 6) {
            if (getDataPrevisaoInstalacao().compareTo(dateWithoutTime) < 0) {
                this.color = "#dc3545";
                status = "Pedido Atrasado. Ajuste a data de previsão de instalação";
            } else if (df.format(getDataPrevisaoInstalacao().getTime()).equals(df.format(new Date().getTime()))) {
                this.color = "#ffc107";
                status = "Instalação e montagem marcado para hoje.";
            }
        }

        if (getStatusPedidoId() == 7) {
            this.color = "#CCC";
            status = "Cancelado";
        }

        this.title = String.format(
            "<span data-toggle=\"tooltip\" title=\"%s\" class=\"fc-title\">#%s - #%s</span>",
            status,
            StringUtilsPad.padLeft(String.valueOf(getId()), 5, "0"),
            getUsuario().getNome()
        );
    }

    public String getIdString()
    {
        return StringUtilsPad.padLeft(String.valueOf(getId()), 5, "0");
    }

    public TbUsuario getUsuario()
    {
        return this.usuario;
    }

    public void setUsuario(TbUsuario usuario)
    {
        this.usuario = usuario;
    }

    public List<TbPedidoItem> getItens()
    {
        return this.itens;
    }

    public void setItens(List<TbPedidoItem> itens)
    {
        this.itens = itens;
    }

    public String getNotaFiscalLabel()
    {
        if (getCaminhoArquivoNotaFiscal().length() == 0) {
            return "Não enviada";
        }

        return "<a href=\"javascript:void(0);\">Visualizar nota</a>";
    }

    public String getPrecoGeralString()
    {
        return formatMoney(getValorTotal());
    }

    public String getPrecoGeralMaisMaoObraString()
    {
        return formatMoney(getValorTotal() + getValorMaoObra());
    }

    public String getValorMaoObraString()
    {
        return formatMoney(getValorMaoObra());
    }

    public String getDataPrevisaoInstalacaoString()
    {
        if (getDataPrevisaoInstalacao() instanceof Date) {
            return this.getSimpleDateFormat("dd/MM/yyyy").format(getDataPrevisaoInstalacao());
        }

        return "";
    }

    public String getDataInclusaoString()
    {
        if (getDataInclusao() instanceof Date) {
            return this.getSimpleDateFormat("dd/MM/yyyy").format(getDataInclusao());
        }

        return "";
    }

    public TbStatusPedido getStatusPedido()
    {
        return this.statusPedido;
    }

    public void setStatusPedido(TbStatusPedido statusPedido)
    {
        this.statusPedido = statusPedido;
    }

    public String getCorPrevisaoInstalacao()
    {
        SimpleDateFormat df = new SimpleDateFormat("yyy-MM-dd");
        String color = "#007bff";
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MILLISECOND, 0);
        Date dateWithoutTime = cal.getTime();

        if (getDataPrevisaoInstalacao().compareTo(dateWithoutTime) < 0) {
            color = "#dc3545";
        } else if (df.format(getDataPrevisaoInstalacao().getTime()).equals(df.format(new Date().getTime()))) {
            color = "#ffc107";
        }

        if (getStatusPedidoId() == 7) {
            color = "#CCC";
        }

        return color;
    }

    public boolean isValid()
    {
        boolean isValid = true;
        if (getDataPrevisaoInstalacao() == null) {
            this.getValidation().add(new RouteParam("prev_entrega", "Informe a data de previsão de entrega válida!"));
            isValid = false;
        }

        return isValid;
    }

    public int getItensNaoCancelado()
    {
        int total = 0;
        for (TbPedidoItem item : getItens()) {
            total += item.getStatusItemId() == 1 ? 1 : 0;
        }
        return total;
    }

    public int getTotalDiasProducao()
    {
        int total = 0;
        for (TbPedidoItem item :getItens()) {
            total += item.getModelo().getQtdDiasProducao();
        }

        return total;
    }

    public String getPedidoPagoString()
    {
        if (getPedidoPago() == 1) {
            return "Sim";
        }

        return "Não";
    }

    public void setLogPedido(List<TbLogPedido> logs)
    {
        this.logPedido = logs;
    }

    public List<TbLogPedido> getLogPedido()
    {
        return this.logPedido;
    }
}
