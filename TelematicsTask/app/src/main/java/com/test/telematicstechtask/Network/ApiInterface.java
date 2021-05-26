package com.test.telematicstechtask.Network;


import com.google.gson.JsonObject;
import com.test.telematicstechtask.ResponseModel.AddProductModel;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.PATCH;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Part;
import retrofit2.http.Path;

public interface ApiInterface {

    @POST("mobile/configure/v1/configvalues")
    Call<AddProductModel> addProductToServer(@Body JsonObject v);

}
