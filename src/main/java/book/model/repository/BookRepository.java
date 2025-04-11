package book.model.repository;

import book.model.entity.BookEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface BookRepository extends JpaRepository<BookEntity, Integer> {
    // 비밀번호 찾기
    @Query( value = "SELECT bpwd FROM book WHERE bno = :bno", nativeQuery = true )
    String findPwd( int bno );

}
