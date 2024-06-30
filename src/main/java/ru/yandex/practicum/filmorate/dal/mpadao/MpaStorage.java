package ru.yandex.practicum.filmorate.dal.mpadao;

import ru.yandex.practicum.filmorate.model.Mpa;

import java.util.List;

public interface MpaStorage {
    Mpa readById(Integer id);

    List<Mpa> readAll();
}
