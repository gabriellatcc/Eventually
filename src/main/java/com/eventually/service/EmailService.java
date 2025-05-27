package com.eventually.service;

import com.sun.mail.util.MailSSLSocketFactory;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
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
 * Serviço responsável por gerar códigos de verificação e enviar e-mails aos usuários,
 * contendo uma senha temporária para recuperação de acesso ao sistema Eventually.
 * O e-mail inclui uma imagem inline (logo do sistema) e uma mensagem em HTML formatada.
 *
 * @author Gabriella Tavares Costa Corrêa (Criação, revisão de documentação e da estrutura da classe)
 * @version 1.0
 * @since 23-05-2025
 */
public class EmailService {
    private String codigoGerado;

    /**
     * Construtor da classe que inicializa o código temporário gerado.
     */
    public EmailService() {
        codigoGerado = gerarCodigo();
    }

//metodo validar email, se ele existe, chamar o gerar codigo

    /**
     * Gera um código numérico aleatório de 6 dígitos, formatado com zeros à esquerda.
     * @return Código de verificação formatado como string de 6 dígitos.
     */
    public static String gerarCodigo() {
        Random rand = new Random();
        int randomCode = rand.nextInt(999999);
        return String.format("%06d", randomCode);
        //chamar o set senha usuario.email.x.do repository = randomcode;
    }

    /**
     * Envia um e-mail com uma nova senha temporária para o endereço de e-mail fornecido.
     * O conteúdo do e-mail é em HTML com um logo da aplicação embutido.
     * @param email Endereço de e-mail do destinatário.
     */
    public void enviarEmail(String email) {
        try {
            String userHome = System.getProperty("user.home");

            System.setProperty("javax.net.ssl.keyStore", userHome + "\\meuKeystore.jks");
            System.setProperty("javax.net.ssl.trustStore", userHome + "\\meuKeystore.jks");

            System.setProperty("javax.net.ssl.keyStorePassword", "minhaSenha");
            System.setProperty("javax.net.ssl.trustStorePassword", "minhaSenha");

            final String from = "eventuallyaplication@gmail.com";
            final String password = "sguozeqqpluitmun";
            final String emailUsuario = email;

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
            JOptionPane.showMessageDialog(null, "Erro ao enviar e-mail: " + e.getMessage());
            e.printStackTrace();
        }
    }
}