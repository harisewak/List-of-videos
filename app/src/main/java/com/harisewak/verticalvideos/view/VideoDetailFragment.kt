package com.harisewak.verticalvideos.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.navArgs
import com.google.android.exoplayer2.MediaItem
import com.google.android.exoplayer2.SimpleExoPlayer
import com.harisewak.verticalvideos.databinding.FragmentVideoDetailBinding
import com.harisewak.verticalvideos.viewmodel.VideoListViewModel
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class VideoDetailFragment : Fragment() {

    private val args: VideoDetailFragmentArgs by navArgs()

    private lateinit var binding: FragmentVideoDetailBinding

    @Inject
    lateinit var player: SimpleExoPlayer

    @Inject
    lateinit var viewModel: VideoListViewModel


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        binding = FragmentVideoDetailBinding.inflate(inflater, container, false)
        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        with(binding) {
            args.video?.let {
                playerView.player = player
                val mediaItem = MediaItem.fromUri(it.videoUrl)
                player.setMediaItem(mediaItem)
                player.prepare()
                player.play()
            }
        }

    }


}
