var crud = {
  init: function() {
      this.binds();
  },

  binds: function() {
    var $btBack = $('.js-back-order');
    var $btnEditar = $('.js-editar');
    var $printOp = $('.js-imprimir-op');
    var $status = $('#status_pedido_id');
    var $cancelarOrder = $('.js-cancelar-pedido');
    var $lancamento = $('.js-lancamento');
    var $fotos = $('.js-fotos');

    $btBack.on('click', crud.event.action);
    $btnEditar.on('click', crud.event.action);
    $printOp.on('click', crud.event.actionOpen);
    $status.on('change', crud.event.changeStatus);
    $cancelarOrder.on('click', crud.event.cancelOrder);
    $lancamento.on('click', crud.event.lancamento);
    $fotos.on('click', crud.event.fotos);
  },

  event: {
    action: function(e) {
      e.preventDefault();
      window.location.href = BASE_URL + $(this).data('href').replace('/pedido', 'pedido');
    },

    actionOpen: function(e) {
      e.preventDefault();
      window.open(BASE_URL + $(this).data('href').replace('/pedido', 'pedido'));
    },

    changeStatus: function() {
      var $modal = $("#alteracaoStatus");
      var $self = $(this);

      $modal.modal('show');
      $modal.find('button.btn-primary').off().on('click', function() {
        var req = $.ajax({
          url: BASE_URL + "pedido/status",
          type: 'post',
          dataType: 'json',
          data: {id: $self.data('id'), status: $self.val()}
        });

        req.done(function(res) {
          if (res.status) {
            flashMessenger.setType(FlashMessenger.SUCCESS).add(res.message).getMessages();
            window.setTimeout(function() {
              window.location.reload();
            }, 500);
          } else {
            flashMessenger.setType(FlashMessenger.ERROR).add(res.message).getMessages();
          }
        });

        req.fail(function() {
          flashMessenger.setType(FlashMessenger.ERROR).add("Ocorreu um erro ao alterar o status do pedido")
            .getMessages();
        });
      });

      $modal.find('[data-dismiss="modal"]').off().on('click', function() {
        $self.val($self.data('status'));
        $modal.modal('hide');
      });
    },

    cancelOrder: function() {
      var $modal = $("#cancelarPedido");
      var $self = $(this);

      $modal.modal('show');
      $modal.find('button.btn-primary').off().on('click', function() {
        var req = $.ajax({
          url: BASE_URL + "pedido/cancelar",
          type: 'post',
          dataType: 'json',
          data: {id: $self.data('id')}
        });

        req.done(function(res) {
          if (res.status) {
            flashMessenger.setType(FlashMessenger.SUCCESS).add(res.message).getMessages();
            window.setTimeout(function() {
              window.location.reload();
            }, 500);
          } else {
            flashMessenger.setType(FlashMessenger.ERROR).add(res.message).getMessages();
          }
        });

        req.fail(function() {
          flashMessenger.setType(FlashMessenger.ERROR).add("Ocorreu um erro ao cancelar o pedido")
            .getMessages();
        });
      });
    },

    lancamento: function() {
      alert("MODAL LANÇAMENTO");
    },

    fotos: function() {
      alert("Fotos");
    }
  }
};

$(document).ready(function() {
  crud.init();
});