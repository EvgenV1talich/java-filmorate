package ru.yandex.practicum.filmorate.service.mpa;

import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.Mpa;

import java.util.List;

@Component
public interface MpaService {

    List<Mpa> readAll();

    Mpa readById(Integer id);

}
