package day03._과제3;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data @Builder
@NoArgsConstructor
@AllArgsConstructor
public class CourceDto{
    private int cno;
    private String cname;

    public CourceEntity toEntity(){
        return CourceEntity.builder()
                .cno( this.cno )
                .cname( this.cname )
                .build();
    }
}
