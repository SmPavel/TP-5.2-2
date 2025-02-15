package ru.vsu.cs.tp.richfamily.view.template

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import ru.vsu.cs.tp.richfamily.MainActivity
import ru.vsu.cs.tp.richfamily.api.model.category.Category
import ru.vsu.cs.tp.richfamily.api.model.wallet.Wallet
import ru.vsu.cs.tp.richfamily.api.service.CategoryApi
import ru.vsu.cs.tp.richfamily.api.service.TemplateApi
import ru.vsu.cs.tp.richfamily.api.service.WalletApi
import ru.vsu.cs.tp.richfamily.databinding.FragmentAddTemplateBinding
import ru.vsu.cs.tp.richfamily.repository.CategoryRepository
import ru.vsu.cs.tp.richfamily.repository.TemplateRepository
import ru.vsu.cs.tp.richfamily.repository.WalletRepository
import ru.vsu.cs.tp.richfamily.utils.Constants
import ru.vsu.cs.tp.richfamily.viewmodel.CategoryViewModel
import ru.vsu.cs.tp.richfamily.viewmodel.TemplateViewModel
import ru.vsu.cs.tp.richfamily.viewmodel.WalletViewModel
import ru.vsu.cs.tp.richfamily.viewmodel.factory.AnyViewModelFactory

class AddTemplateFragment : Fragment() {

    private lateinit var binding: FragmentAddTemplateBinding
    private lateinit var token: String
    private lateinit var walViewModel: WalletViewModel
    private lateinit var temViewModel: TemplateViewModel
    private lateinit var catViewModel: CategoryViewModel
    private lateinit var catList: List<Category>
    private lateinit var walList: List<Wallet>

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentAddTemplateBinding.inflate(
            inflater,
            container,
            false
        )
        token = MainActivity.getToken()
        initViewModels(token = token)
        initAdapters()
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        catViewModel.catList.observe(viewLifecycleOwner) {
            catList = it
        }
        walViewModel.walletList.observe(viewLifecycleOwner) {
            walList = it
        }
        binding.addTemplateButton.setOnClickListener {
            val rbText: String = if (binding.consumptionRb.isChecked) {
                Constants.CONS_TEXT
            } else {
                Constants.INCOME_TEXT
            }

            if (inputCheck(
                    wallet = binding.filledScore.text.toString(),
                    category = binding.filledCategory.text.toString(),
                    opType = rbText,
                    opRecipient = binding.senderEt.toString(),
                    opSum = binding.totalEt.text.toString(),
                    opComment = binding.commentEt.text.toString()
                )) {
                with(binding) {
                    temViewModel.addTemplate(
                        tempName = templateNameEt.text.toString(),
                        walletId = getWalletFromACTV(filledScore.text.toString()),
                        categoryId = getCategoryFromACTV(filledCategory.text.toString()),
                        opType = rbText,
                        opRecipient = senderEt.text.toString(),
                        opSum =  totalEt.text.toString().toFloat(),
                        opComment = commentEt.text.toString()
                    )
                }
                findNavController().popBackStack()
            } else {
                Toast.makeText(
                    requireActivity(),
                    Constants.COMP_FIELDS_TOAST,
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
    }

    private fun getWalletFromACTV(selectedItem: String): Int {
        val selectedClass = walList.find {
            "${it.acc_name} ${it.acc_sum} ${it.acc_currency}" == selectedItem
        }
        return selectedClass!!.id
    }

    private fun getCategoryFromACTV(selectedItem: String): Int {
        val selectedClass = catList.find {
            it.cat_name == selectedItem
        }
        return selectedClass!!.id
    }

    private fun initAdapters() = with(binding) {
        if (token.isNotBlank()) {
            catViewModel.catList.observe(viewLifecycleOwner) {
                val catAdapter = ArrayAdapter(
                    requireActivity(),
                    android.R.layout.simple_list_item_1,
                    it.map { cat -> cat.cat_name })
                filledCategory.setAdapter(catAdapter)
            }
            walViewModel.walletList.observe(viewLifecycleOwner) {
                val walAdapter = ArrayAdapter(
                    requireActivity(),
                    android.R.layout.simple_list_item_1,
                    it.map { wal -> "${wal.acc_name} ${wal.acc_sum} ${wal.acc_currency}" })
                filledScore.setAdapter(walAdapter)
            }
        }
    }


    private fun inputCheck(
        wallet: String,
        category: String,
        opType: String,
        opRecipient: String,
        opSum: String,
        opComment: String
    ): Boolean {
        if (wallet.isNotBlank() &&
            category.isNotBlank() &&
            opType.isNotBlank() &&
            opRecipient.isNotBlank() &&
            opSum.isNotBlank() &&
            opComment.isNotBlank()
        ) {
            return true
        }
        return false
    }

    private fun initViewModels(token: String) {
        if (token.isNotEmpty()) {
            // Operation vm
            val templateApi = TemplateApi.getTemplatesApi()!!
            val temRepository = TemplateRepository(templateApi = templateApi, token = token)
            temViewModel = ViewModelProvider(
                requireActivity(),
                AnyViewModelFactory(
                    repository = temRepository,
                    token = token
                )
            )[TemplateViewModel::class.java]
            // Category vm
            val categoryApi = CategoryApi.getCategoryApi()!!
            val categoryRepository = CategoryRepository(categoryApi = categoryApi, token = token)
            catViewModel = ViewModelProvider(
                requireActivity(),
                AnyViewModelFactory(
                    repository = categoryRepository,
                    token = token
                )
            )[CategoryViewModel::class.java]
            catViewModel.getAllCategories()
            // Wallet vm
            val walletApi = WalletApi.getWalletApi()!!
            val walletRepository = WalletRepository(walletApi = walletApi, token = token)
            walViewModel = ViewModelProvider(
                requireActivity(),
                AnyViewModelFactory(
                    repository = walletRepository,
                    token = token
                )
            )[WalletViewModel::class.java]
            walViewModel.getAllWallets()
        }
    }
}