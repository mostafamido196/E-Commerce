package com.samy.groceryapp.utils;


import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

public class Response<T> {

    public final Status status;

    @Nullable
    public final T data;

    @Nullable
    public final String error;

    private Response(Status status, @Nullable T data, @Nullable String error) {
        this.status = status;
        this.data = data;
        this.error = error;
    }

    public static Response loading() {
        return new Response(Status.LOADING, null, null);
    }
    public static Response ideel() {
        return new Response(Status.IDEL, null, null);
    }

    public static <T> Response success(@NonNull T data) {
        return new Response(Status.SUCCESS, data, null);
    }

    public static Response error(@NonNull String error) {
        return new Response(Status.ERROR, null, error);
    }
}