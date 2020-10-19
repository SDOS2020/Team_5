package org.emberon.winscan.base;

public class TaskRegister {

    private final int totalTasks;
    private final TaskRegisterListener taskRegisterListener;
    private int taskCompleted = 0;
    private boolean failed = false;

    public TaskRegister(int totalTasks,
                        TaskRegisterListener taskRegisterListener) {
        this.totalTasks = totalTasks;
        this.taskRegisterListener = taskRegisterListener;
    }

    public void onSuccess() {
        if (failed) {
            return;
        }

        taskCompleted++;
        if (taskCompleted == totalTasks) {
            taskRegisterListener.onComplete();
        }
    }

    public void onError(String message) {
        if (!failed) {
            failed = true;
            taskRegisterListener.onError(message);
        }
    }
}


