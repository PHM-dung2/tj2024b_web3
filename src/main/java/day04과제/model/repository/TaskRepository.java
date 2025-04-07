package day04과제.model.repository;

import day04과제.model.entity.TaskEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface TaskRepository extends JpaRepository< TaskEntity, Integer > {

    // 1. 비품 등록
    @Modifying
    @Query( value = "INSERT INTO product( name, description, quantity, createdate, updatedate ) " +
            "VALUES( :name, :description, :quantity, :createdate, :updatedate )", nativeQuery = true )
    int postByNative( @Param("name") String name,
                      @Param("description") String description,
                      @Param("quantity") int quantity,
                      @Param("createdate") LocalDateTime createdate,
                      @Param("updatedate") LocalDateTime updatedate );

    // 2. 전체 비품 조회
    @Query( value = "SELECT * FROM product", nativeQuery = true )
    List<TaskEntity> findAllByNative();

    // 3. 개별 비품 조회
    @Query( value = "SELECT * FROM product WHERE id = :id", nativeQuery = true )
    Optional<TaskEntity> findByIdByNative(int id );

    // 4. 비품 수정
    @Modifying
    @Query( value = "UPDATE product SET name = :name, description = :description, " +
            "quantity = :quantity WHERE id = :id", nativeQuery = true )
    int updateByNative( @Param("id") int id,
                        @Param("name") String name,
                        @Param("description") String description,
                        @Param("quantity") int quantity );

    // 5. 비품 삭제
    @Modifying
    @Query( value = "DELETE FROM product WHERE id = :id", nativeQuery = true )
    int deleteByNative( @Param("id") int id );


}
