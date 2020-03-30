package com.example.summonerz

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

internal class MonsterListAdapter :
    RecyclerView.Adapter<MonsterListAdapter.MonsterListViewHolder>() {

    //Cached copy of monsters
    private var monsters = emptyList<Monster>()

    // Provide a reference to the views for each data item
    // Complex data items may need more than one view per item, and
    // you provide access to all the views for a data item in a view holder.
    // Each data item is just a string in this case that is shown in a TextView.
    internal class MonsterListViewHolder private constructor(view: View) :
        RecyclerView.ViewHolder(view) {
        private val iconView: ImageView = view.findViewById<ImageView>(R.id.monster_icon)
        private val nameView: TextView = view.findViewById(R.id.monster_name)
        private val typeView: TextView = view.findViewById(R.id.monster_type)

        fun bindMonster(context: Context, monster: Monster) {
            iconView.setImageResource(
                context.resources.getIdentifier(
                    monster.icon, "drawable",
                    context.packageName
                )
            )
            nameView.text = monster.name
            typeView.text = monster.type
        }

        companion object {

            fun create(parent: ViewGroup): MonsterListViewHolder {
                val view = LayoutInflater.from(parent.context)
                    .inflate(R.layout.monster_list_item_view, parent, false)
                return MonsterListViewHolder(view)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MonsterListViewHolder =
        MonsterListViewHolder.create(parent)

    override fun onBindViewHolder(holder: MonsterListViewHolder, position: Int) =
        holder.bindMonster(holder.itemView.context, monsters[position])

    internal fun setMonsters(monsters: List<Monster>) {
        this.monsters = monsters
        notifyDataSetChanged()
    }

    override fun getItemCount(): Int =
        monsters.size
}