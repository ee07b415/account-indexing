package entity;

import java.util.Objects;

public class Config {
    ProcessType processType;
    String filePath;

    public Config(ProcessType processType, String filePath) {
        this.processType = processType;
        this.filePath = filePath;
    }

    public ProcessType getProcessType() {
        return processType;
    }

    public void setProcessType(ProcessType processType) {
        this.processType = processType;
    }

    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Config config = (Config) o;
        return processType == config.processType && filePath.equals(config.filePath);
    }

    @Override
    public int hashCode() {
        return Objects.hash(processType, filePath);
    }
}
