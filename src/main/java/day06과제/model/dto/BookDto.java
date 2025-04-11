package day06과제.model.dto;

import day04과제.model.entity.BaseTime;
import day06과제.model.entity.BookEntity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Builder
@Data @NoArgsConstructor @AllArgsConstructor
public class BookDto {
    private int bno;
    private String btitle;
    private String bwriter;
    private String bcontent;
    private String bpwd;
    private LocalDateTime createAt;
    private LocalDateTime updateAt;

    public BookEntity toEntity(){
        return  BookEntity.builder()
                .bno( this.bno )
                .btitle( this.btitle )
                .bwriter( this.bwriter )
                .bcontent( this.bcontent )
                .bpwd( this.bpwd )
                .build();
    }
}
