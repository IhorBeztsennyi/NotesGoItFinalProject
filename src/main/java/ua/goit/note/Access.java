package ua.goit.note;

public enum Access {

    ACCESS_PUBLIC("public"),
    ACCESS_PRIVATE("private");

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
