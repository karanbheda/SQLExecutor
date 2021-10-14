var cachedData = [];
var totalRecords = 0;
var records = 50;
var pageNo = 1;

$("#erDiagButton").click(function () {
    $("#sqlOutput").hide();
    $("#erDiag").show();
});

$("input[type='radio']").click(function () {
    if ($(this).val() === '1') {
        PostFunction('changeDbServer?name=mysql');
    }
    else if ($(this).val() === '2') {
        PostFunction('changeDbServer?name=rds');
    }

    setTimeout(() => {
        GetFunction('getDbList');
    }, 250)
});

$("#theme").click(function () {
    if (document.documentElement.getAttribute("data-theme") == "dark") {
        document.documentElement.setAttribute("data-theme", "light");
    } else {
        document.documentElement.setAttribute("data-theme", "dark");
    }
});

$("#runButton").click(function () {
    $("#sqlOutput").show();
    $("#erDiag").hide();
});

$(document).ready(function () {
    GetFunction('getDbList');
    document.documentElement.setAttribute("data-theme", "dark");
});

function GetFunction(funcName) {
    var AppPath = '/queryApi/' + funcName;
    var model = '';
    $.ajax({
        url: AppPath,
        data: {},
        dataType: 'json',
        contentType: 'application/json; charset=utf-8',
        type: "GET",
        async: true,
        success: function (data) {

            for (var i = 0; i < data.length; i++) {
                if (data[i]["isSelected"] == true) {
                    model += "<option value=" + data[i]["value"] + " selected>" + data[i]["value"] + "</option>";
                }
                else {
                    model += "<option value=" + data[i]["value"] + ">" + data[i]["value"] + "</option>";
                }
            }
            $("#dbname").html(model);
            return false;

        },
        error: function () {
            console.log("error" + funcName)
        }
    })
}

function PostFunction(funcName) {
    var AppPath = '/queryApi/' + funcName;

    var ans = {
        query: $("#query").val(),
        page: 1,
        records: 50,
        fromCache: false
    };

    /*if($("#dbname").val() == "" || $("#dbname").length == 0)
    {
    alert("Please select Db Name");
    return false;
    }

    else if($("#query").val()== "" || $("#query").length == 0)
    {
      alert("Please enter a query");
      return false;
      }*/


    $.ajax({
        url: AppPath,
        data: JSON.stringify(ans),
        dataType: 'json',
        contentType: 'application/json; charset=utf-8',
        type: "POST",
        async: true,
        success: function (data) {
            return false;

        },
        error: function () {
            console.log("error" + funcName)
        }
    })

}

function refreshPages() {
    $('#pagination').pagination({
        dataSource: cachedData,
        pageSize: records,
        pageNumber: pageNo,
        showPrevious: false,
        showNext: false,
        autoHidePrevious: true,
        autoHideNext: true,
        callback: function (data, pagination) {
            pageNo = pagination.pageNumber
        },
        afterPageOnClick: function () {
            if (cachedData != undefined && cachedData.length > 0)
                SqlResult(pageNo);
        }
    })
}

function SqlResult(page) {
    var ans = {
        query: $("#query").val(),
        page: page,
        records: records,
        fromCache: false
    };

    $.ajax({
        url: '/queryApi/query',
        data: JSON.stringify(ans),
        dataType: 'json',
        contentType: 'application/json; charset=utf-8',
        type: "POST",
        async: true,
        success: function (obj) {
            if (obj != undefined && obj.data != undefined) {
                totalRecords = obj.totalRecords;
                console.log(totalRecords)
                cachedData = new Array(totalRecords);
                var table = "<thead><tr>";
                var j = 0;
                var cols = Object.keys(obj.data[0]);
                for (var i = 0; i < cols.length; i++) {
                    table += "<th>" + cols[i] + "</th>";
                }

                table += "</tr></thead><tbody>";

                for (var k = 0; k < obj.data.length; k++) {
                    table += '<tr>';
                    for (var j = 0; j < cols.length; j++) {
                        table += '<td title=' + (obj.data[k])[cols[j]] + '>' + (obj.data[k])[cols[j]] + '</td>';
                    }
                    table += '</tr>';
                }

                $("#result").html(table + "</tbody>");
                $("#exec-time").html("Execution time:" + obj.responseTime + " s")
                refreshPages();
            }

        },
        error: function () {
            alert('Error occured while saving offer recipes ');
        }
    })

}

$("#selectRecords").on("change", function () {
    records = parseInt($(this).val());
    SqlResult(pageNo)
})