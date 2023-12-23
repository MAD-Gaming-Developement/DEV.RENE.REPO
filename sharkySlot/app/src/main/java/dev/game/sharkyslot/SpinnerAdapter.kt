package dev.game.sharkyslot

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView

class SpinnerAdapter(private val context: Context, private val slot: IntArray) :
    RecyclerView.Adapter<SpinnerAdapter.ItemViewHolder>() {

    private val comboNumber = 7

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {
        val layoutInflater = LayoutInflater.from(context)
        val view = layoutInflater.inflate(R.layout.spinner_item, parent, false)
        return ItemViewHolder(view)
    }

    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {
        val i = if (position < 7) position else position % comboNumber
        when (slot[i]) {
            1 -> holder.pic.setImageResource(R.drawable.m1)
            2 -> holder.pic.setImageResource(R.drawable.m2)
            3 -> holder.pic.setImageResource(R.drawable.m3)
            4 -> holder.pic.setImageResource(R.drawable.m4)
            5 -> holder.pic.setImageResource(R.drawable.m5)
            6 -> holder.pic.setImageResource(R.drawable.m6)
            7 -> holder.pic.setImageResource(R.drawable.m7)
        }
    }

    override fun getItemCount(): Int {
        return Int.MAX_VALUE
    }

    class ItemViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val pic: ImageView = itemView.findViewById(R.id.spinner_item)
    }
}
