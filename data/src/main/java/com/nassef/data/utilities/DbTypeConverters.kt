package com.nassef.data.utilities

import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.nassef.domain.entities.Source
import java.lang.reflect.Type

class DbTypeConverters {
    @TypeConverter
    fun fromSource(source: Source) : String= Gson().toJson(source)

//    @TypeConverter
//    fun toSource(sourceJson: String) : Source = Gson().fromJson(sourceJson , Source::class.java)

    @TypeConverter
    fun toSource(sourceJson: String) : Source {
        // 💡 FIX: Use TypeToken to get the correct Type object for Gson
        val type: Type = object : TypeToken<Source>() {}.type

        return Gson().fromJson(sourceJson, type)
    }
}