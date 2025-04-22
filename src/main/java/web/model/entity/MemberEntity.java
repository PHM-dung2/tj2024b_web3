package web.model.entity;

import jakarta.persistence.*;
import lombok.*;
import web.model.dto.MemberDto;

import java.util.ArrayList;
import java.util.List;

@Builder @Entity @Table( name = "member" )
@Data @NoArgsConstructor @AllArgsConstructor
public class MemberEntity extends BaseTime{
    @Id
    @GeneratedValue( strategy = GenerationType.IDENTITY )
    private int mno;
    private String memail;
    private String mpwd;
    private String mname;

    // * 양방향 : FK 엔티티를 여러개 가지므로 List 타입으로 만든다.
    @OneToMany( mappedBy = "memberEntity", cascade = CascadeType.ALL, fetch = FetchType.LAZY )
    @ToString.Exclude // 양방향 설계시 toString 롬복의 순환참조 방지
    @Builder.Default // 엔티티 객체 생성시 빌드 메소드 사용하면 기본밗
    private List<ProductEntity> productEntityList = new ArrayList<>();

//    @OneToMany( mappedBy = "FK엔티티 자바 필드명" )
    @OneToMany( mappedBy = "memberEntity", cascade = CascadeType.ALL, fetch = FetchType.LAZY )
    @ToString.Exclude
    @Builder.Default
    private List<ReplyEntity> replyEntityList = new ArrayList<>();

    public MemberDto toDto(){
        return MemberDto.builder()
                .mno( this.mno )
                .memail( this.memail )
                .mname( this.mname )
                .createAt( this.getCreateAt() )
                .updateAt( this.getUpdateAt() )
                .build();
    }

}
