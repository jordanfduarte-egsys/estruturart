<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ page contentType="text/html; charset=UTF-8" %>
<div class="container h-100">
       <h3 class="text-center mb-4">Olá ${usuario.getNome()}, Informe a nova senha</h3>
       <div class="row h-100 justify-content-center align-items-center">
           <div class="col-md-4">
               <h1 class="text-center mb-4">Recuperar</h1>
               <div class="card">
                   <div class="card-body">
                        <c:if test="${message != null}">
                            <div class="alert alert-danger" role="alert">
                                ${message}
                            </div>
                        </c:if>
                       <form method="POST" class="js-change-login">
                           <div class="input-group mb-3">
                               <div class="input-group-prepend">
                                   <span class="input-group-text"><i class="fa fa-key"></i></span>
                               </div>
                               <input type="password" name="senha" autocomplete="off" class="form-control" placeholder="Password" value="${senha}">
                           </div>

                           <div class="row">
                               <div class="col pr-2">
                                   <button type="submit" class="btn btn-block btn-primary">Recuperar</button>
                               </div>
                               <div class="col pl-2">
                                    <a data-toggle="tooltip" data-html="true" class="p-2" title="" data-original-title="A senha deve conter no mínimo seis caracteres alfanuméricos e no mínimo 1 e máximo 3 caracteres especiais, como exemplo {'@', '-', '.', '_', '&', '%', '$', '#', '!', '?'}">
                                        <i class="fas fa-info"></i>
                                    </a>
                               </div>
                           </div>
                       </form>
                   </div>
               </div>
           </div>
       </div>
   </div>