package com.eventually.controller;

import com.eventually.dto.CriarEventoDto;
import com.eventually.dto.PreferenciaFormatoDto;
import com.eventually.dto.PreferenciasUsuarioDto;
import com.eventually.service.AlertaService;
import com.eventually.service.EventoCriacaoService;
import com.eventually.view.CriaEventoModal;
import javafx.scene.image.Image;
import javafx.stage.FileChooser;
import javafx.stage.Window;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.time.LocalDate;
import java.time.LocalTime;

/** PASSÍVEL DE ALTERAÇÕES
 * Classe responsável pela comunicação do modal de "Criar evento" com o backend.
 * @author Yuri Garcia Maia (Estrutura base)
 * @version 1.07
 * @since 2025-06-18
 * @author Gabriella Tavares Costa Corrêa (Revisão de documentação, estrutura e refatoração da parte lógica da classe)
 * @since 2025-06-19
 */
public class CriaEventoController {
    private final CriaEventoModal criaEventoModal;
    private EventoCriacaoService eventoCriacaoService;

    private String emailRecebido;

    private File arquivoFinal;
    private Image imageFinal;

    private Runnable onSucessoCallback;

    private AlertaService alerta =new AlertaService();

    private static final Logger sistemaDeLogger = LoggerFactory.getLogger(CriaEventoController.class);

    /**
     * Construtor do CriaEventoController.
     * @param criaEventoModal A instância da CriaEventoModal associada.
     */
    public CriaEventoController(String emailRecebido, CriaEventoModal criaEventoModal) {
        this.eventoCriacaoService = EventoCriacaoService.getInstancia();
        sistemaDeLogger.info("Inicializado e conectado ao EventoCriacaoService.");

        this.emailRecebido = emailRecebido;

        this.criaEventoModal = criaEventoModal;
        this.criaEventoModal.setCriaEventoController(this);
        configManipuladoresEventoCriaEvento();
    }

    /**
     * Define uma ação (callback) a ser executada quando o evento é criado com sucesso.
     * @param callback A ação a ser executada.
     */
    public void setOnSucesso(Runnable callback) {
        this.onSucessoCallback = callback;
    }
    /**
     * Configura os manipuladores de evento para os componentes do modal criação de evento.
     * Este método associa as ações dos botões e do modal e, em caso de falha na configuração
     * de algum manipulador de evento, uma mensagem de erro é exibida no console.
     */
    private void configManipuladoresEventoCriaEvento() {
        sistemaDeLogger.info("Método configManipuladoresEventoCriaEvento() chamado.");
        try {
            criaEventoModal.getFldNParticipantes().textProperty().addListener((observable, oldValue, newValue) -> {
                if (!newValue.matches("\\d*")) {
                    criaEventoModal.getFldNParticipantes().setText(newValue.replaceAll("[^\\d]", ""));
                }
            });
            criaEventoModal.getBtnEscolherImagem().setOnAction(event -> processarEscolherImagem());

            criaEventoModal.getBtnDecrement().setOnAction(e -> atualizarCapacidade(false));
            criaEventoModal.getBtnIncrement().setOnAction(e -> atualizarCapacidade(true));
            criaEventoModal.getBtnCriar().setOnAction(e-> processarCriarEvento());
            criaEventoModal.getBtnCancelar().setOnAction(e -> processarFecharModal());
        } catch (Exception e) {
            sistemaDeLogger.error("Erro ao configurar manipuladores de criação de evento: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public void processarCriarEvento() {
        sistemaDeLogger.info("Método processarCriarEvento() chamado.");
        try {
            String nomeEvento = criaEventoModal.getFldNomeEvento().getText();
            String descricaoEvento = criaEventoModal.getTaDescricao().getText();
            PreferenciaFormatoDto preferenciaFormato = new PreferenciaFormatoDto(
                    criaEventoModal.getRadioPresencial().isSelected(),
                    criaEventoModal.getRadioOnline().isSelected(),
                    criaEventoModal.getRadioHibrido().isSelected()
            );

            String linkEvento = criaEventoModal.getFldLink().getText();
            String localizacaoEvento = criaEventoModal.getTaLocalizacao().getText();

            Image fotoEvento = this.imageFinal;

            int nParticipantes = criaEventoModal.getParticipantCount();

            String textoDaHora1 = criaEventoModal.getFldHoraInicio().getText();
            LocalTime horaInicial = LocalTime.parse(textoDaHora1);

            LocalDate diaInicial = criaEventoModal.getDatePickerStart().getValue();

            String textoDaHora2 = criaEventoModal.getFldHoraFinal().getText();
            LocalTime horaFinal = LocalTime.parse(textoDaHora2);

            LocalDate diaFinal = criaEventoModal.getDatePickerEnd().getValue();

            PreferenciasUsuarioDto preferenciasEvento = new PreferenciasUsuarioDto(
                    criaEventoModal.getCbCorporativo().isSelected(),
                    criaEventoModal.getCbBeneficente().isSelected(),
                    criaEventoModal.getCbEducacional().isSelected(),
                    criaEventoModal.getCbCultural().isSelected(),
                    criaEventoModal.getCbEsportivo().isSelected(),
                    criaEventoModal.getCbReligioso().isSelected(),
                    criaEventoModal.getCbSocial().isSelected()
            );

            CriarEventoDto dto= new CriarEventoDto(emailRecebido, nomeEvento, descricaoEvento, preferenciaFormato,nParticipantes,horaInicial,diaInicial,horaFinal,diaFinal,preferenciasEvento);

            boolean criacaoFoiOk = eventoCriacaoService.criarEventoSeValido(dto, linkEvento, localizacaoEvento, fotoEvento);

            if (criacaoFoiOk) {
                sistemaDeLogger.info("EventoH criado com sucesso.");
                if (onSucessoCallback != null) {
                    onSucessoCallback.run();
                }
                processarFecharModal();
            }
        } catch (Exception ex) {
            sistemaDeLogger.error("Erro ao criar o evento: " + ex.getMessage());
            ex.printStackTrace();
            alerta.alertarErro("Erro ao criar o evento.");
        }
    }

    private void atualizarCapacidade(boolean incrementa) {
        try {
            int currentValue = Integer.parseInt(criaEventoModal.getFldNParticipantes().getText());
            if (incrementa) {
                currentValue++;
            } else {
                if (currentValue > 0) {
                    currentValue--;
                }
            }
            criaEventoModal.getFldNParticipantes().setText(String.valueOf(currentValue));
        } catch (NumberFormatException e) {
            criaEventoModal.getFldNParticipantes().setText("0");
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

            Window ownerWindow = criaEventoModal.getScene().getWindow();
            File arquivoSelecionado = fileChooser.showOpenDialog(ownerWindow);

            if (arquivoSelecionado != null) {
                Image imagemSelecionada = new Image(arquivoSelecionado.toURI().toString());

                double larguraImagem = imagemSelecionada.getWidth();
                double alturaImagem = imagemSelecionada.getHeight();
                double proporcaoDesejada = 16.0 / 10.0;
                double proporcaoImagem = larguraImagem / alturaImagem;
                double tolerancia = 0.01;

                if (Math.abs(proporcaoImagem - proporcaoDesejada) <= tolerancia) {
                    sistemaDeLogger.info("Imagem válida (proporção 16:10) selecionada: " + arquivoSelecionado.getName() + " (" + (int)larguraImagem + "x" + (int)alturaImagem + ")");
                    imageFinal = imagemSelecionada;
                    criaEventoModal.setPreviewImage(imageFinal);
                    this.arquivoFinal = arquivoSelecionado;

                    criaEventoModal.setArquivoSelecionado(arquivoSelecionado);
                } else {
                    sistemaDeLogger.warn("Imagem com proporção inválida selecionada: " +
                            (int) imagemSelecionada.getWidth() + "x" + (int) imagemSelecionada.getHeight() +
                            " (proporção: " + String.format("%.2f", proporcaoImagem) + ", esperada: " + proporcaoDesejada + ")");

                    this.imageFinal = null;
                    criaEventoModal.setPreviewImage(null);
                    criaEventoModal.setArquivoSelecionado(null);
                    this.arquivoFinal = null;

                    String mensagem = String.format("A imagem selecionada tem proporção %.2f, mas a proporção esperada é %.1f:%1.f.",
                            proporcaoImagem, 16.0, 10.0);
                    alerta.alertarWarn("Imagem com Proporção Inválida",mensagem);
                }
            }
        } catch (Exception e) {
            sistemaDeLogger.error("Erro ao processar a escolha da imagem: " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * Fecha o modal e, em caso de falha uma mensagem de erro é exibida no console.
     * @return nulo para que o modal finalize sua exibição e operação.
     */
    private void processarFecharModal() {
        sistemaDeLogger.info("Método processarFecharModal() chamado.");
        try {
            criaEventoModal.close();
        } catch (Exception e) {
            sistemaDeLogger.error("Erro ao fechar o modal: " + e.getMessage());
        }
    }

}