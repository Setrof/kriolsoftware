#____________________________Parte Grafica ____________________________
import pygame
import os
from M_logico import *
from M_logico_IA import *
pygame.init()
antialias = True
BASE_DIR = os.path.dirname(__file__)
pygame.mixer.init()
som_virar = pygame.mixer.Sound(os.path.join('audio','tecomer.wav'))
som_virar.set_volume(0.5)
#___________tarnsformar imagens em formato de criculo (pecas)___________
def imagem_circular(caminho,raio):
    if not os.path.exists(caminho):
        raise FileNotFoundError(f'arquivo nao encontrado:{caminho}')
    tamanho = raio * 2
    img = pygame.image.load(caminho).convert_alpha()
    img = pygame.transform.smoothscale(img,(tamanho,tamanho))

    mascara = pygame.Surface((tamanho,tamanho),pygame.SRCALPHA)
    pygame.draw.circle(mascara,(255,255,255,255),(raio,raio),raio)

    img.blit(mascara,(0,0),special_flags=pygame.BLEND_RGBA_MULT)
    return img


# PARA FAZER ANIMACAO DE VIRAR AS PECAS
def animar_virar_peca(tela, x, y, de_preta_para_branca=True):
    duracao = 200  # ms
    passos = 10
    atraso = duracao // passos

    if de_preta_para_branca:
        img_inicio = peca_preta
        img_fim = peca_branca
    else:
        img_inicio = peca_branca
        img_fim = peca_preta

    for i in range(passos):
        fator = 1 - i / passos
        largura = max(1, int(60 * fator))

        img = pygame.transform.scale(img_inicio, (largura, 60))
        tela.blit(img, (x - largura // 2, y - 30))
        pygame.display.flip()
        pygame.time.delay(atraso)

    for i in range(passos):
        fator = i / passos
        largura = max(1, int(60 * fator))

        img = pygame.transform.scale(img_fim, (largura, 60))
        tela.blit(img, (x - largura // 2, y - 30))
        pygame.display.flip()
        pygame.time.delay(atraso)

peca_preta = imagem_circular(os.path.join('image','peca_preto.png'),30)
peca_branca = imagem_circular(os.path.join('image','peca_branco.png'),30)


#____________________utilizador VS utilizador MOD grafico____________________
def utilizadores():
    # Tamanho de cada célula do tabuleiro
    CEL = 80

    # Dimensão da janela
    LARGURA = ALTURA = tamanho * CEL

    # cores e fundo
    fundo = pygame.image.load("image/Fundo.jpg")
    fundo = pygame.transform.scale(fundo, (LARGURA, ALTURA))
    global peca_preta
    global peca_branca
    verde =(0,150,0)
    PRETO_COR = (0, 0, 0)
    BRANCO_COR = (255, 255, 255)
    global antialias
    # Janela do jogo
    tela = pygame.display.set_mode((LARGURA, ALTURA))
    pygame.display.set_caption("REVERSSI")
    # Fonte
    fonte = pygame.font.SysFont(None, 28)
    fonte_final = pygame.font.SysFont(None, 60)
    # Cria tabuleiro inicial
    tabuleiro = criar_tabuleiro()
    # Jogador inicial (pretas)
    jogador_atual = preto

    def desenhar():
        tela.fill(verde)
        tela.blit(fundo, (0,0))
        # linhas
        for i in range(tamanho + 1):
            pygame.draw.line(tela, PRETO_COR, (0, i * CEL), (LARGURA, i * CEL), 2)
            pygame.draw.line(tela, PRETO_COR, (i * CEL, 0), (i * CEL, ALTURA), 2)
        # opcoes validas
        opcoes = jogadas_validas(tabuleiro, jogador_atual)
        for (i, j) in opcoes:
            x = j * CEL + CEL // 2
            y = i * CEL + CEL // 2
            pygame.draw.circle(tela, (255, 0, 0), (x, y), 8)
        # peças
        for i in range(tamanho):
            for j in range(tamanho):
                x = j * CEL + CEL // 2
                y = i * CEL + CEL // 2
                if tabuleiro[i][j] == preto:
                    tela.blit(peca_preta, (x - 30, y - 30))
                elif tabuleiro[i][j] == computador:
                    tela.blit(peca_branca, (x - 30, y - 30))
        # pontuação
        p, b = contar_pecas(tabuleiro)
        texto = fonte.render(f"Pretas: {p}  Brancas: {b}", True, BRANCO_COR)
        tela.blit(texto, (10, 10))

    def clique_para_matriz(pos):
        x, y = pos
        return y // CEL, x // CEL
    rodando = True
    while rodando:
        for evento in pygame.event.get():
            if evento.type == pygame.QUIT:
                rodando = False

            if evento.type == pygame.MOUSEBUTTONDOWN:
                l, c = clique_para_matriz(pygame.mouse.get_pos())
                if validar_jogada(tabuleiro, l, c, jogador_atual):
                    antes = contar_pecas(tabuleiro)

                    fazer_jogada(tabuleiro, l, c, jogador_atual)
                    x = c * CEL + CEL // 2
                    y = l * CEL + CEL // 2

                    animar_virar_peca(tela, x, y, jogador_atual == preto)
                    depois = contar_pecas(tabuleiro)
                    if antes != depois:
                        som_virar.play()
                    # troca jogador
                    jogador_atual *= -1
                    # se o próximo não puder jogar, volta
                    if not jogadas_validas(tabuleiro, jogador_atual):
                        jogador_atual *= -1
        desenhar()
        pygame.display.flip()
        # fim do jogo
        if fim_de_jogo(tabuleiro):
            rodando = False
    # mostrar vencedor (Fora do loop!)
    vencedor = vencedor_da_partida(tabuleiro)
    tela.fill((0,0,0))
    if vencedor == preto:
        texto_final = fonte_final.render("Vencedor: Pretas", True, (255,255,255))
    elif vencedor == computador:
        texto_final = fonte_final.render("Vencedor: Brancas", True, (255,255,255))
    else:
        texto_final = fonte_final.render("Empate!", True, (255,255,255))

    rect = texto_final.get_rect(center=(LARGURA // 2, ALTURA // 2))
    tela.blit(texto_final, rect)
    pygame.display.flip()
    pygame.time.delay(3000)







#____________________utilizador VS computador MOD grafico____________________

def utilizador():
    # Tamanho de cada célula do tabuleiro
    CEL = 80
    global peca_preta
    global peca_branca
    # Dimensão da janela
    LARGURA = ALTURA = tamanho * CEL

    # cores e fundo
    fundo = pygame.image.load("image/Fundo.jpg")
    fundo = pygame.transform.scale(fundo, (LARGURA, ALTURA))
    verde = (0, 150, 0)
    PRETO_COR = (0, 0, 0)
    BRANCO_COR = (255, 255, 255)
    global antialias
    # Janela do jogo
    tela = pygame.display.set_mode((LARGURA, ALTURA))
    pygame.display.set_caption("REVERSSI")

    # Fonte
    fonte = pygame.font.SysFont(None, 28)
    fonte_final = pygame.font.SysFont(None, 60)
    # Cria tabuleiro inicial
    tabuleiro = criar_tabuleiro()

    # Jogador inicial (pretas)
    jogador_atual = preto
    #tempo que minha ia vai demorar para dar uma jogada
    tempo_espera_ia = 1000  #em milisegundos
    ultimo_tempo_ia = pygame.time.get_ticks()
    # cria tabuleiro e as peças
    def desenhar():
        tela.fill(verde)
        tela.blit(fundo, (0, 0))

        # Desenha as linhas da grade
        for i in range(tamanho + 1):
            pygame.draw.line(tela, PRETO_COR, (0, i * CEL), (LARGURA, i * CEL), 2)
            pygame.draw.line(tela, PRETO_COR, (i * CEL, 0), (i * CEL, ALTURA), 2)
        #Desenhar onde tem opcoes validas
        opcoes = jogadas_validas(tabuleiro, jogador_atual)
        for (i, j) in opcoes:
            x = j * CEL + CEL // 2
            y = i * CEL + CEL // 2
            pygame.draw.circle(tela, (255, 0, 0), (x, y), 8)
        # Desenha as peças conforme a matriz NumPy
        for i in range(tamanho):
            for j in range(tamanho):
                x = j * CEL + CEL // 2
                y = i * CEL + CEL // 2
                if tabuleiro[i][j] == preto:
                    tela.blit(peca_preta, (x - 30, y - 30))
                elif tabuleiro[i][j] == computador:
                    tela.blit(peca_branca, (x - 30, y - 30))

        # Mostra a pontuação
        p, b = contar_pecas(tabuleiro)
        texto = fonte.render(f"Pretas: {p}  Computador: {b}", True, BRANCO_COR)
        tela.blit(texto, (10, 10))

    # Converte clique do mouse em posição da matriz
    def clique_para_matriz(pos):
        x, y = pos
        return y // CEL, x // CEL

    # Loop principal do jogo
    rodando = True
    while rodando:
        for evento in pygame.event.get():
            # Fecha a janela
            if evento.type == pygame.QUIT:
                rodando = False

            # Clique do mouse (jogador humano)
            if evento.type == pygame.MOUSEBUTTONDOWN and jogador_atual == preto:
                l, c = clique_para_matriz(pygame.mouse.get_pos())

                # Se a jogada for válida
                if validar_jogada(tabuleiro, l, c, jogador_atual):
                    antes = contar_pecas(tabuleiro)
                    fazer_jogada(tabuleiro, l, c, jogador_atual)

                    x = c * CEL + CEL // 2
                    y = l * CEL + CEL // 2

                    animar_virar_peca(tela, x, y, jogador_atual == preto)
                    depois=contar_pecas(tabuleiro)
                    if depois != antes:
                        som_virar.play()
                    jogador_atual = computador
                    ultimo_tempo_ia = pygame.time.get_ticks()

        # IA (computador joga automaticamente)
        if jogador_atual == computador:
            agora= pygame.time.get_ticks()
            if agora - ultimo_tempo_ia >=tempo_espera_ia: #para controlar o tempo de espera definido
                jogadas = jogadas_validas(tabuleiro, computador)
                if jogadas:
                    antes = contar_pecas(tabuleiro)
                    l, c = jogadas[np.random.randint(len(jogadas))]

                    fazer_jogada(tabuleiro, l, c, computador)
                    depois=contar_pecas(tabuleiro)
                    if depois != antes:
                        som_virar.play()
                    jogador_atual = preto
        # Atualiza a tela
        desenhar()
        pygame.display.flip()

        # Verifica fim do jogo
        if fim_de_jogo(tabuleiro):
            pygame.time.delay(2000)
            rodando = False
    # Quando terminar, mostra o vencedor
    vencedor = vencedor_da_partida(tabuleiro)

    tela.fill((0, 0, 0))

    if vencedor == preto:
        texto_final = fonte_final.render("Vencedor: Pretas", True, (255, 255, 255))
    elif vencedor == computador:
        texto_final = fonte_final.render("Vencedor: Computador", True, (255, 255, 255))
    else:
        texto_final = fonte_final.render("Empate!", True, (255, 255, 255))

    # centraliza o texto
    rect = texto_final.get_rect(center=(LARGURA//2, ALTURA//2))
    tela.blit(texto_final, rect)

    pygame.display.flip()
    pygame.time.delay(3000)  # espera 3 segundos