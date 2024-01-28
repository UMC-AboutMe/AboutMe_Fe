import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.aboutme.databinding.ItemAddProfileBinding

class MainProfileAddVPAdapter : RecyclerView.Adapter<MainProfileAddVPAdapter.MainItemViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MainItemViewHolder {
        val binding = ItemAddProfileBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MainItemViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MainItemViewHolder, position: Int) {
        // 필요한 경우 ViewHolder에 데이터를 바인딩합니다.

        holder.bind()
    }

    override fun getItemCount(): Int {
        // 데이터 목록의 크기를 반환합니다.
        return 1 // 실제 데이터 목록의 크기로 변경하세요.
    }

    inner class MainItemViewHolder(private val binding: ItemAddProfileBinding) : RecyclerView.ViewHolder(binding.root) {
        // 필요한 경우 뷰를 정의하고 데이터를 바인딩합니다.
        fun bind() {
            // 필요한 경우 뷰를 정의하고 데이터를 바인딩합니다.
            // 예를 들어:
            // binding.textView.text = item.someText
        }
    }
}
