package web.model.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import web.model.dto.MemberDto;

@Builder @Entity @Table( name = "member" )
@Data @NoArgsConstructor @AllArgsConstructor
public class MemberEntity extends BaseTime{
    @Id
    @GeneratedValue( strategy = GenerationType.IDENTITY )
    private int mno;
    private String memail;
    private String mpwd;
    private String mname;

    public MemberDto toEntity(){
        return MemberDto.builder()
                .mno( this.mno )
                .memail( this.memail )
                .mpwd( this.mpwd )
                .mname( this.mname )
                .createAt( this.getCreateAt() )
                .updateAt( this.getUpdateAt() )
                .build();
    }

}
