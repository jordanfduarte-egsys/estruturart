var communOrcamento = {
    init: function() {
        this.binds();
    },

    binds: function() {
        var $btBack = $('.js-back-orcamento');

        $btBack.on('click', communOrcamento.event.back);
    },

    event: {
        back: function() {
            window.location.href = BASE_URL + $(this).data('href').replace('/orcamento', 'orcamento');
        }
    }
};