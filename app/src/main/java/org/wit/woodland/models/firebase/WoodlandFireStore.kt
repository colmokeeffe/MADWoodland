package org.wit.woodland.models.firebase

import android.content.Context
import android.graphics.Bitmap
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import org.jetbrains.anko.AnkoLogger
import org.wit.woodland.helpers.readImageFromPath
import org.wit.woodland.models.WoodlandModel
import org.wit.woodland.models.WoodlandStore
import java.io.ByteArrayOutputStream
import java.io.File

class WoodlandFireStore(val context: Context) : WoodlandStore, AnkoLogger {

    val woodlands = ArrayList<WoodlandModel>()
    lateinit var userId: String
    lateinit var db: DatabaseReference
    lateinit var st: StorageReference

    override fun findAll(): List<WoodlandModel> {
        return woodlands
    }

    override fun findSearchResults(query: String): List<WoodlandModel> {
        val results = woodlands.filter{p-> p.title.contains(query)}
        return results
    }

    override fun findFavourites(): List<WoodlandModel> {
        val favourites = woodlands.filter{p-> p.favourite == true}
        return favourites
    }


    override fun setFavourite(woodland: WoodlandModel) {
        var foundWoodland: WoodlandModel? = woodlands.find { p -> p.fbId == woodland.fbId }
        if (foundWoodland != null) {
            foundWoodland.favourite = woodland.favourite
        }
        db.child("users").child(userId).child("woodlands").child(woodland.fbId).setValue(woodland)
    }

    override fun countVisited(): Int {
        var count = 0
        for(woodland in woodlands){
            if(woodland.visited ==true){
                count ++
            }
        }
        return count
    }

    override fun countConifer(): Int {
        var count = 0
        for(woodland in woodlands){
            if(woodland.rb_conifer ==true){
                count ++
            }
        }
        return count
    }

    override fun countMixed(): Int {
        var count = 0
        for(woodland in woodlands){
            if(woodland.rb_mixed ==true){
                count ++
            }
        }
        return count
    }

    override fun countBroadleaf(): Int {
        var count = 0
        for(woodland in woodlands){
            if(woodland.rb_broadleaf ==true){
                count ++
            }
        }
        return count
    }

    override fun create(woodland: WoodlandModel) {
        val key = db.child("users").child(userId).child("woodlands").push().key
        key?.let {
            woodland.fbId = key

            woodland.images.forEach{
                if ((it.length) > 0 && (it[0] != 'h')) {
                    updateImage(woodland, it)
                }
            }
            woodlands.add(woodland)
            db.child("users").child(userId).child("woodlands").child(key).setValue(woodland)
        }
    }

    override fun findByFbId(id: String): WoodlandModel? {
        val foundWoodland: WoodlandModel? = woodlands.find { p -> p.fbId === id}
        return foundWoodland
    }

    override fun update(woodland: WoodlandModel) {
        var foundWoodland: WoodlandModel? = woodlands.find { p -> p.fbId == woodland.fbId }
        if (foundWoodland != null) {
            foundWoodland.title = woodland.title
            foundWoodland.description = woodland.description
            foundWoodland.images = woodland.images
            foundWoodland.rating = woodland.rating
            foundWoodland.location = woodland.location
            foundWoodland.notes = woodland.notes
            foundWoodland.visited = woodland.visited
            foundWoodland.carpark = woodland.carpark
            foundWoodland.shop = woodland.shop
            foundWoodland.rb_conifer = woodland.rb_conifer
            foundWoodland.rb_mixed = woodland.rb_mixed
            foundWoodland.rb_broadleaf = woodland.rb_broadleaf
            foundWoodland.items = woodland.items
            foundWoodland.toilets = woodland.toilets
            foundWoodland.date = woodland.date
        }
        woodland.images.forEach{
            if ((it.length) > 0 && (it[0] != 'h')) {
                updateImage(woodland, it)
            }
        }
        db.child("users").child(userId).child("woodlands").child(woodland.fbId).setValue(woodland)
    }

    override fun delete(woodland: WoodlandModel) {
        db.child("users").child(userId).child("woodlands").child(woodland.fbId).removeValue()
        woodlands.remove(woodland)
    }


    override fun clear() {
        woodlands.clear()
    }

    fun fetchWoodlands(woodlandsReady: () -> Unit) {
        val valueEventListener = object : ValueEventListener {
            override fun onCancelled(dataSnapshot: DatabaseError) {
            }
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                dataSnapshot.children.mapNotNullTo(woodlands) { it.getValue<WoodlandModel>(WoodlandModel::class.java) }
                woodlandsReady()
            }
        }
        userId = FirebaseAuth.getInstance().currentUser!!.uid
        db = FirebaseDatabase.getInstance().reference
        st = FirebaseStorage.getInstance().reference
        woodlands.clear()
        db.child("users").child(userId).child("woodlands").addListenerForSingleValueEvent(valueEventListener)
    }





    fun updateImage(woodland: WoodlandModel, image:String) {
        var index = woodland.images.indexOf(image)
        if (image != "") {
            val fileName = File(image)
            val imageName = fileName.getName()

            var imageRef = st.child(userId + '/' + imageName)
            val baos = ByteArrayOutputStream()
            val bitmap = readImageFromPath(context, image)

            bitmap?.let {
                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos)
                val data = baos.toByteArray()
                val uploadTask = imageRef.putBytes(data)
                uploadTask.addOnFailureListener {
                    println(it.message)
                }.addOnSuccessListener { taskSnapshot ->
                    taskSnapshot.metadata!!.reference!!.downloadUrl.addOnSuccessListener {
                        woodland.images[index] = it.toString()
                        db.child("users").child(userId).child("woodlands").child(woodland.fbId).setValue(woodland)
                    }
                }
            }
        }
    }
}
