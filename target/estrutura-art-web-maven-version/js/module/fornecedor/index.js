var crud = {
  init: function () {
      this.binds();
  },

  binds: function () {
      $(".js-alterar-status").on("click", crud.events.changeStatus);
  },

  events: {
    changeStatus: function() {
      var id = $(this).data("id");
      var status = $(this).data("status");
      var alterarPara = 1;
      var message = "";

      if (status == 1) {
        message = "Deseja inativar o fornecedor ?";
        alterarPara = 2;
      } else {
        message = "Deseja ativar o fornecedor ?";
        alterarPara = 1;
      }

      var $modal = $('#modal-alterar-status');
      var $p = $modal.find(".modal-body p");
      $p.html("");
      $p.text(message);
      $modal.modal("show");

      $modal.find(".btn-primary").on('click', function(e) {
        e.preventDefault();

        $.ajax({
          type: "POST",
          dataType: "json",
          data: {status: alterarPara, id: id},
          url: BASE_URL + "fornecedor/alterar-status",
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
                .add("Ocorreu um erro ao alterar o status do fornecedor. Verifique!")
                .getMessages();
            }
          },
          fail: function() {
            flashMessenger.setType(FlashMessenger.ERROR)
              .add("Ocorreu um erro ao alterar o status do fornecedor. Verifique!")
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