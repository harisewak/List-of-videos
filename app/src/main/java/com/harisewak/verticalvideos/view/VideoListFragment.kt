package com.harisewak.verticalvideos.view

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.harisewak.verticalvideos.data.Video
import com.harisewak.verticalvideos.databinding.FragmentVideoListBinding
import com.harisewak.verticalvideos.databinding.ItemVideoBinding
import com.harisewak.verticalvideos.viewmodel.VideoListViewModel
import com.squareup.picasso.Picasso
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class VideoListFragment : Fragment() {

    private lateinit var binding: FragmentVideoListBinding

    @Inject
    lateinit var viewModel: VideoListViewModel

    private val adapter = VideoListAdapter()


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        binding = FragmentVideoListBinding.inflate(inflater, container, false)
        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.rvVideoList.adapter = adapter

        viewModel.videoList.observe(viewLifecycleOwner) {
            adapter.submitList(it)
        }
    }


    inner class VideoListAdapter : RecyclerView.Adapter<VideoListAdapter.VideoViewHolder>() {

        private val diffCallback = object : DiffUtil.ItemCallback<Video>() {

            override fun areItemsTheSame(oldItem: Video, newItem: Video) = oldItem == newItem

            override fun areContentsTheSame(oldItem: Video, newItem: Video) = oldItem == newItem

        }

        private val asyncDiffer = AsyncListDiffer(this, diffCallback)

        fun submitList(list: List<Video>) = asyncDiffer.submitList(list)

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
            VideoViewHolder(ItemVideoBinding.inflate(LayoutInflater.from(parent.context)))

        override fun onBindViewHolder(holder: VideoViewHolder, position: Int) =
            holder.bind(asyncDiffer.currentList[position])

        override fun getItemCount() = asyncDiffer.currentList.size


        inner class VideoViewHolder(private val binding: ItemVideoBinding) :
            RecyclerView.ViewHolder(binding.root) {

            fun bind(video: Video) {
                binding.tvTitle.text = video.title
                binding.tvSubtitle.text = video.subtitle
                binding.root.setOnClickListener {
                    val action =
                        VideoListFragmentDirections.actionVideoListFragmentToVideoDetailFragment(
                            video
                        )
                    findNavController()
                        .navigate(
                            action
                        )
                }
            }

        }

    }

}