package shop.minostreet.shoppingmall.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import shop.minostreet.shoppingmall.domain.OrderProduct;

public interface OrderProductRepository extends JpaRepository<OrderProduct, Long> {
}
