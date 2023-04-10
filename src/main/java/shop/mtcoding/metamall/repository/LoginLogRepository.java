package shop.mtcoding.metamall.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import shop.mtcoding.metamall.domain.LoginLog;

public interface LoginLogRepository extends JpaRepository<LoginLog, Long> {
}
