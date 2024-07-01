package ru.yandex.practicum.filmorate;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;
import ru.yandex.practicum.filmorate.dal.filmDao.FilmDbStorage;
import ru.yandex.practicum.filmorate.dal.genreDao.GenreDbStorage;
import ru.yandex.practicum.filmorate.dal.likesDao.LikesDbStorage;
import ru.yandex.practicum.filmorate.dal.mpaDao.MpaDbStorage;
import ru.yandex.practicum.filmorate.dal.userDao.UserDbStorage;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.Mpa;
import ru.yandex.practicum.filmorate.model.User;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;
import java.util.TreeSet;


@Sql(executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD, scripts = "classpath:schema.sql")
@AutoConfigureTestDatabase
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@SpringBootTest
class FilmorateApplicationTests {

    private final FilmDbStorage filmDBStorage;
    private final UserDbStorage userDBStorage;
    private final GenreDbStorage genreDBStorage;
    private final LikesDbStorage likeDBStorage;
    private final MpaDbStorage mpaDBStorage;

    @DisplayName("Тест получения корректного имени Genre по ID")
    @Test
    public void findGenreByIdTest() {
        Genre genre = genreDBStorage.getById(1);
        Assertions.assertEquals("Комедия", genre.getName(), "Ожидался корректное имя Genre");
    }

    @DisplayName("Тест получения всех Genre")
    @Test
    public void getAllGenresTest() {
        Assertions.assertEquals(6, genreDBStorage.getAll().size(), "Ожидались коррктные Genres");
    }

    @DisplayName("Тест получения имени Mpa по ID")
    @Test
    public void findMpaByIdTest() {
        Mpa mpa = new Mpa(4, "R");
        Assertions.assertEquals(mpa, mpaDBStorage.readById(4), "Ожидался корректный Mpa");
    }

    @DisplayName("Тест получения Mpa по Film ID")
    @Test
    public void findMpaByFilmIdTest() {
        ArrayList<Genre> genres = new ArrayList<>();
        genres.add(new Genre(1, "Комедия"));
        Film film = new Film(1, "Film1Name", "descr", LocalDate.of(1990, 5, 5), 100, genres);
        film.setMpa(new Mpa(4, "R"));
        filmDBStorage.createFilm(film);
        Assertions.assertEquals(film.getMpa(), mpaDBStorage.readById(4), "Ожидались коррктные Mpa");
    }

    @DisplayName("Тест получения всех Mpa")
    @Test
    public void findAllMpaTest() {
        Assertions.assertEquals(5, mpaDBStorage.readAll().size(), "Ожидались коррктные Mpa");
    }


    // ТЕСТЫ USER

    @DisplayName("Тест создания новго User")
    @Test
    public void createUserTest() {
        User user = new User(1L, "helpme@mail.ru", "bezimeni", "s_imenem", LocalDate.of(1999, 1, 1));
        userDBStorage.createUser(user);
        Assertions.assertEquals(user, userDBStorage.getUser(1L), "Ожидались коррктный новый User после создания");
    }

    @DisplayName("Тест обновления данных у существующего User")
    @Test
    public void updateUserTest() {
        User user = new User(1L, "bolshenenado@mail.ru", "bezimeni", "s_imenem", LocalDate.of(1999, 1, 1));
        userDBStorage.createUser(user);
        user.setName("bez_imeni");
        userDBStorage.updateUser(user);
        Assertions.assertEquals(user, userDBStorage.getUser(1L), "Ожидались коррктный User после update");
    }

    @DisplayName("Тест получения существующего User по ID")
    @Test
    public void getUserById() {
        User user = new User(1L, "bolshenenado@mail.ru", "bezimeni", "s_imenem", LocalDate.of(1999, 1, 1));
        userDBStorage.createUser(user);
        Assertions.assertEquals(user, userDBStorage.getUser(1L), "Ожидался коррктный User");
    }

    @DisplayName("Тест DELETE существующего User по ID")
    @Test
    public void deleteUserTest() {
        User user = new User(1L, "bolshenenado@mail.ru", "bezimeni", "s_imenem", LocalDate.of(1999, 1, 1));
        userDBStorage.createUser(user);
        Assertions.assertEquals(1, userDBStorage.getUsers().size());
        userDBStorage.deleteUser(user.getId());
        Assertions.assertEquals(0, userDBStorage.getUsers().size());
    }

    @DisplayName("Тест получения всех существующих User")
    @Test
    public void readAllUsers() {
        User user = new User(1L, "htaehaethaeht@mail.ru", "haethaeh", "haeeah", LocalDate.of(1999, 1, 1));
        User user2 = new User(2L, "fafafa@mail.ru", "hatehaethaeh", "hae", LocalDate.of(1999, 1, 1));
        User user3 = new User(3L, "rgeatehahet@mail.ru", "hthaeaeh", "aehaheaeh", LocalDate.of(1999, 1, 1));
        userDBStorage.createUser(user);
        userDBStorage.createUser(user2);
        userDBStorage.createUser(user3);
        Assertions.assertEquals(3, userDBStorage.getUsers().size(), "Ожидались коррктные Userы");
    }


    //ТЕСТЫ FILM

    @DisplayName("Тест создания новго Film")
    @Test
    public void createFilmTest() {
        ArrayList<Genre> genres = new ArrayList<>();
        genres.add(new Genre(1, "Комедия"));
        Film film = new Film(1, "name", "descr", LocalDate.of(2002, 1, 1), 87, genres);
        film.setMpa(new Mpa(4, "R"));
        filmDBStorage.createFilm(film);
        Assertions.assertEquals(film, filmDBStorage.getFilm(1), "Ожидался коррктный Film");
    }

    @DisplayName("Тест обновления существующего Film")
    @Test
    public void updateFilmTest() {
        ArrayList<Genre> genres = new ArrayList<>();
        genres.add(new Genre(1, "Комедия"));
        Film film = new Film(1, "name", "descr", LocalDate.of(2002, 1, 1), 87, genres);
        film.setMpa(new Mpa(4, "R"));
        filmDBStorage.createFilm(film);
        film.setName("other_name");
        filmDBStorage.updateFilm(film);
        Assertions.assertEquals(film, filmDBStorage.getFilm(1), "Ожидался коррктный Film после update");
    }

    @DisplayName("Тест DELETE существующего Film по ID")
    @Test
    public void deleteFilmTest() {
        ArrayList<Genre> genres = new ArrayList<>();
        genres.add(new Genre(1, "Комедия"));
        Film film = new Film(1, "name", "descr", LocalDate.of(2002, 1, 1), 87, genres);
        film.setMpa(new Mpa(4, "R"));
        filmDBStorage.createFilm(film);
        Film film2 = new Film(1, "name", "descr", LocalDate.of(2002, 1, 1), 87, genres);
        film.setMpa(new Mpa(4, "R"));
        filmDBStorage.createFilm(film);
        Assertions.assertEquals(2, filmDBStorage.getFilmsCount());
        filmDBStorage.deleteFilm(film.getId());
        Assertions.assertEquals(1, filmDBStorage.getFilmsCount());
    }

    @DisplayName("Тест удаления Like к существующему Film существующим User")
    @Test
    public void deleteLikesByFilmId() {
        ArrayList<Genre> genres = new ArrayList<>();
        genres.add(new Genre(1, "Комедия"));
        Film film = new Film(1, "name", "descr", LocalDate.of(2002, 1, 1), 87, genres);
        film.setMpa(new Mpa(4, "R"));
        filmDBStorage.createFilm(film);

        User user = new User(1L, "htaehaethaeht@mail.ru", "haethaeh", "haeeah", LocalDate.of(1999, 1, 1));
        userDBStorage.createUser(user);
        likeDBStorage.addLike(1, 1L);
        Assertions.assertEquals(1, filmDBStorage.getFilm(1).getLikes(), "Ожидались " +
                "новый Like к Film с конкретным id");
        likeDBStorage.deleteLike(1, 1L);
        Assertions.assertEquals(0, filmDBStorage.getFilm(1).getLikes(), "Ожидалось " +
                "удаление существующего Like у Film с конкретным id");
    }

    //ТЕСТЫ FRIENDS

    @DisplayName("Тест добавления Friend к существующему User по Id")
    @Test
    public void addFriendByUserIdTest() {
        User user = new User(1L, "htaehaethaeht@mail.ru", "haethaeh", "haeeah", LocalDate.of(1999, 1, 1));
        User user2 = new User(2L, "fafafa@mail.ru", "hatehaethaeh", "hae", LocalDate.of(1999, 1, 1));
        userDBStorage.createUser(user);
        userDBStorage.createUser(user2);
        userDBStorage.userAddFriend(1L, 2L);
        Set<Long> set = new HashSet<>();
        set.add(user2.getId());
        Assertions.assertEquals(set, userDBStorage.getUser(1L).getFriends(), "Ожидалось "
                + "добавление friend к конкретному User");
    }


    @DisplayName("Test get friends count")
    @Test
    public void getAllFriendByUserIdTest() {
        User user = new User(1L, "fasdf@mail.ru", "rhyeh", "hyerrhyhry", LocalDate.of(1999, 1, 1));
        User user2 = new User(2L, "fads@mail.ru", "heryh", "hrereherh", LocalDate.of(1999, 1, 1));
        User user3 = new User(3L, "fasd@mail.ru", "hreyhre", "fdarsga", LocalDate.of(1999, 1, 1));
        userDBStorage.createUser(user);
        userDBStorage.createUser(user2);
        userDBStorage.createUser(user3);
        userDBStorage.userAddFriend(1L, 2L);
        userDBStorage.userAddFriend(1L, 3L);
        Set<Long> set = new TreeSet<>();
        set.add(2L);
        set.add(3L);
        Assertions.assertEquals(set, userDBStorage.getUser(1L).getFriends(), "Ожидалось получение " +
                "всех friend у конкретного User");
    }

}