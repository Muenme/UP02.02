package com.example.inheck.file

import android.content.Context

class IdGenerator(context: Context) {

    // Используем SharedPreferences - это простое хранилище ключ-значение
    private val prefs = context.getSharedPreferences("id_counters", Context.MODE_PRIVATE)

    // Получить следующий id для любой сущности
    fun nextId(entityName: String): Int {
        val currentId = prefs.getInt(entityName, 0) + 1
        prefs.edit().putInt(entityName, currentId).apply()
        return currentId
    }

    // Посмотреть текущий id без увеличения
    fun currentId(entityName: String): Int {
        return prefs.getInt(entityName, 0)
    }

    // Сбросить счётчик (если нужно)
    fun reset(entityName: String) {
        prefs.edit().putInt(entityName, 0).apply()
    }

    companion object {
        // Названия счётчиков
        const val BUY = "buy"
        const val PRODUCT = "product"
        const val PARTICIPANT = "participant"
    }
}