package com.github.mxsm.uid.core.common;

/**
 * @author mxsm
 * @date 2022/4/17 16:20
 * @Since 1.0.0
 */
public class Result<T> {

    private T data;

    private Status status;

    public T getData() {
        return data;
    }

    public Result(T data, Status status) {
        this.data = data;
        this.status = status;
    }

    public Result() {

    }

    public void setData(T data) {
        this.data = data;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return "Result{" +
            "data=" + data +
            ", status=" + status.name() +
            '}';
    }

    public Result<T> buildSuccess(T data){
        setStatus(Status.SUCCESS);
        setData(data);
        return this;
    }
}
