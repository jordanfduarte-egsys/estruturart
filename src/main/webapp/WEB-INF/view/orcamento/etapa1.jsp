<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ page contentType="text/html; charset=UTF-8" %>
<script src="${source}js/module/orcamento/commun-orcamento.js"></script>
<link rel="stylesheet" href="${source}css/module/orcamento/orcamento.css">

<div class="content p-4">
    <h2>Orçamento - etapa 1</h2>
    <div class="card mb-4">
        <div class="card-body">
            <form action="${source}orcamento/etapa1" method="POST" autocomplete="off" id="etapa1">
                <input name="usuario_id" value="${orcamento.getUsuario().getId()}" id="usuario_id" type="hidden"/>
                <div class="form-row">
                    <fieldset class="col-md-12 mb-3 border">
                        <legend style="width: 214px;">Informações do cliente</legend>
                        <div class="form-row">
                            <div class="col-md-3 mb-3">
                                <label for="cpf_cnpj">Cpf/Cnpj *</label>
                                <input type="text" maxlength="14" data-format="cpf_or_cnpj" class="form-control <c:if test="${orcamento.getUsuario().getValidation().hasParam('cpf_cnpj')}">is-invalid</c:if>" id="cpf_cnpj" name="cpf_cnpj" placeholder="CPF/CNPJ" value="${orcamento.getUsuario().getCpfCnpj()}">
                                <div class="invalid-feedback">
                                    ${orcamento.getUsuario().getValidation().getParamOr('cpf_cnpj', '')}
                                </div>
                            </div>
                            <div class="col-md-1 mb-3">
                                <label for="cpf_cnpj">&nbsp;</label><br/>
                                <button type="button" class="btn btn-primary btn-icon js-find-cpf-cnpj">
                                    <i class="fa fa-user"></i>
                                </button>
                            </div>
                            <div class="col-md-4 mb-3">
                                <label for="nome_completo">
                                    <c:if test="${orcamento.getUsuario().getTipoPessoa().equals('1')}">
                                        Nome completo *
                                    </c:if>
                                    <c:if test="${orcamento.getUsuario().getTipoPessoa().equals('2')}">
                                        Razão social *
                                    </c:if>
                                </label>
                                <input type="text" class="form-control <c:if test="${orcamento.getUsuario().getValidation().hasParam('nome')}">is-invalid</c:if>" <c:if test="${orcamento.getUsuario().getId() > 0}">readonly</c:if> id="nome_completo" name="nome_completo" placeholder="<c:if test="${orcamento.getUsuario().getTipoPessoa().equals('1')}">Nome completo</c:if><c:if test="${orcamento.getUsuario().getTipoPessoa().equals('2')}">Empresa LTDA</c:if>" value="${orcamento.getUsuario().getNome()}">
                                <div class="invalid-feedback">
                                    ${orcamento.getUsuario().getValidation().getParamOr('nome', '')}
                                </div>
                            </div>
                            <div class="col-md-4 mb-3">
                                <label for="rg_inscricao_estadual">Rg/Insrição estadual *</label>
                                <input type="text" maxlength="11" class="form-control <c:if test="${orcamento.getUsuario().getValidation().hasParam('rg_inscricao_estadual')}">is-invalid</c:if>" <c:if test="${orcamento.getUsuario().getId() > 0}">readonly</c:if> id="rg_inscricao_estadual" name="rg_inscricao_estadual" placeholder="RG/Inscrição estadual" value="${orcamento.getUsuario().getRgIncricaoEstadual()}">
                                <div class="invalid-feedback">
                                    ${orcamento.getUsuario().getValidation().getParamOr('rg_inscricao_estadual', '')}
                                </div>
                            </div>
                        </div>
                        <div class="form-row">
                            <div class="col-md-4 mb-3">
                                <label for="email">Email *</label>
                                <div class="input-group">
                                    <div class="input-group-prepend">
                                        <span class="input-group-text" id="emailIcon">@</span>
                                    </div>
                                    <input type="text" class="form-control <c:if test="${orcamento.getUsuario().getValidation().hasParam('email')}">is-invalid</c:if>" <c:if test="${orcamento.getUsuario().getId() > 0}">readonly</c:if> id="email" name="email" placeholder="nome@email.com" value="${orcamento.getUsuario().getEmail()}">
                                    <div class="invalid-feedback">
                                        ${orcamento.getUsuario().getValidation().getParamOr('email', '')}
                                    </div>
                                </div>
                            </div>
                            <div class="col-md-4 mb-3">
                                <label for="telefone">Celular *</label>
                                <div class="input-group">
                                    <div class="input-group-prepend">
                                        <span class="input-group-text" id="telefoneIcon"><i class="fas fa-phone"></i></span>
                                    </div>
                                    <input type="text" maxlength="16" data-format="telefone" class="form-control <c:if test="${orcamento.getUsuario().getValidation().hasParam('telefone')}">is-invalid</c:if>" <c:if test="${orcamento.getUsuario().getId() > 0}">readonly</c:if> id="telefone" name="telefone" placeholder="(000) 00000-0000" value="${orcamento.getUsuario().getTelefone()}">
                                    <div class="invalid-feedback">
                                        ${orcamento.getUsuario().getValidation().getParamOr('telefone', '')}
                                    </div>
                                </div>
                            </div>
                        </div>
                    </fieldset>
                </div>
                <div class="form-row">
                    <fieldset class="col-md-12 mb-3 border">
                        <legend style="width: 245px;">Informações de instalação</legend>
                        <div class="form-row">
                            <div class="col-md-4 mb-3">
                                <label for="cep">Cep *</label>
                                <input data-format="cep" type="text" autocomplete="off" class="form-control <c:if test="${orcamento.getEndereco().getValidation().hasParam('cep')}">is-invalid</c:if>" id="cep" name="cep" placeholder="CEP" value="${orcamento.getEndereco().getCep()}">
                                <div class="invalid-feedback">
                                    ${orcamento.getEndereco().getValidation().getParamOr('cep', '')}
                                </div>
                            </div>
                            <div class="col-md-4 mb-3">
                                <label for="logradouro">Logradouro *</label>
                                <input type="text" class="form-control <c:if test="${orcamento.getEndereco().getValidation().hasParam('logradouro')}">is-invalid</c:if>" id="logradouro" name="logradouro" placeholder="Endereço de entrega" value="${orcamento.getEndereco().getLogradouro()}">
                                <div class="invalid-feedback">
                                    ${orcamento.getEndereco().getValidation().getParamOr('logradouro', '')}
                                </div>
                            </div>
                            <div class="col-md-4 mb-3">
                                <label for="numero">Número *</label>
                                <input type="text" class="form-control <c:if test="${orcamento.getEndereco().getValidation().hasParam('numero')}">is-invalid</c:if>" id="numero" name="numero" placeholder="Número da casa" value="${orcamento.getEndereco().getNumero()}">
                                <div class="invalid-feedback">
                                    ${orcamento.getEndereco().getValidation().getParamOr('numero', '')}
                                </div>
                            </div>
                        </div>
                        <div class="form-row">
                            <div class="col-md-4 mb-3">
                                <label for="estado_id">Estado *</label>
                                <select name="estado_id" id="estado_id" class="form-control <c:if test="${orcamento.getEndereco().getValidation().hasParam('estado_id')}">is-invalid</c:if>">
                                    <option value="0">Selecione!</option>
                                    <c:forEach items="${estados}" var="iterator">
                                        <option value="${iterator.getId()}" <c:if test="${orcamento.getEndereco().getEstadoId() == iterator.getId()}">selected</c:if>>${iterator.getNome()}</option>
                                    </c:forEach>
                                </select>
                                <div class="invalid-feedback">
                                    ${orcamento.getEndereco().getValidation().getParamOr('estado_id', '')}
                                </div>
                            </div>
                            <div class="col-md-4 mb-3">
                                <label for="cidade_id">Cidade *</label>
                                <select name="cidade_id" id="cidade_id" class="form-control <c:if test="${orcamento.getEndereco().getValidation().hasParam('cidade_id')}">is-invalid</c:if>">
                                    <option value="0">Selecione!</option>
                                    <c:forEach items="${cidades}" var="iterator">
                                        <option value="${iterator.getId()}" <c:if test="${orcamento.getEndereco().getCidadeId() == iterator.getId()}">selected</c:if>>${iterator.getNome()}</option>
                                    </c:forEach>
                                </select>
                                <div class="invalid-feedback">
                                    ${orcamento.getEndereco().getValidation().getParamOr('cidade_id', '')}
                                </div>
                            </div>
                            <div class="col-md-4 mb-3">
                                <label for="bairro">Bairro *</label>
                                <input type="text" class="form-control <c:if test="${orcamento.getEndereco().getValidation().hasParam('bairro')}">is-invalid</c:if>" id="bairro" name="bairro" placeholder="Bairro" value="${orcamento.getEndereco().getBairro()}">
                                <div class="invalid-feedback">
                                    ${orcamento.getEndereco().getValidation().getParamOr('bairro', '')}
                                </div>
                            </div>
                        </div>
                    </fieldset>
                </div>
                <div class="card-footer bg-white">
                    <button type="submit" class="btn btn-primary">Avançar</button>
                </div>
            </form>
        </div>
    </div>
 </div>

<script type="text/x-handlebars-template" id="row-cidade">
    <option value="{{id}}">{{nome}}</option>
</script>