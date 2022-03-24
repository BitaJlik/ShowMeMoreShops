package com.bmd.showmemoreshops.data.models

data class User(
    var name: String = "Name",
    var password: String = "NULL",
    var email: String = "Email",
    var UUID : String = "NULL",
    var markets: HashMap<String,Market> = HashMap(),
    var sizeMaxMarkets: Int = 0
) {
}
