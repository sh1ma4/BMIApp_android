package com.example.bmiapp.ui.input

import android.app.AlertDialog
import android.app.Dialog
import android.content.Context
import android.content.SharedPreferences
import android.content.res.Resources
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.bmiapp.Model.UserDataModel
import com.example.bmiapp.R
import kotlinx.android.synthetic.main.fragment_input.*
import java.text.SimpleDateFormat
import java.util.*

/**
 * 入力画面
 */

class InputFragment : Fragment() {

    private var dataList: MutableList<UserDataModel> = mutableListOf()
    private var res: Resources? = null
    private var sharedPreferenceData: SharedPreferences? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        sharedPreferenceData = context?.getSharedPreferences(context!!.packageName, Context.MODE_PRIVATE)
        res = resources
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
                saveUserData()
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

    private fun saveUserData() {
//        val userDataMap: MutableList<UserDataModel> = mutableMapOf()
//        val strCurrentDate = SimpleDateFormat("yyyyMMdd").format(Date())
//        userDataMap[R.string.date.toString()] = strCurrentDate
//        userDataMap[R.string.input_height.toString()] = edit_height.text.toString()
//        userDataMap[R.string.input_weight.toString()] = edit_weight.text.toString()
//        userDataMap[R.string.bmi.toString()] = text_bmi.text.toString()
//        userDataMap[R.string.memo.toString()] = edit_memo.text.toString()
//
//        dataList.add(sharedPreferenceData?.all.toString())
//
//        val editor = sharedPreferenceData?.edit()
//        editor?.putString(strCurrentDate, userDataMap.toString())
//        editor?.commit()

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
        val strBmi = text_bmi.text.toString()
        if (strHeight.isEmpty() || strWeight.isEmpty()) {
            createAlert(R.string.error_empty_height_or_weight.toString()).show()
            return false
        }
        if (formatFirstDecimal(strHeight).toFloat() / 100 > 3.0 || formatFirstDecimal(strWeight).toFloat() > 300.0) {
            createAlert(R.string.error_invalid_height_or_height.toString()).show()
            return false
        }
        if (strBmi.isEmpty()) {
            createAlert(R.string.error_require_calculation_before_save.toString()).show()
            return false
        }
        return true
    }

    private fun isCurrentDayData(): Boolean {
        for (date in this.dataList) {
            val strDate = SimpleDateFormat("yyyy-MM-dd").format(Date())
            val dateArray = strDate.split("-".toRegex())
            if (date.month == dateArray[1] || date.day == dateArray[2]) {
                return false
            }
        }
        return true
    }

    private fun formatFirstDecimal(value: String): String {
        return String.format("%1$.1f", value.toFloat())
    }
}

