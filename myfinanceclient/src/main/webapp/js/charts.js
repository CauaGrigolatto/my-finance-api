$(document).ready(function() {
	transactionsPerCategoryPieChart();
});

function transactionsPerCategoryPieChart() {
	const filterForm = $('#filter-transactions-form');
	const data = filterForm.serializeArray();
	data.push({ name: 'unpaged', value: true });

	$.ajax({
		url: 'http://localhost:15433/myfinanceapi/transaction',
		data: data,
		method: 'GET'
	})
	.done(function(response) {
		buildPieChart(response.data);
		buildBarChart(response.data);
	})
	.fail(function() {

	});
}

function buildPieChart(transactions) {
	const stats = {};

	transactions.forEach(t => {
		const categoryTitle = t.category?.title || "Sem categoria";
		if (!stats[categoryTitle]) {
			stats[categoryTitle] = 0;
		}
		stats[categoryTitle] += 1; // conta 1 por transação
	});

	// Ordena categorias do maior para o menor
	const sortedStats = Object.entries(stats).sort((a, b) => b[1] - a[1]);

	const threshold = 1; // limite mínimo para mostrar separado
	const labels = [];
	const values = [];
	let outras = 0;

	sortedStats.forEach(([label, value]) => {
		if (value >= threshold) {
			labels.push(label);
			values.push(value);
		} else {
			outras += value;
		}
	});

	if (outras > 0) {
		labels.push("Outras");
		values.push(outras);
	}

	const backgroundColors = labels.map(() => getRandomColor());

	const ctx = document.getElementById('transactionsPerCategoryPieChart').getContext('2d');

	if (window.pieChartInstance) {
		window.pieChartInstance.destroy();
	}

	window.pieChartInstance = new Chart(ctx, {
		type: 'pie',
		data: {
			labels: labels,
			datasets: [{
				label: 'Total por Categoria',
				data: values,
				backgroundColor: backgroundColors
			}]
		},
		options: {
			plugins: {
				title: {
					display: true,
					text: 'Transações por Categoria',
					color: '#ffffff',
					font: {
						size: 18,
						weight: 'bold'
					},
					padding: {
						top: 10,
						bottom: 30
					}
				}
			}
		}
	});
}

function buildBarChart(transactions) {
	const stats = {};

	transactions.forEach(t => {
		const categoryTitle = t.category?.title || "Sem categoria";
		if (!stats[categoryTitle]) {
			stats[categoryTitle] = 0;
		}
		stats[categoryTitle] += 1;
	});

	// Ordena categorias do maior para o menor
	const sortedStats = Object.entries(stats).sort((a, b) => b[1] - a[1]);

	const threshold = 1; // limite mínimo para mostrar separado
	const labels = [];
	const values = [];
	let outras = 0;

	sortedStats.forEach(([label, value]) => {
		if (value >= threshold) {
			labels.push(label);
			values.push(value);
		} else {
			outras += value;
		}
	});

	if (outras > 0) {
		labels.push("Outras");
		values.push(outras);
	}

	const backgroundColors = labels.map(() => getRandomColor());

	const ctx = document.getElementById('transactionsPerCategoryBarChart').getContext('2d');

	if (window.barChartInstance) {
		window.barChartInstance.destroy();
	}

	window.barChartInstance = new Chart(ctx, {
		type: 'bar',
		data: {
			labels: labels,
			datasets: [{
				label: 'Total por Categoria',
				data: values,
				backgroundColor: backgroundColors
			}]
		},
		options: {
			scales: {
				y: {
					beginAtZero: true
				}
			},
			plugins: {
				title: {
					display: true,
					text: 'Transações por Categoria (Barras)',
					color: '#ffffff',
					font: {
						size: 18,
						weight: 'bold'
					},
					padding: {
						top: 10,
						bottom: 30
					}
				},
				legend: {
					display: false
				}
			}
		}
	});
}

function getRandomColor() {
	const letters = '0123456789ABCDEF';
	let color = '#';
	for (let i = 0; i < 6; i++) {
		color += letters[Math.floor(Math.random() * 16)];
	}
	return color;
}
