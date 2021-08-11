package com.harisewak.verticalvideos.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.navArgs
import com.google.android.exoplayer2.ExoPlayer
import com.google.android.exoplayer2.MediaItem
import com.google.android.exoplayer2.Player
import com.google.android.material.transition.MaterialContainerTransform
import com.harisewak.verticalvideos.BASE_URL
import com.harisewak.verticalvideos.data.Video
import com.harisewak.verticalvideos.databinding.FragmentVideoDetailBinding
import com.harisewak.verticalvideos.util.*
import com.harisewak.verticalvideos.viewmodel.VideoListViewModel
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class VideoDetailFragment : Fragment() {

    private val args: VideoDetailFragmentArgs by navArgs()

    private lateinit var binding: FragmentVideoDetailBinding

    @Inject
    lateinit var player: ExoPlayer

    @Inject
    lateinit var viewModel: VideoListViewModel


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        sharedElementEnterTransition = MaterialContainerTransform().apply {

            duration = 300L
            isElevationShadowEnabled = true
//            setAllContainerColors(requireContext().themeColor(R.attr.colorSurface))
        }
    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        binding = FragmentVideoDetailBinding.inflate(inflater, container, false)
        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        args.video?.let { playVideo(it, savedInstanceState) }

    }

    private fun playVideo(video: Video, savedInstanceState: Bundle?) {

        val videoUrl = BASE_URL + video.videoUrl

        val thumbnailUrl = BASE_URL + video.thumb

        val curPlaybackPosition: Long = if (savedInstanceState != null) {
            savedInstanceState.get("cur_position") as Long
        } else {
            video.curPosition
        }

        with(binding) {
            playerView.player = player
            logd("exoPlayer instance: $player")
            val mediaItem = MediaItem.fromUri(videoUrl)
            player.setMediaItem(mediaItem)
            player.prepare()
            player.seekTo(curPlaybackPosition)
            player.enableVolume()
            player.play()

            ivThumbnail.load(thumbnailUrl)

            pbLoading.show()

            player.addListener(object : Player.Listener {

                override fun onPlaybackStateChanged(state: Int) {
                    super.onPlaybackStateChanged(state)

                    if (state == Player.STATE_READY) {
                        pbLoading.hide()
                    }

                }
            })
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        outState.putLong("cur_position", player.currentPosition)
        super.onSaveInstanceState(outState)
    }


}
