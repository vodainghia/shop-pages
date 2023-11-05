let timeoutId = null;

$(function () {
    loadListUsersData(1);

    $('#modal-create form').on('submit', function (e) {
        e.preventDefault();
        $.ajax({
            type: "POST",
            url: "/users-ajax/save",
            data: $(this).serialize(),

            success: function (data) {
                $('#modal-create').modal('hide');
                $('.error-message').text('');
                selectLoadDataMethod(getCurrentPageByActiveClass());
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
            selectLoadDataMethod(getCurrentPageByActiveClass());
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
            selectLoadDataMethod(getCurrentPageByActiveClass());
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

$('#table_search').on('input', function () {
    clearTimeout(timeoutId);
    let searchCriteria = $('#table_search').val();

    timeoutId = setTimeout(function () {
        loadSearchedListUsersData(searchCriteria, 1);
    }, 3000);
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

let loadListUsersData = function (pageIndex) {
    $.ajax({
        url: "/users-ajax/list-data",
        method: "GET",
        data: { pageIndex: pageIndex },
        dataType: "HTML",

        success: function (data) {
            $('#user_table').html(data);
        }
    });
}

let loadSearchedListUsersData = function (keyword, pageIndex) {
    $.ajax({
        url: "/users-ajax/search-data",
        type: "POST",
        dataType: "HTML",
        data: { searchCriteria: keyword, pageIndex: pageIndex },

        success: function (data) {
            $('#user_table').html(data);
        }
    });
}

let selectLoadDataMethod = pageNumber => {
    let searchValue = $('#table_search').val();

    !searchValue ? loadListUsersData(pageNumber)
        : loadSearchedListUsersData(searchValue, pageNumber);
};

let getCurrentPageByActiveClass = () => parseInt($('.page-item.active').text(), 10);