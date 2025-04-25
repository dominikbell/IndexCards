package com.example.indexcards.ui.elements

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material3.AssistChipDefaults
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.MenuAnchorType
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.indexcards.R
import com.example.indexcards.data.Category
import com.example.indexcards.data.LanguageData
import com.example.indexcards.utils.box.UiBoxWithCategories
import com.example.indexcards.utils.state.BoxDetails
import com.example.indexcards.utils.state.BoxState
import com.example.indexcards.utils.state.CategoryDetails
import com.example.indexcards.utils.state.CategoryState
import com.example.indexcards.utils.state.emptyBox
import com.example.indexcards.utils.state.emptyCategory
import com.example.indexcards.utils.state.getImageId
import com.example.indexcards.utils.state.toBox
import com.example.indexcards.utils.state.toBoxDetails
import com.example.indexcards.utils.state.toCategoryDetails


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LanguageDropDownMenu(
    modifier: Modifier = Modifier,
    boxUiState: BoxState,
    expanded: Boolean,
    isError: Boolean,
    isEnabled: Boolean = true,
    changeExpanded: () -> Unit = {},
    onValueChange: (String) -> Unit = {},
) {
    val context = LocalContext.current

    ExposedDropdownMenuBox(
        modifier = modifier,
        expanded = expanded,
        onExpandedChange = { changeExpanded() }
    ) {
        OutlinedTextField(
            modifier = modifier.menuAnchor(MenuAnchorType.PrimaryEditable, true),
            readOnly = true,
            value = boxUiState.boxDetails.topic,
            label = { Text(text = stringResource(R.string.language) + "*") },
            onValueChange = { },
            trailingIcon = {
                ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded)
            },
            isError = isError,
            enabled = isEnabled,
        )
        ExposedDropdownMenu(
            expanded = expanded,
            onDismissRequest = { changeExpanded() }) {
            LanguageData.language.entries.forEach { option ->
                DropdownMenuItem(
                    text = {
                        Row(
                            modifier = modifier.fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Image(
                                painter = painterResource(id = getImageId(context, option.key)),
                                contentDescription = null,
                                modifier = Modifier
                                    .size(AssistChipDefaults.IconSize),
                            )
                            Spacer(modifier = Modifier.size(6.dp))
                            Text(text = option.value.first)
                        }
                    },
                    onClick = {
                        onValueChange(option.value.first)
                        changeExpanded()
                    }
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun LanguageDropDownMenuPreview() {
    LanguageDropDownMenu(
        expanded = true,
        isError = false,
        boxUiState = BoxState(boxDetails = emptyBox.copy(topic = "English").toBoxDetails()),
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CategoriesDropDownMenu(
    modifier: Modifier = Modifier,
    currentCategory: Category,
    boxWithCategories: UiBoxWithCategories,
    categoryUiState: CategoryState,
    expanded: Boolean,
    addCategory: Boolean,
    changeExpanded: () -> Unit = {},
    onSelectCategory: (Category) -> Unit = {},
    updateCategoryUiState: (CategoryDetails) -> Unit = {},
    resetCategoryUiState: () -> Unit = {},
    saveCategory: () -> Unit = {},
    updateAddCategory: (Boolean) -> Unit = {},
) {
    val currentlyEmptyCategory = (currentCategory == emptyCategory)

    if (addCategory) {
        Row(
            modifier = modifier
                .fillMaxWidth()
                .padding(top = 4.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically,
        ) {
            OutlinedTextField(
                modifier = Modifier.weight(1F),
                value = categoryUiState.categoryDetails.name,
                label = { Text(text = stringResource(R.string.new_category)) },
                onValueChange = {
                    updateCategoryUiState(
                        categoryUiState.categoryDetails.copy(name = it)
                    )
                }
            )
            IconButton(
                onClick = {
                    resetCategoryUiState()
                    updateAddCategory(false)
//                    changeExpanded()
                }
            ) {
                Icon(
                    imageVector = Icons.Default.Clear,
                    contentDescription = "cancel"
                )
            }
            IconButton(
                onClick = {
                    saveCategory()
                    updateAddCategory(false)
//                    changeExpanded()
                }
            ) {
                Icon(
                    imageVector = Icons.Default.Check,
                    contentDescription = "save"
                )
            }
        }
    } else {
        ExposedDropdownMenuBox(
            modifier = modifier,
            expanded = expanded,
            onExpandedChange = { changeExpanded() }
        ) {
            OutlinedTextField(
                modifier = modifier.menuAnchor(MenuAnchorType.PrimaryEditable, true),
                readOnly = true,
                value = if (!currentlyEmptyCategory) {
                    currentCategory.name
                } else {
                    stringResource(R.string.no_category)
                },
                textStyle = if (currentlyEmptyCategory) {
                    LocalTextStyle.current.copy(fontStyle = FontStyle.Italic)
                } else {
                    LocalTextStyle.current
                },
                label = { Text(text = stringResource(R.string.category)) },
                onValueChange = { },
                trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded) },
            )
            ExposedDropdownMenu(
                expanded = expanded,
                onDismissRequest = {
                    changeExpanded()
                    resetCategoryUiState()
                }
            ) {
                DropdownMenuItem(
                    text = {
                        Text(
                            text = stringResource(id = R.string.no_category),
                            fontStyle = FontStyle.Italic
                        )
                    },
                    onClick = {
                        onSelectCategory(emptyCategory)
                        changeExpanded()
                    }
                )

                if (boxWithCategories.categoryList.isNotEmpty()) {
                    HorizontalDivider()
                }

                boxWithCategories.categoryList.forEach { option ->
                    DropdownMenuItem(
                        text = {
                            Row(
                                modifier = modifier.fillMaxWidth(),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(text = option.name)
                            }
                        },
                        onClick = {
                            onSelectCategory(option)
                            changeExpanded()
                        }
                    )
                }

                DropdownMenuItem(
                    text = {
                        HorizontalDivider(modifier = Modifier.padding(top = 4.dp))

                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable {
                                    resetCategoryUiState()
                                    updateAddCategory(true)
                                },
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically,
                        ) {
                            Text(text = stringResource(id = R.string.new_category))

                            IconButton(
                                onClick = {
                                    resetCategoryUiState()
                                    updateAddCategory(true)
                                }
                            ) {
                                Icon(imageVector = Icons.Filled.Add, contentDescription = "new")
                            }
                        }
                    },
                    onClick = {
                        resetCategoryUiState()
                        changeExpanded()
                        updateAddCategory(true)
                    }
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun CategoriesDropDownMenuPreview() {
    val box = BoxDetails().copy(
        name = "Box 456",
        topic = "Maschinenbau",
        description = "Schreibebiung mit seeeehr langem Text",
    ).toBox()

    val category1 = Category(categoryId = 0, boxId = -1, name = "Catta")
    val category2 = Category(categoryId = 1, boxId = -1, name = "Fanstato")

    val boxWithCategories = UiBoxWithCategories(
        box = box,
        categoryList = listOf(category1, category2)
    )

    CategoriesDropDownMenu(
        currentCategory = category1,
        boxWithCategories = boxWithCategories,
        expanded = false,
        addCategory = false,
        categoryUiState = CategoryState(
            categoryDetails = emptyCategory.toCategoryDetails(),
            isValid = true
        ),
    )
}