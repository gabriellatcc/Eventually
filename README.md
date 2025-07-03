# 📆 Eventually - Sistema de Participação de Eventos
Esta aplicação foi desenvolvida como parte do projeto da disciplina de **Estrutura de Dados** da **FATEC - Prof. Waldomiro May**.

O **Eventually** é uma plataforma desenvolvida para organizar e facilitar a participação de usuários em eventos, oferecendo uma interface clara para visualização, cadastro e gerenciamento de informações. A aplicação permite o registro de usuários e eventos, a inscrição de participantes, a definição de categorias e o controle detalhado por meio de operações CRUD bem estruturadas.

A modelagem dos dados foi feita utilizando as bibliotecas `ArrayList` e `Set<>`, sem o uso de banco de dados, revisando padrões de construção e práticas da disciplina de estrutura de dados.

## ⚙️ Tecnologias Utilizadas

- **Java** (linguagem principal da aplicação)
- **JavaFx** (framework para interface gráfica)
- **CSS** (personalização visual da interface JavaFX)
- **Maven** (ferramenta de build)
- **Intellij** (IDE)

## 📌 Funcionalidades básicas

- **Cadastro e edição de usuários.**  
- **Inscricão e cancelamento de participacao de usuários em eventos.**  
- **Criação, edição, exclusão e vizualização de eventos.**  
- **Controle de programação de eventos.**  
- **Filtros e ordenação de dados de eventos.**  
- **Validações para evitar duplicidade de usuários e eventos.**

## 📃 Armazenamento de dados

Neste projeto, o armazenamento de dados é realizada **em memória**, utilizando as bibliotecas `ArrayList` e `Set` da linguagem Java, sem uso de banco de dados. Abaixo estão alguns trechos de código exemplificando as principais operações de armazenamento, após os dados inseridos nas interfaces serem recebidos pelos controladores e validados pelo serviços:


### ☕ Classe [UsuarioCadastroService.java](https://github.com/gabriellatcc/Eventually/blob/main/src/main/java/com/eventually/service/UsuarioCadastroService.java): CREATE do sistema.

📍 **Criação de usuário**
```java
 public void criarUsuario(CadastrarUsuarioDto dto){
    try {
        Set<TemaPreferencia> comunidades = MapeamentoPreferenciasService.mapearPreferencias(dto.preferencias());

        UsuarioModel novoUsuario = new UsuarioModel(
                dto.nomePessoa(),
                dto.email(),
                dto.senha(),
                dto.localizacaoUsuario(),
                dto.data(),
                null,
                new ArrayList<>(),
                new ArrayList<>(),
                comunidades,
                true
        );
        adicionarUsuario(novoUsuario);
    }
    catch (RuntimeException e) {
        sistemaDeLogger.error("Erro ao criar usuario: " + e.getMessage());
        e.printStackTrace();
        alertaService.alertarErro("Erro ao criar usuario.");
    }
}
```

### ☕ Classe [UsuarioSessaoService.java](https://github.com/gabriellatcc/Eventually/blob/main/src/main/java/com/eventually/service/UsuarioSessaoService.java): READ do sistema.

📍 **Busca de usuário no sistema: dentro da classe é possível buscar de objetos a atributos de objetos.**
```java
 public UsuarioModel procurarUsuario(String email) {
    try{
        Optional<UsuarioModel> usuarioOptional = usuarioCadastroService.getAllUsuarios()
                .stream()
                .filter(usuario -> usuario.getEmail().equalsIgnoreCase(email))
                .findFirst();
        if (usuarioOptional.isPresent()) {
            return usuarioOptional.get();
        }
        else {return null;}
    } catch (Exception e) {
        sistemaDeLogger.info("Erro ao procurar o usuário pelo email: " + e.getMessage());
        e.printStackTrace();
        return null;
    }
}
```
### ☕ Classe [UsuarioAtualizacaoService.java](https://github.com/gabriellatcc/Eventually/blob/main/src/main/java/com/eventually/service/UsuarioAtualizacaoService.java): UPDATE do sistema

📍 **Atualização de atributo do usuário: no exemplo a seguir, o sistema recebe o novo valor de email, busca se já está presente no sistema e, se estiver, não permite a alteração do email registrado na conta do usuário.**
```java
 public boolean atualizarEmail(int idUsuario, String novoEmail) {
    Optional<UsuarioModel> usuarioOpt = buscarUsuarioParaAtualizacao(idUsuario);
    if (usuarioOpt.isEmpty()) {
        return false;
    }

    Optional<UsuarioModel> emailExistenteOpt = usuarioSessaoService.buscarUsuarioPorEmail(novoEmail);

    if (emailExistenteOpt.isPresent() && emailExistenteOpt.get().getId() != idUsuario) {
        alertaService.alertarWarn("Email Indisponível", "Este email já está cadastrado para outro usuário.");
        sistemaDeLogger.warn("Tentativa de alterar email para um já existente ('{}') pelo usuário ID {}.", novoEmail, idUsuario);
        return false;
    }

    if (usuarioCadastroService.isRegraEmailCumprida(novoEmail)) {
        usuarioOpt.get().setEmail(novoEmail);
        notificarSucesso("Email", idUsuario);
        return true;
    } else {
        alertaService.alertarWarn("Edição Inválida", "O formato do email fornecido é inválido.");
        return false;
    }
}
```

### ☕ Classe [UsuarioExclusaoService.java](https://github.com/gabriellatcc/Eventually/blob/main/src/main/java/com/eventually/service/UsuarioExclusaoService.java): DELETE do sistema

📍 **Remoção de usuário: Como nenhum usuário pode ser excluído do sistema, de fato, então ele tem o estado alterado para false (INATIVO). Esta ação define que o usuário não será capaz de realizar uma sesssão no sistema com o email da conta dada como não ativa.**
```java
  public boolean alterarEstadoDoUsuario(int idUsuario, boolean novoEstado) {
    try {
        Optional<UsuarioModel> usuarioOptional = usuarioCadastroService.buscarUsuarioPorId(idUsuario);
        if (usuarioOptional.isPresent()) {
            UsuarioModel usuario = usuarioOptional.get();
            usuario.setEstadoDoUsuario(novoEstado);
            sistemaDeLogger.info("Estado do usuário com ID " + idUsuario + " alterado para " + (novoEstado ? "ATIVO" : "INATIVO") + ".");
            alertaService.alertarInfo("Sucesso: Estado do usuário alterado!");
            return true;
        } else {
            alertaService.alertarWarn("Alteração de Estado Inválida", "Usuário com ID " + idUsuario + " não encontrado.");
            sistemaDeLogger.info("Usuário com ID " + idUsuario + " não encontrado para alterar estado.");
            return false;
        }
    } catch (Exception e) {
        sistemaDeLogger.error("Erro inesperado ao alterar estado do usuário: " + e.getMessage());
        e.printStackTrace();
        alertaService.alertarErro("Erro ao alterar estado do usuário.");
        return false;
    }
}
```

Esses serviços são classes que seguem o padrão singleton (classe única) para manipular os dados dos objetos do tipo <code>UsuarioModel</code>.


### ☕ Classe [EventoCriacaoService.java](https://github.com/gabriellatcc/Eventually/blob/main/src/main/java/com/eventually/service/EventoCriacaoService.java): CREATE do sistema.

📍 **Criação de evento**
```java
 private void criarEvento(CriarEventoDto dto, String link, String localizacao, Image foto) {
    UsuarioModel organizador = usuarioSessaoService.procurarUsuario(dto.emailOrganizador());
    if (organizador == null) {
        sistemaDeLogger.error("CRÍTICO: Não foi possível criar o evento porque o organizador com email '{}' não foi encontrado.", dto.emailOrganizador());
        return;
    }

    FormatoSelecionado formato = converterFormato(dto.preferenciaFormato());
    Set<Comunidade> comunidades = converterComunidades(dto.preferenciasEvento());

    EventoModel novoEvento = new EventoModel(
            organizador,
            dto.tituloEvento(),
            dto.descricaoEvento(),
            formato,
            link,
            localizacao,
            foto,
            dto.nParticipantes(),
            dto.diaInicial(),
            dto.horaInicial(),
            dto.diaFinal(),
            dto.horaFinal(),
            comunidades,
            new ArrayList<>(),
            true,
            false,
            new ArrayList<>()
    );
    novoEvento.setId(proximoId++);

    boolean adicionado = this.listaEventos.add(novoEvento);

    if (adicionado) {
        sistemaDeLogger.info("Evento '{}' criado com ID {} e adicionado à lista geral.", novoEvento.getNome(), novoEvento.getId());

        organizador.getEventosOrganizados().add(novoEvento);
        sistemaDeLogger.info("CONEXÃO FEITA: Evento ID {} associado ao organizador '{}'.", novoEvento.getId(), organizador.getEmail());
    } else {
        sistemaDeLogger.warn("Evento '{}' não foi adicionado (possivelmente um duplicado).", novoEvento.getNome());
    }
}
```

### ☕ Classe [EventoEdicaoService.java](https://github.com/gabriellatcc/Eventually/blob/main/src/main/java/com/eventually/service/EventoEdicaoService.java): UPDATE do sistema.

📍 **Edição de evento**
```java
 public void atualizarEvento(int idDoEvento, EditaEventoModal modal) {
    Optional<EventoModel> optionalEvento = eventoLeituraService.procurarEventoPorId(idDoEvento);

    if (optionalEvento.isEmpty()) {
        logger.info("ERRO CRÍTICO: Tentativa de editar um evento que não existe. ID: " + idDoEvento);
        return;
    }

    EventoModel eventoParaAtualizar = optionalEvento.get();
    sistemaDeLogger.info("Iniciando atualização para o evento real: " + eventoParaAtualizar.getNome());

    String novoNome = modal.getFldNomeEvento().getText();
    if (novoNome != null && !novoNome.isBlank()) {
        eventoParaAtualizar.setNome(novoNome);
    }

    String novaDescricao = modal.getTaDescricao().getText();
    if (novaDescricao != null && !novaDescricao.isBlank()) {
        eventoParaAtualizar.setDescricao(novaDescricao);
    }

    String novoLink = modal.getFldLink().getText();
    if (novoLink != null && !novoLink.isBlank()) {
        eventoParaAtualizar.setLinkAcesso(novoLink);
    }

    String novaLocalizacao = modal.getTaLocalizacao().getText();
    if (novaLocalizacao != null && !novaLocalizacao.isBlank()) {
        eventoParaAtualizar.setLocalizacao(novaLocalizacao);
    }

    String novaCapacidadeStr = modal.getFldNParticipantes().getText();
    if (novaCapacidadeStr != null && !novaCapacidadeStr.isBlank()) {
        try {
            int novaCapacidade = Integer.parseInt(novaCapacidadeStr);
            eventoParaAtualizar.setnParticipantes(novaCapacidade);
        } catch (NumberFormatException e) {
            sistemaDeLogger.info("Valor de capacidade inválido: " + novaCapacidadeStr);
        }
    }

    String novoFormato = modal.getFormato();
    if (!novoFormato.isEmpty()) {
        eventoParaAtualizar.setFormato(FormatoSelecionado.valueOf(novoFormato));
    }

    sistemaDeLogger.info("Evento atualizado para: " + eventoParaAtualizar.getNome());
}
```
### ☕ Classe [EventoLeituraService.java](https://github.com/gabriellatcc/Eventually/blob/main/src/main/java/com/eventually/service/EventoLeiruraServcice.java): READ do sistema.

📍 **Leitura de evento**
```java
public Optional<EventoModel> procurarEventoPorId(int id) {
    try {
        return eventoCriacaoService.buscarEventoPorId(id);
    } catch (Exception e) {
        sistemaDeLogger.error("Erro ao buscar evento por ID: " + id + " - " + e.getMessage());
        e.printStackTrace();
        return Optional.empty();
    }
}
```

### ☕ Classe [EventoExclusaoService.java](https://github.com/gabriellatcc/Eventually/blob/main/src/main/java/com/eventually/service/EventoExclusaoService.java): DELETE do sistema.

📍 **Exclusão de evento**
```java
public boolean alterarEstadoDoEvento(int idEvento, boolean novoEstado) {
    sistemaDeLogger.info("Método alterarEstadoDoEvento() chamado.");
    try {
        Optional<EventoModel> eventoModel = eventoCriacaoService.buscarEventoPorId(idEvento);

        if (eventoModel.isPresent()) {
            EventoModel evento = eventoModel.get();
            evento.setEstado(novoEstado);
            sistemaDeLogger.info("Estado do evento com ID " + idEvento + " alterado para " + (novoEstado ? "ATIVO" : "INATIVO") + ".");
            return true;
        } else {
            sistemaDeLogger.info("EventoH com ID " + idEvento + " não encontrado para alterar estado.");
            return false;
        }
    } catch (Exception e) {
        sistemaDeLogger.error("Erro inesperado ao alterar estado do evento: " + e.getMessage());
        e.printStackTrace();
        alertaService.alertarErro("Erro ao alterar estado do evento.");
        return false;
    }
}
```
Esses serviços são classes que seguem o padrão singleton (classe única) para manipular os dados dos objetos do tipo <code>EventoModel</code>.

## 👥 Equipe

- **Gabriella Tavares Costa Corrêa** - [@gabriellatcc](https://github.com/gabriellatcc) (Estruturação, desenvolvimento backend, integração com frontend, documentação de código e revisão de boas práticas)
- **Lívia Elisei Neves Machado** - [@liviaelisei](https://github.com/liviaelisei) (Consolidação de ideias e documentação)
- **Lucas Lopes de Oliveira** - [@LucasLopes2003](https://github.com/LucasLopes2003) (Consolidação de ideias)
- **Maria Auxilidora da Cunha Pereira** - [@MariaPereiraGit](https://github.com/MariaPereiraGit) (Padronização e criação de design, desenvolvimento de protótipos de telas, revisão da construção de telas)
- **Yuri Garcia Maia** - [@yurigmaia](https://github.com/yurigmaia) (Desenvolvimento frontend de código, padronização e criação de design, desenvolvimento de protótipos de telas, revisão da construção de telas)
