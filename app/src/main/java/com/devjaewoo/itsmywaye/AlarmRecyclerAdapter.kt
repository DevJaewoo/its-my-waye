package com.devjaewoo.itsmywaye

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.devjaewoo.itsmywaye.dao.ItemDAO
import com.devjaewoo.itsmywaye.model.Item
import com.google.android.material.switchmaterial.SwitchMaterial

class AlarmRecyclerAdapter: RecyclerView.Adapter<AlarmRecyclerAdapter.ViewHolder>() {

    var dataSet: List<Item> = ArrayList()
    set(value) {
        field = value
        notifyDataSetChanged()
    }

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        var id: Int
        val name: TextView
        val enable: SwitchMaterial
        val setting: ImageButton

        init {
            id = 0
            name = view.findViewById(R.id.tv_alarm_name)
            enable = view.findViewById(R.id.switch_alarm_enable)
            setting = view.findViewById(R.id.ib_alarm_setting)

            view.findViewById<SwitchMaterial>(R.id.switch_alarm_enable).setOnCheckedChangeListener { _, isChecked ->
                Log.d(TAG, "onCheckedChangeListener[$id]: $isChecked")

                if(ApplicationManager.ItemList[id - 1].enabled != isChecked) {
                    ApplicationManager.ItemList[id - 1].enabled = isChecked
                    ItemDAO(view.context).update(ApplicationManager.ItemList[id - 1])
                }
            }
        }

        fun onBind(item: Item) {
            id = item.id
            name.text = item.name
            enable.isChecked = item.enabled
        }
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.alarm_list_item, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) {
        viewHolder.onBind(dataSet[position])
    }

    override fun getItemCount(): Int = dataSet.size
}