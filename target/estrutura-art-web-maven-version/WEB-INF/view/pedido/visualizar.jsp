<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ page contentType="text/html; charset=UTF-8" %>
<link rel="stylesheet" href="${source}css/module/pedido/visualizar.css">

<div class="content p-4">
    <h2>Detalhe do pedido <i class="fab fa-slack-hash"></i>${pedido.getIdString()}</h2>
    <div class="card mb-4">
        <div class="card-body">
            <div class="form-row">
                <div class="col-md-5 mb-3">
                    <h3>Status: ${pedido.getStatusPedido().getNome()}</h3>
                </div>
                <div class="col-md-3 mb-3">
                    <c:if test="${pedido.getStatusPedidoId() != 7 && pedido.getStatusPedidoId() != 6}">
                        <label for="status_pedido_id">Status</label>
                        <select data-id="${pedido.getId()}" data-status="${pedido.getStatusPedidoId()}" name="status_pedido_id" data-original-status="${pedido.getStatusPedidoId()}" id="status_pedido_id" data-id="${pedido.getId()}" class="form-control <c:if test="${pedido.getValidation().hasParam('status_pedido_id')}">is-invalid</c:if>">
                            <c:forEach items="${statusPedido}" var="iterator">
                                <option value="${iterator.getId()}" <c:if test="${pedido.getStatusPedidoId() == iterator.getId()}">selected</c:if>>${iterator.getNome()}</option>
                            </c:forEach>
                        </select>
                    </c:if>
                    <c:if test="${pedido.getStatusPedidoId() == 7}">
                        <label>&nbsp;</label><br>
                        Status: Cancelado
                    </c:if>
                </div>
                <div class="col-md-2 mb-3 text-right">
                    <label for="id">&nbsp;</label><br/>
                    <c:if test="${pedido.getStatusPedidoId() != 7 && pedido.getStatusPedidoId() != 6}">
                        <button type="button" class="btn btn-outline-secondary js-cancelar-pedido" data-id="${pedido.getId()}">Cancelar Pedido</button>
                    </c:if>
                </div>
            </div>
            <div class="form-row">
                <div class="col-md-5 mb-5">
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
                        <li>
                            <span class="li-one">Pedido pago</span>
                            <span class="li-two">${pedido.getPedidoPagoString()}</span>
                        </li>
                    </ul>
                </div>
                <div class="col-md-5 mb-3">
                    <label for="observacao">Observação: </label>
                    <textarea disabled="disabled" class="form-control <c:if test="${pedido.getValidation().hasParam('observacao')}">is-invalid</c:if>" id="observacao" name="observacao" placeholder="Sobre o orçamento">${pedido.getObservacao()}</textarea>

                    <div class="mt-3 right text-right">
                        <h3>Preço total: R$ ${pedido.getPrecoGeralMaisMaoObraString()}</h3>
                    </div>
                </div>
            </div>

            <fieldset class="col-md-10 mb-3 border">
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
            <fieldset class="col-md-10 mb-3 border">
                <legend style="width: 48px;">Itens</legend>
                <div class="table-modelos-add etapa3 table-error form-control">
                    <table class="table table-striped js-row-material">
                        <tbody>
                            <c:forEach items="${pedido.getItens()}" var="iterator">
                                <tr class="<c:if test="${iterator.getStatusItemId() == 2}">opaco</c:if>" id="item-${iterator.getId()}">
                                    <td><b data-toggle="tooltip" title="Status do item: ${iterator.getStatusItem().getNome()}">#${iterator.getIdString()} - </b>${iterator.getModelo().getNome()} - ${iterator.getDimensao()}</td>
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
                                <tr class="<c:if test="${iterator.getStatusItemId() == 2}">opaco</c:if>">
                                    <td colspan="5">
                                        <div class="pull-left pg-0 text-left">
                                            ${iterator.getModelo().getDescricao()}
                                        </div>
                                        <div class="pull-right text-right" data-id="${iterator.getId()}" data-idString="${iterator.getIdString()}" data-status="${iterator.getStatusItemId()}">
                                            <a href="javascript:void(0);" data-toggle="tooltip" title="Visualizar lançamentos do item" class="js-lancamento fa-lg"><i class="far fa-money-bill-alt text-success"></i></a>
                                            <a href="javascript:void(0);" data-toggle="tooltip" title="Visualizar fotos do item" class="js-fotos fa-lg"><i class="fas fa-camera-retro text-secondary"></i></a>
                                        </div>
                                    </td>
                                </tr>
                                <tr class="tr-none"><td></td></tr>
                            </c:forEach>
                        </tbody>
                    </table>
                </div>
            </fieldset>
            <fieldset class="col-md-10 mb-3 border">
                <legend style="width: 93px;">Histórico</legend>
                <div class="table-modelos-add etapa3 table-error form-control">
                    <ul class="ul-resumo-historico">
                        <c:forEach items="${logs}" var="iterator">
                            <li>
                                <span class="li-one">${iterator.getStatusPedido().getNome()}</span>
                                <span class="li-two">${iterator.getDataInclusaoString()} - ${iterator.getUsuario().getNome().toLowerCase()}</a></span>
                            </li>
                        </c:forEach>
                    </ul>
                </div>
            </fieldset>
            <div class="card-footer bg-white">
                <button type="button" class="btn btn-outline-secondary js-back-order" data-href="/pedido">Voltar</button>
                <c:if test="${pedido.getStatusPedidoId() > 2 && pedido.getStatusPedidoId() != 7}">
                    <button type="button" class="btn btn-secondary js-imprimir-op" data-href="/pedido/imprimir-op/id/${pedido.getId()}"><i class="fas fa-industry text-primary"></i> Imprimir OP</button>
                </c:if>
                <c:if test="${pedido.getStatusPedidoId() != 7}">
                    <button type="button" class="btn btn-primary js-editar" data-href="/pedido/editar/id/${pedido.getId()}"><i class="fas fa-pencil-alt"></i> Editar</button>
                </c:if>
            </div>
        </div>
    </div>
</div>

<div class="modal fade" id="alteracaoStatus" tabindex="-1" role="dialog" aria-labelledby="exampleModalCenterTitle" aria-hidden="true" style="display: none;">
    <div class="modal-dialog modal-dialog-centered" role="document">
        <div class="modal-content">
            <div class="modal-header">
                <h5 class="modal-title" id="exampleModalCenterTitle">Alteração de status</h5>
                <button type="button" class="close" data-dismiss="modal" aria-label="Close">
                    <span aria-hidden="true">×</span>
                </button>
            </div>
            <div class="modal-body">
                <p>Deseja alterar o status do pedido para <b></b> ?</p>
            </div>
            <div class="modal-footer">
                <button type="button" class="btn btn-secondary" data-dismiss="modal">Cancelar</button>
                <button type="button" class="btn btn-primary">Confirmar</button>
            </div>
        </div>
    </div>
</div>

<div class="modal fade" id="cancelarPedido" tabindex="-1" role="dialog" aria-labelledby="exampleModalCenterTitle" aria-hidden="true" style="display: none;">
    <div class="modal-dialog modal-dialog-centered" role="document">
        <div class="modal-content">
            <div class="modal-header">
                <h5 class="modal-title" id="exampleModalCenterTitle">Cancelar pedido #${pedido.getIdString()}</h5>
                <button type="button" class="close" data-dismiss="modal" aria-label="Close">
                    <span aria-hidden="true">×</span>
                </button>
            </div>
            <div class="modal-body">
                <p>Deseja cancelar o pedido <b></b> ?</p>
            </div>
            <div class="modal-footer">
                <button type="button" class="btn btn-secondary" data-dismiss="modal">Cancelar</button>
                <button type="button" class="btn btn-primary">Confirmar</button>
            </div>
        </div>
    </div>
</div>

<div class="modal fade" id="lancamentos" tabindex="-1" role="dialog" aria-labelledby="exampleModalCenterTitle" aria-hidden="true" style="display: none;"></div>
<div class="modal fade" id="fotos" tabindex="-1" role="dialog" aria-labelledby="exampleModalCenterTitle" aria-hidden="true" style="display: none;"></div>

<script type="text/x-handlebars-template" id="modal-template-lancamento">
    <div class="modal-dialog modal-dialog-centered modal-lg" role="document">
        <div class="modal-content">
            <div class="modal-header">
                <h5 class="modal-title" id="exampleModalCenterTitle">Lançamentos do item <i class="fab fa-slack-hash"></i>{{idString}}</h5>
                <button type="button" class="close" data-dismiss="modal" aria-label="Close">
                    <span aria-hidden="true">×</span>
                </button>
            </div>
            <div class="modal-body">
                    <c:if test="${pedido.getStatusPedidoId() != 7}">
                        {{#if status_item_id == 1}}
                            <fieldset class="col-md-12 mb-3 border form-control">
                                <legend>Novo</legend>
                                <div class="form-row">
                                    <div class="col-md-7 mb-3">
                                        <label for="descricao">Descrição lançamento *</label>
                                        <input type="text" class="form-control" id="descricao" name="descricao" placeholder="Informação do lançamento" value="">
                                    </div>
                                    <div class="col-md-4 mb-3">
                                        <label for="valor">
                                            Valor *
                                        </label>
                                        <input type="text" data-format="price" class="form-control" id="valor" name="valor" placeholder="R$ 10,00" value="">
                                    </div>
                                    <div class="col-md-1 mb-3">
                                        <label for="cpf_cnpj">&nbsp;</label></br>
                                        <button type="button" class="btn btn-primary btn-icon js-new-lancamento" data-id="{{id}}">
                                            <i class="fa fa-plus"></i>&nbsp;
                                            <i class="fas fa-dollar-sign text-success"></i>
                                        </button>
                                    </div>
                                </div>
                            </fieldset>
                        {{/if}}
                    </c:if>
                <fieldset>
                    <div class="js-target-lancamentos"></div>
                </fieldset>
            </div>
        </div>
    </div>
</script>

<script type="text/x-handlebars-template" id="modal-template-fotos">
    <div class="modal-dialog modal-dialog-centered modal-lg" role="document">
        <div class="modal-content">
            <div class="modal-header">
                <h5 class="modal-title" id="exampleModalCenterTitle">Fotos do item <i class="fab fa-slack-hash"></i>{{idString}}</h5>
                <button type="button" class="close" data-dismiss="modal" aria-label="Close">
                    <span aria-hidden="true">×</span>
                </button>
            </div>
            <div class="modal-body">
                    <c:if test="${pedido.getStatusPedidoId() != 7}">
                        {{#if status_item_id == 1}}
                            <fieldset class="col-md-12 mb-3 border form-control">
                                <legend>Nova foto</legend>
                                <div class="form-row">
                                    <div class="col-md-3 mb-3">
                                        <label for="observacao">Observação *</label>
                                        <input type="text" class="form-control p-2" id="observacao" name="observacao" placeholder="Sobre a foto" value="">
                                    </div>
                                    <div class="col-md-7 mb-3">
                                        <label>&nbsp;</label><br/>
                                        <input type="file" name="nova_foto" class="form-control p-1"/>
                                    </div>
                                    <div class="col-md-2 mb-3">
                                        <label for="cpf_cnpj">&nbsp;</label></br>
                                        <button type="button" class="btn btn-primary pull-right btn-icon js-new-foto" data-id="{{id}}">
                                            <i class="fa fa-plus"></i>&nbsp;Adicionar
                                        </button>
                                    </div>
                                    <div class="col-md-10 mb-3">
                                        <span>Formato aceito ${extensoes}</span>
                                        <br>
                                        <span>Dimensão do arquivo recomendado ${widthModelo}x${heigthModelo}px</span>
                                    </div>
                                </div>
                            </fieldset>
                        {{/if}}
                    </c:if>
                {{#if total > 0}}
                    <fieldset class="col-md-12 mb-3 border form-control">
                        <div class="pull-left col-md-1 pt-6 js-prev-view text-primary" data-toggle="tooltip" title="Voltar"><i class="fas fa-chevron-left fa-2x"></i></div>
                        <div class="js-target-fotos  col-md-10 pull-left p-2 text-center col-md-10">Nenhuma foto cadastrada!</div>
                        <div class="pull-right col-md-1 text-right pt-6 js-next-view text-primary" data-toggle="tooltip" title="Avançar"><i class="fas fa-chevron-right fa-2x"></i></div>
                    </fieldset>
                {{/if}}
            </div>
        </div>
    </div>
</script>

<script type="text/x-handlebars-template" id="row-lancamentos">
    <div style="border: 1px solid #CCC;padding: 5px;" class="form-row m-0 mb-2">
        <div class="pull-left col-md-12">
            <div class="pull-left">{{descricao}}</div>
        </div>
        <div class="pull-left col-md-12">
            <span>Inclusão: <b>{{dataInclusaoString toDateTimeString}}</b>&nbsp;</span>
            <span>Preço: R$ {{preco numberToReal}}&nbsp;</span>
            <span>Pintura: <b>R$ {{precoPintura numberToReal}}</b>&nbsp;</span>
            <span class="pull-right">Total: <b>R$ {{totalMaisPintura numberToReal}}</b></span>
        </div>
    </div>
</script>

<script type="text/x-handlebars-template" id="row-fotos">
    <div style="border: 1px solid #CCC;padding: 5px;" class="form-row mr-2 col-md-2">
        <div class="pull-left col-md-12 text-center">
            {{observacaoLimitado}}
        </div>
        <div class="pull-left col-md-12">
            <img src="${source}{{caminhoArquivoCompleto}}" data-image="show" class="" data-toggle="tooltip" title="{{observacao}}" style="width: 78px;"/>
        </div>
    </div>
</script>