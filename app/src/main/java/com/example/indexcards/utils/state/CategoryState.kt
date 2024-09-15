package com.example.indexcards.utils.state

import com.example.indexcards.data.Category

val emptyCategory: Category = Category(
    categoryId = -1,
    boxId = -1,
    name = "EMPTY CATEGORY"
)

data class CategoryState(
    val categoryDetails: CategoryDetails = CategoryDetails(),
    val isValid: Boolean = false,
)

data class CategoryDetails(
    val id: Long = -1,
    val boxId: Long = -1,
    val name: String = "",
)

fun CategoryDetails.toCategory(): Category = Category(
    categoryId = id,
    boxId = boxId,
    name = name
)

fun Category.toCategoryDetails(): CategoryDetails = CategoryDetails(
    id = categoryId,
    boxId = boxId,
    name = name
)