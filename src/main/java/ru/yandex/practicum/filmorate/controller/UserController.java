package ru.yandex.practicum.filmorate.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.dto.EventDTO;
import ru.yandex.practicum.filmorate.dto.UserDTO;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.service.FeedService;
import ru.yandex.practicum.filmorate.service.RecommendationService;
import ru.yandex.practicum.filmorate.service.UserService;

import javax.validation.Valid;
import java.util.List;

@RestController
@Slf4j
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;


    @PostMapping
    public ResponseEntity<UserDTO> createUser(@RequestBody @Valid UserDTO newUser) {
        log.info("Получен POST запрос по эндпоинту '/users' на создание user");
        return new ResponseEntity<>(userService.createUser(newUser), HttpStatus.CREATED);
    }

    @PutMapping
    public ResponseEntity<UserDTO> updateUser(@RequestBody @Valid UserDTO newUser) {
        log.info("Получен PUT запрос по эндпоинту '/users' на обновление user");
        return new ResponseEntity<>(userService.updateUser(newUser), HttpStatus.OK);

    }

    @GetMapping
    public ResponseEntity<List<UserDTO>> readAllUsers() {
        log.info("Получен GET запрос по эндпоинту '/users' на получение всех users");
        return new ResponseEntity<>(userService.readUsersList(), HttpStatus.OK);
    }

    @PutMapping("{id}/friends/{friendId}")
    public ResponseEntity<?> addToFriends(@PathVariable Long id, @PathVariable Long friendId) {
        userService.addFiend(id, friendId);
        log.info(
                "Получен PUT запрос по эндпоинту '/users/{}/friends/{}' на добавление friend c ID {} для user c ID {}. ",
                id, friendId, id, friendId);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @DeleteMapping("{id}/friends/{friendId}")
    public ResponseEntity<?> deleteFriend(@PathVariable Long id, @PathVariable Long friendId) {
        userService.deleteFriendById(id, friendId);
        log.info(
                "Получен DELETE запрос по эндпоинту '/users/{}/friends/{}' " +
                        "на удаление friend c ID {} для user c ID {}.",
                id, friendId, id, friendId);
        return new ResponseEntity<>(HttpStatus.valueOf(200));
    }

    @GetMapping("{id}/friends")
    public ResponseEntity<List<UserDTO>> readAllUserFriends(@PathVariable Long id) {
        log.info("Получен GET запрос по эндпоинту '/users/{id}/friends' на получение всех друзей для user c ID {}.",
                id);
        return new ResponseEntity<>(userService.readAllUserFriends(id), HttpStatus.OK);
    }

    @GetMapping("{id}/friends/common/{otherId}")
    public ResponseEntity<List<UserDTO>> getCommonFriends(@PathVariable Long id, @PathVariable Long otherId) {
        log.info(
                "Получен GET запрос по эндпоинту '/users/{}/friends/common/{}' на получение всех общих друзей у Users c ID {} и {}.",
                id, otherId, id, otherId);
        return new ResponseEntity<>(userService.getCommonFriends(id, otherId), HttpStatus.OK);
    }

    @GetMapping("{userId}")
    public ResponseEntity<UserDTO> getUserById(@PathVariable Long userId) {
        log.info("Получен GET запрос по эндпоинту '/users/{userId}' на получение Users c ID {}", userId);
        return new ResponseEntity<>(userService.getUserById(userId), HttpStatus.OK);
    }

    @DeleteMapping("/{userId}")
    public ResponseEntity<?> deleteUser(@PathVariable Long userId) {
        log.info("Получен DELETE запрос по эндпоинту '/users/{}' на удаление User c ID {}. ", userId, userId);
        userService.deleteUser(userId);
        return new ResponseEntity<>(HttpStatus.OK);
    }

}