function apiTest() {
    AppPath = "http://localhost:8080/";

    $.ajax({
        url: '/testApi/test',
        data: {},
        dataType: 'json',
        contentType: 'application/json; charset=utf-8',
        type: "POST",
        async: true,
        success: function (resultData) {
            if (resultData.da === "200") {

                $("#newsfeed").append(resultData.transactRes);

                return false;
            }
            else {
                alertify.alert(resultData.Errmsg);
                return false;
            }
        },
        error: function () {
            alert('Error occured while saving offer recipes ');
        }
    });