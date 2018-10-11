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
    var $foto

    $btBack.on('click', crud.event.action);
    $btnEditar.on('click', crud.event.action);
    $printOp.on('click', crud.event.actionOpen);
    $status.on('change', crud.event.changeStatus);
    $cancelarOrder.on('click', crud.event.cancelOrder);
    $lancamento.on('click', crud.event.lancamento);
    $fotos.on('click', crud.event.fotos);
    $('body').on('click', '.js-next-view', crud.event.nextFoto);
    $('body').on('click', '.js-prev-view', crud.event.prevFoto);
    $('body').on('change', 'input[name="nova_foto"]', crud.event.onFileSelecionado);
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
              idString: $self.parent().data('idstring'),
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
          .add("Ocorreu um erro ao salvar o lançamento. Verifique!")
          .getMessages();
      });
    },

    fotos: function() {
      var $modal = $('#fotos');
      var $self = $(this);

      var req = $.ajax({
        url: BASE_URL + 'pedido/fotos-item',
        type: 'POST',
        dataType: 'json',
        data: {id: $self.parent().data('id')}
      });

      req.done(function(res) {
        if (res.status) {
          var $list = rendererList(
            res.list,
            "#row-fotos",
            null
          );

          var $modalTemplate = renderer(
            {
              idString: $self.parent().data('idstring'),
              id: $self.parent().data('id'),
              status_item_id: $self.parent().data('status'),
              total: res.list.length
            },
            "#modal-template-fotos",
            null
          );

          if (res.list.length > 0) {
            $modalTemplate.find('.js-target-fotos').html($list);
          }
          $modal.html($modalTemplate);
          $modal.modal('show');

          $modal.find('.js-new-foto').off().on('click', crud.event.novoFoto);
        } else {
          flashMessenger.setType(FlashMessenger.ERROR)
            .add(res.message)
            .getMessages();
        }
      });

      req.fail(function() {
        flashMessenger.setType(FlashMessenger.ERROR)
          .add("Ocorreu um erro ao abrir o modal de fotos")
          .getMessages();
      });
    },

    nextFoto: function() {
      var el = document.querySelector('.js-target-fotos');
      if (el.scrollLeft += 40 <= $('.js-target-fotos').width()) {
        el.scrollLeft += 50;
      }
    },

    prevFoto: function() {
      var el = document.querySelector('.js-target-fotos');
      if (el.scrollLeft - 40 > 0) {
        el.scrollLeft -= 50;
      } else if (el.scrollLeft < 40) {
        el.scrollLeft = 0;
      }
    },

    novoFoto: function() {
      var id = $(this).data('id');
      var $self = $(this);
      var $body = $self.closest('.modal-body');
      var obs = $body.find('input[name="observacao"]').val();
      var file = $body.find('input[name="nova_foto"]').val();
      var isValid = true;

      // $body.find('input[name="observacao"]').removeClass('is-invalid');
      // if (obs == "") {
      //   $body.find('input[name="observacao"]').addClass('is-invalid');
      //   isValid = false;
      // }

      $body.find('input[name="nova_foto"]').removeClass('is-invalid');
      if (file == "" || file == null) {
        $body.find('input[name="nova_foto"]').addClass('is-invalid');
        isValid = false;
      }

      if (!isValid) {
        return false;
      }

      var formData = new FormData();
      formData.append('id', id);
      formData.append('obs', obs);
      formData.append('foto', $body.find('input[name="nova_foto"]').get(0).files[0]);

      var req = $.ajax({
        url: BASE_URL + 'pedido/salvar-foto-item',
        type: 'POST',
        data: formData,
        contentType: false,
        processData: false,
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
          .add("Ocorreu um erro ao salvar a foto. Verifique!")
          .getMessages();
      });
    },

    onFileSelecionado: function(e) {
      if (e.target.files.length == 0) {
          flashMessenger.setType(FlashMessenger.ERROR).add("Nenhum Arquivo Selecionado").getMessages();
          $(this).val('');
          return false;
      }

      if (e.target.files[0].size / 1024 / 1024 > 2) {
          flashMessenger.setType(FlashMessenger.ERROR).add("Arquivo deve ser menor que 2 MB").getMessages();
          $(this).val('');
          return false;
      }

      if (["image/jpeg", "image/jpg", "image/png", "image/bmp"].indexOf(e.target.files[0].type) == -1) {
          flashMessenger.setType(FlashMessenger.ERROR).add("Extensão do arquivo enviada inválida. Verifique!").getMessages();
          $(this).val('');
          return false;
      }
    }
  }
};

$(document).ready(function() {
  crud.init();
});