package threadpool;

public class TargetTaskWithError extends TargetTask{
    @Override
    public void run() {
        super.run();
        throw new RuntimeException("Error from:" + taskName);
    }
}
