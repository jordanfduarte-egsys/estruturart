package br.com.estruturart.model;

public class TbStatusPedido
{
    private int id;
    private String nome;

    public static final int PEDIDO_PENDENTE = 1;
    public static final int ORCAMENTO_PENDENTE = 2;
    public static final int PRODUCAO = 3;
    public static final int PEDIDO_PAGO = 4;
    public static final int INSTALACAO = 5;
    public static final int FINALIZADO = 6;
    public static final int CANCELADO = 7;

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

    public String getNome()
    {
        return this.nome;
    }

    public void setNome(String nome)
    {
        this.nome = nome;
    }
}
