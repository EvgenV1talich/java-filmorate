package ru.yandex.practicum.filmorate.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.yandex.practicum.filmorate.dto.UserDto;
import ru.yandex.practicum.filmorate.service.user.UserServiceImpl;

import javax.validation.Valid;
import java.util.List;

@RestController
@Slf4j
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserController {
    private final UserServiceImpl userService;


    @PostMapping
    public ResponseEntity<UserDto> createUser(@RequestBody @Valid UserDto newUser) {
        log.info("POST to '/users' for create user");
        return new ResponseEntity<>(userService.saveUser(newUser), HttpStatus.CREATED);
    }

    @PutMapping
    public ResponseEntity<UserDto> updateUser(@RequestBody @Valid UserDto newUser) {
        log.info("PUT to '/users' to update user");
        return new ResponseEntity<>(userService.updateUser(newUser), HttpStatus.OK);

    }

    @GetMapping
    public ResponseEntity<List<UserDto>> readAllUsers() {
        log.info("GET to '/users' to get all users");
        return new ResponseEntity<>(userService.readAllUsers(), HttpStatus.OK);
    }

    @PutMapping("{id}/friends/{friendId}")
    public ResponseEntity<?> addToFriends(@PathVariable Long id, @PathVariable Long friendId) {
        userService.addFiend(id, friendId);
        log.info(
                "PUT to '/users/{}/friends/{}' to add friend ID {} on user ID {}. ",
                id, friendId, id, friendId);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @DeleteMapping("{id}/friends/{friendId}")
    public ResponseEntity<?> deleteFriend(@PathVariable Long id, @PathVariable Long friendId) {
        userService.deleteFriendById(id, friendId);
        log.info(
                "DELETE to '/users/{}/friends/{}' " +
                        "to delete frienn ID={} for user ID={}.",
                id, friendId, id, friendId);
        return new ResponseEntity<>(HttpStatus.valueOf(200));
    }

    @GetMapping("{id}/friends")
    public ResponseEntity<List<UserDto>> readAllUserFriends(@PathVariable Long id) {
        log.info("GET to '/users/{id}/friends' to get all friends id for user {}.",
                id);
        return new ResponseEntity<>(userService.readAllFriendsByUserId(id), HttpStatus.OK);
    }

    @GetMapping("{id}/friends/common/{otherId}")
    public ResponseEntity<List<UserDto>> getCommonFriends(@PathVariable Long id, @PathVariable Long otherId) {
        log.info(
                "GET to '/users/{}/friends/common/{}' to get all same friends ids between users {} and {}.",
                id, otherId, id, otherId);
        return new ResponseEntity<>(userService.getSameFriendsList(id, otherId), HttpStatus.OK);
    }

    @GetMapping("{userId}")
    public ResponseEntity<UserDto> getUserById(@PathVariable Long userId) {
        log.info("GET to '/users/{userId}' to get user ID={}", userId);
        return new ResponseEntity<>(userService.getUserById(userId), HttpStatus.OK);
    }

    @DeleteMapping("/{userId}")
    public ResponseEntity<?> deleteUser(@PathVariable Long userId) {
        log.info("DELETE to '/users/{}' to delete user {}. ", userId, userId);
        userService.deleteUser(userId);
        return new ResponseEntity<>(HttpStatus.OK);
    }

}