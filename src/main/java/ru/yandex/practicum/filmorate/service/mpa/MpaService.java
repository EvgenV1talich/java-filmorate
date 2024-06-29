package ru.yandex.practicum.filmorate.service.mpa;

import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.MPA;

import java.util.List;

@Component
public interface MpaService {

    List<MPA> readAll();

    MPA readById(Integer id);

}
