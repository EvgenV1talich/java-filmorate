package ru.yandex.practicum.filmorate.service.likes;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.dal.likesdao.LikesDbStorage;

import java.util.Set;

@Slf4j
@Service
@RequiredArgsConstructor
public class LikesServiceImpl implements LikesService {

    private final LikesDbStorage likeStorage;

    @Override
    public void deleteLike(Integer filmId, Long userId) {
        likeStorage.deleteLike(filmId, userId);
    }

    @Override
    public void addLike(Integer idFilm, Long idUser) {
        likeStorage.addLike(idFilm, idUser);
    }

    @Override
    public Set<Long> getLikerByFilmId(Integer filmID) {
        return likeStorage.getLikerByFilmId(filmID);
    }

}