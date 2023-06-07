package com.c23ps266.capstoneprojectnew.adapter

import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.c23ps266.capstoneprojectnew.R
import com.c23ps266.capstoneprojectnew.databinding.ListAudioBinding
import com.c23ps266.capstoneprojectnew.model.AudioModel
import com.c23ps266.capstoneprojectnew.ui.PlayerActivity
import com.c23ps266.capstoneprojectnew.util.createTimeLabel

class ListAudioAdapter(
    private val listAudio: ArrayList<AudioModel>,
    private val onFavorite: OnFavorite,
) : RecyclerView.Adapter<ListAudioAdapter.ListViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListViewHolder {
        val binding = ListAudioBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ListViewHolder(binding)
    }

    override fun getItemCount(): Int = listAudio.size

    override fun onBindViewHolder(holder: ListViewHolder, position: Int) {
        val audio = listAudio[position]

        with(holder.binding) {
            root.setOnClickListener {
                val context = holder.itemView.context
                val intent = Intent(context, PlayerActivity::class.java).apply {
                    putParcelableArrayListExtra(PlayerActivity.EXTRA_AUDIO_LIST, listAudio)
                    putExtra(PlayerActivity.EXTRA_SELECTED_AUDIO_INDEX, position)
                }
                context.startActivity(intent)
            }
            tvTitle.text = audio.title
            tvDuration.text = createTimeLabel(audio.durationMs)

            onFavorite.onCheckFavorite(audio) { result ->
                favorite.setImageResource(
                    if (result) R.drawable.ic_baseline_favorite_24
                    else R.drawable.ic_baseline_favorite_border_24
                )
                favorite.setOnClickListener {
                    onFavorite.onFavoriteClicked(result, audio).also {
                        if (it) R.drawable.ic_baseline_favorite_24
                        else R.drawable.ic_baseline_favorite_border_24
                    }
                }
            }
        }
    }

    interface OnFavorite {
        fun onCheckFavorite(audio: AudioModel, callback: (result: Boolean) -> Unit)
        fun onFavoriteClicked(isAlreadyFavorite: Boolean, audio: AudioModel): Boolean
    }

    class ListViewHolder(val binding: ListAudioBinding) : RecyclerView.ViewHolder(binding.root)
}
