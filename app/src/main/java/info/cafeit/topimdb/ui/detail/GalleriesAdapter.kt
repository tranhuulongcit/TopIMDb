package info.cafeit.topimdb.ui.detail

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.CircularProgressDrawable
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import info.cafeit.topimdb.R
import info.cafeit.topimdb.databinding.ItemGalleryBinding

class GalleriesAdapter(val galleries:List<String>) : RecyclerView.Adapter<GalleriesAdapter.GalleriesVH>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GalleriesVH =
        GalleriesVH(ItemGalleryBinding.inflate(LayoutInflater.from(parent.context), parent, false))

    override fun getItemCount(): Int = galleries.size

    override fun onBindViewHolder(holder: GalleriesVH, position: Int): Unit = holder.binding.root.run {
        val circularProgressDrawable = CircularProgressDrawable(context)
        circularProgressDrawable.strokeWidth = 5f
        circularProgressDrawable.centerRadius = 30f
        circularProgressDrawable.setColorSchemeColors(context.getColor(R.color.blue_500))
        circularProgressDrawable.start()
        Glide.with(context)
            .load(galleries[position])
            .apply(RequestOptions.centerCropTransform().placeholder(circularProgressDrawable))
            .into(this as ImageView)
    }

    inner class GalleriesVH(val binding: ItemGalleryBinding) : RecyclerView.ViewHolder(binding.root)
}

