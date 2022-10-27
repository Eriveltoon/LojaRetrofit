package com.example.lojaretrofit.api

import com.example.lojaretrofit.model.Produto
import retrofit2.Call
import retrofit2.http.GET

//servico (API)
interface ProdutoApi {
//https://oficinacordova.azurewebsites.net/android/rest/produto

    //chamada Postman e endpoint
    @GET("/android/rest/produto")

    //Call equivalente Observable (Angular)
    //Lista do tipo Produto
    fun listar(): Call<List<Produto>>
}