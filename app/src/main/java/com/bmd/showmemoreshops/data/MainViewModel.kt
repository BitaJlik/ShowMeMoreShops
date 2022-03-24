package com.bmd.showmemoreshops.data

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.bmd.showmemoreshops.data.models.*
import com.bmd.showmemoreshops.ui.settings.currency.CurrencyBlock
import com.google.android.gms.maps.model.LatLng

class MainViewModel(application: Application) : AndroidViewModel(application) {
    val liveDataUsers: MutableLiveData<HashMap<String, User>> by lazy { MutableLiveData<HashMap<String, User>>(HashMap()) }

    val liveDataUser: MutableLiveData<User> by lazy { MutableLiveData<User>(User()) }
    val liveDataMarket: MutableLiveData<Market> by lazy { MutableLiveData<Market>(Market()) }
    val liveDataCategory: MutableLiveData<Category> by lazy { MutableLiveData<Category>(Category()) }
    val liveDataProduct: MutableLiveData<Product> by lazy { MutableLiveData<Product>(Product()) }
    val liveDataComment: MutableLiveData<Comment> by lazy { MutableLiveData<Comment>(Comment()) }

    val liveDataTheme: MutableLiveData<Boolean> by lazy { MutableLiveData<Boolean>(false) }
    val liveDataToSettings: MutableLiveData<Boolean> by lazy { MutableLiveData<Boolean>(false) }
    val liveDataIsViewMap: MutableLiveData<Boolean> by lazy { MutableLiveData<Boolean>(false) }
    val liveDataIsSatellite: MutableLiveData<Boolean> by lazy { MutableLiveData<Boolean>(false) }
    val liveDataMoveMarket: MutableLiveData<Boolean> by lazy { MutableLiveData<Boolean>(false) }
    val liveDataIsEditComment: MutableLiveData<Boolean> by lazy { MutableLiveData<Boolean>(false) }

    val liveDataCurrencyBlock: MutableLiveData<CurrencyBlock> = MutableLiveData<CurrencyBlock>(CurrencyBlock())
    val liveDataCurrency: MutableLiveData<Currency> by lazy { MutableLiveData<Currency>(Currency("000", "Українська гривня", 1.0, "UAH")) }

    val liveDataMapPosition : MutableLiveData<LatLng> by lazy { MutableLiveData<LatLng>(LatLng(49.23522374140626, 28.4118144775948)) }

    val dataBase: DataBase = DataBase(this)

}
