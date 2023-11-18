package info.cafeit.topimdb.ui.detail

import android.app.AlertDialog
import android.graphics.Color
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.core.graphics.drawable.DrawableCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.Navigation
import com.google.android.material.chip.Chip
import com.google.android.material.tabs.TabLayoutMediator
import com.google.android.youtube.player.YouTubeInitializationResult
import com.google.android.youtube.player.YouTubePlayer
import com.google.android.youtube.player.YouTubePlayerSupportFragmentX
import info.cafeit.topimdb.BuildConfig
import info.cafeit.topimdb.MainActivity
import info.cafeit.topimdb.R
import info.cafeit.topimdb.databinding.FragmentDetailBinding
import info.cafeit.topimdb.ui.home.Movie
import info.cafeit.topimdb.ui.home.MovieAdapter


class DetailFragment : Fragment() {

    private lateinit var detailViewModel: DetailViewModel
    private lateinit var binding: FragmentDetailBinding
    private lateinit var movieBundle: Movie
    private lateinit var handlerNextPage: Handler
    private lateinit var runNextPage: Runnable


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        with(arguments) {
            movieBundle = this?.getParcelable(MovieAdapter.DATA)!!
        }
    }

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        detailViewModel = ViewModelProvider(this).get(DetailViewModel::class.java)
        binding = FragmentDetailBinding.inflate(inflater, container, false)
        binding.lifecycleOwner = viewLifecycleOwner
        binding.movieModel = movieBundle
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.toolbar.setNavigationOnClickListener( {
            Navigation.findNavController(it).navigateUp()
        })

        binding.viewPagerGallery.adapter = movieBundle.galleries?.let { GalleriesAdapter(it) }
        TabLayoutMediator(binding.indicator, binding.viewPagerGallery) { tab, position ->
        }.attach()


        handleAutoScrollViewPager()

        movieBundle.apply {
            directors?.forEach {
                binding.groupContent.directorGroup.addView(createChip(it))
            }

            genres?.forEach {
                binding.groupContent.genresGroup.addView(createChip(it))
            }

            stars?.forEach {
                binding.groupContent.starGroup.addView(createChip(it))
            }
        }

        loadVideo()
    }

    fun createChip(content:String) : Chip {
        val chip = Chip(context)
        chip.text = content
        chip.setChipBackgroundColorResource(R.color.blue_700)
        chip.isCloseIconVisible = false
        chip.setTextColor(resources.getColor(R.color.white))
        return chip
    }

    fun handleAutoScrollViewPager() {
        handlerNextPage = Handler(Looper.getMainLooper())

        if (movieBundle.galleries != null && movieBundle.galleries!!.size > 1) {
            runNextPage = Runnable {
                if (binding.viewPagerGallery.currentItem < movieBundle.galleries!!.size - 1) {
                    binding.viewPagerGallery.currentItem += 1
                } else {
                    binding.viewPagerGallery.currentItem = 0
                }
                handlerNextPage.postDelayed(runNextPage, 10000);
            }
            handlerNextPage.postDelayed(runNextPage, 10000);
        }
    }

    fun loadVideo() {
        val youTubePlayerFragment = YouTubePlayerSupportFragmentX.newInstance()
        val transaction: FragmentTransaction = childFragmentManager.beginTransaction()
        transaction.replace(R.id.frameYoutubeLoader, youTubePlayerFragment).commit()
        try {
            youTubePlayerFragment.initialize(BuildConfig.YOUTUBE_API_SECRET, object : YouTubePlayer.OnInitializedListener {
                override fun onInitializationSuccess(
                    p0: YouTubePlayer.Provider?,
                    player: YouTubePlayer?,
                    p2: Boolean
                ) {
                    player?.loadVideo(movieBundle.trailer?.split("v=")!![1])
                    Log.e("YOUTUBE", "init success!")
                }

                override fun onInitializationFailure(
                    p0: YouTubePlayer.Provider?,
                    p1: YouTubeInitializationResult?
                ) {
                    Log.e("YOUTUBE", "init error!")
                }

            })
        } catch (ex: Exception) {
            val alertDialog =
                AlertDialog.Builder(this.context).create()
            alertDialog.setMessage(ex.message)
            alertDialog.setButton(
                AlertDialog.BUTTON_NEUTRAL, "OK"
            ) { dialog, which -> dialog.dismiss() }
            alertDialog.show()
        }
    }

    override fun onResume() {
        super.onResume()
        (activity as MainActivity).hideActionBar()
        context?.let {
            val backArrow = ContextCompat.getDrawable(it, androidx.appcompat.R.drawable.abc_ic_ab_back_material)
            backArrow?.let { it1 -> DrawableCompat.setTint(it1, Color.WHITE) }
            binding.toolbar.navigationIcon = backArrow
        }
    }

    override fun onStop() {
        super.onStop()
        (activity as MainActivity).showActionBar()
        handlerNextPage.removeCallbacks(runNextPage)
    }
}