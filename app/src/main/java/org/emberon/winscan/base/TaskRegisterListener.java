package org.emberon.winscan.base;

public interface TaskRegisterListener {
    void onComplete();

    void onError(String message);
}
