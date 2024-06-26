package com.example.playlistmaker.data.network

import com.example.playlistmaker.data.dto.Response
import com.example.playlistmaker.data.dto.TrackRequest
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class RetrofitNetworkClient : NetworkClient {

    private val trackBaseUrl = "https://itunes.apple.com";
    private val retrofit = Retrofit.Builder()
        .baseUrl(trackBaseUrl)
        .addConverterFactory(GsonConverterFactory.create())
        .build()
    private val trackSearchService = retrofit.create(TrackSearchApi::class.java)

    override fun doRequest(dto: Any): Response {
        if (dto is TrackRequest) {
            try {
                val resp = trackSearchService.search(dto.text).execute()

                val body = resp.body() ?: Response()
                return body.apply { resultCode = resp.code() }
            } catch (ex: Exception) {
                return badResponce()
            }

        } else {
            return badResponce()
        }
    }

    private fun badResponce(): Response {
        return Response().apply {
            resultCode = 400
        }
    }

}