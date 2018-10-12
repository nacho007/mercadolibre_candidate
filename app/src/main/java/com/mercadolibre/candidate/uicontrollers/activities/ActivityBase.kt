package com.mercadolibre.candidate.uicontrollers.activities

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.util.Log
import com.mercadolibre.candidate.R
import com.mercadolibre.candidate.constants.BASE_URL
import com.mercadolibre.candidate.constants.DIALOG_DESCRIPTION
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

        dialogError?.onDialogClickListener = this

        if (supportFragmentManager.findFragmentByTag(DIALOG_ERROR) != null) {
            (supportFragmentManager.findFragmentByTag(DIALOG_ERROR) as DialogFragmentError).onDialogClickListener = this
        }

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
            showDialogError(getString(R.string.mobile_internet_error))
        }
    }

    fun processRequest(responseBody: Response<*>?) {
        when {
            responseBody?.code() == 404 -> {
                showDialogError(getString(R.string.mobile_404))
            }
            else -> {
                showDialogError(getString(R.string.mobile_generic_error))
            }
        }
    }


    /**
     *  DialogFragment.show() will take care of adding the fragment
     *  in a transaction.
     */
    private fun showDialogError(description: String) {
        cancelDialogError()

        // Create and show the dialog.
        dialogError = DialogFragmentError()
        val bundle = Bundle()
        bundle.putString(DIALOG_DESCRIPTION, description)
        dialogError?.arguments = bundle

        dialogError?.onDialogClickListener = this
        dialogError?.show(supportFragmentManager, DIALOG_ERROR)
    }


    /**
     *  We also want to remove any currently showing
     *  dialog, so make our own transaction and take care of that here.
     */
    fun cancelDialogError() {
        val ft = supportFragmentManager.beginTransaction()
        val prev = supportFragmentManager.findFragmentByTag(DIALOG_ERROR)
        if (prev != null) {
            ft.remove(prev)
        }
        ft.commit()
    }

    override fun onCancel(description: String) {

    }

    override fun onRetry() {

    }

}