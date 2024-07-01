package ru.yandex.practicum.filmorate.dto;

import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.Mpa;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.PositiveOrZero;
import javax.validation.constraints.Size;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Set;

@Data
@Builder
@EqualsAndHashCode
public class FilmDto {

    private Integer id;

    @NotBlank
    @NotNull
    private String name;

    @Size(min = 0, max = 199)
    private String description;

    private LocalDate releaseDate;

    @PositiveOrZero
    private Integer duration;
    private Set<Long> likes;
    private ArrayList<Genre> genres;
    @NotNull
    private Mpa mpa;
}