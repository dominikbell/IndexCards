package com.example.indexcards.ui.box

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.Button
import androidx.compose.material3.Checkbox
import androidx.compose.material3.FloatingActionButtonDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.indexcards.R
import com.example.indexcards.data.Category
import com.example.indexcards.ui.elements.DescriptionField
import com.example.indexcards.ui.elements.LanguageDropDownMenu
import com.example.indexcards.ui.elements.NameField
import com.example.indexcards.ui.elements.RemindersSwitch
import com.example.indexcards.ui.elements.RequiredFieldsText
import com.example.indexcards.ui.elements.TopicField
import com.example.indexcards.utils.box.UiBoxWithCategories
import com.example.indexcards.utils.state.BoxDetails
import com.example.indexcards.utils.state.BoxState
import com.example.indexcards.utils.state.CategoryDetails
import com.example.indexcards.utils.state.CategoryState
import com.example.indexcards.utils.state.emptyCategory
import com.example.indexcards.utils.state.isLanguage
import com.example.indexcards.utils.state.toBox
import com.example.indexcards.utils.state.toBoxDetails
import com.example.indexcards.utils.state.toCategoryDetails
import java.time.LocalDateTime
import java.time.ZoneOffset

@Composable
fun BoxScreenEditing(
    modifier: Modifier = Modifier,
    boxUiState: BoxState,
    boxWithCategories: UiBoxWithCategories,
    categoryUiState: CategoryState,
    globalReminders: Boolean,
    hasNotificationPermission: Boolean,
    changeGlobalReminders: () -> Unit = {},
    requestNotificationPermission: () -> Boolean = { false },
    onSave: () -> Unit = {},
    updateBoxUiState: (BoxDetails) -> Unit = {},
    setAllReminders: () -> Unit = {},
    updateCategoryUiState: (CategoryDetails) -> Unit = {},
    resetCategoryUiState: () -> Unit = {},
    saveCategory: () -> Unit = {},
    deleteCategory: (Category) -> Unit = {},
) {
    val isLanguage = boxUiState.boxDetails.toBox().isLanguage()
    val remindersEnabled = boxUiState.boxDetails.reminders

    var expanded by remember { mutableStateOf(false) }
    var validName by remember { mutableStateOf(true) }
    var validTopic by remember { mutableStateOf(true) }
    var validCategoryName by remember { mutableStateOf(true) }
    var addCategory by remember { mutableStateOf(false) }
    var editCategory by remember { mutableLongStateOf(-1) }

    fun onSwitchChanged() {
        if (!globalReminders) {
            changeGlobalReminders()
        }
        if (!remindersEnabled) {
            setAllReminders()
        }
        updateBoxUiState(boxUiState.boxDetails.copy(reminders = !remindersEnabled))
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(8.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.SpaceBetween
    ) {
        Column(
            modifier = Modifier.fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            NameField(
                modifier = Modifier.fillMaxWidth(),
                boxUiState = boxUiState,
                isError = !validName,
                onValueChange = {
                    validName = true
                    updateBoxUiState(boxUiState.boxDetails.copy(name = it))
                }
            )

            if (isLanguage) {
                LanguageDropDownMenu(
                    modifier = Modifier.fillMaxWidth(),
                    boxUiState = boxUiState,
                    expanded = expanded,
                    changeExpanded = { expanded = !expanded },
                    isError = !validTopic,
                    onValueChange = {
                        validTopic = true
                        updateBoxUiState(boxUiState.boxDetails.copy(topic = it))
                    }
                )
            } else {
                TopicField(
                    modifier = Modifier.fillMaxWidth(),
                    boxUiState = boxUiState,
                    isError = !validTopic,
                    onValueChange = {
                        validTopic = true
                        updateBoxUiState(boxUiState.boxDetails.copy(topic = it))
                    }
                )
            }

            DescriptionField(
                modifier = Modifier.fillMaxWidth(),
                boxUiState = boxUiState,
                onValueChange = { updateBoxUiState(boxUiState.boxDetails.copy(description = it)) }
            )

            RequiredFieldsText()

            Spacer(modifier = Modifier.size(8.dp))

            RemindersSwitch(
                modifier = modifier,
                enabled = (remindersEnabled && hasNotificationPermission),
                onCheckedChange = {
                    if (!hasNotificationPermission) {
                        val success = requestNotificationPermission()
                        if (success) {
                            onSwitchChanged()
                        }
                    } else {
                        onSwitchChanged()

                    }
                },
                hasNotificationPermission = hasNotificationPermission,
                requestNotificationPermission = requestNotificationPermission
            )

            HorizontalDivider()

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(text = stringResource(id = R.string.use_categories))

                Checkbox(
                    checked = boxUiState.boxDetails.categories,
                    onCheckedChange = { updateBoxUiState(boxUiState.boxDetails.copy(categories = it)) }
                )
            }

            if (boxUiState.boxDetails.categories) {
                boxWithCategories.categoryList.forEach { category ->
                    if (editCategory == category.categoryId) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            OutlinedTextField(
                                modifier = Modifier.weight(1F),
                                value = categoryUiState.categoryDetails.name,
                                onValueChange = {
                                    validCategoryName = true
                                    updateCategoryUiState(
                                        categoryUiState.categoryDetails.copy(name = it)
                                    )
                                },
                                isError = !validCategoryName
                            )

                            Row {
                                IconButton(
                                    onClick = {
                                        validCategoryName = true
                                        resetCategoryUiState()
                                        editCategory = -1
                                    }
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.Clear,
                                        contentDescription = "cancel"
                                    )
                                }
                                IconButton(
                                    onClick = {
                                        if (categoryUiState.isValid) {
                                            saveCategory()
                                            editCategory = -1
                                        } else {
                                            validCategoryName = false
                                        }
                                    }
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.Check,
                                        contentDescription = "save"
                                    )
                                }
                            }
                        }
                    } else {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(30.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text(
                                modifier = Modifier.weight(1F),
                                text = category.name
                            )

                            Row {
                                IconButton(
                                    onClick = { deleteCategory(category) }
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.Delete,
                                        contentDescription = "delete"
                                    )
                                }
                                IconButton(
                                    onClick = {
                                        updateCategoryUiState(category.toCategoryDetails())
                                        editCategory = category.categoryId
                                    }
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.Edit,
                                        contentDescription = "edit"
                                    )
                                }
                            }
                        }
                    }
                }

                if (addCategory) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically,
                    ) {
                        OutlinedTextField(
                            modifier = Modifier.weight(1F),
                            value = categoryUiState.categoryDetails.name,
                            onValueChange = {
                                updateCategoryUiState(
                                    categoryUiState.categoryDetails.copy(name = it)
                                )
                            }
                        )
                        IconButton(
                            onClick = {
                                resetCategoryUiState()
                                addCategory = false
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
                                addCategory = false
                            }
                        ) {
                            Icon(
                                imageVector = Icons.Default.Check,
                                contentDescription = "save"
                            )
                        }
                    }
                } else {
                    HorizontalDivider(modifier = Modifier.padding(top = 4.dp))

                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable {
                                resetCategoryUiState()
                                addCategory = true
                            },
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically,
                    ) {
                        Text(text = stringResource(id = R.string.new_category))

                        IconButton(
                            onClick = {
                                resetCategoryUiState()
                                addCategory = true
                            }
                        ) {
                            Icon(imageVector = Icons.Filled.Add, contentDescription = "new")
                        }
                    }
                }
            }

            HorizontalDivider()

            Button(
                modifier = Modifier.padding(top = 8.dp),
                onClick = {
                    if (boxUiState.isValid) {
                        onSave()
                    } else {
                        if (!boxUiState.validName) {
                            validName = false
                        }
                        if (!boxUiState.validTopic) {
                            validTopic = false
                        }
                    }
                }
            ) {
                Text(text = stringResource(R.string.save))
            }
        }

        if (boxUiState.boxDetails.dateAdded != (-1).toLong()) {
            val date =
                LocalDateTime.ofEpochSecond(boxUiState.boxDetails.dateAdded, 0, ZoneOffset.UTC)
            val day = date.dayOfMonth
            val month = date.month
            val year = date.year
            val hour = date.hour
            val minute = date.minute

            Text(
                modifier = Modifier.padding(
                    bottom = (2 * FloatingActionButtonDefaults.LargeIconSize.value).dp
                ),
                text = stringResource(id = R.string.box_created) +
                        ": $day. $month $year "
                        + stringResource(id = R.string.at)
                        + " $hour:$minute."
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun BoxScreenEditingPreview() {
    val box = BoxDetails().copy(
        name = "Box 456",
        topic = "Maschinenbau",
        categories = true,
        description = "Schreibebiung mit seeeehr langem Text"
    ).toBox()

    val category1 = Category(categoryId = 0, boxId = -1, name = "Catta")
    val category2 = Category(categoryId = 1, boxId = -1, name = "Fanstato")

    val boxWithCategories = UiBoxWithCategories(
        box = box,
        categoryList = listOf(category1, category2)
    )

    BoxScreenEditing(
        modifier = Modifier.height(600.dp),
        boxUiState = BoxState(box.toBoxDetails()),
        boxWithCategories = boxWithCategories,
        categoryUiState = CategoryState(
            categoryDetails = emptyCategory.toCategoryDetails(),
            isValid = true
        ),
        globalReminders = true,
        hasNotificationPermission = true,
    )
}