var cachedData = [];
var totalRecords = 0;
var records = 50;
var pageNo = 1;
var message = "";

$("#erDiagButton").click(function () {
    $("#sqlOutput").hide();
    $("#erDiag").css("display","block");
});

$(".close").click(function () {
    $("#sqlOutput").show();
    $("#erDiag").css("display","none");
});

$("input[name='choice']").click(function () {
    $("#loader").show();
    $(".form-loader").show();

    PostFunction('changeDbServer?name=' + $(this).val(), {});

    var dbSv = GetFunction('getDbServer');
     $('#' + dbSv["value"]).prop('checked',true);
    setDbList();
    setTimeout(() => {
            $("#loader").hide();
            $(".form-loader").hide();
        }, 750)
});

$('select#dbname').on('change', function() {
  PostFunction('changeDb?name='+this.value);
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
    //$("#loader").show();
    $("#loader").show();
    $(".form-loader").show();

    var dbSv = GetFunction('getDbServer');
    $('#' + dbSv["value"]).prop('checked',true);
    setDbList();
    document.documentElement.setAttribute("data-theme", "dark");
    $("#loader").hide();
    $(".form-loader").hide();
});

function setDbList() {
  var data = GetFunction('getDbList');
  var model = ""
  for (var i = 0; i < data.length; i++) {
      if (data[i]["isSelected"] == true) {
          model += "<option value=" + data[i]["value"] + " selected>" + data[i]["value"] + "</option>";
      }
      else {
          model += "<option value=" + data[i]["value"] + ">" + data[i]["value"] + "</option>";
      }
  }
  $("#dbname").html(model);
}

function GetFunction(funcName) {
    var AppPath = '/queryApi/' + funcName;
    var json =''
    $.ajax({
        url: AppPath,
        data: {},
        dataType: 'json',
        contentType: 'application/json; charset=utf-8',
        type: "GET",
        async: false,
        success: function (data) {
          json = data;
        },
        error: function () {
            console.log("error" + funcName)
        }
    })
    return json
}

function PostFunction(funcName, input) {
    var AppPath = '/queryApi/' + funcName;
    var json = ''

    $.ajax({
        url: AppPath,
        data: JSON.stringify(input),
        dataType: 'json',
        contentType: 'application/json; charset=utf-8',
        type: "POST",
        async: false,
        success: function (data) {
            json =  data;
        },
        error: function () {
            console.log("error" + funcName)
        }
    })
  return json;
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
                SqlResult(pageNo, true);
        }
    })
}

function SqlResult(page, fromCache = false) {
    $("#loader").show();
    $(".form-loader").show();


    if ($("#dbname").val() == "" || $("#dbname").length == 0) {
        alert("Please select Database Name");
        $("#loader").hide();
        $(".form-loader").hide();
        return false;
    }
    else if ($("#query").val() == "" || $("#query").length == 0) {
        alert("Please enter a query");
        $("#loader").hide();
        $(".form-loader").hide();
        return false;
    }  

    var ans = {
        query: $("#query").val(),
        page: page,
        records: records,
        fromCache: fromCache
    };



    $.ajax({
        url: '/queryApi/query',
        data: JSON.stringify(ans),
        dataType: 'json',
        contentType: 'application/json; charset=utf-8',
        type: "POST",
        async: true,
        success: function (obj) {
            if (obj != undefined) {
                if(obj.data == undefined) {
                    $("#message").html(obj.message);
                    $("#message").show();
                    $("#result").hide();
                    $("#recordDiv").hide();
                    $("#pagination").hide();
                    $("#exec-time").hide();
                    return;
                }
                totalRecords = obj.totalRecords;
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
                        table += '<td title=\'' + (obj.data[k])[cols[j]] + '\'>' + (obj.data[k])[cols[j]] + '</td>';
                    }
                    table += '</tr>';
                }

                $("#result").html(table + "</tbody>");
                $("#message").hide();
                $("#result").show();
                $("#pagination").show();
                $("#recordDiv").show();
                $("#exec-time").html("Execution time:" + obj.responseTime + " s")
                $("#exec-time").show()
                refreshPages();
            }
            

        },
        error: function () {
            alert('Error');
        }
        
    })

    setTimeout(() => {
        $("#loader").hide();
        $(".form-loader").hide();
        }, 1000
    )

}

$("#selectRecords").on("change", function () {
    records = parseInt($(this).val());
    pageNo = 1;
    SqlResult(pageNo, true)
})