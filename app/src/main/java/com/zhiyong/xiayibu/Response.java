package com.zhiyong.xiayibu;

import android.util.SparseArray;

public enum Response {
    NO(0), YES(1), NEVER(2);

    public final int Value;

    Response(int value) {
        Value = value;
    }

    private static final SparseArray<Response> _map = new SparseArray<>();
    static {
        for (Response response : Response.values()) {
            _map.put(response.Value, response);
        }
    }

    public static Response from(int value) {
        return _map.get(value);
    }
}
