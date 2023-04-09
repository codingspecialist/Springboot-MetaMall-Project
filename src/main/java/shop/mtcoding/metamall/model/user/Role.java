package shop.mtcoding.metamall.model.user;

public enum Role {
    USER("USER"), SELLER("SELLER"), ADMIN("ADMIN");

    private final String role;

    Role(String role) {
        this.role = role;
    }

    public String getRole() {
        return role;
    }
}
