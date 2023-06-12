package com.diningcoach.data.model

import com.google.gson.annotations.SerializedName

data class NutritionModel(
    @SerializedName("id")
    val id: String? = null,
    @SerializedName("calorie")
    val calorie: Int? = null,
    @SerializedName("carbohydrate")
    val carbohydrate: Double? = null,
    @SerializedName("cholesterol")
    val cholesterol: Int? = null,
    @SerializedName("fat")
    val fat: Int? = null,
    @SerializedName("protein")
    val protein: Int? = null,
    @SerializedName("saturated_fat")
    val saturatedFat: Double? = null,
    @SerializedName("sodium")
    val sodium: Int? = null,
    @SerializedName("sugar")
    val sugar: Double? = null,
    @SerializedName("trans_fat")
    val transFat: Int? = null
)