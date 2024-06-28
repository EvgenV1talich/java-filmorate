package ru.yandex.practicum.filmorate.dal.likesdao;

import java.util.List;
import java.util.Set;

public interface LikesDAO {

    void deleteLike(Long filmId, Long userId);

    void addLike(Long filmId, Long userId);

    Set<Long> getLikerByFilmId(Integer filmID);

}
