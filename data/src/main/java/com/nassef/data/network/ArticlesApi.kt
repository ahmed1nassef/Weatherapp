package com.nassef.data.network

//import com.nassef.data.domain.Article
import com.nassef.domain.entities.ArticlesEntity
import retrofit2.http.GET
import retrofit2.http.Query

interface ArticlesApi {//response objectx

    //    @Headers(
//        "Content-Type: application/x-www-form-urlencoded",
//        "accept-encoding: gzip, deflate",
//        "access_token: mtlNzTVmXP4IBSba3z4XXXX"
//
//    )
//    @GET("top-headlines?country=us")
    @GET("top-headlines")
    suspend fun getArticles(@Query("country") country: String = "us"): ArticlesEntity

    @GET("everything")
    suspend fun searchArticles(@Query("q") searchTxt : String) : ArticlesEntity
}