package shop.mtcoding.metamall.model.user;

public enum Role {
    ADMIN("ADMIN"), SELLER("SELLER"), USER("USER");

    private final String role;

    Role(String role) {
        this.role = role;
    }

    public String getRole() {
        return role;
    }
}
