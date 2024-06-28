package ru.yandex.practicum.filmorate.model;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@Getter
@Setter
public class MPA {

    private Integer id;
    private String rate;
    private Integer films_id;

    public MPA(Integer id, String rate, Integer films_id) {
        this.id = id;
        this.rate = rate;
        this.films_id = films_id;
    }

    public MPA() {

    }

    @Override
    public String toString() {
        return "MPA{" +
                "id=" + id +
                ", rate='" + rate + '\'' +
                '}';
    }
}
