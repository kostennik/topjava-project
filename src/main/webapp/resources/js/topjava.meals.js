$(function () {
    makeEditable({
            ajaxUrl: "ajax/meals/",
            datatableApi: $("#datatable").DataTable({
                "paging": false,
                "info": true,
                "columns": [
                    {
                        "data": "dateTime"
                    },
                    {
                        "data": "description"
                    },
                    {
                        "data": "calories"
                    },
                    {
                        "defaultContent": "Edit",
                        "orderable": false
                    },
                    {
                        "defaultContent": "Delete",
                        "orderable": false
                    }
                ],
                "order": [
                    [
                        0,
                        "desc"
                    ]
                ]
            })
        }
    );
});

function setDefaultValues() {
    var tzoffset = (new Date()).getTimezoneOffset() * 60000; //offset in milliseconds
    var localISOTime = (new Date(Date.now() - tzoffset)).toISOString().slice(0, 16);
    $('#dateTime').val(localISOTime);
    $("#calories").val("1000");
}

function updateTable() {
    var formData = $("#filter").serializeArray();
    $.ajax({
        type: "GET",
        url: context.ajaxUrl + "filter",
        data: formData
    }).done(function (data) {
        context.datatableApi.clear().rows.add(data).draw();
    });
}

function clearFilter() {
    $("#filter")[0].reset();
    updateTable();
}