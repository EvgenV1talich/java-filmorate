package ru.yandex.practicum.filmorate.dal.mpaDao;

import ru.yandex.practicum.filmorate.model.Mpa;

import java.util.List;

public interface MpaStorage {
    Mpa readById(Integer id);

    List<Mpa> readAll();
}
