package com.example.bmiapp.ui.history

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.bmiapp.Model.UserDataModel
import com.example.bmiapp.R
import com.example.bmiapp.ui.history.RecycleView.ViewAdapter
import com.squareup.moshi.JsonAdapter
import com.squareup.moshi.Moshi
import com.squareup.moshi.Types

/**
 * 履歴画面
 */

class HistoryFragment : Fragment() {

    private var sharedPreferenceData: SharedPreferences? = null
    val type = Types.newParameterizedType(List::class.java,UserDataModel::class.java)
    private val jsonAdapter: JsonAdapter<List<UserDataModel>> = Moshi.Builder().build().adapter(type)

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        sharedPreferenceData = context?.getSharedPreferences(context!!.packageName, Context.MODE_PRIVATE)
        return inflater.inflate(R.layout.fragment_history, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val recyclerView: RecyclerView = view.findViewById(R.id.recyclerView)
        recyclerView.apply {
            setHasFixedSize(true)
            layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
            adapter = ViewAdapter(getSaveData())
        }
    }

    private fun getSaveData(): MutableList<UserDataModel> {
        val saveData = sharedPreferenceData?.getString("History", "[]")
        return jsonAdapter.fromJson(saveData) as  MutableList<UserDataModel>
    }
}
