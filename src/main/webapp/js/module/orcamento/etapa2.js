var crud = {
    modelos: [],
    requestModeloFind: null,

    init: function() {
        this.binds();
    },
    binds: function() {
        var $busca = $("#busca");

        $busca.on('keyup', crud.event.buscaModelo);
        $('body').on('click', '.js-add-modelos', crud.event.addToCart);
        $('body').on('keyup', '.table-modelos .js-altura, .table-modelos .js-largura', crud.event.calcPrecoTotal);
        $('body').on('change', '.js-pintura-item', crud.event.checkPinturaCart);
        $('body').on('click', '.js-duplicar-item', crud.event.duplicarItemCart);
        $('body').on('click', '.js-remover-item', crud.event.removerItemCart);
        $('body').on('keyup mouseup blur', '.td-qtd-item input[type="number"]', crud.event.alterarQuantidadeCart);

    },
    event: {
        buscaModelo: function() {
            var showLoading = new ShowLoading();
            var $trInit = $('.js-init-modelo');
            var $table = $('.table-modelos table tbody');
            showLoading.setDivAppend($('.table-modelos'));

            if ($(this).val().length < 2) {
                return;
            }

            if ($(this).val().length == 0) {
                $table.find('tr:not(.js-init-modelo)').remove();
            }
            showLoading.show();

            var nome = $(this).val();
            window.setTimeout(function() {
                if (crud.requestModeloFind != null) {
                    console.log(crud.requestModeloFind);
                    crud.requestModeloFind.abort();
                }
                var request = $.ajax({
                    url: BASE_URL + 'modelo/buscar-modelo',
                    type: 'POST',
                    dataTye: 'json',
                    data: {nome: nome},
                    beforeSend: function() {},
                    complete: function() {showLoading.hide()}
                });

                request.done(function(response) {
                    $trInit.addClass('hide');
                    $table.find('tr:not(.js-init-modelo)').remove();

                    crud.modelos = response.list;
                    rendererList(
                        response.list,
                        'row-item',
                        $table
                    );

                    $('[data-format="float"]').off().mask('0000000,00', {reverse: true,  clearOnEmpty: true});

                    if (Object.keys(response.list).length == 0) {
                        $trInit.removeClass('hide');
                    }
                });

                request.fail(function(xhr, textStatus, error) {
                    if (request.status == 500) {
                        console.log("QUAL O ERRO: " + error);
                        flashMessenger.setType(FlashMessenger.ERROR).add("Ocorreu um erro ao consultar os modelos!").getMessages();
                    }

                    $table.find('tr:not(.js-init-modelo)').remove();
                    $trInit.removeClass('hide');
                });

                crud.requestModeloFind = request;
            }, 300);
        },

        addToCart: function() {
            var id = $(this).data('id');
            var model = crud.event.findModel(id);
            var $table = $('.table-modelos-add table tbody');

            $('.table-modelos-add').removeClass('is-invalid');
            model.index = crud.event.getNextIndex();
            model.indexFront = $table.find('.tr-one').length + 1;
            model.alturaPadraoNova = toFloat($(this).closest('tr').find('.js-altura').val());
            model.larguraPadraoNova = toFloat($(this).closest('tr').find('.js-largura').val());
            model.dimensao = "{0}x{1}".format(toFloatBr(model.alturaPadraoNova), toFloatBr(model.larguraPadraoNova));


            model.precoTotal = numberToReal(crud.event.precoItem(
                model.alturaPadrao,
                model.larguraPadrao,
                model.alturaPadraoNova,
                model.larguraPadraoNova,
                +model.precoTotalQtdStr,
                false,
                model.precoPintura,
                1
            ));

            $append = renderer(
                model,
                'row-item-add',
                $table
            );

            if (model.precoPintura == null || model.precoPintura == '') {
                $append.find('.td-pintura').remove();
            }
            $table.find('.js-nenhum-modelo').addClass('hide');
            flashMessenger.setType(FlashMessenger.SUCCESS).add("Item adicionado!").getMessages();
        },

        findModel: function(id) {
            var model = {};
            crud.modelos.forEach(function(value) {
                if (value.id == id) {
                    model = value;
                    return true;
                }
            });

            return model;
        },

        calcPrecoTotal: function() {
            var alturaAntiga = +$(this).closest('td').find('.js-altura').data('altura');
            var larguraAntiga = +$(this).closest('td').find('.js-largura').data('largura');
            var alturaNova = toFloat($(this).closest('td').find('.js-altura').val());
            var larguraNova = toFloat($(this).closest('td').find('.js-largura').val());
            var precoPadrao =  +$(this).closest('tr').next().find('.preco-produto').data('preco-inicial');
            var isPintura = false;
            var precoPintura = 0;
            var quantidade = 1;

            if (alturaNova == "" || typeof alturaNova == 'undefined') {
                alturaNova = 0;
            }

            if (larguraNova == "" || typeof larguraNova == 'undefined') {
                larguraNova = 0;
            }

            $(this).closest('tr').next().find('.preco-produto').html(
                "R$ " + numberToReal(crud.event.precoItem(
                    alturaAntiga,
                    larguraAntiga,
                    alturaNova,
                    larguraNova,
                    precoPadrao,
                    isPintura,
                    precoPintura,
                    quantidade
                ))
            );
        },

        precoItem: function(
            alturaAntiga,
            larguraAntiga,
            alturaNova,
            larguraNova,
            precoPadrao,
            isPintura,
            precoPintura,
            quantidade
        ) {
            var mediaMedidaAntiga = alturaAntiga * larguraAntiga;
            var mediaMedidaNova = alturaNova * larguraNova;

            var precoNovo = ((mediaMedidaNova * precoPadrao) / mediaMedidaAntiga) * quantidade;

            if (isPintura) {
                precoNovo = precoNovo + precoPintura;
            }

            return precoNovo;
        },

        checkPinturaCart: function() {
            var $tr = $(this).closest('.tr-one');
            var alturaNova = +$tr.data('altura-nova');
            var larguraNova = +$tr.data('largura-nova');
            var precoPintura = +$tr.data('preco-pintura');
            var alturaAntiga = +$tr.data('altura');
            var larguraAntiga = +$tr.data('largura');
            var quantidade = +$tr.find('.td-qtd-item input[type="number"]').val();
            var precoPadrao = +$tr.data('preco-inicial');
            var $appendPreco = $tr.find('.js-price-change b');

            $appendPreco.html(
                "R$ " + numberToReal(crud.event.precoItem(
                    alturaAntiga,
                    larguraAntiga,
                    alturaNova,
                    larguraNova,
                    precoPadrao,
                    $(this).is(':checked'),
                    precoPintura,
                    quantidade
                ))
            );
        },

        duplicarItemCart: function() {
            var showLoading = new ShowLoading();

            var $table = $('.table-modelos table tbody');
            showLoading.setDivAppend($('.table-modelos-add'));

            var request = $.ajax({
                url: BASE_URL + 'modelo/buscar-modelo',
                type: 'POST',
                dataTye: 'json',
                data: {id: $(this).closest('.td-btn-added').data('id')},
                beforeSend: function() {showLoading.show()},
                complete: function() {showLoading.hide()}
            });

            request.done(function(response) {
                var model = response.list[0];
                var $tableAppend = $('.table-modelos-add table tbody');

                model.index = crud.event.getNextIndex();
                model.indexFront = model.index + 1;
                model.alturaPadraoNova = model.alturaPadrao;
                model.larguraPadraoNova = model.larguraPadrao;
                model.dimensao = "{0}x{1}".format(toFloatBr(model.alturaPadrao), toFloatBr(model.larguraPadrao));

                model.precoTotal = numberToReal(crud.event.precoItem(
                    model.alturaPadrao,
                    model.larguraPadrao,
                    model.alturaPadraoNova,
                    model.larguraPadraoNova,
                    +model.precoTotalQtdStr,
                    false,
                    model.precoPintura,
                    1
                ));

                $append = renderer(
                    model,
                    'row-item-add',
                    $tableAppend
                );

                if (model.precoPintura == null || model.precoPintura == '') {
                    $append.find('.td-pintura').remove();
                }

                flashMessenger.setType(FlashMessenger.SUCCESS).add("Item adicionado!").getMessages();
            });

            request.fail(function() {
                flashMessenger.setType(FlashMessenger.ERROR).add("Ocorreu um erro ao consultar os modelos!").getMessages();
            });
        },

        getNextIndex: function() {
            var numbers = $(".table-modelos-add table tr[data-index]").map(function(){
                return $(this).data('index');
            }).toArray();

            if (numbers.length == 0) {
                return 0;
            }

            return Math.max.apply(Math, numbers) + 1;
        },

        removerItemCart: function() {
            var index = $(this).closest('tr').data('index');
            var $table = $('.table-modelos-add table');
            $(this).closest('tr').fadeOut("normal", function() {
                $(this).closest('table').find('tr[data-index="' + index + '"]').remove();

                if ($table.find('.tr-one').length == 0) {
                    $table.find('tbody .js-nenhum-modelo').removeClass('hide');
                }
            });

            $(".tooltip").remove();
            var count = 1;
            $table.find('tbody .tr-one').each(function() {
                $(this).find('.td-nome-item').html("{0} °<br/>Item".format(count));
                count++;
            });
            flashMessenger.setType(FlashMessenger.SUCCESS).add("Item removido do orçamento!").getMessages();
        },

        alterarQuantidadeCart: function() {
            var min = $(this).attr('min');
            var max = $(this).attr('max');

            if (+$(this).val() < +min) {
                $(this).val(min);
            }

            if (+$(this).val() > +max) {
                $(this).val(max);
            }

            var $tr = $(this).closest('.tr-one');
            var alturaNova = +$tr.data('altura-nova');
            var larguraNova = +$tr.data('largura-nova');
            var precoPintura = +$tr.data('preco-pintura');
            var alturaAntiga = +$tr.data('altura');
            var larguraAntiga = +$tr.data('largura');
            var precoPadrao = +$tr.data('preco-inicial');
            var $appendPreco = $tr.find('.js-price-change b');
            var isCheckedPreco = $tr.find('.js-pintura-item').is(':checked');

            $appendPreco.html(
                "R$ " + numberToReal(crud.event.precoItem(
                    alturaAntiga,
                    larguraAntiga,
                    alturaNova,
                    larguraNova,
                    precoPadrao,
                    isCheckedPreco,
                    precoPintura,
                    +$(this).val()
                ))
            );
        }
    }
}

$(document).ready(function() {
    communOrcamento.init();
    crud.init();
});