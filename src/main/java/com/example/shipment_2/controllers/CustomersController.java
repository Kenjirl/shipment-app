package com.example.shipment_2.controllers;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.example.shipment_2.models.Customer;
import com.example.shipment_2.models.CustomerDTO;
import com.example.shipment_2.models.Shipment;
import com.example.shipment_2.repositories.CustomersRepository;
import com.example.shipment_2.repositories.ShipmentsRepository;

import jakarta.transaction.Transactional;
import jakarta.validation.Valid;

@Controller
@RequestMapping("/customers")
public class CustomersController {
	@Autowired
	private CustomersRepository repo;
	
	@Autowired
	private ShipmentsRepository shipmentRepo;
	
	@GetMapping({"", "/"})
	public String showCustomerList(Model model) {
		List<Customer> customers = repo.findAll();
		model.addAttribute("customers", customers);
		return "customers/index";
	}
	
	@GetMapping("/detail/{id}")
    public String showCustomerDetail(@PathVariable("id") int id, RedirectAttributes redirectAttributes, Model model) {
		try {
	        Customer customer = repo.findById(id).get();

	        List<Shipment> shipments = shipmentRepo.findByCustomer(customer);

	        model.addAttribute("customer", customer);
	        model.addAttribute("shipments", shipments);
	        return "customers/detail";
	        
	    } catch (Exception e) {
	    	
	        redirectAttributes.addFlashAttribute("errorMessage", "Customer not found!");
	        return "redirect:/customers";
	    }
    }
	
	@GetMapping("/create")
	public String createCustomerPage(Model model) {
		CustomerDTO customerDTO = new CustomerDTO();
        
		model.addAttribute("customerDTO", customerDTO);
        
		return "customers/create";
	}
	
	@PostMapping("/create")
	public String createCustomer(
			@Valid @ModelAttribute CustomerDTO customerDTO,
			BindingResult result, RedirectAttributes redirectAttributes
			) {
		Customer customer = new Customer();
		customer.setName(customerDTO.getName());
		customer.setPhone(customerDTO.getPhone());
		customer.setAddress(customerDTO.getAddress());
		repo.save(customer);
		
		redirectAttributes.addFlashAttribute("successMessage", "New customer has been added!");
		return "redirect:/customers";
	}
	
	@GetMapping("/update/{id}")
	public String updateCustomerPage(@PathVariable("id") int id, RedirectAttributes redirectAttributes, Model model) {
		try {
			
			Customer customer = repo.findById(id).get();
        	CustomerDTO customerDTO = new CustomerDTO();
    		
        	customerDTO.setName(customer.getName());
        	customerDTO.setPhone(customer.getPhone());
        	customerDTO.setAddress(customer.getAddress());
            
    		model.addAttribute("customer", customer);
    		model.addAttribute("customerDTO", customerDTO);

    		return "customers/update";
        } catch (Exception e){
        	redirectAttributes.addFlashAttribute("errorMessage", "Customer not found!");
            return "redirect:/customers";
        }
	}
	
	@PostMapping("/update/{id}")
	public String updateCustomer(Model model, @PathVariable("id") int id, RedirectAttributes redirectAttributes, @Valid @ModelAttribute CustomerDTO customerDTO, BindingResult result) {
		
		try {
			Customer customer = repo.findById(id).get();
			model.addAttribute("customer", customer);
			
			if (result.hasErrors()) {
				redirectAttributes.addFlashAttribute("errorMessage", "Fail to update customer information!");
	            return "redirect:/customers";
			}
			
			customer.setName(customerDTO.getName());
			customer.setPhone(customerDTO.getPhone());
			customer.setAddress(customerDTO.getAddress());
			
			repo.save(customer);
			redirectAttributes.addFlashAttribute("successMessage", "Customer information updated!");
			
		} catch (Exception e) {
			redirectAttributes.addFlashAttribute("errorMessage", "Fail to update customer information!");
		}
		
		return "redirect:/customers";
	}
	
	@PostMapping("/delete/{id}")
	@Transactional
	public String deleteCustomer(@PathVariable("id") int id, RedirectAttributes redirectAttributes) {
	    try {
	        Optional<Customer> customerOpt  = repo.findById(id);
	        if (customerOpt .isPresent()) {
	            Customer customer = customerOpt .get();
	            
	            shipmentRepo.deleteByCustomer(customer);

	            repo.delete(customer);

	            redirectAttributes.addFlashAttribute("successMessage", "Customer and related shipments deleted!");
	        } else {
	            redirectAttributes.addFlashAttribute("errorMessage", "Customer not found!");
	        }
	    } catch (Exception e) {
	        redirectAttributes.addFlashAttribute("errorMessage", "Failed to delete customer information!");
	    }
	    
	    return "redirect:/customers";
	}

}
