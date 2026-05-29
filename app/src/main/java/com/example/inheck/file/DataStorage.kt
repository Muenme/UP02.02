package com.example.inheck.file

import android.content.Context
import com.example.inheck.data.entity.Buy
import com.example.inheck.data.entity.Participant
import com.example.inheck.data.entity.Product
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.google.gson.JsonDeserializationContext
import com.google.gson.JsonDeserializer
import com.google.gson.JsonElement
import com.google.gson.JsonPrimitive
import com.google.gson.JsonSerializationContext
import com.google.gson.reflect.TypeToken
import com.google.gson.JsonSerializer
import java.io.File
import java.lang.reflect.Type
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

// Адаптер для LocalDateTime (Gson не умеет его сериализовать сам)
class LocalDateTimeAdapter : JsonSerializer<LocalDateTime>, JsonDeserializer<LocalDateTime> {

    private val formatter = DateTimeFormatter.ISO_LOCAL_DATE_TIME

    override fun serialize(
        src: LocalDateTime,
        typeOfSrc: Type,
        context: JsonSerializationContext
    ): JsonElement {
        return JsonPrimitive(formatter.format(src))
    }

    override fun deserialize(
        json: JsonElement,
        typeOfT: Type,
        context: JsonDeserializationContext
    ): LocalDateTime {
        return LocalDateTime.parse(json.asString, formatter)
    }
}

// Главный класс для работы с файлом
class DataStorage(private val context: Context) {

    private val gson: Gson = GsonBuilder()
        .registerTypeAdapter(LocalDateTime::class.java, LocalDateTimeAdapter())
        .setPrettyPrinting()
        .create()

    // Имена файлов
    private val BUYS_FILE = "buys.json"
    private val PRODUCTS_FILE = "products.json"
    private val PARTICIPANTS_FILE = "participants.json"

    //ВСПОМОГАТЕЛЬНЫЕ

    // Добавляем генератор id
    private val idGenerator = IdGenerator(context)

    private fun getFile(fileName: String): File {
        return File(context.filesDir, fileName)
    }

    private fun readFile(fileName: String): String {
        val file = getFile(fileName)
        return if (file.exists()) file.readText() else "[]"
    }

    private fun writeFile(fileName: String, content: String) {
        getFile(fileName).writeText(content)
    }

    //PARTICIPANTS

    fun saveParticipants(participants: List<Participant>) {
        val json = gson.toJson(participants)
        writeFile(PARTICIPANTS_FILE, json)
    }

    fun loadParticipants(): List<Participant> {
        val json = readFile(PARTICIPANTS_FILE)
        val type = object : TypeToken<List<Participant>>() {}.type
        return try {
            gson.fromJson(json, type) ?: emptyList()
        } catch (e: Exception) {
            emptyList()
        }
    }

    fun addParticipant(participant: Participant) {
        val list = loadParticipants().toMutableList()
        val exists = list.any { it.name == participant.name }

        if (!exists) {
            // Автонумерация через IdGenerator
            val newParticipant = participant.copy(
                id = idGenerator.nextId(IdGenerator.PARTICIPANT)
            )
            list.add(newParticipant)
            saveParticipants(list)
        }

    }

    fun deleteParticipant(id: Int) {
        val list = loadParticipants().filter { it.id != id }
        saveParticipants(list)
    }

    //PRODUCTS

    fun saveProducts(products: List<Product>) {
        val json = gson.toJson(products)
        writeFile(PRODUCTS_FILE, json)
    }

    fun loadProducts(): List<Product> {
        val json = readFile(PRODUCTS_FILE)
        val type = object : TypeToken<List<Product>>() {}.type
        return try {
            gson.fromJson(json, type) ?: emptyList()
        } catch (e: Exception) {
            emptyList()
        }
    }

    fun addProduct(product: Product): Product {
        val list = loadProducts().toMutableList()
        // Автонумерация через IdGenerator
        val newProduct = product.copy(
            id = idGenerator.nextId(IdGenerator.PRODUCT)
        )
        list.add(newProduct)
        saveProducts(list)
        return newProduct
    }

    fun updateProduct(product: Product) {
        val list = loadProducts().toMutableList()
        val index = list.indexOfFirst { it.id == product.id }
        if (index >= 0) {
            list[index] = product
            saveProducts(list)
        }
    }

    fun deleteProduct(id: Int) {
        val list = loadProducts().filter { it.id != id }
        saveProducts(list)
    }

    //BUYS

    fun saveBuys(buys: List<Buy>) {
        val json = gson.toJson(buys)
        writeFile(BUYS_FILE, json)
    }

    fun loadBuys(): List<Buy> {
        val json = readFile(BUYS_FILE)
        val type = object : TypeToken<List<Buy>>() {}.type
        return try {
            gson.fromJson(json, type) ?: emptyList()
        } catch (e: Exception) {
            emptyList()
        }
    }

    fun addBuy(buy: Buy, products: List<Product>): Buy {
        // Сохраняем продукты и получаем их id
        val savedProducts = products.map { addProduct(it) }
        val productIds = savedProducts.map { it.id }

        val list = loadBuys().toMutableList()

        // Автонумерация через IdGenerator
        val newBuy = buy.copy(
            id = idGenerator.nextId(IdGenerator.BUY),
            productId = productIds
        )

        list.add(newBuy)
        saveBuys(list)
        return newBuy
    }

    fun updateBuy(buy: Buy, products: List<Product>): Buy {
        // Удаляем старые продукты
        val oldBuy = loadBuys().find { it.id == buy.id }
        oldBuy?.productId?.forEach { deleteProduct(it) }

        // Сохраняем новые продукты
        val savedProducts = products.map { addProduct(it) }
        val productIds = savedProducts.map { it.id }

        // Обновляем покупку
        val list = loadBuys().toMutableList()
        val index = list.indexOfFirst { it.id == buy.id }

        val updatedBuy = buy.copy(productId = productIds)

        if (index >= 0) {
            list[index] = updatedBuy
        }

        saveBuys(list)
        return updatedBuy
    }

    fun deleteBuy(id: Int) {
        val list = loadBuys().filter { it.id != id }
        saveBuys(list)
    }

    //ПОЛУЧИТЬ ПРОДУКТЫ ПО BUY

    fun getProductsForBuy(buy: Buy): List<Product> {
        val allProducts = loadProducts()
        return allProducts.filter { it.id in buy.productId }
    }
}