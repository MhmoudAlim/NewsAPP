package com.mahmoudalim.mvvmnewsapp.ui.db

import androidx.room.TypeConverter
import com.mahmoudalim.mvvmnewsapp.ui.models.Source

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