package br.com.estruturart.model;

import br.com.estruturart.model.TbCidade;

public class CepModel extends AbstractModel
{
    private Integer id = 0;
    private String cep;
    private String logradouro;
    private String complemento;
    private String bairro;
    private String localidade;
    private String uf;
    private String unidade;
    private String ibge;
    private String gia;
    private TbCidade cidade;

    public void setId(Integer id)
    {
        this.id = id;
    }

    public Integer getId()
    {
        return this.id;
    }

    public String getCep()
    {
        return this.cep;
    }

    public void setCep(String cep)
    {
        this.cep = cep;
    }

    public String getLogradouro()
    {
        return this.logradouro;
    }

    public void setLogradouro(String logradouro)
    {
        this.logradouro = logradouro;
    }

    public String getComplemento()
    {
        return this.complemento;
    }

    public void setComplemento(String complemento)
    {
        this.complemento = complemento;
    }

    public String getBairro()
    {
        return this.bairro;
    }

    public void setBairro(String bairro)
    {
        this.bairro = bairro;
    }

    public String getLocalidade()
    {
        return this.localidade;
    }

    public void setLocalidade(String localidade)
    {
        this.localidade = localidade;
    }

    public String getUf()
    {
        return this.uf;
    }

    public void setUf(String uf)
    {
        this.uf = uf;
    }

    public String getUnidade()
    {
        return this.unidade;
    }

    public void setUnidade(String unidade)
    {
        this.unidade = unidade;
    }

    public String getIbge()
    {
        return this.ibge;
    }

    public void setIbge(String ibge)
    {
        this.ibge = ibge;
    }

    public String getGia()
    {
        return this.gia;
    }

    public void setGia(String gia)
    {
        this.gia = gia;
    }

    public TbCidade getCidade()
    {
        return this.cidade;
    }

    public void setCidade(TbCidade cidade)
    {
        this.cidade = cidade;
    }

    public boolean isValid() {
        return true;
    }
}