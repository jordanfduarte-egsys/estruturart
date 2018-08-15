<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ page contentType="text/html; charset=UTF-8" %>
<div class="content p-4">
   <c:choose>
        <c:when test="${lancamento.getId() != 0}">
            <h2>Edição de lançamento</h2>
        </c:when>
        <c:otherwise>
            <h2>Cadastro de lançamento</h2>
        </c:otherwise>
    </c:choose>
    <div class="card mb-4">
        <div class="card-body">
            <form action="${source}lancamento/<c:if test="${lancamento.getId() != 0}">editar/id/${lancamento.getId()}</c:if><c:if test="${lancamento.getId() == 0}">cadastro</c:if>" method="POST">
                <div class="form-row">
                    <div class="col-md-4 mb-3">
                        <label for="material_id">Material *</label>
                        <select <c:if test="${lancamento.getPedidoItensId() > 0}">disabled</c:if> name="material_id" id="material_id" class="form-control <c:if test="${lancamento.getValidation().hasParam('material_id')}">is-invalid</c:if>">
                            <option value="0">Selecione!</option>
                            <c:forEach items="${materiais}" var="iterator">
                                <option value="${iterator.getId()}" <c:if test="${lancamento.getMaterialId() == iterator.getId()}">selected</c:if>>${iterator.getDescricao()}</option>
                            </c:forEach>
                        </select>

                        <div class="invalid-feedback">
                            ${lancamento.getValidation().getParamOr('material_id', '')}
                        </div>
                    </div>

                    <div class="col-md-4 mb-3">
                        <label for="preco">Preço *</label>
                        <div class="input-group mb-3">
                            <div class="input-group-prepend">
                                <span class="input-group-text text-success"><i class="fas fa-dollar-sign"></i>&nbsp;-&nbsp;</span>
                            </div>
                            <input type="text" data-format="price" class="form-control <c:if test="${lancamento.getValidation().hasParam('preco')}">is-invalid</c:if>" id="preco" name="preco" placeholder="R$ 50,00" value="${lancamento.getPreco()}">
                            <div class="invalid-feedback">
                                ${lancamento.getValidation().getParamOr('preco', '')}
                            </div>
                        </div>
                    </div>
                </div>

                <div class="form-row">
                    <div class="col-md-8 mb-3">
                        <label for="descricao">Descrição</label>
                        <textarea class="form-control <c:if test="${lancamento.getValidation().hasParam('descricao')}">is-invalid</c:if>" id="descricao" name="descricao" placeholder="Sobre o lançamento">${lancamento.getDescricao()}</textarea>
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