package ru.yandex.practicum.filmorate.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.yandex.practicum.filmorate.dto.GenreDTO;

import java.util.List;

@RestController
@Slf4j
@RequestMapping("/genres")
@RequiredArgsConstructor
public class GenreController {

    private final GenreService genreService;


    @GetMapping
    public ResponseEntity<List<GenreDTO>> readAll() {
        log.info("GET'/genres' all genres");
        return new ResponseEntity<>(genreService.readAll(), HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<GenreDTO> readById(@PathVariable Integer id) {
        log.info("GET'/genres/{}' genre by id", id);
        return new ResponseEntity<>(genreService.readById(id), HttpStatus.OK);
    }

}