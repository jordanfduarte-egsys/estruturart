var BASE_URL = $('#urlBase').val();

window.FlashMessenger = {};
window.FlashMessenger.SUCCESS = "success";
window.FlashMessenger.ERROR = "danger";
window.FlashMessenger.INFO = "primary";

// USO: "a{0}bcd{1}ef".format("foo", "bar");
String.prototype.format = String.prototype.format ||
  function () {
    "use strict";
    var str = this.toString();
    if (arguments.length) {
        var t = typeof arguments[0];
        var key;
        var args = ("string" === t || "number" === t) ?
            Array.prototype.slice.call(arguments)
            : arguments[0];

        for (key in args) {
            str = str.replace(new RegExp("\\{" + key + "\\}", "gi"), args[key]);
        }
    }

    return str;
};

var flashMessenger = {
    messages: [],
    type: window.FlashMessenger.SUCCESS,

    add: function (message) {
        this.messages.push(message);
        return this;
    },

    getMessages: function () {
        this.render(this.messages.join("<br/>"));
        this.messages = [];
        return this;
    },

    render: function (messages) {
        var icon = "";
        if (this.type == "success") {
            icon = '<i class="far fa-thumbs-up fa-2x"></i>';
        } else if (this.type == "danger") {
            icon = '<i class="fas fa-exclamation fa-2x"></i>';
        } else if (this.type == "primary") {
            icon = '<i class="fas fa-exclamation-circle fa-2x"></i>';
        }

        var html  = '<div class="alert alert-'
            + this.type
            + '" alert-dismissible" role="alert">'
            + icon
            + messages
            + '<button type="button" class="close" data-dismiss="alert" aria-label="Close">'
            + '<span aria-hidden="true">×</span>'
            + '</button>'
            + '</div>';

        $(".d-flex > .message").html($(html));
        $(".d-flex > .message").fadeIn(1000, function() {
            $(".d-flex > .message").removeClass('hide');
        });
        window.setTimeout(function(){
            $(".d-flex > .message").addClass("hide");
        }, 6000);
    },

    setType: function(type) {
        this.type = type;
        return this;
    }
};

function bindMask() {
    $('[data-format="cpf"]').off().mask('000.000.000-00', {reverse: true});
    $('[data-format="cnpj"]').off().mask('00.000.000/0000-00', {reverse: true});
    $('[data-format="rg"]').off().mask('000000000-0');
    $('[data-format="inscricao_estadual"]').off().mask('000-00000-00');
    $('[data-format="number"]').off().mask('000000000', {clearOnEmpty: true});
    $('[data-format="cep"]').off().mask('00000-000', {clearOnEmpty: true});
    $('[data-format="float"]').off().mask('0000000,00', {reverse: true,  clearOnEmpty: true});
    $('[data-format="float2"]').off().mask('0000000,000', {reverse: true,  clearOnEmpty: true});
    $('[data-format="telefone"]').off().mask('(000) 00000-0000');
    $('[data-toggle="datepicker"]').mask('00/00/0000');
    $('[data-toggle="datepicker"]').datepicker({
        format: 'dd/mm/yyyy',
        days: ['Segunda', 'Terça', 'Quarta', 'Quinta', 'Sexta', 'Sábado', 'Domingo'],
        daysShort: ['Seg', 'Ter', 'Qua', 'Qui', 'Sex', 'Sab', 'Dom'],
        daysMin: ['Se', 'Te', 'Qu', 'Qi', 'Sx', 'Sa', 'Do'],
        months: ['Janeiro', 'Fevereiro', 'Mar?o', 'Abril', 'Maio', 'Junho', 'Julho', 'Agosto', 'Setembro', 'Outubro', 'Novembro', 'Dezembro'],
        monthsShort: ['Jan', 'FeV', 'Mar', 'Avr', 'Mai', 'Jun', 'Jul', 'Ago', 'Set', 'Out', 'Nov', 'Dez'],
    });
    $('[data-format="price"]').off().priceFormat({
        prefix: '',
        centsSeparator: ',',
        thousandsSeparator: '.',
        clearOnEmpty: true
    });

    $('body').on('click', '[data-image="show"]', function() {
        var image = new Image();
        image.src = $(this).attr("src");
        var viewer = new Viewer(image, {
            hidden: function () {
              viewer.destroy();
            },
        });

        viewer.show();
    });

    $('[data-format="cpf_or_cnpj"]').keydown(function(){
        try {
            $(this).unmask();
        } catch (e) {}

        var tamanho = $(this).val().length;
        if (tamanho < 11) {
            $(this).mask("999.999.999-99");

            $('#etapa1 [for=nome_completo]')
                .html('Nome completo *')
                .next()
                .attr('placeholder', 'Nome completo');
        } else if(tamanho >= 11) {
            $(this).mask("99.999.999/9999-99");

            $('#etapa1 [for=nome_completo]')
                .html('Razão social *')
                .next()
                .attr('placeholder', 'Empresa LTDA');
        }

        // ajustando foco
        var elem = this;
        setTimeout(function(){
            elem.selectionStart = elem.selectionEnd = 10000;
        }, 0);
        // reaplico o valor para mudar o foco
        var currentValue = $(this).val();
        $(this).val('');
        $(this).val(currentValue);
    });

    $('body').on('click', '.js-print-table', function() {
      window.open(BASE_URL + $(this).data('href'));
    });
}

function toDateString(date) {
  var dateAux = date.split("-");
  var horario = dateAux[2].split(" ");
  dateAux[2] = horario[0];

  return "{0}/{1}/{2}".format(dateAux[2], dateAux[1], dateAux[0])
}

function toDateTimeString(date) {
  var dateAux = date.split("-");
  var horario = dateAux[2].split(" ");
  dateAux[2] = horario[0];
  horario = horario[1];
  return "{0}/{1}/{2} {3}".format(dateAux[2], dateAux[1], dateAux[0], horario)
}

function numberToReal(numero) {
    if (numero == '' || numero == null) {
      return "0,00";
    }

    var numero = (+numero).toFixed(2).split('.');
    numero[0] = numero[0].split(/(?=(?:...)*$)/).join('.');
    return numero.join(',');
}

function clearFlash() {
    // Limpa a de mensagem
    $.ajax({
        url: BASE_URL + "auth/flash-messages",
        type: "GET"
    });
}

function configureAjax() {
    var showLoading = new ShowLoading();
    $.ajaxSetup({
        beforeSend: function() {
            showLoading.show();
        },
        complete: function() {
            showLoading.hide();
        }
    });

    $('body').find('form').on('submit', function() {
        showLoading.show();
    });
}

function toFloat(valueStr) {
    if (valueStr == '') return 0;

    if (isNaN(+valueStr)) {
        return +(valueStr.replace('.', '').replace(',', '.'));
    }

    return +valueStr;
}

function toFloatBr(valueFloat) {
    return String(valueFloat).replace('.', ',');
}

function serializeToSimplesParam(json) {
  var jsonReturn = {};
  $.each(json, function(i, v) {
    jsonReturn[v.name] = v.value;
  });

  return jsonReturn;
}

$(document).ready(function() {
  $('.js-clear-form').on('click', function(e) {
      e.preventDefault();
      window.location.href = window.location.href;
  });

  $('.js-back').on('click', function(e) {
      e.preventDefault();
      history.go(-1);
  })

  $('.js-new-register').on('click', function(e) {
      window.location.href = $(this).data('href');
  });

  $(".d-flex > .message").fadeIn(1000, function() {
      $(".d-flex > .message").removeClass('hide');
  });
  window.setTimeout(function(){
      $(".d-flex > .message").addClass("hide");
  }, 6000);

  bindMask();
  clearFlash();
  configureAjax();
});