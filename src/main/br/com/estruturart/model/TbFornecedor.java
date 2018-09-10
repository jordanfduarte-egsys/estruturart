package br.com.estruturart.model;

import java.sql.Date;
import java.sql.SQLException;

import br.com.estruturart.persistency.Fornecedor;
import br.com.estruturart.utility.RouteParam;
import br.com.estruturart.utility.Util;

public class TbFornecedor extends AbstractModel
{
    private Integer id = 0;
    private String nome = "";
    private Date dataInclusao;
    private int status;
    private String telefone = "";

    /**
     * @return the id
     */
    public Integer getId()
    {
        return id;
    }

    /**
     * @param id
     *            the id to set
     */
    public void setId(Integer id)
    {
        this.id = id;
    }

    /**
     * @return the nome
     */
    public String getNome()
    {
        return nome;
    }

    /**
     * @param nome
     *            the nome to set
     */
    public void setNome(String nome)
    {
        this.nome = nome;
    }

    /**
     * @return the dataInclusao
     */
    public Date getDataInclusao()
    {
        return dataInclusao;
    }

    /**
     * @param dataInclusao
     *            the dataInclusao to set
     */
    public void setDataInclusao(Date dataInclusao)
    {
        this.dataInclusao = dataInclusao;
    }

    /**
     * @return the status
     */
    public int getStatus()
    {
        return status;
    }

    /**
     * @param status
     *            the status to set
     */
    public void setStatus(int status)
    {
        this.status = status;
    }

    public String getStatusNome()
    {
        return this.status == 1 ? "Ativo" : "Inativo";
    }

    public String getDateFormat(String format)
    {
        return this.getSimpleDateFormat(format).format(this.getDataInclusao());
    }

    public String getTelefone()
    {
        return this.telefone;
    }

    public String getTelefoneString()
    {
        if (this.telefone.equals("")) {
            return "-";
        }

        return this.telefone;
    }

    public void setTelefone(String telefone)
    {
        this.telefone = telefone;
    }

    @Override
    public boolean isValid() throws SQLException
    {
        boolean isValid = true;

        if (this.getNome().equals("")) {
            this.getValidation().add(new RouteParam("nome", "Informe a descrição!"));
            isValid = false;
        }

        Fornecedor fornecedor = new Fornecedor();
        if (fornecedor.findFornecedorByNome(this.getNome(), this.getId())) {
            this.getValidation().add(new RouteParam("nome", "Fornecedor já cadastrado!"));
            isValid = false;
        }
        return isValid;
    }
}
