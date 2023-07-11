package ru.practicum;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;


@Entity
@Setter
@Getter
@AllArgsConstructor
@Table(name = "Statistics")
@NoArgsConstructor
@ToString
public class StatData {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "statistical_data_id")
    private Long id;

    private  String app;

    private  String uri;

    private  String ip;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime timestamp;

}
