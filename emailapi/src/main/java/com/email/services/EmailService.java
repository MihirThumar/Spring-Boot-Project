package com.email.services;

import java.util.Properties;

import javax.mail.Authenticator;
import javax.mail.Message;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import org.springframework.stereotype.Service;


@Service
public class EmailService {

	public boolean sendEmail(String subject,String to,String message)
	{
boolean f = false;
		
		String form = "mihirthumaraaa@gmail.com";//axaybhimani@gmail.com
		
		//Variable for gmail
		String host = "smtp.gmail.com";
		
		//get the system properties
		Properties properties = System.getProperties();
		System.out.println("PROPERTIES" + properties);
		
		// Seeting important information to properties to object
		
		// host set
		properties.put("mail.smtp.host", host);
		properties.put("mail.smtp.port", "465");
		properties.put("mail.smtp.ssl.enable", "true");
		properties.put("mail.smtp.auth ", "true");
		
		//Step  1: to get the session object..
		
		Session session =  Session.getInstance(properties, new Authenticator() {

			@Override
			protected PasswordAuthentication getPasswordAuthentication() {
				
				
				return new PasswordAuthentication("mihirthumaraaa@gamil.com", "Mightguy@8");
			}
			
		});
		
		session.setDebug(true);
		
		//Step 2 : 	compose the message [text,multi media]
		MimeMessage m = new MimeMessage(session);
		
		try {
			
		//from email
		m.setFrom(form);
		
		//adding recipient to message
		m.addRecipient(Message.RecipientType.TO, new InternetAddress(to));
		
		//adding subject to message
		m.setSubject(subject);
		
		//adding text to message
		m.setText(message);
		
		//Send 
		
		//Step 3 : send the message using Transport Class
		Transport.send(m);
		
		System.out.println("sent succesfully .....");
		
		f = true;
		
		}catch (Exception e) {
			e.printStackTrace();
			
		}
		return f;
	}

}