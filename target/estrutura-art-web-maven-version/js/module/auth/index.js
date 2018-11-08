var crud = {
  init: function() {
    this.binds();
  },

  binds: function() {
    $('.js-reset-pass').on('click', function(e) {
      e.preventDefault();
      $('.js-change-login').addClass('hide');
      $('.js-change-alterar-senha').removeClass('hide');
      $('.js-change-alterar-senha').find('input[name=name]').val($('.js-change-login').find('input[name=name]').val());
    });

    $('.js-back-reset').on('click', function(e) {
      e.preventDefault();
      $('.js-change-login').removeClass('hide');
      $('.js-change-alterar-senha').addClass('hide');
      $('.js-change-alterar-senha').find('input[name=name]').val('');
    });

    $('.js-enviar-email').on('click', function(e) {
      e.preventDefault();
      var val = $(this).closest('form').find('input[name=name]').val();
      if (!val.match(/^\w+([-+.']\w+)*@\w+([-.]\w+)*\.\w+([-.]\w+)*$/)) {
        flashMessenger.setType(FlashMessenger.ERROR).add("Informe um e-mail válido!").getMessages();
        return false;
      }

      $.ajax({
        type: 'POST',
        url: BASE_URL + 'auth/recuperar',
        dataType: 'json',
        data: {email: val},
        success: function(req) {
          if (req.status) {
            flashMessenger.setType(FlashMessenger.SUCCESS).add(req.message).getMessages();
            $('.js-back-reset').trigger('click');
          } else {
            flashMessenger.setType(FlashMessenger.ERROR).add(req.message).getMessages();
          }
        },
        fail: function() {
          flashMessenger.setType(FlashMessenger.ERROR).add("Ocorreu um erro ao recuperar o email").getMessages();
        }
      });
    });
  }
};

$(document).ready(function() {
  crud.init();
});