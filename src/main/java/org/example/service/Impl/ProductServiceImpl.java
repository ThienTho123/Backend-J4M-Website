package org.example.service.Impl;

import lombok.RequiredArgsConstructor;
import org.example.dto.ProductDTO;
import org.example.entity.*;
import org.example.entity.enums.Status;
import org.example.repository.BrandRepository;
import org.example.repository.OriginRepository;
import org.example.repository.ProductRepository;
import org.example.repository.ProductTypeRepository;
import org.example.service.IProductService;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ProductServiceImpl implements IProductService {
    private final ProductRepository productRepository;
    private final ProductTypeRepository productTypeRepository;
    private final BrandRepository brandRepository;
    private final OriginRepository originRepository;

    @Override
    public List<Product> findAll() {
        return productRepository.findAll();
    }

    @Override
    public List<Product> findAllEnable() {
        return productRepository.findProductByStatusOrderByProductIDDesc(Status.Enable);
    }

    @Override
    public Product updateProduct(Integer id, Product newProduct) {
        Product product = productRepository.findById(id).orElse(null);
        if (product != null) {
            product.setAvatar(newProduct.getAvatar());
            product.setTitle(newProduct.getTitle());
            product.setDescription(newProduct.getDescription());
            product.setPrice(newProduct.getPrice());
            product.setMaterial(newProduct.getMaterial());
            product.setStatus(newProduct.getStatus());
            if (newProduct.getProductType() != null && newProduct.getProductType().getProductTypeID() != null) {
                ProductType productType = productTypeRepository.findById(newProduct.getProductType().getProductTypeID()).orElse(null);
                product.setProductType(productType);
            }
            if (newProduct.getBrandID() != null && newProduct.getBrandID().getBrandID() != null) {
                Brand brand = brandRepository.findById(newProduct.getBrandID().getBrandID()).orElse(null);
                product.setBrandID(brand);
            }
            if (newProduct.getOriginID() != null && newProduct.getOriginID().getOriginID() != null) {
                Origin origin = originRepository.findById(newProduct.getOriginID().getOriginID()).orElse(null);
                product.setOriginID(origin);
            }
            return productRepository.save(product);
        }
        return null;
    }

    @Override
    public void deleteProduct(Integer id) {
        Product product = productRepository.findById(id).orElse(null);
        if (product != null) {
            product.setStatus(Status.Disable); // Chuyển trạng thái thành Disable
            productRepository.save(product); // Lưu lại thay đổi vào cơ sở dữ liệu
        }
    }

    @Override
    public Product getProductById(Integer id) {
        return productRepository.findById(id).orElse(null);
    }


    @Override
    public List<Product> findByBrand(String brand, Status status) {
        return productRepository.findProductsByBrandIDBrandNameAndStatusOrderByProductIDDesc(brand, status);
    }

    @Override
    public List<Product> findByCategory(String category, Status status) {
        return productRepository.findProductsByProductTypeCategoryIDCategoryNameAndStatusOrderByProductIDDesc(category, status);
    }

    @Override
    public List<Product> findByProductType(String producttype, Status status) {
        return productRepository.findProductsByProductTypeTypeNameAndStatusOrderByProductIDDesc(producttype, status);
    }

    @Override

    public List<Product> sortProduct(int origin, int size, int category, int productType, int brand) {
        return productRepository.sortProduct(origin, size, category, productType, brand);
    }

    @Override

    public List<Product> findByTitle(String title, Status status) {
        return productRepository.findProductsByTitleContainingAndStatusOrderByProductIDDesc(title, status);
    }

    @Override
    public List<ProductDTO> find10HotestProductEnable() {
        Pageable topTen = PageRequest.of(0, 10);
        List<ProductDTO> top10BestSellingProducts = productRepository.findTop10BestSellingProducts(topTen);
        return top10BestSellingProducts;
    }

    @Override
    public List<Product> findProductWithBrand(Integer brandID) {
        Pageable topTen = PageRequest.of(0, 8);
        return productRepository.findProductsByBrandIDBrandIDAndStatusOrderByProductIDDesc(brandID,topTen,Status.Enable);
    }

    @Override
    public List<Product> findProductSimilar(Integer productTypeID) {
        Pageable topTen = PageRequest.of(0, 20);
        return productRepository.findProductsByProductType_ProductTypeIDAndStatusOrderByProductIDDesc(productTypeID,topTen,Status.Enable);
    }

    @Override
    public Integer HowManyBought(int id) {
        return productRepository.HowManyBought(id);
    }
}


