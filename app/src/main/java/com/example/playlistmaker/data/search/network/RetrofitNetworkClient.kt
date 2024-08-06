package com.example.playlistmaker.data.search.network

import com.example.playlistmaker.data.search.dto.Response
import com.example.playlistmaker.data.search.dto.TrackRequest
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class RetrofitNetworkClient(private val trackSearchService: TrackSearchApi) : NetworkClient {

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