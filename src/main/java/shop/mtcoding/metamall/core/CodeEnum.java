package shop.mtcoding.metamall.core;

public enum CodeEnum {

    SUCCESS(200,"success"),
    UNKNOWN_ERROR(500, "An unknown error has occurred."),
    INVALID_ARGUMENT(400, "One or more arguments are invalid."),
    NOT_FOUND(404, "The requested resource was not found."),
    UNAUTHORIZED(401, "The operation requires authentication."),
    FORBIDDEN(403, "The operation is forbidden."),
    INTERNAL_SERVER_ERROR(500, "An internal server error has occurred.");

    private final int code;
    private final String message;

    private CodeEnum(int code, String message) {
        this.code = code;
        this.message = message;
    }

    public int getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }
}
