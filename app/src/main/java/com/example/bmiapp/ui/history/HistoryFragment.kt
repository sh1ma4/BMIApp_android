package com.example.bmiapp.ui.history

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.bmiapp.Model.BmiModel
import com.example.bmiapp.R
import com.example.bmiapp.ui.history.RecycleView.ViewAdapter
import com.squareup.moshi.JsonAdapter
import com.squareup.moshi.Moshi
import com.squareup.moshi.Types
import java.lang.reflect.ParameterizedType

/**
 * 履歴画面
 */

class HistoryFragment : Fragment() {

    private val type: ParameterizedType = Types.newParameterizedType(List::class.java,BmiModel::class.java)
    private val jsonAdapter: JsonAdapter<List<BmiModel>> = Moshi.Builder().build().adapter(type)

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        super.onCreateView(inflater, container, savedInstanceState)
        return inflater.inflate(R.layout.fragment_history, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val recyclerView: RecyclerView = view.findViewById(R.id.recyclerView)
        recyclerView.apply{
            setHasFixedSize(true)
            layoutManager = LinearLayoutManager(context)
            adapter = ViewAdapter(getSaveData(), context)
        }
    }

    override fun setUserVisibleHint(isVisibleToUser: Boolean) {
        val lastValue = userVisibleHint
        val adapter: ViewAdapter? = null
        super.setUserVisibleHint(isVisibleToUser)
        if (!lastValue && isVisibleToUser) {
            adapter?.refresh(getSaveData())
        }
    }

    private fun getSaveData(): MutableList<BmiModel> {
        val sharedPreferenceData: SharedPreferences = context!!.getSharedPreferences(context!!.packageName, Context.MODE_PRIVATE)
        val saveData = sharedPreferenceData.getString("History", "[]")
        return jsonAdapter.fromJson(saveData) as MutableList<BmiModel>
    }
}
