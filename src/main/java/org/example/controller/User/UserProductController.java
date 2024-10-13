package org.example.controller.User;

import lombok.RequiredArgsConstructor;
import org.example.entity.Product;
import org.example.repository.ProductRepository;
import org.example.service.IProductService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/product")
@RequiredArgsConstructor
public class UserProductController {

    private final IProductService productService;

    @GetMapping("")
    public ResponseEntity<?> getListProduct() {
        List<Product> productList = productService.findAllEnable();

        if (!productList.isEmpty()) {
            Map<String, Object> response = new HashMap<>();
            response.put("products", productList);
            return ResponseEntity.ok(response);
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("No products found");
        }
    }
}