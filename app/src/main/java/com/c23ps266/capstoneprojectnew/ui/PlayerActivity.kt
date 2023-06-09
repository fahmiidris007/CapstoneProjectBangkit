package com.c23ps266.capstoneprojectnew.ui


import android.media.MediaPlayer
import android.os.Bundle
import android.os.Handler
import android.view.View
import android.widget.SeekBar
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.c23ps266.capstoneprojectnew.R
import com.c23ps266.capstoneprojectnew.databinding.ActivityPlayerBinding
import com.c23ps266.capstoneprojectnew.model.AudioModel
import com.c23ps266.capstoneprojectnew.util.createTimeLabel
import com.c23ps266.capstoneprojectnew.viewmodel.MainViewModel
import com.c23ps266.capstoneprojectnew.viewmodel.ViewModelFactory

class PlayerActivity : AppCompatActivity() {
    private lateinit var mediaPlayer: MediaPlayer
    private lateinit var binding: ActivityPlayerBinding
    private var isPlaying: Boolean = false
    private var currentAudioIndex: Int = 0
    private var selectedAudioIndex: Int = -1
    private lateinit var audioList: ArrayList<AudioModel>
    private val viewModel: MainViewModel by viewModels { ViewModelFactory.getInstance(this) }
    private var handler = Handler()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPlayerBinding.inflate(layoutInflater)
        setContentView(binding.root)

        selectedAudioIndex = intent.getIntExtra(EXTRA_SELECTED_AUDIO_INDEX, -1)
        audioList = intent.getParcelableArrayListExtra(EXTRA_AUDIO_LIST) ?: ArrayList()

        setMediaPlayer()
        setSeekBar()
        setBackButton()
        setFavoriteButton()
    }

    private fun setMediaPlayer() {
        mediaPlayer = MediaPlayer()

        if (selectedAudioIndex != -1) {
            currentAudioIndex = selectedAudioIndex
        }

        prepareMediaPlayer()

        binding.playPauseButton.setOnClickListener {
            if (isPlaying) {
                pauseAudio()
            } else {
                playAudio()
            }
        }

        binding.nextButton.setOnClickListener {
            playNextAudio()
        }

        binding.previousButton.setOnClickListener {
            playPreviousAudio()
        }

        mediaPlayer.setOnPreparedListener {
            setLoading(false)
            binding.seekBar.max = mediaPlayer.duration
            binding.totalTime.text = createTimeLabel(mediaPlayer.duration)
            binding.audioTitle.text = audioList[currentAudioIndex].title
        }

        mediaPlayer.setOnCompletionListener {
            isPlaying = false
            binding.playPauseButton.setImageResource(R.drawable.ic_baseline_play_arrow_24)
            binding.seekBar.progress = 0
            binding.elapsedTime.text = createTimeLabel(0)
        }
    }


    private fun prepareMediaPlayer() {
        setLoading(true)
        mediaPlayer.reset()
        mediaPlayer.setDataSource(audioList[currentAudioIndex].uriString)
        mediaPlayer.prepareAsync()
        binding.seekBar.progress = 0
        binding.elapsedTime.text = createTimeLabel(0)
    }

    private fun setSeekBar() {
        binding.seekBar.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar, progress: Int, fromUser: Boolean) {
                if (fromUser) {
                    mediaPlayer.seekTo(progress)
                    binding.elapsedTime.text = createTimeLabel(progress)
                }
            }

            override fun onStartTrackingTouch(seekBar: SeekBar) {
                handler.removeCallbacksAndMessages(null)
            }

            override fun onStopTrackingTouch(seekBar: SeekBar) {
                val newPosition = binding.seekBar.progress
                mediaPlayer.seekTo(newPosition)
                updateSeekBar()
            }
        })
    }

    private fun playNextAudio() {
        currentAudioIndex++
        if (currentAudioIndex >= audioList.size) {
            currentAudioIndex = 0
        }
        pauseAudio()
        prepareMediaPlayer()
        playAudio()
        setFavoriteButton()
    }

    private fun playPreviousAudio() {
        currentAudioIndex--
        if (currentAudioIndex < 0) {
            currentAudioIndex = audioList.size - 1
        }
        pauseAudio()
        prepareMediaPlayer()
        playAudio()
        setFavoriteButton()
    }


    private fun playAudio() {
        mediaPlayer.start()
        isPlaying = true
        binding.playPauseButton.setImageResource(R.drawable.ic_baseline_pause_24)
        binding.tv1.text = getString(R.string.audio_playing)
        binding.audioTitle.text = audioList[currentAudioIndex].title
        updateSeekBar()
    }

    private fun pauseAudio() {
        mediaPlayer.pause()
        isPlaying = false
        binding.playPauseButton.setImageResource(R.drawable.ic_baseline_play_arrow_24)
        binding.tv1.text = getString(R.string.audio_paused)
    }

    private fun updateSeekBar() {
        handler.postDelayed(object : Runnable {
            override fun run() {
                val currentPosition = mediaPlayer.currentPosition
                binding.seekBar.progress = currentPosition
                binding.elapsedTime.text = createTimeLabel(currentPosition)
                handler.postDelayed(this, 1000)
            }
        }, 0)
    }


    private fun setBackButton() {
        binding.back.setOnClickListener {
            finish()
        }
    }


    override fun onStop() {
        super.onStop()
        if (isPlaying) {
            pauseAudio()
        }
        mediaPlayer.release()
        handler.removeCallbacksAndMessages(null)
    }

    private fun setLoading(isLoading: Boolean) {
        if (isLoading) {
            binding.pbPlayerLoading.visibility = View.VISIBLE
            binding.animationView.visibility = View.INVISIBLE
        } else {
            binding.pbPlayerLoading.visibility = View.INVISIBLE
            binding.animationView.visibility = View.VISIBLE
        }
    }

    private fun setFavoriteButton() {
        viewModel.isFavorited(audioList[currentAudioIndex].title).observe(
            this
        ) { favorite ->
            if (favorite != null) {
                if (favorite) {
                    binding.favoriteButton.setImageResource(R.drawable.ic_baseline_favorite_24)
                    binding.favoriteButton.setOnClickListener {
                        viewModel.removeFromFavorite(audioList[currentAudioIndex].title)
                        binding.favoriteButton.setImageResource(R.drawable.ic_baseline_favorite_border_24)
                    }
                } else {
                    binding.favoriteButton.setImageResource(R.drawable.ic_baseline_favorite_border_24)
                    binding.favoriteButton.setOnClickListener {
                        viewModel.addToFavorite(audioList[currentAudioIndex])
                        binding.favoriteButton.setImageResource(R.drawable.ic_baseline_favorite_24)
                    }
                }
            }

        }
    }

    companion object {
        const val EXTRA_AUDIO_LIST = "extra_audio_list"
        const val EXTRA_SELECTED_AUDIO_INDEX = "extra_selected_audio_index"
    }
}
