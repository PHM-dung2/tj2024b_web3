package web.model.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import web.model.entity.CategoryEntity;

@Builder
@Data @NoArgsConstructor @AllArgsConstructor
public class CategoryDto {
    private long cno;
    private String cname;

    // * toDto
    public static CategoryDto toDto( CategoryEntity categoryEntity ){
        return CategoryDto.builder()
                .cno( categoryEntity.getCno() )
                .cname( categoryEntity.getCname() )
                .build();
    }

}
