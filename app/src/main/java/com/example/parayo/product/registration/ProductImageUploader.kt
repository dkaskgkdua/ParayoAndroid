package com.example.parayo.product.registration

import com.example.parayo.api.ParayoApi
import com.example.parayo.api.response.ApiResponse
import com.example.parayo.api.response.ProductImageUploadResponse
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import org.jetbrains.anko.AnkoLogger
import org.jetbrains.anko.error
import java.io.File
import java.lang.Exception

class ProductImageUploader : AnkoLogger {

    /*
      upload 함수는 파일 객체를 받아 API 요청에 맞는 파라미터를 생성하고 업로드 API를 호출하는 함수로
      API를 호출하기 위해서 suspend 함수로 선언했다. 네트워크 요청이 일어나는 곳이기 때문에
      withContext(Dispatchers.IO)로 IO 스레드에서 수행되도록 해야 합니다.
    */
    suspend fun upload(imageFile: File) = try {
        val part = makeImagePart(imageFile)
        withContext(Dispatchers.IO) {
            ParayoApi.instance.uploadProductImages(part)
        }
    } catch(e:Exception) {
        error("상품 이미지 등록 오류", e)
        ApiResponse.error<ProductImageUploadResponse>(
            "알 수 없는 오류가 발생했습니다."
        )
    }

    private fun makeImagePart(imageFile: File) : MultipartBody.Part {
        /*
          MediaType.parse는 http 요청이나 바디에 사용될 컨텐츠의 타입을 지정하는 MediaType객체를 만듬
          파일 업로드를 위해서는 일반적으로 multipart/form-data 타입이 많이 사용됨
        */
        val mediaType = MediaType.parse("multipart/form-data")
        // ReqeustBody.create는 http요청의 바디를 생성해준다. MediaType과 파일로 이루어져 있다.
        val body = RequestBody.create(mediaType, imageFile)
        // 파라미터들을 이용해서 멀티파트 요청의 바디파티를 생성해준다.
        return MultipartBody.Part.createFormData("image", imageFile.name, body)
    }
}