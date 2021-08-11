package com.harisewak.verticalvideos.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.*
import com.google.android.exoplayer2.ExoPlayer
import com.google.android.exoplayer2.SimpleExoPlayer
import com.harisewak.verticalvideos.data.Video
import com.harisewak.verticalvideos.databinding.FragmentVideoListBinding
import com.harisewak.verticalvideos.databinding.ItemVideoBinding
import com.harisewak.verticalvideos.util.load
import com.harisewak.verticalvideos.util.logd
import com.harisewak.verticalvideos.viewmodel.VideoListViewModel
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject


@AndroidEntryPoint
class VideoListFragment : Fragment() {

    private lateinit var binding: FragmentVideoListBinding

    @Inject
    lateinit var viewModel: VideoListViewModel

    private val adapter = VideoListAdapter()

    @Inject
    lateinit var player: ExoPlayer

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        binding = FragmentVideoListBinding.inflate(inflater, container, false)
        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.videoList.observe(viewLifecycleOwner) { videos ->

            binding.rvVideoList.apply {
                videoList = videos
                adapter = this@VideoListFragment.adapter
            }

        }

    }


    inner class VideoListAdapter : RecyclerView.Adapter<VideoListAdapter.VideoViewHolder>() {

        private var playFirstVideo = false

        private val diffCallback = object : DiffUtil.ItemCallback<Video>() {

            override fun areItemsTheSame(oldItem: Video, newItem: Video) = oldItem == newItem

            override fun areContentsTheSame(oldItem: Video, newItem: Video) = oldItem == newItem

        }

        private val asyncDiffer = AsyncListDiffer(this, diffCallback)

        fun submitList(list: List<Video>) = asyncDiffer.submitList(list, object : Runnable {
            override fun run() {
                playFirstVideo = true
            }

        })

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
            VideoViewHolder(
                ItemVideoBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false
                )
            )

        override fun onBindViewHolder(holder: VideoViewHolder, position: Int) =
            holder.bind(asyncDiffer.currentList[position])

        override fun getItemCount() = asyncDiffer.currentList.size

        fun getItem(index: Int): Video = asyncDiffer.currentList[index]


        inner class VideoViewHolder(val binding: ItemVideoBinding) :
            RecyclerView.ViewHolder(binding.root) {

            fun bind(video: Video) {

                logd("onBindViewHolder")

                itemView.tag = this

                with(binding) {

                    tvTitle.text = video.title
                    tvSubtitle.text = video.subtitle
                    ivThumbnail.clipToOutline = true
                    ivThumbnail.load(video.thumb)

                    container.setOnClickListener {

                        video.curPosition = player.currentPosition

                        val action = VideoListFragmentDirections
                            .actionVideoListFragmentToVideoDetailFragment(video)

                        findNavController().navigate(action)
                    }
                }
            }

        }

    }

}