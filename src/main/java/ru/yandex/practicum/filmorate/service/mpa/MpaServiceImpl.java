package ru.yandex.practicum.filmorate.service.mpa;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.dal.mpadao.MpaDbStorage;
import ru.yandex.practicum.filmorate.model.MPA;

import java.util.List;
@Service
@RequiredArgsConstructor
public class MpaServiceImpl implements MpaService {

    private final MpaDbStorage mpaDbStorage;
    @Override
    public List<MPA> readAll() {
        return mpaDbStorage.readAll();
    }

    @Override
    public MPA readById(Integer id) {
        return mpaDbStorage.readById(id);
    }
}
