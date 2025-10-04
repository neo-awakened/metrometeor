/*
 * Copyright © 2014-2025, TWINT AG.
 * All rights reserved.
*/
package zu.ch.nasafestup.data

import Event
import retrofit2.http.GET

interface EventsApi {
    @GET("events")
    suspend fun getEvents(): EventDto
}
