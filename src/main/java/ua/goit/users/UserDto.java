package ua.goit.users;

import ua.goit.note.NoteDao;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import java.util.Set;
import java.util.UUID;

public class UserDto {

    private UUID id;
    private String username;
    private String email;
    private String password;
    private UserRole userRole;
    private Set<NoteDao> notes;

    public UserDto() {
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    @NotEmpty(message = "Please enter username!")
    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    @Email
    @NotEmpty(message = "Please enter email!")
    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    @NotEmpty(message = "Please enter password!")
    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public UserRole getUserRole() {
        return userRole;
    }

    public void setUserRole(UserRole userRole) {
        this.userRole = userRole;
    }

    public Set<NoteDao> getNotes() {
        return notes;
    }

    public void setNotes(Set<NoteDao> notes) {
        this.notes = notes;
    }
}
