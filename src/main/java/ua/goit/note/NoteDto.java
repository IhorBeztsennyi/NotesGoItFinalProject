package ua.goit.note;

import ua.goit.users.UserDto;

import java.util.UUID;

public class NoteDto {

    private UUID id;
    private String name;
    private String content;
    private Access accessType;

    private UserDto user;

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public NoteDto() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Access getAccessType() {
        return accessType;
    }

    public void setAccessType(Access accessType) {
        this.accessType = accessType;
    }

    public UserDto getUser() {
        return user;
    }

    public void setUser(UserDto user) {
        this.user = user;
    }
}
