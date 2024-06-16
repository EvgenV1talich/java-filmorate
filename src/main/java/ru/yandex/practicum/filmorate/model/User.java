package ru.yandex.practicum.filmorate.model;

import lombok.Data;

import javax.validation.constraints.Email;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Set;
import java.util.TreeSet;

@Data
public class User {
    private Long id;
    @Email
    private String email;
    private String login;
    private String name;
    private LocalDate birthday;
    private Set<Long> friends = new TreeSet<>();
    private ArrayList<FriendRequest> requests;

    public User(Long id, String email, String login, String name, LocalDate birthday) {
        this.id = id;
        this.email = email;
        this.login = login;
        this.name = name;
        this.birthday = birthday;
    }

    public void addFriend(Long userId) {
        if (friends.contains(userId)) {
            return;
        }
        friends.add(userId);
    }

    public void removeFriend(Long userId) {
        if (!friends.contains(userId)) {
            return;
        }
        friends.remove(userId);
    }
}
