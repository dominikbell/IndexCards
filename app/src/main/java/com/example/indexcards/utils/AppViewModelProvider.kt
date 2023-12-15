package com.example.indexcards.utils

import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.CreationExtras
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.indexcards.utils.box.AddBoxViewModel
import com.example.indexcards.IndexCardsApplication
import com.example.indexcards.ui.home.HomeScreenViewModel

object AppViewModelProvider {
    val Factory = viewModelFactory {
        initializer {
            HomeScreenViewModel(
                indexCardsApplication().container.appRepository
            )
        }
//        initializer {
//            AddBoxViewModel(
//                indexCardsApplication().container.appRepository
//            )
//        }
    }
}

fun CreationExtras.indexCardsApplication(): IndexCardsApplication =
    (this[ViewModelProvider.AndroidViewModelFactory.APPLICATION_KEY] as IndexCardsApplication)