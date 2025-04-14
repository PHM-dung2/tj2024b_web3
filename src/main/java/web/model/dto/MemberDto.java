package web.model.dto;

import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import web.model.entity.MemberEntity;

import java.time.LocalDateTime;

@Builder
@Data @NoArgsConstructor @AllArgsConstructor
public class MemberDto {
    @Id
    private String email;
    private String pwd;
    private String name;
    private LocalDateTime createAt;
    private LocalDateTime updateAt;

    public MemberEntity toEntity(){
        return MemberEntity.builder()
                .email( this.email )
                .pwd( this.pwd )
                .name( this.name )
                .build();
    }
}
