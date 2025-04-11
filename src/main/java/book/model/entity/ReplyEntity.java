package book.model.entity;

import book.model.dto.ReplyDto;
import jakarta.persistence.*;
import lombok.*;

@Entity @Builder @Table( name = "bookreply" )
@Data @NoArgsConstructor @AllArgsConstructor
public class ReplyEntity extends BaseTime {
    @Id
    @GeneratedValue( strategy = GenerationType.IDENTITY )
    private int rno;
    private String rname;
    private String rcontent;
    private String rpwd;

    @ManyToOne
    private BookEntity bookEntity;

    public ReplyDto toDto(){
        return ReplyDto.builder()
                .rno( this.rno )
                .rname( this.rname )
                .rcontent( this.rcontent )
                .rpwd( this.rpwd )
                .bno( this.bookEntity.getBno() )
                .createAt( this.getCreateAt() )
                .updateAt( this.getUpdateAt() )
                .build();
    }
}
