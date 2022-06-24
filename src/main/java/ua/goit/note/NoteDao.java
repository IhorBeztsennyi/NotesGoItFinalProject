package ua.goit.note;

import org.hibernate.annotations.Type;
import ua.goit.users.UserDao;

import javax.persistence.*;
import java.util.UUID;
@Entity
@Table(name = "note")
@Cacheable
public class NoteDao {
    private UUID id;
    private String name;
    private String content;
    private Access accessType;

    private UserDao user;

    public NoteDao() {
    }
    @Id
    @GeneratedValue
    public UUID getId(){
        return id;
    }

    public void setId(UUID id){
        this.id = id;
    }
    @Column(name="name")
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
    @Column(name="content")
    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
    @Column(name="access_type")
    @Enumerated(EnumType.STRING)
    public Access getAccessType() {
        return accessType;
    }

    public void setAccessType(Access accessType) {
        this.accessType = accessType;
    }

    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.MERGE)
    @JoinColumn(name = "user_id", nullable = false)
    public UserDao getUser() {
        return user;
    }

    public void setUser(UserDao user) {
        this.user = user;
    }
}
