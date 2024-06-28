package ru.yandex.practicum.filmorate.dal.mappers;

import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.dto.FilmDTO;
import ru.yandex.practicum.filmorate.dto.UserDTO;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.User;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

@Component
public class UserRowMapper implements RowMapper<User> {
    @Override
    public User mapRow(ResultSet rs, int rowNum) throws SQLException {
        User user = new User();
        user.setId(rs.getLong("ID"));
        user.setEmail(rs.getString("EMAIL"));
        user.setLogin(rs.getString("LOGIN"));
        user.setName(rs.getString("NAME"));
        user.setBirthday(rs.getDate("BIRTHDAY").toLocalDate());
        return user;
    }
    public User DTOToUser(UserDTO DTO) {
        if (DTO == null) {
            throw new IllegalArgumentException("filmDTO cannot be null");
        }
        User user = new User();
        user.setId(DTO.getId());
        user.setName(DTO.getName());
        user.setEmail(DTO.getEmail());
        user.setLogin(DTO.getLogin());
        user.setBirthday(DTO.getBirthday());
        return user;
    }
    public UserDTO userToDTO(User user) {
        if (user == null) {
            throw new IllegalArgumentException("User cannot be null");
        }

        return UserDTO.builder()
                .id(user.getId())
                .email(user.getEmail())
                .login(user.getLogin())
                .name(user.getName())
                .birthday(user.getBirthday()).build();
    }
    public List<UserDTO> UserListToUserDTOList(List<User>usersList) {
        List<UserDTO> DTOList = new ArrayList<>();
        for (User user : usersList) {
            DTOList.add(userToDTO(user));
        }
        return DTOList;
    }
}
