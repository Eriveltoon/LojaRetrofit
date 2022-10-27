package com.example.lojaretrofit.model

//classe de negocio
data class Produto(
    var precProduto: Float,
    var ativoProduto: Boolean,
    var qtdMinEstoque: Int,
    var nomeProduto: String,
    var idProduto: Int,
    var idCategoria: Int,
    var descProduto: String,
    var descontoPromocao: Float
)
