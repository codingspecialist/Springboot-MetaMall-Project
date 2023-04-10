package shop.mtcoding.metamall.model.product;

import org.springframework.data.jpa.repository.JpaRepository;
import shop.mtcoding.metamall.dto.product.ProductRequest;

import javax.transaction.Transactional;
import java.util.Optional;

public interface ProductRepository extends JpaRepository<Product, Long> {

    @Transactional
    default Product updateName(ProductRequest.UpdateDto updateDto){
        Optional<Product> productPS = findById(updateDto.getId());
        Product product = productPS.get();
        if(productPS.isPresent()){
                product.setName(updateDto.getName());
        }
        return product;
    }
    @Transactional
    default Product updateQty(ProductRequest.UpdateDto updateDto){
        Optional<Product> productPS = findById(updateDto.getId());
        Product product = productPS.get();
        if(productPS.isPresent()){
            product.setQty(updateDto.getQty());
        }
        return product;
    }
    @Transactional
    default Product updatePrice(ProductRequest.UpdateDto updateDto){
        Optional<Product> productPS = findById(updateDto.getId());
        Product product = productPS.get();
        if(productPS.isPresent()){
            product.setPrice(updateDto.getPrice());
        }
        return product;
    }
}
