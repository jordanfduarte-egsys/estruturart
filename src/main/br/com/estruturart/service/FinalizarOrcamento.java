package br.com.estruturart.service;

import br.com.estruturart.model.Orcamento;
import java.sql.Connection;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class FinalizarOrcamento
{
    private HttpServletRequest request;
    private HttpServletResponse response;
    private Orcamento orcamento;
    private boolean isOrcamento = false;
    private Connection connection = null;
    private int id = 0;

    public LogErrorService(HttpServletRequest request, HttpServletResponse response, Orcamento orcamento)
    {
        this.request = request;
        this.response = response;
        this.orcamento = orcamento;
    }

    public void salvar()
    {
        this.salvarCliente();
        this.salvarEndereco();
        this.salvarItens();
        this.salvarLancamentos();
    }

    public void salvarCliente()
    {

    }

    public void salvarEndereco()
    {

    }

    public void salvarItens()
    {

    }

    public void salvarLancamentos()
    {

    }

    public void setIsOrcamento(boolean isOrcamento)
    {
        this.isOrcamento = isOrcamento;
    }

    public int getId()
    {
        return id;
    }

    public void setConnection(Connection conn)
    {
        connection = conn;
    }
}
