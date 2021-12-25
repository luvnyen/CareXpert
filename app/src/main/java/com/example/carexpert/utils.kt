package com.example.carexpert

import android.text.TextUtils
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.android.volley.Request
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import org.json.JSONArray
import org.json.JSONTokener

fun setTextInputEmptyError(_textInput: TextInputEditText, _textInputLayout: TextInputLayout, attr: String) {
    if (TextUtils.isEmpty(_textInput.getText())) {
        _textInputLayout.error = "${attr} cannot be empty"
    }
}

fun getCovidDataAPI(key: String, _textView: TextView, context: AppCompatActivity) {
    // Instantiate the RequestQueue.
    val queue = Volley.newRequestQueue(context)
    val url = "https://api.kawalcorona.com/indonesia/"

    // Request a string response from the provided URL.
    val stringRequest = StringRequest(
        Request.Method.GET, url,
        { response ->
            val jsonArray = JSONTokener(response).nextValue() as JSONArray
            val data = jsonArray.getJSONObject(0).getString(key)
            _textView.setText(data)
        },
        { _textView.text = "ERROR" })

    // Add the request to the RequestQueue.
    queue.add(stringRequest)
}