package com.nordresa.travel.ui.search

import android.content.Context
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import android.widget.ProgressBar
import androidx.appcompat.app.AppCompatActivity
import androidx.core.widget.addTextChangedListener
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.nordresa.travel.databinding.FragmentSearchBinding
import com.nordresa.travel.ui.base.BaseFragment
import com.nordresa.travel.ui.home.HomeFragment
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel


class SearchFragment : BaseFragment<FragmentSearchBinding>() {

    private lateinit var mAdapter : FilteredStopsAdapter
    private val viewModel: SearchViewModel by viewModel()
    private lateinit var progressBar : ProgressBar
    private val args: SearchFragmentArgs by navArgs()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentSearchBinding.inflate(inflater,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setActionBar()
        setupRecyclerView()

        binding.etSearch.requestFocus()
        progressBar  = binding.pbSearch

        // Get the field type from arguments
        val fieldType = args.fieldType
        println("--> SearchFragment opened for field: $fieldType")

        // Update title or hint based on field type
        when (fieldType) {
            HomeFragment.FIELD_DEPARTURE -> {
                binding.etSearch.hint = "Search departure station"
                // You can also update the toolbar title if needed
            }
            HomeFragment.FIELD_DESTINATION -> {
                binding.etSearch.hint = "Search destination station"
            }
        }


        // Debounce user input
        lifecycleScope.launch {
            binding.etSearch.textChanges() // <-- custom extension
                .debounce(400)             // wait 400ms after typing stops
                .distinctUntilChanged()
                .collect { query ->
                    if (query.isBlank()) {
                        viewModel.clearStops()
                    } else {

                        viewModel.fetchStopsData(query)
                    }
                }
        }

        observeFilteredList()

        // Show keyboard
        val imm = requireContext().getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.showSoftInput(binding.etSearch, InputMethodManager.SHOW_IMPLICIT)
    }


    fun setupRecyclerView(){
        mAdapter = FilteredStopsAdapter(
            context = this.requireContext(),
            onItemClicked = { stopsData ->
                // Pass both the selected data and the field type back to HomeFragment
                val fieldType = args.fieldType
                val action = SearchFragmentDirections
                    .actionSearchFragmentToHomeFragment(stopsData, fieldType)
                findNavController().navigate(action)
            }
        )
        binding.rcvSearchResults.apply {
            adapter = mAdapter
            layoutManager = LinearLayoutManager(requireContext())
        }
    }

    fun observeFilteredList() {
        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.uiState.collect { state ->
                    // Hide progress bar for all states except Loading
                    if (state !is StopUiState.Loading) {
                        hideProgressBar(progressBar)
                        binding.progressOverlay.visibility = View.GONE
                    }

                    when (state) {
                        is StopUiState.StopData -> {
                            println("--> data is: ${state.stops}")
                            binding.rcvSearchResults.visibility = View.VISIBLE
                            mAdapter.submitList(state.stops)
                        }
                        StopUiState.Empty -> {
                            binding.rcvSearchResults.visibility = View.VISIBLE
                            mAdapter.submitList(emptyList())
                        }
                        StopUiState.Loading -> {
                            showProgressBar(progressBar)
                            binding.progressOverlay.visibility = View.VISIBLE
                        }
                        is StopUiState.Error -> {
                            binding.rcvSearchResults.visibility = View.VISIBLE
                            // Handle error - maybe show a toast or error message
                            println("--> Error: ${state.message}")
                            mAdapter.submitList(emptyList())
                        }
                        is StopUiState.NoNetwork -> {
                            binding.rcvSearchResults.visibility = View.VISIBLE
                            // Handle no network - maybe show a toast or error message
                            println("--> No network available")
                            mAdapter.submitList(emptyList())
                        }
                    }
                }
            }
        }
    }


    private fun setActionBar(){
        val toolbarComponent = (activity as AppCompatActivity)
        toolbarComponent.setSupportActionBar(binding.searchToolbar)
        toolbarComponent.supportActionBar?.setDisplayHomeAsUpEnabled(true)

        binding.searchToolbar.setNavigationOnClickListener {
            requireActivity().onBackPressedDispatcher.onBackPressed()
        }
    }

}

fun EditText.textChanges(): Flow<String> = callbackFlow {
    val watcher = object : TextWatcher {
        override fun afterTextChanged(s: Editable?) {
            trySend(s.toString()).isSuccess
        }

        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) = Unit
        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) = Unit
    }

    addTextChangedListener(watcher)
    awaitClose { removeTextChangedListener(watcher) }
}
