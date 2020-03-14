package com.kelab.usercenter.result;

public class SingleResult<T> {
    private T obj;

    public T getObj() {
        return obj;
    }

    public void setObj(T obj) {
        this.obj = obj;
    }

    public SingleResult(T obj) {
        this.obj = obj;
    }

    public SingleResult() {
    }
}
