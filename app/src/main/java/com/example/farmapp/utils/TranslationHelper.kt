package com.example.farmapp.utils

import android.content.Context
import com.google.mlkit.nl.translate.TranslateLanguage
import com.google.mlkit.nl.translate.Translation
import com.google.mlkit.nl.translate.Translator
import com.google.mlkit.nl.translate.TranslatorOptions

object TranslationHelper {

    private var translator: Translator? = null

    fun initTranslator(sourceLang: String, targetLang: String, context: Context, onReady: () -> Unit) {
        val options = TranslatorOptions.Builder()
            .setSourceLanguage(sourceLang)
            .setTargetLanguage(targetLang)
            .build()

        translator = Translation.getClient(options)

        // Model download (agar pehli baar use kar rahe ho)
        translator?.downloadModelIfNeeded()
            ?.addOnSuccessListener { onReady() }
            ?.addOnFailureListener { e -> e.printStackTrace() }
    }

    fun translate(text: String, onTranslated: (String) -> Unit) {
        translator?.translate(text)
            ?.addOnSuccessListener { translatedText ->
                onTranslated(translatedText)
            }
            ?.addOnFailureListener {
                onTranslated(text) // fallback → original
            }
    }
}
