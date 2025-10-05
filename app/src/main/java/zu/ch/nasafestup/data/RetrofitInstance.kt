/*
 * Copyright © 2014-2025, TWINT AG.
 * All rights reserved.
*/
package zu.ch.nasafestup.data

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitInstance {
    val api: EventsApi by lazy {
        Retrofit.Builder()
            .baseUrl("https://nubia-kinless-bedraggledly.ngrok-free.dev/api/v1/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(EventsApi::class.java)
    }
}
