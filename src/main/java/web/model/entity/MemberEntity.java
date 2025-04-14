package web.model.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import web.model.dto.MemberDto;

@Builder @Entity @Table( name = "member" )
@Data @NoArgsConstructor @AllArgsConstructor
public class MemberEntity extends BaseTime{
    @Id
    private String email;
    private String pwd;
    private String name;

    public MemberDto toEntity(){
        return MemberDto.builder()
                .email( this.email )
                .pwd( this.pwd )
                .name( this.name )
                .createAt( this.getCreateAt() )
                .updateAt( this.getUpdateAt() )
                .build();
    }

}
