package com.devjaewoo.itsmywaye.adapter

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RadioButton
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import com.devjaewoo.itsmywaye.R
import com.devjaewoo.itsmywaye.TAG
import com.devjaewoo.itsmywaye.dto.RingtoneDataDTO

class RingtoneRecyclerAdapter(private val ringtoneList: List<RingtoneDataDTO>, private var selectedIndex: Int) : RecyclerView.Adapter<RingtoneRecyclerAdapter.ViewHolder>() {

    init {
        ringtoneList[selectedIndex].selected = true
    }

    private val onSelect = { index: Int ->
        Log.d(TAG, "onSelect: prev: $selectedIndex current: $index")
        if(index != selectedIndex) {
            ringtoneList[selectedIndex].selected = false
            ringtoneList[index].selected = true

            notifyItemChanged(selectedIndex)
            notifyItemChanged(index)

            selectedIndex = index
        }
    }

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {

        private val layout = view.findViewById<ConstraintLayout>(R.id.layout_ringtone_item)
        private val radioButton = view.findViewById<RadioButton>(R.id.radioButton)

        init {
            radioButton.isClickable = false
        }

        fun bind(ringtoneData: RingtoneDataDTO) {
            radioButton.text = ringtoneData.name
            radioButton.isChecked = ringtoneData.selected

            radioButton.setOnClickListener { onSelect(adapterPosition) }
            layout.setOnClickListener { onSelect(adapterPosition) }
        }
    }

    fun getSelectedRingtone(): RingtoneDataDTO = ringtoneList[selectedIndex]

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.ringtone_list_item, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) {
        viewHolder.bind(ringtoneList[position])
    }

    override fun getItemCount(): Int = ringtoneList.size
}