import pygame
import sys
pygame.init()

# =================== TELA ===================
LARGURA, ALTURA = 800, 600 # tamanho do display
tela = pygame.display.set_mode((LARGURA, ALTURA)) # cria display tamanho definido
pygame.display.set_caption("Escolha o Modo de Jogo")
# =================== FUNDO ===================
fundo = pygame.image.load("image/reve.png")
fundo = pygame.transform.scale(fundo, (LARGURA, ALTURA))# ajusta a imagem para tela inteira
# =================== CORES ===================
BRANCO = (255, 255, 255)
PRETO = (0, 0, 0)
CINZA = (200, 200, 200)
AZUL = (120, 160, 255)
# =================== FONTES ===================
fonte_titulo = pygame.font.SysFont("arial", 60, True)
fonte_botao = pygame.font.SysFont("arial", 32, True)
#relogio controlar taxa atualizacao display
clock = pygame.time.Clock()

# =================== BOTÕES ===================
#botoes retangulos (x,y,largura,altura)
botao_hxh = pygame.Rect(250, 220, 300, 70)
botao_hxc = pygame.Rect(250, 320, 300, 70)
botao_sair = pygame.Rect(250, 420, 300, 70)

# =================== FUNÇÃO MENU ===================
#vai exibir o menu de escolha e vai retornar mode de jogo escolhido
def menu_modo():
    y_texto = 140 #pos inicai vertical do titulo
    direcao = 1 #direcao movimento vertical do titulo (1=desce, -1 = sobe)
    velocidade =0.46 # vleocidade animacao titulo
    global fundo
    # =================== LOOP PRINCIPAL ===================
    while True:
        mouse = pygame.mouse.get_pos()
#===========+++gerenciar os eventos==============
        for event in pygame.event.get():
            if event.type == pygame.QUIT: #fecha jogo se clicar no x da janela
                pygame.quit()
                sys.exit()
            from M_grafics import utilizadores,utilizador
            if event.type == pygame.MOUSEBUTTONDOWN:
                if botao_hxh.collidepoint(mouse):
                    return utilizadores()   # Humano vs Humano

                if botao_hxc.collidepoint(mouse):
                    return utilizador()   # Humano vs Computador

                if botao_sair.collidepoint(mouse):
                    pygame.quit()
                    sys.exit()
        # =================== DESENHO DO MENU ===================
        tela.fill(BRANCO)  # Preenche a tela com branco
        tela.blit(fundo,(0,0))  # Desenha a imagem de fundo
        # título
        y_texto += direcao * velocidade
        if y_texto > 155 or y_texto < 125: # Faz o título tocar dois limites
            direcao *= -1
        titulo = fonte_titulo.render("REVERSSI", True, BRANCO)
        tela.blit(titulo, titulo.get_rect(center=(LARGURA//2, int(y_texto))))# Centraliza o título
        # =================== CORES DOS BOTÕES ===================
        # Muda a cor do botão quando o mouse está sobre ele
        cor_hxh = AZUL if botao_hxh.collidepoint(mouse) else CINZA
        cor_hxc = AZUL if botao_hxc.collidepoint(mouse) else CINZA
        cor_sair = AZUL if botao_sair.collidepoint(mouse) else CINZA

        # desenhar botões de escolha
        pygame.draw.rect(tela, cor_hxh, botao_hxh, border_radius=12)
        pygame.draw.rect(tela, cor_hxc, botao_hxc, border_radius=12)
        pygame.draw.rect(tela, cor_sair, botao_sair, border_radius=12)

        # Coloca texto dentro dos botões
        tela.blit(
            fonte_botao.render('MULTIPLAYES', True, PRETO),
            (botao_hxh.x + 38, botao_hxh.y + 20)
        )

        tela.blit(
            fonte_botao.render("COM BOOT", True, PRETO),
            (botao_hxc.x + 65, botao_hxc.y + 20)
        )

        tela.blit(
            fonte_botao.render("SAIR", True, PRETO),
            (botao_sair.x + 115, botao_sair.y + 20)
        )
        # Atualiza a tela
        pygame.display.update()
        # Limitar FPS em 60
        clock.tick(60)

# ================= EXECUÇÃO =================
if __name__ == "__main__":
    modo = menu_modo()# Chama o menu e espera o jogador escolher
    pygame.quit()  # Fecha o Pygame
    print("Modo escolhido:", modo) # Mostra no terminal o modo escolhido
