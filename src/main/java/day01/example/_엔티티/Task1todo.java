package day01.example._엔티티;

import jakarta.persistence.*;
import org.hibernate.annotations.ColumnDefault;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
public class Task1todo {
    @Id
    @GeneratedValue( strategy = GenerationType.IDENTITY ) // auto_increment
    private int id;

    @Column( nullable = false, length = 100 ) // not null, varchar(100)
    private String title;

    @Column( nullable = false )
    private boolean state; // 초기값

    @Column( nullable = false )
    private LocalDate createat;

    @Column( nullable = false )
    private LocalDateTime updateat;

}
