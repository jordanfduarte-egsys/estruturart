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

        <link rel="icon" type="image/png" href="${source}files/favicon.png" />
        <title>${nomeEmpresa} | ${title}</title>
    </head>
    <body class="bg-light <c:if test="${route.getController().equals('auth')}">home</c:if>"></body>
        <input id="urlBase" type="hidden" value="${source}"/>

        <c:if test="${isAuth == true}">
            <nav class="navbar navbar-expand navbar-dark bg-primary">
                <a class="sidebar-toggle mr-3" href="#"><i class="fa fa-bars"></i></a>
                <a class="navbar-brand" href="#">${nomeEmpresa}</a>

                <div class="navbar-collapse collapse">
                    <ul class="navbar-nav ml-auto">
                        <li class="nav-item dropdown">
                            <a href="#" id="dd_user" class="nav-link dropdown-toggle" data-toggle="dropdown"><i class="fa fa-user"></i> ${auth.getCodigo()} - ${auth.getNome()}</a>
                            <div class="dropdown-menu dropdown-menu-right" aria-labelledby="dd_user">
                                <a href="${source}usuario/editar/id/${auth.getId()}" class="dropdown-item">Perfil</a>
                                <a href="${source}auth/logout" class="dropdown-item">Sair</a>
                            </div>
                        </li>
                    </ul>
                </div>
            </nav>
        </c:if>

        <div class="d-flex">
            <c:if test="${isAuth == true}">
                <div class="sidebar sidebar-dark bg-dark">
                    <ul class="list-unstyled">
                        <li class="<c:if test="${route.getController().equals('home')}">active</c:if>"><a href="${source}home"><i class="fa fa-fw fa-home"></i> Home</a></li>
                        <li class="<c:if test="${route.getController().equals('usuario')}">active</c:if>"><a href="${source}usuario"><i class="fa fa-fw fa-user"></i> Usuários</a></li>
                        <li class="<c:if test="${route.getController().equals('fornecedor')}">active</c:if>"><a href="${source}fornecedor"><i class="fa fa-fw fa-cube"></i> Fornecedores</a></li>
                        <li class="<c:if test="${route.getController().equals('material')}">active</c:if>"><a href="${source}material"><i class="fa fa-fw fa-cubes"></i> Materiais</a></li>
                        <li class="<c:if test="${route.getController().equals('modelo')}">active</c:if>"><a href="${source}modelo"><i class="fab fa-windows"></i> Modelos</a></li>
                        <li class="<c:if test="${route.getController().equals('orcamento')}">active</c:if>"><a href="${source}orcamento"><i class="fas fa-cart-plus"></i> Orçamento</a></li>
                        <li class="<c:if test="${route.getController().equals('pedido')}">active</c:if>"><a href="${source}pedido"><i class="fab fa-slack-hash"></i> Pedidos</a></li>
                        <li class="<c:if test="${route.getController().equals('lancamento') && !route.getActionName().equals('fluxo-caixa')}">active</c:if>"><a href="${source}lancamento"><i class="fa fa-fw fa-hand-holding-usd"></i> Lançamento</a></li>
                        <li class="<c:if test="${route.getController().equals('lancamento') && route.getActionName().equals('fluxo-caixa')}">active</c:if>"><a href="${source}lancamento/fluxo-caixa"><i class="fa fa-fw fa-calendar-alt"></i> Fluxo de caixa</a></li>
                        <li class="<c:if test="${route.getController().equals('parametro')}">active</c:if>"><a href="${source}parametro"><i class="fa fa-fw fa-cogs"></i> Configuração</a></li>
                        <li><a href="${source}auth/logout"><i class="fa fa-fw fa-sign-out-alt"></i> Sair</a></li>
                    </ul>
                </div>
            </c:if>
            <div class="message hide">
                <c:if test="${flashMessenger.isMessages()}">
                    <div class="alert alert-${flashMessenger.getType()}" alert-dismissible" role="alert">
                        <c:if test="${flashMessenger.getType().equals('success')}">
                            <i class="far fa-thumbs-up fa-2x"></i>
                        </c:if>
                        <c:if test="${flashMessenger.getType().equals('danger')}">
                            <i class="fas fa-exclamation fa-2x"></i>
                        </c:if>
                        <c:if test="${flashMessenger.getType().equals('primary')}">
                            <i class="fas fa-exclamation-circle fa-2x"></i>
                        </c:if>
                        ${flashMessenger.getMessages()}
                        <button type="button" class="close" data-dismiss="alert" aria-label="Close">
                            <span aria-hidden="true">×</span>
                        </button>
                    </div>
                </c:if>
            </div>
            <jsp:include page="/WEB-INF/view/${route.getController()}/${route.getActionName()}.jsp"/>
        </div>

        <script src="${source}js/jquery.min.js"></script>
        <script src="${source}js/datepicker-master/dist/datepicker.js"></script>
        <script src="${source}js/fullcalendar-3.9.0/lib/moment.min.js"></script>
        <script src="${source}js/fullcalendar-3.9.0/fullcalendar.min.js"></script>
        <script src="${source}js/fullcalendar-3.9.0/locale/pt-br.js"></script>
        <script src="${source}js/viewerjs-master/dist/viewer.js"></script>
        <script src="${source}js/show-loading.js"></script>
        <script src="${source}js/jquery.easy-autocomplete.js"></script>
        <script src="${source}js/jquery.priceformat.js"></script>
        <script src="${source}js/jquery.mask.js"></script>
        <script src="${source}js/bootstrap.bundle.min.js"></script>
        <script src="${source}js/bootadmin.min.js"></script>
        <script src="${source}js/common.js"></script>
        <script src="${source}js/renderer.js"></script>
        <script src="${source}js/module/${route.getController()}/${route.getActionName()}.js"></script>
    </body>
</html>
