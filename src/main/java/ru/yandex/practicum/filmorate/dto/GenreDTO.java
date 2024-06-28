package ru.yandex.practicum.filmorate.dto;

import lombok.Builder;
import lombok.Data;

import javax.persistence.criteria.CriteriaBuilder;

@Data
@Builder
public class GenreDTO {

    private Integer id;
    private String name;

}
