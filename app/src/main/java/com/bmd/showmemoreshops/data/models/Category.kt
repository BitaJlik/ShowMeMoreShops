package com.bmd.showmemoreshops.data.models

data class Category(
    var products: HashMap<String,Product> = HashMap(),
    var nameCategory: String = "NULL",
    var categoryUUID : String = java.util.UUID.randomUUID().toString()
) {
}