package day06과제.model.repository;

import day06과제.model.entity.BookEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface BookRepository extends JpaRepository<BookEntity, Integer> {
    // 비밀번호 찾기
    @Query( value = "SELECT bpwd FROM book WHERE bno = :bno", nativeQuery = true )
    String findPwd( int bno );

}
