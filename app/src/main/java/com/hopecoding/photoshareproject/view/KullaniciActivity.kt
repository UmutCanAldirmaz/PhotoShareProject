package com.hopecoding.photoshareproject.view

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.hopecoding.photoshareproject.R
import kotlinx.android.synthetic.main.activity_main.*

class KullaniciActivity : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        auth= FirebaseAuth.getInstance()

        val guncelKullanici=auth.currentUser
        if(guncelKullanici!=null){
            val intent=Intent(this, HaberlerActivity::class.java)
            startActivity(intent)
            finish()

        }

    }


    fun girisYap(view:View){
        auth.signInWithEmailAndPassword(emailText.text.toString(),passwordText.text.toString()).addOnCompleteListener{task ->
            if(task.isSuccessful){
                val guncelKullanici=auth.currentUser?.email.toString()
                Toast.makeText(this,"Hoşgeldiniz: ${guncelKullanici}",Toast.LENGTH_LONG).show()

                val intent=Intent(this, HaberlerActivity::class.java)
                startActivity(intent)
                finish()
            }
        }.addOnFailureListener { exception ->
            Toast.makeText(this,exception.localizedMessage,Toast.LENGTH_LONG).show()
        }

    }

    fun kayitOl(view:View){

        val email=emailText.text.toString()
        val sifre=passwordText.text.toString()
        auth.createUserWithEmailAndPassword(email,sifre).addOnCompleteListener { task ->
            if(task.isSuccessful){
                //Kayıt olundu mesajı verelim
                Toast.makeText(this,"Kayıtınız gerçekleşmiştir.Kullanıcı adı: ${email}",Toast.LENGTH_LONG).show()
            }
        }.addOnFailureListener { exception ->
            Toast.makeText(this,exception.localizedMessage,Toast.LENGTH_LONG).show()
        }


    }
}