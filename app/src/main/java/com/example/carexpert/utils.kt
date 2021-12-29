package com.example.carexpert

import android.text.TextUtils
import android.widget.AutoCompleteTextView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.android.volley.Request
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import org.json.JSONObject
import org.json.JSONTokener
import java.text.NumberFormat

var username_global = ""
var username_other_global = ""
var date_post_global = ""
var time_post_global = ""

fun setUsername(username : String){
    username_global = username
}

fun setOtherUsername(username_other : String){
    username_other_global = username_other
}

fun setPostOther(username_other : String, date_post : String, time_post : String){
    username_other_global = username_other
    date_post_global = date_post
    time_post_global = time_post
}

fun setTextInputEmptyError(_textInput: TextInputEditText, _textInputLayout: TextInputLayout, attr: String) {
    if (TextUtils.isEmpty(_textInput.getText())) {
        _textInputLayout.error = "$attr cannot be empty"
    } else {
        _textInputLayout.error = ""
    }
}

fun setAutoCompleteTextViewEmptyError(_textInput: AutoCompleteTextView, _textInputLayout: TextInputLayout, attr: String) {
    if (TextUtils.isEmpty(_textInput.getText())) {
        _textInputLayout.error = "$attr cannot be emptys"
    } else {
        _textInputLayout.error = ""
    }
}

fun getCovidDataAPI(key: String, _textView: TextView, context: AppCompatActivity, type : String) {
    // Instantiate the RequestQueue.
    val queue = Volley.newRequestQueue(context)
    val url = "https://data.covid19.go.id/public/api/update.json"

    // Request a string response from the provided URL.
    val stringRequest = StringRequest(
        Request.Method.GET, url,
        { response ->
            val obj = JSONTokener(response).nextValue() as JSONObject
            val update = obj.get("update") as JSONObject
            val _type = update.get(type) as JSONObject
            val data = _type.get(key)
            val myString = when (key) {
                "tanggal" -> data
                else -> {
                    NumberFormat.getInstance().format(data)
                }
            }

            if (type == "penambahan") {
                if (key == "tanggal") {
                    _textView.text = "Last updated: $myString"
                } else {
                    _textView.text = "+$myString cases"
                }
            } else {
                _textView.text = myString.toString()
            }
        },
        { _textView.text = "ERROR" })

    // Add the request to the RequestQueue.
    queue.add(stringRequest)
}