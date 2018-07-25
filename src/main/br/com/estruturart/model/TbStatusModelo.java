package br.com.estruturart.model;

import java.sql.SQLException;

public class TbStatusModelo extends AbstractModel
{
    private Integer id = 0;
    private String nome = "";

    @Override
    public boolean isValid() throws SQLException
    {
        return false;
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

    public void setNome(String descricao)
    {
        this.nome = descricao;
    }
}
