package ru.yandex.practicum.filmorate.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.yandex.practicum.filmorate.dto.MpaDTO;
import ru.yandex.practicum.filmorate.service.MpaService;

import java.util.List;

@RestController
@Slf4j
@RequestMapping("/mpa")
@RequiredArgsConstructor
public class MpaController {

    final MpaService mpaService;

    @GetMapping
    public ResponseEntity<List<MpaDTO>> readAll() {
        log.info("GET '/mpa' all mpa");
        return new ResponseEntity<>(mpaService.readAll(), HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<MpaDTO> readById(@PathVariable Integer id) {
        log.info("GET'/mpa/{}' mpa by id", id);
        return new ResponseEntity<>(mpaService.readById(id), HttpStatus.OK);
    }

}