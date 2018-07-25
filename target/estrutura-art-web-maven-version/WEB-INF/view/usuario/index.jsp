<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ page contentType="text/html; charset=UTF-8" %>
<div class="content p-4">
   <h2>Listagem de usuários</h2>

        <div class="card-body p-0">
          <form class="form-inline justify-content-end p-3" method="POST"  action="${source}usuario">
              <a class="nav-link active js-busca-avancada" href="javascript:void(0);">busca avançada</a>
              <label class="sr-only" for="inlineFormInputName2">Busca por nome</label>
              <input type="text" class="form-control mr-sm-2" id="inlineFormInputName2" name="busca_nome_rapido" placeholder="Busca por nome" value="${filter.getParam('busca_nome_rapido')}">

              <button type="button" class="btn btn-primary btn-icon btn-pill js-new-register" data-href="${source}usuario/cadastro">
                  <i class="fas fa-plus p-1"></i>
              </button>
          </form>
        </div>

        <form class="card mb-4 p-3 js-toggle-avancada hide <c:if test="${isFiltroAvancado}">show</c:if>" method="POST"  action="${source}usuario">
            <div class="bg-light p-1 mb-3">
                <h3>Busca avançada</h3>
            </div>
            <div class="form-inline">
               <label class="sr-only" for="inlineFormInputName3">Nome</label>
               <input type="text" class="form-control mr-sm-2" id="inlineFormInputName3" name="busca_nome" placeholder="Nome" value="${filter.getParam('busca_nome')}">

               <label class="sr-only" for="inlineFormInputName4">Cpf ou Cnpj</label>
               <input type="text" class="form-control mr-sm-2" id="inlineFormInputName4" name="cpf_cnpj" placeholder="Cpf ou Cnpj" value="${filter.getParam('cpf_cnpj')}">

               <label class="sr-only" for="inlineFormInputName5">Rg ou inscrição estadual</label>
               <input type="text" class="form-control mr-sm-2" id="inlineFormInputName5" name="rg_incricao_estadual" placeholder="Rg ou inscrição estadual" value="${filter.getParam('rg_incricao_estadual')}">

               <label class="sr-only" for="inlineFormInputName6">E-mail</label>
               <input type="text" class="form-control mr-sm-2" id="inlineFormInputName6" name="email" placeholder="E-mail" value="${filter.getParam('email')}">
              </div>
            <div class="form-inline justify-content-end p-2">
                <button type="submit" class="btn btn-primary">Buscar</button>
                &nbsp;
                <button type="button" class="btn btn-outline-secondary js-clear-form">Limpar</button>
            </div>
       </form>

    <div class="card-body p-0">
         <table class="table">
             <thead class="thead-dark">
             <tr>
                 <th scope="col">#</th>
                 <th scope="col">Nome</th>
                 <th scope="col">Perfil</th>
                 <th scope="col">Status</th>
                 <th scope="col">Tipo Pessoa</th>
                 <th scope="col">Inclusão</th>
                 <th scope="col">&nbsp;</th>
             </tr>
             </thead>
             <tbody>
                 <c:if test="${paginator.getIterator().size() > 0}">
                     <c:forEach items="${paginator.getIterator()}" var="iterator">
                         <tr>
                             <th scope="row">${iterator.getId()}</th>
                             <td>${iterator.getNome()}</td>
                             <td>${iterator.getPerfil().getDescricao()}</td>
                             <td>${iterator.getStatusUsuario().getDescricao()}</td>
                             <td>${iterator.getTipoPessoaNome()}</td>
                             <td>${iterator.getDataInclusao('dd/MM/yyyy')}</td>
                             <td class=" actions align-rigth" style="">
                                <a href="${source}usuario/editar/id/${iterator.getId()}" class="btn btn-icon btn-pill btn-primary" data-toggle="tooltip" data-original-title="Edit"><i class="fa fa-fw fa-edit"></i></a>
                                <a href="javascript:void(0);" class="btn btn-icon btn-pill btn-danger" data-toggle="modal" data-target="#modalStatus" data-original-title="Delete"><i class="fas fas fa-user-times"></i></a>
                             </td>
                         </tr>
                     </c:forEach>
                 </c:if>
                 <c:if test="${paginator.getIterator().size() == 0}">
                     <tr>
                         <td colspan="6">Nenhum usuário cadastrado!</td>
                     </tr>
                </c:if>
             </tbody>
         </table>
         <c:if test="${paginator.isPaginator()}">
             <jsp:include page="/WEB-INF/view/partial/paginator.jsp"/>
        </c:if>
     </div>
 </div>