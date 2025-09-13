package com.example.farmapp.utils

import android.app.Activity
import androidx.appcompat.app.AlertDialog

class LanguageDialog(private val activity: Activity) {

    fun showLanguageDialog(onSelected: (String) -> Unit) {
        val languages = arrayOf("English", "Hindi", "Khortha", "Magahi", "Nagpuri", "Kudmali")

        val builder = AlertDialog.Builder(activity)
        builder.setTitle("Choose Language")
        builder.setCancelable(false)

        // ✅ Items set karo BEFORE create
        builder.setItems(languages) { dialogInterface, which ->
            val selectedLang = when (which) {
                0 -> "en"
                1 -> "hi"
                2 -> "kho"
                3 -> "mag"
                4 -> "nag"
                5 -> "kud"
                else -> "en"
            }
            dialogInterface.dismiss()   // dialog close karo
            onSelected(selectedLang)
        }

        val dialog = builder.create()  // ✅ Ab items sahi se show honge
        dialog.show()
    }
}
