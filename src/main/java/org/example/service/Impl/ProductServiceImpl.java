package org.example.service.Impl;

import lombok.RequiredArgsConstructor;
import org.example.entity.Brand;
import org.example.entity.Origin;
import org.example.entity.Product;
import org.example.entity.ProductType;
import org.example.entity.enums.Status;
import org.example.repository.BrandRepository;
import org.example.repository.OriginRepository;
import org.example.repository.ProductRepository;
import org.example.repository.ProductTypeRepository;
import org.example.service.IProductService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

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
       if (product!=null){
           product.setAvatar(newProduct.getAvatar());
           product.setTitle(newProduct.getTitle());
           product.setDescription(newProduct.getDescription());
           product.setPrice(newProduct.getPrice());
           product.setMaterial(newProduct.getMaterial());
           product.setStatus(newProduct.getStatus());
           if (newProduct.getProductType()!=null && newProduct.getProductType().getProductTypeID()!=null){
               ProductType productType = productTypeRepository.findById(newProduct.getProductType().getProductTypeID()).orElse(null);
               product.setProductType(productType);
           }
           if (newProduct.getBrandID()!=null && newProduct.getBrandID().getBrandID()!=null){
               Brand brand = brandRepository.findById(newProduct.getBrandID().getBrandID()).orElse(null);
               product.setBrandID(brand);
           }
           if (newProduct.getOriginID()!=null && newProduct.getOriginID().getOriginID()!=null)
           {
               Origin origin = originRepository.findById(newProduct.getOriginID().getOriginID()).orElse(null);
               product.setOriginID(origin);
           }
           return productRepository.save(product);
       }
       return null;
    }

    @Override
    public void  deleteProduct(Integer id) {
        Product product = productRepository.findById(id).orElse(null);
        assert product != null;
        product.setStatus(Status.Disable);
        productRepository.save(product);
    }
}