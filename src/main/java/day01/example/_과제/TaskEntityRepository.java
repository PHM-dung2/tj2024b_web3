package day01.example._과제;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository // 스프링 컨테이너의 빈 등록
public interface TaskEntityRepository extends JpaRepository< TaskEntity, Integer > {
}
