package ru.yandex.practicum.filmorate.dal.likesdao;

import java.util.Set;

public interface LikesDao {

    void deleteLike(Integer filmId, Long userId);

    void addLike(Integer filmId, Long userId);

    Set<Long> getLikerByFilmId(Integer filmID);

}
