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
    errorMessageClass: '.error-message',

    ascText: 'asc',
    descText: 'desc',
    sortUpIconClass: 'pl-1 fas fa-sort-up',
    sortDownIconClass: 'pl-1 fas fa-sort-down',
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
    currentSortDirection: SELECTORS.ascText,
}

$(function () {
    loadSearchedListUsersData(searchCriteriaBody);

    $(SELECTORS.modalCreateForm).on('submit', function (e) {
        e.preventDefault();

        $.ajax({
            type: "POST",
            url: ROUTING.createUser,
            data: $(this).serialize(),

            success: function () {
                onSuccess(SELECTORS.modalCreate);
            },

            error: function (jqXHR) {
                onError(jqXHR);
            }
        });
    });

    $(SELECTORS.modalUpdateForm).on('submit', function (e) {
        e.preventDefault();
        $.ajax({
            type: "PUT",
            url: ROUTING.updateUser,
            data: $(this).serialize(),

            success: function () {
                onSuccess(SELECTORS.modalUpdate);
            },

            error: function (jqXHR) {
                onError(jqXHR);
            }
        });
    });

    $(SELECTORS.modalDeleteForm).on('submit', function (e) {
        e.preventDefault();
        const deleteEmail = $(SELECTORS.deleteTargetEmail).val();

        $.ajax({
            url: ROUTING.deleteUser,
            type: "DELETE",
            data: { email: deleteEmail },

            success: function () {
                onSuccess(SELECTORS.modalDelete);
            },

            error: function (jqXHR) {
                onError(jqXHR);
            }
        });
    });

    $(SELECTORS.tableSearchBtn).on('input', function () {
        clearTimeout(timeoutId);
        const searchCriteria = $(SELECTORS.tableSearchBtn).val();

        searchCriteriaBody.lastSearchValue = searchCriteria;
        searchCriteriaBody.lastPageIndex = 1;

        timeoutId = setTimeout(function () {
            loadSearchedListUsersData(searchCriteriaBody);
        }, 3000);
    });
});

const onSuccess = (modalSelector) => {
    $(modalSelector).modal('hide');
    $(SELECTORS.errorMessageClass).text('');
    loadSearchedListUsersData(searchCriteriaBody);
}

const onError = (jqXHR) => {
    const errors = jqXHR.responseJSON;
    $(SELECTORS.errorMessageClass).text('');

    for (let field in errors) {
        $('#' + field + '-error').text(errors[field]);
    }
}

const sortTable = (columnName) => {
    const lastCol = getTableHeaderColumnSelector(searchCriteriaBody.lastSortColumn);
    lastCol.children('i').remove();

    if (columnName === searchCriteriaBody.lastSortColumn) {
        searchCriteriaBody.currentSortDirection =
            searchCriteriaBody.currentSortDirection === SELECTORS.ascText ?
                SELECTORS.descText : SELECTORS.ascText;
    } else {
        searchCriteriaBody.lastSortColumn = columnName;
        searchCriteriaBody.currentSortDirection = SELECTORS.ascText;
    }

    loadSearchedListUsersData(searchCriteriaBody);
};

const changeArrowDirection = (columnName) => {
    const currentCol = getTableHeaderColumnSelector(columnName);
    const arrowIcon = document.createElement('i');

    arrowIcon.className = searchCriteriaBody.currentSortDirection === SELECTORS.ascText ?
        SELECTORS.sortUpIconClass : SELECTORS.sortDownIconClass;

    currentCol.append(arrowIcon);
}

const getTableHeaderColumnSelector = (col) => $(`th[sort-column='${col}']`);

const movingToPage = (pageNumber) => {
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
            changeArrowDirection(searchCriteriaBody.lastSortColumn);
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
