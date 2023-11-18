package info.cafeit.topimdb.ui.home

import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.core.graphics.drawable.DrawableCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import info.cafeit.topimdb.R
import info.cafeit.topimdb.databinding.FragmentHomeBinding
import info.cafeit.topimdb.util.SpacesItemDecoration


class HomeFragment : Fragment(), SwipeRefreshLayout.OnRefreshListener  {

    private lateinit var homeViewModel: HomeViewModel
    private lateinit var binding: FragmentHomeBinding
    private lateinit var movieAdapter: MovieAdapter
    private lateinit var defaultItemDecoration: SpacesItemDecoration
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        homeViewModel = ViewModelProvider(this).get(HomeViewModel::class.java);
        binding  =  FragmentHomeBinding.inflate(inflater, container, false);
        binding.lifecycleOwner = viewLifecycleOwner;
        return binding.root;
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.refreshListMovie.setOnRefreshListener(this)
        binding.refreshListMovie.setColorSchemeColors(resources.getColor(R.color.blue_500,null))

        movieAdapter = MovieAdapter(homeViewModel.movieList);
        binding.recycleListMovie.apply {
            layoutManager = GridLayoutManager(context, 2);
            defaultItemDecoration = SpacesItemDecoration(20, 2)
            addItemDecoration(defaultItemDecoration);
            itemAnimator = DefaultItemAnimator();
            adapter = movieAdapter

        }

        homeViewModel.notify.observe(viewLifecycleOwner, Observer {
            if (it != null) {
                binding.recycleListMovie.adapter?.notifyItemChanged(it)
            } else {
                binding.recycleListMovie.adapter?.notifyDataSetChanged()
            }
        });

        homeViewModel.fetchData {  }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == R.id.action_settings) {
            binding.recycleListMovie.apply {
                movieAdapter.switchViewGrid = !movieAdapter.switchViewGrid
                layoutManager = if (movieAdapter.switchViewGrid) GridLayoutManager(context, 2) else LinearLayoutManager(context)

                removeItemDecoration(defaultItemDecoration);
                defaultItemDecoration = SpacesItemDecoration(if (movieAdapter.switchViewGrid) 20 else 40, if (movieAdapter.switchViewGrid) 2 else 1)
                addItemDecoration(defaultItemDecoration);

                adapter?.notifyDataSetChanged()
                item.icon = ContextCompat.getDrawable(context, if (!movieAdapter.switchViewGrid) R.drawable.view_grid else R.drawable.view_list)
            }
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onRefresh() {
        homeViewModel.fetchData {
            binding.refreshListMovie.isRefreshing = false
        }
    }
}