package shop.mtcoding.metamall.model.product;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import shop.mtcoding.metamall.model.user.User;

import java.util.List;
import java.util.Optional;

public interface ProductRepository extends JpaRepository<Product, Long> {

    @Query("select p from Product p join fetch p.seller s")
    List<Product> findAll();
    @Query("select p from Product p join fetch p.seller s where p.id=:id")
    Optional<Product> findById(@Param("id") Long id);
    @Query("select p from Product p join fetch p.seller s where p.id=:id and s=:user")
    Optional<Product> findByIdAndUser(@Param("id") Long id, @Param("user") User user);
}
