package web.model.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import web.model.entity.ProductEntity;

import java.util.List;

@Repository
public interface ProductRepository extends JpaRepository< ProductEntity, Integer > {

    // 방법1. JPA 기본적인 함수 제공
    // save, findAll, findById, delete 등

    // 방법2. 쿼리메소드, 규칙 : 명명규칙( 카멜 )
        // findBy ~ : select
            // findByCno[x] : ProductEntity에는 cno가 존재하지 않아서 불가능
                //
            // findByPname[o] : ProductEntity pname 존재해서 가능

    List<ProductEntity> findByCategoryEntity();
    // 방법3. 네이티브 쿼리, 규칙 : mysql 코드

    // 방법4. * JPQL, 규칙 : 자바가 만든 slq 코드

}
