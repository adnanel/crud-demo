package sample.api.model;

public class Student {
    long id;
    String firstName;
    String lastName;
    Indeks indeks;

    public Student() {}

    public Student(long id, String firstName, String lastName, Indeks indeks) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.indeks = indeks;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public Indeks getIndeks() {
        return indeks;
    }

    public void setIndeks(Indeks indeks) {
        this.indeks = indeks;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }
}
