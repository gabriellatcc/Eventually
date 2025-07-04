package com.eventually.service;

import com.eventually.model.UsuarioModel;
import com.sun.mail.util.MailSSLSocketFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.InputStream;
import java.util.Optional;
import java.util.Properties;
import java.util.Random;
import javax.activation.DataHandler;
import javax.mail.Message;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import javax.mail.util.ByteArrayDataSource;
import javax.activation.DataSource;

/**
 * Serviço responsável por gerar código para substituir uma senha esquecida e enviar e-mails aos usuários,
 * O e-mail inclui uma imagem inline (logo do sistema) e uma mensagem em HTML formatada.
 * @author Gabriella Tavares Costa Corrêa (Criação, revisão de documentação e da estrutura da classe)
 * @version 1.05
 * @since 23-05-2025
 */
public class EmailService {
    private String codigoGerado;
    private String emailRecebido;
    private UsuarioCadastroService usuarioCadastroService; //referencia

    private static final Logger sistemaDeLogger = LoggerFactory.getLogger(EmailService.class);
    private AlertaService alertaService = new AlertaService();

    public EmailService(String email) {
        this.usuarioCadastroService = UsuarioCadastroService.getInstancia();
        this.emailRecebido = email;
    }

    private void validarEmail() {
        sistemaDeLogger.info("Método validarEmail() chamado para o e-mail: " + emailRecebido);
        try {
            Optional<UsuarioModel> usuarioOptional = usuarioCadastroService.getAllUsuarios()
                    .stream()
                    .filter(usuario -> usuario.getEmail().equalsIgnoreCase(emailRecebido))
                    .findFirst();

            if (usuarioOptional.isPresent()) {
                this.codigoGerado = gerarCodigo();

                if (this.codigoGerado != null) {
                    UsuarioModel usuarioParaAtualizar = usuarioOptional.get();
                    usuarioParaAtualizar.setSenha(this.codigoGerado);

                    enviarEmail();
                    alertaService.alertarInfo("Um código foi enviado para o seu e-mail. Ele é a sua nova senha temporária.\nUse-o para fazer login e altere-a nas configurações.");
                } else {
                    alertaService.alertarErro("Falha ao gerar o código de recuperação. Tente novamente.");
                }
            } else {
                alertaService.alertarErro("O e-mail não está cadastrado no sistema");
            }
        } catch (Exception e) {
            sistemaDeLogger.error("Erro ao validar o email no sistema: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private String gerarCodigo() {
        try {
            Random rand = new Random();
            int randomCode = rand.nextInt(900000) + 100000;
            return String.valueOf(randomCode);
        } catch (Exception e) {
            sistemaDeLogger.error("Erro ao gerar codigo: " + e.getMessage());
            e.printStackTrace();
            return null;
        }
    }

    private void enviarEmail() {
        try {
            String userHome = System.getProperty("user.home");

            System.setProperty("javax.net.ssl.keyStore", userHome + "\\meuKeystore.jks");
            System.setProperty("javax.net.ssl.trustStore", userHome + "\\meuKeystore.jks");
            System.setProperty("javax.net.ssl.keyStorePassword", "minhaSenha");
            System.setProperty("javax.net.ssl.trustStorePassword", "minhaSenha");

            final String from = "eventuallyaplication@gmail.com";
            final String password = "sguozeqqpluitmun";
            final String emailUsuario = emailRecebido;

            MailSSLSocketFactory sf = new MailSSLSocketFactory();
            sf.setTrustAllHosts(true);

            Properties props = new Properties();
            props.put("mail.smtp.auth", "true");
            props.put("mail.smtp.starttls.enable", "true");
            props.put("mail.smtp.ssl.enable", "true");
            props.put("mail.smtp.ssl.socketFactory", sf);
            props.put("mail.smtp.host", "smtp.gmail.com");
            props.put("mail.smtp.port", "465");

            Session session = Session.getInstance(props, new javax.mail.Authenticator() {
                protected PasswordAuthentication getPasswordAuthentication() {
                    return new PasswordAuthentication(from, password);
                }
            });

            session.setDebug(true);

            MimeMessage message = new MimeMessage(session);
            message.setFrom(new InternetAddress(from));
            message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(emailUsuario));
            message.setSubject("Eventually: Sua nova senha temporária");

            InputStream imageStream = getClass().getResourceAsStream("/images/logoEmail.jpg");

            if (imageStream != null) {
                sistemaDeLogger.info("Imagem do logo encontrada. Montando e-mail com imagem.");
                MimeMultipart multipart = new MimeMultipart("related");

                String htmlComImagem = """
                <html>
                <body style="font-family: Arial, sans-serif; text-align: center; padding: 20px; font-size: 17px; line-height: 1.6; color: #333;">
                    <img src="cid:logoImage" style="width: 150px; margin-bottom: 20px;"/>
                    <h2 style="color: #9a009a; font-size: 28px;">Recuperação de Senha</h2>
                    <p>Esta é sua nova senha temporária:</p>
                    <p style="font-size: 32px; font-weight: bold; color: #000; margin: 20px 0; letter-spacing: 2px;">%s</p>
                    <p>Entre no sistema e altere sua senha nas configurações.</p>
                    <p style="margin-top: 30px; font-size: 14px; color: #666;">— Equipe Eventually Application 🌟</p>
                </body>
                </html>
                """.formatted(this.codigoGerado);
                MimeBodyPart htmlPart = new MimeBodyPart();
                htmlPart.setContent(htmlComImagem, "text/html; charset=utf-8");
                multipart.addBodyPart(htmlPart);

                MimeBodyPart imagePart = new MimeBodyPart();
                DataSource fds = new ByteArrayDataSource(imageStream, "image/jpeg");
                imagePart.setDataHandler(new DataHandler(fds));
                imagePart.setHeader("Content-ID", "<logoImage>");
                imagePart.setDisposition(MimeBodyPart.INLINE);
                multipart.addBodyPart(imagePart);

                message.setContent(multipart);
            } else {
                sistemaDeLogger.error("RECURSO NÃO ENCONTRADO: Não foi possível encontrar 'logoEmail.jpg'. Enviando e-mail sem a imagem.");
                String htmlSemImagem = """
                <html>
                <body style="font-family: Arial, sans-serif; text-align: center; padding: 20px; font-size: 17px; line-height: 1.6; color: #333;">
                    <h2 style="color: #9a009a; font-size: 28px;">Recuperação de Senha</h2>
                    <p>Esta é sua nova senha temporária:</p>
                    <p style="font-size: 32px; font-weight: bold; color: #000; margin: 20px 0; letter-spacing: 2px;">%s</p>
                    <p>Entre no sistema e altere sua senha nas configurações.</p>
                    <p style="margin-top: 30px; font-size: 14px; color: #666;">— Equipe Eventually Application 🌟</p>
                </body>
                </html>
                """.formatted(this.codigoGerado);
                message.setContent(htmlSemImagem, "text/html; charset=utf-8");
            }

            Transport.send(message);
            sistemaDeLogger.info("E-mail de recuperação enviado com sucesso para " + emailUsuario);
        } catch (Exception e) {
            sistemaDeLogger.error("Erro CRÍTICO ao enviar e-mail: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public void enviar() {
        try {
            validarEmail();
        } catch (Exception e) {
            sistemaDeLogger.error("Erro ao iniciar o processo de envio: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
