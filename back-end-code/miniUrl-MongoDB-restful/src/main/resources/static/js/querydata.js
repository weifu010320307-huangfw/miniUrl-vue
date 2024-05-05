const csrfToken= $("meta[name='_csrf']").attr("content");
const csrfHeader= $("meta[name='_csrf_header']").attr("content");


$('#userdata').DataTable({
        "processing": true,
        "serverSide": true,
        "ajax": {
            "url": "/userdata",

            "type": "POST",
            "beforeSend": function(request){
                if(csrfToken && csrfHeader)
                {
                    request.setRequestHeader(csrfHeader, csrfToken);
                }

            },
            "dataType": "json",
            "contentType": "application/json",
            "data": function (d) {
                return JSON.stringify(d);
            }
        },
        "columns": [
            {"data": "shortUrl", "width": "20%"},
            {"data": "longUrl","width": "50%", render: $.fn.dataTable.render.text()},
            {"data": "shortenTimes", "width": "10%"},
            {"data": "accessTimes", "width": "10%"},
            {"data": "belongUser", "width": "10%"},

        ]
    });