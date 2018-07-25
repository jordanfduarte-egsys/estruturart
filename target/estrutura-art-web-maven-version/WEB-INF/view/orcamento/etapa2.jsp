<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ page contentType="text/html; charset=UTF-8" %>
<script src="${source}js/module/orcamento/commun-orcamento.js"></script>
<link rel="stylesheet" href="${source}css/module/orcamento/orcamento.css">

<div class="content p-4">
    <h2>Orçamento - etapa 2</h2>
    <div class="card mb-4">
        <div class="card-body">
            <form action="${source}orcamento/etapa2" method="POST" id="form">
                <div class="form-row">
                    <fieldset class="col-md-8 mb-3 border">
                        <legend style="width: 91px;">Modelos</legend>
                        <input type="text" class="form-control" id="busca" autocomplete="off" placeholder="Busque um modelo para montar o orçamento e tecle ENTER">
                        <input id="filtrado" type="hidden"/>
                        <div class="table-modelos table-error form-control">
                            <table class="table table-striped js-row-modelos">
                                <tbody>
                                    <tr class="js-init-modelo">
                                        <td colspan="5">Faça um filtro para encontrar os modelos!</td>
                                    </tr>
                                </tbody>
                            </table>
                        </div>
                    </fieldset>
                </div>
                <div class="form-row">
                    <fieldset class="col-md-8 mb-3 border">
                        <legend style="width: 173px;">Listagem de itens</legend>
                        <div class="table-modelos-add table-error form-control <c:if test="${orcamento.getValidation().hasParam('modelos')}">is-invalid</c:if>">
                            <table class="table table-striped js-row-material">
                                <tbody>
                                    <c:forEach items="${orcamento.getModelos()}" var="iterator" varStatus="loop">
                                        <tr data-index="${iterator.getIndex()}" class="tr-one" data-altura-nova="${iterator.getAlturaNova()}"
                                         data-largura-nova="${iterator.getLarguraNova()}" data-preco-pintura="${iterator.getPrecoPintura()}"
                                         data-altura="${iterator.getAlturaPadrao()}" data-largura="${iterator.getLarguraPadrao()}"
                                         data-preco-inicial="${iterator.getPrecoTotalQuantidadeString()}">
                                            <td rowspan="2" class="td-nome-item">
                                                ${loop.index + 1} °<br>Item</td><td>${iterator.getNome()}
                                                <input name="item[${iterator.getIndex()}]id" type="hidden" value="${iterator.getId()}">
                                                <input name="item[${iterator.getIndex()}]altura" type="hidden" value="${iterator.getAlturaNova()}">
                                                <input name="item[${iterator.getIndex()}]largura" type="hidden" value="${iterator.getLarguraNova()}">
                                            </td>
                                            <td class="td-btn-added-price js-price-change"><b>R$ ${iterator.getPrecoItemTotalString()}</b></td>
                                            <td class="td-pintura">
                                                <input name="item[${iterator.getIndex()}]isPintura" class="js-pintura-item" type="checkbox" value="1" id="pintura-${iterator.getIndex()}" data-toggle="tooltip" title="Preço pintura: R$ ${iterator.getPrecoPinturaString()}" <c:if test="${iterator.getIsPintura()}">checked</c:if>/>
                                                <label for="pintura-${iterator.getIndex()}">&nbsp;Pintura</label>
                                            </td>
                                            </td>
                                            <td class="td-qtd-item">
                                                <div class="input-group mb-3">
                                                    <div class="input-group-prepend">
                                                        <div class="input-group-text" id="basic-addon1">Qtd</div>
                                                    </div>
                                                    <input name="item[${iterator.getIndex()}]quantidade" min="1" max="200" type="number"  value="${iterator.getQuantidadeCompra()}" class="form-control">
                                                </div>
                                            </td>
                                            <td class="td-btn-added" data-id="${iterator.getId()}">
                                                <button type="button" class="btn btn-primary btn-icon btn-pill js-duplicar-item" data-toggle="tooltip" title="Duplicar">
                                                    <i class="fas fa-fw fa-clone"></i>
                                                </button>
                                                <button class="btn btn-danger btn-icon btn-pill js-remover-item" type="button" data-toggle="tooltip" title="Remover item">
                                                    <i class="fas fa-trash fa-fw"></i>
                                                </button>
                                            </td>
                                        </tr>
                                        <tr data-index="${iterator.getIndex()}" class="tr-two">
                                            <td colspan="6">${iterator.getDescricao()} - ${iterator.getDimensaoNova()}</td>
                                        </tr>
                                        <tr class="tr-none" data-index="${iterator.getIndex()}"><td></td></tr>
                                    </c:forEach>
                                    <c:if test="${orcamento.getModelos().size() == 0}">
                                        <tr class="js-nenhum-modelo">
                                            <td colspan="5">Nenhum modelo adicionado!</td>
                                        </tr>
                                    </c:if>
                                </tbody>
                            </table>
                        </div>
                    </fieldset>
                </div>
                <div class="card-footer bg-white">
                    <button type="button" class="btn btn-outline-secondary js-back-orcamento" data-href="/orcamento/etapa1">Voltar</button>
                    <button type="submit" class="btn btn-primary">Avançar</button>
                </div>
            </form>
        </div>
    </div>
 </div>

<script type="text/x-handlebars-template" id="row-item">
    <tr style="border: 1px solid #CCC;" class="tr-line-one">
        <td rowspan="2">
            <img class="card-img-top" data-image="show" src="${source}{{imagemSource}}" alt="Card image cap" data-toggle="tooltip" title="Visualizar imagem">
        </td>
        <td>{{nome}}</td>
        <td>
            <div class="col1">
                <div class="input-group">
                    <div class="input-group-prepend">
                        <span class="input-group-text" id="inputGroupPrepend3">Largura</span>
                    </div>
                    <input data-largura="{{larguraPadrao}}" class="js-largura form-control col-md-6" data-format="float2" type="text" value="{{larguraPadrao toFloatBr}}">
                </div>
            </div>
            <div class="col2">
                <div class="input-group">
                    <div class="input-group-prepend">
                        <span class="input-group-text" id="inputGroupPrepend3">Altura</span>
                    </div>
                    <input data-altura="{{alturaPadrao}}" class="js-altura form-control col-md-6" data-format="float2" type="text" value="{{alturaPadrao toFloatBr}}">
                </div>
            </div>
        </td>
        <td rowspan="2" class="tr-line-two">
            <button type="button" class="btn btn-danger btn-icon btn-pill js-add-modelos" data-id="{{id}}" data-toggle="tooltip" title="Adicionar item ao carrinho">
                <i class="fas fa-cart-plus fa-fw"></i>
            </button>
        </td>
    </tr>
    <tr class="tr-line-tree">
        <td class="preco-produto" data-preco-inicial="{{precoTotalQtdStr}}">R$ {{precoTotalQtdStr numberToReal}}</td>
        <td colspan="2">{{descricao}}</td>
    </tr>
</script>

<script type="text/x-handlebars-template" id="row-item-add">
    <tr data-index="{{index}}" class="tr-one" data-altura-nova="{{alturaPadraoNova}}" data-largura-nova="{{larguraPadraoNova}}" data-preco-pintura="{{precoPintura}}" data-altura="{{alturaPadrao}}" data-largura="{{larguraPadrao}}" data-preco-inicial="{{precoTotalQtdStr}}">
        <td rowspan="2" class="td-nome-item">
            {{indexFront}} °<br>Item</td><td>{{nome}}
            <input name="item[{{index}}]id" type="hidden" value="{{id}}">
            <input name="item[{{index}}]altura" type="hidden" value="{{alturaPadraoNova}}">
            <input name="item[{{index}}]largura" type="hidden" value="{{larguraPadraoNova}}">
        </td>
        <td class="td-btn-added-price js-price-change"><b>R$ {{precoTotal}}</b></td>
        <td class="td-pintura">
            <input name="item[{{index}}]isPintura" class="js-pintura-item" type="checkbox" value="1" id="pintura-{{index}}" data-toggle="tooltip" title="Preço pintura: R$ {{precoPintura numberToReal}}">
            <label for="pintura-{{index}}">&nbsp;Pintura</label>
        </td>
        </td>
        <td class="td-qtd-item">
            <div class="input-group mb-3">
                <div class="input-group-prepend">
                    <div class="input-group-text" id="basic-addon1">Qtd</div>
                </div>
                <input name="item[{{index}}]quantidade" min="1" max="200" type="number" value="{{quantidadeCompra}}" class="form-control">
            </div>
        </td>
        <td class="td-btn-added" data-id="{{id}}">
            <button type="button" class="btn btn-primary btn-icon btn-pill js-duplicar-item" data-toggle="tooltip" title="Duplicar">
                <i class="fas fa-fw fa-clone"></i>
            </button>
            <button class="btn btn-danger btn-icon btn-pill js-remover-item" type="button" data-toggle="tooltip" title="Remover item">
                <i class="fas fa-trash fa-fw"></i>
            </button>
        </td>
    </tr>
    <tr data-index="{{index}}" class="tr-two">
        <td colspan="6">{{descricao}} - {{dimensao}}mm</td>
    </tr>
    <tr class="tr-none" data-index="{{index}}"><td></td></tr>
</script>