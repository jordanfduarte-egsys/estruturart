var APPEND = "APPEND";
var PREPEND = "PREPEND";

function getTemplate(templateNome) {
    return document.getElementById(templateNome.replace(".", "").replace(/#/g, "")).innerHTML;
}
function renderer(data, templateNome, $el, mode = APPEND) {
    var template = null;

    var $template = getTemplate(templateNome);
    $template = mapper(data, $template, '');

    var $elTo = $($template);
    if ($el != null) {
      if (mode == APPEND) {
          $el.append($elTo);
          $elTo.hide().show('slow');
      } else {
          $el.prepend($elTo);
      }
    }

    return $elTo;
}

function rendererList(data, templateNome, $el, mode = APPEND) {
    var template = null;

    var $templateAux = getTemplate(templateNome);
    var $template = '';
    for (var i = 0; i < Object.keys(data).length; i++) {
        var value = data[i];
        $template = $template + mapper(value, $templateAux, '');
    }

    var $elTo = $($template);
    if ($el != null) {
      if (mode == APPEND) {
          $el.append($elTo);
          $elTo.hide().show('slow');
      } else {
          $el.prepend($elTo);
      }
    }

    return $elTo;
}

function mapper(data, $template, keyAdjust) {
    Object.keys(data).map(function(objectKey) {
        var value = data[objectKey];
        if (typeof value == 'object' && !Array.isArray(value)) {
            $template = mapper(value, $template, objectKey);
        } else {
            if (keyAdjust != '') {
                objectKey = keyAdjust + "." + objectKey;
            }

            var arrSearch = $template.match(new RegExp("(({{" + objectKey + ")((\\s))(\\w*)(}}))|({{" + objectKey + "}})", "g"));
            for (index in arrSearch) {
                var arrSplitFn = arrSearch[index].replace("{{", "").replace("}}", "").split(" ");
                if (arrSplitFn.length == 2) {
                    value = window[arrSplitFn[1]](value);
                }

                $template = $template.replace(arrSearch[index], value);
            }

        }
    });

    return $template;
}

