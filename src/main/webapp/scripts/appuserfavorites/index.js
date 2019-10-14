var MODE={};

$(function () {
    loadAndRenderFav();
});

function loadAndRenderFav() {
    return AjaxUtil.get("/api/appuserfavorite/findByUsername").complete(function (xhr) {
        if (xhr.status == 200) {
            var data = xhr.responseJSON;
            console.log(data);
            $('#divcontent').empty();
            data.sort((a, b) => (a.id > b.id) ? 1 : -1)
            $.each(data, function (i, v) {
                var eleDetail = $("#template").html()
                    .replaceAll("#NAME", v.name)
                    .replaceAll("#LINK", v.link)
                    .replaceAll("#DESC", v.description)
                    .replaceAll("#ID", v.id)
                $('#divcontent').append(eleDetail);
            });

            $(".content a[href^='http']").each(function() {
                $(this).css({
                    background: "url(http://www.google.com/s2/favicons?domain=" + this.hostname + ") left center no-repeat",
                    "padding-left": "30px"
                });
            });

        }
    });
}

function userfavoriteEdit(ele, id) {
    return AjaxUtil.get("/rest/appUserFavorites/" + id).complete(function (xhr) {
        if (xhr.status == 200) {
            var data = xhr.responseJSON;
            var $ele = $('#defaultModal');
            clearModal();
            $ele.find('[name="name"]').val(data.name);
            $ele.find('[name="link"]').val(data.link);
            $ele.find('[name="description"]').val(data.description);
            $ele.modal('show');
            MODE={
                status:"UPDATE",
                data:data,
            }

        }
    });
}

function userfavoriteDelete(ele, id) {
    return AjaxUtil.delete("/rest/appUserFavorites/" + id).complete(function (xhr) {
        if (xhr.status == 204) {
            loadAndRenderFav();
        }
    });
}


function clearModal() {
    $('#defaultModal').find('input,select,textarea').val('');
}
function openModal() {
    clearModal();
    $('#defaultModal').modal('show');
    MODE={
        status:"CREATE"
    }
}
$('#form_validation').validate({
    rules: {
        'checkbox': {
            required: true
        },
        'gender': {
            required: true
        }
    },
    highlight: function (input) {
        $(input).parents('.form-line').addClass('error');
    },
    unhighlight: function (input) {
        $(input).parents('.form-line').removeClass('error');
    },
    errorPlacement: function (error, element) {
        $(element).parents('.form-group').append(error);
    }
});

$('#form_validation').submit(function (e) {
    //prevent Default functionality
    e.preventDefault();
    var form = $(this);
    var url = form.attr('action');
    var data = getFormData(form);
    var validate = $('#form_validation').valid();
    if (validate) {
        console.log(data);
        data['username'] = session.user;
        if(MODE.status=="CREATE") {
            AjaxUtil.post("/rest/appUserFavorites", JSON.stringify(data)).complete(function (xhr) {
                if (xhr.status == 201) {
                    loadAndRenderFav().then(function (value) {
                        $('#defaultModal').modal('hide');
                    })
                }
            });
        }else if(MODE.status=="UPDATE"){
            AjaxUtil.put("/rest/appUserFavorites/"+MODE.data.id, JSON.stringify(data)).complete(function (xhr) {
                if (xhr.status == 200) {
                    loadAndRenderFav().then(function (value) {
                        $('#defaultModal').modal('hide');
                    })
                }
            });
        }
    }
});

