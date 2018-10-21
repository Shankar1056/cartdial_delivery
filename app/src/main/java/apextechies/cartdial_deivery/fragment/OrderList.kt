package apextechies.cartdial_deivery.fragment

import android.app.Fragment
import android.content.Intent
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import apextechies.cartdial_deivery.R
import apextechies.cartdial_deivery.activity.OrderDetails
import apextechies.cartdial_deivery.adapter.QcListAdapter
import apextechies.cartdial_deivery.common.ClsGeneral
import apextechies.cartdial_deivery.common.ConstantValue
import apextechies.cartdial_deivery.common.WebServices
import apextechies.cartdial_deivery.listener.OnItemClick
import apextechies.cartdial_deivery.model.OrderListModel
import apextechies.cartdialqc.api.Download_web
import apextechies.cartdialqc.api.OnTaskCompleted
import kotlinx.android.synthetic.main.fragment_orderlist.*
import org.apache.http.NameValuePair
import org.apache.http.message.BasicNameValuePair
import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject

class OrderList: Fragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_orderlist,container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        orderListRV.layoutManager = LinearLayoutManager(activity)


        getAllOrder()
    }

    private fun getAllOrder() {
        var list = ArrayList<NameValuePair>()
        list.add(BasicNameValuePair("id", ClsGeneral.getPreferences(activity, ConstantValue.USERID)))
        var web = Download_web(activity, object : OnTaskCompleted {
            override fun onTaskCompleted(response: String) {
                var list = ArrayList<OrderListModel>()

                if (response.length>0){

                    try {
                        var jobj = JSONObject(response)
                        if (jobj.optString("status").equals("true")) {
                            var jarray = jobj.optJSONArray("data")
                            for (i in 0 until jarray.length()) {
                                var jo = jarray.optJSONObject(i)
                                list.add(OrderListModel(jo.optString("id"), jo.optString("admin_id"), jo.optString("delivery_boy_id")
                                , jo.optString("order_id"), jo.optString("status")))
                            }
                            orderListRV.adapter = QcListAdapter(activity, list, object : OnItemClick {

                                override fun position(pos: Int) {
                                    startActivity(Intent(activity, OrderDetails::class.java)
                                            .putExtra("order_id", list[pos].order_id))
                                }
                            })

                        }
                    }
                    catch (e: JSONException){

                    }
                }
            }

        })

        web.setApiToken(ClsGeneral.getPreferences(activity, WebServices.XAPIKEY))
        web.setReqType(false)
        web.setData(list)
        web.execute(WebServices.ORDERLIST)
    }
}