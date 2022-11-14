package com.smart.service;

import java.util.Properties;

import javax.mail.BodyPart;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;

import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.JsonNode;



@Service
public class EmailService {
	
//	public boolean sendEmails(String subject,String messagee, String to) throws MessagingException {
//		Properties props = System.getProperties();
//		props.put("mail.smtps.auth", "true");
//		props.put("mail.smtp.starttls.enable", "true");
//		props.put("mail.smtp.host", "smtp.gmail.com");
//		props.put("mail.smtp.port", "25"); // TLS Port
//	
//		
//		Session session = Session.getInstance(props, null);
//		MimeMessage message = new MimeMessage(session);
//		message.setFrom("mihirthumaraaa@gmail.com");
//		message.setRecipients(Message.RecipientType.TO, to);
//		message.setSubject("Anrita Test Mail");
//		message.setText("this mail is use for test mail");
//		BodyPart bodyPart = new MimeBodyPart();
//		bodyPart.setText("Hello,\n" + "This is a test email from Anrita system to test the SMTP connectivity.");
//		Multipart multipart = new MimeMultipart();
//		multipart.addBodyPart(bodyPart);
//		message.setContent(multipart);
//		
//		Transport tr = session.getTransport("smtp");
//		tr.connect("smtp.gmail.com", "mihirthumaraaa@gmail.com", "fbkyexluzmcpcijn");//
//
//		tr.sendMessage(message, message.getAllRecipients());
//		tr.close();
//		
//		return true;
//	}

	public boolean sendEmail(String subject,String message, String to)
	{
		boolean f = false;
		
		String from = "mihirthumaraaa@gmail.com";
	
		//variable  for gamil
		String host = "smtp.gmail.com";
		
		//get the system properties
		Properties properties = System.getProperties();
		System.out.println("PROPERTIES : "+ properties);
		
		//setting important information to properties object
		
		//host set
		properties.put("mail.smtp.host	", host);
		properties.put("mail.smtp.port", "465");
		properties.put("mail.smtp.ssl.enable", "true");
		properties.put("mail.smtp.starttls.enable", "true");
		properties.put("mail.smtp.auth", "true");
//		properties.put("mail.test-connection", "fasle");
		
		//Step 1 : to get the session object..
		Session session = Session.getInstance(properties,null);
		
		
		
		session.setDebug(true);
		
		//Step 2 : compose the messeage [text,multi media]
		MimeMessage m = new MimeMessage(session);
		
		try {
			
			//from email
			m.setFrom(from);
			
			//adding recipient to message
			m.addRecipients(Message.RecipientType.TO, to);
			
			//adding subject to message
			m.setSubject(subject);
			
			//adding text to message
			m.setContent(message, "text/html");
			
			//send
		
			//Step 3 : send the message using transport class
			
			Transport tr = session.getTransport();
			tr.connect(host,from,"oouexugorxzeimqy");
			tr.sendMessage(m,m.getAllRecipients());
			tr.close();
			
			System.out.println("Sent success.....");
			f = true;
			
			
		} catch (Exception e) {
			
			e.printStackTrace();
			
		}
		
		return f;
		
	}
	
}
