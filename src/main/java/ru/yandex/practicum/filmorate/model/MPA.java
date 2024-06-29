package ru.yandex.practicum.filmorate.model;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MPA {

    private Integer id;
    private String name;

    public MPA(Integer id, String name, Integer films_id) {
        this.id = id;
        this.name = name;
    }

    public MPA() {

    }

    @Override
    public String toString() {
        return "MPA{" +
                "id=" + id +
                ", name='" + name + '\'' +
                '}';
    }
}
