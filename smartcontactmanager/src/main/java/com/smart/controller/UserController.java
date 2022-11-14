package com.smart.controller;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.security.Principal;
import java.util.Map;
import java.util.Optional;

import javax.servlet.http.HttpSession;

import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.fasterxml.jackson.databind.JsonNode;
import com.razorpay.Order;
import com.razorpay.RazorpayClient;
import com.razorpay.RazorpayException;
import com.smart.dao.ContactRepository;
import com.smart.dao.MyOrderRepository;
import com.smart.dao.UserRepository;
import com.smart.entites.Contact;
import com.smart.entites.MyOrder;
import com.smart.entites.User;
import com.smart.helper.Message;

@Controller
@RequestMapping("/user")
public class UserController {

	@Autowired
	private BCryptPasswordEncoder bCryptPasswordEncoder;
	
	@Autowired
	private UserRepository repository;

	@Autowired
	private ContactRepository contactrepo;
	
	@Autowired
	private MyOrderRepository myOrderRepository;

	// method for adding common data to response
	@ModelAttribute
	public void addCommonData(Model m, Principal princi) {
		// Get the user using userName(Email)

		String name = princi.getName();
		System.out.println("User Name -> :  " + name);

		User user = repository.getUserByUserName(name);
		System.out.println(user.getId());
		m.addAttribute("user", user);
	}

	// dash-board home
	@RequestMapping("/index")
	public String dashboard(Model m) {
		return "normal/user_dashboard";
	}

	// open add form handler
	@GetMapping("/addcontact")
	public String openAddContactForm(Model m) {
		m.addAttribute("title", "Add Contact");
		m.addAttribute("contact", new Contact());

		return "normal/add_contact_form";
	}

	// processing add contact form
	@PostMapping("/process-contact")
	public String processContact(@ModelAttribute Contact contact, Principal p,
			@RequestParam("profileImage") MultipartFile file, HttpSession s) {
		try {

			String name = p.getName();
			User user = repository.getUserByUserName(name);

			// processing and uploading file...

			if (file.isEmpty()) {
				// If file is empty then try our message
				System.out.println("File is Empty");
				contact.setImage("contact.jpg");

			} else {
				// file to folder and update the name to cotact
				contact.setImage(file.getOriginalFilename());

				File secondFile = new ClassPathResource("static/img").getFile();

				Path path = Paths.get(secondFile.getAbsolutePath() + File.separator + file.getOriginalFilename());

				Files.copy(file.getInputStream(), path, StandardCopyOption.REPLACE_EXISTING);

				System.out.println("Image is Uploade");

				// message success....
				s.setAttribute("message", new Message("Your Contact is added !! you can add more...!!", "success"));

			}

			contact.setUser(user);
			user.getContacts().add(contact);

			this.repository.save(user);

			System.out.println("DATA :-> " + contact);

			System.out.println("Added to database");

		} catch (Exception e) {
			System.out.println("Error" + e.getMessage());
			e.printStackTrace();

			// message error
			s.setAttribute("message", new Message("Some thing went wrong try again later !!", "danger"));

		}

		return "normal/add_contact_form";
	}

	// Show Contacts Handler
	// per page = 5[n]
	// current page = 0 [page]
	@GetMapping("/show-contacts/{page}")
	public String showContacts(@PathVariable("page") Integer page, Model m, Principal p) {
		m.addAttribute("title", "Show User Contacts");

		// Send Contact list

		String userName = p.getName();
		User user = this.repository.getUserByUserName(userName);

		Pageable pageable = PageRequest.of(page, 4);

		Page<Contact> contacts = this.contactrepo.findContactsByUser(user.getId(), pageable);

		m.addAttribute("contacts", contacts);
		m.addAttribute("currentPage", page);
		m.addAttribute("totalpages", contacts.getTotalPages());

		return "normal/show_contacts";
	}

	// Showing particuler contact Detail
	@GetMapping("/{cId}/contact")
	public String showContactDetail(@PathVariable("cId") Integer cId, Model m, Principal p) {
		System.out.println("CID :- > " + cId);

		Optional<Contact> contactOptional = this.contactrepo.findById(cId);
		Contact contact = contactOptional.get();

		//
		String userName = p.getName();
		User user = this.repository.getUserByUserName(userName);

		if (user.getId() == contact.getUser().getId()) {
			m.addAttribute("contact", contact);
			m.addAttribute("title", contact.getName());
		}

		return "normal/contact_detail";
	}

	// delete Contact Handler
	@RequestMapping("/delete/{cId}")
	public String deleteContact(@PathVariable("cId") Integer cId, Model m, HttpSession s, Principal p) {
		try {
			String userName = p.getName();
			User user = this.repository.getUserByUserName(userName);

			Contact contact = this.contactrepo.findById(cId).get();

			if (user.getId() == contact.getUser().getId()) {
				// delete old image
				String image = contact.getImage();
				File resource = new ClassPathResource("static/img").getFile();
				Path path = Paths.get(resource.getAbsolutePath() + File.separator + image);
				Files.delete(path);

				// contact.setUser(null);

				this.contactrepo.delete(contact);
				s.setAttribute("message", new Message("Contect delete Successfully", "success"));

			} else {

				s.setAttribute("message", new Message("You Don't Have This Permission", "danger"));
			}
		} catch (Exception e) {
			e.printStackTrace();
			s.setAttribute("message", new Message("Something Went Wrong", "danger"));
		}

		return "redirect:/user/show-contacts/0";
	}

	// open update form handler
	@PostMapping("/update-contact/{cid}")
	public String updateForm(Model m, @PathVariable("cid") Integer cid) {

		m.addAttribute("title", "Update Contact");

		Contact contact = this.contactrepo.findById(cid).get();

		m.addAttribute("contact", contact);

		return "normal/update_form";
	}

	// update contact handler
	@PostMapping("/process-update/{cid}")
	public String updateHandler(@ModelAttribute Contact contact, @PathVariable("cid") Integer cid,
			@RequestParam("profileImage") MultipartFile file, Model m, HttpSession s, Principal p) {

		try {
			// old contact detail
			Contact oldContactDetail = this.contactrepo.findById(cid).get();

			// image.....
			if (!file.isEmpty()) {
				// file work....
				// rewrite

				// delete old photo
				File deleteFile = new ClassPathResource("static/img").getFile();
				File file1 = new File(deleteFile, oldContactDetail.getImage());
				file1.delete();

				// update new photo
				File secondFile = new ClassPathResource("static/img").getFile();
				Path path = Paths.get(secondFile.getAbsolutePath() + File.separator + file.getOriginalFilename());
				Files.copy(file.getInputStream(), path, StandardCopyOption.REPLACE_EXISTING);

				contact.setImage(file.getOriginalFilename());

			} else {
				contact.setImage(oldContactDetail.getImage());
			}

			User user = this.repository.getUserByUserName(p.getName());
			contact.setUser(user);
			contact.setcId(cid);
			this.contactrepo.save(contact);

			s.setAttribute("message", new Message("Your Contact Update SuccesFully ....", "success"));

		} catch (Exception e) {
			e.printStackTrace();

			s.setAttribute("message", new Message("Your Contact Not Update SuccesFully !!", "danger"));

		}

		System.out.println("Contact :-> " + cid);
		System.out.println("Contact >> " + contact.getName());

		return "redirect:/user/" + contact.getcId() + "/contact";
	}

	// your profile handler
	@GetMapping("/profile")
	public String yourProfile(Model m) {
		m.addAttribute("title", "Profile Page");
		return "normal/profile";
	}

	// user delete handler
	@PostMapping("/delete-user/{uid}")
	public String deleteUser(@PathVariable("uid") Integer uid, HttpSession s) {
		System.out.println(uid);
		try {
			
			User user = this.repository.findById(uid).get();
			
			File file = new ClassPathResource("static/img").getFile();
			Path path = Paths.get(file.getAbsolutePath()+"/"+user.getImageUrl());
			Files.delete(path);
			
			this.repository.deleteById(uid);
			
			s.setAttribute("message", new Message("User Succesfully Delete See you Soon !!", "success"));

		} catch (Exception e) {
			e.printStackTrace();
			s.setAttribute("message", new Message("User Not Delete TRY AGAIN...", "danger"));
		}
		return "home";
	}

	// user update open form handler
	@PostMapping("/user-update/{uid}")
	public String updateOpenUser(Model m) {
		m.addAttribute("title", "Update User");
		return "normal/update_user_form";
	}

	// user update handler
	@PostMapping("/process-user-update/{uid}")
	public String updateUser(@ModelAttribute User user, @PathVariable("uid") Integer uid, Principal p,
			@RequestParam("profileImage") MultipartFile file, HttpSession s) {
		try {
			// old User Detail..
			User oldUserDetail = this.repository.findById(uid).get();

			// Image..
			if (file.isEmpty()) {
				
				user.setImageUrl("default.jpg");

			} else {

				// delete old photo...
				File deletefile = new ClassPathResource("static/img").getFile();
				File file1 = new File(deletefile, oldUserDetail.getImageUrl());
				file1.delete();

				// Add New Photo
				File saveFile = new ClassPathResource("static/img").getFile();
				Path path = Paths.get(saveFile.getAbsolutePath() + File.separator + file.getOriginalFilename());
				Files.copy(file.getInputStream(), path, StandardCopyOption.REPLACE_EXISTING);
				user.setImageUrl(file.getOriginalFilename());

			}
			
			User user1 = this.repository.getUserByUserName(p.getName());
			
			this.repository.save(user1);
			
			s.setAttribute("message", new Message("Your Information Update SuccesFully...", "success"));
			
		} catch (Exception e) {

			e.printStackTrace();
			s.setAttribute("message", new Message("Something Went Wrong Try Again Sometime Later !!", "danger"));

		}

		return "normal/profile";
	}
	
	// Open setting handler
	@GetMapping("/settings")
	public String openSetting()
	{
		return "normal/settings";
	}
	
	//Change Password handler
	@PostMapping("/change-password")
	public String changePassword(@RequestParam("oldpassword") String oldpassword, HttpSession s,
			@RequestParam("newpassword") String newpassword,Principal p)
	{
		System.out.println("Old password "+oldpassword);
		System.out.println("New password "+newpassword);
		
		User currentUser = this.repository.getUserByUserName(p.getName());
		System.out.println(currentUser);
		
		if (this.bCryptPasswordEncoder.matches(oldpassword, currentUser.getPassword())) 
		{
			
			currentUser.setPassword(this.bCryptPasswordEncoder.encode(newpassword));
			this.repository.save(currentUser);
			s.setAttribute("message", new Message("Your Password is Succesfully Updated", "success"));
			
		} else {

			// error Code
			s.setAttribute("message", new Message("Please Enter Correct Old Password", "danger"));
			
		}
		
		return "redirect:/user/index";
	}
	
	//Creating order for payment
	@PostMapping("/create_order")
	@ResponseBody
	public String createOrder(@RequestBody Map<String, Object> data,Principal p) throws RazorpayException
	{
		//System.out.println("order_function ex");
		System.out.println(data);
		
		int amt = Integer.parseInt(data.get("amount").toString());
		
		var client = new RazorpayClient("rzp_test_CasvULeibjcRSx", "RwCP0bcss54obPOxQEpLIefS");
		
		JSONObject ob = new JSONObject();
		ob.put("amount", amt*100);
		ob.put("currency", "INR");
		ob.put("receipt", "txn_909909");
		
		//creating new order
		Order order = client.orders.create(ob);
		System.out.println(order);
		
		//save the order in database
		
		MyOrder myOrder = new MyOrder();
		
		myOrder.setAmount(order.get("amount") + "");
		myOrder.setOrderId(order.get("id"));
		myOrder.setPaymentId(null);
		myOrder.setStatus("created");
		myOrder.setUser(this.repository.getUserByUserName(p.getName()));
		myOrder.setReceipt(order.get("receipt"));
		
		this.myOrderRepository.save(myOrder);
		
		//if you want you can save this to your data
		
		return order.toString();
	}
	
	@PostMapping("/update_order")
	public ResponseEntity<?> updateOrder(@RequestBody JsonNode data)
	{   try{
		MyOrder myorder = this.myOrderRepository.findByOrderId(data.get("order_id").asText());
		
		myorder.setPaymentId(data.get("payment_id").asText());
		myorder.setStatus(data.get("status").asText());
		System.out.println(data);
		this.myOrderRepository.save(myorder); 
		
		
		return ResponseEntity.ok(Map.of("msg","updated"));
		
	}catch(Exception e){
		e.getLocalizedMessage();
		System.out.println(e);
		
		return ResponseEntity.ok(Map.of("msg","updated"));
	}
	
	}
	
}