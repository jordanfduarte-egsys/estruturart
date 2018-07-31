<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ page contentType="text/html; charset=UTF-8" %>
<link rel="stylesheet" href="${source}css/module/pedido/index.css">
<div class="content p-4">
   <h2>Pedidos</h2>

    <div class="card-body p-0">
      <form class="form-inline justify-content-end pb-3 js-form-simples" method="POST" action="${source}pedido">
            <a class="nav-link active js-busca-avancada" href="javascript:void(0);">busca avançada</a>
            <label class="sr-only" for="inlineFormInputName2">Pedido</label>
            <input type="text" class="form-control col-md-4" id="inlineFormInputName2" name="id" placeholder="Busca por número de pedido" value="${filter.getParam('id')}">
      </form>
    </div>

    <form class="card mb-4 pb-3 js-toggle-avancada hide <c:if test="${isFiltroAvancado}">show</c:if> js-form-avancado" method="POST"  action="${source}pedido">
        <div class="bg-light p-1 mb-3">
            <h3>Busca avançada</h3>
        </div>
        <div class="form-row">
            <div class="col-md-3 mb-3">
                <label for="inlineFormInputName3">Nome</label>
                <input type="text" class="form-control mr-sm-2" id="inlineFormInputName3" name="nome" placeholder="Nome ou descrição do pedido" value="${filter.getParam('titulo')}">
            </div>
            <div class="col-md-3 mb-3">
                <label for="status_id">Status *</label>
                <select name="status_id" id="status_id" class="form-control">
                    <option value="0">Selecione!</option>
                    <c:forEach items="${status}" var="iterator">
                        <option value="${iterator.getId()}" <c:if test="${filter.getParam('status') == iterator.getId()}">selected</c:if>>${iterator.getNome()}</option>
                    </c:forEach>
                </select>
            </div>

            <div class="col-md-3 mb-3">
                <label for="inlineFormInputName4">Cpf ou destinatário</label>
                <input type="text" class="form-control mr-sm-2" id="inlineFormInputName4" name="cep_or_destinatario" placeholder="Cpf ou destinatário" value="${filter.getParam('cep_or_destinatario')}">
            </div>
        </div>
        <div class="form-row">
            <div class="col-md-3 mb-3">
                <label for="inlineFormInputName5">Apartir de</label>
                <input type="text" class="form-control mr-sm-2" id="inlineFormInputName5" name="data_ini" placeholder="dd/MM/yyyy" value="${filter.getParam('data_ini')}">
            </div>

            <div class="col-md-3 mb-3">                
            </div>
            <div class="col-md-3 mb-3 buttons-filtro">
                <button type="button" class="btn btn-outline-secondary js-clear-form">Limpar</button>
                &nbsp;
                <button type="submit" class="btn btn-primary">Buscar</button>
            </div>
        </div>
   </form>

   <div class="card-body p-0 js-calendar" id="calendario"></div>
</div>
