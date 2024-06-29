package ru.yandex.practicum.filmorate.service.film;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import ru.yandex.practicum.filmorate.dal.filmdao.FilmStorage;
import ru.yandex.practicum.filmorate.dal.genredao.GenreDbStorage;
import ru.yandex.practicum.filmorate.dal.mappers.FilmMapper;
import ru.yandex.practicum.filmorate.dal.mpadao.MpaDbStorage;
import ru.yandex.practicum.filmorate.dto.FilmDTO;
import ru.yandex.practicum.filmorate.exceptions.FilmValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.service.UserService;
import ru.yandex.practicum.filmorate.service.likes.LikesService;
import ru.yandex.practicum.filmorate.validators.FilmValidator;

import java.time.Instant;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;


@Slf4j
@Service
@RequiredArgsConstructor
public class FilmServiceImpl implements FilmService {
    private final FilmStorage filmStorage;
    private final UserService userService;
    private final LikesService likeService;
    private final MpaDbStorage mpaStorage;
    private final GenreDbStorage genreDbStorage;
    private final FilmMapper mapper;

    @Override
    public FilmDTO createFilm(FilmDTO filmDTO) {
 /*       Film film = mapper.dtoToFilm(filmDTO);
        if (FilmValidator.validate(film)) {
            log.debug("Film {} saved.", filmDTO.getId());
            return mapper.filmToDTO(filmStorage.createFilm(film));
        }
        throw new FilmValidationException("Invalid film" + filmDTO);*/
        if (FilmValidator.validate(mapper.dtoToFilm(filmDTO))
                && FilmValidator.filmMpaValidation(filmDTO.getMpa(), mpaStorage.readAll())
                && FilmValidator.filmGenreValidation(filmDTO.getGenres(), (ArrayList<Genre>) genreDbStorage.getAll())) {
        Film film = filmStorage.createFilm(mapper.dtoToFilm(filmDTO));

            log.debug("Film {} saved.", filmDTO.getId());
            return mapper.filmToDTO(film);
        } throw  new FilmValidationException("Invalid film!");
    }

    @Override
    public FilmDTO updateFilm(FilmDTO filmDTO) {
        Film film = mapper.dtoToFilm(filmDTO);
        if (FilmValidator.validate(film)) {
            log.debug("Film {} updated.", filmDTO.getId());
            return mapper.filmToDTO(filmStorage.updateFilm(film));
        }
        throw new FilmValidationException("Invalid film " + filmDTO);
    }

    @Override
    public List<FilmDTO> readAllFilms() {
        log.debug("Get films list");
        return mapper.listFilmsToListDto(filmStorage.getFilms());
    }

    @Override
    public FilmDTO getFilm(Integer id) {
        log.debug("Get film {}.", id);
        return mapper.filmToDTO(filmStorage.getFilm(id));
    }

    @Override
    public void deleteLikeById(Integer idFilm, Long idUser) {
        filmStorage.getFilm(idFilm);
        userService.getUserById(idUser); // для валидации
        likeService.deleteLike(idFilm, idUser);
        log.debug("User {} deleted like from film {}", idUser, idFilm);
    }

    @Override
    public void userLike(Integer idFilm, Long idUser) {
        filmStorage.getFilm(idFilm);
        userService.getUserById(idUser); // для валидации
        likeService.addLike(idFilm, idUser);
        log.debug("Add like user {} to film {}", idUser, idFilm);
    }

    @Override
    public List<FilmDTO> getTopFilms(Long count) {
        log.debug("Get top {} films by likes", count);
        return mapper.listFilmsToListDto(filmStorage.getTopFilms(count));
    }

    @Override
    public void deleteFilm(Integer id) {
        filmStorage.deleteFilm(id);
    }



}
