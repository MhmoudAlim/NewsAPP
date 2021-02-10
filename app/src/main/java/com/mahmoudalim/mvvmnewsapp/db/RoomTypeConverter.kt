package com.mahmoudalim.mvvmnewsapp.db

import androidx.room.TypeConverter
import com.mahmoudalim.mvvmnewsapp.models.Source

class RoomTypeConverter {

    @TypeConverter
    fun fromSource(source: Source): String {
        return source.name
    }

    @TypeConverter
    fun toSource(name: String): Source {
        return Source(name, name)
    }
}