package org.wit.woodland.models

import android.content.Context
import android.net.Uri
import com.google.gson.*
import com.google.gson.reflect.TypeToken
import org.wit.woodland.helpers.*
import timber.log.Timber
import java.lang.reflect.Type
import java.util.*

const val JSON_FILE = "woodlands.json"
val gsonBuilder: Gson = GsonBuilder().setPrettyPrinting()
                 .registerTypeAdapter(Uri::class.java, UriParser())
                 .create()
val listType: Type = object : TypeToken<ArrayList<WoodlandModel>>() {}.type

fun generateRandomId(): Long {
    return Random().nextLong()
}

class WoodlandJSONStore(private val context: Context) : WoodlandStore {

    var woodlands = mutableListOf<WoodlandModel>()

    init {
        if (exists(context, JSON_FILE)) {
            deserialize()
        }
    }

    override fun findAll(): MutableList<WoodlandModel> {
        logAll()
        return woodlands
    }

    override fun create(woodland: WoodlandModel) {
        woodland.id = generateRandomId()
        woodlands.add(woodland)
        serialize()
    }


    override fun update(woodland: WoodlandModel) {
        val woodlandsList = findAll() as ArrayList<WoodlandModel>
        var foundWoodland: WoodlandModel? = woodlandsList.find { p -> p.id == woodland.id }
        if (foundWoodland != null) {
            foundWoodland.title = woodland.title
            foundWoodland.description = woodland.description
            foundWoodland.image = woodland.image
            foundWoodland.lat = woodland.lat
            foundWoodland.lng = woodland.lng
            foundWoodland.zoom = woodland.zoom
        }
        serialize()
    }

    override fun delete(woodland: WoodlandModel) {
        woodlands.remove(woodland)
        serialize()
    }

    private fun serialize() {
        val jsonString = gsonBuilder.toJson(woodlands, listType)
        write(context, JSON_FILE, jsonString)
    }

    private fun deserialize() {
        val jsonString = read(context, JSON_FILE)
        woodlands = gsonBuilder.fromJson(jsonString, listType)
    }

    private fun logAll() {
        woodlands.forEach { Timber.i("$it") }
    }
}

class UriParser : JsonDeserializer<Uri>,JsonSerializer<Uri> {
    override fun deserialize(
        json: JsonElement?,
        typeOfT: Type?,
        context: JsonDeserializationContext?
    ): Uri {
        return Uri.parse(json?.asString)
    }

    override fun serialize(
        src: Uri?,
        typeOfSrc: Type?,
        context: JsonSerializationContext?
    ): JsonElement {
        return JsonPrimitive(src.toString())
    }
}