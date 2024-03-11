package ru.yandex.practicum.filmorate.model;

import lombok.*;

import java.time.Duration;
import java.time.LocalDate;

/**
 * Film.
 */
@Data
@AllArgsConstructor
public class Film {
    @NonNull
    private Integer id;
    private String name;
    private String description;
    private LocalDate releaseDate;
    private Duration duration;

}
