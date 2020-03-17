package com.example.bmiapp.ui.input

import android.app.AlertDialog
import android.app.Dialog
import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.bmiapp.Model.BmiModel
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

    private val type = Types.newParameterizedType(List::class.java,BmiModel::class.java)
    private val jsonAdapter: JsonAdapter<List<BmiModel>> = Moshi.Builder().build().adapter(type)


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        super.onCreateView(inflater, container, savedInstanceState)
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
                    showAlert(resources.getString(R.string.error_require_calculation_before_save))
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
        val strDate = SimpleDateFormat("yyyy-MM-dd",Locale.JAPAN).format(Date())
       return strDate.split("-".toRegex())
    }

    private fun saveUserData() {
        val sharedPreferenceData = context!!.getSharedPreferences(context!!.packageName, Context.MODE_PRIVATE)
        val dateArray = storeDateToArray()
        val userData = BmiModel(dateArray[1], dateArray[2], edit_height.text.toString(), edit_weight.text.toString(), text_bmi.text.toString(), edit_memo.text.toString())
        val saveData = sharedPreferenceData.getString("History", "[]")
        val saveDataList : MutableList<BmiModel>  = jsonAdapter.fromJson(saveData) as  MutableList<BmiModel>

        // データ登録
//        if (isCurrentDayData()) {
            saveDataList.add(userData)
            val editor = sharedPreferenceData.edit()
            editor.apply {
                val json = jsonAdapter.toJson(saveDataList)
                editor.putString("History",json)
                Toast.makeText(context, "BMIデータを保存しました", Toast.LENGTH_SHORT).show()
                editor.apply()
            }
//        }
    }

    private fun showAlert (message: String) {
        val builder = android.app.AlertDialog.Builder(activity)
        builder.setTitle("")
            .setMessage(message)
            .create().show()
    }

    private fun validateInputValue(): Boolean {
        val strHeight = edit_height.text.toString()
        val strWeight = edit_weight.text.toString()
        if (strHeight.isEmpty() || strWeight.isEmpty()) {
            showAlert(resources.getString(R.string.error_empty_height_or_weight))
            return false
        }
        if (formatFirstDecimal(strHeight).toFloat() / 100 > 3.0 || formatFirstDecimal(strWeight).toFloat() > 300.0) {
            showAlert(resources.getString(R.string.error_invalid_height_or_height))
            return false
        }
        return true
    }

    private fun isCurrentDayData(): Boolean {
        val dateArray = storeDateToArray()
        val sharedPreferenceData = context!!.getSharedPreferences(context!!.packageName, Context.MODE_PRIVATE)
        val saveData = sharedPreferenceData.getString("History", "[]")
        val savedData = jsonAdapter.fromJson(saveData) as MutableList<BmiModel>
        if (savedData.size > 0) {
            for (data in savedData) {
                if (data.month == dateArray[1] || data.day == dateArray[2]) {
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

