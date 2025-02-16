package com.example.aboutme.Search

import android.content.Context
import android.provider.ContactsContract
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.aboutme.R

class DialogProfAdapter(private val context: Context) :
    RecyclerView.Adapter<DialogProfAdapter.ViewHolder> () {

    var datas = mutableListOf<DialogProfData>()
    // 선택된 아이템의 시리얼 번호 목록
    val checkedSerials: List<Int>
        get() = datas.filter { it.isChecked }.map { it.serial_number }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.item_profile, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int = datas.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(datas[position])
    }

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        private val profImg: ImageView = itemView.findViewById(R.id.select_profile_iv)
        private val profName: TextView = itemView.findViewById(R.id.select_profile_name_et)
        private val checkBox: CheckBox = itemView.findViewById(R.id.select_checkBox)
        private val profNum: TextView = itemView.findViewById(R.id.select_profile_num_et)
        private val textView8: TextView = itemView.findViewById(R.id.textView8)

        init {
            // 체크박스의 상태가 변경될 때마다 해당 상태를 업데이트하고 이벤트를 처리
            checkBox.setOnCheckedChangeListener { _, isChecked ->
                val position = adapterPosition
                if (position != RecyclerView.NO_POSITION) {
                    val item = datas[position]
                    item.isChecked = isChecked  // 체크 상태를 업데이트
                }
            }
        }

        fun bind(item: DialogProfData) {
//            Glide.with(itemView)
//                .load(item.profile_img)
//                .into(profImg)
            if (item.profile_img.startsWith("http")) {
                // URL인 경우 Glide를 사용하여 이미지 로드 및 표시
                Glide.with(itemView.context)
                    .load(item.profile_img)
                    .into(profImg)
            } else {
                // 리소스 아이디인 경우 setImageResource() 메서드를 사용하여 이미지 설정
                profImg.setImageResource(item.profile_img.toInt())
            }

            profName.text = item.profile_name
            textView8.text = item.profile_name
            profNum.text = item.profile_num
            checkBox.isChecked = item.isChecked  // 체크 상태를 설정
        }
    }
    // 체크 여부 확인하는 함수
    fun isItemChecked(position: Int): Boolean {
        return datas[position].isChecked
    }

    // 선택된 아이템의 serial_number 반환하는 함수
    fun getSelectedSerialNumber(position: Int): Int {
        return datas[position].serial_number
    }

}