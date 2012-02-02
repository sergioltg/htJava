package br.com.htecon.util;

import java.io.File;
import java.util.Properties;

import javax.activation.DataHandler;
import javax.activation.FileDataSource;
import javax.mail.Message;
import javax.mail.Multipart;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;

public class EnviaEmail {
	
	private String servidor;
	private String user;
	private String password;	
	
	public EnviaEmail(String servidor, String user, String password) {
		this.servidor = servidor;
		this.user = user;		
		this.password = password;
	}
	
	public void enviarEmail(String from, String subject, String mensagem, File fileAttached, String[] tos) throws Exception {
		enviarEmail(from, subject, mensagem, fileAttached, tos, null);
	}

	public void enviarEmail(String from, String subject, String mensagem, File fileAttached, String[] tos, String[] ccs) throws Exception {
		
		Properties props = new Properties();
		props.setProperty("mail.transport.protocol", "smtp");
		props.setProperty("mail.smtp.host", servidor);
		props.setProperty("mail.smtp.auth", "true");
		
		Session mailSession = Session.getDefaultInstance(props, null);
		Transport transport = mailSession.getTransport();

		// create and fill the first message part
		MimeBodyPart mbp1 = new MimeBodyPart();
		mbp1.setContent(mensagem, "text/html;charset=iso-8859-1");
		
		Multipart mp = new MimeMultipart();
		mp.addBodyPart(mbp1);
		
		if (fileAttached != null) {		
			// create the second message part
			MimeBodyPart mbp2 = new MimeBodyPart();
	
			FileDataSource fds = new FileDataSource(fileAttached);
			mbp2.setDataHandler(new DataHandler(fds));
			mbp2.setFileName(fds.getName());

			// create the Multipart and add its parts to it
			mp.addBodyPart(mbp2);
		}

		MimeMessage message = new MimeMessage(mailSession);
		message.setSubject(subject);
		message.setContent(mp);
		message.setFrom(new InternetAddress(from));
		for (String to:tos) {
			message.addRecipient(Message.RecipientType.TO, new InternetAddress(
				to));
		}
		if (ccs != null) {
			for (String to:ccs) {
				message.addRecipient(Message.RecipientType.CC, new InternetAddress(
					to));
			}	
		}
		transport.connect(servidor, user, password);
		transport.sendMessage(message, message
				.getRecipients(Message.RecipientType.TO));
		transport.close();		
	}
	
	
	private class SMTPAuthenticator extends javax.mail.Authenticator {
		
		private String user;
		private String password;
		
		public SMTPAuthenticator(String user, String password) {
			this.user = user;
			this.password = password;		
		}		
		
        public PasswordAuthentication getPasswordAuthentication() {
           return new PasswordAuthentication(user, password);
        }
    }
	

}
