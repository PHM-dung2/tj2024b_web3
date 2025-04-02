package day02._BaseTime;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.awt.print.Book;

@Repository
public interface BookEntityRepository extends JpaRepository<BookEntity, Integer>{
}
