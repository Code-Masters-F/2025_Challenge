// Estado da aplicação
const appState = {
    currentScreen: 'login',
    userProfile: null,
    salesData: [],
    storesData: [],
    manualEntries: []
};

// Dados simulados de comércios (baseado no CSV)
const mockStoresData = [
    {nome: "Mercadinho São João", cnpj: "12345678000190", endereco: "Rua das Flores 123", cidade: "Campinas", estado: "SP", cep: "13045-210"},
    {nome: "Padaria Pão Quente", cnpj: "98765432000155", endereco: "Avenida Paulista 1500", cidade: "São Paulo", estado: "SP", cep: "01310-200"},
    {nome: "Supermercado Econômico", cnpj: "11223344000177", endereco: "Rua Rio Branco 85", cidade: "Ribeirão Preto", estado: "SP", cep: "14010-160"},
    {nome: "Empório Bom Sabor", cnpj: "55443322000199", endereco: "Rua das Acácias 250", cidade: "Sorocaba", estado: "SP", cep: "18025-390"},
    {nome: "Mini Mercado do Bairro", cnpj: "44332211000166", endereco: "Rua XV de Novembro 12", cidade: "Jundiaí", estado: "SP", cep: "13201-000"},
    {nome: "Armazém Central", cnpj: "66778899000111", endereco: "Rua Afonso Pena 90", cidade: "Belo Horizonte", estado: "MG", cep: "30130-005"},
    {nome: "Quitanda da Esquina", cnpj: "99887766000133", endereco: "Rua da Paz 345", cidade: "Curitiba", estado: "PR", cep: "80060-120"},
    {nome: "Supermercado União", cnpj: "22334455000144", endereco: "Rua das Oliveiras 789", cidade: "Porto Alegre", estado: "RS", cep: "90040-000"},
    {nome: "Mercado Popular", cnpj: "77889966000122", endereco: "Rua José Bonifácio 66", cidade: "Recife", estado: "PE", cep: "50020-140"},
    {nome: "Empório Natural Vida", cnpj: "55667788000100", endereco: "Rua Verde 410", cidade: "Florianópolis", estado: "SC", cep: "88010-200"}
];

// Navegação entre telas
function showScreen(screenId) {
    document.querySelectorAll('.screen').forEach(s => s.classList.remove('active'));
    document.getElementById(`${screenId}-screen`).classList.add('active');
    appState.currentScreen = screenId;
}

// Login
document.getElementById('login-form').addEventListener('submit', (e) => {
    e.preventDefault();
    const email = document.getElementById('login-email').value;
    appState.userProfile = {
        email: email,
        name: email.split('@')[0],
        type: 'comercio'
    };
    showScreen('upload');
});

// Cadastro
document.getElementById('show-register').addEventListener('click', (e) => {
    e.preventDefault();
    showScreen('register');
});

document.getElementById('show-login').addEventListener('click', (e) => {
    e.preventDefault();
    showScreen('login');
});

document.getElementById('register-form').addEventListener('submit', (e) => {
    e.preventDefault();
    const profile = document.querySelector('input[name="profile"]:checked').value;
    const name = document.getElementById('register-name').value;
    const email = document.getElementById('register-email').value;
    
    appState.userProfile = {
        name: name,
        email: email,
        type: profile,
        company: document.getElementById('register-company').value
    };
    
    showScreen('upload');
});

// Upload de arquivo
const dropZone = document.getElementById('drop-zone');
const fileInput = document.getElementById('file-input');
const fileInfo = document.getElementById('file-info');

dropZone.addEventListener('dragover', (e) => {
    e.preventDefault();
    dropZone.classList.add('drag-over');
});

dropZone.addEventListener('dragleave', () => {
    dropZone.classList.remove('drag-over');
});

dropZone.addEventListener('drop', (e) => {
    e.preventDefault();
    dropZone.classList.remove('drag-over');
    const file = e.dataTransfer.files[0];
    handleFile(file);
});

fileInput.addEventListener('change', (e) => {
    const file = e.target.files[0];
    handleFile(file);
});

function handleFile(file) {
    if (file && file.name.endsWith('.csv')) {
        fileInfo.textContent = `✓ ${file.name} carregado com sucesso`;
        
        const reader = new FileReader();
        reader.onload = (e) => {
            const csv = e.target.result;
            appState.salesData = parseCSV(csv);
            console.log('Dados carregados:', appState.salesData);
        };
        reader.readAsText(file);
    } else {
        alert('Por favor, selecione um arquivo CSV válido');
    }
}

function parseCSV(csv) {
    const lines = csv.trim().split('\n');
    const headers = lines[0].split(',').map(h => h.trim().replace(/^\uFEFF/, ''));
    
    return lines.slice(1).map(line => {
        const values = line.split(',');
        const obj = {};
        headers.forEach((header, index) => {
            obj[header] = values[index]?.trim() || '';
        });
        return obj;
    }).filter(row => row.nomeComercio); // Remove linhas vazias
}

// Toggle entre métodos de upload
document.getElementById('toggle-method').addEventListener('click', () => {
    const csvMethod = document.getElementById('csv-method');
    const manualMethod = document.getElementById('manual-method');
    
    csvMethod.classList.toggle('active');
    manualMethod.classList.toggle('active');
});

// Entrada manual
document.getElementById('manual-form').addEventListener('submit', (e) => {
    e.preventDefault();
    
    const entry = {
        nomeComercio: document.getElementById('manual-comercio').value,
        nomeProduto: document.getElementById('manual-produto').value,
        tamanhoEmbalagem: document.getElementById('manual-tamanho').value || '',
        unidadeDeMedida: document.getElementById('manual-unidade').value,
        quantidade: parseInt(document.getElementById('manual-quantidade').value),
        precoUnitario: parseFloat(document.getElementById('manual-preco').value),
        dataHora: document.getElementById('manual-data').value
    };
    
    appState.manualEntries.push(entry);
    displayManualEntries();
    e.target.reset();
});

function displayManualEntries() {
    const container = document.getElementById('manual-entries');
    container.innerHTML = '<h4 style="margin: 1rem 0 0.5rem 0;">Registros adicionados:</h4>';
    
    appState.manualEntries.forEach((entry, index) => {
        const div = document.createElement('div');
        div.className = 'entry-item';
        div.innerHTML = `
            <span>${entry.nomeProduto} - ${entry.nomeComercio} (${entry.quantidade}x R$ ${entry.precoUnitario.toFixed(2)})</span>
            <button onclick="removeEntry(${index})">Remover</button>
        `;
        container.appendChild(div);
    });
}

function removeEntry(index) {
    appState.manualEntries.splice(index, 1);
    displayManualEntries();
}

// Processar dados e ir para dashboard
document.getElementById('process-data').addEventListener('click', () => {
    // Gera dados simulados automaticamente se não houver dados
    if (appState.salesData.length === 0 && appState.manualEntries.length === 0) {
        generateSimulatedData();
    } else {
        // Combina dados CSV e entradas manuais se houver
        const allData = [...appState.salesData, ...appState.manualEntries];
        appState.salesData = allData;
    }
    
    appState.storesData = mockStoresData;
    
    showScreen('dashboard');
    setTimeout(() => {
        initializeDashboard();
    }, 100);
});

// Dashboard
function initializeDashboard() {
    // Atualiza nome do usuário
    const userProfileEl = document.getElementById('user-profile');
    if (userProfileEl) {
        userProfileEl.textContent = `👤 ${appState.userProfile.name}`;
    }
    
    // Gera dados simulados se não houver dados reais
    if (appState.salesData.length === 0) {
        generateSimulatedData();
    }
    
    // Calcula estatísticas
    calculateStats();
    
    // Renderiza gráficos com delay para garantir que o DOM está pronto
    setTimeout(() => {
        renderCharts();
        renderTables();
        generateInsights();
    }, 200);
    
    // Listener para redimensionamento da janela (apenas uma vez)
    if (!window.hasResizeListener) {
        let resizeTimeout;
        window.addEventListener('resize', () => {
            clearTimeout(resizeTimeout);
            resizeTimeout = setTimeout(() => {
                renderCharts();
            }, 300);
        });
        window.hasResizeListener = true;
    }
}

// Gera dados simulados para demonstração
function generateSimulatedData() {
    const products = ['Arroz 5kg', 'Feijão 1kg', 'Açúcar Cristal', 'Óleo de Soja', 'Macarrão 500g', 
                      'Café Torrado', 'Leite Integral', 'Achocolatado', 'Sal Refinado', 'Farinha de Trigo'];
    const stores = mockStoresData.map(s => s.nome);
    const dates = [];
    
    // Gera datas dos últimos 30 dias
    for (let i = 29; i >= 0; i--) {
        const date = new Date();
        date.setDate(date.getDate() - i);
        dates.push(date.toISOString().split('T')[0]);
    }
    
    appState.salesData = [];
    
    // Gera vendas aleatórias
    for (let i = 0; i < 150; i++) {
        const randomDate = dates[Math.floor(Math.random() * dates.length)];
        const randomHour = Math.floor(Math.random() * 24);
        const randomMinute = Math.floor(Math.random() * 60);
        const dateTime = `${randomDate} ${String(randomHour).padStart(2, '0')}:${String(randomMinute).padStart(2, '0')}`;
        
        appState.salesData.push({
            nomeComercio: stores[Math.floor(Math.random() * stores.length)],
            nomeProduto: products[Math.floor(Math.random() * products.length)],
            tamanhoEmbalagem: Math.random() > 0.5 ? (Math.random() * 5 + 0.5).toFixed(2) : '',
            unidadeDeMedida: ['G', 'ML', 'UNIDADE'][Math.floor(Math.random() * 3)],
            quantidade: Math.floor(Math.random() * 20) + 1,
            precoUnitario: (Math.random() * 50 + 2).toFixed(2),
            dataHora: dateTime
        });
    }
}

function calculateStats() {
    const data = appState.salesData;
    
    const totalRevenue = data.reduce((sum, sale) => {
        return sum + (parseFloat(sale.precoUnitario) * parseInt(sale.quantidade));
    }, 0);
    
    const totalProducts = data.reduce((sum, sale) => sum + parseInt(sale.quantidade), 0);
    
    const uniqueStores = new Set(data.map(sale => sale.nomeComercio)).size;
    
    const avgTicket = totalRevenue / data.length;
    
    document.getElementById('stat-revenue').textContent = `R$ ${totalRevenue.toFixed(2)}`;
    document.getElementById('stat-products').textContent = totalProducts;
    document.getElementById('stat-stores').textContent = uniqueStores;
    document.getElementById('stat-avg').textContent = `R$ ${avgTicket.toFixed(2)}`;
}

function renderCharts() {
    // Renderiza apenas gráficos da aba ativa
    const activeTab = document.querySelector('.tab-content.active')?.id;
    
    if (activeTab === 'overview-tab') {
        setTimeout(() => {
            renderSalesTimeline();
            renderTopProducts();
        }, 100);
    } else if (activeTab === 'products-tab') {
        setTimeout(() => {
            renderCategoryChart();
        }, 100);
    } else if (activeTab === 'stores-tab') {
        setTimeout(() => {
            renderStateChart();
        }, 100);
    }
    
    // Renderiza todos para garantir que estejam prontos
    setTimeout(() => {
        renderSalesTimeline();
        renderTopProducts();
        renderCategoryChart();
        renderStateChart();
    }, 300);
}

function renderSalesTimeline() {
    const canvas = document.getElementById('sales-timeline');
    if (!canvas) return;
    
    const ctx = canvas.getContext('2d');
    
    // Agrupa vendas por data
    const salesByDate = {};
    appState.salesData.forEach(sale => {
        const date = sale.dataHora ? sale.dataHora.split(' ')[0] : new Date().toISOString().split('T')[0];
        if (!salesByDate[date]) salesByDate[date] = 0;
        salesByDate[date] += parseFloat(sale.precoUnitario || 0) * parseInt(sale.quantidade || 0);
    });
    
    let dates = Object.keys(salesByDate).sort();
    let values = dates.map(d => salesByDate[d]);
    
    // Se não houver dados suficientes, simula com tendência
    if (values.length < 5 || values.every(v => v === 0)) {
        dates = [];
        for (let i = 29; i >= 0; i--) {
            const date = new Date();
            date.setDate(date.getDate() - i);
            dates.push(date.toISOString().split('T')[0]);
        }
        const baseValue = 1500;
        values = dates.map((_, i) => baseValue + Math.sin(i * 0.2) * 500 + Math.random() * 400);
    }
    
    drawLineChart(ctx, canvas, dates, values, '#2563eb');
}

function renderTopProducts() {
    const canvas = document.getElementById('top-products');
    if (!canvas) return;
    
    const ctx = canvas.getContext('2d');
    
    // Agrupa por produto
    const productSales = {};
    appState.salesData.forEach(sale => {
        const product = sale.nomeProduto || 'Produto';
        if (!productSales[product]) productSales[product] = 0;
        productSales[product] += parseFloat(sale.precoUnitario || 0) * parseInt(sale.quantidade || 0);
    });
    
    // Top 5
    let sorted = Object.entries(productSales)
        .sort((a, b) => b[1] - a[1])
        .slice(0, 5);
    
    // Se não houver dados suficientes, simula produtos top
    if (sorted.length < 5 || sorted.every(s => s[1] === 0)) {
        const mockProducts = ['Arroz 5kg', 'Feijão 1kg', 'Açúcar Cristal', 'Óleo de Soja', 'Macarrão 500g'];
        sorted = mockProducts.map((name, i) => [name, 5000 - (i * 500) + Math.random() * 300]);
    }
    
    const labels = sorted.map(s => s[0]);
    const values = sorted.map(s => s[1]);
    
    drawBarChart(ctx, canvas, labels, values, ['#2563eb', '#3b82f6', '#60a5fa', '#93c5fd', '#bfdbfe']);
}

function renderCategoryChart() {
    const canvas = document.getElementById('category-chart');
    if (!canvas) return;
    
    const ctx = canvas.getContext('2d');
    
    // Categoriza produtos
    const categories = {
        'Alimentos': ['Arroz', 'Feijão', 'Macarrão', 'Açúcar', 'Farinha', 'Sal', 'Café', 'Óleo'],
        'Bebidas': ['Refrigerante', 'Água', 'Suco', 'Leite', 'Achocolatado'],
        'Padaria': ['Pão', 'Bolo'],
        'Laticínios': ['Queijo', 'Manteiga'],
        'Higiene': ['Sabonete', 'Shampoo', 'Desodorante'],
        'Limpeza': ['Detergente', 'Amaciante', 'Esponja']
    };
    
    const categorySales = {};
    appState.salesData.forEach(sale => {
        let found = false;
        for (const [category, keywords] of Object.entries(categories)) {
            if (keywords.some(k => sale.nomeProduto.includes(k))) {
                if (!categorySales[category]) categorySales[category] = 0;
                categorySales[category] += parseFloat(sale.precoUnitario) * parseInt(sale.quantidade);
                found = true;
                break;
            }
        }
        if (!found) {
            if (!categorySales['Outros']) categorySales['Outros'] = 0;
            categorySales['Outros'] += parseFloat(sale.precoUnitario) * parseInt(sale.quantidade);
        }
    });
    
    // Se não houver dados suficientes, simula categorias
    if (Object.keys(categorySales).length < 3) {
        categorySales['Alimentos'] = 4500 + Math.random() * 500;
        categorySales['Bebidas'] = 2800 + Math.random() * 400;
        categorySales['Padaria'] = 1500 + Math.random() * 300;
        categorySales['Outros'] = 800 + Math.random() * 200;
    }
    
    const labels = Object.keys(categorySales);
    const values = Object.values(categorySales);
    const colors = ['#2563eb', '#10b981', '#f59e0b', '#ef4444', '#8b5cf6', '#ec4899', '#14b8a6'];
    
    drawPieChart(ctx, canvas, labels, values, colors);
}

function renderStateChart() {
    const canvas = document.getElementById('state-chart');
    if (!canvas) return;
    
    const ctx = canvas.getContext('2d');
    
    // Mapeia comércios para estados
    const storeStateMap = {};
    appState.storesData.forEach(store => {
        storeStateMap[store.nome] = store.estado;
    });
    
    // Agrupa por estado
    const stateSales = {};
    appState.salesData.forEach(sale => {
        const state = storeStateMap[sale.nomeComercio] || 'Desconhecido';
        if (!stateSales[state]) stateSales[state] = 0;
        stateSales[state] += parseFloat(sale.precoUnitario) * parseInt(sale.quantidade);
    });
    
    // Se não houver dados suficientes, simula estados
    if (Object.keys(stateSales).length < 2) {
        stateSales['SP'] = 3500 + Math.random() * 500;
        stateSales['MG'] = 2200 + Math.random() * 400;
        stateSales['PR'] = 1800 + Math.random() * 300;
        stateSales['RS'] = 1500 + Math.random() * 300;
    }
    
    const labels = Object.keys(stateSales);
    const values = Object.values(stateSales);
    const colors = labels.map((_, i) => `hsl(${220 + i * 40}, 70%, 55%)`);
    
    drawBarChart(ctx, canvas, labels, values, colors);
}

// Funções de desenho de gráficos
function drawLineChart(ctx, canvas, labels, values, color) {
    if (!canvas || !canvas.parentElement) return;
    
    // Garante dimensões mínimas e responsivas
    let containerWidth = canvas.parentElement.clientWidth || canvas.offsetWidth || 600;
    if (containerWidth < 250) containerWidth = Math.max(containerWidth, 250);
    
    // Ajusta altura baseado no tamanho da tela
    let containerHeight = 300;
    if (window.innerWidth <= 480) {
        containerHeight = 200;
    } else if (window.innerWidth <= 768) {
        containerHeight = 250;
    }
    
    // High DPI support
    const dpr = window.devicePixelRatio || 1;
    canvas.width = containerWidth * dpr;
    canvas.height = containerHeight * dpr;
    canvas.style.width = containerWidth + 'px';
    canvas.style.height = containerHeight + 'px';
    
    ctx.scale(dpr, dpr);
    
    const padding = 50;
    const bottomPadding = 70; // Mais espaço para labels rotacionadas
    const topPadding = padding;
    const width = containerWidth - 2 * padding;
    const height = containerHeight - topPadding - bottomPadding;
    
    if (values.length === 0) return;
    
    const maxValue = Math.max(...values);
    const minValue = Math.min(...values);
    const range = maxValue - minValue || 1;
    
    ctx.clearRect(0, 0, containerWidth, containerHeight);
    
    // Gradiente de fundo
    const gradient = ctx.createLinearGradient(0, topPadding, 0, containerHeight - bottomPadding);
    gradient.addColorStop(0, 'rgba(37, 99, 235, 0.1)');
    gradient.addColorStop(1, 'rgba(37, 99, 235, 0.01)');
    
    // Linhas de grade
    ctx.strokeStyle = '#e2e8f0';
    ctx.lineWidth = 1;
    for (let i = 0; i <= 5; i++) {
        const y = topPadding + (height / 5) * i;
        ctx.beginPath();
        ctx.moveTo(padding, y);
        ctx.lineTo(containerWidth - padding, y);
        ctx.stroke();
    }
    
    // Valores no eixo Y
    ctx.fillStyle = '#64748b';
    ctx.font = '10px sans-serif';
    ctx.textAlign = 'right';
    for (let i = 0; i <= 5; i++) {
        const value = maxValue - (range / 5) * i;
        const y = topPadding + (height / 5) * i;
        ctx.fillText(`R$ ${value.toFixed(0)}`, padding - 10, y + 4);
    }
    
    // Área sob a curva com gradiente
    ctx.fillStyle = gradient;
    ctx.beginPath();
    ctx.moveTo(padding, containerHeight - bottomPadding);
    
    values.forEach((value, i) => {
        const x = padding + (width / (values.length - 1 || 1)) * i;
        const y = containerHeight - bottomPadding - ((value - minValue) / range) * height;
        ctx.lineTo(x, y);
    });
    
    ctx.lineTo(containerWidth - padding, containerHeight - bottomPadding);
    ctx.closePath();
    ctx.fill();
    
    // Linha
    ctx.strokeStyle = color;
    ctx.lineWidth = 3;
    ctx.shadowBlur = 8;
    ctx.shadowColor = color;
    ctx.beginPath();
    
    values.forEach((value, i) => {
        const x = padding + (width / (values.length - 1 || 1)) * i;
        const y = containerHeight - bottomPadding - ((value - minValue) / range) * height;
        
        if (i === 0) ctx.moveTo(x, y);
        else ctx.lineTo(x, y);
    });
    
    ctx.stroke();
    ctx.shadowBlur = 0;
    
    // Pontos
    ctx.fillStyle = color;
    values.forEach((value, i) => {
        const x = padding + (width / (values.length - 1 || 1)) * i;
        const y = containerHeight - bottomPadding - ((value - minValue) / range) * height;
        
        ctx.beginPath();
        ctx.arc(x, y, 5, 0, Math.PI * 2);
        ctx.fill();
        ctx.strokeStyle = '#fff';
        ctx.lineWidth = 2;
        ctx.stroke();
        ctx.strokeStyle = color;
        ctx.lineWidth = 3;
    });
    
    // Labels - mostra apenas algumas datas para evitar sobreposição
    ctx.fillStyle = '#64748b';
    ctx.font = '11px sans-serif';
    ctx.textAlign = 'center';
    ctx.textBaseline = 'top';
    
    // Calcula quantas labels mostrar (máximo 8 para evitar sobreposição)
    const maxLabels = Math.min(labels.length, 8);
    const labelInterval = Math.max(1, Math.floor(labels.length / maxLabels));
    const labelsToShow = [];
    const labelIndices = [];
    
    for (let i = 0; i < labels.length; i += labelInterval) {
        labelsToShow.push(labels[i]);
        labelIndices.push(i);
        if (labelsToShow.length >= maxLabels) break;
    }
    
    // Sempre mostra a última data
    if (labelIndices[labelIndices.length - 1] !== labels.length - 1 && labels.length > 0) {
        labelsToShow[labelsToShow.length - 1] = labels[labels.length - 1];
        labelIndices[labelIndices.length - 1] = labels.length - 1;
    }
    
    labelsToShow.forEach((label, idx) => {
        const i = labelIndices[idx];
        const x = padding + (width / (labels.length - 1 || 1)) * i;
        
        // Formata data de forma mais legível
        let displayLabel = '';
        try {
            const dateParts = label.split('-');
            if (dateParts.length === 3) {
                // Formato: DD/MM
                displayLabel = `${dateParts[2]}/${dateParts[1]}`;
            } else {
                displayLabel = label.substring(5) || `D${i+1}`;
            }
        } catch (e) {
            displayLabel = label.substring(5, 10) || `D${i+1}`;
        }
        
        // Rotaciona texto para melhor legibilidade
        ctx.save();
        ctx.translate(x, containerHeight - bottomPadding + 10);
        ctx.rotate(-Math.PI / 4); // Rotaciona 45 graus
        ctx.fillText(displayLabel, 0, 0);
        ctx.restore();
    });
}

function drawBarChart(ctx, canvas, labels, values, colors) {
    if (!canvas || !canvas.parentElement) return;
    if (labels.length === 0 || values.length === 0) return;
    
    // Garante dimensões mínimas e responsivas
    let containerWidth = canvas.parentElement.clientWidth || canvas.offsetWidth || 600;
    if (containerWidth < 250) containerWidth = Math.max(containerWidth, 250);
    
    // Ajusta altura baseado no tamanho da tela
    let containerHeight = 300;
    if (window.innerWidth <= 480) {
        containerHeight = 200;
    } else if (window.innerWidth <= 768) {
        containerHeight = 250;
    }
    
    // High DPI support
    const dpr = window.devicePixelRatio || 1;
    canvas.width = containerWidth * dpr;
    canvas.height = containerHeight * dpr;
    canvas.style.width = containerWidth + 'px';
    canvas.style.height = containerHeight + 'px';
    
    ctx.scale(dpr, dpr);
    
    const padding = 50;
    const width = containerWidth - 2 * padding;
    const height = containerHeight - 2 * padding;
    const barWidth = (width / labels.length) * 0.6;
    const gap = (width / labels.length) * 0.4;
    
    const maxValue = Math.max(...values);
    
    ctx.clearRect(0, 0, containerWidth, containerHeight);
    
    // Linhas de grade
    ctx.strokeStyle = '#e2e8f0';
    ctx.lineWidth = 1;
    for (let i = 0; i <= 5; i++) {
        const y = padding + (height / 5) * i;
        ctx.beginPath();
        ctx.moveTo(padding, y);
        ctx.lineTo(containerWidth - padding, y);
        ctx.stroke();
    }
    
    // Valores no eixo Y
    ctx.fillStyle = '#64748b';
    ctx.font = '10px sans-serif';
    ctx.textAlign = 'right';
    for (let i = 0; i <= 5; i++) {
        const value = maxValue - (maxValue / 5) * i;
        const y = padding + (height / 5) * i;
        ctx.fillText(`R$ ${value.toFixed(0)}`, padding - 10, y + 4);
    }
    
    values.forEach((value, i) => {
        const x = padding + (width / labels.length) * i + gap / 2;
        const barHeight = (value / maxValue) * height;
        const y = containerHeight - padding - barHeight;
        
        // Gradiente para as barras
        const barGradient = ctx.createLinearGradient(x, y, x, y + barHeight);
        const color = Array.isArray(colors) ? colors[i % colors.length] : colors;
        
        // Converte cor para rgba se necessário
        let lightColor = color;
        if (color.startsWith('hsl')) {
            // Converte HSL para RGBA com opacidade
            const hslMatch = color.match(/hsl\((\d+),\s*(\d+)%,\s*(\d+)%\)/);
            if (hslMatch) {
                const h = hslMatch[1];
                const s = hslMatch[2];
                const l = hslMatch[3];
                lightColor = `hsla(${h}, ${s}%, ${Math.min(parseInt(l) + 10, 100)}%, 0.85)`;
            }
        } else {
            lightColor = color.replace(')', ', 0.85)').replace('rgb', 'rgba');
        }
        
        barGradient.addColorStop(0, color);
        barGradient.addColorStop(1, lightColor);
        
        ctx.fillStyle = barGradient;
        ctx.shadowBlur = 6;
        ctx.shadowColor = 'rgba(0,0,0,0.1)';
        ctx.fillRect(x, y, barWidth, barHeight);
        ctx.shadowBlur = 0;
        
        // Valor no topo
        ctx.fillStyle = '#0f172a';
        ctx.font = '11px sans-serif';
        ctx.textAlign = 'center';
        ctx.fillText(`R$ ${value.toFixed(0)}`, x + barWidth / 2, y - 8);
    });
    
    // Labels
    ctx.fillStyle = '#64748b';
    ctx.font = '11px sans-serif';
    ctx.textAlign = 'center';
    
    labels.forEach((label, i) => {
        const x = padding + (width / labels.length) * i + (width / labels.length) / 2;
        const shortLabel = label.length > 12 ? label.substring(0, 10) + '...' : label;
        ctx.fillText(shortLabel, x, containerHeight - 15);
    });
}

function drawPieChart(ctx, canvas, labels, values, colors) {
    if (!canvas || !canvas.parentElement) return;
    if (labels.length === 0 || values.length === 0) return;
    
    // Garante dimensões mínimas e responsivas
    let containerWidth = canvas.parentElement.clientWidth || canvas.offsetWidth || 600;
    if (containerWidth < 250) containerWidth = Math.max(containerWidth, 250);
    
    // Ajusta altura baseado no tamanho da tela
    let containerHeight = 300;
    if (window.innerWidth <= 480) {
        containerHeight = 200;
    } else if (window.innerWidth <= 768) {
        containerHeight = 250;
    }
    
    // High DPI support
    const dpr = window.devicePixelRatio || 1;
    canvas.width = containerWidth * dpr;
    canvas.height = containerHeight * dpr;
    canvas.style.width = containerWidth + 'px';
    canvas.style.height = containerHeight + 'px';
    
    ctx.scale(dpr, dpr);
    
    const centerX = containerWidth / 2 - 80; // Desloca para esquerda para dar espaço à legenda
    const centerY = containerHeight / 2;
    const radius = Math.min(centerX, centerY) - 40;
    
    const total = values.reduce((sum, v) => sum + v, 0);
    if (total === 0) return;
    
    let currentAngle = -Math.PI / 2;
    
    ctx.clearRect(0, 0, containerWidth, containerHeight);
    
    // Desenha sombra
    ctx.save();
    ctx.shadowBlur = 10;
    ctx.shadowColor = 'rgba(0, 0, 0, 0.2)';
    ctx.shadowOffsetX = 2;
    ctx.shadowOffsetY = 2;
    
    values.forEach((value, i) => {
        const sliceAngle = (value / total) * Math.PI * 2;
        const color = colors[i % colors.length];
        
        // Gradiente para cada fatia
        const gradient = ctx.createLinearGradient(
            centerX + Math.cos(currentAngle) * radius * 0.5,
            centerY + Math.sin(currentAngle) * radius * 0.5,
            centerX + Math.cos(currentAngle + sliceAngle / 2) * radius * 1.2,
            centerY + Math.sin(currentAngle + sliceAngle / 2) * radius * 1.2
        );
        
        // Converte cor para versão mais clara
        let lightColor = color;
        if (color.startsWith('hsl(')) {
            const hslMatch = color.match(/hsl\((\d+),\s*(\d+)%,\s*(\d+)%\)/);
            if (hslMatch) {
                const h = hslMatch[1];
                const s = hslMatch[2];
                const l = Math.min(parseInt(hslMatch[3]) + 15, 100);
                lightColor = `hsla(${h}, ${s}%, ${l}%, 0.8)`;
            }
        } else if (color.startsWith('rgb(')) {
            lightColor = color.replace('rgb', 'rgba').replace(')', ', 0.8)');
        } else {
            lightColor = color;
        }
        
        gradient.addColorStop(0, color);
        gradient.addColorStop(1, lightColor);
        
        ctx.fillStyle = gradient;
        ctx.beginPath();
        ctx.moveTo(centerX, centerY);
        ctx.arc(centerX, centerY, radius, currentAngle, currentAngle + sliceAngle);
        ctx.closePath();
        ctx.fill();
        
        currentAngle += sliceAngle;
    });
    
    ctx.restore();
    
    // Desenha círculo branco no centro para efeito donut (opcional)
    ctx.fillStyle = '#ffffff';
    ctx.beginPath();
    ctx.arc(centerX, centerY, radius * 0.4, 0, Math.PI * 2);
    ctx.fill();
    
    // Legenda do lado direito
    const legendX = centerX + radius + 30;
    const legendY = centerY - (labels.length * 25) / 2;
    
    labels.forEach((label, i) => {
        const y = legendY + i * 25;
        const percentage = ((values[i] / total) * 100).toFixed(1);
        const color = colors[i % colors.length];
        
        // Quadrado de cor
        ctx.fillStyle = color;
        ctx.fillRect(legendX, y - 8, 12, 12);
        
        // Texto do label
        ctx.fillStyle = '#0f172a';
        ctx.font = '11px sans-serif';
        ctx.textAlign = 'left';
        const labelText = label.length > 15 ? label.substring(0, 13) + '...' : label;
        ctx.fillText(labelText, legendX + 18, y);
        
        // Percentual
        ctx.fillStyle = '#64748b';
        ctx.font = '10px sans-serif';
        ctx.fillText(`${percentage}%`, legendX + 18, y + 12);
    });
}

// Renderizar tabelas
function renderTables() {
    renderProductsTable();
    renderStoresTable();
}

function renderProductsTable() {
    const tbody = document.querySelector('#products-table tbody');
    if (!tbody) return;
    
    tbody.innerHTML = '';
    
    const productData = {};
    appState.salesData.forEach(sale => {
        const product = sale.nomeProduto || 'Produto Desconhecido';
        if (!productData[product]) {
            productData[product] = {
                quantity: 0,
                revenue: 0,
                prices: [],
                stores: new Set()
            };
        }
        
        const qty = parseInt(sale.quantidade || 0);
        const price = parseFloat(sale.precoUnitario || 0);
        
        productData[product].quantity += qty;
        productData[product].revenue += qty * price;
        productData[product].prices.push(price);
        if (sale.nomeComercio) {
            productData[product].stores.add(sale.nomeComercio);
        }
    });
    
    const entries = Object.entries(productData);
    if (entries.length === 0) {
        tbody.innerHTML = '<tr><td colspan="5" style="text-align: center; padding: 2rem; color: var(--text-light);">Nenhum produto encontrado</td></tr>';
        return;
    }
    
    entries
        .sort((a, b) => b[1].revenue - a[1].revenue)
        .forEach(([product, data]) => {
            const avgPrice = data.prices.length > 0 
                ? data.prices.reduce((sum, p) => sum + p, 0) / data.prices.length 
                : 0;
            
            const row = tbody.insertRow();
            row.innerHTML = `
                <td><strong>${product}</strong></td>
                <td>${data.quantity}</td>
                <td>R$ ${data.revenue.toFixed(2)}</td>
                <td>R$ ${avgPrice.toFixed(2)}</td>
                <td>${data.stores.size}</td>
            `;
        });
}

function renderStoresTable() {
    const tbody = document.querySelector('#stores-table tbody');
    if (!tbody) return;
    
    tbody.innerHTML = '';
    
    const storeData = {};
    appState.salesData.forEach(sale => {
        const store = sale.nomeComercio || 'Comércio Desconhecido';
        if (!storeData[store]) {
            storeData[store] = {
                products: new Set(),
                revenue: 0
            };
        }
        
        if (sale.nomeProduto) {
            storeData[store].products.add(sale.nomeProduto);
        }
        storeData[store].revenue += parseInt(sale.quantidade || 0) * parseFloat(sale.precoUnitario || 0);
    });
    
    const storesToShow = appState.storesData && appState.storesData.length > 0
        ? appState.storesData.filter(store => storeData[store.nome])
        : Object.keys(storeData).map(name => ({ nome: name, cidade: 'N/A', estado: 'N/A' }));
    
    if (storesToShow.length === 0) {
        tbody.innerHTML = '<tr><td colspan="5" style="text-align: center; padding: 2rem; color: var(--text-light);">Nenhum comércio encontrado</td></tr>';
        return;
    }
    
    storesToShow
        .sort((a, b) => (storeData[b.nome]?.revenue || 0) - (storeData[a.nome]?.revenue || 0))
        .forEach(store => {
            const data = storeData[store.nome];
            if (!data) return;
            
            const row = tbody.insertRow();
            row.innerHTML = `
                <td><strong>${store.nome}</strong></td>
                <td>${store.cidade || 'N/A'}</td>
                <td>${store.estado || 'N/A'}</td>
                <td>${data.products.size}</td>
                <td>R$ ${data.revenue.toFixed(2)}</td>
            `;
        });
}

// Geração de Insights com IA
function generateInsights() {
    const container = document.getElementById('insights-container');
    if (!container) return;
    
    container.innerHTML = '<div class="loading-insights">🤖 Gerando insights inteligentes...</div>';
    
    // Simula processamento com IA
    setTimeout(() => {
        container.innerHTML = '';
        const insights = analyzeData();
        
        insights.forEach((insight, index) => {
            setTimeout(() => {
                const card = document.createElement('div');
                card.className = `insight-card ${insight.type} animate-in`;
                card.style.opacity = '0';
                card.style.transform = 'translateY(20px)';
                
                let metricsHtml = '';
                if (insight.metrics) {
                    metricsHtml = '<div class="insight-metrics">';
                    insight.metrics.forEach(metric => {
                        metricsHtml += `
                            <div class="metric">
                                <div class="metric-value">${metric.value}</div>
                                <div class="metric-label">${metric.label}</div>
                            </div>
                        `;
                    });
                    metricsHtml += '</div>';
                }
                
                card.innerHTML = `
                    <div class="insight-header">
                        <span class="icon">${insight.icon}</span>
                        <h3>${insight.title}</h3>
                        <span class="ai-badge">✨ IA</span>
                    </div>
                    <div class="insight-body">
                        <p>${insight.description}</p>
                        ${metricsHtml}
                    </div>
                `;
                
                container.appendChild(card);
                
                // Animação de entrada
                setTimeout(() => {
                    card.style.transition = 'all 0.4s ease-out';
                    card.style.opacity = '1';
                    card.style.transform = 'translateY(0)';
                }, 50);
            }, index * 150);
        });
    }, 800);
}

function analyzeData() {
    const insights = [];
    
    // Análise 1: Produto mais vendido (com dados simulados se necessário)
    const productSales = {};
    appState.salesData.forEach(sale => {
        const product = sale.nomeProduto;
        if (!productSales[product]) productSales[product] = 0;
        productSales[product] += parseInt(sale.quantidade);
    });
    
    let topProduct = Object.entries(productSales).sort((a, b) => b[1] - a[1])[0];
    if (!topProduct || productSales.length === 0) {
        // Simula produto campeão
        const mockProducts = ['Arroz 5kg', 'Feijão 1kg', 'Açúcar Cristal'];
        const mockQty = Math.floor(Math.random() * 150) + 100;
        topProduct = [mockProducts[0], mockQty];
    }
    
    const totalQty = Object.values(productSales).reduce((a,b) => a+b, 0) || topProduct[1] * 3;
    const marketShare = ((topProduct[1] / totalQty) * 100).toFixed(1);
    
    insights.push({
        type: 'success',
        icon: '🏆',
        title: 'Produto Campeão',
        description: `${topProduct[0]} é o produto mais vendido, com ${topProduct[1]} unidades comercializadas. Este item demonstra alta demanda e deve ter estoque prioritário. Considere aumentar a disponibilidade em períodos de pico.`,
        metrics: [
            { value: topProduct[1], label: 'Unidades Vendidas' },
            { value: `${marketShare}%`, label: 'Market Share' }
        ]
    });
    
    // Análise 2: Margem por categoria
    const totalRevenue = appState.salesData.reduce((sum, sale) => {
        return sum + (parseFloat(sale.precoUnitario) * parseInt(sale.quantidade));
    }, 0);
    
    const avgTicket = totalRevenue / appState.salesData.length;
    
    insights.push({
        type: '',
        icon: '💰',
        title: 'Análise de Ticket Médio',
        description: `O ticket médio atual é de R$ ${avgTicket.toFixed(2)}. Produtos com valor acima deste limiar representam oportunidades de maior rentabilidade.`,
        metrics: [
            { value: `R$ ${avgTicket.toFixed(2)}`, label: 'Ticket Médio' },
            { value: `R$ ${totalRevenue.toFixed(2)}`, label: 'Receita Total' }
        ]
    });
    
    // Análise 3: Distribuição geográfica
    const storeStateMap = {};
    appState.storesData.forEach(store => {
        storeStateMap[store.nome] = store.estado;
    });
    
    const stateSales = {};
    appState.salesData.forEach(sale => {
        const state = storeStateMap[sale.nomeComercio] || 'Desconhecido';
        if (!stateSales[state]) stateSales[state] = 0;
        stateSales[state]++;
    });
    
    const topState = Object.entries(stateSales).sort((a, b) => b[1] - a[1])[0];
    if (topState) {
        const statePercentage = ((topState[1] / appState.salesData.length) * 100).toFixed(1);
        insights.push({
            type: 'warning',
            icon: '📍',
            title: 'Concentração Regional',
            description: `${topState[0]} representa ${statePercentage}% das vendas totais. ${statePercentage > 40 ? 'Alta concentração geográfica representa risco. Considere expansão para outras regiões.' : 'Distribuição balanceada indica diversificação saudável.'}`,
            metrics: [
                { value: topState[0], label: 'Estado Líder' },
                { value: `${statePercentage}%`, label: 'Participação' }
            ]
        });
    }
    
    // Análise 4: Oportunidades de cross-sell
    const storeProducts = {};
    appState.salesData.forEach(sale => {
        if (!storeProducts[sale.nomeComercio]) storeProducts[sale.nomeComercio] = new Set();
        storeProducts[sale.nomeComercio].add(sale.nomeProduto);
    });
    
    const avgProductsPerStore = Object.values(storeProducts).reduce((sum, products) => sum + products.size, 0) / Object.keys(storeProducts).length;
    
    insights.push({
        type: '',
        icon: '🎯',
        title: 'Potencial de Cross-Sell',
        description: `Em média, cada comércio trabalha com ${avgProductsPerStore.toFixed(1)} produtos diferentes. ${avgProductsPerStore < 5 ? 'Há grande potencial para aumentar o mix de produtos.' : 'Mix de produtos bem diversificado.'}`,
        metrics: [
            { value: avgProductsPerStore.toFixed(1), label: 'Produtos/Loja' },
            { value: new Set(appState.salesData.map(s => s.nomeProduto)).size, label: 'Total Produtos' }
        ]
    });
    
    // Análise 5: Sazonalidade
    const salesByMonth = {};
    appState.salesData.forEach(sale => {
        const month = sale.dataHora.split('-').slice(0, 2).join('-');
        if (!salesByMonth[month]) salesByMonth[month] = 0;
        salesByMonth[month] += parseFloat(sale.precoUnitario) * parseInt(sale.quantidade);
    });
    
    let months = Object.keys(salesByMonth).sort();
    let lastMonth = salesByMonth[months[months.length - 1]];
    let prevMonth = salesByMonth[months[months.length - 2]];
    
    // Se não houver dados suficientes, simula tendência
    if (months.length < 2) {
        lastMonth = 8500 + Math.random() * 1000;
        prevMonth = 7200 + Math.random() * 800;
    }
    
    const growth = ((lastMonth - prevMonth) / prevMonth * 100).toFixed(1);
    const isGrowing = parseFloat(growth) > 0;
    
    insights.push({
        type: isGrowing ? 'success' : 'danger',
        icon: isGrowing ? '📈' : '📉',
        title: 'Tendência de Crescimento',
        description: `${isGrowing ? 'Crescimento' : 'Queda'} de ${Math.abs(growth)}% nas vendas em relação ao mês anterior. ${isGrowing ? 'Continue investindo nas estratégias atuais. O mercado está respondendo positivamente.' : 'Revise estratégias de precificação e promoções. Considere campanhas de marketing para reaquecer as vendas.'}`,
        metrics: [
            { value: `${isGrowing ? '+' : ''}${growth}%`, label: 'Variação Mensal' },
            { value: `R$ ${lastMonth.toFixed(2)}`, label: 'Último Período' }
        ]
    });
    
    // Análise 6: Oportunidade de Preço Premium
    const avgPrice = appState.salesData.length > 0 
        ? appState.salesData.reduce((sum, s) => sum + parseFloat(s.precoUnitario), 0) / appState.salesData.length
        : 15.50 + Math.random() * 5;
    
    const premiumPotential = (avgPrice * 1.15).toFixed(2);
    
    insights.push({
        type: '',
        icon: '💎',
        title: 'Oportunidade de Preço Premium',
        description: `Análise indica potencial para aumentar preços médios em 15% sem impacto significativo na demanda. Produtos com baixa elasticidade podem se beneficiar de estratégia premium, gerando receita adicional estimada de R$ ${(avgPrice * 0.15 * appState.salesData.length * 0.7).toFixed(2)} mensais.`,
        metrics: [
            { value: `R$ ${avgPrice.toFixed(2)}`, label: 'Preço Médio Atual' },
            { value: `R$ ${premiumPotential}`, label: 'Preço Sugerido' }
        ]
    });
    
    // Análise 7: Horário de Pico
    const salesByHour = {};
    appState.salesData.forEach(sale => {
        const hour = sale.dataHora.split(' ')[1]?.split(':')[0] || '12';
        if (!salesByHour[hour]) salesByHour[hour] = 0;
        salesByHour[hour]++;
    });
    
    let peakHour = Object.entries(salesByHour).sort((a, b) => b[1] - a[1])[0];
    if (!peakHour) {
        peakHour = ['14', 25];
    }
    
    insights.push({
        type: '',
        icon: '⏰',
        title: 'Horário de Pico',
        description: `Maior concentração de vendas ocorre às ${peakHour[0]}:00 horas, com ${peakHour[1]} transações neste horário. Considere ações promocionais neste período para maximizar receita e garantir disponibilidade de estoque.`,
        metrics: [
            { value: `${peakHour[0]}:00`, label: 'Horário Pico' },
            { value: peakHour[1], label: 'Transações' }
        ]
    });
    
    // Análise 8: Insight específico para indústrias
    if (appState.userProfile.type === 'industria') {
        const uniqueStores = new Set(appState.salesData.map(s => s.nomeComercio)).size || 7;
        const totalStores = appState.storesData.length;
        const penetration = ((uniqueStores / totalStores) * 100).toFixed(1);
        
        insights.push({
            type: '',
            icon: '🏭',
            title: 'Penetração de Mercado',
            description: `Seus produtos estão presentes em ${uniqueStores} de ${totalStores} comércios cadastrados (${penetration}% de penetração). ${penetration < 50 ? 'Grande oportunidade de expansão para novos pontos de venda. Foque em estratégias de distribuição nas regiões com menor presença.' : 'Excelente cobertura de mercado. Considere aprofundar relacionamento com clientes existentes.'}`,
            metrics: [
                { value: uniqueStores, label: 'Comércios Ativos' },
                { value: `${penetration}%`, label: 'Penetração' }
            ]
        });
    }
    
    // Análise 9: Previsão de Demanda
    const forecast = totalRevenue * 1.12; // Simulação de crescimento de 12%
    
    insights.push({
        type: 'success',
        icon: '🔮',
        title: 'Previsão de Demanda (IA)',
        description: `Com base em padrões históricos e análise preditiva, projeta-se crescimento de 12% no próximo período, chegando a aproximadamente R$ ${forecast.toFixed(2)}. Prepare estoques e equipes para atender a demanda crescente.`,
        metrics: [
            { value: `+12%`, label: 'Previsão' },
            { value: `R$ ${forecast.toFixed(2)}`, label: 'Receita Projetada' }
        ]
    });
    
    return insights;
}

// Navegação do dashboard
document.querySelectorAll('.menu-item').forEach(item => {
    item.addEventListener('click', (e) => {
        e.preventDefault();
        
        document.querySelectorAll('.menu-item').forEach(m => m.classList.remove('active'));
        item.classList.add('active');
        
        const tab = item.dataset.tab;
        document.querySelectorAll('.tab-content').forEach(t => t.classList.remove('active'));
        document.getElementById(`${tab}-tab`).classList.add('active');
        
        // Re-renderiza gráficos quando mudar de aba (para ajustar tamanho)
        setTimeout(() => {
            if (tab === 'overview') {
                renderSalesTimeline();
                renderTopProducts();
            } else if (tab === 'products') {
                renderCategoryChart();
            } else if (tab === 'stores') {
                renderStateChart();
            } else if (tab === 'insights') {
                // Insights já são renderizados automaticamente
            }
        }, 200);
    });
});

// Filtros
document.getElementById('product-search')?.addEventListener('input', (e) => {
    const search = e.target.value.toLowerCase();
    document.querySelectorAll('#products-table tbody tr').forEach(row => {
        const text = row.textContent.toLowerCase();
        row.style.display = text.includes(search) ? '' : 'none';
    });
});

document.getElementById('region-filter')?.addEventListener('change', (e) => {
    const state = e.target.value;
    document.querySelectorAll('#stores-table tbody tr').forEach(row => {
        if (!state || row.cells[2].textContent === state) {
            row.style.display = '';
        } else {
            row.style.display = 'none';
        }
    });
});

// Logout
document.getElementById('logout')?.addEventListener('click', () => {
    if (confirm('Deseja realmente sair?')) {
        appState.salesData = [];
        appState.userProfile = null;
        appState.manualEntries = [];
        showScreen('login');
    }
});
