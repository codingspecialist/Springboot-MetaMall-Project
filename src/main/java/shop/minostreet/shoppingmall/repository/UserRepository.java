package shop.minostreet.shoppingmall.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;
import shop.minostreet.shoppingmall.domain.User;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {

//    @Query("select u from User u where u.username = :username")
    Optional<User> findByUsername(@Param("username") String username);
}
