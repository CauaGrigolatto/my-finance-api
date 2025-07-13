$(document).ready(function() {
	loadTransactions();
	loadUnpagedCategories();

	$(document).on('click', '#transactions-tab', function() {		
		loadTransactions();
	});
	
	$(document).on('click', '#categories-tab', function() {		
		loadCategories();
	});
		
	$('#addTransactionModal #btn-save-transaction').on('click', function() {
		saveTransaction();
	});

	$('#addCategoryModal #btn-save-category').on('click', function() {
		saveCategory();
	});

	$('#filter-transactions-form #btn-filter').on('click', function() {
		$(document).find('#transactions-tab').click();
		loadTransactions();
	});
	
	$(document).on('click', '.btn-delete-transaction', function() {
		Swal.fire({
			title: 'Tem certeza?',
			text: "Você não poderá reverter isto!",
			icon: 'warning',
			background: '#1e1e1e',
			color: '#f1f5f9', // cor clara para texto (quase branco/cinza bem claro)
			showCancelButton: true,
			confirmButtonColor: '#3b82f6', // azul (botão principal)
			cancelButtonColor: '#ef4444',  // vermelho (botão cancelar)
			confirmButtonText: 'Sim, quero continuar!',
			cancelButtonText: 'Cancelar'
		}).then((result) => {
			if (result.isConfirmed) {
				const id = $(this).data('id');
				deleteTransaction(id);
			}
		})
		
	});
	
	$(document).on('click', '.btn-delete-category', function() {
		const id = $(this).data('id');
		deleteCategory(id);
	});
	
	$(document).on('click', '.btn-edit-transaction', function() {
		const id = $(this).data('id');
		
		$.ajax({
			url: 'http://localhost:15433/myfinanceapi/transaction/' + id,
			method: 'GET'
		})
		.done(function(response) {
			const transaction = response.data;
			
			$('#addTransactionModal').modal('show');
			
			const modal = $('#addTransactionModal');
			const form = modal.find('#save-transaction-form');
			form.attr('data-method', 'PUT');
			form.attr('data-id', transaction.id);
			form.find('#description').val(transaction.description);
			form.find('#value').val(transaction.value);
			form.find('#type').val(transaction.type);
			form.find('#category').val(transaction.category?.id);
			form.find('#dueDate').val(transaction.dueDate);
		})
		.fail(function() {
			
		});
	});
	
	$(document).on('click', '#add-transaction-modal', function() {
		const modal = $('#addTransactionModal');
		const form = modal.find('#save-transaction-form');
		form.attr('data-method', 'POST');
		form.attr('data-id', -1);
	});
	
	$(document).on('hidden.bs.modal', '#addTransactionModal', function() {
		const form = $(this).find('#save-transaction-form');
		form[0].reset();
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
		setPaginationIndexes(response, loadTransactions);
		loadFinancesSummary();
		transactionsPerCategoryPieChart();
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
	listItem = createTransactionListItem(transaction);
	$('#transaction-list .list-group').append(listItem);
}

function createTransactionListItem(transaction) {
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
                            <li><button class="dropdown-item btn-edit-transaction" href="#" data-id="${transaction.id}"><i class="bi bi-pencil me-2"></i>Editar</button></li>
                            <li><button class="dropdown-item text-danger btn-delete-transaction" data-id="${transaction.id}"><i class="bi bi-trash me-2"></i>Excluir</button></li>
                        </ul>
                    </div>
                </div>
            </div>
        </div>
    `);

	return listItem;
}

function clearCategoriesList() {
	$('#categories-list .list-group').empty();
}

function listCategories(categories) {
	$(categories).each(function() {
		appendCategory($(this)[0]);
	});
}

function appendCategory(category) {
	listItem = createCategoryListItem(category);
	$('#categories-list .list-group').append(listItem);
}

function createCategoryListItem(category) {
    const listItem = $(`
        <div class="list-group-item category-item bg-transparent text-light border-0 border-bottom mb-2">
            <div class="d-flex w-100 justify-content-between align-items-center">
                <div>
                    <h6 class="mb-1">${category.title}</h6>
                </div>
                <div class="text-end">
                    <button class="btn btn-sm btn-outline-danger btn-delete-category" 
                            data-id="${category.id}" 
                            title="Excluir categoria">
                        <i class="bi bi-trash"></i>
                    </button>
                </div>
            </div>
        </div>
    `);

    return listItem;
}

function setPaginationIndexes(apiResponse, callbackLoadPagination) {
	const pagination = $('.pagination');
	pagination.empty(); // Limpa a paginação existente

	const ul = $('<ul class="pagination justify-content-center"></ul>');

	// Botão "Anterior"
	const prevItem = $('<li class="page-item"></li>');
	if (apiResponse.page === 1) {
		prevItem.addClass('disabled');
	}
	prevItem.append(
		$('<button class="page-link" aria-label="Previous"></button>')
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
		$('<button class="page-link" href="#" aria-label="Next"></button>')
			.append($('<span aria-hidden="true">&raquo;</span>'))
			.append($('<span class="sr-only">Próxima</span>'))
	);
	ul.append(nextItem);

	pagination.append(ul);
	setTransactionIndexesButtons(apiResponse, callbackLoadPagination);
}

function setTransactionIndexesButtons(apiResponse, callbackLoadPagination) {
	$('.page-link').on('click', function(e) {
		e.preventDefault();
		const page = $(this).data('page') ||
			($(this).attr('aria-label') === 'Next' ? apiResponse.page + 1 : apiResponse.page - 1);

		if (page >= 1 && page <= apiResponse.totalPages && page !== apiResponse.page) {
			callbackLoadPagination(page);
		}
	});
}

function saveTransaction() {
	const modal = $('#addTransactionModal');
	const form = modal.find('#save-transaction-form');
	const method = form.attr('data-method');
	
	let url = 'http://localhost:15433/myfinanceapi/transaction';
	
	if (method === 'PUT') {
		const id = form.attr('data-id');
		url = 'http://localhost:15433/myfinanceapi/transaction/' + id
	}

	const formData = {};
	$.each(form.serializeArray(), function(i, field) {
		formData[field.name] = field.value;
	});

	const jsonData = JSON.stringify(formData);

	$.ajax({
		url: url,
		data: jsonData,
		method: method,
		contentType: 'application/json',
		dataType: 'json'
	})
	.done(function() {
		modal.modal('hide');
		$(document).find('#transactions-tab').click();
	})
	.fail(function() {

	});
}

function loadCategories(page = 1) {
	$.ajax({
		url: 'http://localhost:15433/myfinanceapi/category',
		data: {page: page, pageSize: 5},
		method: 'GET'
	})
	.done(function(response) {
		clearCategoriesList();
		listCategories(response.data);
		setPaginationIndexes(response, loadCategories);
	})
	.fail(function() {

	});
}

function loadUnpagedCategories() {
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

	selectCategories.empty().append('<option value="" selected>Selecione uma categoria</option>');

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
		loadUnpagedCategories();
	})
	.fail(function() {

	});
}

function loadFinancesSummary() {
	loadRevenues();
	loadExpenses();
	loadBalance();
}

function loadRevenues() {
	$.ajax({
		url: 'http://localhost:15433/myfinanceapi/transaction/revenues-sum',
		method: 'GET'
	})
	.done(function(response) {
		$('#revenues-sum').text(response.data);
	})
	.fail(function() {
		
	})
}

function loadExpenses() {
	$.ajax({
		url: 'http://localhost:15433/myfinanceapi/transaction/expenses-sum',
		method: 'GET'
	})
	.done(function(response) {
		$('#expenses-sum').text(response.data);
	})
	.fail(function() {
		
	})
}

function loadBalance() {
	$.ajax({
		url: 'http://localhost:15433/myfinanceapi/transaction/balance',
		method: 'GET'
	})
	.done(function(response) {
		$('#balance').text(response.data);
	})
	.fail(function() {
		
	})
}

function deleteTransaction(id) {
	$.ajax({
		url: 'http://localhost:15433/myfinanceapi/transaction/' + id,
		method: 'DELETE'
	})
	.done(function() {
		loadTransactions(1);
	})
	.fail(function() {
		
	})
}

function deleteCategory(id) {
	$.ajax({
		url: 'http://localhost:15433/myfinanceapi/category/' + id,
		method: 'DELETE'
	})
	.done(function() {
		loadCategories(1);
	})
	.fail(function() {
		
	})
}
