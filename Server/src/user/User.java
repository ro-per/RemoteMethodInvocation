package user;

import java.io.Serializable;
import java.util.Objects;
import java.util.Random;

public class User implements Serializable {

    private String name;
    private final String idUser;

    /* ----------------------------- CONSTRUCTOR ----------------------------- */
    public User() {
        this.idUser = "u" + new Random().nextLong();
    }

    public User(String name) {
        this.name = name.toLowerCase();
        this.idUser = "u" + new Random().nextLong();
    }

    /* ----------------------------- GETTERS ----------------------------- */
    public String getName() {
        return name;
    }

    public String getIdUser() {
        return idUser;
    }


    /* ----------------------------- SETTERS ----------------------------- */
    public void setName(String name) {
        this.name = name;
    }


    /* ----------------------------- METHODS ----------------------------- */

    public boolean compareWithString(String name) {
        return this.name.contains(name.toLowerCase());
    }

    /* ----------------------------- OVERRIDE ----------------------------- */
    @Override
    public String toString() {
        return name;
    }

    /* ----------------------------- EQUALS/HASH ----------------------------- */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        User user = (User) o;
        return name.contains(user.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(idUser, name);
    }
}
