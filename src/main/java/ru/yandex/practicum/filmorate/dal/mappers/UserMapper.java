package ru.yandex.practicum.filmorate.dal.mappers;

import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.dto.UserDto;
import ru.yandex.practicum.filmorate.model.User;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Component
public class UserMapper {

    private UserMapper() {
    }

    public UserDto userToDTO(User user) {
        if (user == null) {
            throw new IllegalArgumentException("user cannot be null");
        }

        return UserDto.builder()
                .id(user.getId())
                .email(user.getEmail())
                .login(user.getLogin())
                .name(user.getName())
                .birthday(user.getBirthday())
                .build();
    }

    public User dtoToUser(UserDto userDTO) {
        if (userDTO == null) {
            throw new IllegalArgumentException("userDTO cannot be null");
        }
        User user = new User();
        user.setId(userDTO.getId());
        user.setEmail(userDTO.getEmail());
        user.setLogin(userDTO.getLogin());
        user.setName(userDTO.getName());
        user.setBirthday(userDTO.getBirthday());
        return user;
    }

    public List<UserDto> listUsersToListDto(Collection<User> users) {
        List<UserDto> userDTOS = new ArrayList<>();
        for (User user : users) {
            userDTOS.add(userToDTO(user));
        }
        return userDTOS;
    }

}