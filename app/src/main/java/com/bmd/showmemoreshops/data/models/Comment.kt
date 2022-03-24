package com.bmd.showmemoreshops.data.models

data class Comment(
    var user: String = "User",
    var comment: String = "Edit this",
    var email: String = "Email@example.com",
    var date: String = "01.02.2003",
    var warns: Int = 0,
    var commentUUID : String = java.util.UUID.randomUUID().toString()
) {

}