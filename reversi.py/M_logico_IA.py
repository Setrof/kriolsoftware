import numpy as np

# tamanho do tabuleiro (8x8)
tamanho = 8
# para representar os estados das casas
vazio = 0      # casa vazia
preto = 1      # casa preta
computador = -1     # casa branca
# contadores de vitória (MD3)
vitoria_computador = 0
vitoria_preto = 0
partidas_para_vencer = 2
# direções possíveis ao redor de uma peça (8 direções)
direcoes = [
    (-1, -1), (-1, 0), (-1, 1),   # diagonais acima
    ( 0, -1),          ( 0, 1),   # esquerda e direita
    ( 1, -1), ( 1, 0), ( 1, 1)    # diagonais abaixo
]
# cria e retorna o tabuleiro inicial do jogo
def criar_tabuleiro():  # criar tabuleiro
    tabuleiro = np.zeros((tamanho, tamanho), dtype=int)
    # coloca as 4 peças no centro do tabuleiro
    tabuleiro[3][3] = computador
    tabuleiro[3][4] = preto
    tabuleiro[4][3] = preto
    tabuleiro[4][4] = computador
    # retorna tabuleiro criado
    return tabuleiro

# verifica se o utilizador está a jogar dentro do tabuleiro
def dentro_tabuleiro(tabuleiro, l, c):
    return 0 <= l < tamanho and 0 <= c < tamanho

# para verificar se a jogada é válida segundo as regras do Reversi
def validar_jogada(tabuleiro, l, c, jogador):

    # se a posição estiver fora do tabuleiro ou não estiver vazia
    if not dentro_tabuleiro(tabuleiro, l, c) or tabuleiro[l][c] != vazio:
        return False
    # define o adversário
    adversario = computador if jogador == preto else preto
    # verifica todas as direções
    for dx, dy in direcoes:
        i, j = l + dx, c + dy  # avança uma posição na direção escolhida
        encontrou = False
        # procura peças do adversário
        while dentro_tabuleiro(tabuleiro, i, j) and tabuleiro[i][j] == adversario:
            encontrou = True
            i += dx
            j += dy
        # se terminar numa peça do jogador, a jogada é válida
        if encontrou and dentro_tabuleiro(tabuleiro, i, j) and tabuleiro[i][j] == jogador:
            return True
    # se nenhuma direção for válida
    return False
# retorna todas as jogadas válidas de um jogador
def jogadas_validas(tabuleiro, jogador):
    return [
        (i, j)  # posição válida
        for i in range(tamanho)
        for j in range(tamanho)
        if validar_jogada(tabuleiro, i, j, jogador)
    ]
# executa uma jogada e vira as peças necessárias
def fazer_jogada(tabuleiro, l, c, jogador):

    # coloca a peça do jogador na posição escolhida
    tabuleiro[l][c] = jogador

    # define o adversário
    adversario = computador if jogador == preto else preto

    # percorre todas as direções
    for dx, dy in direcoes:
        i, j = l + dx, c + dy
        pecas = []  # lista de peças que podem ser viradas
        # enquanto encontrar peças do adversário
        while dentro_tabuleiro(tabuleiro, i, j) and tabuleiro[i][j] == adversario:
            pecas.append((i, j))
            i += dx
            j += dy
        # se terminar numa peça do jogador, vira as peças guardadas
        if dentro_tabuleiro(tabuleiro, i, j) and tabuleiro[i][j] == jogador:
            for x, y in pecas:
                tabuleiro[x][y] = jogador

# conta quantas peças existem no tabuleiro
def contar_pecas(tabuleiro):
    pretas = np.sum(tabuleiro == preto)     # contar peças pretas
    brancas = np.sum(tabuleiro == computador)   # contar peças brancas
    return pretas, brancas

# verifica o fim do jogo
def fim_de_jogo(tabuleiro):
    return not jogadas_validas(tabuleiro, preto) and not jogadas_validas(tabuleiro, computador)

# verifica o vencedor da partida (para funcionamento do MD3)
def vencedor_da_partida(tabuleiro):
    pretas, computadores = contar_pecas(tabuleiro)

    if pretas > computadores:
        return preto     # preto venceu
    elif computadores > pretas:
        return computador    # computador venceu
    else:
        return vazio     # empate
