package ru.yandex.practicum.filmorate.service.likes;

import javax.persistence.criteria.CriteriaBuilder;
import java.util.List;
import java.util.Set;

public interface LikesService {

    void deleteLike(Integer filmId, Long userId);

    void addLike(Integer filmId, Long userId);

    Set<Long> getLikerByFilmId(Integer filmID);

;

}