package ru.yandex.practicum.filmorate.dto;

import lombok.Data;

import javax.persistence.criteria.CriteriaBuilder;

@Data
public class GenreDTO {

    private Integer id;
    private String name;

}
