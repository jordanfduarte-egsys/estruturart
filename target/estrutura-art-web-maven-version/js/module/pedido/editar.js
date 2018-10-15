var crud = {
  idEstado: null,

  init: function() {
      this.binds();
  },

  binds: function() {
    var $btBack = $('.js-back-order');
    var $status = $('#status_pedido_id');
    var $cancelarOrder = $('.js-cancelar-pedido');
    var $removerItem = $('.js-remove-item');
    var $cep = $('#cep');
    var $estadoId = $('#estado_id');
    var $cidadeId = $('#cidade_id');

    $btBack.on('click', crud.event.action);
    $status.on('change', crud.event.changeStatus);
    $removerItem.on('click', crud.event.removerItem);
    $cancelarOrder.on('click', crud.event.cancelOrder);
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
    action: function(e) {
      e.preventDefault();
      window.location.href = BASE_URL + $(this).data('href').replace('/pedido', 'pedido');
    },

    changeStatus: function() {
      if (confirm("Deseja alterar o status do pedido para X ?")) {
        alert("OK");
      } else {
        $(this).val($(this).data('original-status'));
      }
    },

    cancelOrder: function() {
      alert("CANCEL ORDER");
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
    },

    removerItem: function(e) {
      e.preventDefault();
      var id = $(this).closest('tr').data('id');
      $(this).closest('table').find('tr[data-id="' + id + '"]').addClass('opaco');
      $(this).parent().append('<input name="item_remover[' + $(this).parent().data('item') + ']" type="hidden" value="' + $(this).parent().data('item') + '">');
      $(this).remove();
    }
  }
};

$(document).ready(function() {
  crud.init();
});