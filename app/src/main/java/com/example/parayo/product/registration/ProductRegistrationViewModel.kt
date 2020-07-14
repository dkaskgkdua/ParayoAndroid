package com.example.parayo.product.registration

import android.app.Application
import androidx.lifecycle.MutableLiveData
import com.example.parayo.product.category.categoryList
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
}