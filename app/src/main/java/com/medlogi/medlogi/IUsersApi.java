package com.medlogi.medlogi;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface IUsersApi {

    @POST("/login")
    Call<User> createUser(@Body User user);

}
