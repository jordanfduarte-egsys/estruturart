package br.com.estruturart.model;

public class TbStatusUsuario
{
    private int id;
    private String descricao;
    /**
     * @return the id
     */
	public int getId()
	{
        return id;
    }
    /**
     * @param id the id to set
     */
	public void setId(int id)
	{
        this.id = id;
    }
    /**
     * @return the descricao
     */
	public String getDescricao()
	{
        return descricao;
    }
    /**
     * @param descricao the descricao to set
     */
	public void setDescricao(String descricao)
	{
        this.descricao = descricao;
    }
}
