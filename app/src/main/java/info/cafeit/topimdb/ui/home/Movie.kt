package info.cafeit.topimdb.ui.home

import android.os.Parcel
import android.os.Parcelable
import java.io.Serializable
import java.util.*
import kotlin.collections.ArrayList

class Movie : Serializable, Parcelable {
    var id: Int? = null
    var slug: String? = null
    var title: String? = null
    var description: String? = null
    var year = 0
    var runningTime: String? = null
    var rating = 0f
    var genres: MutableList<String>? = null
    var galleries: MutableList<String>? = null
    var stars: MutableList<String>? = null
    var directors: MutableList<String>? = null
    var trailer: String? = null
    var createAt: Date? = null

    override fun describeContents(): Int {
        return 0
    }

    override fun writeToParcel(dest: Parcel, flags: Int) {
        dest.writeValue(id)
        dest.writeString(slug)
        dest.writeString(title)
        dest.writeString(description)
        dest.writeInt(year)
        dest.writeString(runningTime)
        dest.writeFloat(rating)
        dest.writeStringList(genres)
        dest.writeStringList(galleries)
        dest.writeStringList(stars)
        dest.writeStringList(directors)
        dest.writeString(trailer)
        dest.writeLong(if (createAt != null) createAt!!.time else -1)
    }

    fun readFromParcel(source: Parcel) {
        id = source.readValue(Int::class.java.classLoader) as Int?
        slug = source.readString()
        title = source.readString()
        description = source.readString()
        year = source.readInt()
        rating = source.readFloat()
        runningTime = source.readString()
        genres = source.createStringArrayList()
        galleries = source.createStringArrayList()
        stars = source.createStringArrayList()
        directors = source.createStringArrayList()
        trailer = source.readString()
        val tmpCreateAt = source.readLong()
        createAt = if (tmpCreateAt == -1L) null else Date(tmpCreateAt)
    }

    constructor() {}
    protected constructor(`in`: Parcel) {
        id = `in`.readValue(Int::class.java.classLoader) as Int?
        slug = `in`.readString()
        title = `in`.readString()
        description = `in`.readString()
        year = `in`.readInt()
        runningTime = `in`.readString()
        rating = `in`.readFloat()
        genres = `in`.createStringArrayList()
        galleries = `in`.createStringArrayList()
        stars = `in`.createStringArrayList()
        directors = `in`.createStringArrayList()
        trailer = `in`.readString()
        val tmpCreateAt = `in`.readLong()
        createAt = if (tmpCreateAt == -1L) null else Date(tmpCreateAt)
    }

    companion object {
        val CREATOR: Parcelable.Creator<Movie?> =
            object : Parcelable.Creator<Movie?> {
                override fun createFromParcel(source: Parcel): Movie? {
                    return Movie(source)
                }

                override fun newArray(size: Int): Array<Movie?> {
                    return arrayOfNulls(size)
                }
            }
    }
}