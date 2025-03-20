package com.example.shipment_2.controllers;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
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
import com.example.shipment_2.models.Product;
import com.example.shipment_2.models.Shipment;
import com.example.shipment_2.models.ShipmentDTO;
import com.example.shipment_2.repositories.CustomersRepository;
import com.example.shipment_2.repositories.ProductsRepository;
import com.example.shipment_2.repositories.ShipmentsRepository;

import jakarta.validation.Valid;

@Controller
@RequestMapping("/shipments")
public class ShipmentsController {
	@Autowired
	private ShipmentsRepository repo;
	@Autowired
	private CustomersRepository customerRepo;
	@Autowired
	private ProductsRepository productRepo;
	
	@GetMapping({"", "/"})
	public String showShipmentList(Model model) {
		List<Shipment> shipments = repo.findAll(Sort.by(Sort.Direction.DESC, "createdAt"));
		model.addAttribute("shipments", shipments);
		return "shipments/index";
	}
	
	@GetMapping("/detail/{id}")
    public String showShipmentDetail(@PathVariable("id") int id, RedirectAttributes redirectAttributes, Model model) {
        Optional<Shipment> shipment = repo.findById(id);
        if (shipment.isPresent()) {
            model.addAttribute("shipment", shipment.get());
            return "shipments/detail";
        } else {
        	redirectAttributes.addFlashAttribute("errorMessage", "Shipment not found!");
            return "redirect:/shipments";
        }
    }
	
	@GetMapping("/create")
	public String createShipmentPage(Model model) {
		ShipmentDTO shipmentDTO = new ShipmentDTO();
		List<Customer> customers = customerRepo.findAll(Sort.by(Sort.Direction.ASC, "name"));
		List<Product> products = productRepo.findAll(Sort.by(Sort.Direction.ASC, "merchant.name", "name"));
        
		model.addAttribute("shipmentDTO", shipmentDTO);
		model.addAttribute("products", products);
        model.addAttribute("customers", customers);
        
		return "shipments/create";
	}
	
	@PostMapping("/create")
	public String createShipment(
			@Valid @ModelAttribute ShipmentDTO shipmentDTO,
			BindingResult result, RedirectAttributes redirectAttributes
			) {
		Shipment shipment = new Shipment();
		shipment.setProduct(shipmentDTO.getProductId());
		shipment.setCustomer(shipmentDTO.getCustomerId());
		shipment.setProductQuantity(shipmentDTO.getProductQuantity());
		shipment.setShipmentPrice(shipmentDTO.getShipmentPrice());
		shipment.setTotalPrice(shipmentDTO.getTotalPrice());
		shipment.setStatus("Shipped");
		shipment.setCreatedAt(shipmentDTO.getCreatedAt());
		shipment.setArrivedAt(shipmentDTO.getArrivedAt());
		repo.save(shipment);
		
		redirectAttributes.addFlashAttribute("successMessage", "New delivery has been sent out!");
		return "redirect:/shipments";
	}
	
	@GetMapping("/update/{id}")
	public String updateShipmentPage(@PathVariable("id") int id, RedirectAttributes redirectAttributes, Model model) {
		try {
			
			Shipment shipment = repo.findById(id).get();
        	ShipmentDTO shipmentDTO = new ShipmentDTO();
    		List<Customer> customers = customerRepo.findAll(Sort.by(Sort.Direction.ASC, "name"));
    		List<Product> products = productRepo.findAll(Sort.by(Sort.Direction.ASC, "merchant.name", "name"));
    		
    		shipmentDTO.setCustomerId(shipment.getCustomer());
    		shipmentDTO.setProductId(shipment.getProduct());
    		shipmentDTO.setProductQuantity(shipment.getProductQuantity());
    		shipmentDTO.setShipmentPrice(shipment.getShipmentPrice());
    		shipmentDTO.setStatus(shipment.getStatus());
    		shipmentDTO.setCreatedAt(shipment.getCreatedAt());
    		shipmentDTO.setArrivedAt(shipment.getArrivedAt());
            
    		model.addAttribute("shipment", shipment);
    		model.addAttribute("shipmentDTO", shipmentDTO);
    		model.addAttribute("products", products);
            model.addAttribute("customers", customers);
            
    		return "shipments/update";
        } catch (Exception e){
        	redirectAttributes.addFlashAttribute("errorMessage", "Shipment not found!");
            return "redirect:/shipments";
        }
	}
	
	@PostMapping("/update/{id}")
	public String updateShipment(
			Model model,
			@PathVariable("id") int id, RedirectAttributes redirectAttributes, 
			@Valid @ModelAttribute ShipmentDTO shipmentDTO,
			BindingResult result
			) {
		
		try {
			Shipment shipment = repo.findById(id).get();
			model.addAttribute("shipment", shipment);
			
			if (result.hasErrors()) {
				redirectAttributes.addFlashAttribute("errorMessage", "Fail to update shipment information!");
	            return "redirect:/shipments";
			}
			
			shipment.setCustomer(shipmentDTO.getCustomerId());
			shipment.setProduct(shipmentDTO.getProductId());
			shipment.setProductQuantity(shipmentDTO.getProductQuantity());
			shipment.setShipmentPrice(shipmentDTO.getShipmentPrice());
			shipment.setTotalPrice((shipment.getProduct().getPrice() * shipmentDTO.getProductQuantity()) + shipmentDTO.getShipmentPrice());
			shipment.setStatus(shipmentDTO.getStatus());
			shipment.setCreatedAt(shipmentDTO.getCreatedAt());
			shipment.setArrivedAt(shipmentDTO.getArrivedAt());
			
			repo.save(shipment);
			redirectAttributes.addFlashAttribute("successMessage", "Shipment updated!");
			
		} catch (Exception e) {
			redirectAttributes.addFlashAttribute("errorMessage", "Fail to update shipment information!");
		}
		
		return "redirect:/shipments";
	}
	
	@PostMapping("delete/{id}")
	public String deleteShipment(@PathVariable("id") int id, RedirectAttributes redirectAttributes) {
		
		try {
			
			Shipment shipment = repo.findById(id).get();
			
			repo.delete(shipment);
			
			redirectAttributes.addFlashAttribute("successMessage", "Shipment deleted!");
			
		} catch (Exception e) {
			redirectAttributes.addFlashAttribute("errorMessage", "Failed to delete shipment information!");
		}
		
		return "redirect:/shipments";
	}
}
