package com.momen.redditnews.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

data class NewsRoot(
    var status: Boolean,
    var `data`: Data?,
)

data class Data(
    var children: List<Children>,
)

data class Children(
    var `data`: DataX,
)

@Parcelize
data class DataX(
    var title: String,
    var selftext: String,
    var url_overridden_by_dest: String?,
) : Parcelable