<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ page contentType="text/html; charset=UTF-8" %>
<div class="content p-4">
   <h2>Listagem de modelos</h2>

    <div class="card-body p-0">
      <form class="form-inline justify-content-end p-3" method="POST" action="${source}modelo">
          <label class="sr-only" for="inlineFormInputName2">Busca por descrição</label>
          <input type="text" class="form-control mr-sm-2" id="inlineFormInputName2" name="nome" placeholder="Busca por nome" value="${filter.getParam('nome')}">

          <button type="button" class="btn btn-primary btn-icon btn-pill js-new-register" data-href="${source}modelo/cadastro">
              <i class="fas fa-plus p-1"></i>
          </button>
      </form>
    </div>

    <div class="card-body p-0">
         <table class="table">
             <thead class="thead-dark">
             <tr>
                 <th scope="col">#</th>
                 <th scope="col">Nome</th>
                 <th scope="col">Imagem</th>
                 <th scope="col">Dimensão</th>
                 <th scope="col">Preço</th>
                 <th scope="col">Produção</th>
                 <th scope="col">Status</th>
                 <th scope="col">&nbsp;</th>
             </tr>
             </thead>
             <tbody>
                 <c:if test="${paginator.getIterator().size() > 0}">
                     <c:forEach items="${paginator.getIterator()}" var="iterator">
                         <tr>
                             <th scope="row">${iterator.getId()}</th>
                             <td>${iterator.getNome()}</td>
                             <td><a href="javascript:void();" class="js-image-show"><img src="${source}${iterator.getImagemSource()}" data-image="show"/></a></td>
                             <td>${iterator.getDimensao()}</td>
                             <td>R$ ${iterator.getPrecoTotalSubQueryString()}</td>
                             <td>${iterator.getQtdDiasProducao()} dia(s)</td>
                             <td>${iterator.getStatusModelo().getNome()}</td>
                             <td class=" actions align-rigth">
                                <a href="${source}modelo/editar/id/${iterator.getId()}" class="btn btn-icon btn-pill btn-primary" data-toggle="tooltip" title="" data-original-title="Editar"><i class="fa fa-fw fa-edit"></i></a>
                                <a href="javascript:void(0);" class="btn btn-icon btn-pill btn-danger js-alterar-status" data-id="${iterator.getId()}" data-status="${iterator.getStatusModelo().getId()}" data-toggle="tooltip" title="Alterar status"><i class="fas fa-minus-circle"></i>&nbsp;</a>
                             </td>
                         </tr>
                     </c:forEach>
                 </c:if>
                 <c:if test="${paginator.getIterator().size() == 0}">
                     <tr>
                         <td colspan="7">Nenhum modelo cadastrado!</td>
                     </tr>
                </c:if>
             </tbody>
         </table>
         <jsp:include page="/WEB-INF/view/partial/paginator.jsp"/>
     </div>
 </div>

<div class="modal" tabindex="-1" role="dialog" id="modal-alterar-status">
<div class="modal-dialog" role="document">
    <div class="modal-content">
    <div class="modal-header">
        <h5 class="modal-title">Alterar status do modelo</h5>
        <button type="button" class="close" data-dismiss="modal" aria-label="Fechar">
        <span aria-hidden="true">&times;</span>
        </button>
    </div>
    <div class="modal-body">
        <p></p>
    </div>
    <div class="modal-footer">
        <button type="button" class="btn btn-secondary" data-dismiss="modal">Cancelar</button>
        <button type="button" class="btn btn-primary">Confirmar</button>
    </div>
    </div>
</div>
</div>