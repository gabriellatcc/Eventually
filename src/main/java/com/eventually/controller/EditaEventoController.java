package com.eventually.controller;

import com.eventually.model.Comunidade;
import com.eventually.service.AlertaService;
import com.eventually.service.EditaEventoService;
import com.eventually.view.HomeView;
import com.eventually.view.modal.EditaEventoModal;
import javafx.scene.image.Image;
import javafx.stage.FileChooser;

import java.io.File;
import java.util.Set;

/**
 * Controller para a view EditaEventoModal.
 * Responsável por popular os dados iniciais do evento no formulário e por
 * lidar com as ações do usuário, delegando a lógica de salvamento para o EditaEventoService.
 * @version 1.02
 * @author Gabriella Tavares Costa Corrêa (Construção da documentação e revisão da parte lógica da estrutura)
 * @since 2025-07-01
 */
public class EditaEventoController {

    private final EditaEventoModal view;
    private final HomeView.EventoH eventoParaEditar;
    private final EditaEventoService editaEventoService;
    private AlertaService alertaService = new AlertaService();
    private Runnable aoFecharCallback;
    /**
     * Construtor que estabelece as conexões entre a View, o Model e o Service.
     *
     * @param view             A instância do modal de edição.
     * @param eventoParaEditar O objeto de evento a ser modificado.
     * @param aoSalvarCallback
     */
    public EditaEventoController(EditaEventoModal view, HomeView.EventoH eventoParaEditar, Runnable aoSalvarCallback) {
        this.aoFecharCallback = aoSalvarCallback;
        this.view = view;
        this.eventoParaEditar = eventoParaEditar;
        this.editaEventoService = EditaEventoService.getInstance();
        initialize();
    }

    /**
     * Método de inicialização que orquestra a configuração do controller.
     */
    private void initialize() {
        popularCamposComDadosAtuais();
        configurarAcoesDosBotoes();
    }

    /**
     * Preenche os campos do formulário com os dados atuais do evento.
     * Para campos de texto, os dados são inseridos no prompt text como uma dica.
     * Para outros campos, o valor é selecionado diretamente.
     */
    private void popularCamposComDadosAtuais() {
        view.getFldNomeEvento().setPromptText("Atual: " + eventoParaEditar.titulo());
        view.getTaDescricao().setPromptText("Atual: " + eventoParaEditar.descricao());
        view.getTaLocalizacao().setPromptText("Atual: " + eventoParaEditar.local());

        view.getFldNParticipantes().setText(String.valueOf(eventoParaEditar.capacidade()));
        view.getFldLink().setPromptText("Atual: " + eventoParaEditar.linkAcesso());
        view.getDatePickerStart().setValue(eventoParaEditar.dataI());
        view.getDatePickerEnd().setValue(eventoParaEditar.dataF());
        view.getFldHoraInicio().setPromptText("Atual: " + eventoParaEditar.horaI());
        view.getFldHoraFinal().setPromptText("Atual: " + eventoParaEditar.horaF());

        if (eventoParaEditar.imagem() != null) {
            view.setPreviewImage(eventoParaEditar.imagem());
        }

        switch (eventoParaEditar.formato()) {
            case "presencial":
                view.getRadioPresencial().setSelected(true);
                break;
            case "online":
                view.getRadioOnline().setSelected(true);
                break;
            case "híbrido":
                view.getRadioHibrido().setSelected(true);
                break;
        }

        Set<String> tagsAtuais = eventoParaEditar.preferencias();
        if (tagsAtuais.contains(Comunidade.CORPORATIVO)) view.getCbCorporativo().setSelected(true);
        if (tagsAtuais.contains(Comunidade.BENEFICENTE)) view.getCbBeneficente().setSelected(true);
        if (tagsAtuais.contains(Comunidade.EDUCACIONAL)) view.getCbEducacional().setSelected(true);
        if (tagsAtuais.contains(Comunidade.CULTURAL)) view.getCbCultural().setSelected(true);
        if (tagsAtuais.contains(Comunidade.ESPORTIVO)) view.getCbEsportivo().setSelected(true);
        if (tagsAtuais.contains(Comunidade.RELIGIOSO)) view.getCbReligioso().setSelected(true);
        if (tagsAtuais.contains(Comunidade.SOCIAL)) view.getCbSocial().setSelected(true);
    }

    /**
     * Configura todos os listeners de eventos para os componentes interativos da view.
     */
    private void configurarAcoesDosBotoes() {
        view.getBtnSalvar().setOnAction(e -> handleSalvar());
        view.getBtnCancelar().setOnAction(e -> view.close());
        view.getBtnEscolherImagem().setOnAction(e -> handleEscolherImagem());
        view.getBtnIncrement().setOnAction(e -> handleIncremento(true));
        view.getBtnDecrement().setOnAction(e -> handleIncremento(false));
    }

    /**
     * Lida com o clique no botão Salvar. Delega a lógica de atualização para o serviço,
     * exibe uma confirmação e fecha o modal.
     */
    private void handleSalvar() {
        int idDoEvento = eventoParaEditar.id();

        editaEventoService.atualizarEvento(idDoEvento, view);

        alertaService.alertarInfo("Atualizado com sucesso!");

        if (aoFecharCallback != null) {
            aoFecharCallback.run();
        }

        view.close();
    }

    /**
     * Abre um FileChooser para que o usuário selecione uma nova imagem para o evento.
     * Atualiza a pré-visualização na interface.
     */
    private void handleEscolherImagem() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Escolha uma imagem para o evento");
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("Imagens", "*.png", "*.jpg", "*.jpeg", "*.gif"),
                new FileChooser.ExtensionFilter("Todos os Arquivos", "*.*")
        );
        File arquivo = fileChooser.showOpenDialog(view.getScene().getWindow());

        if (arquivo != null) {
            Image novaImagem = new Image(arquivo.toURI().toString());
            view.setPreviewImage(novaImagem);
            view.setArquivoSelecionado(arquivo);
        }
    }

    /**
     * Lida com os cliques nos botões de incremento e decremento da capacidade de participantes.
     * @param incrementar true para incrementar, false para decrementar.
     */
    private void handleIncremento(boolean incrementar) {
        try {
            int contagemAtual = Integer.parseInt(view.getFldNParticipantes().getText());
            if (incrementar) {
                contagemAtual++;
            } else {
                if (contagemAtual > 0) {
                    contagemAtual--;
                }
            }
            view.getFldNParticipantes().setText(String.valueOf(contagemAtual));
        } catch (NumberFormatException e) {
            view.getFldNParticipantes().setText("0");
        }
    }
}