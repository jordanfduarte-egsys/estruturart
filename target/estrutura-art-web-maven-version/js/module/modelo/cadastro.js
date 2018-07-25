var crud = {
    init: function () {
        this.binds();
    },

    binds: function () {
        var $busca = $('#busca');
        var $filtrado = $('#filtrado');
        var $table = $('.js-row-material tbody');
        var $porcentagemAcrescimo = $('#porcentagem_acrescimo');
        var $comDesconto = $('.js-calc-desc-max');
        var $pintura = $('#preco_pintura');
        var $imagem = $("#imagem");

        var options = {
            url: function (phrase) {
                return BASE_URL + "material/buscar-modelo?nome=" + phrase + "&format=json";
            },
            getValue: "descricaoFiltro",
            listLocation: "list",
            list: {
                match: {
                    enabled: true
                },
                onClickEvent: function() {
                    var item = $busca.getSelectedItemData();
                    renderer(
                        item, // alterações no html modelo
                        "#row-material", // Template modelo que vai ser clonado
                        $table // Para onde vai ser jogado o modelo alterado
                    );

                    $('.table-materiais').animate({
                        scrollTop: 100000000000
                    }, 2000);

                    $table.find(".js-nenhum-material").remove();
                    $table.find(".js-remover").off().on("click", crud.events.removeModelo);
                    $busca.val("");
                    crud.events.changePorcentagem();
                }
            }
        };

        $busca.easyAutocomplete(options);
        $porcentagemAcrescimo.on('blur', crud.events.de0a10);
        $porcentagemAcrescimo.on('keypress, change, keydown, keyup', crud.events.changePorcentagem);
        $pintura.on('keypress, change, keydown, keyup', crud.events.changePorcentagem);
        $imagem.on('change', crud.events.onFileSelecionado);
        $table.find(".js-remover").on("click", crud.events.removeModelo);
    },

    events: {
        removeModelo: function () {
            if ($(this).closest("tbody").find("tr").length == 1) {
                $(this).closest("tbody").append(
                    '<tr class="js-nenhum-material">\
                        <td colspan="4">Nenhum material selecionado!</td>\
                    </tr>'
                );
            }

            $(this).closest("tr").remove();
            crud.events.changePorcentagem();
        },

        de0a10: function (e) {
            if ($(this).val() < 0 || $(this).val() > 100) {
                $(this).val("");
            }
        },

        changePorcentagem: function() {
            var $porcentagemAcrescimo = $('#porcentagem_acrescimo');
            var $total = $('.js-calc-total');
            var $totalMateriais = $('.js-preco-materiais');
            var $table = $('.js-row-material tbody');
            var $pintura = $('#preco_pintura');
            var $comDesconto = $('.js-calc-desc-max');
            var precoPintura = +$pintura.val().replace(/[.]/g, '').replace(',', '.');
            var totalDescontoSpan = 0;
            var totalSpan = 0;
            var $qtdMateriais = $('.js-qtd-material');

            $table.find("td.js-preco").each(function() {
                totalDescontoSpan = totalSpan += +$(this).data('preco');
            });

            var precoMateriais = totalSpan;
            totalSpan = ((+$porcentagemAcrescimo.val() * totalSpan) / 100) + totalSpan;

            $total.find("b").html(numberToReal(totalSpan + precoPintura));
            $totalMateriais.find('b').html(numberToReal(precoMateriais));
            $comDesconto.find("b").html(numberToReal(totalDescontoSpan + precoPintura));
            $qtdMateriais.find("b").html($table.find("td.js-preco").length);
        },

        onFileSelecionado: function(e) {
            if (e.target.files.length == 0) {
                flashMessenger.setType(FlashMessenger.ERROR).add("Nenhum Arquivo Selecionado").getMessages();
                return;
            }

            if (e.target.files[0].size / 1024 / 1024 > 2) {
                flashMessenger.setType(FlashMessenger.ERROR).add("Arquivo deve ser menor que 2 MB").getMessages();
                return;
            }

            if (["image/jpeg", "image/jpg", "image/png", "image/bmp"].indexOf(e.target.files[0].type) == -1) {
                flashMessenger.setType(FlashMessenger.ERROR).add("Extensão do arquivo enviada inválida. Verifique!").getMessages();
                return;
            }

            $('.js-image-changed').removeClass('hide');
            $('.js-image').addClass('hide');
        }
    }
};

$(document).ready(function () {
    crud.init();
});
