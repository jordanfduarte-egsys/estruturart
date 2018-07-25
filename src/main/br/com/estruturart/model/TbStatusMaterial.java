package br.com.estruturart.model;

import java.sql.SQLException;

public class TbStatusMaterial extends AbstractModel
{
    private Integer id = 0;
    private String descricao = "";

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

    public String getDescricao()
    {
        return descricao;
    }

    public void setDescricao(String descricao)
    {
        this.descricao = descricao;
    }
}
