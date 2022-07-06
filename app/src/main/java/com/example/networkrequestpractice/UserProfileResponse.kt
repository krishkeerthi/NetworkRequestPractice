package com.example.networkrequestpractice

import com.google.gson.annotations.SerializedName

data class UserProfileResponse(
    val page: String,
    @SerializedName("per_page")
    val perPage: String,
    @SerializedName("total")
    val totalPages: String,
    @SerializedName("total_pages")
    val currentPage: String,
    val data: List<UserEntity>
)

data class UserEntity(
    val id: String,
    @SerializedName("first_name")
    val firstName: String,
    @SerializedName("last_name")
    val lastName: String,
    val email: String,
    @SerializedName("avatar")
    val avatarLink: String
)
