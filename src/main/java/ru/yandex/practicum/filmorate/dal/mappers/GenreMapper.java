package ru.yandex.practicum.filmorate.dal.mappers;

import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.dto.GenreDto;
import ru.yandex.practicum.filmorate.model.Genre;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Component
public class GenreMapper {

    private GenreMapper() {
    }

    public Genre dtoToGenre(GenreDto genreDTO) {
        return Genre.builder()
                .id(genreDTO.getId())
                .name(genreDTO.getName())
                .build();
    }

    public GenreDto genreToDto(Genre genre) {
        return GenreDto.builder()
                .id(genre.getId())
                .name(genre.getName())
                .build();
    }

    public List<GenreDto> listGenreToListDto(Collection<Genre> genres) {
        List<GenreDto> genreDTOS = new ArrayList<>();
        for (Genre genre : genres) {
            genreDTOS.add(genreToDto(genre));
        }
        return genreDTOS;
    }

}