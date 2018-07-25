'use strict';
;(function (window, document, $) {

        function ShowLoading() {
            this.message = null;
            this.$messageDisplay = null;
            this.$overlay = null;
            this.$divAppend = null;

            this.setMessage = function($message) {
                this.message = $message;

                // Utilizado para trocar mensagem com o show loading ainda aberto
                if (this.$messageDisplay != null) {
                  this.__showMessage();
                }
                return this;
            };

            this.getMessage = function() {
                return this.message;
            };

            this.hide = function(callback) {
                if (typeof callback === "function") {
                  callback(this);
                } else {
                    var self = this;
                    window.setTimeout(function() {
                        self.$overlay.remove();
                    }, 300);
                }

                if (this.$divAppend != null) {
                    this.$divAppend.css({'position': 'inherit'});
                }

                return this;
            };

            this.setDivAppend = function($el) {
                this.$divAppend = $el;
                return this;
            };

            this.__showMessage = function() {
                this.$messageDisplay.css({
                    'top': '48%',
                    'left': '50%',
                    'position': 'absolute',
                    'padding-left': '10px',
                    'color': 'rgb(255, 255, 255)'
                });
                this.$messageDisplay.html(this.getMessage());
                this.$overlay.append(this.$messageDisplay);
            };

            this.show = function () {
                var docHeight = $(window).height();
                var img = $('<i/>', {
                  'class': 'fa fa-spinner fa-pulse fa-spin fa-3x'
                }).css({
                  'position': 'absolute',
                  'top': '50%',
                  'left': '50%',
                  'color': 'white',
                  'margin': '-20px -27px',
                  'font-size': '30px',
                });

                if ($('body').find("#overlay").length) {
                    this.$overlay = $('body').find("#overlay");

                    if (typeof this.getMessage() == "function" && this.getMessage() != null) {
                        this.$messageDisplay = $('<div/>');
                        this.__showMessage();
                    }
                } else {
                    this.$overlay = $('<div/>', {'id': 'overlay'});

                    if (typeof this.getMessage() == "function" && this.getMessage() != null) {
                        this.$messageDisplay = $('<div/>');
                        this.__showMessage();
                    }

                    if (this.$divAppend != null) {
                        this.$divAppend.append(this.$overlay.append(img));
                        this.$divAppend.css({'position': 'relative'});

                        $('#overlay')
                            .css({
                                'position': 'absolute',
                                'height': '100%',
                                'top': 0,
                                'left': 0,
                                'background-color': 'rgba(000, 000, 000, 0.5)',
                                'width': '100%',
                                'z-index': 5000
                            });
                    } else {
                        $('body').append(this.$overlay.append(img));
                        $('#overlay')
                        .css({
                            'position': 'fixed',
                            'height': '100%',
                            'top': 0,
                            'left': 0,
                            'background-color': 'rgba(000, 000, 000, 0.5)',
                            'width': '100%',
                            'z-index': 5000
                        });
                    }
                }

                return this;
            };
        }

    window.ShowLoading = ShowLoading;
})(window, document, jQuery);