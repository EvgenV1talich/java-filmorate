package ru.yandex.practicum.filmorate.dal.mappers;

import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.dto.FilmDTO;
import ru.yandex.practicum.filmorate.model.MPA;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

@Component
public class FilmRowMapper implements RowMapper<Film> {
    @Override
    public Film mapRow(ResultSet rs, int rowNum) throws SQLException {
        Film film = new Film();
        film.setId(rs.getInt("ID"));
        film.setName(rs.getString("NAME"));
        film.setDescription(rs.getString("DESCRIPTION"));
        film.setReleaseDate(rs.getDate("RELEASE_DATE").toLocalDate());
        film.setDuration(rs.getInt("DURATION"));
        film.setMpa((MPA) rs.getObject("MPA"));
        return film;
    }

    public Film DTOToFilm(FilmDTO DTO) {
        if (DTO == null) {
            throw new IllegalArgumentException("filmDTO cannot be null");
        }
        Film film = new Film();
        film.setId(DTO.getId());
        film.setName(DTO.getName());
        film.setDescription(DTO.getDescription());
        film.setReleaseDate(DTO.getReleaseDate());
        film.setDuration(DTO.getDuration());
        film.setLikesFromUsers(DTO.getLikes());
        film.setGenre(DTO.getGenre());
        return film;
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
                .genre(film.getGenre())
                .build();
    }
    public List<FilmDTO> filmsToDTO(List<Film> filmList) {
        List<FilmDTO> DTOlist = new ArrayList<>();
        for (Film film : filmList) {
            DTOlist.add(filmToDTO(film));
        }
        return DTOlist;
    }
}
