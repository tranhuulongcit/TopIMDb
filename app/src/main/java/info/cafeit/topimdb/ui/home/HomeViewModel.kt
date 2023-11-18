package info.cafeit.topimdb.ui.home

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import info.cafeit.grpc.IMDbServiceGrpc
import info.cafeit.grpc.Imdb
import info.cafeit.topimdb.BuildConfig
import info.cafeit.topimdb.util.Const
import io.grpc.okhttp.OkHttpChannelBuilder
import io.reactivex.Single
import io.reactivex.SingleObserver
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import java.util.*
import kotlin.collections.ArrayList

class HomeViewModel : ViewModel() {

    val movieList = ArrayList<Movie>();
    private val _notify = MutableLiveData<Int>()
    val notify: LiveData<Int> = _notify

    fun fetchData(listener: (() -> Unit)) {
        val channel = OkHttpChannelBuilder.forAddress(BuildConfig.BASE_GRPC_HOST, BuildConfig.BASE_GRPC_PORT)
            .usePlaintext()
            .build()
        val stub = IMDbServiceGrpc.newBlockingStub(channel)

        val requestMessage = Imdb.ListIMDbRequest.newBuilder().build()

        Single.fromCallable { stub.list(requestMessage) }
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(object : SingleObserver<Imdb.IMDbResponse> {
                override fun onSuccess(response: Imdb.IMDbResponse) {
                    movieList.clear()
                    Log.d("IMDB", response.status);
                    if (Const.SUCCESS.equals(response.status)) {
                        for (index in response.dataList.indices) {
                            val imdb:Imdb.IMDb = response.dataList[index]
                            val movie = Movie()
                            movie.title = imdb.title
                            movie.description = imdb.description
                            movie.year = imdb.year
                            movie.rating = imdb.rating
                            movie.trailer = imdb.trailer
                            movie.genres = imdb.genresList
                            movie.galleries = imdb.galleriesList.toMutableList()
                            movie.stars = imdb.starsList
                            movie.directors = imdb.creatorsList
                            //movie.createAt = imdb.createAt
                            movieList.add(movie)
                            _notify.value = index
                        }


                    } else {

                    }
                    channel.shutdown()
                    listener.invoke()
                }

                override fun onSubscribe(d: Disposable) {
                    Log.d("IMDB", "subscribe")
                }

                override fun onError(e: Throwable) {
                    Log.d("IMDB", e.message.toString())
                }
            })
    }
}