package com.bmd.showmemoreshops.data.models

data class Product(
    var nameProduct: String = "Edit",
    var price: Double = 0.0,
    var amount: Int = 0,
    var isDiscount: Boolean = false,
    var productUUID : String = java.util.UUID.randomUUID().toString()
) {
    fun setNewProduct(product: Product) {
        this.price = product.price
        this.amount = product.amount
        this.isDiscount = product.isDiscount
    }
}
