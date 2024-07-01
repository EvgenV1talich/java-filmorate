package ru.yandex.practicum.filmorate.service.film;

import ru.yandex.practicum.filmorate.dto.FilmDto;

import java.util.List;

public interface FilmService {

    FilmDto createFilm(FilmDto film);

    FilmDto updateFilm(FilmDto film);

    List<FilmDto> readAllFilms();

    FilmDto getFilm(Integer id);

    void deleteLikeById(Integer idFilm, Long idUser);

    void userLike(Integer idFilm, Long idUser);

    List<FilmDto> getTopFilms(Long count);

    void deleteFilm(Integer id);


}