package shop.minostreet.shoppingmall.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.data.web.PageableDefault;
import shop.minostreet.shoppingmall.domain.Product;

import java.util.Optional;

public interface ProductRepository extends JpaRepository<Product, Long> {
    Optional<Product> findByName(@Param("name") String name);
    Page<Product> findAll(@PageableDefault(size = 10, page = 0, direction = Sort.Direction.DESC) Pageable pageable);
}
