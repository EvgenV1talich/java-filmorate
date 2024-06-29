package ru.yandex.practicum.filmorate.dal.mappers;

import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.dto.GenreDTO;
import ru.yandex.practicum.filmorate.dto.MpaDTO;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.MPA;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class GenreMapper {

    private GenreMapper() {
    }

    public Genre dtoToGenre(GenreDTO genreDTO) {
        return Genre.builder()
                .id(genreDTO.getId())
                .name(genreDTO.getName())
                .build();
    }

    public GenreDTO genreToDto(Genre genre) {
        return GenreDTO.builder()
                .id(genre.getId())
                .name(genre.getName())
                .build();
    }

    public List<GenreDTO> listGenreToListDto(Collection<Genre> genres) {
        List<GenreDTO> genreDTOS = new ArrayList<>();
        for (Genre genre : genres) {
            genreDTOS.add(genreToDto(genre));
        }
        return genreDTOS;
    }

}