package ru.yandex.practicum.filmorate.model;

import lombok.AllArgsConstructor;
import lombok.Data;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;

@Data
@AllArgsConstructor
public class User {
    @NotNull private Integer id;
    @Email private String email;
    private String login;
    private String name;
    private LocalDate birthday;
}
