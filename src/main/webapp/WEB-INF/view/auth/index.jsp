<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ page contentType="text/html; charset=UTF-8" %>
<div class="container h-100">
       <div class="row h-100 justify-content-center align-items-center">
           <div class="col-md-4">
               <h1 class="text-center mb-4">${nomeEmpresa}</h1>
               <div class="card">
                   <div class="card-body">
                           <c:if test="${msgPassword != null || msgUsuario != null}">
                            <div class="alert alert-danger" role="alert">
                                ${msgUsuario}
                                <c:if test="${msgPassword != null && msgUsuario != null}">
                                    <br/>
                                </c:if>
                                ${msgPassword}
                            </div>
                        </c:if>
                       <form method="POST">
                           <div class="input-group mb-3">
                               <div class="input-group-prepend">
                                   <span class="input-group-text"><i class="fa fa-user"></i></span>
                               </div>
                               <input type="text" name="name" autocomplete="off" class="form-control" placeholder="Username" value="${name}">
                           </div>

                           <div class="input-group mb-3">
                               <div class="input-group-prepend">
                                   <span class="input-group-text"><i class="fa fa-key"></i></span>
                               </div>
                               <input type="password" name="pass" autocomplete="off" class="form-control" placeholder="Password" value="${pass}">
                           </div>

                           <div class="form-check mb-3">
                               <label class="form-check-label">
                                   <input type="checkbox" name="remember" class="form-check-input">
                                   Remember Me
                               </label>
                           </div>

                           <div class="row">
                               <div class="col pr-2">
                                   <button type="submit" class="btn btn-block btn-primary">Login</button>
                               </div>
                               <div class="col pl-2">
                                   <a class="btn btn-block btn-link" href="#">Forgot Password</a>
                               </div>
                           </div>
                       </form>
                   </div>
               </div>
           </div>
       </div>
   </div>