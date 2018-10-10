package com.mercadolibre.candidate.uicontrollers.activities

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.widget.Toast
import com.mercadolibre.candidate.R
import com.mercadolibre.candidate.constants.BASE_URL
import com.mercadolibre.candidate.constants.DIALOG_ERROR
import com.mercadolibre.candidate.interfaces.OnDialogClickListener
import com.mercadolibre.candidate.uicontrollers.dialogs.DialogFragmentError
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Call
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

open class ActivityBase : AppCompatActivity(), OnDialogClickListener {

    lateinit var retrofit: Retrofit
    private var dialogError: DialogFragmentError? = null
    var tag = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        tag = this.javaClass.simpleName

        val logging = HttpLoggingInterceptor()
        logging.level = HttpLoggingInterceptor.Level.BODY

        val httpClient = OkHttpClient.Builder()
        httpClient.addInterceptor(logging)

        dialogError = DialogFragmentError()
        dialogError?.onDialogClickListener = this

        retrofit = Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .client(httpClient.build())
                .build()
    }

    open fun onFailure(call: Call<*>?) {
        if (call?.isCanceled == true) {
            Log.e(tag, getString(R.string.mobile_service_cancelled))
        } else {
            showDialogError()
        }
    }

    fun processRequest(responseBody: Response<*>?) {
        when {
            responseBody?.code() == 404 -> {
                Toast.makeText(applicationContext, getString(R.string.mobile_404), Toast.LENGTH_SHORT).show()
            }
            else -> {
                Toast.makeText(applicationContext, getString(R.string.mobile_generic_error), Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun showDialogError() {
        dialogError?.show(supportFragmentManager, DIALOG_ERROR)
    }

    override fun onCancel() {

    }

    override fun onRetry() {

    }
}