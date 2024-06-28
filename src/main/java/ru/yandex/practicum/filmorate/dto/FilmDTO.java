package ru.yandex.practicum.filmorate.dto;

import lombok.Builder;
import lombok.Data;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.MPA;

import javax.validation.constraints.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Set;

@Data
@Builder
public class FilmDTO {

    private Integer id;
    @NotNull
    private String name;
    @Size(min = 0, max = 200)
    private String description;
    @Past
    private LocalDate releaseDate;
    private Integer duration;
    private Set<Long> likes;
    private ArrayList<Genre> genre;
    private MPA mpa;

}
