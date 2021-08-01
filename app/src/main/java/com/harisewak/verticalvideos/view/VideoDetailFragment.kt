package com.harisewak.verticalvideos.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.harisewak.verticalvideos.databinding.FragmentVideoDetailBinding
import com.harisewak.verticalvideos.viewmodel.VideoListViewModel
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class VideoDetailFragment : Fragment() {

    private lateinit var binding: FragmentVideoDetailBinding

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


    }


}
