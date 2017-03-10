package com.caro.smartmodule.helpers;
import java.util.concurrent.Callable;
import java.util.concurrent.FutureTask;
/**
 * 任务结束回调onFinish的添加
 * @author zhao.yang
 *
 * @param <T>
 */
public abstract class SimpleFutureTask<T> extends FutureTask<T> {

    public SimpleFutureTask(Callable<T> callable) {
        super(callable);
    }

    @Override
    protected void done() {
        onFinish();
    }

    public abstract void onFinish();
}


