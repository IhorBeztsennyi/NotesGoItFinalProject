package ua.goit.note;

import org.hibernate.annotations.Type;
import org.springframework.data.annotation.Id;

import javax.persistence.*;
import java.util.UUID;

public class NoteDao {

    private UUID id;
    private String name;
    private String content;
    private Access accessType;

    public NoteDao() {
    }
    @Id
    @Type(type="org.hibernate.type.PostgresUUIDType")
    @Column(name="id",columnDefinition="uuid")
    @GeneratedValue(strategy = GenerationType.AUTO)
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

    public void setContent(String noteContent) {
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
}
