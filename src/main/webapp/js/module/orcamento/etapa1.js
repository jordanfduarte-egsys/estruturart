var crud = {
    idEstado: null,

    init: function() {
        this.binds();
    },

    binds: function() {
        var $btnFindCpfCnpj = $(".js-find-cpf-cnpj");
        var $cep = $('#cep');
        var $estadoId = $('#estado_id');
        var $cidadeId = $('#cidade_id');

        $btnFindCpfCnpj.on('click', crud.event.findUser);
        $cep.on('blur', crud.event.findCep);
        $estadoId.on('change', function() {
            var showLoading = new ShowLoading();
            showLoading.show();
            crud.event.findCidades(
                $(this).val(),
                function (res) {
                    if (res.status) {
                        if (res.list) {
                            $cidadeId.html('');
                            rendererList(
                                res.list, // alterações no html modelo
                                "#row-cidade", // Template modelo que vai ser clonado
                                $cidadeId // Para onde vai ser jogado o modelo alterado
                            );
                        }
                    }
                    showLoading.hide();
                }
            )
        });
    },

    event: {
        findUser: function() {
            var $cpfCnpj = $('#cpf_cnpj');
            if ($cpfCnpj.val() != "") {
                var $nomeCompleto = $('#nome_completo');
                var $rgInscricaoEstadual = $('#rg_inscricao_estadual');
                var $email = $('#email');
                var $telefone = $('#telefone');
                var $usuarioId = $('#usuario_id');

                var request = $.ajax({
                    url: BASE_URL + 'usuario/find-cpf-cnpj',
                    type: 'POST',
                    dataType: 'json',
                    data: {cpf_cnpj: $cpfCnpj.val().replace(/[.]|[/]|[-]/g, '')}
                });

                request.done(function(response) {
                    if (response.status) {
                        if (response.object.id > 0) {
                            $cpfCnpj.val(response.object.cpfCnpj);
                            $nomeCompleto.val(response.object.nome).prop('readonly', true);
                            $rgInscricaoEstadual.val(response.object.rgIncricaoEstadual).prop('readonly', true);
                            $email.val(response.object.email).prop('readonly', true);
                            $telefone.val(response.object.telefone).prop('readonly', true);
                            $usuarioId.val(response.object.id);
                        } else {
                            $nomeCompleto.val().prop('readonly', false);
                            $rgInscricaoEstadual.val().prop('readonly', false);
                            $email.val().prop('readonly', false);
                            $telefone.val().prop('readonly', false);
                            $usuarioId.val('');
                        }
                    }
                });

                request.fail(function() {
                    flashMessenger.setType(FlashMessenger.ERROR).add("Ocorreu um erro ao consultar o CPF/CNPJ").getMessages();
                });
            }
        },

        findCep: function() {
            if ($(this).val() != '') {
                var $logradouro = $('#logradouro');
                var $numero = $('#numero');
                var $estadoId = $('#estado_id');
                var $cidadeId = $('#cidade_id');
                var $bairro = $('#bairro');

                var request = $.ajax({
                    url: BASE_URL + 'cidade/find-cep',
                    type: 'POST',
                    dataType: 'json',
                    data: {cep: $(this).val().replace(/[.]|[/]|[-]/g, '')}
                });

                request.done(function(response) {
                    if (response.status) {
                        if (response.object.cep) {
                            $logradouro.val(response.object.logradouro);
                            $estadoId.val(response.object.cidade.estado.id);
                            $bairro.val(response.object.bairro);
                        } else {
                            $logradouro.val('');
                            $estadoId.val(0);
                            $bairro.val('');
                        }

                        if (crud.idEstado != response.object.cidade.estado.id) {
                            var showLoading = new ShowLoading();
                            showLoading.show();
                            crud.event.findCidades(
                                response.object.cidade.estado.id,
                                function (res) {
                                    if (res.status) {
                                        if (res.list) {
                                            $cidadeId.html('');
                                            rendererList(
                                                res.list, // alterações no html modelo
                                                "#row-cidade", // Template modelo que vai ser clonado
                                                $cidadeId // Para onde vai ser jogado o modelo alterado
                                            );

                                            $cidadeId.val(response.object.cidade.id);
                                        }
                                    }

                                    showLoading.hide();
                                }
                            );
                        } else {
                            $cidadeId.val(response.object.cidade.id);
                        }
                    }
                });

                request.fail(function() {
                    flashMessenger.setType(FlashMessenger.ERROR).add("Ocorreu um erro ao consultar o CEP").getMessages();
                });
            }
        },

        findCidades: function(estadoId, callback) {
            crud.idEstado = estadoId;
            var request = $.ajax({
                url: BASE_URL + 'cidade/find-cidade',
                type: 'POST',
                dataType: 'json',
                data: {estado_id: estadoId}
            });

            request.done(function(response) {
                callback(response);
            });

            request.fail(function() {
                flashMessenger.setType(FlashMessenger.ERROR).add("Ocorreu um erro ao consultar as cidades").getMessages();
                callback({status: false});
            });
        }
    }
};

$(document).ready(function() {
    crud.init();
    communOrcamento.init();
});
