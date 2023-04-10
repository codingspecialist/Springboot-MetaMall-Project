package shop.mtcoding.metamall.model.ordersheet;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface OrderSheetRepository extends JpaRepository<OrderSheet, Long> {

    @Query("select o from OrderSheet o where o.user.username = :username")
    List<OrderSheet> findOrderSheetsByUserName(@Param("username") String username);
}
