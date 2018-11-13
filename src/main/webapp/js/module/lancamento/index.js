var crud = {
  init: function () {
      this.binds();
  },

  binds: function () {
      $(".js-excluir").on("click", crud.events.changeStatus);
  },

  events: {
    changeStatus: function() {
      var id = $(this).data("id");

      var $modal = $('#modal-cancelar-lancamento');
      var $p = $modal.find(".modal-body p b");
      $p.text(id);
      $modal.modal("show");

      $modal.find(".btn-primary").off('click').on('click', function(e) {
        e.preventDefault();

        $.ajax({
          type: "POST",
          dataType: "json",
          data: {id: id},
          url: BASE_URL + "lancamento/cancelar",
          success: function(res) {
            if (res.status) {
              flashMessenger.setType(FlashMessenger.SUCCESS)
                .add(res.message)
                .getMessages();
                $modal.modal('hide');

                window.setTimeout(function() {
                  window.location.reload();
                }, 1500);
            } else {
              flashMessenger.setType(FlashMessenger.ERROR)
                .add("Ocorreu um erro ao cancelar o lançamento. Verifique!")
                .getMessages();
            }
          },
          fail: function() {
            flashMessenger.setType(FlashMessenger.ERROR)
              .add("Ocorreu um erro ao cancelar o lançamento. Verifique!")
              .getMessages();
          }
        })
      });
    },
  }
};

$(document).ready(function () {
  crud.init();
});

