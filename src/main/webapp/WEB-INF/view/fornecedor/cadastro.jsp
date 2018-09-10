<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ page contentType="text/html; charset=UTF-8" %>
<div class="content p-4">
   <c:choose>
        <c:when test="${fornecedor.getId() != 0}">
            <h2>Edição de fornecedor</h2>
        </c:when>
        <c:otherwise>
            <h2>Cadastro de fornecedor</h2>
        </c:otherwise>
    </c:choose>
    <div class="card mb-4">
        <div class="card-body">
            <form action="${source}fornecedor/<c:if test="${fornecedor.getId() != 0}">editar/id/${fornecedor.getId()}</c:if><c:if test="${fornecedor.getId() == 0}">cadastro</c:if>" method="POST">
                <div class="form-row">
                    <div class="col-md-4 mb-3">
                        <label for="nome">Nome</label>
                        <input type="text" class="form-control <c:if test="${fornecedor.getValidation().hasParam('nome')}">is-invalid</c:if>" id="nome" name="nome" placeholder="Fornecedor Ferro & Alumнnio" value="${fornecedor.getNome()}">
                        <div class="invalid-feedback">
                            ${fornecedor.getValidation().getParamOr('nome', '')}
                        </div>
                    </div>
                    <div class="col-md-4 mb-3">
                        <label for="telefone">Telefone</label>
                        <input type="text" data-format="telefone" class="form-control <c:if test="${fornecedor.getValidation().hasParam('telefone')}">is-invalid</c:if>" id="telefone" name="telefone" placeholder="(000) 00000-0000" value="${fornecedor.getTelefone()}">
                    </div>
               </div>

                <div class="card-footer bg-white">
                    <button type="button" class="btn btn-outline-secondary js-back">Voltar</button>
                    <button type="submit" class="btn btn-primary">Salvar</button>
                </div>
            </form>
        </div>
    </div>
 </div>