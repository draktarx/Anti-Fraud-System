package antifraud.user.endpoint.request;

public enum OperationAccess {

    LOCK(true),
    UNLOCK(false);

    private final boolean value;

    OperationAccess(boolean value) {
        this.value = value;
    }

    public boolean getValue() {
        return value;
    }
}
