package day06과제.model.dto;

import day06과제.model.entity.ReplyEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Builder
@Data @NoArgsConstructor @AllArgsConstructor
public class ReplyDto {
    private int rno;
    private String rname;
    private String rcontent;
    private String rpwd;
    private int bno;
    private LocalDateTime createAt;
    private LocalDateTime updateAt;

    public ReplyEntity toEntity(){
        return ReplyEntity.builder()
                .rno( this.rno )
                .rname( this.rname )
                .rcontent( this.rcontent )
                .rpwd( this.rpwd )
                .build();
    } // f end
}
