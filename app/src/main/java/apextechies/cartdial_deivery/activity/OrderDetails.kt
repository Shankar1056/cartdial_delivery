package apextechies.cartdial_deivery.activity

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.provider.Settings
import android.support.v4.app.ActivityCompat
import android.support.v4.view.GravityCompat
import android.support.v4.widget.DrawerLayout
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import apextechies.cartdial_deivery.BuildConfig
import apextechies.cartdial_deivery.R
import apextechies.cartdial_deivery.common.ClsGeneral
import apextechies.cartdial_deivery.common.Utilz
import apextechies.cartdial_deivery.common.WebServices
import apextechies.cartdial_deivery.model.ProdPrice
import apextechies.cartdial_deivery.model.ProductDetailsmodel
import apextechies.cartdialqc.api.Download_web
import apextechies.cartdialqc.api.OnTaskCompleted
import comcater.employeemanagement.adapter.ProductDetailsAdapter
import kotlinx.android.synthetic.main.activity_details.*
import org.apache.http.NameValuePair
import org.apache.http.message.BasicNameValuePair
import org.json.JSONException
import org.json.JSONObject

class OrderDetails: AppCompatActivity() {

    var prodList = ArrayList<ProductDetailsmodel>()
    var priceList = ArrayList<ProdPrice>()
    var ordrcreatedby = ""
    var paymentMethod = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_details)

        initWidgit()
        clickListener()

        call.setOnClickListener {
            if (!checkPermissions()) {
                requestPermissions()
            } else {
                makecall()
            }
        }
    }

    private fun clickListener() {
        /*nextTV.setOnClickListener {
            if (input_orderId.text.toString().trim().equals("")){
                Toast.makeText(this, "Enter Order ID First", Toast.LENGTH_SHORT).show()
            }else {
                startActivity(Intent(this, DeviceFeatureOptionActivity::class.java)
                        .putExtra("orderid", input_orderId.text.toString()))
            }
        }*/
    }

    fun checkPermissions(): Boolean {
        val permissionState = ActivityCompat.checkSelfPermission(this,
                Manifest.permission.CALL_PHONE)
        return permissionState == PackageManager.PERMISSION_GRANTED
    }
    fun requestPermissions() {
        val shouldProvideRationale = ActivityCompat.shouldShowRequestPermissionRationale(this,
                Manifest.permission.CALL_PHONE)

        if (shouldProvideRationale) {
            ActivityCompat.requestPermissions(this,
                    arrayOf(Manifest.permission.CALL_PHONE),
                    REQUEST_PERMISSIONS_REQUEST_CODE)

        } else {
            startLocationPermissionRequest()
        }
    }

    fun startLocationPermissionRequest() {
        ActivityCompat.requestPermissions(this@OrderDetails,
                arrayOf(Manifest.permission.CALL_PHONE),
                REQUEST_PERMISSIONS_REQUEST_CODE)
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>,
                                            grantResults: IntArray) {
        if (requestCode == REQUEST_PERMISSIONS_REQUEST_CODE) {
            if (grantResults.size <= 0) {
            } else if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                makecall()
            } else {
                val intent =  Intent()
                intent.setAction(
                        Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                val uri = Uri.fromParts("package",
                        BuildConfig.APPLICATION_ID, null);
                intent.setData(uri);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
            }
        }
    }


    private fun makecall() {
        try {
            val intent = Intent(Intent.ACTION_DIAL, Uri.parse("tel:"+mobileTV.text.toString()))
            startActivity(intent)
        } catch (e: Exception) {
            //TODO smth
        }

    }

    private fun initWidgit() {
        prodRV.layoutManager = LinearLayoutManager(this)
        prodRV.isNestedScrollingEnabled = false
        input_orderId.setText(intent.getStringExtra("order_id"))
        getOrderDetails()
    }

    private fun getOrderDetails() {
        val list = ArrayList<NameValuePair>()
        list.add(BasicNameValuePair("order_id", intent.getStringExtra("order_id")))
        var web = Download_web(this, object : OnTaskCompleted {
            override fun onTaskCompleted(response: String) {
                getProdDetails()
                var firstname = ""
                var lastname = ""
                var adrdess1 = ""
                var address2 = ""
                var city = ""
                var state = ""
                var pincode = ""
                try {

                    var obj = JSONObject(response)
                    var array = obj.getJSONArray("data")
                    if (array.length() > 0) {
                        ordrdetailsCV.visibility = View.VISIBLE
                        for (i in 0 until array.length()) {
                            var jo = array.getJSONObject(i)
                            if (jo.optString("meta_key").equals("_created_via")) {
                                ordrcreatedby = jo.optString("meta_value")
                            }
                            if (jo.optString("meta_key").equals("_billing_first_name")) {
                                firstname = jo.optString("meta_value")
                                nameTV.text = firstname
                            }
                            if (jo.optString("meta_key").equals("_billing_last_name")) {
                                lastname = jo.optString("meta_value")
                                nameTV.text = firstname + " " + lastname
                            }
                            if (jo.optString("meta_key").equals("_billing_company")) {
                                var ordrcreatedby = jo.optString("meta_value")
                            }
                            if (jo.optString("meta_key").equals("_billing_email")) {
                                emailTV.text = jo.optString("meta_value")
                            }
                            if (jo.optString("meta_key").equals("_billing_phone")) {
                                mobileTV.text = jo.optString("meta_value")
                            }
                            if (jo.optString("meta_key").equals("_billing_address_1")) {
                                adrdess1 = jo.optString("meta_value")
                            }
                            if (jo.optString("meta_key").equals("_billing_address_2")) {
                                address2 = jo.optString("meta_value")
                            }
                            if (jo.optString("meta_key").equals("_billing_city")) {
                                city = jo.optString("meta_value")
                            }
                            if (jo.optString("meta_key").equals("_billing_state")) {
                                state = jo.optString("meta_value")
                            }
                            if (jo.optString("meta_key").equals("_billing_postcode")) {
                                pincode = jo.optString("meta_value")
                                addressTV.text = adrdess1 + " " + address2 + " " + city + " " + state + " " + pincode
                            }
                            if (jo.optString("meta_key").equals("_payment_method")) {
                                paymentMethod = jo.optString("meta_value")
                            }
                        }
                    }
                } catch (e: JSONException) {
                    e.printStackTrace()
                }

            }


        })

        web.setReqType(false);
        web.setData(list)
        web.execute(WebServices.USERDETAILS_BYORDERID)
    }

    fun getProdDetails() {
        var list = ArrayList<NameValuePair>()
        list.add(BasicNameValuePair("order_id", intent.getStringExtra("order_id")))
        var web = Download_web(this, object : OnTaskCompleted {
            override fun onTaskCompleted(response: String) {
                try {
                    prodList.clear()
                    priceList.clear()
                    var obj = JSONObject(response)
                    var array = obj.getJSONArray("data")
                    if (array.length() > 0) {
                        for (i in 0 until array.length()) {
                            var jo = array.getJSONObject(i)
                            //priceList.clear()
                            var priceArray = jo.getJSONArray("product_price")
                            //priceList.clear()
                            var dummylist = ArrayList<ProdPrice>()
                            for (j in 0 until priceArray.length()) {
                                var priceobj = priceArray.getJSONObject(j)


                                priceList.add(ProdPrice(priceobj.optString("meta_key"), priceobj.optString("meta_value")))
                                dummylist.addAll(priceList)
                                priceList.clear()
                            }
                            prodList.add(ProductDetailsmodel(jo.optString("order_item_type"), jo.optString("order_item_name"), dummylist))

                        }

                        prodRV.adapter = ProductDetailsAdapter(prodList)
                    }
                } catch (e: JSONException) {
                    e.printStackTrace()
                }
            }
        })

        web.setReqType(false)
        web.setApiToken(ClsGeneral.getPreferences(this, WebServices.XAPIKEY))
        web.setData(list)
        web.execute(WebServices.PRODUCTDETAILS_BYORDERID)
    }






    companion object {

        private val REQUEST_PERMISSIONS_REQUEST_CODE = 34
    }
}
