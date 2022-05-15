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

interface AlarmListener {
    fun onAlarmChanged(id: Int, enabled: Boolean)
    fun onAlarmEdit(id: Int)
}

class AlarmRecyclerAdapter(private val listener: AlarmListener):
    RecyclerView.Adapter<AlarmRecyclerAdapter.ViewHolder>() {

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
        }

        fun onBind(item: Item, listener: AlarmListener) {
            id = item.id
            name.text = item.name
            enable.isChecked = item.enabled

            enable.setOnCheckedChangeListener { _, isChecked ->
                listener.onAlarmChanged(id, isChecked)
            }

            setting.setOnClickListener {
                listener.onAlarmEdit(id)
            }
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
        viewHolder.onBind(ApplicationManager.ItemList[position], listener)
    }

    override fun getItemCount(): Int = ApplicationManager.ItemList.size
}