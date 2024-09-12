package antifraud.user.endpoint.response;

public record AppUserAccessInfo(String status) {

    public AppUserAccessInfo(String username, Boolean locked) {
        this("User " + username + " " + (locked ? "locked" : "unlocked") + "!");
    }

}
