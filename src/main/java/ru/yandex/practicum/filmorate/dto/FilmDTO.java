package ru.yandex.practicum.filmorate.dto;

import lombok.Builder;
import lombok.Data;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.MPA;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.PositiveOrZero;
import javax.validation.constraints.Size;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Set;

@Data
@Builder
public class FilmDTO {

    private Integer id;

    @NotBlank
    private String name;

    @Size(min = 0, max = 199)
    private String description;

    private LocalDate releaseDate;

    @PositiveOrZero
    private Integer duration;
    private Set<Long> likes;
    private ArrayList<Genre> genres;
    private MPA mpa;
}