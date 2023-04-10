package shop.mtcoding.metamall.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import shop.mtcoding.metamall.domain.Product;

public interface ProductRepository extends JpaRepository<Product, Long> {
}
