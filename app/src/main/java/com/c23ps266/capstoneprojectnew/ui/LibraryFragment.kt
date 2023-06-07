package com.c23ps266.capstoneprojectnew.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.c23ps266.capstoneprojectnew.adapter.ListAudioAdapter
import com.c23ps266.capstoneprojectnew.databinding.FragmentLibraryBinding
import com.c23ps266.capstoneprojectnew.model.AudioModel
import com.c23ps266.capstoneprojectnew.viewmodel.MainViewModel
import com.c23ps266.capstoneprojectnew.viewmodel.ViewModelFactory


class LibraryFragment : Fragment() {
    private var _binding: FragmentLibraryBinding? = null
    private val binding get() = _binding!!
    private val viewModel: MainViewModel by viewModels { ViewModelFactory.getInstance(requireContext()) }
    private lateinit var listAdapter: ListAudioAdapter
    private val audioData = ArrayList<AudioModel>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        _binding = FragmentLibraryBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setListAdapter()
        setLibrary()
    }

    private fun setListAdapter() {
        listAdapter = ListAudioAdapter(
            audioData,
            object : ListAudioAdapter.OnFavorite {
                override fun onCheckFavorite(
                    audio: AudioModel,
                    callback: (result: Boolean) -> Unit,
                ) {
                    viewModel.isFavorited(audio.title).apply {
                        observe(viewLifecycleOwner, callback)
                        removeObserver(callback)
                    }
                }

                override fun onFavoriteClicked(
                    isAlreadyFavorite: Boolean,
                    audio: AudioModel,
                ): Boolean {
                    return if (isAlreadyFavorite) {
                        viewModel.removeFromFavorite(audio.title)
                        false
                    } else {
                        viewModel.addToFavorite(audio)
                        true
                    }
                }
            }
        )
        binding.rvLibrary.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = listAdapter
        }

    }

    private fun setLibrary() {
        viewModel.getFavoriteAudios().observe(viewLifecycleOwner) { audioList ->
            if (audioList.isEmpty()) {
                binding.tvEmpty.visibility = View.VISIBLE
                binding.rvLibrary.visibility = View.GONE
            } else {
                binding.tvEmpty.visibility = View.GONE
                audioData.clear()
                audioData.addAll(audioList)
                listAdapter.notifyDataSetChanged()
            }
        }
    }
}