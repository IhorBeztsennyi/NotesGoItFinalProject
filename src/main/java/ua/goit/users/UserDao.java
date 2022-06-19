package ua.goit.users;

import ua.goit.note.NoteDao;

import javax.persistence.*;
import java.util.Set;
import java.util.UUID;

@Entity
@Table(name = "users")
public class UserDao {

    private UUID id;
    private String username;
    private String email;
    private String password;
    private UserRole userRole;
    private Set<NoteDao> notes;

    public UserDao(UUID id, String username, String email, String password, UserRole userRole, Set<NoteDao> notes) {
        this.id = id;
        this.username = username;
        this.email = email;
        this.password = password;
        this.userRole = userRole;
        this.notes = notes;
    }

    public UserDao() {
    }

    @Id
    @GeneratedValue
    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    @Column(name = "username")
    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    @Column(name = "email")
    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    @Column(name = "password")
    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    @Column(name = "user_role")
    @Enumerated(EnumType.STRING)
    public UserRole getUserRole() {
        return userRole;
    }

    public void setUserRole(UserRole userRole) {
        this.userRole = userRole;
    }

    @OneToMany(mappedBy = "user", fetch = FetchType.EAGER, cascade = CascadeType.REMOVE)
    public Set<NoteDao> getNotes() {
        return notes;
    }

    public void setNotes(Set<NoteDao> notes) {
        this.notes = notes;
    }
}
