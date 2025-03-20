package com.example.shipment_2.controllers;

import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.example.shipment_2.models.Merchant;
import com.example.shipment_2.models.Product;
import com.example.shipment_2.models.ProductDTO;
import com.example.shipment_2.models.Shipment;
import com.example.shipment_2.repositories.MerchantsRepository;
import com.example.shipment_2.repositories.ProductsRepository;
import com.example.shipment_2.repositories.ShipmentsRepository;

import jakarta.transaction.Transactional;
import jakarta.validation.Valid;

@Controller
@RequestMapping("/products")
public class ProductsController {
	@Autowired
	private ProductsRepository repo;
	
	@Autowired
	private ShipmentsRepository shipmentRepo;

	@Autowired
	private MerchantsRepository merchantRepo;
	
	@GetMapping({"", "/"})
	public String showProductList(Model model) {
		List<Product> products = repo.findAll();
		model.addAttribute("products", products);
		return "products/index";
	}
	
	@GetMapping("/detail/{id}")
    public String showProductDetail(@PathVariable("id") int id, RedirectAttributes redirectAttributes, Model model) {
		try {
	        Product product = repo.findById(id).get();
	        List<Shipment> shipments = shipmentRepo.findByProduct(product);

	        model.addAttribute("product", product);
	        model.addAttribute("shipments", shipments);
	        return "products/detail";
	        
	    } catch (Exception e) {
	    	
	        redirectAttributes.addFlashAttribute("errorMessage", "Product not found!");
	        return "redirect:/products";
	    }
    }
	
	@GetMapping("/create")
	public String createProductPage(Model model) {
		ProductDTO productDTO = new ProductDTO();
		List<Merchant> merchants = merchantRepo.findAll(Sort.by(Sort.Direction.ASC, "name"));
        
		model.addAttribute("productDTO", productDTO);
		model.addAttribute("merchants", merchants);
        
		return "products/create";
	}
	
	@PostMapping("/create")
	public String createProduct(
			@Valid @ModelAttribute ProductDTO productDTO,
			BindingResult result, RedirectAttributes redirectAttributes
			) {
		
		if (productDTO.getImage().isEmpty()) {
			result.addError(new FieldError("productDTO", "image", "The product image file is required"));
		}
		
		if (result.hasErrors()) {
			redirectAttributes.addFlashAttribute("errorMessage", "Fail to store product!");
			return "products/index";
		}
		
		MultipartFile image = productDTO.getImage();
		Date created_at = new Date();
		String storageFileName = created_at.getTime() + "_" + image.getOriginalFilename();
		
		try {
			String uploadDir = "public/images/";
			Path uploadPath = Paths.get(uploadDir);
			
			if (!Files.exists(uploadPath)) {
				Files.createDirectories(uploadPath);
			}
			
			try (InputStream inputStream = image.getInputStream()) {
				Files.copy(inputStream, Paths.get(uploadDir + storageFileName),
						StandardCopyOption.REPLACE_EXISTING);
			}
		} catch (Exception ex) {
			System.out.println("Exception: " + ex.getMessage());
		}
		
		Product product = new Product();
		product.setMerchant(productDTO.getMerchantId());
		product.setName(productDTO.getName());
		product.setImage(storageFileName);
		product.setPrice(productDTO.getPrice());
		product.setUnit(productDTO.getUnit());
		
		repo.save(product);

		redirectAttributes.addFlashAttribute("successMessage", "New product has been added!");
		return "redirect:/products";
	}
	
	@GetMapping("/update/{id}")
    public String showUpdatePage(@PathVariable("id") int id, RedirectAttributes redirectAttributes, Model model) {
		try {
	        Product product = repo.findById(id).get();
	        ProductDTO productDTO = new ProductDTO();
	        List<Merchant> merchants = merchantRepo.findAll(Sort.by(Sort.Direction.ASC, "name"));
	        
	        productDTO.setMerchantId(product.getMerchant());
	        productDTO.setName(product.getName());
	        productDTO.setPrice(product.getPrice());
	        productDTO.setUnit(product.getUnit());

	        model.addAttribute("product", product);
	        model.addAttribute("merchants", merchants);
	        model.addAttribute("productDTO", productDTO);
	        
	        return "products/update";
	        
	    } catch (Exception e) {
	        redirectAttributes.addFlashAttribute("errorMessage", "Product not found!");
	        return "redirect:/products";
	    }
    }
	
	@PostMapping("/update/{id}")
	public String updateProduct(@PathVariable("id") int id, @Valid @ModelAttribute ProductDTO productDTO, Model model, BindingResult result, RedirectAttributes redirectAttributes) {
		
		try {
			
			Product product = repo.findById(id).get();
			model.addAttribute("product", product);
			
			if (result.hasErrors()) {
				redirectAttributes.addFlashAttribute("errorMessage", "Fail to store product!");
				return "products/index";
			}
			
			if (!productDTO.getImage().isEmpty()) {
				
				String uploadDir = "public/images/";
				Path oldImagePath = Paths.get(uploadDir + product.getImage());
				
				try {
					Files.delete(oldImagePath);
				} catch (Exception e) {
					System.out.println("Exception: " + e.getMessage());
				}
		
				MultipartFile image = productDTO.getImage();
				Date created_at = new Date();
				String storageFileName = created_at.getTime() + "_" + image.getOriginalFilename();
					
				try (InputStream inputStream = image.getInputStream()) {
					Files.copy(inputStream, Paths.get(uploadDir + storageFileName),
							StandardCopyOption.REPLACE_EXISTING);
				}
				
				product.setImage(storageFileName);
			}
			
			product.setMerchant(productDTO.getMerchantId());
			product.setName(productDTO.getName());
			product.setPrice(productDTO.getPrice());
			product.setUnit(productDTO.getUnit());
			
			repo.save(product);
	
			redirectAttributes.addFlashAttribute("successMessage", "Product information has been updated!");
		
		} catch (Exception e) {
			redirectAttributes.addFlashAttribute("successMessage", "Fail to update product information!");			
		}
		
		return "redirect:/products";
	}
	
	@PostMapping("/delete/{id}")
	@Transactional
	public String deleteProduct(@PathVariable("id") int id, RedirectAttributes redirectAttributes) {
	    try {
	        Optional<Product> productOpt  = repo.findById(id);
	        if (productOpt .isPresent()) {
	            Product product = productOpt.get();
	            
	            shipmentRepo.deleteByProduct(product);

	            Path imagePath = Paths.get("public/images/" + product.getImage());
				
				try {
					Files.delete(imagePath);
				} catch (Exception e) {
					System.out.println("Exception: " + e.getMessage());
				}
				
	            repo.delete(product);

	            redirectAttributes.addFlashAttribute("successMessage", "Product and related shipments deleted!");
	        } else {
	            redirectAttributes.addFlashAttribute("errorMessage", "Product not found!");
	        }
	    } catch (Exception e) {
	        redirectAttributes.addFlashAttribute("errorMessage", "Failed to delete product information!");
	    }
	    
	    return "redirect:/products";
	}
}
