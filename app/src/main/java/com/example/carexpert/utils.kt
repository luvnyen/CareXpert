package com.example.carexpert

import android.text.TextUtils
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout

fun setTextInputEmptyError(_textInput: TextInputEditText, _textInputLayout: TextInputLayout, attr: String) {
    if (TextUtils.isEmpty(_textInput.getText())) {
        _textInputLayout.error = "${attr} cannot be empty"
    }
}