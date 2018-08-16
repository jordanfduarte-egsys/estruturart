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
        <link rel="stylesheet" href="${source}js/viewerjs-master/dist/viewer.css">
        <link rel="stylesheet" href="${source}js/datepicker-master/dist/datepicker.css">
        <link rel="stylesheet" href="${source}js/fullcalendar-3.9.0/fullcalendar.min.css">

        <title>Estrutura Art ${title}</title>
    </head>
    <body class="bg-light" onload="window.print(); return false;">
        <div class="d-flex">
            <h1 class="p-5" style="margin: 0 auto;">Fluxo de caixa</h1>
        </div>
        <div class="content p-4">
            <div class="card-body p-0">
                <div class="form-row">
                    <div class="col-md-12 mb-3">
                        <ul class="list-group pull-left">
                            <li class="list-group-item mr-2">Filtro de ${dataIni} até ${dataFim}</li>
                        </ul>
                        <ul class="list-group pull-left">
                            <li class="list-group-item mr-2">Total de gastos com a empresa: R$ ${totais.getSomaEmpresaString()}</li>
                        </ul>
                        <ul class="list-group pull-left">
                            <li class="list-group-item mr-2">Total de vendas de pedidos: R$ ${totais.getLucroEmpresaString()}</li>
                        </ul>
                        <ul class="list-group pull-left">
                            <li class="list-group-item mr-2">Lucro no periodo: R$ ${totais.getDiferencaString()}</li>
                        </ul>
                    </div>
                </div>
                <table class="table table-bordered">
                    <thead class="thead-dark">
                    <tr>
                        <th scope="col">Pedido</th>
                        <th scope="col">Material</th>
                        <th scope="col">Descrição</th>
                        <th scope="col">Valor</th>
                        <th scope="col">Data pedido</th>
                        <th scope="col">Data instalação</th>
                    </tr>
                    </thead>
                    <tbody>
                        <c:if test="${lancamentos.size() > 0}">
                            <c:forEach items="${lancamentos}" var="iterator1">
                                <tr>
                                    <th scope="row">
                                        <c:if test="${iterator1.getPedidoItem().getId() > 0}">
                                        <a href="${source}pedido/visualizar/id/${iterator1.getPedidoItem().getPedidoId()}#item-${iterator1.getPedidoItem().getId()}" target="_BLANK">#${iterator1.getPedidoItem().getIdString()}</a>
                                        </c:if>
                                        <c:if test="${iterator1.getPedidoItem().getId() == 0}">-</c:if>
                                    </th>
                                    <td>
                                        <c:if test="${iterator1.getPedidoItem().getId() == 0}">
                                            ${iterator1.getMaterial().getDescricao()}
                                        </c:if>
                                    </td>
                                    <td>
                                        ${iterator1.getDescricao()}
                                    </td>
                                    <td>R$ ${iterator1.getPrecoString()}</td>
                                    <td>
                                        <c:if test="${iterator1.getPedidoItem().getId() > 0}">
                                            ${iterator1.getPedidoItem().getPedido().getDataInclusaoString()}
                                        </c:if>
                                        <c:if test="${iterator1.getPedidoItem().getId() == 0}">-</c:if>
                                    </td>
                                    <td>
                                        <c:if test="${iterator1.getPedidoItem().getId() > 0}">
                                            ${iterator1.getPedidoItem().getPedido().getDataPrevisaoInstalacaoString()}
                                        </c:if>
                                        <c:if test="${iterator1.getPedidoItem().getId() == 0}">-</c:if>
                                    </td>
                                </tr>
                            </c:forEach>
                        </c:if>
                        <c:if test="${lancamentos.size() == 0}">
                            <tr>
                                <td colspan="5">Nenhum resultado encontrado com esse filtro!</td>
                            </tr>
                        </c:if>
                    </tbody>
                </table>
            </div>
        </div>
    </body>
</html>