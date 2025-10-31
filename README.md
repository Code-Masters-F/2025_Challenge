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
    * 2) Estatísticas por produto
    * 3) Estatísticas por região (cidade/estado)
    * 0) Voltar ao menu principal

### 3) Consultar por ID de comércio
* **Função:** Consulta detalhada de um comércio específico.
* **Descrição:** Exibe os dados cadastrais do comércio, seu total de vendas, os produtos vendidos e a data da última venda registrada.

### 4) Exportar relatório (.txt/.csv)
* **Função:** Geração de arquivo externo com os resultados das análises.
* **Descrição:** Permite exportar um relatório geral ou filtrado por comércio, nos formatos TXT ou CSV.

### 0) Sair
* **Função:** Encerrar o programa com segurança.

## 4. Estrutura do Banco de Dados

O sistema consiste em duas tabelas principais para armazenar os dados de comércios e suas respectivas vendas.

### Tabela: Pequeno-Varejo

| Campo    | Tipo                                | Descrição              |
| -------- | ----------------------------------- | ---------------------- |
| id       | NUMBER(10) **IDENTITY PRIMARY KEY** | Gerado automaticamente |
| nome     | VARCHAR2(255)                       | Nome da loja           |
| cnpj     | VARCHAR2(18)                        | CNPJ da loja           |
| endereco | VARCHAR2(500)                       | Endereço completo      |
| cidade   | VARCHAR2(100)                       | Cidade                 |
| estado   | VARCHAR2(50)                        | Estado                 |
| cep      | VARCHAR2(10)                        | CEP                    |




### Tabela: Venda

| Campo             | Tipo                                | Descrição                       |
| ----------------- | ----------------------------------- | ------------------------------- |
| id                | NUMBER(10) **IDENTITY PRIMARY KEY** | Gerado automaticamente          |
| id_varejo         | NUMBER(10)                          | FK → pequenoVarejo.id           |
| nome_produto      | VARCHAR2(255)                       | Nome do produto                 |
| tamanho_embalagem | NUMBER(10,2)                        | Peso ou volume                  |
| preco_unitario    | NUMBER(10,2)                        | Preço unitário                  |
| unidade_de_medida | VARCHAR2(50)                        | Unidade de medida               |
| quantidade        | NUMBER(10,3)                        | Quantidade vendida              |
| data_hora         | TIMESTAMP WITH TIME ZONE            | Data da venda (default = agora) |



## 5. Tecnologias Utilizadas

* **Java Puro**
* **JDBC (Java Database Connectivity)**

## 6. Visão de Longo Prazo

Embora a entrega atual (Fase 7) se concentre no processamento de CSV via console, a visão completa do produto inclui:

* **Coleta Multicanal:** Recebimento de dados via notas fiscais, arquivos Excel, PDF ou até mesmo dados compartilhados via WhatsApp.
* **OCR e IA:** Uso de tecnologia OCR (Reconhecimento Óptico de Caracteres) com IA para capturar dados de imagens, como fotos de notas fiscais ou prints de tela.
* **Front-End:** Um front-end para a experiência do usuário, permitindo login, visualização de estatísticas e upload de arquivos.
* **Hierarquia de Redes:** Permitir que donos de múltiplas lojas criem um "perfil de rede" para análises centralizadas.
