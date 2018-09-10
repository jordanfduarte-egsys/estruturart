var crud = {
  init: function() {
    this.binds();
  },

  binds: function() {
    crud.event.fullCalendar();
  },

  event: {
    fullCalendar: function(data = null) {
      var $calendar = $('.js-calendar');

      var post = {};
      $calendar.fullCalendar(confCalender);
      $('button.fc-today-button').on('click', function(e) {
        e.preventDefault();
        confCalender.events = [];
        eventsAdded = [];
        crud.event.today()
      });

      var request = $.ajax({
        url: BASE_URL + 'pedido/buscar',
        type: 'post',
        dataType: 'json',
        data: post
      });

      request.done(function(response) {
        confCalender.defaultDate = response.date;
        response.list.forEach(function(value, index) {
          if (eventsAdded.indexOf(value.id) == -1) {
            eventsAdded.push(value.id);
            confCalender.events.push({
              start: value.start,
              url: value.url.format(window.location.href.replace("/home", "/pedido")),
              title: value.title,
              color: value.color
            });
          }
        });
        console.log(confCalender);
        $calendar.fullCalendar('destroy');
        $calendar.fullCalendar(confCalender);

        $('button.fc-today-button').on('click', function(e) {
          e.preventDefault();
          confCalender.events = [];
          eventsAdded = [];
          crud.event.today()
        });
      });

      request.fail(function(response) {
        flashMessenger.setType(FlashMessenger.ERROR)
          .add("Ocorreu um erro ao consultar os pedidos")
          .getMessages();
      });
    },
  }
}

var eventsAdded = [];
var confCalender = {
  defaultView: 'agendaDay',
  locale: 'pt-br',
  displayEventTime: false,
  timezone:'local',
  header: {
    left: '',
    center: 'title',
    right: 'agendaDay'
  },
  eventClick: function(event) {
    if (event.url) {
      window.open(event.url);
      return false;
    }
  },
  eventRender: function (event, element) {
    element.find('.fc-title').html(event.title);
    element.find('.fc-list-item-title').html(event.title);
  },
  navLinks: true,
  editable: false,
  eventLimit: true,
  events: []
};

$(document).ready(function() {
  crud.init();
});
