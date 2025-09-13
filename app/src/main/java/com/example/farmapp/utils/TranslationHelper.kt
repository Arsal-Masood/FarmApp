package com.example.farmapp.utils

import android.content.Context
import com.example.farmapp.ui.view.MainActivity
import com.google.mlkit.nl.translate.TranslateLanguage
import com.google.mlkit.nl.translate.Translator
import com.google.mlkit.nl.translate.TranslatorOptions

import org.json.JSONObject
import java.io.InputStream
//
//object TranslationHelper {
//
//    private var translator: Translator? = null
//    private var dictionary: JSONObject? = null
//
//    fun initDictionary(context: Context) {
//        val inputStream: InputStream = context.resources.openRawResource(
//            context.resources.getIdentifier("custom_dict", "raw", context.packageName)
//        )
//        val json = inputStream.bufferedReader().use { it.readText() }
//        dictionary = JSONObject(json)
//    }
//
//    fun initTranslator(sourceLang: String, targetLang: String, context: Context, onReady: () -> Unit) {
//        // Agar ML Kit support karta hai → init translator
//        if (TranslateLanguage.getAllLanguages().contains(targetLang)) {
//            val options = TranslatorOptions.Builder()
//                .setSourceLanguage(sourceLang)
//                .setTargetLanguage(targetLang)
//                .build()
//            translator = com.google.mlkit.nl.translate.Translation.getClient(options)
//            onReady()
//        } else {
//            // Custom dictionary only
//            translator = null
//            initDictionary(context)
//            onReady()
//        }
//    }
//
//    fun translate(text: String, targetLang: String, callback: (String) -> Unit) {
//        // Agar ML Kit translator initialized hai → use it
//        translator?.let {
//            it.translate(text)
//                .addOnSuccessListener { translated ->
//                    callback(translated)
//                }
//                .addOnFailureListener {
//                    callback(text) // fallback
//                }
//            return
//        }
//
//
//
//        // Agar custom dictionary me hai → use it
//        dictionary?.let { dict ->
//            val translated = dict.optJSONObject(text)?.optString(targetLang) ?: text
//            callback(translated)
//        } ?: callback(text)
//    }
//    fun translateWithDictionary(text: String, targetLang: String): String {
//        dictionary ?: return text
//        val dict = dictionary!!
//        // Split by space or punctuation
//        val words = text.split(" ", ",", ".", ":", "%").map { it.trim() }
//        val translatedWords = words.map { word ->
//            dict.optJSONObject(word)?.optString(targetLang) ?: word
//        }
//        return translatedWords.joinToString(" ")
//    }
//
//    // Overloaded simple method for default targetLang
////    fun translate(text: String, callback: (String) -> Unit) {
////        translate(text, MainActivity.selectedLanguage, callback)
////    }
//    fun translate(text: String, callback: (String) -> Unit) {
//        if (translator != null) {
//            translator!!.translate(text)
//                .addOnSuccessListener { callback(it) }
//                .addOnFailureListener { callback(text) }
//        } else {
//            val translated = translateWithDictionary(text, MainActivity.selectedLanguage)
//            callback(translated)
//        }
//    }
//
//}
object TranslationHelper {

    private var translator: Translator? = null
    private var dictionary: JSONObject? = null

    fun initDictionary(context: Context) {
        val inputStream: InputStream = context.resources.openRawResource(
            context.resources.getIdentifier("custom_dict", "raw", context.packageName)
        )
        val json = inputStream.bufferedReader().use { it.readText() }
        dictionary = JSONObject(json)
    }

    fun initTranslator(sourceLang: String, targetLang: String, context: Context, onReady: () -> Unit) {
        if (TranslateLanguage.getAllLanguages().contains(targetLang)) {
            val options = TranslatorOptions.Builder()
                .setSourceLanguage(sourceLang)
                .setTargetLanguage(targetLang)
                .build()
            translator = com.google.mlkit.nl.translate.Translation.getClient(options)
            onReady()
        } else {
            translator = null
            initDictionary(context)
            onReady()
        }
    }

    private fun translateWithDictionary(text: String, targetLang: String): String {
        if (dictionary == null) return text
        val dict = dictionary!!

        // Split by punctuation and spaces for partial translations
        val regex = Regex("[:,%]") // symbols ke aage space maintain karne ke liye
        val parts = text.split(regex).map { it.trim() }
        val symbols = regex.findAll(text).map { it.value }.toList()

        val translatedParts = parts.map { part ->
            dict.optJSONObject(part)?.optString(targetLang) ?: part
        }

        // Combine back translated parts with original symbols
        val result = StringBuilder()
        for (i in translatedParts.indices) {
            result.append(translatedParts[i])
            if (i < symbols.size) result.append(symbols[i])
            if (i < translatedParts.size - 1) result.append(" ")
        }

        return result.toString()
    }

    fun translate(text: String, targetLang: String, callback: (String) -> Unit) {
        translator?.let {
            it.translate(text)
                .addOnSuccessListener { translated -> callback(translated) }
                .addOnFailureListener { callback(text) }
            return
        }

        val translated = translateWithDictionary(text, targetLang)
        callback(translated)
    }

    fun translate(text: String, callback: (String) -> Unit) {
        translate(text, MainActivity.selectedLanguage, callback)
    }
}
