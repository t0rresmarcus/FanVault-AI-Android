package com.fanvault.app.data.remote

import retrofit2.http.Body
import retrofit2.http.POST

data class ChatRequest(val question: String)

data class ChatResponse(
    val answer: String = "",
    val sources: List<String> = emptyList()
)

interface ChatApi {
    @POST("chat")
    suspend fun chat(@Body request: ChatRequest): ChatResponse
}
