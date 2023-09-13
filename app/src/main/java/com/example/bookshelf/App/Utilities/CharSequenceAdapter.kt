package com.example.bookshelf.App.Utilities

import com.google.gson.TypeAdapter
import com.google.gson.stream.JsonReader
import com.google.gson.stream.JsonWriter

class CharSequenceAdapter: TypeAdapter<CharSequence>() {
    override fun write(out: JsonWriter, value: CharSequence?) {
        if (value == null) {
            out.nullValue()
        } else {
            out.value(value.toString())
        }
    }

    override fun read(`in`: JsonReader): CharSequence {
        return `in`.nextString()
    }
}