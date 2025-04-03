package day03._과제3;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.AbstractList;

@Data @Builder
@NoArgsConstructor
@AllArgsConstructor
public class StudentDto {
    private int sno;
    private String sname;
    private int cno;

    public StudentEntity toEntity(){
        return StudentEntity.builder()
               .sno( this.sno )
               .sname( this.sname )
                .cno( this.cno )
               .build();
    }
}
