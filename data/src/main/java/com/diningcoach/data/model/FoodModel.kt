package com.diningcoach.data.model

import com.google.gson.annotations.SerializedName

data class FoodModel(
    @SerializedName("allergy_info")
    val allergyInfo: String? = null,
    @SerializedName("barcode")
    val barcode: String? = null,
    @SerializedName("brand_name")
    val brandName: String? = null,
    @SerializedName("country")
    val country: String? = null,
    @SerializedName("food_category")
    val foodCategory: String? = null,
    @SerializedName("food_image")
    val foodImage: String? = null,
    @SerializedName("food_type")
    val foodType: String? = null,
    @SerializedName("id")
    val id: String? = null,
    @SerializedName("name")
    val name: String? = null,
    @SerializedName("nutrition_info")
    val nutritionInfo: NutritionModel? = null,
    @SerializedName("report_no")
    val reportNo: String? = null,
    @SerializedName("storage")
    val storage: String? = null
)