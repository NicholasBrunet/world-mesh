package dev.worldmesh.regionworker.logging;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public final class WorkerLogger {

    private WorkerLogger() {
    }

    public static Logger logger(Class<?> type) {
        return LoggerFactory.getLogger(type);
    }
}