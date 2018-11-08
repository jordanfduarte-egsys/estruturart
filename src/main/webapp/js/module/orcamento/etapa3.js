var crud = {
    init: function() {
        this.binds();
    },

    binds: function() {
        var $desconto = $('#desconto');
        var $maoObra = $('#mao_obra');
        var $btnCriarOrcamento = $('.js-save-orcamento');

        $desconto.on('keyup mouseup blur', crud.event.calculoDesconto);
        $desconto.on('keydown', crud.event.inputNumber);
        $maoObra.on('keyup blur', crud.event.calculoMaoObra);
        $btnCriarOrcamento.on('click', crud.event.criarOrcamento);
        crud.event.initTooltipe();
    },

    event: {
        initTooltipe: function() {
            var $prevEntrega = $('#prev_entrega');
            var $desconto = $('#desconto');
            var $maoObra = $('#mao_obra');

            if ($prevEntrega.val() == "") {
                $prevEntrega.closest('.input-group').find('[data-toggle="tooltip"]').tooltip('show');
            } else if ($desconto.val() == "") {
                $desconto.closest('.input-group').find('[data-toggle="tooltip"]').tooltip('show');
            } else if ($maoObra.val() == "") {
                $maoObra.closest('.input-group').find('[data-toggle="tooltip"]').tooltip('show');
            }
        },

        calcSubTotal: function(totalItensSemPintura, totalPintura, porcentagemDesconto, maoObra) {
            var total = totalItensSemPintura - ((totalItensSemPintura * porcentagemDesconto) / 100);
            total += totalPintura + maoObra;

            return total;
        },

        inputNumber: function(e) {
            if (e.which == 110 || e.which == 194) {
                return false;
            }
        },

        calculoDesconto: function(e) {
            var $table = $('.js-row-material');
            var totalItensSemPintura = toFloat($table.data('total-itens'));
            var totalPintura = toFloat($table.data('total-pintura'));
            var porcentagemDesconto = toFloat($(this).val());
            var maoObra = toFloat($('#mao_obra').val());
            var $append = $('.js-sub-total');
            var total = crud.event.calcSubTotal(totalItensSemPintura, totalPintura, porcentagemDesconto, maoObra);
            $append.html(numberToReal(total));

            if (porcentagemDesconto > toFloat($('#max_porcentagem').val())) {
                $append.parent().addClass('text-danger');
            } else {
                $append.parent().removeClass('text-danger');
            }
        },

        calculoMaoObra: function() {
            var $table = $('.js-row-material');
            var totalItensSemPintura = toFloat($table.data('total-itens'));
            var totalPintura = toFloat($table.data('total-pintura'));
            var porcentagemDesconto = toFloat($('#desconto').val());
            var maoObra = toFloat($(this).val());
            var $append = $('.js-sub-total');
            var total = crud.event.calcSubTotal(totalItensSemPintura, totalPintura, porcentagemDesconto, maoObra);
            $append.html(numberToReal(total));

            if (porcentagemDesconto > toFloat($('#max_porcentagem').val())) {
                $append.parent().addClass('text-danger');
            } else {
                $append.parent().removeClass('text-danger');
            }
        },

        criarOrcamento: function(e) {
            e.preventDefault();

            $('form').append('<input name="is_orcamento" value="1" type="hidden" />');
            $(this).off();
            $(this).parent().find('button[type=submit]').trigger('click');
        }
    }
}

$(document).ready(function() {
    communOrcamento.init();
    crud.init();
});