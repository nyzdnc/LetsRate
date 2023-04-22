package com.example.letsrate.model

import com.google.firebase.Timestamp
import com.google.type.Date

data class RateModel(
    val commentTitle: String,
    val productName: String,
    val sellerName: String,
    val comment: String,
    val rate: String,
    val downloadUrl: String? = null,
    val userEmail: String,
    var rateId: String? = null,
    val createdDate : Timestamp
    )