package shop.mtcoding.metamall.model.orderproduct;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import shop.mtcoding.metamall.model.ordersheet.OrderSheet;

public interface OrderProductRepository extends JpaRepository<OrderProduct, Long> {
    @Query("select op from OrderProduct op join fetch op.orderSheet os where os=:os")
    OrderProduct findByOrderSheet(@Param("os") OrderSheet os);
}
