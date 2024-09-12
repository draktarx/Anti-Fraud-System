package antifraud.user.endpoint.response;

public record AppUserSuccessfullyDeletedResponseDto(String username, String status) {

    public AppUserSuccessfullyDeletedResponseDto(String username) {
        this(username, "Deleted successfully!");
    }

}
