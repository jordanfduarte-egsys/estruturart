package br.com.estruturart.service;

import br.com.estruturart.model.Orcamento;
import java.sql.Connection;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import br.com.estruturart.persistency.Usuario;
import br.com.estruturart.persistency.Endereco;
import br.com.estruturart.persistency.Pedido;
import br.com.estruturart.persistency.LogPedido;
import br.com.estruturart.persistency.PedidoItem;
import br.com.estruturart.persistency.Lancamento;

import br.com.estruturart.model.TbPedido;
import br.com.estruturart.model.TbPedidoItem;
import br.com.estruturart.model.TbLancamento;
import br.com.estruturart.model.TbModelo;
import java.text.SimpleDateFormat;
import java.util.Date;

public class FinalizarOrcamento
{
    private HttpServletRequest request;
    private HttpServletResponse response;
    private Orcamento orcamento;
    private boolean isOrcamento = false;
    private Connection connection = null;

    private Usuario usuario;
    private Endereco endereco;
    private Pedido pedido;
    private PedidoItem pedidoItem;
    private LogPedido logPedido;
    private Lancamento lancamento;

    public FinalizarOrcamento(HttpServletRequest request, HttpServletResponse response, Orcamento orcamento)
    {
        this.request = request;
        this.response = response;
        this.orcamento = orcamento;
    }

    public void salvar() throws java.sql.SQLException
    {
        this.salvarCliente();
        this.salvarPedido();
        this.salvarLogPedido();
        this.salvarItens();
        this.salvarEndereco();
    }

    public void salvarCliente() throws java.sql.SQLException
    {
        if (orcamento.getUsuario().getId() == 0) {
            orcamento.getUsuario().setId(usuario.insert(orcamento.getUsuario()));
        }
    }

    public void salvarEndereco() throws java.sql.SQLException
    {
        orcamento.getEndereco().setComplemento(
            String.format("Endereço cadastrado ao criar o pedido %d", orcamento.getPedido().getId())
        );
        orcamento.getEndereco().setUsuarioId(orcamento.getUsuario().getId());
        orcamento.getEndereco().setPedidoId(orcamento.getPedido().getId());
        endereco.insert(orcamento.getEndereco());
    }

    public void salvarPedido() throws java.sql.SQLException
    {
        TbPedido pedidoEntity = new TbPedido();
        pedidoEntity.setCaminhoArquivoNotaFiscal("");
        pedidoEntity.setDataPrevisaoInstalacao(orcamento.getPrevEntrega());
        pedidoEntity.setValorTotal(orcamento.getPrecoSubTotal());
        pedidoEntity.setValorMaoObra(orcamento.getValorMaoObra());
        pedidoEntity.setPedidoPago(0);
        pedidoEntity.setObservacao(orcamento.getObservacao());
        pedidoEntity.setDescontoGeral(orcamento.getDesconto());
        pedidoEntity.setUsuarioId(orcamento.getUsuario().getId());
        pedidoEntity.setStatusPedidoId(isOrcamento
            ? TbPedido.ORCAMENTO_PENDENTE
            : TbPedido.PEDIDO_PENDENTE
        );
        orcamento.setPedido(pedidoEntity);
        orcamento.getPedido().setId(pedido.insert(pedidoEntity));
    }

    public void salvarItens() throws java.sql.SQLException
    {
        for (TbModelo modelo : orcamento.getModelos()) {
            TbPedidoItem pedidoItemEntity = new TbPedidoItem();
            pedidoItemEntity.setLargura(modelo.getLarguraNova());
            pedidoItemEntity.setAltura(modelo.getAlturaNova());
            pedidoItemEntity.setQuantidade(modelo.getQuantidadeCompra());
            pedidoItemEntity.setStatusItemId(TbPedidoItem.ATIVO);
            pedidoItemEntity.setPedidoId(orcamento.getPedido().getId());
            pedidoItemEntity.setModeloId(modelo.getId());

            pedidoItemEntity.setId(pedidoItem.insert(pedidoItemEntity));

            TbLancamento lancamento = new TbLancamento();
            lancamento.setPreco(modelo.getPrecoTotal());
            lancamento.setPrecoPintura(modelo.getPrecoPintura());
            lancamento.setDescricao(
                String.format(
                    "Lançamento referente ao item #%d do pedido %d no dia %s",
                    pedidoItemEntity.getId(),
                    orcamento.getPedido().getId(),
                    new SimpleDateFormat("dd/MM/yyyy").format(new Date())

                )
            );
            lancamento.setDesconto(modelo.getPorcentagemAcrescimo());
            lancamento.setUsuarioId(orcamento.getUsuario().getId());
            lancamento.setPedidoItensId(pedidoItemEntity.getId());
            salvarLancamentos(lancamento);
        }
    }

    public void salvarLogPedido() throws java.sql.SQLException
    {
        logPedido.insert(orcamento.getPedido().getStatusPedidoId(), orcamento.getUsuario().getId());
    }

    public void salvarLancamentos(TbLancamento lancamentoEntity) throws java.sql.SQLException
    {
        lancamento.insert(lancamentoEntity);
    }

    public void setIsOrcamento(boolean isOrcamento)
    {
        this.isOrcamento = isOrcamento;
    }

    public int getId()
    {
        return orcamento.getPedido().getId();
    }

    public void setConnection(Connection conn)
    {
        connection = conn;

        usuario = new Usuario();
        endereco = new Endereco();
        pedido = new Pedido();
        pedidoItem = new PedidoItem();
        lancamento = new Lancamento();
        logPedido = new LogPedido();

        usuario.setConnection(conn);
        endereco.setConnection(conn);
        pedido.setConnection(conn);
        pedidoItem.setConnection(conn);
        logPedido.setConnection(conn);
        lancamento.setConnection(conn);
    }
}
