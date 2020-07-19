package com.example.parayo.product.registration

import android.app.Activity.RESULT_OK
import android.app.Application
import android.content.Intent
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.parayo.api.response.ApiResponse
import com.example.parayo.api.response.ProductImageUploadResponse
import com.example.parayo.product.category.categoryList
import kotlinx.coroutines.launch
import net.codephobia.ankomvvm.lifecycle.BaseViewModel

class ProductRegistrationViewModel(app: Application) : BaseViewModel(app){
    // imageUrls 는 이미지 업로드 후에 반환받은 이미지 주소를 저장할 변수이다.
    // 주소가 입력되면 자동으로 이미지를 표시해주도록 데이터바인딩을 이용하기 위해
    // List<MutableLiveDate<String?>>으로 선언함
    val imageUrls: List<MutableLiveData<String?>> = listOf(
        MutableLiveData(null as String?),
        MutableLiveData(null as String?),
        MutableLiveData(null as String?),
        MutableLiveData(null as String?)
    )
    // imageIds는 업로드 후 반환받은 이미지의 id들을 저장할 리스트이다.
    val imageIds: MutableList<Long?> =
        mutableListOf(null, null, null, null)

    val productName = MutableLiveData("")
    val description = MutableLiveData("")
    val price = MutableLiveData("")

    // categories는 뷰에서 스피너로 보여줄 카테고리명 리스트를 담는 변수이다. 데이터 바인딩을 위해
    // MutableLiveData를 사용하고 기본값으로 카테고리명 리스트를 넣어줌
    val categories = MutableLiveData(categoryList.map { it.name })

    // categoryIdSelected는 뷰의 스피너를 통해 선택된 카테고리의 아이디를 저장할 변수이다.
    // 기본값으로 첫번째 카테고리의 아이디값을 이용함.
    var categoryIdSelected: Int? = categoryList[0].id

    val descriptionLimit = 500
    val productNameLimit = 40

    // productNameLength와 descriptionLength 는 뷰에 표시될 현재 각 테스트의 길이(0/00형태)이다.
    // 역시 데이터바인딩을 이용하기 위해 MutableLiveData를 사용
    val productNameLength = MutableLiveData("0/$productNameLimit")
    val descriptionLength = MutableLiveData("0/$descriptionLimit")

    var currentImageNum = 0

    /*
       checkProductNameLength() 와 cehckDescriptionLength()는 각각 바인딩된 문자열의 길이를 체크하고
       길이 제한을 넘었을 경우 길이 제한까지 잘라준다. 그리고 각각의 문자열 길이를 0/00 형태로
       productNameLength와 descriptionLength에 반영해준다.
    */
    fun checkProductNameLength() {
        productName.value?.let {
            if(it.length > productNameLimit) {
                productName.value = it.take(productNameLimit)
            }
            productNameLength.value = "${productName.value?.length}/$productNameLimit"
        }
    }

    fun checkDescriptionLength() {
        description.value?.let {
            if(it.length > descriptionLimit) {
                description.value = it.take(descriptionLimit)
            }
            descriptionLength.value = "${description.value?.length}/$descriptionLimit"
        }
    }

    /*
      onCategorySelect 함수는 스피너에서 카테고리가 검색되었을 스피너의 OnItemSelectedListener 에서
      항목의 인덱스를 받아 동작할 함수이다. 해당하는 인덱스에 있는 카테고리 정보 중 id 값을 가져다가
      categoryIdSelected에 대입시켜준다.
    */
    fun onCategorySelect(position: Int) {
        categoryIdSelected = categoryList[position].id
    }

    fun pickImage(imageNum: Int) {
        /*
          inetent는 로컬 파일 시스템에서 특정 타입의 파일을 선택하는 액티비티 인텐트를 생성해준다.
          여기에는 apply {} 블록 안에 type을 image* 로 설정해 이미지 파일을 선택할 수 있도록
          설정해주었다.
        */
        val intent = Intent(Intent.ACTION_GET_CONTENT).apply {
            type = "image/*"
        }
        /*
          intent.resolveActivity 함수는 인텐트 데이터에 기반해 이 인텐트를 실질적으로 핸들링하는
          액티비티 컴포넌트를 반환한다. 이 함수를 이용하면 디바이스에 파일을 선택할 수 있는
          액티비티가 존재하는지를 체크할 수 있다. 만일 존재하지 않는다면 null을 반환하므로 null이 아닌
          경우 다음 로직을 수행할 수 있게 됩니다. 파라미터로 쓰이는 PackageManager 객체는 컨텍스트를
          통해서만 접글할 수 있지만 BaseViewModel은 이미 Application 클래스를 참조하고 있으므로
          이를 통해 PackageManager 객체를 가져올 수 있습니다.
         */
        intent.resolveActivity(app.packageManager)?.let {
            // startActivityForResult 함수는 액티비티를 실행하고 그 액티비티가 종료되면서 반환하는
            // 결과값을 받아오는 onAcitivityResult 콜백을 실행하도록 만들어준다. 여기에서
            // ACTION_GET_CONTENT로 실행된 액티비티에서 반환하는 값은 선택된 파일의 URI가 된다.
            startActivityForResult(intent, REQUEST_PICK_IMAGES)
        }
        // 이미지를 업로드한 후 응답으로 받은 이미지의 경로와 id 값을 설정해주기 위해 pickImages()
        // 함수에서 받은 이미지의 인덱스를 임시로 저장함. 사용자가 연속적으로 이미지 선택 버튼을 누를
        // 수 있기 때문에 정확한 동작을 위해서는 업로드 중이면 프로그래스 다이얼로그를 띄어주고 추가적인
        // 동작을 방지하는 것이 바람직하지만 여기에서는 구현하지 않았습니다.
        currentImageNum = imageNum
    }
    /*
      onActivityResult 함수는 startActivityForResult 함수에 의해 실행된 액티비티가 종료되었을 때
      수행되는 콜백이다. 원래는 startActivityForResult 도 onActivityResult 도 액티비티의 함수이지만
      BaseAcitivity와 BaseViewModel이 뷰모델에서 startActivityForResult를 사용할 수 있도록 구현해주고
      있고 뷰모델의 onActivityResult도 호출해주고 있다.
    */
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if(resultCode != RESULT_OK)
            return
        when(requestCode) {
            REQUEST_PICK_IMAGES->data?.let { uploadImage(it) }
        }
    }
    private fun uploadImage(intent: Intent) =
        /*
          getContent는 URI로부터 파일을 읽어오는 역할을 한다. 실제 구현은 BaseViewModel에서
          어플리케이션 컨텍스트의 ContentResolver를 통해 URI의 파일을 열고 임시 파일을 생성해 복사한 후
          그 파일을 반환하도록 되어 있다.
        */
        getContent(intent.data)?.let {imageFile ->
            // viewModelScope는 뷰모델이 가지고 있는 코루틴 스코프이다. 이를 통해 실행 중인 코루틴은
            // 뷰모델이 클리어될 때 모두 중단된다.
            viewModelScope.launch {
                val response = ProductImageUploader().upload(imageFile)
                onImageUploadResponse(response)
            }
        }
    // 이미지가 업로드된 후 받은 응답으로 imageUrls와 imageIds의 currentImageNum 번째 값들을
    // 변경해주고 있다.
    private fun onImageUploadResponse(
        response: ApiResponse<ProductImageUploadResponse>
    ) {
        if(response.success && response.data != null) {
            imageUrls[currentImageNum].value = response.data.filePath
            imageIds[currentImageNum] = response.data.productImageId
        } else {
            toast(response.message ?: "알 수 없는 오류가 발생했습니다.")
        }
    }

    companion object {
        const val REQUEST_PICK_IMAGES = 0
    }
}