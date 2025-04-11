package book.model.entity;


import book.model.dto.BookDto;
import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Entity @Builder @Table( name = "book" )
@Data @NoArgsConstructor @AllArgsConstructor
public class BookEntity extends BaseTime {
    @Id
    @GeneratedValue( strategy = GenerationType.IDENTITY )
    private int bno;
    private String btitle;
    private String bwriter;
    private String bcontent;
    private String bpwd;

    @ToString.Exclude
    @Builder.Default
    @OneToMany( mappedBy = "bookEntity", cascade = CascadeType.ALL, fetch = FetchType.LAZY )
    private List<ReplyEntity> replyEntityList = new ArrayList<>();

    public BookDto toDto(){
        return BookDto.builder()
                .bno( this.bno )
                .btitle( this.btitle )
                .bwriter( this.bwriter )
                .bcontent( this.bcontent )
                .bpwd( this.bpwd )
                .createAt( this.getCreateAt() )
                .updateAt( this.getUpdateAt() )
                .build();
    } // f end



}
