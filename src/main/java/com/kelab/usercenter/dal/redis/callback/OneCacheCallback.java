package com.kelab.usercenter.dal.redis.callback;

public interface OneCacheCallback<K, V> {

    V queryFromDB(K missKey);

}
