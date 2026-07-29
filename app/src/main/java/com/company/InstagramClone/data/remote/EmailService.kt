package com.company.InstagramClone.data.remote

import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.Body
import retrofit2.http.Header
import retrofit2.http.POST

data class Sender(
    val name: String,
    val email: String
)

data class Recipient(
    val email: String
)

data class EmailRequest(
    val sender: Sender,
    val to: List<Recipient>,
    val subject: String,
    val htmlContent: String
)

data class EmailResponse(
    val messageId: String
)

interface EmailService {
    @POST("smtp/email")
    suspend fun sendEmail(
        @Header("api-key") apiKey: String,
        @Body request: EmailRequest
    ): Response<EmailResponse>

    companion object {
        private const val BASE_URL = "https://api.brevo.com/v3/"

        fun create(): EmailService {
            return Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build()
                .create(EmailService::class.java)
        }
    }
}
