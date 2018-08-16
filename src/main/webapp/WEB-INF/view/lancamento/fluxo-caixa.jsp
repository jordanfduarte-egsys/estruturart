<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ page contentType="text/html; charset=UTF-8" %>
<div class="content p-4">
   <h2>Fluxo de caixa</h2>

    <div class="card-body p-0">
      <form class="form justify-content-end pb-3" method="POST" action="${source}lancamento/fluxo-caixa">
            <div class="form-row p-0 col-md-8">
                <div class="col-md-4 mb-3">
                    <label class="sr-only" for="inlineFormInputName2">De</label>
                    <input type="text" data-toggle="datepicker" class="form-control" id="inlineFormInputName2" name="data_ini" placeholder="Data inicial dd/mm/yyyy" value="${dataIni}">
                </div>
                <div class="col-md-4 mb-3">
                    <label class="sr-only" for="inlineFormInputName3">Até</label>
                    <input type="text" data-toggle="datepicker" class="form-control" id="inlineFormInputName3" name="data_fim" placeholder="Data Final dd/mm/yyyy" value="${dataFim}">
                </div>
                <div class="col-md-4 mb-3">
                    <button type="submit" class="btn btn-primary btn-icon">
                        <i class="fas fa-search p-1"></i>
                    </button>
                    <button type="button" class="btn btn-secondary btn-icon js-print-table" data-href="lancamento/fluxo-caixa-impressao">
                        <i class="fas fa-print p-1"></i>&nbsp; Exportar
                    </button>
                </div>
            </div>
      </form>
    </div>

    <div class="card-body p-0">
        <div class="form-row">
            <div class="col-md-12 mb-3">
                <ul class="list-group pull-left">
                    <li class="list-group-item mr-2">Total de gastos com a empresa: R$ ${totais.getSomaEmpresaString()}</li>
                </ul>
                <ul class="list-group pull-left">
                    <li class="list-group-item mr-2">Total de vendas de pedidos: R$ ${totais.getLucroEmpresaString()}</li>
                </ul>
                <ul class="list-group pull-left">
                    <li class="list-group-item mr-2">Lucro no periodo: R$ ${totais.getDiferencaString()}</li>
                </ul>
            </div>
        </div>
         <table class="table table-bordered">
             <thead class="thead-dark">
             <tr>
                 <th scope="col">Pedido</th>
                 <th scope="col">Material</th>
                 <th scope="col">Descrição</th>
                 <th scope="col">Valor</th>
                 <th scope="col">Data pedido</th>
                 <th scope="col">Data instalação</th>
             </tr>
             </thead>
             <tbody>
                 <c:if test="${lancamentos.size() > 0}">
                     <c:forEach items="${lancamentos}" var="iterator1">
                         <tr>
                            <th scope="row">
                                <c:if test="${iterator1.getPedidoItem().getId() > 0}">
                                   <a href="${source}pedido/visualizar/id/${iterator1.getPedidoItem().getPedidoId()}#item-${iterator1.getPedidoItem().getId()}" target="_BLANK">#${iterator1.getPedidoItem().getIdString()}</a>
                                </c:if>
                                <c:if test="${iterator1.getPedidoItem().getId() == 0}">-</c:if>
                            </th>
                            <td>
                                <c:if test="${iterator1.getPedidoItem().getId() == 0}">
                                    ${iterator1.getMaterial().getDescricao()}
                                </c:if>
                            </td>
                            <td>
                                ${iterator1.getDescricao()}
                            </td>
                            <td>R$ ${iterator1.getPrecoString()}</td>
                            <td>
                                <c:if test="${iterator1.getPedidoItem().getId() > 0}">
                                    ${iterator1.getPedidoItem().getPedido().getDataInclusaoString()}
                                </c:if>
                                <c:if test="${iterator1.getPedidoItem().getId() == 0}">-</c:if>
                            </td>
                            <td>
                                <c:if test="${iterator1.getPedidoItem().getId() > 0}">
                                    ${iterator1.getPedidoItem().getPedido().getDataPrevisaoInstalacaoString()}
                                </c:if>
                                <c:if test="${iterator1.getPedidoItem().getId() == 0}">-</c:if>
                            </td>
                         </tr>
                     </c:forEach>
                 </c:if>
                 <c:if test="${lancamentos.size() == 0}">
                     <tr>
                         <td colspan="5">Nenhum resultado encontrado com esse filtro!</td>
                     </tr>
                </c:if>
             </tbody>
         </table>
     </div>
 </div>
 <iframe id="printf" style="width: 0;height: 0;border: none;"></iframe>