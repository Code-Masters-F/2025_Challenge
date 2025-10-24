# Challenge 2025: Sistema de Coleta e Análise de Vendas (BI de Varejo)

[Vídeo Pitch](https://www.youtube.com/watch?v=7u8Nv14x_2E&t)

[Contexto da aplicação e Histórias de usuário](https://docs.google.com/document/d/1hA9xPOIarcOwa0dvtctY5EPa6Rx4bELIObJjrv10lCw/edit?usp=sharing)


![Status](https://img.shields.io/badge/Status-Em_Desenvolvimento-blue)

## 1. Descrição

Este projeto é uma aplicação Java desenvolvida para solucionar a falta de ferramentas acessíveis para coleta e análise de dados de venda (Sell-out) em pequenos e médios varejistas.

O objetivo é criar um sistema independente de ERP que funcione como um "mini-BI local". A aplicação é capaz de ler arquivos CSV, armazenar registros de vendas e comércios, gerar análises estatísticas e exportar relatórios em `.txt` ou `.csv` para apoiar decisões de negócio.

## 2. Contexto

Esta aplicação foi desenvolvida como parte do **Challenge 2025**. O problema central é que pequenos varejos costumam registrar suas vendas de formas não convencionais (Excel, PDF, ou até WhatsApp), dificultando a análise de dados.

A solução visa criar um ecossistema onde esses dados possam ser coletados, estruturados e analisados. O principal cliente do sistema são as indústrias, que comprariam os dados e estatísticas dos pequenos varejos para traçar estratégias de trade marketing. Os próprios lojistas também se beneficiam, podendo visualizar dashboards de seus próprios registros.

## 3. Funcionalidades (Entrega Fase 7)

A versão atual da aplicação é um sistema de console em Java puro, focado em ler, organizar e analisar dados recebidos via arquivos `.csv`. O JDBC é utilizado para garantir a persistência real dos dados.

O menu principal oferece as seguintes opções:

### 1) Importar arquivo CSV
* **Função:** Leitura e armazenamento dos dados.
* **Descrição:** O sistema solicita o caminho do(s) arquivo(s) CSV (ex: `vendas.csv`, `comercios.csv`). Cada linha é transformada em um registro e armazenada no banco de dados. Importações adicionais não sobrescrevem os dados anteriores.

### 2) Exibir estatísticas
* **Função:** Geração de relatórios analíticos no console.
* **Descrição:** Processa os dados importados e exibe indicadores como total de vendas, top produtos e tendências.
* **Submenu de Estatísticas**:
    * 1) Estatísticas gerais (todas as vendas)
    * 2) Estatísticas por comércio
    * 3) Estatísticas por produto
    * 4) Estatísticas por região (cidade/estado)
    * 5) Tendências de crescimento
    * 0) Voltar ao menu principal

### 3) Consultar por ID de comércio
* **Função:** Consulta detalhada de um comércio específico.
* **Descrição:** Exibe os



