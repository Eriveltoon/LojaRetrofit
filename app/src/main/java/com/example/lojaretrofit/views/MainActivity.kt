package com.example.lojaretrofit.views

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import com.example.lojaretrofit.R
import com.example.lojaretrofit.api.ProdutoApi
import com.example.lojaretrofit.databinding.ActivityMainBinding
import com.example.lojaretrofit.model.Produto
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
                if(response.isSuccessful){
                    val listaProduto = response.body()

                    val nomeProduto = listaProduto?.first()?.nomeProduto

                    alert("Sucesso", "Nome do primeiro Produto: $nomeProduto")
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

    fun alert(titulo: String, msg: String){
        AlertDialog.Builder(this)
            .setTitle(titulo)
            .setMessage(msg)
            .setPositiveButton("Ok", null)
            .create()
            .show()
    }
}