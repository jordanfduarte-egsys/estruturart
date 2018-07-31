var crud = {
  init: function() {
    this.binds();
  },

  binds: function() {
    var $formSimples = $('.js-form-simples');
    var $formAvancado = $('.js-busca-avancada');

    $formAvancado.on("click", crud.event.toggleBuscaAvancada);
    $formAvancado.on("submit", function(e) {e.preventDefault(); crud.event.fullCalendar();});
    $formSimples.on("submit", function(e) {e.preventDefault(); crud.event.fullCalendar();});
    $('body').find('button[aria-label="next"]').on('click', crud.event.next);
    $('body').find('button[aria-label="prev"]').on('click', crud.event.prev);
    crud.event.fullCalendar();
  },

  event: {

    toggleBuscaAvancada: function (e) {
        e.preventDefault();
        $(".js-toggle-avancada").toggleClass('show');
    },

    fullCalendar: function(data = null) {
      var $calendar = $('.js-calendar');
      var $formSimples = $('.js-form-simples');
      var $formAvancado = $('.js-form-avancado');

      var post = $formSimples.serializeArray();
      post = $.extend(post, $formAvancado.serializeArray());
      post = serializeToSimplesParam(post);
      post.data_filtro = data;

      if (post.data_filtro) {
        data.data_ini = "";
      }

      $calendar.fullCalendar(confCalender);
      var request = $.ajax({
        url: BASE_URL + 'pedido/buscar',
        type: 'post',
        dataType: 'json',
        data: post
      });

      request.done(function(response) {
        confCalender.defaultDate = response.date;
        confCalender.events = [];
        response.list.forEach(function(value, index) {
          console.log(index, value);
          confCalender.events.push({
            start: value.start,
            url: value.url.format(window.location.href),
            title: value.title,
            color: value.color,
            textEscape: false
          });
        });
        console.log(confCalender);
        $calendar.fullCalendar('destroy');
        $calendar.fullCalendar(confCalender);
      });

      /**
       * {
    //   title: 'Click for Google',
    //   url: 'http://google.com/',
    //   start: '2018-03-28',
    //   end:  '2018-03-28',
    //   color: '#CCC' atrasado
    // }
       */

      request.fail(function(response) {
        flashMessenger.setType(FlashMessenger.ERROR)
          .add("Ocorreu um erro ao consultar os pedidos")
          .getMessages();
      });
    },

    prev: function() {
      // pegar dia atual e menos um
      var $calendar = $('.js-calendar');
      var date = $calendar.fullCalendar('getDate');
      var novaData = "{0}/{1}/{2}".format('01', date.month() + 1, date.year());
      crud.event.fullCalendar(novaData);
    },

    next: function() {
      // pegar dia atual e mais um
      var $calendar = $('.js-calendar');
      var date = $calendar.fullCalendar('getDate');
      var novaData = "{0}/{1}/{2}".format('01', date.month() + 1, date.year());
      crud.event.fullCalendar(novaData);
    }
  }
}

var confCalender = {
  // defaultView: 'listWeek',
  locale: 'pt-br',
  displayEventTime: false,
  header: {
    left: 'prev,next today',
    center: 'title',
    right: 'month,agendaWeek,agendaDay,listWeek'
  },
  eventClick: function(event) {
    if (event.url) {
      window.open(event.url);
      return false;
    }
  },
  //  defaultDate: '2018-03-12',
  navLinks: true, // can click day/week names to navigate views
  editable: false,
  eventLimit: true, // allow "more" link when too many events
  events: [
  ]
};

$(document).ready(function() {
  crud.init();
});
