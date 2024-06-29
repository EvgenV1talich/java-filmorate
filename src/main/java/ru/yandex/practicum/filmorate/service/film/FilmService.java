package ru.yandex.practicum.filmorate.service.film;

import ru.yandex.practicum.filmorate.dto.FilmDTO;

import java.util.List;

public interface FilmService {

    FilmDTO createFilm(FilmDTO film);

    FilmDTO updateFilm(FilmDTO film);

    List<FilmDTO> readAllFilms();

    FilmDTO getFilm(Integer id);

    void deleteLikeById(Integer idFilm, Long idUser);

    void userLike(Integer idFilm, Long idUser);

    List<FilmDTO> getTopFilms(Long count);

    void deleteFilm(Integer id);


}