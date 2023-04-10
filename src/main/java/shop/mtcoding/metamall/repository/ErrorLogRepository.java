package shop.mtcoding.metamall.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import shop.mtcoding.metamall.domain.ErrorLog;

public interface ErrorLogRepository extends JpaRepository<ErrorLog, Long> {
}
