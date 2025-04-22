package web.model.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Entity @Builder @Table( name = "category" )
@Data @NoArgsConstructor @AllArgsConstructor
public class CategoryEntity{
    @Id // premary key
    @GeneratedValue( strategy = GenerationType.IDENTITY ) // auto_increment
    private long cno; // 카테고리 식별번호

    @Column( nullable = false, length = 100 ) // not null, 길이 : 100
    private String cname; // 카테고리 이름

    // * 양방향
    @OneToMany( mappedBy = "categoryEntity", cascade = CascadeType.ALL, fetch = FetchType.LAZY )
    @ToString.Exclude
    @Builder.Default
    private List<ProductEntity> productEntityList = new ArrayList<>();


}
