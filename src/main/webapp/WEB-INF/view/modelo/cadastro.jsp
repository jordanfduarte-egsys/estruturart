<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ page contentType="text/html; charset=UTF-8" %>
<link rel="stylesheet" href="${source}css/module/modelo/cadastro.css">
<div class="content p-4">
   <c:choose>
        <c:when test="${modelo.getId() != 0}">
            <h2>Edição de modelo</h2>
        </c:when>
        <c:otherwise>
            <h2>Cadastro de modelo</h2>
        </c:otherwise>
    </c:choose>
    <div class="card mb-4">
        <div class="card-body">
            <form action="${source}modelo/<c:if test="${modelo.getId() != 0}">editar/id/${modelo.getId()}</c:if><c:if test="${modelo.getId() == 0}">cadastro</c:if>" method="POST" enctype="multipart/form-data">
                <div class="form-row">
                    <div class="col-md-5 mb-3">
                        <label for="nome">Nome *</label>
                        <input type="text" class="form-control <c:if test="${modelo.getValidation().hasParam('nome')}">is-invalid</c:if>" id="nome" name="nome" placeholder="Janela de correr" value="${modelo.getNome()}">
                        <div class="invalid-feedback">
                            ${modelo.getValidation().getParamOr('nome', '')}
                        </div>
                    </div>
                    <div class="col-md-5 mb-3">
                        <label for="status_modelo_id">Status</label>
                        <select name="status_modelo_id" id="status_modelo_id" class="form-control <c:if test="${modelo.getValidation().hasParam('status_modelo_id')}">is-invalid</c:if>">
                            <option value="0">Selecione!</option>
                            <c:forEach items="${statusModelo}" var="iterator">
                                <option value="${iterator.getId()}" <c:if test="${modelo.getStatusModeloId() == iterator.getId()}">selected</c:if>>${iterator.getNome()}</option>
                            </c:forEach>
                        </select>

                        <div class="invalid-feedback">
                            ${modelo.getValidation().getParamOr('status_modelo_id', '')}
                        </div>
                    </div>
                 </div>
                 <div class="form-row">
                    <div class="col-md-10 mb-3">
                        <label for="descricao">Descrição *</label>
                        <textarea class="form-control <c:if test="${modelo.getValidation().hasParam('descricao')}">is-invalid</c:if>" id="descricao" name="descricao" placeholder="Sobre esse modelo">${modelo.getDescricao()}</textarea>
                        <div class="invalid-feedback">
                            ${modelo.getValidation().getParamOr('descricao', '')}
                        </div>
                    </div>
                </div>

               <div class="form-row">
                   <fieldset class="col-md-10 mb-3 border">
                        <legend style="width: 91px;">Materiais</legend>
                        <input type="text" class="form-control" id="busca" name="busca" placeholder="Busque um material para montar o modelo e tecle ENTER">
                        <input id="filtrado" type="hidden"/>
                        <div class="table-materiais table-error form-control <c:if test="${modelo.getValidation().hasParam('materiais')}">is-invalid</c:if>">
                            <table class="table table-striped js-row-material">
                                <thead>
                                    <tr>
                                        <td>Material</td>
                                        <td>Fornecedor</td>
                                        <td>Quantidade</td>
                                        <td>Preço</td>
                                    </tr>
                                </thead>
                                <tbody>
                                   <c:forEach items="${modelo.getMateriais()}" var="iterator">
                                        <tr>
                                            <td>${iterator.getDescricao()}</td>
                                            <td>${iterator.getFornecedor().getNome()}</td>
                                            <td>${iterator.getQuantidade()} ${iterator.getUnidadeMedida().getNome()}</td>
                                            <td class="js-preco" data-preco="<c:if test="${iterator.getMateriaPrima() == 1}">${iterator.getPreco()}</c:if>">
                                                <c:if test="${iterator.getMateriaPrima() == 1}">
                                                    R$ ${iterator.getPrecoBR()}
                                                </c:if>
                                                <a href="javasxript:void(0);" class="js-remover"><i class="fas fa-trash text-right text-danger pull-right"></i></a>
                                                <input type="hidden" name="materiais[]" value="${iterator.getId()}" />
                                            </td>
                                        </tr>
                                    </c:forEach>
                                     <c:if test="${modelo.getMateriais().size() == 0}">
                                         <tr class="js-nenhum-material">
                                            <td colspan="4">Nenhum material selecionado!</td>
                                          </tr>
                                     </c:if>
                                </tbody>
                            </table>
                            <div class="invalid-feedback">
                                ${modelo.getValidation().getParamOr('materiais', '')}
                            </div>
                        </div>
                   </fieldset>
               </div>

               <div class="form-row">
                    <div class="col-md-2 mb-3">
                        <label for="largura_padrao">Largura *</label>
                        <input type="text" data-format="float" class="form-control <c:if test="${modelo.getValidation().hasParam('largura_padrao')}">is-invalid</c:if>" id="largura_padrao" name="largura_padrao" placeholder="99,00" value="${modelo.getLarguraPadraoString()}">
                        <div class="invalid-feedback">
                            ${modelo.getValidation().getParamOr('largura_padrao', '')}
                        </div>
                    </div>
                    <a data-toggle="tooltip" data-html="true" class="p-2 sobre-medida" title="" data-original-title="Informe as medidas de largura e altura em (mm)">
                         <i class="fas fa-info"></i>
                     </a>
                    <div class="col-md-2 mb-3">
                        <label for="altura_padrao">Altura *</label>
                        <input type="text" data-format="float" class="form-control <c:if test="${modelo.getValidation().hasParam('altura_padrao')}">is-invalid</c:if>" id="altura_padrao" name="altura_padrao" placeholder="99,00" value="${modelo.getAlturaPadraoString()}">
                        <div class="invalid-feedback">
                            ${modelo.getValidation().getParamOr('altura_padrao', '')}
                        </div>
                    </div>
                    <div class="col-md-2 mb-3">
                        <label for="preco_pintura">Preço pintura</label>
                        <div class="input-group mb-3">
                            <div class="input-group-prepend">
                                 <span class="input-group-text text-success"><i class="fas fa-dollar-sign"></i></span>
                            </div>
                            <input type="text" data-format="price" class="form-control <c:if test="${modelo.getValidation().hasParam('preco_pintura')}">is-invalid</c:if>" id="preco_pintura" name="preco_pintura" placeholder="R$ 50,00" value="${modelo.getPrecoPinturaString()}">
                            <div class="invalid-feedback">
                                ${modelo.getValidation().getParamOr('preco_pintura', '')}
                            </div>
                        </div>
                    </div>

                    <div class="col-md-2 mb-3">
                        <label for="nome">% Max. acrescimo *</label>
                        <div class="input-group mb-3">
                            <div class="input-group-prepend">
                                 <span class="input-group-text">%</span>
                            </div>
                            <input type="number" min="0" max="100" data-format="number" class="form-control <c:if test="${modelo.getValidation().hasParam('porcentagem_acrescimo')}">is-invalid</c:if>" id="porcentagem_acrescimo" name="porcentagem_acrescimo" placeholder="10%" value="${modelo.getPorcentagemAcrescimoString()}">
                            <div class="invalid-feedback">
                                ${modelo.getValidation().getParamOr('porcentagem_acrescimo', '')}
                            </div>
                        </div>
                    </div>
                    <div class="col-md-2 mb-3">
                        <label for="qtd_dias_producao">Dias produção *</label>
                        <div class="input-group mb-3">
                            <div class="input-group-prepend">
                                 <span class="input-group-text"><i class="fas fa-sun text-warning"></i></span>
                            </div>
                            <input type="number" min="0" max="100" data-format="number" class="form-control <c:if test="${modelo.getValidation().hasParam('qtd_dias_producao')}">is-invalid</c:if>" id="qtd_dias_producao" name="qtd_dias_producao" placeholder="10 dias" value="${modelo.getQtdDiasProducaoString()}">
                            <div class="invalid-feedback">
                                ${modelo.getValidation().getParamOr('qtd_dias_producao', '')}
                            </div>
                        </div>
                    </div>
               </div>

               <div class="form-row">
                   <fieldset class="col-md-4 mb-3 border">
                        <legend style="width: 91px;">Imagem</legend>

                        <div class="upload js-upload">
                            <img src="${source}${modelo.getImagemSource()}" class="js-image" data-image="show">
                            <i class="fas fa-check js-image-changed green fa-5x text-success hide"><br/><span>Selecionado</span></i>
                            <input type="file" name="imagem" id="imagem" accept=".png, .jpg, .bmp" class="">
                        </div>
                        <div class="upload-field table-error <c:if test="${modelo.getValidation().hasParam('imagem')}">is-invalid</c:if>">

                            <span>Formato aceito ${extensoes}</span>
                            <br>
                            <span>Dimensão do arquivo recomendado ${widthModelo}x${heigthModelo}px</span>
                            <div class="invalid-feedback">
                                ${modelo.getValidation().getParamOr('imagem', '')}
                            </div>
                        </div>
                   </fieldset>

                   <div class="col-md-6 mb-3 text-right js-resumo resumo">
                       <h4 class="text-left">Resumo</h4>
                       <ul>
                            <li>
                                <div class="js-qtd-material">
                                    <span>Qtd. material </span>
                                    <span><b>${modelo.getMateriais().size()}</b></span>
                                </div><br>
                            </li>
                            <li>
                                <div class="js-preco-materiais">
                                    <span>Preço materiais </span>
                                    <span>R$ <b>${modelo.getPrecoTotalMateriaisString()}</b></span>
                                </div><br>
                            </li>
                            <li>
                                <div class="js-preco-materiais-sem-desconto">
                                    <span>Preço materiais sem desconto </span>
                                    <span>R$ <b>${modelo.getPrecoTotalQuantidadeStringString()}</b></span>
                                </div><br>
                            </li>
                            <li>
                                <div class="js-calc-desc-max">
                                    <span>Materiais + Pintura com max. desconto</span>
                                    <span>R$ <b>${modelo.getPrecoComMaxDescontoString()}</b></span>
                                </div><br>
                            </li>
                            <li>
                               <div class="js-calc-total">
                                    <span>Materiais + Pintura sem desconto </span>
                                    <span>R$ <b>${modelo.getPrecoTotalString()}</b></span>
                                </div><br>
                            </li>
                       </ul>
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

 <script type="text/x-handlebars-template" id="row-material">
 <tr>
    <td>{{descricao}}</td>
    <td>{{fornecedor.nome}}</td>
    <td>{{quantidade}} {{unidadeMedida.nome}}</td>
    <td class="js-preco" data-preco="{{preco}}">
        {{#if preco > 0}}
            R$ {{precoBr}}
        {{/if}}
        <a href="javasxript:void(0);" class="js-remover"><i class="fas fa-trash text-right text-danger pull-right" ></i></a>
        <input type="hidden" name="materiais[]" value="{{id}}" />
    </td>
</tr>
</script>