package ru.vsu.cs.tp.richfamily.view.credit

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import ru.vsu.cs.tp.richfamily.R
import ru.vsu.cs.tp.richfamily.databinding.FragmentCreditBinding

class CreditFragment : Fragment() {

    private lateinit var binding: FragmentCreditBinding
    private val args by navArgs<CreditFragmentArgs>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentCreditBinding.inflate(
            inflater,
            container,
            false
        )
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setCredit()
        binding.continueButton.setOnClickListener {
            findNavController()
                .navigate(R.id.action_creditFragment_to_creditListFragment)
        }
    }

    private fun setCredit() = with(binding) {
        creditTotalTv.text = args.credit.cr_all_sum.toString().format("%.3f")
        percentageTv.text = args.credit.cr_percent.toString().format("%.3f")
        periodTv.text = args.credit.cr_period.toString().format("%.3f")
        crPercentsSumTv.text = args.credit.cr_percents_sum.toString().format("%.3f")
        crSumPlusPercentsTv.text = args.credit.cr_sum_plus_percents.toString().format("%.3f")
        monthlySumTv.text = args.credit.cr_month_pay.toString().format("%.3f")
    }
}