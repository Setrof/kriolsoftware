#____________________________Parte Grafica ____________________________
import pygame
import os
from M_logico import *
from M_logico_IA import *
pygame.init()
antialias = True
BASE_DIR = os.path.dirname(__file__) # Pega o diretório atual do arquivo principal
pygame.mixer.init()  # Inicializa o módulo de som do Pygame
som_virar = pygame.mixer.Sound(os.path.join('audio','tecomer.wav')) # Carrega o som
som_virar.set_volume(0.5)  # Define volume do som (0.0 a 1.0)
#___________tarnsformar imagens em formato de criculo (pecas)___________
def imagem_circular(caminho,raio):
    if not os.path.exists(caminho):  # Verifica se o arquivo existe
        raise FileNotFoundError(f'arquivo nao encontrado:{caminho}')
    tamanho = raio * 2 # Tamanho final da imagem (diametro)
    img = pygame.image.load(caminho).convert_alpha() # Carrega a imagem para fazer a conversao
    img = pygame.transform.smoothscale(img,(tamanho,tamanho))  # Redimensiona para o tamanho desejado
    # Cria uma máscara circular para cortar a imagem
    mascara = pygame.Surface((tamanho,tamanho),pygame.SRCALPHA) # Superfície transparente
    pygame.draw.circle(mascara,(255,255,255,255),(raio,raio),raio) # Desenha círculo branco na máscara
    # Aplica a máscara na imagem usando blend
    img.blit(mascara,(0,0),special_flags=pygame.BLEND_RGBA_MULT)
    return img


# PARA FAZER ANIMACAO DE VIRAR AS PECAS
def animar_virar_peca(tela, x, y, de_preta_para_branca=True):
    duracao = 200   # duração total da animação em ms
    passos = 10 # número de quadros na animação
    atraso = duracao // passos # atraso entre cada quadro

    # Define a imagem inicial e final dependendo do tipo de peça
    if de_preta_para_branca:
        img_inicio = peca_preta
        img_fim = peca_branca
    else:
        img_inicio = peca_branca
        img_fim = peca_preta
    # =================== PRIMEIRA METADA DA ANIMAÇÃO (achatar a peça) ===================
    for i in range(passos):
        fator = 1 - i / passos  # Reduz a largura progressivamente
        largura = max(1, int(60 * fator))  # Evita largura zero

        img = pygame.transform.scale(img_inicio, (largura, 60)) # Escala a imagem horizontalmente
        tela.blit(img, (x - largura // 2, y - 30)) # Desenha a imagem na tela
        pygame.display.flip() # Atualiza a tela
        pygame.time.delay(atraso) # Espera o tempo do atraso
    # =================== SEGUNDA METADA DA ANIMAÇÃO (expandir a nova peça) ===================
    for i in range(passos):
        fator = i / passos  # Aumenta a largura progressivamente
        largura = max(1, int(60 * fator))

        img = pygame.transform.scale(img_fim, (largura, 60))
        tela.blit(img, (x - largura // 2, y - 30))
        pygame.display.flip()
        pygame.time.delay(atraso)
#=================== CRIAÇÃO DAS PEÇAS ===================
peca_preta = imagem_circular(os.path.join('image','peca_preto.png'),30) # Peça preta com raio 30
peca_branca = imagem_circular(os.path.join('image','peca_branco.png'),30) # Peça branca com raio 30


#____________________utilizador VS utilizador MOD grafico____________________
def utilizadores():
# =================== CONFIGURAÇÕES DO TABULEIRO ===================
    # Tamanho de cada célula do tabuleiro
    CEL = 80

# Dimensão do dispaly  de acordo tamanho do tabuleiro
    LARGURA = ALTURA = tamanho * CEL

    # cores e fundo
    fundo = pygame.image.load("image/Fundo.jpg")
    fundo = pygame.transform.scale(fundo, (LARGURA, ALTURA))
    global peca_preta  # Usa a peça preta definida globalmente
    global peca_branca # Usa a peça branca definida globalmente
    verde =(0,150,0)
    PRETO_COR = (0, 0, 0)
    BRANCO_COR = (255, 255, 255)
    global antialias
    # Janela do jogo
    tela = pygame.display.set_mode((LARGURA, ALTURA))
    pygame.display.set_caption("REVERSSI")
    # Fonte
    fonte = pygame.font.SysFont(None, 28)  # Fonte para pontuação
    fonte_final = pygame.font.SysFont(None, 60)  # Fonte para resultado final
    # Cria tabuleiro inicial
    tabuleiro = criar_tabuleiro() # Cria tabuleiro inicial
    # Jogador inicial (pretas)# Começa com as peças pretas
    jogador_atual = preto
    # =================== FUNÇÃO PARA DESENHAR O TABULEIRO ===================
    def desenhar():
        tela.fill(verde) # Preenche com verde
        tela.blit(fundo, (0,0)) # Coloca imagem de fundo
        # Desenha linhas do tabuleiro
        for i in range(tamanho + 1):
            pygame.draw.line(tela, PRETO_COR, (0, i * CEL), (LARGURA, i * CEL), 2)
            pygame.draw.line(tela, PRETO_COR, (i * CEL, 0), (i * CEL, ALTURA), 2)
        # Mostra opções válidas para jogar (pequenos círculos vermelhos)
        opcoes = jogadas_validas(tabuleiro, jogador_atual)
        for (i, j) in opcoes:
            x = j * CEL + CEL // 2
            y = i * CEL + CEL // 2
            pygame.draw.circle(tela, (255, 100, 100), (x, y), 5)
        # Desenha todas as peças do tabuleiro
        for i in range(tamanho):
            for j in range(tamanho):
                x = j * CEL + CEL // 2
                y = i * CEL + CEL // 2
                if tabuleiro[i][j] == preto:
                    tela.blit(peca_preta, (x - 30, y - 30))
                elif tabuleiro[i][j] == computador:
                    tela.blit(peca_branca, (x - 30, y - 30))
        # Mostra pontuação atual
        p, b = contar_pecas(tabuleiro)
        texto = fonte.render(f"Pretas: {p}  Brancas: {b}", True, BRANCO_COR)
        tela.blit(texto, (10, 10))

    # =================== CONVERTE CLIQUE DO MOUSE PARA POSIÇÃO NO TABULEIRO ===================
    def clique_para_matriz(pos):
        x, y = pos
        return y // CEL, x // CEL  # Converte coordenadas de pixels para índices da matriz
    # =================== LOOP PRINCIPAL DO JOGO ===================
    rodando = True
    while rodando:
        for evento in pygame.event.get():
            if evento.type == pygame.QUIT:
                rodando = False
            # Quando o jogador clica com o mouse
            if evento.type == pygame.MOUSEBUTTONDOWN:
                l, c = clique_para_matriz(pygame.mouse.get_pos())# Converte clique para linha/coluna
                if validar_jogada(tabuleiro, l, c, jogador_atual):  # Verifica se a jogada é válida
                    antes = contar_pecas(tabuleiro)  # Conta peças antes da jogada
                    # Faz a jogada no tabuleiro
                    fazer_jogada(tabuleiro, l, c, jogador_atual)
                    x = c * CEL + CEL // 2
                    y = l * CEL + CEL // 2

                    animar_virar_peca(tela, x, y, jogador_atual == preto)
                    depois = contar_pecas(tabuleiro) # Conta peças depois da jogada
                    if antes != depois:
                        som_virar.play() # Toca som se a jogada alterou o tabuleiro
                    # troca jogador
                    jogador_atual *= -1
                    # Se o próximo jogador não tiver jogadas válidas, volta para o anterior
                    if not jogadas_validas(tabuleiro, jogador_atual):
                        jogador_atual *= -1
        desenhar() # Desenha tabuleiro atualizado
        pygame.display.flip() # Atualiza a tela
        # fim do jogo
        if fim_de_jogo(tabuleiro):
            rodando = False
    # mostrar vencedor (Fora do loop!)
    # =================== MOSTRAR VENCEDOR ===================
    vencedor = vencedor_da_partida(tabuleiro)
    tela.fill((0,0,0)) # Limpa tela
    if vencedor == preto:
        texto_final = fonte_final.render("Vencedor: Preto", True, (255,255,255))
    elif vencedor == computador:
        texto_final = fonte_final.render("Vencedor: Branco", True, (255,255,255))
    else:
        texto_final = fonte_final.render("Empate!", True, (255,255,255))

    rect = texto_final.get_rect(center=(LARGURA // 2, ALTURA // 2)) # Centraliza o texto
    tela.blit(texto_final, rect)
    pygame.display.flip()
    pygame.time.delay(2000)  # Mostra o vencedor por 2 segundos







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
            pygame.draw.circle(tela, (255, 100, 100), (x, y), 5)
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
        texto = fonte.render(f"Pretas: {p}  Brancas: {b}", True, BRANCO_COR)
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
                    # Se o jogador atual não tiver jogadas válidas, passa a vez
                if not jogadas_validas(tabuleiro, jogador_atual):
                        jogador_atual = computador if jogador_atual == preto else preto
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
        texto_final = fonte_final.render("Voce venceu!", True, (255, 255, 255))
    elif vencedor == computador:
        texto_final = fonte_final.render("Perdeste", True, (255, 255, 255))
    else:
        texto_final = fonte_final.render("Empate!", True, (255, 255, 255))

    # centraliza o texto
    rect = texto_final.get_rect(center=(LARGURA//2, ALTURA//2))
    tela.blit(texto_final, rect)

    pygame.display.flip()
    pygame.time.delay(3000)  # espera 3 segundos