var crud = {
    init: function () {
        this.binds();
    },

    binds: function () {
        $(".js-busca-avancada").on("click", crud.events.toggleBuscaAvancada);
        $("input[name=tipo_pessoa]").on("click", crud.events.tootlgeTipoPessoa)
    },

    events: {
        toggleBuscaAvancada: function (e) {
            e.preventDefault();

            $(".js-toggle-avancada").toggleClass('show');
        }
    }
};

$(document).ready(function () {
    crud.init();
});