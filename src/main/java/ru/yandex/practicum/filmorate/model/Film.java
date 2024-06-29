package ru.yandex.practicum.filmorate.model;

import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Objects;
import java.util.Set;
import java.util.TreeSet;


@Getter
@Setter
@RequiredArgsConstructor
public class Film {

    private Integer id;
    private String name;
    private String description;
    private LocalDate releaseDate;
    private Integer duration;
    private Set<Genre> genre;
    private Set<Long> likesFromUsers;
    private MPA mpa;

    public Film(Integer id,
                String name,
                String description,
                LocalDate releaseDate,
                Integer duration,
                Set<Genre> genre) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.releaseDate = releaseDate;
        this.duration = duration;
        this.genre = genre;
        this.likesFromUsers = new TreeSet<Long>();
    }

    public void setMpa(MPA mpa) {
        this.mpa = mpa;
    }

    public void setGenre(Set<Genre> genre) {
        this.genre = genre;
    }

    public void setLikesFromUsers(Set<Long> likesFromUsers) {
        this.likesFromUsers = likesFromUsers;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Film film = (Film) o;
        return Objects.equals(id, film.id) && Objects.equals(name, film.name) && Objects.equals(description, film.description) && Objects.equals(releaseDate, film.releaseDate) && Objects.equals(duration, film.duration);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, description, releaseDate, duration);
    }

    @Override
    public String toString() {
        return "Film{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", releaseDate=" + releaseDate +
                ", duration=" + duration +
                '}';
    }
    public void addGenre(Genre newGenre) {
        genre.add(newGenre);
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public LocalDate getReleaseDate() {
        return releaseDate;
    }

    public void setReleaseDate(LocalDate releaseDate) {
        this.releaseDate = releaseDate;
    }

    public Integer getDuration() {
        return duration;
    }

    public void addLike(Long userId) {
        if (likesFromUsers.contains(userId)) {
            return;
        }

        likesFromUsers.add(userId);
    }

    public void removeLike(Long userId) {
        if (!likesFromUsers.contains(userId)) {
            return;
        }
        likesFromUsers.remove(userId);
    }

    public static final Comparator<Film> compareByLikes = new Comparator<Film>() {
        @Override
        public int compare(Film o1, Film o2) {
            return Integer.compare(o1.getLikes(), o2.getLikes());
        }
    };

    public int getLikes() {
        if (likesFromUsers.isEmpty()) {
            return 0;
        } else {
            return likesFromUsers.size();
        }
    }


    public void setDuration(Integer duration) {
        this.duration = duration;
    }
}
