package shop.minostreet.shoppingmall.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import shop.minostreet.shoppingmall.domain.OrderProduct;

import java.util.List;

public interface OrderProductRepository extends JpaRepository<OrderProduct, Long> {


    List<OrderProduct> findByOrderSheetId(Long id);
}
