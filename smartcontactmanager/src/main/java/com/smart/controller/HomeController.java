package com.smart.controller;

import java.util.Random;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.smart.dao.UserRepository;
import com.smart.entites.User;
import com.smart.helper.Message;
import com.smart.service.EmailService;

@Controller
public class HomeController {
	
	Random random = new Random(000000);
	
	@Autowired
	private EmailService emailService;
	
	@Autowired
	private BCryptPasswordEncoder passwordEncoder;
	
	@Autowired
	private UserRepository repo;
	
	@RequestMapping("/")
	public String home(Model m) {
		m.addAttribute("title", "Home - Smart Contact Manager");
		return "home";
	}

	@RequestMapping("/about")
	public String about(Model m) {
		m.addAttribute("title", "About - Smart Contact Manger");
		return "about";
	}

	//Open User Verification Page
	@GetMapping("/verify")
	public String openVerifyEmail(Model m)
	{
		m.addAttribute("title","Verify User");
		return "verify_user";
	}
	
	//Send Email
	@PostMapping("/send-email")
	public String sendEmail(@RequestParam("email") String email,HttpSession s,Model m)
	{
		m.addAttribute("title","Register Email");
		
		System.out.println("Email  "+ email);
		
		int otp = random.nextInt(999999);
		
		String subject ="OTP Form SCM for Register User";
		
		String message = "<div style='border:1px solid #e2e2e2; padding 20px;'>" 
									+ "<h1>"
									+"OTP is"
									+"<b> "+ otp
									+"</n>"
									+"</h1>"
									+"</div>";
		
		String to = email;
		
		boolean b = this.emailService.sendEmail(subject, message, to);
		
		if(b)
		{
			s.setAttribute("myotp", otp);
			s.setAttribute("email", to);
			
			return "register_user_email_form";
		}else {
			
			s.setAttribute("message", "Please Checak your emial id ");
			return "verify_user";
			
		}
		
		
	}
	
	//Verify Email
	@PostMapping("/verify-email")
	public String verifyEmail(@RequestParam("otp") int otp,HttpSession s,Model m)
	{
		int myOtp = (int)s.getAttribute("myotp");
		String email = (String) s.getAttribute("email");
		
		if(myOtp==otp)
		{
			m.addAttribute("message", new Message("Your Email is succsefully Verified Complete Other Detail..", 
					"success"));
			s.setAttribute("email", email);
			return "redirect:/signup";
			
		}else
		{
			s.setAttribute("message", "Please, Enter Right Otp !! ");
			return "register_user_email_form";
		}
		
	}
	
	@RequestMapping("/signup")
	public String signup(Model m,HttpSession s)
	{
		String email = (String) s.getAttribute("email");
		m.addAttribute("title","Register - Smart Contact Manger");
		m.addAttribute("user",new User());
		m.addAttribute("verifyEmail",email);
		return "signup";
	}
	
	// This handler for Registration
	
	@PostMapping("/do_register")
	public String registerUser(@Valid @ModelAttribute("user") User user, BindingResult r,
			@RequestParam(value = "agreement",defaultValue = "false") boolean agreement,
			Model m, HttpSession s)
	{
		try {
			
			if(!agreement)
			{
				System.out.println("First check the terms and conditions");
				throw new Exception("First check the terms and conditions");
			}
			
			if(r.hasErrors())
			{
				System.out.println("ERROR" + r.toString());
				m.addAttribute("user",user);
				return "signup";	
			}
			
			user.setRole("ROLE_USER");
			user.setEnabled(true);
			user.setImageUrl("default.jpg");
			String encode = passwordEncoder.encode(user.getPassword());
			user.setPassword(passwordEncoder.encode(user.getPassword()));
			
			System.out.println("Agrrement " + agreement);
			System.out.println(user);
			
			User result = this.repo.save(user);
			
			m.addAttribute("user", new User());
			
			s.setAttribute("mesg", new Message("Succesfully Registerd !!", "alert-success"));
			return "signup";
			
		} catch (Exception e) {
			e.printStackTrace();
			m.addAttribute("user",user);
			s.setAttribute("mesg", new Message("Something Went Wrong !! " + e.getMessage(), "alert-danger"));
			return "signup";
		}
		
		
	}
	
	// Handler for Custom Login
	@GetMapping("/login")
	public String customLogin(Model m)
	{
		m.addAttribute("title","Login Page");
		return "login";
	}
	
	@GetMapping("/login-fail")
	public String errorPage(Model m)
	{
		m.addAttribute("title","Error-Page");
		return "login-fail";
	}
	
}
