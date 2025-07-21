# 💰 MyFinanceAPI

**MyFinanceAPI** é uma API REST desenvolvida em **Java EE com Servlets**, com o objetivo de gerenciar transações financeiras pessoais, como receitas e despesas, organizadas por categorias. A API permite realizar operações completas de CRUD e também obter resumos financeiros com base nas transações registradas.

---

## 📌 Funcionalidades

A API permite as seguintes operações:

### 🔹 Cadastro de Transações
- Descrição da transação  
- Valor  
- Tipo (receita ou despesa)  
- Categoria (ex: alimentação, transporte, saúde, lazer, etc.)  
- Data da transação  

### 🔹 Atualização e Exclusão
- Atualizar ou remover transações já registradas.

### 🔹 Consulta Individual
- Buscar uma transação pelo seu identificador único (ID).

### 🔹 Consulta de Múltiplas Transações
- Com suporte a:
  - Paginação
  - Filtro por período (mês e ano)
  - Filtro por tipo (receitas ou despesas)
  - Filtro por categoria
  - Filtro por descrição

### 🔹 Resumo Financeiro
- Total de receitas total e por categoria  
- Total de despesas total e por categoria  
- Saldo financeiro atual  

### 🔹 Gerenciamento de Categorias
A API também permite a gestão das categorias utilizadas nas transações:

- Criar nova categoria
- Listar todas as categorias existentes
- Atualizar categorias
- Excluir categorias

---

## 🔧 Tecnologias Utilizadas

- **Jakarta Servlet API 6.1** — Implementação moderna de Servlets, conforme a especificação Jakarta EE.
- **Jetty** — Servidor leve e embutido (incluindo suporte a JNDI).
- **JDBC com MySQL** — Comunicação com o banco de dados via `mysql-connector-j`.
- **Gson** — Serialização e desserialização de objetos Java em JSON.
- **Apache HttpClient** — Cliente HTTP para chamadas externas.
- **Apache Commons Lang 3** — Utilitários adicionais para manipulação de strings, objetos e coleções.
- **Logback** — Sistema de logging moderno e configurável.

---

## 📄 Requisitos

### ✅ Requisitos Funcionais

- [RF1] Cadastrar transações financeiras com descrição, valor, tipo, categoria e data.
- [RF2] Atualizar e excluir transações registradas.
- [RF3] Consultar transações por identificador.
- [RF4] Consultar transações com filtros por período, descrição, tipo e categoria, com paginação.
- [RF5] Obter resumo financeiro com totais por categoria e saldo atual.
- [RF6] CRUD completo de categorias.

### 🛠 Requisitos Não-Funcionais

- [RNF1] Desenvolvida com **Java EE e Servlets**
- [RNF2] Utiliza **JSON** como formato de entrada e saída
- [RNF3] Segue o **padrão REST** para definição de endpoints e operações

---

## 🗄 Banco de Dados

O modelo e o script SQL para criação do banco de dados estão localizados em: myfinanceapi/src/main/resources/database

---



