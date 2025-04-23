package web.model.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity @Builder @Table( name = "reply" )
@Data @NoArgsConstructor @AllArgsConstructor
public class ReplyEntity extends BaseTime{
    @Id
    @GeneratedValue( strategy = GenerationType.IDENTITY )
    private long rno; // 댓글 식별전호

    @Column( nullable = false )
    private String rcontent; // 댓글 내용

    // * 단방향
    @ManyToOne
    @JoinColumn( name = "mno" )
    private MemberEntity memberEntity; // 작성 전

    @ManyToOne
    @JoinColumn( name = "pno" )
    private ProductEntity productEntity;



}
