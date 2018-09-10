<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<!DOCTYPE HTML>
<html lang="en">
    <head>
        <meta charset="utf-8">
        <meta name="viewport" content="width=device-width, initial-scale=1, shrink-to-fit=no">
        <meta http-equiv="Content-Type" content="text/html;charset=utf-8" />

        <link rel="stylesheet" href="${source}css/bootstrap.min.css">
        <link rel="stylesheet" href="${source}css/easy-autocomplete.css">
        <link rel="stylesheet" href="${source}css/fontawesome-all.min.css">
        <link rel="stylesheet" href="${source}css/bootadmin.min.css">
        <link rel="stylesheet" href="${source}css/common.css">
        <link rel="stylesheet" href="${source}css/module/pedido/imprimir-op.css">
        <title>Ordem de Produção #${pedido.getIdString()}</title>
    </head>
    <body class="bg-light" onload="window.print(); return false;">
        <div class="d-flex">
            <h1 class="p-5">Ordem de Produção</h1>
        </div>
        <div class="content">
            <div class="form-row">
                <ul>
                    <li>
                        <span class="pull-left">Pedido: #${pedido.getIdString()}</span>
                        <span class="pull-right">Cliente: ${pedido.getUsuario().getNome()}</span>
                    </li>
                    <li>
                        <span class="pull-left">Total itens: ${pedido.getItens().size()}</span>
                        <span class="pull-right">CPF/CNPJ: ${pedido.getUsuario().getCpfCnpjString()}</span>
                    </li>
                    <li>
                        <span class="pull-left">Prev. de entrega: ${pedido.getDataPrevisaoInstalacaoString()}</span>
                        <span class="pull-right">Entrega: ${pedido.getEndereco().getLogradouro()}, ${pedido.getEndereco().getNumero()} - ${pedido.getEndereco().getBairro()} - ${pedido.getEndereco().getCidade().getNome()} - ${pedido.getEndereco().getCidade().getEstado().getUf()} - CEP: ${pedido.getEndereco().getCepString()}</span>
                    </li>
                    <li>
                        <span class="pull-left">Dias para produção: ${pedido.getTotalDiasProducao()}</span>
                        <span class="pull-right">Observação: ${pedido.getObservacao()}</span>
                    </li>
                </ul>
            </div>
            <hr>

            <table class="table table-striped js-row-material">
                <tbody>
                    <c:forEach items="${pedido.getItens()}" var="iterator">
                        <c:if test="${iterator.getStatusItemId() == 1}">
                            <tr class="table-secondary">
                                <td rowspan="2" class="col-img">
                                    <img src="${source}${iterator.getModelo().getImagemSource()}" class=""/>
                                </td>
                                <td><b>#${iterator.getIdString()} - </b>${iterator.getModelo().getNome()} - ${iterator.getDimensao()} - Original <b>(${iterator.getModelo().getDimensao()})</b></td>
                                <td>
                                    <c:if test="${iterator.getIsPintura()}">
                                        <label for="pintura-${iterator.getId()}">&nbsp;<i class="far fa-check-square"></i> Pintura <b></b></label>
                                    </c:if>
                                    <c:if test="${iterator.getIsPintura() == false}">
                                        <label for="pintura-${iterator.getId()}">Sem pintura</label>
                                    </c:if>
                                </td>
                                <td>
                                    <b>Qtd.:</b> ${iterator.getQuantidade()} un.
                                </td>
                                <td>
                                    &nbsp;
                                </td>
                            </tr>
                            <tr class="table-secondary">
                                <td colspan="4">
                                    <div class="pull-left pg-0 text-left">
                                        <b>${iterator.getModelo().getDescricao()}</b>
                                    </div>
                                </td>
                            </tr>
                            <tr>
                                <td colspan="5">
                                    <table class="table table-bordered">
                                        <tr>
                                            <th>Material</th>
                                            <th>Quantidade</th>
                                            <th>Fornecedor</th>
                                        </tr>
                                        <c:forEach items="${iterator.getModelo().getMateriais()}" var="material">
                                            <tr>
                                                <td>${material.getDescricao()} por '${material.getUnidadeMedida().getNome()}</td>
                                                <td>${material.getQuantidade()}</td>
                                                <td>${material.getFornecedor().getNome()}</td>
                                            </tr>
                                        </c:forEach>
                                    </table>
                                </td>
                            </tr>
                            <tr class="tr-none"><td></td></tr>
                        </c:if>
                    </c:forEach>
                </tbody>
            </table>
        </div>
    </body>
</html>