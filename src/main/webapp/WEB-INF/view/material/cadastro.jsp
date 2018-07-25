<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ page contentType="text/html; charset=UTF-8" %>
<div class="content p-4">
   <c:choose>
        <c:when test="${material.getId() != 0}">
            <h2>Edição de material</h2>
        </c:when>
        <c:otherwise>
            <h2>Cadastro de material</h2>
        </c:otherwise>
    </c:choose>
    <div class="card mb-4">
        <div class="card-body">
            <form action="${source}material/<c:if test="${material.getId() != 0}">editar/id/${material.getId()}</c:if><c:if test="${material.getId() == 0}">cadastro</c:if>" method="POST">
                <div class="form-row">
                    <div class="col-md-4 mb-3">
                        <label for="descricao">Descrição *</label>
                        <input type="text" class="form-control <c:if test="${material.getValidation().hasParam('descricao')}">is-invalid</c:if>" id="descricao" name="descricao" placeholder="Barra de alumнnio" value="${material.getDescricao()}">
                        <div class="invalid-feedback">
                            ${material.getValidation().getParamOr('descricao', '')}
                        </div>
                    </div>
                    <div class="col-md-4 mb-3">
                        <label for="fornecedor_id">Fornecedor *</label>
                        <select name="fornecedor_id" id="fornecedor_id" class="form-control <c:if test="${material.getValidation().hasParam('fornecedor_id')}">is-invalid</c:if>">
                            <option value="0">Selecione!</option>
                            <c:forEach items="${fornecedores}" var="iterator">
                                <option value="${iterator.getId()}" <c:if test="${material.getFornecedorId() == iterator.getId()}">selected</c:if>>${iterator.getNome()}</option>
                            </c:forEach>
                        </select>

                        <div class="invalid-feedback">
                            ${material.getValidation().getParamOr('fornecedor_id', '')}
                        </div>
                    </div>
               </div>

               <div class="form-row">
                    <div class="col-md-4 mb-3">
                        <label for="status_material_id">Status</label>
                        <select name="status_material_id" id="status_material_id" class="form-control <c:if test="${material.getValidation().hasParam('status_material_id')}">is-invalid</c:if>">
                            <option value="0">Selecione!</option>
                            <c:forEach items="${statusMaterial}" var="iterator">
                                <option value="${iterator.getId()}" <c:if test="${material.getStatusMaterialId() == iterator.getId()}">selected</c:if>>${iterator.getDescricao()}</option>
                            </c:forEach>
                        </select>

                        <div class="invalid-feedback">
                            ${material.getValidation().getParamOr('status_material_id', '')}
                        </div>
                    </div>

                    <div class="col-md-2 mb-3">
                        <label for="quantidade">Quantidade *</label>
                        <input type="number" min="1" data-format="number" class="js-qtd form-control <c:if test="${material.getValidation().hasParam('quantidade')}">is-invalid</c:if>" id="quantidade" name="quantidade" placeholder="1 unidade, 2 kg ..." value="${material.getQuantidade()}">
                        <div class="invalid-feedback">
                            ${material.getValidation().getParamOr('quantidade', '')}
                        </div>
                    </div>

                    <div class="col-md-2 mb-3">
                        <label for="unidade_medida_id">Unidade medida *</label>
                        <select name="unidade_medida_id" id="unidade_medida_id" class="form-control <c:if test="${material.getValidation().hasParam('unidade_medida_id')}">is-invalid</c:if>">
                            <option value="0">Selecione!</option>
                            <c:forEach items="${unidadeMedidas}" var="iterator">
                                <option value="${iterator.getId()}" <c:if test="${material.getUnidadeMedidaId() == iterator.getId()}">selected</c:if>>${iterator.getNome()}</option>
                            </c:forEach>
                        </select>

                        <div class="invalid-feedback">
                            ${material.getValidation().getParamOr('unidade_medida_id', '')}
                        </div>
                    </div>
               </div>

               <div class="form-row">
                   <div class="col-md-4">
                       <label for="preco">Preço *</label>
                        <div class="input-group mb-3">
                            <div class="input-group-prepend">
                                 <span class="input-group-text"><i class="fas fa-dollar-sign"></i></span>
                            </div>
                            <input type="text" data-format="price" class="form-control <c:if test="${material.getValidation().hasParam('preco')}">is-invalid</c:if>" id="preco" name="preco" placeholder="Preço do material" value="${material.getPrecoBR()}">
                            <div class="invalid-feedback">
                                ${material.getValidation().getParamOr('preco', '')}
                            </div>
                        </div>
                    </div>

                    <div class="col-md-4 mb-3" style="margin-top: 34px;margin-left: 24px;">

                        <input type="checkbox" class="form-check-input" <c:if test="${material.getMateriaPrima() == 1}">checked="checked"</c:if> id="materia_prima" name="materia_prima" value="1">
                        <input type="hidden" name="materia_prima" value="0"/>
                        <label for="materia_prima" class="form-check-label">Materia prima</label>
                        <a data-toggle="tooltip" data-html="true" class="p-2" title="" data-original-title="Colocar descrição para que isso serve">
                            <i class="fas fa-info"></i>
                        </a>
                    </div>
               </div>

               <div class="form-row form-group form-check mb-0">

               </div>

                <div class="card-footer bg-white">
                    <button type="button" class="btn btn-outline-secondary js-back">Voltar</button>
                    <button type="submit" class="btn btn-primary">Salvar</button>
                </div>
            </form>
        </div>
    </div>
 </div>