package br.com.estruturart.model;

public class TbPerfil
{
    private int id = 0;
    private String descricao;

    public static int CLIENTE = 1;
    public static int FUNCIONARIO = 2;
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

	public String getIdString()
	{
        return String.valueOf(this.id);
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
