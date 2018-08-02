<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ page contentType="text/html; charset=UTF-8" %>
<link rel="stylesheet" href="${source}css/module/pedido/visualizar.css">

<div class="content p-4">
    <h2>Detalhe do pedido #${pedido.getIdString()}</h2>
    <div class="card mb-4">
        <div class="card-body">
            <div class="form-row">
                <div class="col-md-4 mb-3">
                    <h3>Status: ${pedido.getStatusPedido().getNome()}</h3>
                </div>
                <div class="col-md-2 mb-3">
                    <label for="status_pedido_id">Status</label>
                    <select name="status_pedido_id" data-original-status="${pedido.getStatusPedidoId()}" id="status_pedido_id" data-id="${pedido.getId()}" class="form-control <c:if test="${pedido.getValidation().hasParam('status_pedido_id')}">is-invalid</c:if>">
                        <option value="0">Selecione!</option>
                        <c:forEach items="${statusPedido}" var="iterator">
                            <option value="${iterator.getId()}" <c:if test="${pedido.getStatusPedidoId() == iterator.getId()}">selected</c:if>>${iterator.getNome()}</option>
                        </c:forEach>
                    </select>
                </div>
                <div class="col-md-2 mb-3 text-right">
                    <label for="id">&nbsp;</label><br/>
                    <button type="button" class="btn btn-outline-secondary js-cancelar-pedido" data-id="${pedido.getId()}">Cancelar Pedido</button>
                </div>
            </div>
            <div class="form-row">
                <div class="col-md-4 mb-3">
                    <ul class="ul-resumo">
                        <li>
                            <span class="li-one">Cliente</span>
                            <span class="li-two"><a target="_BLANK" href="${source}usuario/editar/id/${pedido.getUsuario().getId()}">${pedido.getUsuario().getCpfCnpjString()} - ${pedido.getUsuario().getNome()}</a></span>
                        </li>
                        <li>
                            <span class="li-one">Total Itens</span>
                            <span class="li-two">${pedido.getItens().size()}</span>
                        </li>
                        <li>
                            <span class="li-one">Data de instalação</span>
                            <span class="li-two" style="padding: 0px 2px 1px;color:#FFF; background-color: ${pedido.getCorPrevisaoInstalacao()}">
                                ${pedido.getDataPrevisaoInstalacaoString()}</span>
                        </li>
                        <li>
                            <span class="li-one">Preço dos itens</span>
                            <span class="li-two">R$ ${pedido.getPrecoGeralString()}</span>
                        </li>
                        <li>
                            <span class="li-one">Desconto total</span>
                            <span class="li-two">${pedido.getDescontoGeralString()} %</span>
                        </li>
                        <li>
                            <span class="li-one">Mão de obra</span>
                            <span class="li-two">R$ ${pedido.getValorMaoObraString()}</span>
                        </li>
                        <li>
                            <span class="li-one">Nota fiscal</span>
                            <span class="li-two">${pedido.getNotaFiscalLabel()}</span>
                        </li>
                    </ul>
                </div>
                <div class="col-md-4 mb-3">
                    <label for="observacao">Observação: </label>
                    <textarea disabled="disabled" class="form-control <c:if test="${pedido.getValidation().hasParam('observacao')}">is-invalid</c:if>" id="observacao" name="observacao" placeholder="Sobre o orçamento">${pedido.getObservacao()}</textarea>

                    <div class="mt-3 right text-right">
                        <h3>Preço total: R$ ${pedido.getPrecoGeralMaisMaoObraString()}</h3>
                    </div>
                </div>
            </div>

            <fieldset class="col-md-8 mb-3 border">
                <legend style="width: 223px;">Endereço de instalação</legend>
                <div class="form-row">
                    <div class="mb-3">
                        Endereço: ${pedido.getEndereco().getLogradouro()}, ${pedido.getEndereco().getNumero()} - ${pedido.getEndereco().getBairro()} - ${pedido.getEndereco().getCidade().getNome()} - ${pedido.getEndereco().getCidade().getEstado().getUf()} - CEP: ${pedido.getEndereco().getCepString()}
                    </div>
                    <div class="mb-3">
                        Complemento: ${pedido.getEndereco().getComplemento()}
                    </div>
                </div>
            </fieldset>
            <fieldset class="col-md-8 mb-3 border">
                <legend style="width: 48px;">Itens</legend>
                <div class="table-modelos-add etapa3 table-error form-control">
                    <table class="table table-striped js-row-material">
                        <tbody>
                            <c:forEach items="${pedido.getItens()}" var="iterator">
                                <tr>
                                    <td><b data-toggle="tooltip" title="Status do item: ${iterator.getStatusItem().getNome()}">#${iterator.getIdString()} - </b>${iterator.getModelo().getNome()} - ${iterator.getModelo().getDimensao()}</td>
                                    <td>Preço: R$ ${iterator.getPrecoItem()}</td>
                                    <td>
                                        <c:if test="${iterator.getIsPintura()}">
                                            <label for="pintura-${iterator.getId()}">&nbsp;+ Pintura: <b> R$ ${iterator.getSomaPrecoPintura()}</b></label>
                                        </c:if>
                                        <c:if test="${iterator.getIsPintura() == false}">
                                            <label for="pintura-${iterator.getId()}">Sem pintura</label>
                                        </c:if>
                                    </td>
                                    <td>
                                        <b>Qtd.:</b> ${iterator.getQuantidade()} un.
                                    </td>
                                    <td>
                                        &nbsp;
                                    </td>
                                </tr>
                                <tr>
                                    <td colspan="5">
                                        <div class="pull-left pg-0 text-left">
                                            ${iterator.getModelo().getDescricao()}
                                        </div>
                                        <div class="pull-right text-right" data-id="${iterator.getId()}">
                                            <a href="javascript:void(0);" data-toggle="tooltip" title="Visualizar lançamentos do item" class="js-lancamento fa-2x"><i class="far fa-money-bill-alt text-success"></i></a>
                                            <a href="javascript:void(0);" data-toggle="tooltip" title="Visualizar fotos do item" class="js-fotos fa-2x"><i class="fas fa-camera-retro text-secondary"></i></a>
                                        </div>
                                    </td>
                                </tr>
                                <tr class="tr-none"><td></td></tr>
                            </c:forEach>
                        </tbody>
                    </table>
                </div>
            </fieldset>
            <div class="card-footer bg-white">
                <button type="button" class="btn btn-outline-secondary js-back-order" data-href="/pedido">Voltar</button>
                <button type="button" class="btn btn-default js-imprimir-op" data-href="/pedido/imprimir-op/id/${pedido.getId()}"><i class="fas fa-industry text-primary"></i> Imprimir OP</button>
                <button type="button" class="btn btn-primary js-editar" data-href="/pedido/editar/id/${pedido.getId()}"><i class="fas fa-pencil-alt"></i> Editar</button>
            </div>
        </div>
    </div>
</div>