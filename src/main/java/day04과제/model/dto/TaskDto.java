package day04과제.model.dto;

import day04과제.model.entity.TaskEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data @Builder
@NoArgsConstructor
@AllArgsConstructor
public class TaskDto {
    private int id;
    private String name;
    private String description;
    private int quantity;
    private LocalDateTime createdate;
    private LocalDateTime updatedate;

    public TaskEntity toEntity(){
        return TaskEntity.builder()
                .id( this.id )
                .name( this.name )
                .description( this.description )
                .quantity( this.quantity )
                .build();
    } // f end

}
