package ru.yandex.practicum.filmorate.model;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@Getter
@Setter
public class MPA {

    private Integer id;
    private String rate;

    public MPA(Integer id, String rate) {
        this.id = id;
        this.rate = rate;
    }

    @Override
    public String toString() {
        return "MPA{" +
                "id=" + id +
                ", rate='" + rate + '\'' +
                '}';
    }
}
