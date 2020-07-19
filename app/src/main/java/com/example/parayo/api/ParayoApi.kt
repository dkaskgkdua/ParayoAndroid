package com.example.parayo.api

import com.example.parayo.api.request.SigninRequest
import com.example.parayo.api.request.SignupRequest
import com.example.parayo.api.response.ApiResponse
import com.example.parayo.api.response.ProductImageUploadResponse
import com.example.parayo.api.response.SigninResponse
import okhttp3.MultipartBody
import retrofit2.http.*

interface ParayoApi {
    @GET("/api/v1/hello")
    suspend fun hello(): ApiResponse<String>

    companion object {
        val instance = ApiGenerator()
            .generate(ParayoApi::class.java)
    }

    @POST("/api/v1/users")
    suspend fun signup(@Body signupRequest: SignupRequest)
        : ApiResponse<Void>

    @POST("/api/v1/signin")
    suspend fun signin(@Body signinRequest: SigninRequest)
        : ApiResponse<SigninResponse>

    // 파일 업로드가 필요한 API에는 일반적으로 @Multipart 어노테이션을 붙여 이 요청의 바디가
    // multipart임을 알려야 한다. 그리고 Multipart로 설정된 API로 설정된 API 요청의 파라미터들은
    // @Part 어노테이션을 붙여 이 파라미터가 multipart 요청의 일부임을 알려야한다.
    @Multipart
    @POST("/api/v1/product_images")
    suspend fun uploadProductImages(
        @Part images: MultipartBody.Part
    ): ApiResponse<ProductImageUploadResponse>
}