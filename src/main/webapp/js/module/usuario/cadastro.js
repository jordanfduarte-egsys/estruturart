var FISICA = 1;
var JURIDICA = 2;
var PERFIL_CLIENTE = 1;

var crud = {
    init: function () {
        this.binds();
    },

    binds: function () {
        $("input[name=tipo_pessoa]").on("click", crud.events.toggleTipoPessoa);
        $("input[name=perfil_id]").on("click", crud.events.togglePerfil);
    },

    events: {

        toggleTipoPessoa: function (e) {
            var $cpf = $("#cpfCnpj");
            var $nome = $("#nome");
            var $rg = $("#numeroDocumento");

            if (+$(this).val() == FISICA) {
                $cpf.attr("placeholder", "000.000.000-99").attr("data-format", "cpf").prev().html("Cpf *");
                $nome.attr("placeholder", "Jordan Duarte").prev().html("Nome *");
                $rg.attr("placeholder", "00000000-5").attr("data-format", "rg").prev().html("RG");
            }

            if (+$(this).val() == JURIDICA) {
                $cpf.attr("placeholder", "00.000.000/0001-00").attr("data-format", "cnpj").prev().html("Cnpj *");
                $nome.attr("placeholder", "Empresa S.A").prev().html("Razão Social *");
                $rg.attr("placeholder", "000.00000-00").attr("data-format", "inscricao_estadual").prev().html("Inscrição estadual");
            }

            $cpf.val("");
            $rg.val("");

            bindMask();
        },

        togglePerfil: function (e) {
            if ($(this).val() == PERFIL_CLIENTE) {
                $("#validationServerPass").closest(".form-row").addClass("hide");
            } else {
                $("#validationServerPass").closest(".form-row").removeClass("hide");
                $("#validationServerPass").val("");
            }
        }
    }
};

$(document).ready(function () {
    crud.init();
});