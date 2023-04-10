package shop.mtcoding.metamall.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;
import shop.mtcoding.metamall.domain.Product;
import shop.mtcoding.metamall.domain.User;

import java.util.Optional;

public interface ProductRepository extends JpaRepository<Product, Long> {
    Optional<Product> findByName(@Param("name") String name);

}
