package com.example.indexcards.data

object LanguageData {
    val language = mapOf(
        "_ar" to "Arabic",
        "_cn" to "Chinese",
        "_hr" to "Croatian",
        "_dk" to "Danish",
        "_uk" to "English",
        "_ee" to "Estonian",
        "_fi" to "Finnish",
        "_fr" to "French",
        "_de" to "German",
        "_gr" to "Greek",
        "_il" to "Hebrew",
        "_hu" to "Hungarian",
        "_is" to "Icelandic",
        "_it" to "Italian",
        "_jp" to "Japanese",
        "_kr" to "Korean",
        "_no" to "Norwegian",
        "_ir" to "Persian",
        "_pt" to "Portuguese",
        "_ru" to "Russian",
        "_es" to "Spanish",
        "_se" to "Swedish",
        "_tr" to "Turkish",
    )
}

/* TODO: UK-flag not recognized*/
fun String.toFlag(): String {
    return LanguageData.language.filter { it.value == this }.keys.first()
        .substring(1)
        .uppercase()
        .map { char ->
            Character.codePointAt("$char", 0) - 0x41 + 0x1F1E6
        }
        .map { codePoint ->
            Character.toChars(codePoint)
        }
        .joinToString(separator = "") { charArray ->
            String(charArray)
        }
}