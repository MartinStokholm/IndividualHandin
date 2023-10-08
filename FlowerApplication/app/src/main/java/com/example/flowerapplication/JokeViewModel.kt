package com.example.flowerapplication

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET

class JokeViewModel : ViewModel() {

    private val jokeApiService: JokeApiService by lazy {
        createRetrofit().create(JokeApiService::class.java)
    }

    private val _joke = MutableLiveData<String>()
    val joke: LiveData<String>
        get() = _joke

    fun fetchJokeFromApi() {
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val response = jokeApiService.getJoke()
                if (response.isSuccessful && response.body() != null) {
                    val newJoke = response.body()!!.value
                    withContext(Dispatchers.Main) {
                        _joke.value = newJoke
                    }
                } else {
                    // TODO error handling
                }
            } catch (e: Exception) {
                // TODO network error handling
            }
        }
    }

    private fun createRetrofit(): Retrofit {
        return Retrofit.Builder()
            .baseUrl("https://api.chucknorris.io/jokes/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }
}

interface JokeApiService {
    @GET("random")
    suspend fun getJoke(): retrofit2.Response<JokeResponse>
}
