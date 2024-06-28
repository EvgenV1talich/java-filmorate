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
import ru.yandex.practicum.filmorate.model.MPA;
import ru.yandex.practicum.filmorate.service.mpa.MpaService;


import java.util.List;

@RestController
@Slf4j
@RequestMapping("/mpa")
@RequiredArgsConstructor
public class MpaController {

    private final MpaService mpaService;

    @GetMapping
    public List<MPA> readAll() {
        log.info("Получен GET запрос по эндпоинту '/mpa' на получение всех mpa");
        return mpaService.readAll();
    }

    @GetMapping("/{id}")
    public MPA readById(@PathVariable Integer id) {
        log.info("Получен GET запрос по эндпоинту '/mpa/{}' на получение mpa по id", id);
        return mpaService.readById(id);
    }

}