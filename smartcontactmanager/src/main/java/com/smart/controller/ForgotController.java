package com.smart.controller;

import java.util.Random;

import javax.mail.MessagingException;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.smart.dao.UserRepository;
import com.smart.entites.User;
import com.smart.helper.Message;
import com.smart.service.EmailService;

@Controller
public class ForgotController {
 
	Random random = new Random(000000);
	
	@Autowired
	private EmailService emailService;
	
	@Autowired
	private UserRepository userRepository;
	
	@Autowired
	private BCryptPasswordEncoder bCryptPasswordEncoder;
	
	@GetMapping("/forgot")
	public String openEmailForm()
	{
		return "forgot_email_form";
	}
	
	 
	@PostMapping("/send-otp")
	public String sendOTP(@RequestParam("email") String email, HttpSession s) 
	{
		System.out.println("email : "+ email);
		
		//genrating otp of 4 digit
		
		int otp = random.nextInt(999999);
		System.out.println("Otp "+otp);
		
		//write code for send otp to email...
		
		String subject="OTP From SCM";
		
		String message = "<div style='border:1px solid #e2e2e2; padding 20px;'>" 
									+ "<h1>"
									+"OTP is"
									+"<b> "+ otp
									+"</n>"
									+"</h1>"
									+"</div>";
		
		String 	to = email;
		
		boolean flag = this.emailService.sendEmail(subject, message, to);
		
		if(flag)
		{
			
			s.setAttribute("myotp", otp);
			s.setAttribute("email", to);
			
			return "verify_otp";
			
		}else {
			
			s.setAttribute("message", "Check Your emial id !!" );
			
			return "forgot_email_form";
			
		}

		
	}
	
	//verify otp
	@PostMapping("/verify-otp")
	public String verifyOtp(@RequestParam("otp") int otp,HttpSession s)
	{
		int myOtp = (int) s.getAttribute("myotp");
		String email	= (String) s.getAttribute("email");
		
		if(myOtp==otp)
		{
			
			// Password Change Form
			User user = this.userRepository.getUserByUserName(email);
			
			if (user==null) {
				
				//Error message
				s.setAttribute("message", "user does not exits with this email !!");
				System.out.println("Not ...........sucess......................");
				return "forgot_email_form";
			}else {
				
				//send change password form
				s.setAttribute("email", email);
				return "password_change_form";
			}
			
		}else
		{
			s.setAttribute("message", "you have enterd wrong otp");
			return "verify_otp";
		}
		
	}
	
	//Change password
	@PostMapping("/change-password")
	public String changePassword(@RequestParam("newpassword") String password,HttpSession s)
	{
		String email = (String) s.getAttribute("email");
		User user = this.userRepository.getUserByUserName(email);
		user.setPassword(this.bCryptPasswordEncoder.encode(password));
		this.userRepository.save(user);
		return "redirect:/login?change=password changed succesfully...";
	}
	
}
