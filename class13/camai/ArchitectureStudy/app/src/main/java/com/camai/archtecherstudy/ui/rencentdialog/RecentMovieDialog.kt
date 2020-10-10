package com.camai.archtecherstudy.ui.rencentdialog

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.databinding.Observable
import androidx.fragment.app.DialogFragment
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.camai.archtecherstudy.R
import com.camai.archtecherstudy.data.repository.MovieRepositoryImpl
import com.camai.archtecherstudy.databinding.RecentMovieListPopupBinding
import com.camai.archtecherstudy.observer.RecentViewModel

class RecentMovieDialog(var keywork: (String) -> Unit) : DialogFragment() {


    private val vm: RecentViewModel by lazy {
        RecentViewModel()
    }
    private val recentMovieAdapter: RecentMovieAdapter by lazy {
        RecentMovieAdapter {
            //  recycler View item click movie name to Activity
            keywork.invoke(it)
            vm.closeDialog()
        }
    }
    
    private lateinit var binding: RecentMovieListPopupBinding

    override fun onStart() {
        super.onStart()
        dialog?.window?.setLayout(
            WindowManager.LayoutParams.MATCH_PARENT,
            WindowManager.LayoutParams.WRAP_CONTENT
        )
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding =
            DataBindingUtil.bind(
                inflater.inflate(
                    R.layout.recent_movie_list_popup,
                    container,
                    false
                )
            )!!
        binding.vm = vm
        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setAdapterAndRecyclerViewInit()

        setupObserverCallBack()

    }

    //  RecyclerView Adapter Set
    private fun setAdapterAndRecyclerViewInit() {

        recentMovieAdapter.notifyDataSetChanged()

        //  recycler view init and adapter connect
        binding.recyclerRecentView.run {
            adapter = recentMovieAdapter
            setHasFixedSize(false)
            addItemDecoration(DividerItemDecoration(context, LinearLayoutManager.VERTICAL))
        }

        //  get Recent Data
        vm.setRecentData()
    }
    
    private fun setupObserverCallBack() {
        vm.isFailed.addOnPropertyChangedCallback(object : Observable.OnPropertyChangedCallback() {
            override fun onPropertyChanged(sender: Observable?, propertyId: Int) {
                Toast.makeText(requireContext(), "최근 검색된 항목이 없습니다.", Toast.LENGTH_LONG).show()
            }
        })

        vm.isSuccess.addOnPropertyChangedCallback(object : Observable.OnPropertyChangedCallback() {
            override fun onPropertyChanged(sender: Observable?, propertyId: Int) {
                var name: String = vm.clickKeyword.get().toString()
                keywork.invoke(name)
                dismiss()
            }
        })

        vm.dimissDialog.addOnPropertyChangedCallback(object :
            Observable.OnPropertyChangedCallback() {
            override fun onPropertyChanged(sender: Observable?, propertyId: Int) {
                dismiss()
            }
        })

    }


    companion object {
        const val TAG = "MovieSearchDialog"
    }
}