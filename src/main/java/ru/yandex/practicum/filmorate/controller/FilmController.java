package ru.yandex.practicum.filmorate.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ru.yandex.practicum.filmorate.dto.FilmDTO;
import ru.yandex.practicum.filmorate.service.film.FilmServiceImpl;

import javax.validation.Valid;
import java.util.List;

@RestController
@Slf4j
@RequestMapping("/films")
@RequiredArgsConstructor
public class FilmController {
    private final FilmServiceImpl service;

    @PostMapping
    public ResponseEntity<FilmDTO> createFilm(@RequestBody @Valid FilmDTO newFilm) {
        log.info("POST  '/films' createFilm {}.", newFilm.getName());
        return new ResponseEntity<>(service.createFilm(newFilm), HttpStatus.CREATED);
    }

    @PutMapping
    public ResponseEntity<FilmDTO> updateFilm(@RequestBody @Valid FilmDTO newFilm) {
        log.info("PUT '/films' film ID {}.", newFilm.getId());
        return new ResponseEntity<>(service.updateFilm(newFilm), HttpStatus.OK);
    }

    @GetMapping
    public ResponseEntity<List<FilmDTO>> readAllFilmsList() {
        log.info("GET '/films' all films");
        return new ResponseEntity<>(service.readAllFilms(), HttpStatus.OK);
    }

    @PutMapping("{id}/like/{userId}")
    public ResponseEntity<?> addLikeFromUser(@PathVariable Integer id, @PathVariable Long userId) {
        service.userLike(id, userId);
        log.info(
                "PUT  '/films/{}/like/{}' to add like to film (id {}) from user_id {}",
                id, userId, id, userId);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @DeleteMapping("{id}/like/{userId}")
    public ResponseEntity<?> deleteLike(@PathVariable Integer id, @PathVariable Long userId) {
        service.deleteLikeById(id, userId);
        log.info(
                "DELETE '/films/{}/like/{}' to remove like from film_id {} from user_id{}",
                id, userId, id, userId);
        return new ResponseEntity<>(HttpStatus.valueOf(200));
    }

    @GetMapping("/popular")
    public ResponseEntity<List<FilmDTO>> getTopFilms(@RequestParam(required = false, defaultValue = "10") Long count) {
        log.info("GET '/films/popular' highest likes {} films",
                count);
        return new ResponseEntity<>(service.getTopFilms(count), HttpStatus.OK);
    }

    @GetMapping("{filmId}")
    public ResponseEntity<FilmDTO> getFilm(@PathVariable Integer filmId) {
        log.info("GET '/films/{}'  film_id {}.", filmId, filmId);
        return new ResponseEntity<>(service.getFilm(filmId), HttpStatus.OK);
    }

//    @GetMapping("/common")
//    public ResponseEntity<List<FilmDTO>> getCommonFilms(@RequestParam Long userId, @RequestParam Long friendId) {
//        log.info("Получен GET запрос по эндпоинту '/films/common' на получение общих Film у двух Users c ID {} и {}.",
//                userId, friendId);
//        return new ResponseEntity<>(service.getCommonFilms(userId, friendId), HttpStatus.OK);
//    }

    @DeleteMapping("/{filmId}")
    public ResponseEntity<?> deleteFilm(@PathVariable Integer filmId) {
        log.info("DELETE '/films/{}' to remove film", filmId);
        service.deleteFilm(filmId);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}