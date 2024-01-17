package com.example.newsapp.data.db.converter

import androidx.room.TypeConverter
import androidx.room.TypeConverters
import com.example.newsapp.data.model.Source

class SourceTypeConverter {
    @TypeConverter
    fun fromSource(source: Source): String{
        return source.id + "&" + source.name
    }

    @TypeConverter
    fun toSource(str: String): Source{
        val sourceStringArray = str.split("&")
        return Source(sourceStringArray[0], sourceStringArray[1])
    }
}