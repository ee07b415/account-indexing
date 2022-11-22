package model;

import io.vertx.core.CompositeFuture;

public interface IO {
    CompositeFuture fetch();
}
