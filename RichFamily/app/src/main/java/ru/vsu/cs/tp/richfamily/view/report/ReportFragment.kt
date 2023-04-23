package ru.vsu.cs.tp.richfamily.view.report

import android.os.Build
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import ru.vsu.cs.tp.richfamily.adapter.*
import ru.vsu.cs.tp.richfamily.api.model.Income
import ru.vsu.cs.tp.richfamily.api.model.Operation
import ru.vsu.cs.tp.richfamily.app.App
import ru.vsu.cs.tp.richfamily.databinding.FragmentReportBinding
import java.time.LocalDate
import java.time.LocalTime
import java.time.temporal.ChronoUnit

class ReportFragment : Fragment() {
    private lateinit var adapter: OperationRVAdapter

    private lateinit var binding: FragmentReportBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentReportBinding.inflate(
            inflater,
            container,
            false
        )

        return binding.root
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        App.initRetrofit()
        initRcView()
        getCons()
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun getCons() {
        CoroutineScope(Dispatchers.IO).launch {
//            val list = App.serviceAPI.getWallets(loginViewModel.token.value!!)
            val list = mutableListOf(
                Operation(0, "Доход 1", "Расход",1000f, LocalDate.now(), LocalTime.now().truncatedTo(
                    ChronoUnit.MINUTES)),
                Operation(0, "Доход 2", "Доход", 299f, LocalDate.now(), LocalTime.now().truncatedTo(
                    ChronoUnit.MINUTES)),
                Operation(0, "Доход 1", "Расход",1012300f, LocalDate.now(), LocalTime.now().truncatedTo(
                    ChronoUnit.MINUTES)),
                Operation(0, "Доход 2", "Доход", 29329f, LocalDate.now(), LocalTime.now().truncatedTo(
                    ChronoUnit.MINUTES)),
            )
            requireActivity().runOnUiThread {
                adapter.submitList(list)
            }
        }
    }

    private fun initRcView() = with(binding) {
        adapter = OperationRVAdapter()
        operationsRv.layoutManager = LinearLayoutManager(context)
        operationsRv.adapter = adapter
    }
}