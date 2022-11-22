package module;

import java.util.UUID;

public interface Processing {
    void start(UUID jobId);

    void shutdown();
}
