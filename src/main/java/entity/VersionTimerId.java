package entity;

/**
 * Used in callback map to keep the version and timer id
 */
public class VersionTimerId {
    int version;
    Long timerId;

    public VersionTimerId(int version, Long timerId) {
        this.version = version;
        this.timerId = timerId;
    }

    public int getVersion() {
        return version;
    }

    public Long getTimerId() {
        return timerId;
    }
}
