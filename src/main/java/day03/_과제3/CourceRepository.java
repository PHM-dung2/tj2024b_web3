package day03._과제3;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CourceRepository extends JpaRepository< CourceEntity, Integer > {
}
