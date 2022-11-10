package com.example.lojaretrofit.views

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import com.example.lojaretrofit.R
import com.example.lojaretrofit.api.ProdutoApi
import com.example.lojaretrofit.databinding.ActivityMainBinding
import com.example.lojaretrofit.databinding.ItemProdutoBinding
import com.example.lojaretrofit.model.Produto
import com.squareup.picasso.Picasso
import retrofit2.*
import retrofit2.converter.gson.GsonConverterFactory

class MainActivity : AppCompatActivity() {
    lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        chamarApiListProd()
    }

    fun chamarApiListProd(){
        //1 - Criar uma instancia do Reterofit
        val retrofit = Retrofit.Builder()
            .baseUrl("https://oficinacordova.azurewebsites.net")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
        
        //2 - Criar uma instancia do servico
        val service = retrofit.create(ProdutoApi::class.java)

        //3 - Criar uma chamada (Mas nao faz a chamada na API)
        val chamada = service.listar()

        //4 - Definir um Callback de retorno
        //Tipo do callback tem que ser igual do Arquivo ProdutoApi
        val callback = object : Callback<List<Produto>>{
            //Chegou no back-end, teve uma resposta do back-end mas pode ser sucesso ou erro
            override fun onResponse(call: Call<List<Produto>>, response: Response<List<Produto>>) {
                //isSuccessful quando retorna 200 do laravel, verificacao no laravel para o cod de retorno
                if(response.isSuccessful){


                    val listaProduto = response.body()

                    //nao precisa mais das 2 linhas abaixo pq ja fez de forma dinamica
                    //val nomeProduto = listaProduto?.first()?.nomeProduto

                    //alert("Sucesso", "Nome do primeiro Produto: $nomeProduto")

                    mostrarProdutos(listaProduto)
                }else{
                    alert("Erro",response.code().toString())
                }
            }

            //Nem chegou a bater no back-end
            override fun onFailure(call: Call<List<Produto>>, t: Throwable) {
                //t.message.toString() captura msg de erro
                alert("Erro", t.message.toString())
            }

        }
        //5 - Executar a chamada
        //enqueue equivalente subscribe (Angular)
        chamada.enqueue(callback)
    }

    //Funcao dos elementos dinamicos
    fun mostrarProdutos(listaProdutos: List<Produto>?)
    {
        //0 - Iterar (passar) pelos produtos
        //substitui o it = nome ->
        listaProdutos?.forEach {

            //1 - Inflar o layout do item da lista
            //chamando o XML item_produto
            val itemProduto = ItemProdutoBinding.inflate(layoutInflater)

            //2 - Configurar as views(componentes) com os dados do backend
            itemProduto.textNome.text = it.nomeProduto
            itemProduto.textPreco.text = it.precProduto.toString()

            //2.5 - Obter imagem do item
            //utilizar biblioteca Picasso
            Picasso.get().load("https://oficinacordova.azurewebsites.net/android/rest/produto/image/${it.idProduto}").into(itemProduto.imageView)

            //3 - Adicionar o layout no container
            //itemProduto.root pega a tela que esta dentro do binding
            binding.container.addView(itemProduto.root)
        }
    }

    fun alert(titulo: String, msg: String){
        AlertDialog.Builder(this)
            .setTitle(titulo)
            .setMessage(msg)
            .setPositiveButton("Ok", null)
            .create()
            .show()
    }
}