package web.model.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity @Builder @Table( name = "image" )
@Data @NoArgsConstructor @AllArgsConstructor
public class ImgEntity {
    @Id
    @GeneratedValue( strategy = GenerationType.IDENTITY )
    private long ino; // 이미지 식별번호

    @Column( nullable = false )
    private String lname; // 이미지명

    // * 단방향
    @ManyToOne
    @JoinColumn( name = "pno" )
    private ProductEntity productEntity;

}
