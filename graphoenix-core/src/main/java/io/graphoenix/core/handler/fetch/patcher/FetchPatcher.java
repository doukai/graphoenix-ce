package io.graphoenix.core.handler.fetch.patcher;

public class FetchPatcher implements Patcher {

    private String path;

    private String target;

    public FetchPatcher(String path, String target) {
        this.path = path;
        this.target = target;
    }

    public String getPath() {
        return path;
    }

    public FetchPatcher setPath(String path) {
        this.path = path;
        return this;
    }

    public String getTarget() {
        return target;
    }

    public FetchPatcher setTarget(String target) {
        this.target = target;
        return this;
    }

    @Override
    public boolean isFetchPatcher() {
        return true;
    }
}
