package com.c23ps266.capstoneprojectnew.ui


import android.media.MediaPlayer
import android.os.Bundle
import android.os.Handler
import android.widget.SeekBar
import androidx.appcompat.app.AppCompatActivity
import com.c23ps266.capstoneprojectnew.R
import com.c23ps266.capstoneprojectnew.databinding.ActivityPlayerBinding
import java.util.concurrent.TimeUnit

class PlayerActivity : AppCompatActivity() {
    private lateinit var mediaPlayer: MediaPlayer

    private lateinit var binding: ActivityPlayerBinding

    private var isPlaying: Boolean = false
    private var currentAudioIndex: Int = 0
    private var audioList: Array<Int> = arrayOf(R.raw.alan_walker_alone, R.raw.alan_walker_memories, R.raw.alan_walker_faded)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPlayerBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setMediaPlayer()
        setSeekBar()
        setBackButton()
    }

    private fun setMediaPlayer(){
        mediaPlayer = MediaPlayer.create(this, audioList[currentAudioIndex])

        binding.playPauseButton.setOnClickListener {
            if (isPlaying) {
                pauseAudio()
            } else {
                playAudio()
            }
        }

        mediaPlayer.setOnCompletionListener {
            playNextAudio()
        }

        binding.previousButton.setOnClickListener {
            playPreviousAudio()
        }

        binding.nextButton.setOnClickListener {
            playNextAudio()
        }
    }

    private fun setSeekBar(){
        val duration = mediaPlayer.duration
        binding.seekBar.max = duration

        binding.seekBar.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                if (fromUser) {
                    mediaPlayer.seekTo(progress)
                }
            }

            override fun onStartTrackingTouch(seekBar: SeekBar?) {}

            override fun onStopTrackingTouch(seekBar: SeekBar?) {}
        })

    }

    private fun playPreviousAudio() {
        if (currentAudioIndex > 0) {
            currentAudioIndex--
        } else {
            currentAudioIndex = audioList.size - 1
        }
        playAudio()
    }

    private fun playNextAudio() {
        if (currentAudioIndex < audioList.size - 1) {
            currentAudioIndex++
        } else {
            currentAudioIndex = 0
        }
        playAudio()
    }

    private fun playAudio() {
        if (mediaPlayer.isPlaying) {
            mediaPlayer.stop()
        }
        mediaPlayer.release()
        mediaPlayer = MediaPlayer.create(this, audioList[currentAudioIndex])
        mediaPlayer.start()
        isPlaying = true
        binding.playPauseButton.setImageResource(R.drawable.ic_baseline_pause_24)
        binding.tv1.text = getString(R.string.audio_playing)

        val audioTitle = getAudioTitle(audioList[currentAudioIndex])
        binding.songTitle.text = audioTitle

        val totalTime = formatTime(mediaPlayer.duration)
        binding.totalTime.text = totalTime

        val handler = Handler()
        handler.postDelayed(object : Runnable {
            override fun run() {
                try {
                    val currentPosition = mediaPlayer.currentPosition
                    binding.seekBar.progress = currentPosition
                    val elapsedTime = formatTime(currentPosition)
                    binding.elapsedTime.text = elapsedTime

                    if (currentPosition >= mediaPlayer.duration) {
                        playNextAudio()
                    } else {
                        handler.postDelayed(this, 1000)
                    }
                } catch (e: IllegalStateException) {
                    e.printStackTrace()
                }
            }
        }, 0)
    }

    private fun pauseAudio() {
        mediaPlayer.pause()
        isPlaying = false
        binding.playPauseButton.setImageResource(R.drawable.ic_baseline_play_arrow_24)
        binding.tv1.text = getString(R.string.audio_paused)
    }

    private fun formatTime(timeInMillis: Int): String {
        val minutes = TimeUnit.MILLISECONDS.toMinutes(timeInMillis.toLong())
        val seconds = TimeUnit.MILLISECONDS.toSeconds(timeInMillis.toLong()) % 60
        return String.format("%02d:%02d", minutes, seconds)
    }

    private fun getAudioTitle(audioId: Int): String {
        val resources = resources
        val audioName = resources.getResourceEntryName(audioId)
        return audioName
    }

    private fun setBackButton(){
        binding.back.setOnClickListener {
            finish()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        if (this::mediaPlayer.isInitialized) {
            mediaPlayer.release()
        }
    }
}



