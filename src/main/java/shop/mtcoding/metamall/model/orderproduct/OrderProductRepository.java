package shop.mtcoding.metamall.model.orderproduct;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import shop.mtcoding.metamall.model.ordersheet.OrderSheet;

import java.util.List;

public interface OrderProductRepository extends JpaRepository<OrderProduct, Long> {

}
