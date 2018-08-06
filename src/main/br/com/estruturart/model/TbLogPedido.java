package br.com.estruturart.model;

import java.util.Date;
import java.util.Calendar;
import java.sql.SQLException;

import br.com.estruturart.model.TbPedido;
import br.com.estruturart.model.TbUsuario;
import br.com.estruturart.utility.RouteParam;

public class TbLogPedido extends AbstractModel
{
    private int id = 0;
    private Date dataInclusao;
    private int statusPedidoId;
    private int pedidoId;
    private int usuarioId;
    private TbPedido pedido;
    private TbUsuario usuario;
    private TbStatusPedido statusPedido;

    public TbStatusPedido getStatusPedido()
    {
        return this.statusPedido;
    }

    public void setStatusPedido(TbStatusPedido statusPedido)
    {
        this.statusPedido = statusPedido;
    }

    public int getId()
    {
        return this.id;
    }

    public void setId(int id)
    {
        this.id = id;
    }

    public Date getDataInclusao()
    {
        return this.dataInclusao;
    }

    public void setDataInclusao(Date dataInclusao)
    {
        this.dataInclusao = dataInclusao;
    }

    public String getDataInclusaoString()
    {
        return this.getSimpleDateFormat("dd/MM/yyyy HH:mm:ss").format(getDataInclusao());
    }

    public int getStatusPedidoId()
    {
        return this.statusPedidoId;
    }

    public void setStatusPedidoId(int statusPedidoId)
    {
        this.statusPedidoId = statusPedidoId;
    }

    public int getUsuarioId()
    {
        return this.usuarioId;
    }

    public void setUsuarioId(int usuarioId)
    {
        this.usuarioId = usuarioId;
    }

    public TbPedido getPedido()
    {
        return this.pedido;
    }

    public void setPedido(TbPedido pedido)
    {
        this.pedido = pedido;
    }

    public TbUsuario getUsuario()
    {
        return this.usuario;
    }

    public void setUsuario(TbUsuario usuario)
    {
        this.usuario = usuario;
    }

    public int getPedidoId()
    {
        return this.pedidoId;
    }

    public void setPedidoId(int pedidoId)
    {
        this.pedidoId = pedidoId;
    }

    public boolean isValid() {return true;}
}