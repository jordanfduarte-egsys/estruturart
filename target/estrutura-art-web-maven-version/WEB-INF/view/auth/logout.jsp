<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<!DOCTYPE HTML>
<html lang="en">
    <head>
        <meta charset="utf-8">
        <meta name="viewport" content="width=device-width, initial-scale=1, shrink-to-fit=no">
        <meta http-equiv="Content-Type" content="text/html;charset=utf-8" />
        <link rel="icon" type="image/png" href="${source}files/favicon.png" />
        <title>Logout</title>
    </head>
        <p>Desconectando...</p>

        <script src="${source}js/jquery.min.js"></script>
        <script>
            if (window.document.cookie.match(/login/)) {
                var d = new Date();
                d.setTime(d.getTime() - (1000 * 60 * 60 * 24));
                var expiris = "expires=" + d.toGMTString();
                window.document.cookie = "login=";
            }

            $(document).ready(function() {
                setTimeout(function() {
                   window.location.href = window.location.href.replace("/logout", "");
                }, 1500);
            });
        </script>
    </body>
</html>
