package apextechies.cartdial_deivery.adapter

import android.content.Context
import android.os.Build.ID
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.RadioButton
import android.widget.RadioGroup
import android.widget.Toast
import apextechies.cartdial_deivery.R
import apextechies.cartdial_deivery.listener.OnItemClick
import apextechies.cartdial_deivery.model.OrderListModel
import java.lang.NullPointerException


class QcListAdapter( private val context: Context, private val myCartModels: ArrayList<OrderListModel>, private val onItemClick: OnItemClick) : RecyclerView.Adapter<QcListAdapter.MyViewHolder>() {
    inner class MyViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        var orderId: TextView



        init {
            orderId = view.findViewById<View>(R.id.orderId) as TextView

        }





    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val itemView = LayoutInflater.from(parent.context)
                .inflate(R.layout.row_orderlist, parent, false)

        return MyViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val sa = myCartModels!![position]

        holder.orderId.text = "Order ID: "+sa.order_id
       holder.itemView.setOnClickListener {
           onItemClick.position(position)
       }




    }

    override fun getItemCount(): Int {
        return myCartModels!!.size
    }

}
