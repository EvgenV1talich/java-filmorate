package ru.yandex.practicum.filmorate.model;

import lombok.Getter;
import lombok.Setter;
import java.time.LocalDate;
import java.util.Comparator;
import java.util.Objects;
import java.util.Set;
import java.util.TreeSet;


@Getter
@Setter
public class Film {

    private Integer id;
    private String name;
    private String description;
    private LocalDate releaseDate;
    private Integer duration;
    private Set<Long> likesFromUsers;

    public Film(Integer id, String name, String description, LocalDate releaseDate, Integer duration) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.releaseDate = releaseDate;
        this.duration = duration;
        this.likesFromUsers = new TreeSet<Long>();
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

    public void addLike(Integer userId) {
        if (likesFromUsers.contains(userId)) {
            return;
        }
        likesFromUsers.add(Long.valueOf(userId));
    }
    public void removeLike(Integer userId) {
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
