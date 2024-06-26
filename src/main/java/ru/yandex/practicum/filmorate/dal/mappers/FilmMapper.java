package ru.yandex.practicum.filmorate.dal.mappers;

import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.dto.FilmDto;
import ru.yandex.practicum.filmorate.model.Film;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Component
public class FilmMapper {

    private FilmMapper() {
    }

    public FilmDto filmToDTO(Film film) {
        if (film == null) {
            throw new IllegalArgumentException("film cannot be null");
        }
        return FilmDto.builder()
                .id(film.getId())
                .name(film.getName())
                .description(film.getDescription())
                .releaseDate(film.getReleaseDate())
                .duration(film.getDuration())
                .likes(film.getLikesFromUsers())
                .genres(film.getGenre())
                .mpa(film.getMpa())
                .build();
    }

    public Film dtoToFilm(FilmDto filmDTO) {
        if (filmDTO == null) {
            throw new IllegalArgumentException("filmDTO cannot be null");
        }
        Film film = new Film();
        film.setId(filmDTO.getId());
        film.setName(filmDTO.getName());
        film.setDescription(filmDTO.getDescription());
        film.setReleaseDate(filmDTO.getReleaseDate());
        film.setDuration(filmDTO.getDuration());
        film.setLikesFromUsers(filmDTO.getLikes());
        film.setGenre(filmDTO.getGenres());
        film.setMpa(filmDTO.getMpa());
        return film;
    }

    public List<FilmDto> listFilmsToListDto(Collection<Film> films) {
        List<FilmDto> filmDTOS = new ArrayList<>();
        for (Film film : films) {
            filmDTOS.add(filmToDTO(film));
        }
        return filmDTOS;
    }

}