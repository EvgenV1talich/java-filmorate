package ru.yandex.practicum.filmorate.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.dto.FilmDTO;
import ru.yandex.practicum.filmorate.exceptions.FilmNotFoundException;
import ru.yandex.practicum.filmorate.exceptions.FilmValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.service.FilmService;
import ru.yandex.practicum.filmorate.service.UserService;
import ru.yandex.practicum.filmorate.validators.FilmValidator;

import javax.validation.Valid;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@RestController
@Slf4j
public class FilmController {

    private final FilmService filmService;
    private final UserService userService;

    @GetMapping("/films")
    public Collection<FilmDTO> getFilms() {
        return filmService.getFilms();
    }

    @PostMapping("/films")
    public ResponseEntity<FilmDTO> postFilm(@RequestBody @Valid FilmDTO film) {
        System.out.println("--------------Пытаемся создать фильм-----------");
        if(if exist){

        } else{
            return new ResponseEntity<>(filmService.createFilm(film), HttpStatus.BAD_REQUEST);
        }
    }

    @PutMapping("/films")
    public ResponseEntity<FilmDTO> putFilm(@RequestBody Film film) {
        if (!FilmValidator.validate(film)) {
            log.error("Ошибка валидации фильма при запросе PUT /users");
            throw new FilmValidationException("Ошибка валидации фильма. Проверьте данные.");
        }
        if (!filmService.containsFilm(film.getId())) {
            throw new FilmNotFoundException("Такой фильм не найден!");
        } else {
            return new ResponseEntity<>(filmService.updateFilm(film), HttpStatus.OK);
        }
    }

    @PutMapping("films/{id}/like/{userId}")
    public void addLikeToFilm(@PathVariable Integer id, @PathVariable Long userId) {
        filmService.addLikeToFilm(userId, id);
    }

    @DeleteMapping("films/{id}/like/{userId}")
    public void removeLikeFromFilm(@PathVariable Integer id, @PathVariable Long userId) {
        if (!filmService.containsFilm(id)) {
            throw new FilmNotFoundException("Фильм с таким ID не найден!");
        } /*else if (!userService.containsUser(userId)) {
            throw new UserNotFoundException("Пользователь с таким ID не найден!");
        }*/ else {
            filmService.removeLikeToFilm(id, userId);
        }
    }

    @GetMapping("/films/popular")
    public List<FilmDTO> getPopularFilmsList(@RequestParam(name = "count") Optional<Integer> count) {
        /*if (count.isEmpty()) {
            return filmService.getMostPopularFilms(10);
        } else if (count.get() > filmService.getFilmsCount()) {
            return filmService.getMostPopularFilms(filmService.getFilmsCount());
        } else {
            return filmService.getMostPopularFilms(count.get());
        }*/
        return null;
    }

}
