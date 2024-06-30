package ru.yandex.practicum.filmorate.model;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@EqualsAndHashCode
public class Mpa {

    private Integer id;
    private String name;

    public Mpa(Integer id, String name) {
        this.id = id;
        this.name = name;
    }

    public Mpa() {

    }

    @Override
    public String toString() {
        return "MPA{" +
                "id=" + id +
                ", name='" + name + '\'' +
                '}';
    }
}
