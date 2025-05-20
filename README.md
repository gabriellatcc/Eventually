# 📆 Eventually - Sistema de Participação  de de Eventos
Esta aplicação foi desenvolvida como parte do projeto da disciplina de **Estrutura de Dados** da **FATEC - Prof. Waldomiro May**.

O **Eventually** é é uma plataforma desenvolvida para organizar e facilitar a participação de usuários em eventos, oferecendo uma interface clara para visualização, cadastro e gerenciamento de informações. A aplicação permite o registro de usuários e eventos, a inscrição de participantes, a definição de categorias e o controle detalhado por meio de operações CRUD bem estruturadas

A modelagem dos dados foi feita utilizando as bibliotecas `ArrayList` e `Set<>`, sem o uso de banco de dados, revisando padrões de construção e práticas da disciplina de estrutura de dados.

## ⚙️ Tecnologias Utilizadas

- **Java** (linguagem principal da aplicação)
- **JavaFx** (framework para interface gráfica)
- css para o java fx personalizado
- **Maven** (ferramenta de build)
- **Intellij** (IDE)

## 📌 Funcionalidades básicas

- **Cadastro e edição de usuários.**  
- **Inscricão e cancelamento de participacao de usuários em eventos.**  
- **Criação, edição, exclusão e vizualizacao de eventos.**  
- **Controle de programação de eventos.**  
- **Filtros e ordenação de dados de eventos326.**  
- **Validações para evitar duplicidade de usuários e eventos.**

## 📃 Armazenamento de dados

Neste projeto, a persistência de dados é realizada **em memória**, utilizando as bibliotecas `ArrayList` e `Set` da linguagem Java, sem uso de banco de dados. Abaixo estão alguns trechos de código exemplificando as principais operações de armazenamento, após os dados inseridos nas interfaces serem recebidos pelos controladores e validados pelo serviços:

### 📁 Classe `EventoRepository`

📍 **Criação de evento**
```java
public void adicionarEvento(EventoModel evento) {
    int id = System.identityHashCode(evento);
    evento.setId(id);
    listaEventos.add(evento);
}
```

📍 **Exclusão de evento**
```java
public boolean removerEvento(EventoModel evento) {
    return listaEventos.remove(evento);
}
```

📍 **Busca de evento por ID**
```java
public Optional<EventoModel> buscarEventoPorId(int id) {
    return listaEventos.stream()
            .filter(evento -> evento.getId() == id)
            .findFirst();
}
```

📍 **Listagem de todos os eventos**
```java
public List<EventoModel> getAllEventos() {
    return listaEventos;
}
```

### 📁 Classe `UsuarioRepository`

📍 **Criação de usuário**
```java
public void adicionarUsuario(UsuarioModel usuario) {
    int id = System.identityHashCode(usuario);
    usuario.setId(id);
    listaUsuarios.add(usuario);
}
```

📍 **Remoção de usuário**
```java
public boolean removerUsuario(UsuarioModel usuario) {
    return listaUsuarios.remove(usuario);
}
```

📍 **Busca de usuário por ID**
```java
public Optional<UsuarioModel> buscarUsuarioPorId(int id) {
    return listaUsuarios.stream()
            .filter(usuario -> usuario.getId() == id)
            .findFirst();
}
```

📍 **Listagem de todos os usuários**
```java
public Set<UsuarioModel> getAllUsuarios() {
    return listaUsuarios;
}
```

Essas classes (`EventoRepository` e `UsuarioRepository`) são responsáveis por toda a lógica de armazenamento e gerenciamento de dados em memória, utilizando estruturas de dados da linguagem Java de forma eficiente.

## 👥 Equipe

- **Gabriella Tavares Costa Corrêa** - [@gabriellatcc](https://github.com/gabriellatcc) (Estruturação, desenvolvimento backend, integração com frontend, documentação de código e revisão de boas práticas)
- **Yuri Garcia Mai** - [@yurigmaia](https://github.com/yurigmaia) (Desenvolvimento frontend de código, padronização e criação de design, desenvolvimento de protótipos de telas, revisão da construção de telas)
- **Maria Auxilidora da Cunha Pereira** - [@maripereira55](https://github.com/maripereira55) (Padronização e criação de design, desenvolvimento de protótipos de telas, revisão da construção de telas)
- **Lívia Elisei Neves Machado** - [@liviaelisei](https://github.com/liviaelisei) (Consolidação de ideias e documentação)
- **Lucas Lopes de Oliveira** - [@LucasLopes2003](https://github.com/LucasLopes2003) (Consolidação de ideias)
