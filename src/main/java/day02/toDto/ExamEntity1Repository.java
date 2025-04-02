package day02.toDto;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ExamEntity1Repository extends JpaRepository< ExamEntity1, Integer > {
}
