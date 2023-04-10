package shop.mtcoding.metamall.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import shop.mtcoding.metamall.core.exception.Exception400;
import shop.mtcoding.metamall.dto.user.UserRequest;
import shop.mtcoding.metamall.model.product.Product;
import shop.mtcoding.metamall.model.product.ProductRepository;

import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@RestController
public class ProductController {

    private final ProductRepository productRepository;

    @PostMapping("/seller/product/enroll")
    public Product enroll(@RequestBody UserRequest.ProductDto productDto) {
        Optional<Product> productOP = productRepository.findByName();
        if (productOP.isPresent()) {
            throw new Exception400("이미 등록된 상품입니다.");
        }

        Product product = Product.builder().name(productDto.getName())
                .price(productDto.getPrice())
                .qty(productDto.getQty())
                .createdAt(productDto.getCreatedAt())
                .build();

        return product;
    }

    @GetMapping("/product")
    public List<Product> allProduct() {
        List<Product> products = productRepository.findAll();
        return products;
    }

    @GetMapping("/product/{id}")
    public Product product(@PathVariable Long id) {
        Optional<Product> findProduct = productRepository.findById(id);
        if (!findProduct.isPresent()) {
            throw new Exception400("잘못된 접근입니다.");
        }
        return findProduct.get();
    }

    @PutMapping("/seller/product/{id}")
    public Product updateProduct(@PathVariable Long id, @RequestBody UserRequest.ProductDto productDto) {
        Optional<Product> findProduct = productRepository.findById(id);
        if (!findProduct.isPresent()) {
            throw new Exception400("잘못된 접근입니다.");
        }
        Product product = findProduct.get();
        product.setName(productDto.getName());
        product.setUpdatedAt(productDto.getUpdatedAt());
        product.setQty(productDto.getQty());
        product.setPrice(productDto.getPrice());

        return product;
    }

    @DeleteMapping("/seller/product/{id}")
    public void deleteProduct(@PathVariable Long id) {
        Optional<Product> product = productRepository.findById(id);
        if (!product.isPresent()) {
            throw new Exception400("잘못된 접근입니다.");
        }
        productRepository.deleteById(id);
    }
}
