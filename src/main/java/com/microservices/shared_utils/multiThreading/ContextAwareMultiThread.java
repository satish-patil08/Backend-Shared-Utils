package com.microservices.shared_utils.multiThreading;

import com.microservices.shared_utils.threadLocals.AuthTokenStorage;
import com.microservices.shared_utils.threadLocals.MongoConnectionStorage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;
import java.util.stream.Collectors;

@Component
public class ContextAwareMultiThread {

    private final ExecutorService executorService;

    @Autowired
    public ContextAwareMultiThread(ExecutorService executorService) {
        this.executorService = executorService;
    }

    public void execute(Runnable task) {
        String currentSessionData = MongoConnectionStorage.getConnection();
        String authTokenStored = AuthTokenStorage.getToken();

        executorService.execute(() -> {
            MongoConnectionStorage.setConnection(currentSessionData);
            AuthTokenStorage.setToken(authTokenStored);
            try {
                task.run();
            } finally {
                MongoConnectionStorage.clear();
                AuthTokenStorage.clear();
            }
        });
    }

    public <T> List<Future<T>> invokeAll(List<Callable<T>> tasks) throws InterruptedException {
        String currentSessionData = MongoConnectionStorage.getConnection();
        String authTokenStored = AuthTokenStorage.getToken();

        List<Callable<T>> wrappedTasks = tasks.stream()
                .map(task -> (Callable<T>) () -> {
                    setContext(currentSessionData, authTokenStored);
                    try {
                        return task.call();
                    } finally {
                        clearContext();
                    }
                }).collect(Collectors.toList());

        return executorService.invokeAll(wrappedTasks);
    }

    private void setContext(String sessionData, String authToken) {
        MongoConnectionStorage.setConnection(sessionData);
        AuthTokenStorage.setToken(authToken);
    }

    private void clearContext() {
        MongoConnectionStorage.clear();
        AuthTokenStorage.clear();
    }

}
