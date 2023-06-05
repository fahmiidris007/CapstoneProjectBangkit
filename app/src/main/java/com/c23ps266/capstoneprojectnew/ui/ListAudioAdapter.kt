package com.c23ps266.capstoneprojectnew.ui

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.c23ps266.capstoneprojectnew.R
import com.c23ps266.capstoneprojectnew.databinding.ListAudioBinding
import com.c23ps266.capstoneprojectnew.model.AudioModel

class ListAudioAdapter(private val listAudio: ArrayList<AudioModel>) :
    RecyclerView.Adapter<ListAudioAdapter.ListViewHolder>() {
    private var selectedAudioIndex: Int = -1

    class ListViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val binding = ListAudioBinding.bind(itemView)
        fun bind(audio: AudioModel) {
            with(binding) {
                tvTitle.text = audio.title
                tvDuration.text = audio.durationMs.toString()
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.list_audio, parent, false)
        return ListViewHolder(view)
    }

    override fun getItemCount(): Int = listAudio.size

    private fun setSelectedAudioIndex(index: Int) {
        selectedAudioIndex = index
        notifyDataSetChanged()
    }


    override fun onBindViewHolder(holder: ListViewHolder, position: Int) {
        val audio = listAudio[position]
        holder.bind(audio)

        holder.itemView.setOnClickListener {
            setSelectedAudioIndex(position)
            val context = holder.itemView.context
            val intent = Intent(context, PlayerActivity::class.java).apply {
                putExtra(PlayerActivity.EXTRA_AUDIO_TITLE, audio.title)
                putExtra(PlayerActivity.EXTRA_AUDIO_DURATION, audio.durationMs)
                putExtra(PlayerActivity.EXTRA_AUDIO_URL, audio.uriString)
                putParcelableArrayListExtra(PlayerActivity.EXTRA_AUDIO_LIST, listAudio)
                putExtra(PlayerActivity.EXTRA_SELECTED_AUDIO_INDEX, selectedAudioIndex)
            }
            context.startActivity(intent)
        }
    }

}