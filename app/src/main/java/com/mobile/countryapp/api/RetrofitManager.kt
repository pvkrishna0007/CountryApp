package com.mobile.countryapp.api

import com.fasterxml.jackson.databind.DeserializationFeature
import com.fasterxml.jackson.databind.ObjectMapper
import com.mobile.countryapp.BuildConfig
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ApplicationComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.jackson.JacksonConverterFactory
import java.util.concurrent.TimeUnit

@Module
@InstallIn(ApplicationComponent::class)
object RetrofitManagerModule {
    private const val BASE_URL = "https://dl.dropboxusercontent.com/"

    /**
     *  Created ApiInterface object
     */
    @Provides
    fun provideApiInterface(retrofit: Retrofit): ApiInterface {
        return retrofit.create(ApiInterface::class.java)
    }

    /**
     *  Object mapper for json parser
     */
    @Provides
    fun provideObjectMapper(): ObjectMapper {
        return ObjectMapper().apply {
            configure(DeserializationFeature.FAIL_ON_IGNORED_PROPERTIES, false)
            configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)
        }
    }

    /**
     *  Created Retrofit client object
     */
    @Provides
    fun providesRetrofitCustomClient(objectMapper: ObjectMapper): Retrofit {
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

        return Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(JacksonConverterFactory.create(objectMapper))
            .client(okHttpBuilder.build())
            .build()
    }

}