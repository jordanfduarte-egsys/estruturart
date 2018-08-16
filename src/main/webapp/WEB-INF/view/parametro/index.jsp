<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ page contentType="text/html; charset=UTF-8" %>
<div class="content p-4">   
    <h2>Configuração do sistema</h2>
    <div class="card mb-4">
        <div class="card-body">
            <form action="${source}parametro" method="POST">
                <fieldset class="col-md-8 mb-3 border">
                    <legend>Sobre a empresa</legend>
                    <div class="form-row">
                        <div class="col-md-4 mb-3">
                            <label for="cep">Cep</label>
                            <input type="text" class="form-control" id="cep" name="cep" placeholder="Cep do local" value="${parametro.getCep()}">                        
                        </div>
                        <div class="col-md-4 mb-3">
                            <label for="logradouro">Endereço</label>
                            <input type="text" class="form-control" id="logradouro" name="logradouro" placeholder="Endereço" value="${parametro.getLogradouro()}">                        
                        </div>
                        <div class="col-md-4 mb-3">
                            <label for="numero">Número</label>
                            <input type="text" class="form-control" id="numero" name="numero" placeholder="100" value="${parametro.getNumero()}">                        
                        </div>
                    </div>

                    <div class="form-row">
                        <div class="col-md-2 mb-3">
                            <label for="bairro">Bairro</label>
                            <input type="text" class="form-control" id="bairro" name="bairro" placeholder="Bairro" value="${parametro.getBairro()}">                        
                        </div>
                        <div class="col-md-4 mb-3">
                            <label for="complemento">Complemento</label>
                            <textarea class="form-control" id="complemento" name="complemento" placeholder="Cep do local">${parametro.getComplemento()}</textarea>
                        </div>     
                        <div class="col-md-4 mb-3">
                            <label for="cidade">Cidade</label>
                            <input type="text" class="form-control" id="cidade" name="cidade" placeholder="Nome da cidade" value="${parametro.getCidade()}">                        
                        </div>
                        <div class="col-md-2 mb-3">
                            <label for="uf">Uf</label>
                            <input type="text" class="form-control" id="uf" name="uf" maxlenght="2" placeholder="UF" value="${parametro.getUf()}">                        
                        </div>               
                    </div>
                </fieldset>
                <fieldset class="col-md-8 mb-3 border">
                    <legend>Sobre o envio de E-mail</legend>
                    <div class="form-row">
                        <div class="col-md-4 mb-3">
                            <label for="from">E-mail</label>
                            <input type="text" class="form-control" id="from" name="from" placeholder="E-mail de origem" value="${parametro.getFrom()}">                        
                        </div>
                        <div class="col-md-4 mb-3">
                            <label for="host_mail">Servidor</label>
                            <input type="text" class="form-control" id="host_mail" name="host_mail" placeholder="Servidor de e-mail" value="${parametro.getHostMail()}">                        
                        </div>
                        <div class="col-md-4 mb-3">
                            <label for="host">Host Empresa</label>
                            <input type="text" class="form-control" id="host" name="host" placeholder="100" value="${parametro.getHost()}">                        
                        </div>
                    </div>

                    <div class="form-row">
                        <div class="col-md-4 mb-3">
                            <label for="usuario">Usuário</label>
                            <input type="text" class="form-control" id="usuario" name="usuario" placeholder="Nome do usuario de autenticação" value="${parametro.getUsuario()}">                        
                        </div>
                        <div class="col-md-4 mb-3">
                            <label for="senha">Senha de acesso</label>
                            <input type="password" class="form-control" id="senha" name="senha" placeholder="*****" value="${parametro.getSenha()}">                        
                        </div>
                    </div>
                </fieldset>

                <div class="card-footer bg-white">
                    <button type="submit" class="btn btn-primary"><i class="fas fa-cogs"></i>&nbsp;Salvar</button>
                </div>
            </form>
        </div>
    </div>
 </div>
