import pygame
import sys

pygame.init()

# =================== TELA ===================
LARGURA, ALTURA = 800, 600
tela = pygame.display.set_mode((LARGURA, ALTURA))
pygame.display.set_caption("Escolha o Modo de Jogo")
# =================== FUNDO ===================
fundo = pygame.image.load("image/reve.png")
fundo = pygame.transform.scale(fundo, (LARGURA, ALTURA))
# =================== CORES ===================
BRANCO = (255, 255, 255)
PRETO = (0, 0, 0)
CINZA = (200, 200, 200)
AZUL = (120, 160, 255)
# =================== FONTES ===================
fonte_titulo = pygame.font.SysFont("arial", 60, True)
fonte_botao = pygame.font.SysFont("arial", 32, True)

clock = pygame.time.Clock()

# =================== BOTÕES ===================
botao_hxh = pygame.Rect(250, 220, 300, 70)
botao_hxc = pygame.Rect(250, 320, 300, 70)
botao_sair = pygame.Rect(250, 420, 300, 70)

# =================== FUNÇÃO MENU ===================
def menu_modo():
    y_texto = 140
    direcao = 1
    velocidade =0.46
    global fundo
    while True:
        mouse = pygame.mouse.get_pos()

        for event in pygame.event.get():
            if event.type == pygame.QUIT:
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
        # fundo
        tela.fill(BRANCO)
        tela.blit(fundo,(0,0))
        # título
        y_texto += direcao * velocidade
        if y_texto > 155 or y_texto < 125:
            direcao *= -1
        titulo = fonte_titulo.render("REVERSSI", True, BRANCO)
        tela.blit(titulo, titulo.get_rect(center=(LARGURA//2, int(y_texto))))

        # hover
        cor_hxh = AZUL if botao_hxh.collidepoint(mouse) else CINZA
        cor_hxc = AZUL if botao_hxc.collidepoint(mouse) else CINZA
        cor_sair = AZUL if botao_sair.collidepoint(mouse) else CINZA

        # desenhar botões
        pygame.draw.rect(tela, cor_hxh, botao_hxh, border_radius=12)
        pygame.draw.rect(tela, cor_hxc, botao_hxc, border_radius=12)
        pygame.draw.rect(tela, cor_sair, botao_sair, border_radius=12)

        # texto dos botões
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

        pygame.display.update()
        clock.tick(60)

# ================= EXECUÇÃO =================
if __name__ == "__main__":
    modo = menu_modo()
    pygame.quit()
    print("Modo escolhido:", modo)
