package br.com.estruturart.model;

import java.sql.Date;
import java.sql.SQLException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.fileupload.FileItem;

import br.com.estruturart.persistency.Modelo;
import br.com.estruturart.utility.RouteParam;

public class TbModelo extends AbstractModel
{
    private Integer id = 0;
    private String nome = "";
    private String descricao = "";
    private float larguraPadrao;
    private float alturaPadrao;
    private float larguraNova;
    private float alturaNova;
    private String imagem;
    private String imagemSource = "";
    private float precoPintura = 0;
    private float porcentagemAcrescimo = 1;
    private int qtdDiasProducao = 1;
    private Date dataInclusao;
    private int statusModeloId;
    private TbStatusModelo statusModelo;
    private List<TbMaterial> materiais = new ArrayList<TbMaterial>();
    private FileItem fileItem;
    private float precoTotalSubQuery;
    private String precoTotalQtdStr;
    private int index = 0;
    private boolean isPintura = false;
    private int quantidadeCompra = 1;
    private String base64Image;

    public TbModelo()
    {
        setStatusModelo(new TbStatusModelo());
    }

    public Integer getId()
    {
        return id;
    }

    public void setId(Integer id)
    {
        this.id = id;
    }

    public String getNome()
    {
        return nome;
    }

    public void setNome(String nome)
    {
        this.nome = nome;
    }

    public String getDescricao()
    {
        return descricao;
    }

    public void setDescricao(String descricao)
    {
        this.descricao = descricao;
    }

    public float getLarguraPadrao()
    {
        return larguraPadrao;
    }

    public String getLarguraPadraoString()
    {
        DecimalFormat twoPlaces = new DecimalFormat("0.00");
        return twoPlaces.format(larguraPadrao).equals("0,00") ? "" : twoPlaces.format(larguraPadrao);
    }

    public void setLarguraPadrao(float larguraPadrao)
    {
        this.larguraPadrao = larguraPadrao;
    }

    public float getAlturaPadrao()
    {
        return alturaPadrao;
    }

    public String getAlturaPadraoString()
    {
        DecimalFormat twoPlaces = new DecimalFormat("0.00");
        return twoPlaces.format(alturaPadrao).equals("0,00") ? "" : twoPlaces.format(alturaPadrao);
    }

    public void setAlturaPadrao(float alturaPadrao)
    {
        this.alturaPadrao = alturaPadrao;
    }

    public String getImagem()
    {
        return imagem;
    }

    public void setImagem(String imagem)
    {
        this.imagem = imagem;
        this.getImagemSource();
    }

    public float getPrecoPintura()
    {
        return precoPintura;
    }

    public String getPrecoPinturaString()
    {
        return formatMoney(precoPintura);
    }

    public void setPrecoPintura(float precoPintura)
    {
        this.precoPintura = precoPintura;
    }

    public float getPorcentagemAcrescimo()
    {
        return porcentagemAcrescimo;
    }

    public String getPorcentagemAcrescimoString()
    {
        return String.valueOf(Math.round(porcentagemAcrescimo));
    }

    public void setPorcentagemAcrescimo(float porcentagemAcrescimo)
    {
        this.porcentagemAcrescimo = porcentagemAcrescimo;
    }

    public int getQtdDiasProducao()
    {
        return qtdDiasProducao;
    }

    public String getQtdDiasProducaoString()
    {
        return String.valueOf(Math.round(qtdDiasProducao));
    }

    public void setQtdDiasProducao(int qtdDiasProducao)
    {
        this.qtdDiasProducao = qtdDiasProducao;
    }

    public Date getDataInclusao()
    {
        return dataInclusao;
    }

    public void setDataInclusao(Date dataInclusao)
    {
        this.dataInclusao = dataInclusao;
    }

    public int getStatusModeloId()
    {
        return statusModeloId;
    }

    public void setStatusModeloId(int statusModeloId)
    {
        this.statusModeloId = statusModeloId;
    }

    public String getDateFormat(String format)
    {
        return this.getSimpleDateFormat(format).format(this.getDataInclusao());
    }

    @Override
    public boolean isValid() throws SQLException
    {
        boolean isValid = true;

        if (this.getNome().equals("")) {
            this.getValidation().add(new RouteParam("nome", "Informe a nome!"));
            isValid = false;
        }

        if (this.getDescricao().equals("")) {
            this.getValidation().add(new RouteParam("descricao", "Informe a descrição!"));
            isValid = false;
        }

        if (this.getLarguraPadrao() <= 0) {
            this.getValidation().add(new RouteParam("largura_padrao", "Informe a largura padrão!"));
            isValid = false;
        }

        if (this.getAlturaPadrao() <= 0) {
            this.getValidation().add(new RouteParam("altura_padrao", "Informe a altura padrão!"));
            isValid = false;
        }

        if (this.getPorcentagemAcrescimo() <= 0) {
            this.getValidation().add(new RouteParam("porcentagem_acrescimo", "Informe a porcentagem de acréscimo!"));
            isValid = false;
        }

        if (this.getQtdDiasProducao() <= 0) {
            this.getValidation()
                    .add(new RouteParam("qtd_dias_producao", "Informe a quantidade de dias para produção!"));
            isValid = false;
        }

        if (this.getStatusModeloId() <= 0) {
            this.getValidation().add(new RouteParam("status_modelo_id", "Seleciona o status do modelo!"));
            isValid = false;
        }

        if (this.getMateriais().size() == 0) {
            this.getValidation()
                    .add(new RouteParam("materiais", "Seleciona os materiais necessários para montagem desse modelo!"));
            isValid = false;
        }

        Modelo modelo = new Modelo();
        if (modelo.findModeloByNome(this.getNome(), this.getId())) {
            this.getValidation().add(new RouteParam("nome", "Modelo já cadastrado!"));
            isValid = false;
        }

        return isValid;
    }

    /**
     * @return the statusModelo
     */
    public TbStatusModelo getStatusModelo()
    {
        return statusModelo;
    }

    /**
     * @param statusModelo the statusModelo to set
     */
    public void setStatusModelo(TbStatusModelo statusModelo)
    {
        this.statusModelo = statusModelo;
    }

    public String getPrecoPadrao()
    {
        float preco = (float) 1.23;
        DecimalFormat twoPlaces = new DecimalFormat("0.00");
        return twoPlaces.format((preco));
    }

    public String getDimensao()
    {
        return String.format("%sx%smm", new DecimalFormat("#.##").format(this.larguraPadrao),
                new DecimalFormat("#.##").format(this.alturaPadrao));
    }

    public String getDimensaoNova()
    {
        return String.format("%sx%smm", new DecimalFormat("#.##").format(this.larguraNova),
                new DecimalFormat("#.##").format(this.alturaNova));
    }

    public String getImagemSource()
    {
        this.imagemSource = String.format("files/sem-foto.jpg");
        if (!this.imagem.equals("")) {
            this.imagemSource = String.format("files/modelos/%s", this.imagem);
        }

        return this.imagemSource;
    }

    /**
     * @return the materiais
     */
    public List<TbMaterial> getMateriais()
    {
        return materiais;
    }

    /**
     * @param materiais the materiais to set
     */
    public void setMateriais(List<TbMaterial> materiais)
    {
        this.materiais = materiais;
    }

    /**
     * @param materiais
     *            the materiais to set
     */
    public void setMateriais(TbMaterial material)
    {
        this.materiais.add(material);
    }

    public String getPrecoTotalSemPinturaString()
    {
        float total = 0;
        for (TbMaterial material : materiais) {
            total += material.getPreco();
        }

        total = ((total * this.porcentagemAcrescimo) / 100) + total;
        return formatMoney(total);
    }

    public String getPrecoTotalString()
    {
        float sumPrice = this.getPrecoTotal();
        sumPrice = ((sumPrice * this.porcentagemAcrescimo) / 100) + sumPrice;
        return formatMoney(sumPrice + getPrecoPintura());
    }

    public float getPrecoSemAcrescimo()
    {
        float total = 0;
        for (TbMaterial material : materiais) {
            total += material.getPreco();
        }

        return total;
    }

    public String getPrecoTotalQuantidadeString()
    {
        if (this.precoTotalQtdStr == null) {
            float total = 0;
            for (TbMaterial material : materiais) {
                total += material.getPreco();
            }

            total = ((total * this.porcentagemAcrescimo) / 100) + total;
            this.precoTotalQtdStr = String.valueOf(total * this.quantidadeCompra);
        }

        return this.precoTotalQtdStr;
    }

    public String getPrecoTotalMateriaisString()
    {
        float total = 0;
        for (TbMaterial material : materiais) {
            total += material.getPreco();
        }

        return formatMoney(total);
    }

    public float getPrecoTotal()
    {
        float total = 0;
        for (TbMaterial material : materiais) {
            total += material.getPreco();
        }

        return total;
    }

    public float getPrecoItemTotalSemAcrescimoSemPintura()
    {
        return calculoGeral(
            getAlturaPadrao(),
            getLarguraPadrao(),
            getAlturaNova(),
            getLarguraNova(),
            getPrecoSemAcrescimo(),
            false,
            getPrecoPintura(),
            getQuantidadeCompra()
        );
    }

    public float getPrecoItemTotalComAcrescimoSemPintura()
    {
        return calculoGeral(
            getAlturaPadrao(),
            getLarguraPadrao(),
            getAlturaNova(),
            getLarguraNova(),
            Float.valueOf(getPrecoTotalQuantidadeString()),
            false,
            getPrecoPintura(),
            getQuantidadeCompra()
        );
    }

    public String getPrecoItemTotalString()
    {
        return formatMoney(getPrecoItemTotal());
    }

    public float getPrecoItemTotal()
    {
        return calculoGeral(
            getAlturaPadrao(),
            getLarguraPadrao(),
            getAlturaNova(),
            getLarguraNova(),
            Float.valueOf(getPrecoTotalQuantidadeString()),
            getIsPintura(),
            getPrecoPintura(),
            getQuantidadeCompra()
        );
    }

    public float calculoGeral(
        float alturaAntiga,
        float larguraAntiga,
        float alturaNova,
        float larguraNova,
        float precoPadrao,
        boolean isPintura,
        float precoPintura,
        int quantidade
    ) {
        System.out.println(
            "NO HTML ======"
            + " Altura Antiga: " +alturaAntiga
            + " Largura Antiga: " +larguraAntiga
            + " Altura Nova: " +alturaNova
            + " Largura Nova: " +larguraNova
            + " preco padrao: " +precoPadrao
            + " Is Pintura: " +isPintura
            + " Preco Pintura: " +precoPintura
            + " QTD: " +quantidade
        );
        float mediaMedidaAntiga = alturaAntiga * larguraAntiga;
        float mediaMedidaNova = alturaNova * larguraNova;

        float precoNovo = ((mediaMedidaNova * precoPadrao) / mediaMedidaAntiga) * quantidade;

        if (isPintura) {
            precoNovo = precoNovo + precoPintura;
        }

        return precoNovo;
    }

    public String getPrecoComMaxDescontoString()
    {
        float sumPrice = getPrecoTotal();
        return formatMoney(sumPrice + getPrecoPintura());
    }

    /**
     * @return the fileItem
     */
    public FileItem getFileItem()
    {
        return fileItem;
    }

    /**
     * @param fileItem the fileItem to set
     */
    public void setFileItem(FileItem fileItem)
    {
        this.fileItem = fileItem;
    }

    public void clearMateriais()
    {
        materiais.clear();
    }

    /**
     * @return the precoTotalSubQuery
     */
    public float getPrecoTotalSubQuery()
    {
        return precoTotalSubQuery;
    }

    /**
     * @return the precoTotalSubQuery
     */
    public String getPrecoTotalSubQueryString()
    {
        return formatMoney(precoTotalSubQuery);
    }

    /**
     * @param precoTotalSubQuery the precoTotalSubQuery to set
     */
    public void setPrecoTotalSubQuery(float precoTotalSubQuery)
    {
        this.precoTotalSubQuery = precoTotalSubQuery;
    }

    public int getIndex()
    {
        return this.index;
    }

    public void setIndex(int index)
    {
        this.index = index;
    }

    public boolean getIsPintura()
    {
        return this.isPintura;
    }

    public int isPintura()
    {
        return this.isPintura ? 1 : 0;
    }

    public void setIsPintura(boolean isPintura)
    {
        this.isPintura = isPintura;
    }

    public int getQuantidadeCompra()
    {
        return this.quantidadeCompra;
    }

    public void setQuantidadeCompra(int quantidadeCompra)
    {
        this.quantidadeCompra = quantidadeCompra;
    }

    public float getLarguraNova()
    {
        return this.larguraNova;
    }

    public void setLarguraNova(float larguraNova)
    {
        this.larguraNova = larguraNova;
    }

    public float getAlturaNova()
    {
        return this.alturaNova;
    }

    public void setAlturaNova(float alturaNova)
    {
        this.alturaNova = alturaNova;
    }

    public void setBase64Image(String base64Image)
    {
        this.base64Image = base64Image;
    }
}
