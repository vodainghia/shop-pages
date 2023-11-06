const SELECTORS = {
    modalCreate: '#modal-create',
    modalCreateForm: '#modal-create form',
    modalUpdate: '#modal-update',
    modalUpdateForm: '#modal-update form',
    modalDelete: '#modal-delete',
    modalDeleteForm: '#modal-delete form',
    tableSearchBtn: '#table_search',
    listUsersTable: '#user_table',

    nameFieldCreate: '#name',
    emailFieldCreate: '#email',
    passwordFieldCreate: '#password',
    confirmPasswordFieldCreate: '#confirmPassword',

    nameFieldUpdate: '#update-name',
    emailFieldUpdate: '#update-email',
    emailTargetFieldUpdate: '#targetEmail',
    passwordFieldUpdate: '#update-password',
    confirmPasswordFieldUpdate: '#update-confirmPassword',

    deleteTargetEmail: '#delete-email',
    deleteTargetEmailDisplay: '#delete-email-display',

    nameRowOnTable: 'td:nth-child(2)',
    emailRowOnTable: 'td:nth-child(3)',
    pageItemActive: '.page-item.active',
    errorMessageClass: '.error-message',

};

const ROUTING = {
    createUser: '/users-ajax/save',
    updateUser: '/users-ajax/update',
    deleteUser: '/users-ajax/delete',
    loadListUsers: '/users-ajax/list-data',
    loadListSearchedUsers: '/users-ajax/search-data',

};

let timeoutId = null;

$(function () {
    loadListUsersData(1);

    $(SELECTORS.modalCreateForm).on('submit', function (e) {
        e.preventDefault();
        $.ajax({
            type: "POST",
            url: ROUTING.createUser,
            data: $(this).serialize(),

            success: function (data) {
                $(SELECTORS.modalCreate).modal('hide');
                $(SELECTORS.errorMessageClass).text('');
                selectLoadDataMethod(getCurrentPageByActiveClass());
            },

            error: function (jqXHR, textStatus, err) {
                let errors = jqXHR.responseJSON;
                $(SELECTORS.errorMessageClass).text('');

                for (let field in errors) {
                    $('#' + field + '-error').text(errors[field]);
                }
            }
        })
    });
});

$(SELECTORS.modalUpdateForm).on('submit', function (e) {
    e.preventDefault();
    $.ajax({
        type: "PUT",
        url: ROUTING.updateUser,
        data: $(this).serialize(),

        success: function (data) {
            $(SELECTORS.modalUpdate).modal('hide');
            $(SELECTORS.errorMessageClass).text('');
            selectLoadDataMethod(getCurrentPageByActiveClass());
        },

        error: function (jqXHR, textStatus, err) {
            let errors = jqXHR.responseJSON;
            $(SELECTORS.errorMessageClass).text('');

            for (let field in errors) {
                $('#' + field + '-error').text(errors[field]);
            }
        }
    })
});

$(SELECTORS.modalDeleteForm).on('submit', function (e) {
    e.preventDefault();
    let deleteEmail = $(SELECTORS.deleteTargetEmail).val();

    $.ajax({
        url: ROUTING.deleteUser,
        type: "DELETE",
        data: { email: deleteEmail },

        success: function (data) {
            $(SELECTORS.modalDelete).modal('hide');
            $(SELECTORS.errorMessageClass).text('');
            selectLoadDataMethod(getCurrentPageByActiveClass());
        },

        error: function (jqXHR, textStatus, err) {
            let errors = jqXHR.responseJSON;
            $(SELECTORS.errorMessageClass).text('');

            for (let field in errors) {
                $('#' + field + '-error').text(errors[field]);
            }
        }
    });
});

$(SELECTORS.tableSearchBtn).on('input', function () {
    clearTimeout(timeoutId);
    let searchCriteria = $(SELECTORS.tableSearchBtn).val();

    timeoutId = setTimeout(function () {
        loadSearchedListUsersData(searchCriteria, 1);
    }, 3000);
});

function clearContent(button) {
    $(SELECTORS.nameFieldCreate).val('');
    $(SELECTORS.emailFieldCreate).val('');
    $(SELECTORS.passwordFieldCreate).val('');
    $(SELECTORS.confirmPasswordFieldCreate).val('');
    $(SELECTORS.errorMessageClass).text('');
};

function editBtnClick(button) {
    const targetRow = $(button).closest('tr');
    const targetFullname = targetRow.find(SELECTORS.nameRowOnTable).text();
    const targetEmail = targetRow.find(SELECTORS.emailRowOnTable).text();

    $(SELECTORS.nameFieldUpdate).val(targetFullname);
    $(SELECTORS.emailFieldUpdate).val(targetEmail);
    $(SELECTORS.emailTargetFieldUpdate).val(targetEmail);
    $(SELECTORS.passwordFieldUpdate).val('');
    $(SELECTORS.confirmPasswordFieldUpdate).val('');
    $(SELECTORS.errorMessageClass).text('');
}

function deleteBtnClick(button) {
    const targetRow = $(button).closest('tr');
    const deleteEmail = targetRow.find(SELECTORS.emailRowOnTable).text();

    $(SELECTORS.deleteTargetEmail).val(deleteEmail);
    $(SELECTORS.deleteTargetEmailDisplay).text(deleteEmail);
    $(SELECTORS.errorMessageClass).text('');
}

function loadListUsersData(pageIndex) {
    $.ajax({
        url: ROUTING.loadListUsers,
        method: "GET",
        data: { pageIndex: pageIndex },
        dataType: "HTML",

        success: function (data) {
            $(SELECTORS.listUsersTable).html(data);
        }
    });
}

function loadSearchedListUsersData(keyword, pageIndex) {
    $.ajax({
        url: ROUTING.loadListSearchedUsers,
        type: "POST",
        dataType: "HTML",
        data: { searchCriteria: keyword, pageIndex: pageIndex },

        success: function (data) {
            $(SELECTORS.listUsersTable).html(data);
        }
    });
}

function selectLoadDataMethod(pageNumber) {
    let searchValue = $(SELECTORS.tableSearchBtn).val();

    !searchValue ? loadListUsersData(pageNumber)
        : loadSearchedListUsersData(searchValue, pageNumber);
};

let getCurrentPageByActiveClass = () => parseInt($(SELECTORS.pageItemActive).text(), 10);
