package com.company.InstagramClone.data.remote

import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.Body
import retrofit2.http.Header
import retrofit2.http.POST

data class EmailRequest(
    val from: String,
    val to: String,
    val subject: String,
    val html: String
)

data class EmailResponse(
    val id: String
)

interface EmailService {
    @POST("emails")
    suspend fun sendEmail(
        @Header("Authorization") apiKey: String,
        @Body request: EmailRequest
    ): Response<EmailResponse>

    companion object {
        private const val BASE_URL = "https://api.resend.com/"

        fun create(): EmailService {
            return Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build()
                .create(EmailService::class.java)
        }
    }
}
