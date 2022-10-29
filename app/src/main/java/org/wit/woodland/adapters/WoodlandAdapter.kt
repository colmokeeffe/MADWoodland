package org.wit.woodland.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso
import org.wit.woodland.databinding.CardWoodlandBinding
import org.wit.woodland.models.WoodlandModel

interface WoodlandListener {
    fun onWoodlandClick(woodland: WoodlandModel)
}

class WoodlandAdapter constructor(
    private var woodlands: List<WoodlandModel>,
    private val listener: WoodlandListener
) :
    RecyclerView.Adapter<WoodlandAdapter.MainHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MainHolder {
        val binding = CardWoodlandBinding
            .inflate(LayoutInflater.from(parent.context), parent, false)

        return MainHolder(binding)
    }

    override fun onBindViewHolder(holder: MainHolder, position: Int) {
        val woodland = woodlands[holder.adapterPosition]
        holder.bind(woodland, listener)
    }

    override fun getItemCount(): Int = woodlands.size

    class MainHolder(private val binding: CardWoodlandBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(woodland: WoodlandModel, listener: WoodlandListener) {
            binding.woodlandTitle.text = woodland.title
            binding.woodlandDescription.text = woodland.description
            Picasso.get().load(woodland.image).resize(300,300).into(binding.imageIcon)
            binding.root.setOnClickListener { listener.onWoodlandClick(woodland) }
        }
    }
}
