<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ page contentType="text/html; charset=UTF-8" %>
<div class="content p-4">
   <h2>Listagem de lançamentos</h2>

    <div class="card-body p-0">
      <form class="form-inline justify-content-end p-3" method="POST" action="${source}lancamento">
          <label class="sr-only" for="inlineFormInputName2">Busca por material ou item</label>
          <input type="text" class="form-control col-3" id="inlineFormInputName2" name="busca" placeholder="Busca por material ou item" value="${filter.getParam('busca')}">
            &nbsp;
          <button type="button" class="btn btn-primary btn-icon btn-pill js-new-register" data-href="${source}lancamento/cadastro">
              <i class="fas fa-plus p-1"></i>
          </button>
      </form>
    </div>

    <div class="card-body p-0">
         <table class="table">
             <thead class="thead-dark">
             <tr>
                 <th scope="col">#</th>
                 <th scope="col">Decrição</th>
                 <th scope="col">Item/Material</th>
                 <th scope="col">Valor</th>
                 <th scope="col">&nbsp;</th>
             </tr>
             </thead>
             <tbody>
                 <c:if test="${paginator.getIterator().size() > 0}">
                     <c:forEach items="${paginator.getIterator()}" var="iterator1">
                         <tr>
                             <th scope="row">${iterator1.getId()}</th>
                             <td>${iterator1.getDescricao()}</td>
                             <td>
                                <c:if test="${iterator1.getPedidoItem().getId() > 0}">
                                    <a href="${source}pedido/visualizar/id/${iterator1.getPedidoItem().getPedidoId()}#item-${iterator1.getPedidoItem().getId()}" target="_BLANK">#${iterator1.getPedidoItem().getIdString()}</a>
                                </c:if>
                                <c:if test="${iterator1.getPedidoItem().getId() == 0}">
                                    ${iterator1.getMaterial().getDescricao()}
                                </c:if>
                            </td>
                             <td>R$ ${iterator1.getPrecoString()}</td>
                             <td class=" actions align-rigth" style="">
                                    <a href="${source}lancamento/editar/id/${iterator1.getId()}" class="btn btn-icon btn-pill btn-primary" data-toggle="tooltip" title="" data-original-title="Edit"><i class="fa fa-fw fa-edit"></i></a>
                                    <a href="javascript:void(0);" class="btn btn-icon btn-pill btn-danger" data-toggle="tooltip" title="" data-original-title="Inativar"><i class="fa fa-fw fa-trash"></i></a>
                             </td>
                         </tr>
                     </c:forEach>
                 </c:if>
                 <c:if test="${paginator.getIterator().size() == 0}">
                     <tr>
                         <td colspan="5">Nenhum lançamento cadastrado!</td>
                     </tr>
                </c:if>
             </tbody>
         </table>
         <jsp:include page="/WEB-INF/view/partial/paginator.jsp"/>
     </div>
 </div>