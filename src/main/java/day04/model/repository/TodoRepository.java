package day04.model.repository;

import day04.model.entity.TodoEntity;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@Transactional
public interface TodoRepository extends JpaRepository< TodoEntity, Integer > {

    // JPA Repository
    // 1. .save() 2. findById() 3. findAll() 4. deleteById() 등등 미리 만들어진 CRUD 메소드 제공

    // 2. 쿼리메소드(JPQL 이용한 메소드 이름 기반으로 자동 생성) // ================================
    // Spring JAP에서 SQL 문장을 직접 작성하지 않고 메소드 일므으로 쿼리 생성한다. < 카멜표기법 >
    // *** 메소드 명명 규칙 ***
        // 1. fingBy필드명 : 조회
        // 2. fingBy필드명Containing :
    List<TodoEntity> findByTitle( String title );
        // List<TodoEntity> : 조회 결과를 List 타입
        // findByTitle : title 필드를 select(조회)한다. * 주의할점 : < 카멜표기법 >과 메소드 명명 규칙
        // ( String title ) : 조회 조건
            // mybatis : select * from todo where title = ${ title }

        // 2. findBy필드명Containing : 포함된(like) 조회
    List<TodoEntity> findByTitleContaining( String keyword ); // 변수명 노란줄 오타
            // mybatis : select * from todo where title like %${title}%

        // 3. findBy필드명And필드명 : 두 조건을 조회, And, Or
    List<TodoEntity> findByTitleAndContent( String title, String content );

        // 4. existsBy필드명 : 조건에 맞는 엔티티 여부(true/false) 조회, 반환타입 : boolean
    boolean existsByTitle( String title );

        // 5. coutnBy필드명 : 조건에 맞는 엔티티 개수 조회, 반환타입 : long
    long countByTitle( String title );

        // 6. deleteBy필드명 : 조건에 맞는 엔티티 삭제, 반환타입 : void
    void deleteByTitle( String title );

    // 3. 네이티브쿼리( * SQL 직접 작성 * ) //========================================================
    // Spring JPA에서 SQL 문법을 직접 작성하여 실행한다.
    // *** @Query( value="SQL문", nativeQuery = true ) ***
        // -> Query는 select 위한 어노테이션이므로 insert, update, delete할 경우에는 @Modifying 같이 사용한다.
    // SQL 문의 매개변수를 작성시에는 :매개변수명 작성하여 매개변수 대입할 수 있다.
        // 1.
    @Query( value="select * from todo where title = :title", nativeQuery = true )
    List<TodoEntity> findByTitleNative( String title );
        // List<TodoEntity> : 조회 결과를  List 타입
        // fingByTitleNative : 규칙 없으므로 아무거나
        // ( String title ) : 조회 조건으로 SQL 문법의 매개변수

        // 2.
    @Query( value = "select * from todo where title like %:keyword% ", nativeQuery = true )
    List<TodoEntity> findByTitleNativeSearch( String keyword );

        // 3. delete
    @Modifying // 네이티브쿼리 (@Query)사용시 selete만 지원하므로 insert/update/delete 쿼리는 @Modifying 같이 사용된다.
    @Query( value = "delete from todo where title = :title", nativeQuery = true )
    int deleteByNative( String title );

        // 4. update
    @Modifying
    @Query( value = "update todo set title = :title where id = :id", nativeQuery = true )
    int updateTitleByNative( String title );;

}
