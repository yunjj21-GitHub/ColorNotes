package com.yunjung.mynote.feature_note.domain.util

sealed class OrderType{
    object Ascending : OrderType() // 오름차순
    object Descending : OrderType() // 내림차순
}
