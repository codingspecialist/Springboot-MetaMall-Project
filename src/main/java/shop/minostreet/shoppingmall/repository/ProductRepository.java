package shop.minostreet.shoppingmall.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.data.web.PageableDefault;
import shop.minostreet.shoppingmall.domain.Product;

import java.util.Optional;

public interface ProductRepository extends JpaRepository<Product, Long> {
    Optional<Product> findByName(@Param("name") String name);
    @EntityGraph(attributePaths = "seller")
    Page<Product> findAll(Pageable pageable);
}
