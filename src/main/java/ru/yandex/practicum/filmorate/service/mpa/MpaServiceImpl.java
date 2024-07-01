package ru.yandex.practicum.filmorate.service.mpa;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.dal.mpaDao.MpaDbStorage;
import ru.yandex.practicum.filmorate.model.Mpa;

import java.util.List;

@Service
@RequiredArgsConstructor
public class MpaServiceImpl implements MpaService {

    private final MpaDbStorage mpaDbStorage;

    @Override
    public List<Mpa> readAll() {
        return mpaDbStorage.readAll();
    }

    @Override
    public Mpa readById(Integer id) {
        return mpaDbStorage.readById(id);
    }
}
