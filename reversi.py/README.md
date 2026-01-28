# Reversi (Othello) em Python com Pygame

## Descrição do Projeto

Este projeto consiste no desenvolvimento de um jogo digital do tipo **Reversi (Othello)**, implementado na linguagem **Python**, utilizando a biblioteca **Pygame** para a construção da interface gráfica, interação com o utilizador, animações e reprodução de áudio.

O projeto foi desenvolvido no contexto académico, com o objetivo de aplicar conceitos fundamentais de programação, tais como lógica computacional, programação gráfica e interação humano-computador.

---

## Tecnologias Utilizadas

- Linguagem de Programação: **Python**
- Biblioteca Gráfica: **Pygame**
- Paradigma: **Programação modular**
- Recursos multimédia:
  - Imagens (PNG / JPG)
  - Sons (WAV)

---

## Estrutura do Projeto

O projeto encontra-se organizado em módulos, garantindo uma separação clara entre lógica e interface gráfica


---

## Funcionalidades Implementadas

- Menu inicial com seleção do modo de jogo
- Modo **Humano vs Humano**
- Modo **Humano vs Computador**
- Validação automática de jogadas
- Destaque visual das jogadas válidas
- Animação de viragem das peças
- Reprodução de efeitos sonoros
- Contagem de pontuação em tempo real
- Ecrã final com indicação de vitória, derrota ou empate

---

## Funcionamento do Sistema

Após a execução do programa, o utilizador é apresentado a um menu inicial onde pode selecionar o modo de jogo.  
Em seguida, o sistema cria o tabuleiro inicial e inicia o loop principal do jogo, responsável por:

- Processar eventos do rato
- Validar jogadas
- Atualizar o estado do tabuleiro
- Renderizar os elementos gráficos
- Verificar as condições de fim de jogo

No final da partida, é apresentado um ecrã com o resultado do jogo.

---

## Melhorias Futuras

Apesar de o sistema cumprir os objetivos propostos,futuramente desnvolveremos versoes melhoradas:

### Implementação de Graus de Dificuldade
- **Fácil:** jogadas aleatórias válidas
- **Médio:** escolha da jogada com maior ganho imediato
- **Difícil:** implementação de algoritmos 

### Melhorias Gráficas
- Ecrãs distintos para vitória, derrota e empate
- Animações específicas para cada resultado
- Efeitos sonoros diferenciados

### Chat de Provocações
- Mensagens automáticas durante a partida
- Comentários finais consoante o resultado do jogo
- Interação textual com o jogador humano

### Outras Evoluções
- Personalização visual do tabuleiro e das peças
- Modo online ou em rede local

---

## Conclusão

O projeto apresenta uma implementação funcional e organizada do jogo Reversi, demonstrando a aplicação prática de conceitos de programação gráfica e lógica de jogos.  
A estrutura modular adotada permite fácil manutenção e expansão futura do sistema.

---

## Autoria

Projeto desenvolvido por:

- **Danilo Oliveira**
- **Marcelo Cruz**
- **Evelyn Rodrigues**

---

## Execução do Projeto

Para executar o jogo, é necessário ter o Python e o Pygame instalados.

```bash
pip install pygame
python main.py
