package com.eventually.service;

import com.eventually.controller.RegisterController;
import com.eventually.model.UsuarioModel;
import com.sun.mail.util.MailSSLSocketFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.util.Optional;
import java.util.Properties;
import java.util.Random;
import javax.activation.DataHandler;
import javax.imageio.ImageIO;
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
import javax.swing.*;

/**
 * Serviço responsável por gerar código para substituir uma senha esquecida e enviar e-mails aos usuários,
 * O e-mail inclui uma imagem inline (logo do sistema) e uma mensagem em HTML formatada.
 * @author Gabriella Tavares Costa Corrêa (Criação, revisão de documentação e da estrutura da classe)
 * @version 1.01
 * @since 23-05-2025
 */
public class EmailService {
    private String codigoGerado;
    private String emailRecebido;
    private UsuarioCadastroService usuarioCadastroService; //referencia

    private static final Logger sistemaDeLogger = LoggerFactory.getLogger(EmailService.class);
    private AlertaService alertaService =new AlertaService();

    /**
     * Construtor da classe que inicializa o código temporário gerado.
     */
    public EmailService(String email) {
        this.emailRecebido = email;
    }

    /**
     * Valida o email inserido pelo usuário, se ele existe, gera um código e reseta a senha do usuário e, em caso de
     * falha, uma mensagem é exibida no console.
     */
    private void validarEmail(){
        sistemaDeLogger.info("Método validarEmail() chamado.");
       try {
           Optional<UsuarioModel> usuarioOptional = usuarioCadastroService.getAllUsuarios()
                   .stream()
                   .filter(usuario -> usuario.getEmail().equalsIgnoreCase(emailRecebido))
                   .findFirst();
           if (usuarioOptional.isPresent()) {
               String codigo = gerarCodigo();
               usuarioOptional.get().setSenha(codigo);
               enviarEmail();
               alertaService.alertarInfo("Realize a sessão com o código enviado pelo email, ele é a sua nova senha. \n Isso poderá ser alterado depois..");
           } else {
               alertaService.alertarErro("O email não está cadastrado no sistema");
           }}catch (Exception e){
           sistemaDeLogger.error("Erro ao validar o email no sistema: "+e.getMessage());
           e.printStackTrace();
       }
    }

    /**
     * Gera um código numérico aleatório de 6 dígitos, formatado com zeros à esquerda e, em caso de
     * falha, uma mensagem é exibida no console.
     * @return código de verificação formatado como string de 6 dígitos.
     */
    private String gerarCodigo() {
        try {
            Random rand = new Random();
            int randomCode = rand.nextInt(999999);
            return String.format("%06d", randomCode);
        } catch (Exception e) {
            sistemaDeLogger.error("Erro ao gerar codigo: "+e.getMessage());
            e.printStackTrace();
            return null;
        }
    }

    /**
     * Envia um e-mail com o código para senha temporária para o endereço de e-mail fornecido e, em caso de
     * falha no envio, uma mensagem é exibida no console.
     * O conteúdo do e-mail é em HTML com um logo da aplicação embutido.
     */
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

            String html = """
            <html>
            <body style="font-family: Arial, sans-serif; text-align: center;">
                <img src="cid:logoImage" style="width: 150px;"/>
                <h2 style="color: #9a009a;">Recuperação de Senha</h2>
                <p>Esta é sua nova senha temporária:</p>
                <p style="font-size: 20px; font-weight: bold;">%s</p>
                <p>Entre no sistema e altere sua senha nas configurações.</p>
                <p style="margin-top: 30px;">— Equipe Eventually Application 🌟</p>
            </body>
            </html>
        """.formatted(codigoGerado);

            MimeMultipart multipart = new MimeMultipart("related");

            MimeBodyPart htmlPart = new MimeBodyPart();
            htmlPart.setContent(html, "text/html; charset=utf-8");
            multipart.addBodyPart(htmlPart);

            MimeBodyPart imagePart = new MimeBodyPart();

            ImageIcon icon = new ImageIcon(getClass().getResource("/images/logoEmail.jpg"));
            Image image = icon.getImage();

            BufferedImage bufferedImage = new BufferedImage(
                    image.getWidth(null),
                    image.getHeight(null),
                    BufferedImage.TYPE_INT_ARGB
            );
            Graphics2D g2d = bufferedImage.createGraphics();
            g2d.drawImage(image, 0, 0, null);
            g2d.dispose();

            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            ImageIO.write(bufferedImage, "jpg", baos);
            InputStream is = new ByteArrayInputStream(baos.toByteArray());

            DataSource fds = new ByteArrayDataSource(is, "image/jpg");
            imagePart.setDataHandler(new DataHandler(fds));
            imagePart.setHeader("Content-ID", "<logoImage>");
            imagePart.setDisposition(MimeBodyPart.INLINE);
            multipart.addBodyPart(imagePart);

            message.setContent(multipart);

            Transport.send(message);
        } catch (Exception e) {
            sistemaDeLogger.error("Erro ao enviar e-mail: " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * Inicia a operação de enviar o email da situação e, em caso de falha, é exibida uma mensagem no console.
     */
    public void enviar() {
        try {
            validarEmail();
        } catch (Exception e) {
            sistemaDeLogger.error("Erro ao enviar e-mail: " + e.getMessage());
            e.printStackTrace();
        }
    }
}