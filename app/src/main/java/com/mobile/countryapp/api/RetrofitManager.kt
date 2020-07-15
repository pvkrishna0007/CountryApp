package com.mobile.countryapp.api

import com.fasterxml.jackson.databind.DeserializationFeature
import com.fasterxml.jackson.databind.ObjectMapper
import com.mobile.countryapp.BuildConfig
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.jackson.JacksonConverterFactory
import java.util.concurrent.TimeUnit

object RetrofitManager {
    private const val BASE_URL = "https://dl.dropboxusercontent.com/"

    /**
     *  Created ApiInterface object
     */
    fun getApiInterface(): ApiInterface {
        return getRetrofitCustomClient().create(ApiInterface::class.java)
    }

    /**
     *  Object mapper for json parser
     */
    private val objectMapper: ObjectMapper
        get() {
            val mapper = ObjectMapper()
            mapper.configure(DeserializationFeature.FAIL_ON_IGNORED_PROPERTIES, false)
            mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)
            return mapper
        }

    /**
     *  Created Retrofit client object
     */
    private fun getRetrofitCustomClient(): Retrofit {
        val okHttpBuilder = OkHttpClient.Builder()

        if (BuildConfig.DEBUG) {
            // Added logging interceptor for network requests and responses
            val logging = HttpLoggingInterceptor()
            logging.level = HttpLoggingInterceptor.Level.BODY
            okHttpBuilder.addInterceptor(logging)
        }

        // Updated connection/read/write time out
        okHttpBuilder.connectTimeout((30 * 1000).toLong(), TimeUnit.MILLISECONDS)
        okHttpBuilder.readTimeout(180, TimeUnit.SECONDS)
        okHttpBuilder.writeTimeout((830 * 1000).toLong(), TimeUnit.MILLISECONDS)

        val mapper = objectMapper
        return Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(JacksonConverterFactory.create(mapper))
            .client(okHttpBuilder.build())
            .build()
    }

}