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

      $modal.find('p b').html($self.find('option:selected').html());
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
            }, 1000);
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
      var $modal = $('#lancamentos');
      var $self = $(this);

      var req = $.ajax({
        url: BASE_URL + 'pedido/lancamento-item',
        type: 'POST',
        dataType: 'json',
        data: {id: $self.parent().data('id')}
      });

      req.done(function(res) {
        if (res.status) {
          var $list = rendererList(
            res.list,
            "#row-lancamentos",
            null
          );

          var $modalTemplate = renderer(
            {
              idString: res.list[0]['idString'],
              id: $self.parent().data('id'),
              status_item_id: $self.parent().data('status')
            },
            "#modal-template-lancamento",
            null
          );

          $modalTemplate.find('.js-target-lancamentos').html($list);
          $modal.html($modalTemplate);
          $modal.modal('show');

          $modal.find('.js-new-lancamento').off().on('click', crud.event.novoLancamento);

          $('[data-format="price"]').off().priceFormat({
            prefix: '',
            centsSeparator: ',',
            thousandsSeparator: '.',
            clearOnEmpty: true
          });
        } else {
          flashMessenger.setType(FlashMessenger.ERROR)
            .add(res.message)
            .getMessages();
        }
      });

      req.fail(function() {
        flashMessenger.setType(FlashMessenger.ERROR)
          .add("Ocorreu um erro ao abrir o modal de lançamento")
          .getMessages();
      });
    },

    novoLancamento: function() {
      var id = $(this).data('id');
      var $self = $(this);
      var $body = $self.closest('.modal-body');
      var desc = $body.find('input[name="descricao"]').val();
      var valor = $body.find('input[name="valor"]').val();
      var isValid = true;

      $body.find('input[name="descricao"]').removeClass('is-invalid');
      if (desc == "") {
        $body.find('input[name="descricao"]').addClass('is-invalid');
        isValid = false;
      }

      $body.find('input[name="valor"]').removeClass('is-invalid');
      if (valor == "") {
        $body.find('input[name="valor"]').addClass('is-invalid');
        isValid = false;
      }

      if (!isValid) {
        return false;
      }

      var req = $.ajax({
        url: BASE_URL + 'pedido/salvar-lancamento-item',
        type: 'POST',
        dataType: 'json',
        data: {id: id, valor: toFloat(valor), descricao: desc}
      });

      req.done(function(res) {
        if (res.status) {
          flashMessenger.setType(FlashMessenger.SUCCESS)
            .add(res.message)
            .getMessages();

          $('.modal').modal('hide');
          window.setTimeout(function() {
            window.location.reload();
          }, 1000);
        } else {
          flashMessenger.setType(FlashMessenger.ERROR)
            .add(res.message)
            .getMessages();
        }
      });

      req.fail(function() {
        flashMessenger.setType(FlashMessenger.ERROR)
          .add("Ocorreu um erro ao salvaro lançamento. Verifique!")
          .getMessages();
      });
    },

    fotos: function() {
      alert("Fotos");
    }
  }
};

$(document).ready(function() {
  crud.init();
});