package ru.yandex.practicum.filmorate.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.yandex.practicum.filmorate.dal.mappers.MpaMapper;
import ru.yandex.practicum.filmorate.dto.MpaDto;
import ru.yandex.practicum.filmorate.service.mpa.MpaService;

import java.util.List;

@RestController
@Slf4j
@RequestMapping("/mpa")
@RequiredArgsConstructor
public class MpaController {

    private final MpaService mpaService;
    private final MpaMapper mapper;

    @GetMapping
    public ResponseEntity<List<MpaDto>> readAll() {
        log.info("GET '/mpa' all mpa");
        return new ResponseEntity<>(mapper.listMpaToListDto(mpaService.readAll()), HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<MpaDto> readById(@PathVariable Integer id) {
        log.info("GET'/mpa/{}' mpa by id", id);
        return new ResponseEntity<>(mapper.mpaToDto(mpaService.readById(id)), HttpStatus.OK);
    }

}