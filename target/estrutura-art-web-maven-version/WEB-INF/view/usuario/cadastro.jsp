<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ page contentType="text/html; charset=UTF-8" %>
<div class="content p-4">
    <c:choose>
        <c:when test="${usuario.getId() != 0}">
            <h2>Edição de usuário</h2>
        </c:when>
        <c:otherwise>
            <h2>Cadastro de usuário</h2>
        </c:otherwise>
    </c:choose>

    <div class="card mb-4">
        <div class="card-body">
            <form action="${source}usuario/<c:if test="${usuario.getId() != 0}">editar/id/${usuario.getId()}</c:if><c:if test="${usuario.getId() == 0}">cadastro</c:if>" method="POST">
                <div class="row">
                    <legend class="col-form-label col-sm-2 pt-0">Tipo de pessoa</legend>
                    <div class="col-sm-10">
                        <c:forEach items="${usuario.getTiposPessoa()}" var="pessoa">
                            <div class="form-check">
                                <input class="form-check-input" type="radio" <c:if test="${pessoa.getIdString() == usuario.getTipoPessoa()}">checked="checked"</c:if> name="tipo_pessoa" id="tipo-${pessoa.getId()}" value="${pessoa.getId()}">
                                <label class="form-check-label" for="tipo-${pessoa.getId()}">
                                    ${pessoa.getNome()}
                                </label>
                            </div>
                        </c:forEach>
                    </div>
                </div>
                <div class="form-row">
                    <div class="col-md-4 mb-3">
                        <c:choose>
                            <c:when test="${usuario.getTipoPessoa() == '1'}">
                                <label for="cpfCnpj">Nome *</label>
                                <input type="text" class="form-control <c:if test="${usuario.getValidation().hasParam('nome')}">is-invalid</c:if>" id="nome" name="nome" placeholder="Jordan Duarte" value="${usuario.getNome()}">
                            </c:when>
                            <c:otherwise>
                                <label for="cpfCnpj">Razão Social *</label>
                                <input type="text" class="form-control <c:if test="${usuario.getValidation().hasParam('nome')}">is-invalid</c:if>" id="nome" name="nome" placeholder="Empresa S.A" value="${usuario.getNome()}">
                            </c:otherwise>
                        </c:choose>
                        <div class="invalid-feedback">
                            ${usuario.getValidation().getParamOr('nome', '')}
                        </div>
                    </div>
                    <div class="col-md-4 mb-3">
                        <c:choose>
                            <c:when test="${usuario.getTipoPessoa() == '1'}">
                                <label for="cpfCnpj">Cpf *</label>
                                <input type="text" class="form-control <c:if test="${usuario.getValidation().hasParam('cpf_cnpj')}">is-invalid</c:if>" id="cpfCnpj" name="cpf_cnpj" data-format="cpf" placeholder="000.000.000-99" value="${usuario.getCpfCnpj()}" >
                            </c:when>
                            <c:otherwise>
                                <label for="cpfCnpj">CNPJ *</label>
                                <input type="text" class="form-control <c:if test="${usuario.getValidation().hasParam('cpf_cnpj')}">is-invalid</c:if>" id="cpfCnpj" name="cpf_cnpj" data-format="cnpj" placeholder="00.000.000/0001-00" value="${usuario.getCpfCnpj()}" >
                            </c:otherwise>
                        </c:choose>
                        <div class="invalid-feedback">
                            ${usuario.getValidation().getParamOr('cpf_cnpj', '')}
                        </div>
                    </div>
               </div>
               <div class="form-row">
                    <div class="col-md-4 mb-3">
                        <label for="validationServerEmail">E-mail *</label>
                        <div class="input-group">
                            <div class="input-group-prepend">
                                <span class="input-group-text" id="emailIcon">@</span>
                            </div>
                            <input type="text" class="form-control <c:if test="${usuario.getValidation().hasParam('email')}">is-invalid</c:if>" id="validationServerEmail" placeholder="jordan@email.com" aria-describedby="emailIcon" name="email" value="${usuario.getEmail()}">
                            <div class="invalid-feedback">
                                ${usuario.getValidation().getParamOr('email', '')}
                            </div>
                        </div>
                    </div>
                    <div class="col-md-4 mb-3">
                        <c:choose>
                            <c:when test="${usuario.getTipoPessoa() == '1'}">
                                <label for="cpfCnpj">RG</label>
                                <input type="text" class="form-control" id="numeroDocumento" name="rg_inscricao_estadual" data-format="rg" placeholder="00000000-5" value="${usuario.getRgIncricaoEstadual()}">
                            </c:when>
                            <c:otherwise>
                                <label for="cpfCnpj">Inscrição Estadual</label>
                                <input type="text" class="form-control" id="numeroDocumento" name="rg_inscricao_estadual" data-format="inscricao_estadual" placeholder="000-00000-00" value="${usuario.getRgIncricaoEstadual()}">
                            </c:otherwise>
                        </c:choose>
                    </div>
                </div>
                <div class="form-row">
                    <div class="col-md-4 mb-3">
                        <label for="validationServerFone">Telefone *</label>
                        <div class="input-group">
                            <div class="input-group-prepend">
                                <span class="input-group-text" id="telefoneIcon"><i class="fas fa-phone"></i></span>
                            </div>
                            <input type="text" class="form-control <c:if test="${usuario.getValidation().hasParam('telefone')}">is-invalid</c:if>" id="validationServerFone" name="telefone" data-format="telefone" placeholder="(000) 99000-0000" aria-describedby="telefoneIcon" value="${usuario.getTelefone()}">
                            <div class="invalid-feedback">
                                ${usuario.getValidation().getParamOr('telefone', '')}
                            </div>
                        </div>
                    </div>
                </div>
                <div class="row">
                    <legend class="col-form-label col-sm-2 pt-0">Perfil</legend>
                    <div class="col-sm-10">
                        <c:forEach items="${usuario.getPerfis()}" var="perfil">
                            <div class="form-check">
                                <input class="form-check-input" type="radio" <c:if test="${perfil.getId() == usuario.getPerfil().getId() || (usuario.getPerfil().getId() == 0 && perfil.getId() == 1)}">checked="checked"</c:if> name="perfil_id" id="perfil-${perfil.getId()}" value="${perfil.getId()}">
                                <label class="form-check-label" for="perfil-${perfil.getId()}">
                                    ${perfil.getDescricao()}
                                </label>
                            </div>
                        </c:forEach>
                    </div>
                </div>
                <div class="form-row <c:if test="${usuario.getPerfil().getId() == 0 || usuario.getPerfil().getId() == 1}">hide</c:if>">
                    <div class="col-md-4 mb-3">
                        <label for="validationServerPass">Senha *</label>
                        <div class="input-group">
                            <div class="input-group-prepend">
                                <span class="input-group-text" id="passIcon"><i class="fas fa-unlock-alt"></i></span>
                            </div>
                            <input type="password" class="form-control  <c:if test="${usuario.getValidation().hasParam('senha')}">is-invalid</c:if>" id="validationServerPass" name="senha" placeholder="*********" aria-describedby="passIcon">
                            <a data-toggle="tooltip" data-html="true" class="p-2" title="" data-original-title="A senha deve conter no mнnimo seis caracteres alfanumйricos e no mínimo 1 e máximo 3 caracteres especiais, como exemplo {'@', '-', '.', '_', '&', '%', '$', '#', '!', '?'}">
                                <i class="fas fa-info"></i>
                            </a>
                            <div class="invalid-feedback">
                                ${usuario.getValidation().getParamOr('senha', '')}
                            </div>
                        </div>
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