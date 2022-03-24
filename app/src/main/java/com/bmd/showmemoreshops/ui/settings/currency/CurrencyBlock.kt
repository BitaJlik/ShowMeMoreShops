package com.bmd.showmemoreshops.ui.settings.currency

import android.annotation.SuppressLint
import android.os.AsyncTask
import android.util.Log
import com.bmd.showmemoreshops.data.models.Currency
import org.json.JSONArray
import org.json.JSONException
import java.io.BufferedReader
import java.io.IOException
import java.io.InputStreamReader
import java.net.HttpURLConnection
import java.net.URL

class CurrencyBlock(val currencyList: ArrayList<Currency> = ArrayList()) {
    private val url = "https://bank.gov.ua/NBUStatService/v1/statdirectory/exchange?json"
    init { GetDataServersURL().execute(url) }

    @SuppressLint("StaticFieldLeak")
    inner class GetDataServersURL : AsyncTask<String?, String?, String?>() {

        override fun doInBackground(vararg strings: String?): String? {
            var connection: HttpURLConnection? = null
            var reader: BufferedReader? = null
            try {
                val url = URL(strings[0])
                connection = url.openConnection() as HttpURLConnection
                connection.connect()
                val stream = connection.inputStream
                reader = BufferedReader(InputStreamReader(stream))
                val buffer = StringBuilder()
                var line: String?
                while (reader.readLine().also { line = it } != null) {
                    buffer.append(line).append("\n")
                }
                return buffer.toString()
            } catch (e: IOException) {
                e.printStackTrace()
            } finally {
                connection?.disconnect()
                try {
                    reader?.close()
                } catch (e: IOException) {
                    e.printStackTrace()
                }
            }
            return null
        }

        override fun onPostExecute(s: String?) {
            super.onPostExecute(s)
            currencyList.clear()
            try {
                if (s != null) {
                    val jsonArrayCurrency = JSONArray(s)
                    (0..jsonArrayCurrency.length()).forEach { i ->
                        val jsonObject = jsonArrayCurrency.getJSONObject(i)
                        val currency = Currency(
                            jsonObject.getString("r030"),
                            jsonObject.getString("txt"),
                            jsonObject.getDouble("rate"),
                            jsonObject.getString("cc"))
                        currencyList.add(currency)
                    } }
            } catch (e: JSONException) {
                Log.e("JSON error", "JSON format error")
            }
            currencyList.sortWith(compareBy { it.name })
            currencyList.add(0,Currency("000","Українська гривня",1.0,"UAH"))
        }
    }

}