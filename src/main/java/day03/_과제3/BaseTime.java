package day03._과제3;

import jakarta.persistence.EntityListeners;
import jakarta.persistence.MappedSuperclass;
import lombok.Data;
import lombok.Getter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

@MappedSuperclass
@EntityListeners( AuditingEntityListener.class )
@Getter
public class BaseTime {

    @CreatedDate
    private LocalDateTime createdate;

    @LastModifiedDate
    private LocalDateTime updatedate;

}
