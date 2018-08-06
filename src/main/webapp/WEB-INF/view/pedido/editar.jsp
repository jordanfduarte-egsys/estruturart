<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ page contentType="text/html; charset=UTF-8" %>
<link rel="stylesheet" href="${source}css/module/pedido/visualizar.css">

<div class="content p-4">
    <h2>Editar pedido #${pedido.getIdString()}</h2>
    <div class="card mb-4">
        <div class="card-body">
            <form action="${source}pedido/editar/id/${pedido.getId()}" method="POST" autocomplete="off" id="pedido">
                <div class="form-row">
                    <div class="col-md-4 mb-3">
                        <h3>Status: ${pedido.getStatusPedido().getNome()}</h3>
                    </div>
                    <div class="col-md-4 mb-3">
                        <label for="status_pedido_id">Status</label>
                        <select name="status_pedido_id" disabled="disabled" data-original-status="${pedido.getStatusPedidoId()}" id="status_pedido_id" data-id="${pedido.getId()}" class="form-control <c:if test="${pedido.getValidation().hasParam('status_pedido_id')}">is-invalid</c:if>">
                            <option value="0">Selecione!</option>
                            <c:forEach items="${statusPedido}" var="iterator">
                                <option value="${iterator.getId()}" <c:if test="${pedido.getStatusPedidoId() == iterator.getId()}">selected</c:if>>${iterator.getNome()}</option>
                            </c:forEach>
                        </select>
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
                        <div style="margin: 0px 22px 0px 0px;">
                            <label for="prev_entrega">Prev. entrega:</label>
                            <div class="input-group mb-3">
                                <div class="input-group-prepend" data-toggle="tooltip" title="" data-original-title="Verifique a data de previsão de entrega!">
                                    <span class="input-group-text text-primary"><i class="fas fa-truck"></i></span>
                                </div>
                                <input type="text" data-toggle="datepicker" class="form-control <c:if test="${pedido.getValidation().hasParam('prev_entrega')}">is-invalid</c:if>" id="prev_entrega" name="prev_entrega" placeholder="dd/mm/yyyy" value="${pedido.getDataPrevisaoInstalacaoString()}">
                                <div class="invalid-feedback">
                                    ${pedido.getValidation().getParamOr('prev_entrega', '')}
                                </div>
                            </div>
                        </div>
                    </div>
                    <div class="col-md-4 mb-3">
                        <label for="observacao">Observação: </label>
                        <textarea class="form-control <c:if test="${pedido.getValidation().hasParam('observacao')}">is-invalid</c:if>" id="observacao" name="observacao" placeholder="Sobre o orçamento">${pedido.getObservacao()}</textarea>

                        <div class="mt-3 right text-right">
                            <h3>Preço total: R$ ${pedido.getPrecoGeralMaisMaoObraString()}</h3>
                        </div>
                    </div>
                </div>

                <fieldset class="col-md-8 mb-3 border">
                    <legend style="width: 223px;">Endereço de instalação</legend>
                    <div class="form-row">
                        <div class="col-md-4 mb-3">
                            <label for="cep">Cep *</label>
                            <input data-format="cep" type="text" autocomplete="off" class="form-control <c:if test="${pedido.getEndereco().getValidation().hasParam('cep')}">is-invalid</c:if>" id="cep" name="cep" placeholder="CEP" value="${pedido.getEndereco().getCep()}">
                            <div class="invalid-feedback">
                                ${pedido.getEndereco().getValidation().getParamOr('cep', '')}
                            </div>
                        </div>
                        <div class="col-md-4 mb-3">
                            <label for="logradouro">Logradouro *</label>
                            <input type="text" class="form-control <c:if test="${pedido.getEndereco().getValidation().hasParam('logradouro')}">is-invalid</c:if>" id="logradouro" name="logradouro" placeholder="Endereço de entrega" value="${pedido.getEndereco().getLogradouro()}">
                            <div class="invalid-feedback">
                                ${pedido.getEndereco().getValidation().getParamOr('logradouro', '')}
                            </div>
                        </div>
                        <div class="col-md-4 mb-3">
                            <label for="numero">Número *</label>
                            <input type="text" class="form-control <c:if test="${pedido.getEndereco().getValidation().hasParam('numero')}">is-invalid</c:if>" id="numero" name="numero" placeholder="Número da casa" value="${pedido.getEndereco().getNumero()}">
                            <div class="invalid-feedback">
                                ${pedido.getEndereco().getValidation().getParamOr('numero', '')}
                            </div>
                        </div>
                    </div>
                    <div class="form-row">
                        <div class="col-md-4 mb-3">
                            <label for="estado_id">Estado *</label>
                            <select name="estado_id" id="estado_id" class="form-control <c:if test="${pedido.getEndereco().getValidation().hasParam('estado_id')}">is-invalid</c:if>">
                                <option value="0">Selecione!</option>
                                <c:forEach items="${estados}" var="iterator">
                                    <option value="${iterator.getId()}" <c:if test="${pedido.getEndereco().getEstadoId() == iterator.getId()}">selected</c:if>>${iterator.getNome()}</option>
                                </c:forEach>
                            </select>
                            <div class="invalid-feedback">
                                ${pedido.getEndereco().getValidation().getParamOr('estado_id', '')}
                            </div>
                        </div>
                        <div class="col-md-4 mb-3">
                            <label for="cidade_id">Cidade *</label>
                            <select name="cidade_id" id="cidade_id" class="form-control <c:if test="${pedido.getEndereco().getValidation().hasParam('cidade_id')}">is-invalid</c:if>">
                                <option value="0">Selecione!</option>
                                <c:forEach items="${cidades}" var="iterator">
                                    <option value="${iterator.getId()}" <c:if test="${pedido.getEndereco().getCidadeId() == iterator.getId()}">selected</c:if>>${iterator.getNome()}</option>
                                </c:forEach>
                            </select>
                            <div class="invalid-feedback">
                                ${pedido.getEndereco().getValidation().getParamOr('cidade_id', '')}
                            </div>
                        </div>
                        <div class="col-md-4 mb-3">
                            <label for="bairro">Bairro *</label>
                            <input type="text" class="form-control <c:if test="${pedido.getEndereco().getValidation().hasParam('bairro')}">is-invalid</c:if>" id="bairro" name="bairro" placeholder="Bairro" value="${pedido.getEndereco().getBairro()}">
                            <div class="invalid-feedback">
                                ${pedido.getEndereco().getValidation().getParamOr('bairro', '')}
                            </div>
                        </div>
                    </div>
                </fieldset>
                <fieldset class="col-md-8 mb-3 border">
                    <legend style="width: 48px;">Itens</legend>
                    <div class="table-modelos-add etapa3 table-error form-control">
                        <table class="table table-striped js-row-material">
                            <tbody>
                                <c:forEach items="${pedido.getItens()}" var="iterator">
                                    <tr data-id="${pedido.getId()}" class="<c:if test="${iterator.getStatusItemId() == 2}">opaco</c:if>">
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
                                    <tr data-id="${pedido.getId()}" class="<c:if test="${iterator.getStatusItemId() == 2}">opaco</c:if>">
                                        <td colspan="5">
                                            <div class="pull-left pg-0 text-left">
                                                ${iterator.getModelo().getDescricao()}
                                            </div>
                                            <div class="pull-right text-right" data-item="${iterator.getId()}">
                                                <c:if test="${iterator.getStatusItemId() == 1}">
                                                    <a href="javascript:void(0);" data-toggle="tooltip" title="Cancelar item" class="js-remove-item fa-lg"><i class="fas fa-trash-alt text-danger"></i></a>
                                                </c:if>
                                            </div>
                                        </td>
                                    </tr>
                                    <tr data-id="${pedido.getId()}" class="tr-none"><td></td></tr>
                                </c:forEach>
                            </tbody>
                        </table>
                    </div>
                </fieldset>
                <div class="card-footer bg-white">
                    <button type="button" class="btn btn-outline-secondary js-back-order" data-href="/pedido/visualizar/id/${pedido.getId()}">Voltar</button>
                    <button type="submit" class="btn btn-primary"><i class="fas fa-pencil-alt"></i> Salvar</button>
                </div>
            </form>
        </div>
    </div>
</div>

<script type="text/x-handlebars-template" id="row-cidade">
    <option value="{{id}}">{{nome}}</option>
</script>