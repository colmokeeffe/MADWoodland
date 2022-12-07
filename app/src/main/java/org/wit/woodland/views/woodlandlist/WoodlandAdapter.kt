package org.wit.woodland.views.woodlandlist

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import kotlinx.android.synthetic.main.card_woodland.view.*
import org.wit.woodland.R
import org.wit.woodland.models.WoodlandModel

interface WoodlandListener {
    fun onWoodlandClick(woodland: WoodlandModel)
    fun onFavouriteClick(woodland: WoodlandModel,favourite: Boolean)
}

class WoodlandAdapter constructor(
    private var woodlands: ArrayList<WoodlandModel>,
    private val listener: WoodlandListener
) :
    RecyclerView.Adapter<WoodlandAdapter.MainHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MainHolder {
        return MainHolder(
            LayoutInflater.from(parent?.context).inflate(
                R.layout.card_woodland,
                parent,
                false
            )

        )
    }

    override fun onBindViewHolder(holder: MainHolder, position: Int) {
        val woodland = woodlands[holder.adapterPosition]
        holder.bind(woodland,listener)
    }


    override fun getItemCount(): Int = woodlands.size

    class MainHolder constructor(itemView: View) : RecyclerView.ViewHolder(itemView) {



        fun bind(woodland: WoodlandModel, listener: WoodlandListener) {
            var favourite = woodland.favourite
            if(favourite) {
                itemView.favouriteButton.setImageResource(android.R.drawable.btn_star_big_on);
            }
            itemView.tag = woodland.fbId
            itemView.woodlandTitle.text = woodland.title
            itemView.description.text = woodland.description
            if(woodland.images.size>0) {
                var imagestring = woodland.images.first()
                /*itemView.imageIcon.setImageBitmap(
                    readImageFromPath(
                        itemView.context,
                        imagestring
                    )
                )

                 */
                Glide.with(itemView.context).load(imagestring).into(itemView.imageIcon);

            }
            itemView.setOnClickListener { listener.onWoodlandClick(woodland) }
            itemView.favouriteButton.setOnClickListener {
                if(favourite == true){
                    itemView.favouriteButton.setImageResource(android.R.drawable.btn_star_big_off);
                    favourite = false;
                    listener.onFavouriteClick(woodland, favourite)
                }
                else{
                    itemView.favouriteButton.setImageResource(android.R.drawable.btn_star_big_on);
                    favourite = true;
                    listener.onFavouriteClick(woodland, favourite)
                }
            }

        }
    }
}
