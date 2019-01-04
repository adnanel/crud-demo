package sample.api.model;

import java.time.LocalDateTime;

public class Indeks {
    long id;
    LocalDateTime createdAt;

    public Indeks() {
    }

    public Indeks(long id, LocalDateTime createdAt) {
        this.id = id;
        this.createdAt = createdAt;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

}
