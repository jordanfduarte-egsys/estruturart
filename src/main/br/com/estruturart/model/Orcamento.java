package br.com.estruturart.model;

import br.com.estruturart.model.TbUsuario;
import br.com.estruturart.model.TbModelo;
import java.sql.SQLException;
import br.com.estruturart.model.TbEndereco;
import br.com.estruturart.model.TbPedido;
import br.com.estruturart.utility.RouteParam;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Date;
import java.util.Calendar;
import br.com.estruturart.utility.Feriados;
import java.util.concurrent.TimeUnit;
import java.text.ParseException;

public class Orcamento extends AbstractModel
{
    private TbUsuario usuario;
    private TbEndereco endereco;
    private TbPedido pedido;
    private List<TbModelo> modelos;
    private float desconto = 0;
    private Date prevEntrega;
    private float valorMaoObra = 0;
    private String observacao;

    private boolean isValidEtapa1;
    private boolean isValidEtapa2;

    public static final int ETAPA1 = 1;
    public static final int ETAPA2 = 2;
    public static final int ETAPA3 = 3;

    public Orcamento()
    {
        usuario = new TbUsuario();
        endereco = new TbEndereco();
        modelos = new ArrayList<TbModelo>();
    }

    public boolean getIsValidEtapa2()
    {
        return this.isValidEtapa2;
    }

    public void setIsValidEtapa2(boolean isValidEtapa2)
    {
        this.isValidEtapa2 = isValidEtapa2;
    }

    public boolean isValidEtapa2()
    {
        return this.isValidEtapa2;
    }

    public boolean getIsValidEtapa1()
    {
        return this.isValidEtapa1;
    }

    public void setIsValidEtapa1(boolean isValidEtapa1)
    {
        this.isValidEtapa1 = isValidEtapa1;
    }

    public boolean isValidEtapa1()
    {
        return this.isValidEtapa1;
    }

    public TbUsuario getUsuario()
    {
        return this.usuario;
    }

    public void setUsuario(TbUsuario usuario)
    {
        this.usuario = usuario;
    }

    public TbEndereco getEndereco()
    {
        return this.endereco;
    }

    public void setEndereco(TbEndereco endereco)
    {
        this.endereco = endereco;
    }

    public TbPedido getPedido()
    {
        return this.pedido;
    }

    public void setPedido(TbPedido pedido)
    {
        this.pedido = pedido;
    }

    public boolean isValid() {return true;}

    public boolean isValid(int etapa) throws SQLException
    {
        boolean isEtapaValid = false;

        switch (etapa) {
            case ETAPA1:
                this.getUsuario().getValidation().clear();
                this.getEndereco().getValidation().clear();
                boolean isValidUsuario = getUsuario().isValid();
                boolean isValidEndereco = getEndereco().isValid();
                isEtapaValid = isValidUsuario && isValidEndereco;
                setIsValidEtapa1(isEtapaValid);
            break;
            case ETAPA2:
                this.getValidation().clear();
                boolean isValidModelos = getModelos().size() > 0;
                isEtapaValid = isValidModelos;
                if (!isEtapaValid) {
                    this.getValidation().add(new RouteParam("modelos", "Selecione um item no orçamento!"));
                } else {
                    calcPrevEntrega();
                }

                setIsValidEtapa2(isEtapaValid);
            break;
            case ETAPA3:
                this.getValidation().clear();
                boolean isValidPrevEntrega = true;
                boolean isValidMaoObra = true;

                if (getPrevEntrega() == null) {
                    this.getValidation().add(new RouteParam("prev_entrega", "Informe a data de previsão de entrega válido!"));
                    isValidPrevEntrega = false;
                }

                if (getValorMaoObra() <= 0) {
                    this.getValidation().add(new RouteParam("mao_obra", "Informe o valor de mão de obra válido!"));
                    isValidMaoObra = false;
                }

                isEtapaValid = isValidPrevEntrega && isValidMaoObra;
            break;
        }
        return isEtapaValid;
    }

    public List<TbModelo> getModelos()
    {
        return this.modelos;
    }

    public void setModelos(List<TbModelo> modelos)
    {
        this.modelos = modelos;
    }

    public void setModelo(TbModelo modelo)
    {
        this.modelos.add(modelo);
    }

    public TbModelo getModelo(int index)
    {
        for (TbModelo model : getModelos()) {
            if (model.getIndex() == index) {
                return model;
            }
        }

        return new TbModelo();
    }

    public boolean hasIndex(int index)
    {
        for (TbModelo model : getModelos()) {
            if (model.getIndex() == index) {
                return true;
            }
        }

        return false;
    }

    public String getPrecoItensGeralString()
    {
        float total = 0;
        for (TbModelo modelo : getModelos()) {
            total += modelo.getPrecoItemTotal();
        }

        return formatMoney(total);
    }

    public String getPrecoSubTotalString()
    {
        float total = 0;
        for (TbModelo modelo : getModelos()) {
            total += modelo.getPrecoItemTotal();
        }

        total = total - ((total * desconto) / 100);

        return formatMoney(total + getValorMaoObra());
    }

    public float getPrecoSubTotal()
    {
        float total = 0;
        for (TbModelo modelo : getModelos()) {
            total += modelo.getPrecoItemTotal();
        }

        total = total - ((total * desconto) / 100);

        return total + getValorMaoObra();
    }

    public float getDesconto()
    {
        return this.desconto;
    }

    public String getDescontoString()
    {
        return formatMoney(this.desconto);
    }

    public void setDesconto(float desconto)
    {
        this.desconto = desconto;
    }

    public Date getPrevEntrega()
    {
        return this.prevEntrega;
    }

    public String getPrevEntregaString()
    {
        return this.getSimpleDateFormat("dd/MM/yyyy").format(this.prevEntrega);
    }

    public void setPrevEntrega(Date prevEntrega)
    {
        this.prevEntrega = prevEntrega;
    }

    public void calcPrevEntrega()
    {
        this.prevEntrega = getPrevEntregaCalculo();
    }

    public Date getPrevEntregaCalculo()
    {
        // Classe que vai verificar o feriado
        Feriados  feriados = new Feriados();

        // Soma de dias de produção de todos os itens
        int somaProducao = getTotalDiasProducao();

        // Instancia do dia de hj
        Date data = new Date();

        // Dia de hj mais a soma de dias de producao dos itens
        data = new Date(data.getTime() + TimeUnit.DAYS.toMillis(somaProducao + 1));

        // Instancia do Calendario
        Calendar cal = Calendar.getInstance();

        try {
            // validação de FDS
            int week = 0;
            while (true) {
                // Verifica se hj é o FDS Sunday 1.... Saturday 7
                cal.setTime(data);
                week = cal.get(Calendar.WEEK_OF_YEAR);

                // Se hj é feriado ou se é fds, soma mais um dia
                if (feriados.verificaFeriado(data) || week == 6 || week == 7) {
                    data = new Date(data.getTime() + TimeUnit.DAYS.toMillis(1));
                } else {
                    break;
                }
            }
        } catch (ParseException e) {
            System.out.println("ERRO CALCULO DATA");
            data = new Date();
        } catch (Exception e) {
            System.out.println("ERRO CALCULO DATA 2");
            data = new Date();
        }

        return data;
    }

    public float getValorMaoObra()
    {
        return this.valorMaoObra;
    }

    public String getValorMaoObraString()
    {
        return formatMoney(this.valorMaoObra);
    }

    public void setValorMaoObra(float valorMaoObra)
    {
        this.valorMaoObra = valorMaoObra;
    }

    public String getObservacao()
    {
        return this.observacao;
    }

    public void setObservacao(String observacao)
    {
        this.observacao = observacao;
    }

    public String getPorcentagemMaximaSomaString()
    {
        // Calculo dos valores sem acrescimo
        float totalSemAcrescimo = 0;
        float totalPintura = 0;
        for (TbModelo modelo : getModelos()) {
            totalSemAcrescimo += modelo.getPrecoItemTotalSemAcrescimoSemPintura();
            totalPintura += modelo.getIsPintura() ? modelo.getPrecoPintura() : 0;
        }

        // Calculo dos valores com acrescimo
        float totalComAcrescimo = 0;
        for (TbModelo modelo : getModelos()) {
            totalComAcrescimo += modelo.getPrecoItemTotalComAcrescimoSemPintura();
        }

        float porcentagem = (100 - ((totalSemAcrescimo * 100) / totalComAcrescimo));

        return new DecimalFormat("#.##").format(porcentagem);
    }

    public int getTotalDiasProducao()
    {
        int total = 0;
        for (TbModelo modelo : getModelos()) {
            total += modelo.getQtdDiasProducao();
        }

        return total;
    }

    public float getTotalItensAcrescimoSemPintura()
    {
        // Calculo dos valores com acrescimo
        float totalComAcrescimo = 0;
        for (TbModelo modelo : getModelos()) {
            totalComAcrescimo += modelo.getPrecoItemTotalComAcrescimoSemPintura();
        }

        return totalComAcrescimo;
    }

    public float getTotalPintura()
    {
        // Calculo dos valores com acrescimo
        float totalPintura = 0;
        for (TbModelo modelo : getModelos()) {
            totalPintura += modelo.getIsPintura() ? modelo.getPrecoPintura() : 0;
        }

        return totalPintura;
    }
}
