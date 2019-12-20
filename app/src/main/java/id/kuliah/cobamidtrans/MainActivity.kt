package id.kuliah.cobamidtrans

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.github.siyamed.shapeimageview.mask.PorterShapeImageView
import com.midtrans.sdk.corekit.callback.TransactionFinishedCallback
import com.midtrans.sdk.corekit.core.*
import com.midtrans.sdk.corekit.core.themes.CustomColorTheme
import com.midtrans.sdk.corekit.models.*
import com.midtrans.sdk.corekit.models.snap.TransactionResult
import com.midtrans.sdk.uikit.SdkUIFlowBuilder
import java.util.ArrayList
import butterknife.BindView
import butterknife.ButterKnife
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity(), TransactionFinishedCallback, View.OnClickListener {
//==================================================================================================
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        initMid()

        gopayClick.setOnClickListener {
            transactionRequester()
            MidtransSDK.getInstance().startPaymentUiFlow(this, PaymentMethod.GO_PAY)
        }

        alfamartClick.setOnClickListener {
            transactionRequester()
            MidtransSDK.getInstance().startPaymentUiFlow(this, PaymentMethod.ALFAMART)
        }

    }
//==================================================================================================
    private fun initMid() {
        SdkUIFlowBuilder.init()
            .setClientKey(Constant.CLIENT_KEY)
            .setContext(this)
            .setTransactionFinishedCallback { result ->
                Log.w(
                    TAG,
                    result.response.statusMessage
                )
            }
            .setMerchantBaseUrl(Constant.BASE_URL)
            .enableLog(true)
            .setColorTheme(
                CustomColorTheme(
                    "#FFE51255",
                    "#B61548",
                    "#FFE51255"
                )
            )
            .buildSDK()
    }
//==================================================================================================
    override fun onTransactionFinished(transactionResult: TransactionResult) {
        Log.w(TAG, transactionResult.response.statusMessage)
        if((transactionResult.response.statusMessage).isEmpty()){
            Toast.makeText(this, "Kontol", Toast.LENGTH_LONG).show()
        }
        else{
            Toast.makeText(this, "Memek", Toast.LENGTH_LONG).show()
        }
    }
//==================================================================================================
    override fun onClick(v: View) {
        transactionRequester()
        if (v.id == R.id.bankClick) {
            MidtransSDK.getInstance().startPaymentUiFlow(this, PaymentMethod.BANK_TRANSFER)
            overridePendingTransition(R.anim.slide_in, R.anim.slide_out)
        } else if (v.id == R.id.gopayClick) {
            MidtransSDK.getInstance().startPaymentUiFlow(this, PaymentMethod.GO_PAY)
            overridePendingTransition(R.anim.slide_in, R.anim.slide_out)
        }
    }
//==================================================================================================
    private fun transactionRequester() {
        var userDetail: UserDetail? = LocalDataHandler.readObject("user_details", UserDetail::class.java)
        if (userDetail == null) {
            userDetail = UserDetail()
            userDetail.userFullName = "Ahmad Satiri"
            userDetail.email = "bangtiray@gmail.com"
            userDetail.phoneNumber = "08123456789"
            userDetail.userId = "bangtiray-6789"

            val userAddresses = ArrayList<UserAddress>()
            val userAddress = UserAddress()

            userAddress.address = "Jalan Andalas Gang Sebelah No. 1"
            userAddress.city = "Jakarta"
            userAddress.addressType = com.midtrans.sdk.corekit.core.Constants.ADDRESS_TYPE_BOTH
            userAddress.zipcode = "12345"
            userAddress.country = "IDN"
            userAddresses.add(userAddress)
            userDetail.userAddresses = userAddresses
            LocalDataHandler.saveObject("user_details", userDetail)
        }

        val transactionRequest = TransactionRequest(System.currentTimeMillis().toString() + "", 8000.0)
        val itemDetails2 = ItemDetails("Tiket Kereta", 8000.0, 1, "Prameks")


        val itemDetailsList = ArrayList<ItemDetails>()
        itemDetailsList.add(itemDetails2)

        transactionRequest.itemDetails = itemDetailsList
        MidtransSDK.getInstance().transactionRequest = transactionRequest
    }
//==================================================================================================
    companion object {
        private val TAG = "transactionresult"
    }
//==================================================================================================
}
