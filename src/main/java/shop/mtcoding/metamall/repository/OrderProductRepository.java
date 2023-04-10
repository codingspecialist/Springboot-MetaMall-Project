package shop.mtcoding.metamall.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import shop.mtcoding.metamall.domain.OrderProduct;

public interface OrderProductRepository extends JpaRepository<OrderProduct, Long> {
}
