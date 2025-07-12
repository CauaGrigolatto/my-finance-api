$(document).ready(function() {
	loadTransactions();
	loadCategories();

	$('#addTransactionModal #btn-save-transaction').on('click', function() {
		saveTransaction();
	});

	$('#addCategoryModal #btn-save-category').on('click', function() {
		saveCategory();
	});

	$('#filter-transactions-form #btn-filter').on('click', function() {
		loadTransactions();
	});
});

function loadTransactions(page = 1) {
	const filterForm = $('#filter-transactions-form');
	const data = filterForm.serializeArray();
	data.push({ name: 'page', value: page });
	data.push({ name: 'pageSize', value: 5 });

	$.ajax({
		url: 'http://localhost:15433/myfinanceapi/transaction',
		data: data,
		method: 'GET'
	})
	.done(function(response) {
		clearTransactionsList();
		listTransactions(response.data);
		setPaginationIndexes(response);
	})
	.fail(function() {

	});
}

function clearTransactionsList() {
	$('#transaction-list .list-group').empty();
}

function listTransactions(transactions) {
	$(transactions).each(function() {
		appendTransaction($(this)[0]);
	});
}

function appendTransaction(transaction) {
	listItem = functionCreateTransactionListItem(transaction);
	$('#transaction-list .list-group').append(listItem);
}

function functionCreateTransactionListItem(transaction) {
	const itemClass = transaction.type === 'REVENUE' ? 'revenue-item' : 'expense-item';
	const textColor = transaction.type === 'REVENUE' ? 'text-success' : 'text-warning';
	const valuePrefix = transaction.type === 'REVENUE' ? '' : '- ';

	const formattedValue = valuePrefix + 'R$ ' + parseFloat(transaction.value).toFixed(2).replace('.', ',');

	const listItem = $(`
        <div class="list-group-item transaction-item ${itemClass} bg-transparent text-light border-0 border-bottom mb-2">
            <div class="d-flex w-100 justify-content-between align-items-center">
                <div>
                    <h6 class="mb-1">${transaction.description}</h6>
                    <div class="d-flex flex-column">
                        <small class="text-muted">Categoria: ${transaction.category?.title || 'Sem categoria'}</small>
                        <small class="text-muted">Vencimento: ${transaction.dueDate}</small>
                    </div>
                </div>
                <div class="text-end">
                    <span class="fw-bold ${textColor}">${formattedValue}</span>
                    <div class="dropdown d-inline-block ms-3">
                        <button class="btn btn-sm btn-outline-secondary dropdown-toggle" type="button" data-bs-toggle="dropdown">
                            <i class="bi bi-three-dots-vertical"></i>
                        </button>
                        <ul class="dropdown-menu dropdown-menu-end">
                            <li><a class="dropdown-item edit-btn" href="#" data-id="${transaction.id}"><i class="bi bi-pencil me-2"></i>Editar</a></li>
                            <li><a class="dropdown-item text-danger delete-btn" href="#" data-id="${transaction.id}"><i class="bi bi-trash me-2"></i>Excluir</a></li>
                        </ul>
                    </div>
                </div>
            </div>
        </div>
    `);

	return listItem;
}
function setPaginationIndexes(apiResponse) {
	const pagination = $('.pagination');
	pagination.empty(); // Limpa a paginação existente

	const ul = $('<ul class="pagination justify-content-center"></ul>');

	// Botão "Anterior"
	const prevItem = $('<li class="page-item"></li>');
	if (apiResponse.page === 1) {
		prevItem.addClass('disabled');
	}
	prevItem.append(
		$('<a class="page-link" href="#" aria-label="Previous"></a>')
			.append($('<span aria-hidden="true">&laquo;</span>'))
			.append($('<span class="sr-only">Anterior</span>'))
	);
	ul.append(prevItem);

	// Números das páginas
	for (let i = 1; i <= apiResponse.totalPages; i++) {
		const pageItem = $('<li class="page-item"></li>');
		if (i === apiResponse.page) {
			pageItem.addClass('active');
		}
		pageItem.append(
			$('<a class="page-link" href="#"></a>')
				.text(i)
				.data('page', i)
		);
		ul.append(pageItem);
	}

	// Botão "Próxima"
	const nextItem = $('<li class="page-item"></li>');
	if (apiResponse.page === apiResponse.totalPages) {
		nextItem.addClass('disabled');
	}
	nextItem.append(
		$('<a class="page-link" href="#" aria-label="Next"></a>')
			.append($('<span aria-hidden="true">&raquo;</span>'))
			.append($('<span class="sr-only">Próxima</span>'))
	);
	ul.append(nextItem);

	pagination.append(ul);
	setIndexButtons(apiResponse);
}

function setIndexButtons(apiResponse) {
	$('.page-link').on('click', function(e) {
		e.preventDefault();
		const page = $(this).data('page') ||
			($(this).attr('aria-label') === 'Next' ? apiResponse.page + 1 : apiResponse.page - 1);

		if (page >= 1 && page <= apiResponse.totalPages && page !== apiResponse.page) {
			loadTransactions(page);
		}
	});
}

function saveTransaction() {
	const modal = $('#addTransactionModal');
	const form = modal.find('#save-transaction-form');

	const formData = {};
	$.each(form.serializeArray(), function(i, field) {
		formData[field.name] = field.value;
	});

	const jsonData = JSON.stringify(formData);

	$.ajax({
		url: 'http://localhost:15433/myfinanceapi/transaction',
		data: jsonData,
		method: 'POST',
		contentType: 'application/json',
		dataType: 'json'
	})
	.done(function() {
		form[0].reset();
		modal.modal('hide');
		loadTransactions(1);
	})
	.fail(function() {

	});
}

function loadCategories() {
	$.ajax({
		url: 'http://localhost:15433/myfinanceapi/category',
		data: { unpaged: true },
		method: 'GET'
	})
	.done(function(response) {
		setCategoriesModalAddTransaction(response.data);
		setCategoriesFilter(response.data);
	})
	.fail(function() {

	});
}

function setCategoriesFilter(categories) {
	const form = $('#filter-transactions-form');
	const selectCategories = form.find('#category');

	selectCategories.empty().append('<option value="" selected>Todas</option>');

	$(categories).each(function(index, category) {
		selectCategories.append(
			$('<option></option>')
				.val(category.id)
				.text(category.title)
		);
	});
}

function setCategoriesModalAddTransaction(categories) {
	const modal = $('#addTransactionModal');
	const selectCategories = modal.find('#category');

	selectCategories.empty().append('<option value="" selected disabled>Selecione uma categoria</option>');

	$(categories).each(function(index, category) {
		selectCategories.append(
			$('<option></option>')
				.val(category.id)
				.text(category.title)
		);
	});
}

function saveCategory() {
	const modal = $('#addCategoryModal');
	const form = modal.find('#save-category-form');

	const formData = {};
	$.each(form.serializeArray(), function(i, field) {
		formData[field.name] = field.value;
	});

	const jsonData = JSON.stringify(formData);

	$.ajax({
		url: 'http://localhost:15433/myfinanceapi/category',
		data: jsonData,
		method: 'POST',
		contentType: 'application/json',
		dataType: 'json'
	})
	.done(function() {
		form[0].reset();
		modal.modal('hide');
		loadCategories();
	})
	.fail(function() {

	});
}
