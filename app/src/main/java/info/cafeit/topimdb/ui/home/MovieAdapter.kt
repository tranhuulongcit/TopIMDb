package info.cafeit.topimdb.ui.home

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.navigation.Navigation
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.CircularProgressDrawable
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import info.cafeit.topimdb.R
import info.cafeit.topimdb.databinding.ItemMovieBinding
import info.cafeit.topimdb.ui.home.MovieAdapter.MovieViewHoder


class MovieAdapter(private val movieList: List<Movie>) :
    RecyclerView.Adapter<MovieViewHoder>() {

    companion object {
        val DATA = "DATA"
        val LIST_VIEW = 1
        val GRID_VIEW = 2
    }

    var switchViewGrid = true

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MovieViewHoder {
        val binding = ItemMovieBinding.inflate(LayoutInflater.from(parent.context), parent, false);
        val layoutParams: ViewGroup.LayoutParams = binding.root.getLayoutParams()

        layoutParams.height = if (viewType == GRID_VIEW) (parent.width * 0.8).toInt() else (parent.width * 1.2).toInt()
        binding.root.setLayoutParams(layoutParams)
        return MovieViewHoder(binding)
    }

    override fun onBindViewHolder(holder: MovieViewHoder, position: Int) {
        holder.bind(movieList[position])
    }

    override fun getItemCount(): Int {
        return movieList.size
    }

    override fun getItemViewType(position: Int): Int {
        return if (switchViewGrid) GRID_VIEW else LIST_VIEW
    }

    inner class MovieViewHoder(val binding: ItemMovieBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(movie: Movie) {

            val circularProgressDrawable = CircularProgressDrawable(binding.root.context)
            circularProgressDrawable.strokeWidth = 5f
            circularProgressDrawable.centerRadius = 30f
            circularProgressDrawable.setColorSchemeColors(binding.root.context.getColor(R.color.blue_500))
            circularProgressDrawable.start()

            Glide.with(binding.imgBanner.context)
                    .load(movie.galleries!![0])
                    .apply(RequestOptions.centerCropTransform().placeholder(circularProgressDrawable))
                    .into(binding.imgBanner)
            binding.title.setText(movie.title)
            binding.rating.rating = movie.rating
            binding.movieItem.setOnClickListener {
                val bundle = Bundle()
                bundle.putParcelable(DATA, movie)
                Navigation.findNavController(it).navigate(R.id.nav_detail, bundle)
            }
        }

    }

}