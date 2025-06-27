package com.eventually.controller;

import com.eventually.service.AlertaService;
import com.eventually.service.UsuarioAtualizacaoService;
import com.eventually.service.UsuarioSessaoService;
import com.eventually.view.MudancaImagemModal;
import com.eventually.view.SettingsView;
import javafx.scene.image.Image;
import javafx.stage.FileChooser;
import javafx.stage.Window;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;

/**
 * Controller para a lógica do modal de seleção de imagem.
 * Gerencia as ações de abrir o seletor de arquivos, pré-visualizar e salvar.
 * @author Gabriella Tavares Costa Corrêa (Construção da documentação, da classe e revisão da parte lógica da estrutura)
 * @version 1.0
 * @since 2025-06-24
 */
public class MudancaImagemController {
    private final MudancaImagemModal mudancaImagemModal;

    private UsuarioAtualizacaoService usuarioAtualizacaoService;
    private UsuarioSessaoService usuarioSessaoService;
    private SettingsView settingsView;

    private String emailRecebido, valorRecebido;

    private File arquivoFinal;

    private Image imageFinal;

    private AlertaService alerta=new AlertaService();

    private static final Logger sistemaDeLogger = LoggerFactory.getLogger(MudancaImagemController.class);

    /**
     * Construtor que associa o controller à sua respectiva view.
     * @param mudancaImagemModal a instância do modal MudancaImagemModal associada.
     */
    public MudancaImagemController(SettingsView settingsView, String email, String valor, MudancaImagemModal mudancaImagemModal) {
        this.settingsView = settingsView;
        this.usuarioAtualizacaoService = UsuarioAtualizacaoService.getInstancia();
        this.usuarioSessaoService = UsuarioSessaoService.getInstancia();
        sistemaDeLogger.info("Inicializado e conectado ao SettingsView, UsuarioAtualizacaoService, UsuarioCadastroService e UsuarioSessaoService");

        this.emailRecebido = email;
        this.valorRecebido = valor;
        this.mudancaImagemModal = mudancaImagemModal;
        this.mudancaImagemModal.setMudancaImagemController(this);
        this.arquivoFinal = null;
        configManipuladoresMudaImagem();
    }

    /**
     * Configura os manipuladores de evento para os componentes do modal de alteração de foto do usuário
     * Este método associa as ações dos botões e do modal e, em caso de falha na configuração
     * de algum manipulador de evento, uma mensagem de erro é exibida no console.
     */
    private void configManipuladoresMudaImagem() {
        sistemaDeLogger.info("Método configManipuladoresMudaImagem() chamado.");
        try {
            mudancaImagemModal.getBtnEscolherImagem().setOnAction(event -> processarEscolherImagem());
            mudancaImagemModal.getBtnSalvar().setOnAction(event -> processarSalvar());
            mudancaImagemModal.getBtnFechar().setOnAction(event -> processarFecharModal());
        } catch (Exception e) {
            sistemaDeLogger.error("Erro ao configurar manipuladores de mudança: " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * Lida com o clique no botão "Escolher Arquivo", abre um FileChooser para que o usuário possa selecionar um
     * arquivo de imagem, valida se a imagem selecionada tem exatamente 200x200 pixels e, em caso de falha, exibe uma
     * mensagem no console.
     */
    private void processarEscolherImagem() {
        sistemaDeLogger.info("Método processarEscolherImagem() chamado.");
        try {
            FileChooser fileChooser = new FileChooser();
            fileChooser.setTitle("Selecione uma Imagem (200x200 pixels)");

            fileChooser.getExtensionFilters().addAll(
                    new FileChooser.ExtensionFilter("Imagens", "*.png", "*.jpg", "*.jpeg", "*.gif", "*.bmp"),
                    new FileChooser.ExtensionFilter("Todos os Arquivos", "*.*")
            );

            Window ownerWindow = mudancaImagemModal.getScene().getWindow();
            File arquivoSelecionado = fileChooser.showOpenDialog(ownerWindow);

            if (arquivoSelecionado != null) {
                Image imagemSelecionada = new Image(arquivoSelecionado.toURI().toString());

                if (imagemSelecionada.getWidth() == 200 && imagemSelecionada.getHeight() == 200) {
                    sistemaDeLogger.info("Imagem válida (200x200) selecionada: " + arquivoSelecionado.getName());
                    imageFinal = imagemSelecionada;
                    mudancaImagemModal.setPreviewImage(imageFinal);
                    mudancaImagemModal.setArquivoSelecionado(arquivoSelecionado);
                } else {
                    sistemaDeLogger.warn("Imagem com dimensões inválidas selecionada: " +
                            (int) imagemSelecionada.getWidth() + "x" + (int) imagemSelecionada.getHeight());

                    mudancaImagemModal.setPreviewImage(null);
                    mudancaImagemModal.setArquivoSelecionado(null);
                    this.imageFinal = null;
                    this.arquivoFinal = null;

                    int largura = (int) imagemSelecionada.getWidth();
                    int altura = (int) imagemSelecionada.getHeight();
                    String mensagem = String.format("A imagem selecionada tem %d de largura e %d de altura.", largura, altura);

                    alerta.alertarWarn("Imagem com Tamanho Inválido",mensagem);
                }
            }
        } catch (Exception e) {
            sistemaDeLogger.error("Erro ao processar a escolha da imagem: " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * Lida com o clique no botão "Salvar", armazena o arquivo selecionado na variável 'arquivoFinal', fecha o modal e,
     * em caso de falha, é exibida uma mensagem na tela.
     */
    private void processarSalvar() {
        sistemaDeLogger.info("Método processarSalvar() chamado.");
        try {
            this.arquivoFinal = mudancaImagemModal.getArquivoSelecionado();
            if (this.arquivoFinal != null) {
                settingsView.setAvatarImagem(imageFinal);

                int id = usuarioSessaoService.procurarID(emailRecebido);
                usuarioAtualizacaoService.atualizarFoto(id, imageFinal);

                System.out.println("Imagem salva (referência): " + this.arquivoFinal.getAbsolutePath());
                mudancaImagemModal.close();
            }
        } catch (Exception e) {
            sistemaDeLogger.error("Algum erro ocorreu ao salvar a foto: " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * Retorna o arquivo que foi confirmado pelo usuário ao clicar em "Salvar".
     * Se o usuário fechou o modal sem salvar, retorna null.
     * @return O objeto File da imagem selecionada, ou null se nenhuma imagem foi salva.
     */
    public File getArquivoFinal() {return arquivoFinal;}

    /**
     * Fecha o modal e, em caso de falha uma mensagem de erro é exibida no console.
     * @return nulo para que o modal finalize sua exibição e operação.
     */
    private void processarFecharModal() {
        sistemaDeLogger.info("Método processarFecharModal() chamado.");
        try {
            mudancaImagemModal.close();
        } catch (Exception e) {
            sistemaDeLogger.error("Erro ao fechar o modal: " + e.getMessage());
        }
    }
}