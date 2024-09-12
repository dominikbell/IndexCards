package com.example.indexcards.utils

// Given an index where a string should be cut off, find the index of the word that
// might be cut by this cut off index
fun String.getCutString(cutOffIndex: Int): String {
    var newString = ""
    val listOfWords = this.split(" ")
    var ind = 0

    for (word in listOfWords) {
        if (ind + word.length > cutOffIndex) {
            if (newString[newString.length - 2] in listOf(',', '.', ';', ':')) {
                newString = newString.substring(0, newString.length - 2)
            }
            newString += " ..."
            break
        } else {
            ind += word.length + 1
            newString += "$word "
        }
    }


    return newString
}