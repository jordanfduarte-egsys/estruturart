<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ page contentType="text/html; charset=UTF-8" %>
<div class="content p-4">
   <h2>Listagem de materiais</h2>

    <div class="card-body p-0">
      <form class="form-inline justify-content-end p-3" method="POST" action="${source}material">
          <label class="sr-only" for="inlineFormInputName2">Busca por descrição</label>
          <input type="text" class="form-control mr-sm-2" id="inlineFormInputName2" name="descricao" placeholder="Busca por descrição" value="${filter.getParam('descricao')}">

          <button type="button" class="btn btn-primary btn-icon btn-pill js-new-register" data-href="${source}material/cadastro">
              <i class="fas fa-plus p-1"></i>
          </button>
      </form>
    </div>

    <div class="card-body p-0">
         <table class="table">
             <thead class="thead-dark">
             <tr>
                 <th scope="col">#</th>
                 <th scope="col">Descrição</th>
                 <th scope="col">Qtd.</th>
                 <th scope="col">Fornecedor</th>
                 <th scope="col">Preço</th>
                 <th scope="col">Status</th>
                 <th scope="col">&nbsp;</th>
             </tr>
             </thead>
             <tbody>
                 <c:if test="${paginator.getIterator().size() > 0}">
                     <c:forEach items="${paginator.getIterator()}" var="iterator">
                         <tr>
                             <th scope="row">${iterator.getId()}</th>
                             <td>${iterator.getDescricao()}</td>
                             <td>${iterator.getQuantidade()} ${iterator.getUnidadeMedida().getNome()}</td>
                             <td>${iterator.getFornecedor().getNome()}</td>
                             <td>R$ ${iterator.getPrecoBR()}</td>
                             <td>${iterator.getStatusMaterial().getDescricao()}</td>
                             <td class=" actions align-rigth" style="">
                                    <a href="${source}material/editar/id/${iterator.getId()}" class="btn btn-icon btn-pill btn-primary" data-toggle="tooltip" title="" data-original-title="Edit"><i class="fa fa-fw fa-edit"></i></a>
                                    <a href="javascript:void(0);" class="btn btn-icon btn-pill btn-danger" data-toggle="tooltip" title="" data-original-title="Inativar"><i class="fa fa-fw fa-trash"></i></a>
                             </td>
                         </tr>
                     </c:forEach>
                 </c:if>
                 <c:if test="${paginator.getIterator().size() == 0}">
                     <tr>
                         <td colspan="7">Nenhum material cadastrado!</td>
                     </tr>
                </c:if>
             </tbody>
         </table>
         <jsp:include page="/WEB-INF/view/partial/paginator.jsp"/>
     </div>
 </div>