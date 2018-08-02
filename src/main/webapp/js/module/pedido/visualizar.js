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
      if (confirm("Deseja alterar o status do pedido para X ?")) {
        alert("OK");
      } else {
        $(this).val($(this).data('original-status'));
      }
    },

    cancelOrder: function() {
      alert("CANCEL ORDER");
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