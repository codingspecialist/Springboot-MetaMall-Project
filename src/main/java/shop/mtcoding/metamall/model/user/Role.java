package shop.mtcoding.metamall.model.user;

public enum Role {
    CUSTOMER("CUSTOMER"), SELLER("SELLER"), ADMIN("ADMIN");

    private final String role;

    Role(String role) {
        this.role = role;
    }

    public String getRole() {
        return role;
    }
}
