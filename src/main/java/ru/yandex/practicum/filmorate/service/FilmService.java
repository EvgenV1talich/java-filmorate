package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.dal.filmdao.FilmDbStorage;
import ru.yandex.practicum.filmorate.dal.mappers.FilmRowMapper;
import ru.yandex.practicum.filmorate.dal.userdao.UserDbStorage;
import ru.yandex.practicum.filmorate.dto.FilmDTO;
import ru.yandex.practicum.filmorate.model.Film;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
public class FilmService {

    private final FilmDbStorage filmStorage;
    private final UserDbStorage userStorage;
    private final FilmRowMapper mapper;


    public void addLikeToFilm(Long userId, Integer filmId) {
        Film newFilm = filmStorage.getFilm(filmId);
        newFilm.addLike(userId);
        filmStorage.updateFilm(newFilm);
    }

    public void removeLikeToFilm(Integer filmId, Long userId) {
        Film film = filmStorage.getFilm(filmId);
        film.removeLike(userId);
        filmStorage.updateFilm(film);
    }

    public List<FilmDTO> getMostPopularFilms(Integer count) {
        Collection<Film> films = filmStorage.getFilms().values();
        //TODO переписать на DTO
        films.stream()
                .sorted((a, b) -> b.getLikes() - a.getLikes())
                .limit(count)
                .collect(Collectors.toList());
        return null;
    }
    public Collection<FilmDTO> getFilms() {
        return mapper.filmsToDTO(((List<Film>) filmStorage.getFilms().values()));
    }
    public FilmDTO createFilm(FilmDTO film) {
        return mapper.filmToDTO(filmStorage.createFilm(mapper.DTOToFilm(film)));
    }
    public boolean containsFilm(Integer id) {
        return (filmStorage.contains(id));
    }
    public FilmDTO updateFilm(Film film) {
        filmStorage.updateFilm(film);
        return mapper.filmToDTO(film);
    }
    public Film getFilm(Integer id) {
        return filmStorage.getFilm(id);
    }
    /*public Integer getFilmsCount() {
        return filmStorage.getCount();
    }*/

}
