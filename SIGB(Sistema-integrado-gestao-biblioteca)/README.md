# Sistema Integrado de Gestão de Biblioteca (SIGB)

## Descrição do Projeto
Este projeto consiste no desenvolvimento de uma aplicação desktop para a gestão automatizada de uma biblioteca, implementada na linguagem Java, utilizando a biblioteca Swing para a construção da interface gráfica e a persistência local em base de dados SQLite.
O projeto foi desenvolvido no contexto académico, especificamente no âmbito da unidade curricular de **Algoritmos e Estruturas de Dados**, com o objetivo prático de aplicar conceitos fundamentais de algoritmia e estrutura de dados, manipulação de dados atravez de estruturas como stack e Filas.
---

## Tecnologias Utilizadas
* **Linguagem de Programação:** Java (JDK 21 ou superior)
* **Biblioteca Gráfica:** Java Swing / AWT
* **Base de Dados:** SQLite (Persistência local leve via JDBC)
* **Paradigma:** Programação Orientada a Objetos (POO) com arquitetura MVC
* **Ambiente de Desenvolvimento:** Visual Studio Code / Ubuntu Linux && Microsoft Windows

---

## Estrutura do Projeto
O projeto encontra-se organizado em 5 pacotes (packages), garantindo uma separação clara entre a lógica algorítmica, persistência de dados e a interface gráfica:
* `database`: Configuração e conexão atravez dos DAO com o banco de dados local SQLite.
* `model`: Classes de entidades estruturais (Livro, Utilizador, Emprestimo).
* `service`: Validações de regras de negócio e processamento interno dos algoritmos.
* `structures`: Implementação e gestão das estruturas de dados dinâmicas em memória.
* `view`: Telas, painéis e componentes visuais da interface gráfica.

---

## Estruturas de Dados Presentes no Trabalho
Para otimizar as operações em memória antes da persistência no disco, o sistema faz uso estratégico de diferentes estruturas de dados:

* **Filas Dinâmicas (`Queue`):** Utilizadas para a gestão automática da **Lista de Espera** de livros populares. Quando um utilizador tenta requisitar um livro que já se encontra totalmente emprestado, o seu ID é inserido numa fila (*FIFO - First In, First Out*). Assim que o livro é devolvido, o algoritmo retira o primeiro utilizador da fila para lhe atribuir a prioridade de reserva.
* **Pilhas Dinâmicas (`Stack`):** Implementadas para gerir o **Histórico Recente de Transações** efetuadas no balcão durante a sessão atual. Cada operação concluída com sucesso (empréstimo ou devolução) é empilhada (*LIFO - Last In, First Out*). Isto permite uma auditoria rápida e possibilita futuras operações de reversão ou consulta cronológica inversa das ações do operador.
* **Matrizes Bidimensionais (`Object[][]`):** Aplicadas na camada de visualização para o mapping e renderização de dados tabulares dentro do componente dinâmico `JTable`. Os algoritmos convertem as coleções de objetos extraídas da base de dados em arranjos bidimensionais estruturados em linhas e colunas em tempo real.

---

## Funcionalidades Implementadas
* Menu lateral fixo com navegação centralizada via `CardLayout` (sem janelas pop-up persistentes)
* Módulo de Gestão de Livros organizado por abas (`JTabbedPane`)
  * Aba de consulta e pesquisa algorítmica em tempo real integrada a uma tabela dinâmica (`JTable`)
  * Aba de formulário para inserção e registo de novos exemplares no inventário
* Módulo de Gestão de Utilizadores organizado por abas
  * Aba de listagem, busca e visualização de dados dos leitores cadastrados
  * Aba de formulário dedicado ao cadastro de novos perfis de utilizadores
* Módulo de Transações Operacionais com 3 abas distintas
  * Aba para a realização rápida de novos empréstimos associando utilizador e livro
  * Aba de monitorização e consulta de prazos com destaque visual para **atrasos**
  * Aba de devoluções rápidas com tratamento de exceções, feedback de status e atualização automática de stock

---

## Funcionamento do Sistema
Após a execução do programa, o utilizador é apresentado à `JanelaPrincipal` onde o menu lateral à esquerda comanda o conteúdo renderizado no painel dinâmico à direita. O loop de eventos do Java Swing atua em conjunto com a camada de serviços para:
1. Processar cliques de navegação e submissões de formulários.
2. Executar algoritmos de validação para garantir a consistência dos dados inseridos.
3. Consultar e atualizar o estado da base de dados SQLite através de operações CRUD.
4. Renderizar componentes gráficos iterativos dotados de barras de scroll dinâmicas.
5. Manipular em memória as regras de prioridade das Filas e o empilhamento das Pilhas de auditoria.

---

## Melhorias Futuras
Apesar de o sistema cumprir os objetivos propostos, futuramente desenvolveremos versões melhoradas com foco em algoritmos mais complexos:

### Otimização de Algoritmos de Pesquisa
* **Fácil:** Implementação de filtros sequenciais simples por múltiplos campos em simultâneo.
* **Médio:** Substituição de pesquisas lineares por algoritmos de busca binária estruturada após a ordenação dos vetores.
* **Difícil:** Implementação de **Árvores Binárias de Busca (BST)** ou **Tabelas Hash** para indexação e redução da complexidade de pesquisa de livros para $O(\log n)$ ou $O(1)$.

### Melhorias Gráficas e Alertas
* Integração com bibliotecas de Look and Feel moderno (como o FlatLaf) para suporte a temas visuais.
* Geração automática de relatórios em formato PDF recorrendo a algoritmos de exportação de dados.

### Outras Evoluções
* Controle de acesso baseado em funções (Autenticação de Administradores e Operadores).
* Migração do armazenamento local para uma arquitetura de banco de dados cliente-servidor distribuída.

---

## Conclusão
O projeto apresenta uma implementação funcional, robusta e organizada de um Sistema de Gestão de Biblioteca, demonstrando com sucesso a aplicação prática de conceitos de algoritmos estruturados, manipulação de filas e pilhas, arquitetura MVC e persistência em banco de dados.
A modularidade do código garante que novas estruturas de dados mais complexas possam ser acopladas no futuro com o mínimo de impacto na interface gráfica.

---

## Autoria e Divisão de Responsabilidades
Projeto desenvolvido em regime de dupla académica por:

* **Leonardo Rosa**
  * **Responsabilidade Principal:** Desenvolvimento da Interface Gráfica (Front-End / Camada *View*). Criação dos painéis dinâmicos, estruturação dos menus de navegação em `CardLayout` e componentização por abas superiores (`JTabbedPane`).

* **Danilo Oliveira** (ID Estudante: 6098)
  * **Responsabilidade Principal:** Desenvolvimento da Lógica de Negócio, Algoritmia e Persistência (Back-End / Camadas *Model*, *Service*, *Structures* e *Database*). Implementação das estruturas de dados (Filas e Pilhas em memória) e integração com o banco SQLite.

---

## Execução do Projeto
Para executar o sistema, é necessário ter o Java JDK instalado na máquina.

1. Navegue até à pasta de códigos fontes do projeto:
   ```bash
   cd src
   execucao --> ficheiro Main.java