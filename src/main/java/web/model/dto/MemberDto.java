package web.model.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import web.model.entity.MemberEntity;

import java.time.LocalDateTime;

@Builder
@Data @NoArgsConstructor @AllArgsConstructor
public class MemberDto {
    private int mno;
    private String memail;
    private String mpwd;
    private String mname;
    private LocalDateTime createAt;
    private LocalDateTime updateAt;

    public MemberEntity toEntity(){
        return MemberEntity.builder()
                .memail( this.memail )
                .mpwd( this.mpwd )
                .mname( this.mname )
                .build();
    }
}
