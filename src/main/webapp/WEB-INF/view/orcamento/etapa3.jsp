<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ page contentType="text/html; charset=UTF-8" %>
<script src="${source}js/module/orcamento/commun-orcamento.js"></script>
<link rel="stylesheet" href="${source}css/module/orcamento/orcamento.css">

<div class="content p-4">
    <h2>Orçamento - etapa 3</h2>
    <div class="card mb-4">
        <div class="card-body">
            <form action="${source}orcamento/etapa3" method="POST">
                <div class="form-row">
                    <fieldset class="col-md-8 mb-3 border">
                        <legend style="width: 91px;">Resumo</legend>
                        <div class="table-modelos-add etapa3 table-error form-control">
                            <table class="table table-striped js-row-material" data-total-itens="${orcamento.getTotalItensAcrescimoSemPintura()}" data-total-pintura="${orcamento.getTotalPintura()}">
                                <tbody>
                                    <c:forEach items="${orcamento.getModelos()}" var="iterator">
                                        <tr data-index="${iterador.getIndex()}">
                                            <td>${iterator.getNome()} - ${iterator.getDimensao()}</td>
                                            <td>Preço: R$ ${iterator.getPrecoItemTotalString()}</td>
                                            <td>
                                                <c:if test="${iterator.getIsPintura()}">
                                                    <label for="pintura-${iterator.getIndex()}">&nbsp;Pintura: <b>+ R$ ${iterator.getPrecoPinturaString()}</b></label>
                                                </c:if>
                                                <c:if test="${iterator.getIsPintura() == false}">
                                                    <label for="pintura-${iterator.getIndex()}">Sem pintura</label>
                                                </c:if>
                                            </td>
                                            <td>
                                                <b>Qtd.:</b> ${iterator.getQuantidadeCompra()} un.
                                            </td>
                                            <td>
                                                &nbsp;
                                            </td>
                                        </tr>
                                        <tr data-index="${iterador.getIndex()}">
                                            <td colspan="5">${iterator.getDescricao()}</td>
                                        </tr>
                                        <tr class="tr-none" data-index="${iterador.getIndex()}"><td></td></tr>
                                    </c:forEach>
                                    <c:if test="${orcamento.getModelos().size() == 0}">
                                        <tr class="js-nenhum-modelo">
                                            <td colspan="5">Nenhum modelo selecionado!</td>
                                        </tr>
                                    </c:if>
                                </tbody>
                            </table>
                        </div>
                        <hr>
                        <div class="form-row">
                            <div class="col-md-4 mb-3">
                                <label for="preco_toral">Preço total itens: </label>
                                <div class="input-group mb-3">
                                    <div class="input-group-prepend">
                                        <span class="input-group-text text-success"><i class="fas fa-dollar-sign"></i></span>
                                    </div>
                                    <input type="text" class="form-control" disabled="disabled" value="R$ ${orcamento.getPrecoItensGeralString()}">
                                </div>
                            </div>
                            <div class="col-md-4 mb-3">
                                <label for="total_itens">Total itens:</label>
                                <input type="text" class="form-control" disabled="disabled" value="${orcamento.getModelos().size()}">
                            </div>
                            <div class="col-md-4 mb-3">
                                <label for="prev_entrega">Prev. entrega:</label>
                                <div class="input-group mb-3">
                                    <div class="input-group-prepend" data-toggle="tooltip" title="" data-original-title="Verifique a data de previsão de entrega!">
                                        <span class="input-group-text text-primary"><i class="fas fa-truck"></i></span>
                                    </div>
                                    <input type="text" data-toggle="datepicker" class="form-control <c:if test="${orcamento.getValidation().hasParam('prev_entrega')}">is-invalid</c:if>" id="prev_entrega" name="prev_entrega" placeholder="dd/mm/yyyy" value="${orcamento.getPrevEntregaString()}">
                                    <div class="invalid-feedback">
                                        ${orcamento.getValidation().getParamOr('prev_entrega', '')}
                                    </div>
                                </div>
                            </div>
                        </div>

                        <div class="form-row">
                            <div class="col-md-4 mb-3">
                                <label for="porcentagem_desconto">% max desconto: </label>
                                <div class="input-group mb-3">
                                    <div class="input-group-prepend">
                                        <span class="input-group-text"><i class="fas fa-percent"></i></span>
                                    </div>
                                    <input type="text" class="form-control" id="max_porcentagem" disabled="disabled" value="${orcamento.getPorcentagemMaximaSomaString()}">
                                </div>
                            </div>
                            <div class="col-md-4 mb-3">
                                <label for="desconto">% Desconto: </label>
                                <div class="input-group mb-3">
                                    <div class="input-group-prepend" data-toggle="tooltip" title="" data-original-title="Informe uma porcentagem de desconto entre o máximo aceitável!">
                                        <span class="input-group-text"><i class="fas fa-percent"></i></span>
                                    </div>
                                    <input type="number" min="0" max="100" step="0.1" class="form-control <c:if test="${orcamento.getValidation().hasParam('desconto')}">is-invalid</c:if>" id="desconto" name="desconto" placeholder="% desconto" value="${orcamento.getDesconto()}">
                                    <div class="invalid-feedback">
                                        ${orcamento.getValidation().getParamOr('desconto', '')}
                                    </div>
                                </div>
                            </div>
                            <div class="col-md-4 mb-3">
                                <label for="desconto">Mão de obra: </label>
                                <div class="input-group mb-3">
                                    <div class="input-group-prepend" data-toggle="tooltip" title="" data-original-title="Informe o valor da mão de obra!">
                                        <span class="input-group-text text-success"><i class="fas fa-dollar-sign"></i></span>
                                    </div>
                                    <input type="text" data-format="price" class="form-control <c:if test="${orcamento.getValidation().hasParam('mao_obra')}">is-invalid</c:if>" id="mao_obra" name="mao_obra" placeholder="R$ 100,00" value="${orcamento.getValorMaoObraString()}">
                                    <div class="invalid-feedback">
                                        ${orcamento.getValidation().getParamOr('mao_obra', '')}
                                    </div>
                                </div>
                            </div>
                        </div>
                        <div class="form-row"><div class="col-md-12 mb-3"><h3>Sub total: R$ <span class="js-sub-total">${orcamento.getPrecoSubTotalString()}</span></h3></div></div>
                        <div class="form-row">
                            <div class="col-md-12 mb-3">
                                <label for="descricao">Observação: </label>
                                <textarea class="form-control <c:if test="${modelo.getValidation().hasParam('observacao')}">is-invalid</c:if>" id="observacao" name="observacao" placeholder="Sobre o orçamento">${orcamento.getObservacao()}</textarea>
                            </div>
                        </div>
                    </fieldset>
                </div>
                <div class="card-footer bg-white">
                    <button type="button" class="btn btn-outline-secondary ja-back-orcamento" data-href="/orcamento/etapa2">Voltar</button>
                    <button type="submit" class="btn btn-default js-save-orcamento" data-status="1"><i class="fas fa-shopping-cart text-primary"></i> Criar pedido</button>
                    <button type="button" class="btn btn-primary js-save-orcamento" data-status="2"><i class="fas fa-shopping-cart"></i> Criar orçamento</button>
                    <a data-toggle="tooltip" data-html="true" class="p-2" title="" data-original-title="Click em 'Criar orçamento' para finalizar e criar o pedido. O status ficará pendente de aprovação. Click em 'Criar pedido' para finalizar e iniciar a produção dessa ordem de serviço.">
                        <i class="fas fa-info"></i>
                    </a>
                </div>
            </form>
        </div>
    </div>
</div>