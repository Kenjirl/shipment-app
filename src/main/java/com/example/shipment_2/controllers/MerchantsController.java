package com.example.shipment_2.controllers;

import java.util.ArrayList;
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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.example.shipment_2.models.Merchant;
import com.example.shipment_2.models.MerchantDTO;
import com.example.shipment_2.models.Product;
import com.example.shipment_2.models.Shipment;
import com.example.shipment_2.repositories.MerchantsRepository;
import com.example.shipment_2.repositories.ProductsRepository;
import com.example.shipment_2.repositories.ShipmentsRepository;

import jakarta.transaction.Transactional;
import jakarta.validation.Valid;

@Controller
@RequestMapping("/merchants")
public class MerchantsController {
	@Autowired
	private MerchantsRepository repo;
	
	@Autowired
	private ProductsRepository productRepo;

	@Autowired
	private ShipmentsRepository shipmentRepo;
	
	@GetMapping({"", "/"})
	public String showMerchantList(Model model) {
		List<Merchant> merchants = repo.findAll();
		model.addAttribute("merchants", merchants);
		return "merchants/index";
	}
	
	@GetMapping("/detail/{id}")
	public String showMerchantDetail(@PathVariable("id") int id, @RequestParam(value = "tab", defaultValue = "products") String tab, RedirectAttributes redirectAttributes, Model model) {
		try {
			if (!tab.equals("products") && !tab.equals("shipments")) {
	            tab = "products";
	        }
	        model.addAttribute("activeTab", tab);
	        
			Merchant merchant = repo.findById(id).get();
			List<Product> products = productRepo.findByMerchant(merchant);
			List<Shipment> shipments = products.isEmpty() ? new ArrayList<>() : shipmentRepo.findByProductIn(products);
			
			model.addAttribute("merchant", merchant);
			model.addAttribute("products", products);
			model.addAttribute("shipments", shipments);
	        
			return "merchants/detail";
			
		} catch (Exception e) {
			redirectAttributes.addFlashAttribute("errorMessage", "Merchant not found!");
			return "merchants/detail";
		}
    }
	
	@GetMapping("/create")
	public String createMerchantPage(Model model) {
		MerchantDTO merchantDTO = new MerchantDTO();
        
		model.addAttribute("merchantDTO", merchantDTO);
        
		return "merchants/create";
	}
	
	@PostMapping("/create")
	public String createMerchant(
			@Valid @ModelAttribute MerchantDTO merchantDTO,
			BindingResult result, RedirectAttributes redirectAttributes
			) {
		Merchant merchant = new Merchant();
		merchant.setName(merchantDTO.getName());
		merchant.setPhone(merchantDTO.getPhone());
		merchant.setAddress(merchantDTO.getAddress());
		repo.save(merchant);
		
		redirectAttributes.addFlashAttribute("successMessage", "New merchant has been added!");
		return "redirect:/merchants";
	}
	
	@GetMapping("/update/{id}")
	public String updateMerchantPage(@PathVariable("id") int id, RedirectAttributes redirectAttributes, Model model) {
		try {
			
			Merchant merchant = repo.findById(id).get();
        	MerchantDTO merchantDTO = new MerchantDTO();
    		
        	merchantDTO.setName(merchant.getName());
        	merchantDTO.setPhone(merchant.getPhone());
        	merchantDTO.setAddress(merchant.getAddress());
            
    		model.addAttribute("merchant", merchant);
    		model.addAttribute("merchantDTO", merchantDTO);

    		return "merchants/update";
        } catch (Exception e){
        	redirectAttributes.addFlashAttribute("errorMessage", "Merchant not found!");
            return "redirect:/merchants";
        }
	}
	
	@PostMapping("/update/{id}")
	public String updateMerchant(Model model, @PathVariable("id") int id, RedirectAttributes redirectAttributes, @Valid @ModelAttribute MerchantDTO merchantDTO, BindingResult result) {
		
		try {
			Merchant merchant = repo.findById(id).get();
			model.addAttribute("merchant", merchant);
			
			if (result.hasErrors()) {
				redirectAttributes.addFlashAttribute("errorMessage", "Fail to update merchant information!");
	            return "redirect:/merchants";
			}
			
			merchant.setName(merchantDTO.getName());
			merchant.setPhone(merchantDTO.getPhone());
			merchant.setAddress(merchantDTO.getAddress());
			
			repo.save(merchant);
			redirectAttributes.addFlashAttribute("successMessage", "Merchant information updated!");
			
		} catch (Exception e) {
			redirectAttributes.addFlashAttribute("errorMessage", "Fail to update merchant information!");
		}
		
		return "redirect:/merchants";
	}
	
	@PostMapping("/delete/{id}")
	@Transactional
	public String deleteMerchant(@PathVariable("id") int id, RedirectAttributes redirectAttributes) {
	    try {
	        Optional<Merchant> merchantOpt  = repo.findById(id);
	        if (merchantOpt .isPresent()) {
	            Merchant merchant = merchantOpt .get();

	            List<Product> products = productRepo.findByMerchant(merchant);
	            
	            if (!products.isEmpty()) {
	                List<Shipment> shipments = shipmentRepo.findByProductIn(products);
	                shipmentRepo.deleteAll(shipments);
	                productRepo.deleteAll(products);
	            }

	            repo.delete(merchant);

	            redirectAttributes.addFlashAttribute("successMessage", "Merchant with it's related products and shipments deleted!");
	        } else {
	            redirectAttributes.addFlashAttribute("errorMessage", "Merchant not found!");
	        }
	    } catch (Exception e) {
	        redirectAttributes.addFlashAttribute("errorMessage", "Failed to delete merchant information!");
	    }
	    
	    return "redirect:/merchants";
	}
}
