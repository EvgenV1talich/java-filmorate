package ru.yandex.practicum.filmorate.dal.mappers;

import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.dto.FilmDTO;
import ru.yandex.practicum.filmorate.dto.MpaDTO;
import ru.yandex.practicum.filmorate.dto.UserDTO;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.MPA;
import ru.yandex.practicum.filmorate.model.User;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Component
public class FilmMapper {

    private FilmMapper() {
    }

    public FilmDTO filmToDTO(Film film) {
        if (film == null) {
            throw new IllegalArgumentException("film cannot be null");
        }
        return FilmDTO.builder()
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

    public Film dtoToFilm(FilmDTO filmDTO) {
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

    public List<FilmDTO> listFilmsToListDto(Collection<Film> films) {
        List<FilmDTO> filmDTOS = new ArrayList<>();
        for (Film film : films) {
            filmDTOS.add(filmToDTO(film));
        }
        return filmDTOS;
    }

}