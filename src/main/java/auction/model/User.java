package auction.model;

public class User {
    private Integer id;

    private String name;

    private String password;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name == null ? null : name.trim();
    }

    public String getpassword() {
        return password;
    }

    public void setpassword(String password) {
        this.password = password == null ? null : password.trim();
    }
}