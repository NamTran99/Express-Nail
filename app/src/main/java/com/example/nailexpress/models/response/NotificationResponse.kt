package com.example.nailexpress.models.response

import com.google.gson.annotations.SerializedName

data class NotificationResponse(
    @SerializedName("is_read") val isRead: Boolean? = false,
    @SerializedName("updated_at") val updatedAt: String? = "",
    @SerializedName("avatar_url") val avatarUrl: String? = "",
    @SerializedName("user_id") val userId: Int? = 0,
    @SerializedName("data_id") val dataId: Int? = 0,
    @SerializedName("meta_data") val metaData: Any? = null,
    @SerializedName("created_at") val createdAt: String? = "",
    @SerializedName("id") val id: Int? = 0,
    @SerializedName("type") val type: Int? = 0,
    @SerializedName("thumbnail_url") val thumbnailUrl: String? = "",
    @SerializedName("message") val message: String? = "",
    @SerializedName("sender_id") val senderId: Int? = 0
)