package ua.goit.note;

public enum Access {

    ACCESS_PUBLIC("Public"),
    ACCESS_PRIVATE("Private");

    private String access;

    Access(String access){
        this.access = access;
    }

    public String getAccess() {
        return access;
    }

    public void setAccess(String access) {
        this.access = access;
    }
}
