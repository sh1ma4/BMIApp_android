package com.example.bmiapp.ui.input

import android.app.AlertDialog
import android.app.Dialog
import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.bmiapp.Model.UserDataModel
import com.example.bmiapp.R
import com.squareup.moshi.JsonAdapter
import com.squareup.moshi.Moshi
import com.squareup.moshi.Types
import kotlinx.android.synthetic.main.fragment_input.*
import java.text.SimpleDateFormat
import java.util.*

/**
 * 入力画面
 */

class InputFragment : Fragment() {

    private var sharedPreferenceData: SharedPreferences? = null
    val type = Types.newParameterizedType(List::class.java,UserDataModel::class.java)
    val jsonAdapter: JsonAdapter<List<UserDataModel>> = Moshi.Builder().build().adapter(type)


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        sharedPreferenceData = context?.getSharedPreferences(context!!.packageName, Context.MODE_PRIVATE)
        return inflater.inflate(R.layout.fragment_input, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        // [BMIを計算する]ボタンアクション
        calc_bmi_button.setOnClickListener {
            if (validateInputValue()) {
                text_bmi.text = calculateBMI(edit_height.text.toString(), edit_weight.text.toString())
            }
        }
        // [削除]ボタンアクション
        delete_button.setOnClickListener {
            initData()
        }
        // [保存する]ボタンアクション
        save_button.setOnClickListener {
            if (validateInputValue()) {
                if (text_bmi.text.toString().isEmpty()) {
                    createAlert(R.string.error_require_calculation_before_save.toString()).show()
                } else {
                    saveUserData()
                }
            }
        }
    }

    private fun calculateBMI(height: String, weight: String): String {
        val floatHeight = height.toFloat() / 100
        val floatWeight = weight.toFloat()
        return formatFirstDecimal((floatWeight / (floatHeight * floatHeight)).toString())
    }

    private fun initData() {
        edit_height.setText("")
        edit_weight.setText("")
        text_bmi.text = ""
        edit_memo.setText("")
    }

    private fun storeDateToArray(): List<String> {
        val strDate = SimpleDateFormat("yyyy-MM-dd").format(Date())
       return strDate.split("-".toRegex())
    }

    private fun saveUserData() {
        val dateArray = storeDateToArray()
        val userData = UserDataModel(dateArray[1], dateArray[2], edit_height.text.toString(), edit_weight.text.toString(), text_bmi.text.toString(), edit_memo.text.toString())
        val saveData = sharedPreferenceData?.getString("History", "[]")
        val saveDataList : MutableList<UserDataModel>  = jsonAdapter.fromJson(saveData) as  MutableList<UserDataModel>

        if (isCurrentDayData()) {
            // 更新
            // TODO: 修正する
        } else {
            // データ登録
            saveDataList.add(userData)
            val editor = sharedPreferenceData?.edit()
            val json = jsonAdapter.toJson(saveDataList)
            editor?.putString("History",json)
            Toast.makeText(context, "BMIデータを保存しました", Toast.LENGTH_LONG).show()
        }
    }

    private fun createAlert(dialogMessage: String): Dialog {
        AlertDialog.Builder(activity).setTitle("")
                                     .setMessage(dialogMessage)
                                     .setPositiveButton("閉じる"){ _, _ ->
                                     }
        return AlertDialog.Builder(activity).create()
    }

    private fun validateInputValue(): Boolean {
        val strHeight = edit_height.text.toString()
        val strWeight = edit_weight.text.toString()
        if (strHeight.isEmpty() || strWeight.isEmpty()) {
            createAlert(R.string.error_empty_height_or_weight.toString()).show()
            return false
        }
        if (formatFirstDecimal(strHeight).toFloat() / 100 > 3.0 || formatFirstDecimal(strWeight).toFloat() > 300.0) {
            createAlert(R.string.error_invalid_height_or_height.toString()).show()
            return false
        }
        return true
    }

    private fun isCurrentDayData(): Boolean {
        val dateArray = storeDateToArray()
        val userData: MutableMap<String, *>? = sharedPreferenceData?.all
        if (userData != null) {
            for (data in userData) {
                Log.d("", data.value as String)
                // TODO: 修正
                if (data.value == dateArray[1] || data.value == dateArray[2]) {
                    return false
                }
            }
        }
        return true
    }

    private fun formatFirstDecimal(value: String): String {
        return String.format("%1$.1f", value.toFloat())
    }
}

