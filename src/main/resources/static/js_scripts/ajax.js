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

    colName: `th[sort-column='${columnName}']`,
    nameRowOnTable: 'td:nth-child(2)',
    emailRowOnTable: 'td:nth-child(3)',
    errorMessageClass: '.error-message',

};

const ROUTING = {
    createUser: '/users-ajax/save',
    updateUser: '/users-ajax/update',
    deleteUser: '/users-ajax/delete',
    loadListSearchedUsers: '/users-ajax/search-data',

};

let timeoutId = null;

let searchCriteriaBody = {
    lastSearchValue: '',
    lastPageIndex: 1,
    lastSortColumn: 'id',
    currentSortDirection: 'asc',
}


$(function () {
    loadSearchedListUsersData(searchCriteriaBody);
    changeArrowDirection(searchCriteriaBody.lastSortColumn);

    $(SELECTORS.modalCreateForm).on('submit', function (e) {
        e.preventDefault();

        $.ajax({
            type: "POST",
            url: ROUTING.createUser,
            data: $(this).serialize(),

            success: function (data) {
                $(SELECTORS.modalCreate).modal('hide');
                $(SELECTORS.errorMessageClass).text('');
                loadSearchedListUsersData(searchCriteriaBody);
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
            loadSearchedListUsersData(searchCriteriaBody);
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
            loadSearchedListUsersData(searchCriteriaBody);
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

    searchCriteriaBody.lastSearchValue = searchCriteria;
    searchCriteriaBody.lastPageIndex = 1;

    timeoutId = setTimeout(function () {
        loadSearchedListUsersData(searchCriteriaBody);
    }, 3000);
});

let sortTable = (columnName) => {
    if (columnName === searchCriteriaBody.lastSortColumn) {
        searchCriteriaBody.currentSortDirection = searchCriteriaBody.currentSortDirection === 'asc' ? 'desc' : 'asc';
    } else {
        searchCriteriaBody.lastSortColumn = columnName;
        searchCriteriaBody.currentSortDirection = 'asc';
    }

    changeArrowDirection(columnName);

    loadSearchedListUsersData(searchCriteriaBody);
};

let changeArrowDirection = (columnName) => {
    let lastCol = $(SELECTORS.colName(searchCriteriaBody.lastSortColumn));
    let currentCol = $(SELECTORS.colName(columnName));
    let arrowIcon = document.createElement('i');

    arrowIcon.className = searchCriteriaBody.currentSortDirection === 'asc' ? 'fas fa-sort-up' : 'fas fa-sort-down';
    lastCol.find('i').remove();
    currentCol.appendChild(arrowIcon);
}

let movingToPage = (pageNumber) => {
    searchCriteriaBody.lastPageIndex = pageNumber;

    loadSearchedListUsersData(searchCriteriaBody);
}

function loadSearchedListUsersData(requestBody) {
    $.ajax({
        url: ROUTING.loadListSearchedUsers,
        type: "POST",
        contentType: 'application/json; charset=utf-8',
        dataType: 'HTML',
        data: JSON.stringify(requestBody),

        success: function (data) {
            $(SELECTORS.listUsersTable).html(data);
        }
    });
}

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
