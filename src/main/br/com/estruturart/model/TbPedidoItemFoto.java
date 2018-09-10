package br.com.estruturart.model;

import java.util.Date;
import br.com.estruturart.utility.StringUtilsPad;
import br.com.estruturart.utility.RouteParam;
import org.apache.commons.fileupload.FileItem;

public class TbPedidoItemFoto extends AbstractModel
{
    private Integer id = 0;
    private String idStringItem = "";
    private String caminhoArquivo;
    private String caminhoArquivoCompleto;
    private FileItem fileFoto;
    private Date dataInclusao;
    private String observacao;
    private String observacaoLimitado;
    private Integer pedidoItensId = 0;

    public Integer getId()
    {
        return this.id;
    }

    public void setId(Integer id)
    {
        this.id = id;
    }

    public String getCaminhoArquivo()
    {
        return this.caminhoArquivo;
    }

    public void setCaminhoArquivo(String caminhoArquivo)
    {
        this.caminhoArquivo = caminhoArquivo;
        this.caminhoArquivoCompleto = String.format(
            "files/item/%d/%s",
            getPedidoItensId(),
            caminhoArquivo
        );
    }

    public Date getDataInclusao()
    {
        return this.dataInclusao;
    }

    public void setDataInclusao(Date dataInclusao)
    {
        this.dataInclusao = dataInclusao;
    }

    public String getObservacao()
    {
        return this.observacao;
    }

    public void setObservacao(String observacao)
    {
        this.observacao = observacao;
        if (observacao.length() > 13) {
            this.observacaoLimitado = observacao.substring(0, 10) + "...";
        } else {
            this.observacaoLimitado = observacao;
        }
    }

    public Integer getPedidoItensId()
    {
        return this.pedidoItensId;
    }

    public void setPedidoItensId(Integer pedidoItensId)
    {
        this.pedidoItensId = pedidoItensId;
        this.idStringItem = StringUtilsPad.padLeft(String.valueOf(pedidoItensId), 5, "0");
    }

    public String getCaminhoArquivoCompleto()
    {
        return this.caminhoArquivoCompleto;
    }

    public void setCaminhoArquivoCompleto(String caminhoArquivoCompleto)
    {
        this.caminhoArquivoCompleto = caminhoArquivoCompleto;
    }

    public FileItem getFileFoto()
    {
        return this.fileFoto;
    }

    public void setFileFoto(FileItem fileFoto)
    {
        this.fileFoto = fileFoto;
    }

    public boolean isValid()
    {
        boolean isValid = true;
        if (getCaminhoArquivo().equals("")) {
            this.getValidation().add(new RouteParam("foto", "Arquivo informado inválido!"));
            isValid = false;
        }

        if (getObservacao().equals("")) {
            this.getValidation().add(new RouteParam("observacao", "Informe a observação!"));
            isValid = false;
        }

        return isValid;
    }
}