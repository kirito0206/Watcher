package com.example.watcher.Retrofit;

import com.example.watcher.data.CommentData;
import com.example.watcher.data.Region;

import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.POST;

public interface WatcherService {
    @Headers("User-Agent:Mozilla/5.0 (Windows NT 10.0; WOW64)")@GET("data/list-total")
    Call<Region> getCall();

    @Headers({"Content-Type: application/json","Accept: application/json"})//需要添加头
    @POST("register")
    Call<ResponseBody>callRegister(@Body RequestBody info);

    @Headers({"Content-Type: application/json","Accept: application/json"})//需要添加头
    @POST("login")
    Call<ResponseBody>callLogin(@Body RequestBody info);

    @Headers({"Content-Type: application/json","Accept: application/json"})//需要添加头
    @POST("comment")
    Call<ResponseBody>sendMainComment(@Body RequestBody info);

    @Headers({"Content-Type: application/json","Accept: application/json"})//需要添加头
    @POST("subcomment")
    Call<ResponseBody>sendSubComment(@Body RequestBody info);

    @Headers({"Content-Type: application/json","Accept: application/json"})//需要添加头
    @POST("starcomment")
    Call<ResponseBody>starComment(@Body RequestBody info);

    @Headers({"Content-Type: application/json","Accept: application/json"})//需要添加头
    @POST("starsubcomment")
    Call<ResponseBody>starSubComment(@Body RequestBody info);

    @Headers({"Content-Type: application/json","Accept: application/json"})//需要添加头
    @POST("deletecomment")
    Call<ResponseBody>deleteComment(@Body RequestBody info);

    @Headers({"Content-Type: application/json","Accept: application/json"})//需要添加头
    @POST("deletesubcomment")
    Call<ResponseBody>deleteSubComment(@Body RequestBody info);

    @GET("getcomment")
    Call<CommentData>getComments();
}
