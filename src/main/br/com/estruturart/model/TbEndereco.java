package br.com.estruturart.model;

import br.com.estruturart.model.TbEstado;
import br.com.estruturart.model.TbUsuario;
import br.com.estruturart.model.TbPedido;
import br.com.estruturart.utility.RouteParam;
import br.com.estruturart.utility.Util;

public class TbEndereco extends AbstractModel
{
    private Integer id = 0;
    private String cep;
    private String logradouro;
    private String bairro;
    private String numero;
    private String complemento;
    private Integer estadoId = 0;
    private Integer cidadeId = 0;
    private Integer usuarioId = 0;
    private Integer pedidoId = 0;
    private TbEstado estado;
    private TbCidade cidade;
    private TbUsuario usuario;
    private TbPedido pedido;

    public Integer getId()
    {
        return this.id;
    }

    public void setId(Integer id)
    {
        this.id = id;
    }

    public String getCep()
    {
        return this.cep;
    }

    public void setCep(String cep)
    {
        this.cep = cep;
    }

    public String getLogradouro()
    {
        return this.logradouro;
    }

    public void setLogradouro(String logradouro)
    {
        this.logradouro = logradouro;
    }

    public String getBairro()
    {
        return this.bairro;
    }

    public void setBairro(String bairro)
    {
        this.bairro = bairro;
    }

    public String getNumero()
    {
        return this.numero;
    }

    public void setNumero(String numero)
    {
        this.numero = numero;
    }

    public String getComplemento()
    {
        return this.complemento;
    }

    public void setComplemento(String complemento)
    {
        this.complemento = complemento;
    }

    public Integer getEstadoId()
    {
        return this.estadoId;
    }

    public void setEstadoId(Integer estadoId)
    {
        this.estadoId = estadoId;
    }

    public Integer getUsuarioId()
    {
        return this.usuarioId;
    }

    public void setUsuarioId(Integer usuarioId)
    {
        this.usuarioId = usuarioId;
    }

    public Integer getPedidoId()
    {
        return this.pedidoId;
    }

    public void setPedidoId(Integer pedidoId)
    {
        this.pedidoId = pedidoId;
    }

    public TbEstado getEstado()
    {
        return this.estado;
    }

    public void setEstado(TbEstado estado)
    {
        this.estado = estado;
    }

    public TbUsuario getUsuario()
    {
        return this.usuario;
    }

    public void setUsuario(TbUsuario usuario)
    {
        this.usuario = usuario;
    }

    public TbPedido getPedido()
    {
        return this.pedido;
    }

    public void setPedido(TbPedido pedido)
    {
        this.pedido = pedido;
    }

    public boolean isValid()
    {
        boolean isValid = true;

        if (getCep().length() < 8 || getCep().length() > 8) {
            this.getValidation().add(new RouteParam("cep", "Cep informado inválido!"));
            isValid = false;
        }

        if (getLogradouro().equals("")) {
            this.getValidation().add(new RouteParam("logradouro", "Logradouro informado inválido!"));
            isValid = false;
        }

        if (getBairro().equals("")) {
            this.getValidation().add(new RouteParam("bairro", "Bairro informado inválido!"));
            isValid = false;
        }

        if (getNumero().equals("")) {
            this.getValidation().add(new RouteParam("numero", "Número da casa informado inválido!"));
            isValid = false;
        }

        if (getCidadeId() <= 0) {
            this.getValidation().add(new RouteParam("cidade_id", "Selecione uma cidade!"));
            isValid = false;
        }

        if (getEstadoId() <= 0) {
            this.getValidation().add(new RouteParam("estado_id", "Selecione um estado!"));
            isValid = false;
        }

        return isValid;
    }

    public Integer getCidadeId()
    {
        return this.cidadeId;
    }

    public void setCidadeId(Integer cidadeId)
    {
        this.cidadeId = cidadeId;
    }

    public TbCidade getCidade()
    {
        return this.cidade;
    }

    public void setCidade(TbCidade cidade)
    {
        this.cidade = cidade;
    }
}