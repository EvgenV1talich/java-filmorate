package ru.yandex.practicum.filmorate.model;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class FriendRequest {

    private Integer requestUser;
    private Integer responseUser;
    private LocalDateTime date;
    private boolean firstApproved;
    private boolean secondApproved;
    private String status;

    public FriendRequest(Integer requestUser, Integer responseUser, LocalDateTime date, boolean firstApproved, boolean secondApproved, String status) {
        this.requestUser = requestUser;
        this.responseUser = responseUser;
        this.date = date;
        this.firstApproved = firstApproved;
        this.secondApproved = secondApproved;
        this.status = status;
    }

    public boolean checkStatus() {
        return true;
    }

    @Override
    public String toString() {
        return "FriendRequest{" +
                "requestUser=" + requestUser +
                ", responseUser=" + responseUser +
                ", date=" + date +
                ", status='" + status + '\'' +
                '}';
    }
}
