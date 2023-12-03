package com.hopecoding.photoshareproject.view

import android.content.AbstractThreadedSyncAdapter
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.hopecoding.photoshareproject.model.Post
import com.hopecoding.photoshareproject.R
import com.hopecoding.photoshareproject.adapter.HaberRecyclerAdapter
import kotlinx.android.synthetic.main.activity_haberler.*

class HaberlerActivity : AppCompatActivity() {
    private lateinit var auth: FirebaseAuth
    private lateinit var database: FirebaseFirestore
    private lateinit var recyclerViewAdapter: HaberRecyclerAdapter

    var postListesi=ArrayList<Post>()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_haberler)

        auth = FirebaseAuth.getInstance()
        database = FirebaseFirestore.getInstance()


        verileriAl()

        var layoutManager=LinearLayoutManager(this)
        recyclerView.layoutManager=layoutManager
        recyclerViewAdapter= HaberRecyclerAdapter(postListesi)
        recyclerView.adapter=recyclerViewAdapter


    }

    fun verileriAl() {
        //database.collection("Post").whereEqualTo("email","atil@gmail.com") belirli email postlarını görmek için
        database.collection("Post").orderBy("tarih",Query.Direction.DESCENDING).addSnapshotListener { snapshot, exception ->
            if (exception != null) {
                Toast.makeText(this, exception.localizedMessage, Toast.LENGTH_LONG).show()
            } else {
                if (snapshot != null) {
                    if(!snapshot.isEmpty){

                        val documents=snapshot.documents
                        postListesi.clear()

                        for(document in documents){
                            val email=document.get("email")as String
                            val yorum=document.get("yorum")as String
                            val gorselUrl=document.get("gorselurl")as String

                            val indirilenPost= Post(email,yorum,gorselUrl)
                            postListesi.add(indirilenPost)
                        }

                        recyclerViewAdapter.notifyDataSetChanged()
                    }
                }

            }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        val menuInflater = menuInflater
        menuInflater.inflate(R.menu.option_menu, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == R.id.SharePhoto) {
            //fotoğraf paylaşma aktivitesine gidilecek
            val intent = Intent(this, PhotoShareActivity::class.java)
            startActivity(intent)
            //finish yazmadığımız için kullanıcı her program açıldığında bu sayfadan açılacaktır.
            //menüde quit kısmı yapıp oradan çıkartabiliriz.


        } else if (item.itemId == R.id.Quit) {
            //Quit kısmına tıkladığında olacak olanlar:
            //AnaSayfaya gidilecek ve Firebaseden çıkılacak.
            auth.signOut()
            val intent = Intent(this, KullaniciActivity::class.java)
            startActivity(intent)
            finish()
        }


        return super.onOptionsItemSelected(item)
    }
}