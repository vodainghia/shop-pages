$(function () {
    loadListUsersData();

    $('#modal-create form').on('submit', function (e) {
        e.preventDefault();
        $.ajax({
            type: "POST",
            url: "/users-ajax/save",
            data: $(this).serialize(),

            success: function (data) {
                $('#modal-create').modal('hide');
                $('.error-message').text('');
                loadListUsersData();
            },

            error: function (jqXHR, textStatus, err) {
                let errors = jqXHR.responseJSON;
                $('.error-message').text('');

                for (let field in errors) {
                    $('#' + field + '-error').text(errors[field]);
                }
            }
        })
    });
});

$('#modal-update form').on('submit', function (e) {
    e.preventDefault();
    $.ajax({
        type: "PUT",
        url: "/users-ajax/update",
        data: $(this).serialize(),

        success: function (data) {
            $('#modal-update').modal('hide');
            $('.error-message').text('');
            loadListUsersData();
        },

        error: function (jqXHR, textStatus, err) {
            let errors = jqXHR.responseJSON;
            $('.error-message').text('');

            for (let field in errors) {
                $('#' + field + '-error').text(errors[field]);
            }
        }
    })
});

$('#modal-delete form').on('submit', function (e) {
    e.preventDefault();
    let deleteEmail = $("#delete-email").val();

    $.ajax({
        url: "/users-ajax/delete",
        type: "DELETE",
        data: { email: deleteEmail },

        success: function (data) {
            $('#modal-delete').modal('hide');
            $('.error-message').text('');
            loadListUsersData();
        },

        error: function (jqXHR, textStatus, err) {
            let errors = jqXHR.responseJSON;
            $('.error-message').text('');

            for (let field in errors) {
                $('#' + field + '-error').text(errors[field]);
            }
        }
    });
});

function clearContent(button) {
    $('#name').val('');
    $('#email').val('');
    $('#password').val('');
    $('#confirmPassword').val('');
    $('.error-message').text('');
};

function editBtnClick(button) {
    const targetRow = $(button).closest('tr');
    const targetFullname = targetRow.find('td:nth-child(2)').text();
    const targetEmail = targetRow.find('td:nth-child(3)').text();

    $('#update-name').val(targetFullname);
    $('#update-email').val(targetEmail);
    $('#targetEmail').val(targetEmail);
    $('#update-password').val('');
    $('#update-confirmPassword').val('');
    $('.error-message').text('');
}

function deleteBtnClick(button) {
    const targetRow = $(button).closest('tr');
    const deleteEmail = targetRow.find('td:nth-child(3)').text();

    $('#delete-email').val(deleteEmail);
    $('#delete-email-display').text(deleteEmail);
    $('.error-message').text('');
}

let loadListUsersData = function () {
    $.ajax({
        url: "/users-ajax/list-data",
        method: "POST",
        dataType: "HTML",

        success: function (data) {
            $('#user_table').html(data);
        }
    });
}
