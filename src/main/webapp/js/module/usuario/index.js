var crud = {
    init: function () {
        this.binds();
    },

    binds: function () {
        $(".js-busca-avancada").on("click", crud.events.toggleBuscaAvancada);
        $("input[name=tipo_pessoa]").on("click", crud.events.tootlgeTipoPessoa);
        $(".js-alterar-status").on("click", crud.events.changeStatus);
    },

    events: {
      changeStatus: function() {
        var id = $(this).data("id");
        var status = $(this).data("status");
        var arrStatus = [];

        arrStatus.push({id: 0, name: "Selecione"});
        if (status ==  1) {
          arrStatus.push({id: 2, name: "Inativar"});
          arrStatus.push({id: 3, name: "Bloquear"});
        } else if (status ==  2) {
          arrStatus.push({id: 1, name: "Ativar"});
          arrStatus.push({id: 3, name: "Bloquear"});
        } else if (status ==  3) {
          arrStatus.push({id: 2, name: "Inativar"});
          arrStatus.push({id: 1, name: "Ativar"});
        }

        var $modal = $('#modal-alterar-status');
        var $s = $modal.find("select");
        $s.html("");
        arrStatus.forEach(function(e, v) {
          $s.append(new Option(e.name, e.id));
        });

        $modal.modal("show");

        $modal.find(".btn-primary").on('click', function(e) {
          e.preventDefault();

          if (+$s.val() == 0) {
            flashMessenger.setType(FlashMessenger.ERROR)
              .add("Selecioe o status do usuário")
              .getMessages();

              return false;
          }

          $.ajax({
            type: "POST",
            dataType: "json",
            data: {status: $s.val(), id: id},
            url: BASE_URL + "usuario/alterar-status",
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
                  .add("Ocorreu um erro ao alterar o status do usuário. Verifique!")
                  .getMessages();
              }
            },
            fail: function() {
              flashMessenger.setType(FlashMessenger.ERROR)
                .add("Ocorreu um erro ao alterar o status do usuário. Verifique!")
                .getMessages();
            }
          })
        });
      },

      toggleBuscaAvancada: function (e) {
          e.preventDefault();

          $(".js-toggle-avancada").toggleClass('show');
      }
    }
};

$(document).ready(function () {
    crud.init();
});