package com.eventually.controller;

import com.eventually.dto.EventoEdicaoDto;
import com.eventually.model.Comunidade;
import com.eventually.model.FormatoSelecionado;
import com.eventually.service.AlertaService;
import com.eventually.service.EventoEdicaoService;
import com.eventually.view.HomeView;
import com.eventually.view.modal.EditaEventoModal;
import javafx.scene.image.Image;
import javafx.stage.FileChooser;

import java.io.File;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.HashSet;
import java.util.Set;

/**
 * Controller para a view EditaEventoModal.
 * Responsável por popular os dados iniciais do evento no formulário e por
 * lidar com as ações do usuário, delegando a lógica de salvamento para o EventoEdicaoService.
 * @version 1.04
 * @author Gabriella Tavares Costa Corrêa (Construção da documentação e revisão da parte lógica da estrutura)
 * @since 2025-07-01
 */
public class EditaEventoController {
    private final EditaEventoModal view;
    private Image novaImagemSelecionada = null;

    private final HomeView.EventoH eventoParaEditar;
    private final EventoEdicaoService eventoEdicaoService;
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
        this.eventoEdicaoService = EventoEdicaoService.getInstance();
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
     * Lida com o clique no botão Salvar. Coleta todos os dados da view,
     * cria um DTO e delega a lógica de atualização para o serviço.
     */
    private void handleSalvar() {
        int idDoEvento = eventoParaEditar.id();

        String nome = view.getFldNomeEvento().getText();
        String descricao = view.getTaDescricao().getText();
        String localizacao = view.getTaLocalizacao().getText();
        String link = view.getFldLink().getText();
        int capacidade = Integer.parseInt(view.getFldNParticipantes().getText());

        FormatoSelecionado formato = converterFormato();
        Set<Comunidade> comunidades = converterComunidades();

        LocalDate dataInicio = view.getDatePickerStart().getValue();
        LocalDate dataFim = view.getDatePickerEnd().getValue();

        String horaInicioStr = view.getFldHoraInicio().getText().isBlank() ? eventoParaEditar.horaI() : view.getFldHoraInicio().getText();
        String horaFimStr = view.getFldHoraFinal().getText().isBlank() ? eventoParaEditar.horaF() : view.getFldHoraFinal().getText();
        LocalTime horaInicio = LocalTime.parse(horaInicioStr);
        LocalTime horaFim = LocalTime.parse(horaFimStr);

        Image novaImagem = this.novaImagemSelecionada;

        EventoEdicaoDto dto = new EventoEdicaoDto(
                nome, descricao, localizacao, link, capacidade, formato, comunidades,
                dataInicio, horaInicio, dataFim, horaFim, novaImagem
        );

        eventoEdicaoService.atualizarEvento(idDoEvento, dto);

        alertaService.alertarInfo("Evento atualizado com sucesso!");

        if (aoFecharCallback != null) {
            aoFecharCallback.run();
        }
        view.close();
    }

    /**
     * Método auxiliar para converter o estado dos RadioButtons para o Enum FormatoSelecionado.
     */
    private FormatoSelecionado converterFormato() {
        if (view.getRadioPresencial().isSelected()) return FormatoSelecionado.PRESENCIAL;
        if (view.getRadioOnline().isSelected()) return FormatoSelecionado.ONLINE;
        if (view.getRadioHibrido().isSelected()) return FormatoSelecionado.HIBRIDO;
        return FormatoSelecionado.valueOf(eventoParaEditar.formato().toUpperCase());
    }

    /**
     * Método auxiliar para converter o estado dos CheckBoxes para um Set de Enums Comunidade.
     */
    private Set<Comunidade> converterComunidades() {
        Set<Comunidade> selecionadas = new HashSet<>();
        if (view.getCbCorporativo().isSelected()) selecionadas.add(Comunidade.CORPORATIVO);
        if (view.getCbBeneficente().isSelected()) selecionadas.add(Comunidade.BENEFICENTE);
        if (view.getCbEducacional().isSelected()) selecionadas.add(Comunidade.EDUCACIONAL);
        if (view.getCbCultural().isSelected()) selecionadas.add(Comunidade.CULTURAL);
        if (view.getCbEsportivo().isSelected()) selecionadas.add(Comunidade.ESPORTIVO);
        if (view.getCbReligioso().isSelected()) selecionadas.add(Comunidade.RELIGIOSO);
        if (view.getCbSocial().isSelected()) selecionadas.add(Comunidade.SOCIAL);
        return selecionadas;
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

            this.novaImagemSelecionada = novaImagem;

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