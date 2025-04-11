package day06과제.model.repository;

import day06과제.model.entity.ReplyEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface ReplyRepository extends JpaRepository<ReplyEntity, Integer> {
    // 비밀번호 찾기
    @Query( value = "SELECT rpwd FROM bookreply WHERE rno = :rno", nativeQuery = true )
    String findPwd( int rno );
}
