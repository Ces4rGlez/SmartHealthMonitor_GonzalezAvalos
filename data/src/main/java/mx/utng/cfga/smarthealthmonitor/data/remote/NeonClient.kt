package mx.utng.cfga.smarthealthmonitor.data.remote

import mx.utng.cfga.smarthealthmonitor.data.BuildConfig
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object NeonClient {
    // URL Base unificada con el Host del proyecto
    private const val BASE_URL = "https://${BuildConfig.NEON_HOST}/"

    val AUTH_HEADER  = "Bearer ${BuildConfig.NEON_API_KEY}"

    /** 
     * CONFIGURACIÓN DE CONEXIÓN UNIFICADA
     * Para la API HTTP de Neon, el host en la cadena de conexión DEBE coincidir 
     * exactamente con el host de la URL (el Project ID).
     */
    val CONN_STRING  = "postgresql://neondb_owner:npg_GUaPkN46wWHy@${BuildConfig.NEON_HOST}/neondb"

    val api: NeonApiService by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .client(OkHttpClient.Builder()
                .addInterceptor(HttpLoggingInterceptor().apply {
                    level = HttpLoggingInterceptor.Level.BODY
                }).build())
            .build()
            .create(NeonApiService::class.java)
    }
}
