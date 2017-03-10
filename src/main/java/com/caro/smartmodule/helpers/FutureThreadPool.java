package com.caro.smartmodule.helpers;

/**
 * Created by caro on 16/8/29.
 * 参考:http://blog.csdn.net/yangzhaomuma/article/details/51722779
 *
 * 以下是创建的工具类，结合封装了Callable/Runnable、FutureTask以及线程池，方便调用。这里特别注意executeFutureTask方法，在该方法中，重写了done方法以及新增
 onFinish抽象方法，可以通过回调onFinish，通知调用者任务执行结束。调用者，也可以通过FutureTask的get方法来阻塞，直到任务结束。
 */
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.FutureTask;

public class FutureThreadPool {

    private FutureThreadPool(){}
    private volatile static FutureThreadPool futureThreadPool;
    private static ExecutorService threadExecutor;
    /**
     * 获取线程池实例（单例模式）
     * @return
     */
    public static FutureThreadPool getInstance(){
        if(futureThreadPool==null){
            synchronized (FutureThreadPool.class) {
                futureThreadPool=new FutureThreadPool();
                threadExecutor=Executors.newSingleThreadExecutor();
            }
        }
        return futureThreadPool;
    }


    /**
     * 线程池处理Runnable(无返回值)
     * @param runnable Runnable参数
     */
    public void executeTask(Runnable runnable){
        threadExecutor.execute(runnable);
    }

    /**
     * 线程池处理Callable<T>，FutureTask<T>类型有返回值
     * @param callable Callable<T>参数
     * @return FutureTask<T>
     */
    public <T> FutureTask<T> executeTask(Callable<T> callable){
        FutureTask<T> futureTask= new FutureTask<T>(callable);
        threadExecutor.submit(futureTask);
        return futureTask;

    }
    /**
     * 线程池处理Runnable，FutureTask<T>类型有返回值(该方法不常用)
     * @param runnable
     * @param result Runnable任务执行完成后，返回的标识（注意：在调用时传入值，将在Runnable执行完成后，原样传出）
     * @return FutureTask<T>
     */
    public <T> FutureTask<T> executeTask(Runnable runnable,T result){
        FutureTask<T> futureTask= new FutureTask<T>(runnable,result);
        threadExecutor.submit(futureTask);
        return futureTask;
    }
    /**
     * 线程池处理自定义SimpleFutureTask，任务结束时有onFinish事件返回提示
     * @param mFutureTask 自定义SimpleFutureTask
     */
    public  <T> FutureTask<T>  executeFutureTask(SimpleFutureTask<T> mFutureTask){
        threadExecutor.submit(mFutureTask);
        return mFutureTask;
    }


}
