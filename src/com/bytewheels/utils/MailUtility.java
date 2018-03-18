package com.bytewheels.utils;

import java.util.Properties;

import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.activation.FileDataSource;
import javax.mail.Authenticator;
import javax.mail.BodyPart;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;

public class MailUtility {

	private static Session createSession(String username, String password) {
		Session session = null;
		String properties[][] = { { "mail.smtp.starttls.enable", "true" }, { "mail.smtp.host", "smtp.gmail.com" },
				{ "mail.smtp.port", "587" }, { "mail.smtp.auth", "true" }, { "mail.transport.protocol", "smtp" } };

		Properties props = new Properties();
		for (String[] strArr : properties) {
			props.put(strArr[0], strArr[1]);
		}

		if (null == username || null == password) {
			return null;
		}

		session = Session.getInstance(props, new Authenticator() {
			protected PasswordAuthentication getPasswordAuthentication() {
				return new PasswordAuthentication(username, password);
			}
		});

		session.setDebug(false);
		return session;
	}

	public static void sendMail(final String from, final String to, final String cc, final String subject,
			final String content, final String strFileName, final String attachmentPath, final String mimeType,
			final String strPassword) {

		if (null == to || null == from || null == mimeType) {
			return;
		}
		Thread thread = new Thread() {
			public void run() {
				try {
					Session session = createSession(from, strPassword);
					Message message = new MimeMessage(session);

					message.setFrom(new InternetAddress(from));
					message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(to));
					if (null != cc && !cc.trim().isEmpty()) {
						message.setRecipients(Message.RecipientType.CC, InternetAddress.parse(cc));
					}

					if (null != content) {
						message.setContent(content, mimeType);
					}

					if (null != subject) {
						message.setSubject(subject);
					}

					if (null != attachmentPath && !attachmentPath.trim().isEmpty()) {
						Multipart multipart = new MimeMultipart();
						BodyPart textPart = new MimeBodyPart();
						BodyPart attachmentPart = new MimeBodyPart();
						DataSource source = new FileDataSource(attachmentPath);

						textPart.setContent(null != content ? content : "", mimeType);
						multipart.addBodyPart(textPart);
						attachmentPart.setDataHandler(new DataHandler(source));
						attachmentPart.setFileName(strFileName);
						multipart.addBodyPart(attachmentPart);
						message.setContent(multipart);
					}

					Transport.send(message);
				} catch (AddressException e) {
					e.printStackTrace();
				} catch (MessagingException e) {
					e.printStackTrace();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		};
		thread.start();
	}
}