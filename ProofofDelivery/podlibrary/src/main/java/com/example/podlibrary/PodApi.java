package com.example.podlibrary;

import okhttp3.MultipartBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;

/**
 * Created by bryanyeung on 2/2/2017.
 */
interface PodApi {
    @Multipart
    @POST("/upload")
    Call<ResponseBody> uploadPOD(@Part MultipartBody.Part image);
}
