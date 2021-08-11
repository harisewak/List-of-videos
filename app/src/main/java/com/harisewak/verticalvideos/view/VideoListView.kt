package com.harisewak.verticalvideos.view

import android.content.Context
import android.util.AttributeSet
import android.view.*
import android.view.View.OnClickListener
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.ProgressBar
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.exoplayer2.ExoPlayer
import com.google.android.exoplayer2.MediaItem
import com.google.android.exoplayer2.Player
import com.google.android.exoplayer2.ui.AspectRatioFrameLayout
import com.google.android.exoplayer2.ui.StyledPlayerView
import com.harisewak.verticalvideos.BASE_URL
import com.harisewak.verticalvideos.data.Video
import com.harisewak.verticalvideos.util.disableVolume
import com.harisewak.verticalvideos.util.logd
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

/*
* Custom recyclerview for playing videos upon scroll
* - It keeps reference of SimpleExoPlayer in itself and uses a single instance for all videos
* - Videos that are completely visible are played
* */

@AndroidEntryPoint
class VideoListView : RecyclerView {

    private var thumbnail: ImageView? = null
    private var progress: ProgressBar? = null
    private var viewHolderParent: View? = null
    private var frameLayout: FrameLayout? = null

    private lateinit var videoView: StyledPlayerView

    @Inject
    lateinit var videoPlayer: ExoPlayer

    var videoList = listOf<Video>()

    private var videoSurfaceDefaultHeight = 0
    private var screenDefaultHeight = 0
    private var curVideoPos = -1
    private var isVideoViewAdded = false
    private lateinit var curVideo: Video

    constructor(context: Context) : super(context) {
        init(context)
    }

    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs) {
        init(context)
    }

    private fun init(context: Context) {
        val currentWindowMetrics =
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.R) {
                (getContext().getSystemService(Context.WINDOW_SERVICE) as WindowManager).currentWindowMetrics.bounds
            } else {
                // todo not handling right now
                null
            }
        videoSurfaceDefaultHeight = currentWindowMetrics!!.centerX()
        screenDefaultHeight = currentWindowMetrics.centerY()
        videoView = StyledPlayerView(context)
        videoView.resizeMode = AspectRatioFrameLayout.RESIZE_MODE_ZOOM

        addOnScrollListener(object : OnScrollListener() {

            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {

                super.onScrollStateChanged(recyclerView, newState)

                if (newState == SCROLL_STATE_IDLE) {
                    logd("onScrollStateChanged: called.")

                    if (thumbnail != null) { // show the old thumbnail
                        thumbnail!!.visibility = VISIBLE
                    }

                    // There's a special case when the end of the list has been reached.
                    // Need to handle that with this bit of logic
                    if (!recyclerView.canScrollVertically(1)) {
                        playVideo(true)
                    } else {
                        playVideo(false)
                    }
                }
            }
        })

        addOnChildAttachStateChangeListener(object : OnChildAttachStateChangeListener {
            override fun onChildViewAttachedToWindow(view: View) {/* Not used */
            }

            override fun onChildViewDetachedFromWindow(view: View) {
                if (viewHolderParent != null && viewHolderParent == view) {
                    curVideo.curPosition = videoPlayer.currentPosition
                    logd("curVideo.name ${curVideo.title}")
                    logd("curVideo.curPosition ${curVideo.curPosition}")
                    resetVideoView()
                }
            }
        })

        // Player initialization and binding to player view
        videoPlayer.disableVolume()
        videoView.useController = false
        videoView.player = videoPlayer

        videoPlayer.addListener(object : Player.Listener {

            override fun onPlayerStateChanged(playWhenReady: Boolean, playbackState: Int) {
                when (playbackState) {
                    Player.STATE_BUFFERING -> {
                        logd("onPlayerStateChanged: Buffering video.")
                        if (progress != null) {
                            progress!!.visibility = VISIBLE
                        }
                    }
                    Player.STATE_ENDED -> {
                        logd("onPlayerStateChanged: Video ended.")
                        videoPlayer.seekTo(0)
                    }
                    Player.STATE_IDLE -> {
                    }
                    Player.STATE_READY -> {
                        logd("onPlayerStateChanged: Ready to play.")
                        if (progress != null) {
                            progress!!.visibility = GONE
                        }
                        if (!isVideoViewAdded) {
                            addVideoView()
                        }
                    }
                    else -> {
                        /* Not used */
                    }
                }
            }

        })
    }

    fun playVideo(isEndOfList: Boolean) {
        val targetPosition: Int
        if (!isEndOfList) {
            val startPosition =
                (layoutManager as LinearLayoutManager?)!!.findFirstVisibleItemPosition()
            var endPosition =
                (layoutManager as LinearLayoutManager?)!!.findLastVisibleItemPosition()

            // if there is more than 2 list-items on the screen, set the difference to be 1
            if (endPosition - startPosition > 1) {
                endPosition = startPosition + 1
            }

            // something is wrong. return.
            if (startPosition < 0 || endPosition < 0) {
                return
            }

            // if there is more than 1 list-item on the screen
            targetPosition = if (startPosition != endPosition) {
                val startPositionVideoHeight = getVisibleVideoSurfaceHeight(startPosition)
                val endPositionVideoHeight = getVisibleVideoSurfaceHeight(endPosition)
                if (startPositionVideoHeight > endPositionVideoHeight) startPosition else endPosition
            } else {
                startPosition
            }
        } else {
            targetPosition = videoList.size - 1
        }

        logd("playVideo: target position: $targetPosition")

        // video is already playing so return
        if (targetPosition == curVideoPos) {
            return
        }

        // updating position of previously played video
        if (curVideoPos != -1) {
            videoList[curVideoPos].curPosition = videoPlayer.currentPosition
        }

        // set the position of the list-item that is to be played
        curVideoPos = targetPosition

        // remove any old surface views from previously playing videos
        videoView.visibility = INVISIBLE

        removeVideoView(videoView)

        val currentPosition =
            targetPosition - (layoutManager as LinearLayoutManager?)!!.findFirstVisibleItemPosition()

        val child = getChildAt(currentPosition) ?: return

        val holder: VideoListFragment.VideoListAdapter.VideoViewHolder =
            child.tag as VideoListFragment.VideoListAdapter.VideoViewHolder

        // accessing view references of current video
        with(holder.binding) {
            thumbnail = ivThumbnail
            progress = pbLoading
            viewHolderParent = root
            frameLayout = container
        }

        videoView.player = videoPlayer

        logd("exoPlayer instance: $videoPlayer")

        viewHolderParent!!.setOnClickListener(videoViewClickListener)

        curVideo = videoList[targetPosition]

        val mediaItem = MediaItem.fromUri(BASE_URL + curVideo.videoUrl)

        videoPlayer.apply {
            setMediaItem(mediaItem)
            prepare()
            seekTo(curVideo.curPosition)
            play()
        }

    }

    private val videoViewClickListener = OnClickListener { logd("videoView clicked") }

    /**
     * Returns the visible region of the video surface on the screen.
     * if some is cut off, it will return less than the @videoSurfaceDefaultHeight
     * @param playPosition
     * @return
     */
    private fun getVisibleVideoSurfaceHeight(playPosition: Int): Int {
        val at =
            playPosition - (layoutManager as LinearLayoutManager?)!!.findFirstVisibleItemPosition()
        logd("getVisibleVideoSurfaceHeight: at: $at")
        val child = getChildAt(at) ?: return 0
        val location = IntArray(2)
        child.getLocationInWindow(location)
        return if (location[1] < 0) {
            location[1] + videoSurfaceDefaultHeight
        } else {
            screenDefaultHeight - location[1]
        }
    }

    // Remove the old player
    private fun removeVideoView(videoView: StyledPlayerView?) {
        val parent = videoView?.parent as ViewGroup?

        if (parent == null) return

        val index = parent.indexOfChild(videoView)
        if (index >= 0) {
            parent.removeViewAt(index)
            isVideoViewAdded = false
            viewHolderParent!!.setOnClickListener(null)
        }
    }

    private fun addVideoView() {
        frameLayout!!.addView(videoView)
        isVideoViewAdded = true
        videoView.requestFocus()
        videoView.visibility = VISIBLE
        videoView.alpha = 1f
        thumbnail!!.visibility = GONE
    }

    private fun resetVideoView() {
        if (isVideoViewAdded) {
            removeVideoView(videoView)
            curVideoPos = -1
            videoView.visibility = INVISIBLE
            thumbnail!!.visibility = VISIBLE
        }
    }

    fun releasePlayer() {
        videoPlayer.release()
        viewHolderParent = null
    }

    override fun setAdapter(adapter: Adapter<*>?) {

        if (videoList.isEmpty()) {
            throw Error("Video list must be set before setting adapter")
        }

        super.setAdapter(adapter)

        (adapter as VideoListFragment.VideoListAdapter).submitList(videoList)
    }
}