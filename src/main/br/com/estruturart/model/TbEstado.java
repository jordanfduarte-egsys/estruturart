package br.com.estruturart.model;

public class TbEstado extends AbstractModel
{
    private Integer id = 0;
    private String uf;
    private String nome;

    public Integer getId()
    {
        return this.id;
    }

    public void setId(Integer id)
    {
        this.id = id;
    }

    public String getUf()
    {
        return this.uf;
    }

    public void setUf(String uf)
    {
        this.uf = uf;
    }

    public String getNome()
    {
        return this.nome;
    }

    public void setNome(String nome)
    {
        this.nome = nome;
    }

    public boolean isValid() {
        return true;
    }
}