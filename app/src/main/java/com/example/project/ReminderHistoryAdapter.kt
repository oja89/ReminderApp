
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import com.example.project.ReminderInfo
import com.example.project.databinding.ReminderHistoryItemBinding

class ReminderHistoryAdapter(context: Context, private val list: List<ReminderInfo>) : BaseAdapter() {

    private val inflater: LayoutInflater =
        context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater

    override fun getView(position: Int, convertView: View?, container: ViewGroup?): View? {
        var rowBinding = ReminderHistoryItemBinding.inflate(inflater, container, false)
        //set payment info values to the list item
        rowBinding.infoMsgText.text = list[position].message
        rowBinding.infoCreateTime.text = list[position].creation_time
        rowBinding.infoCreatorId.text = list[position].creator_id
        rowBinding.infoTimeStr.text = list[position].reminder_time
        rowBinding.infoReminderSeen.text = list[position].reminder_seen.toString()
        rowBinding.infoLocationX.text = list[position].location_x.toString()
        rowBinding.infoLocationY.text = list[position].location_y.toString()

        return rowBinding.root
    }

    override fun getItem(position: Int): Any {
        return list[position]
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getCount(): Int {
        return list.size
    }

}

