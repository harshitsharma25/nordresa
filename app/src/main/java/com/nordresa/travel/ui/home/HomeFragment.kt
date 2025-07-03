package com.nordresa.travel.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.google.firebase.auth.FirebaseAuth
import com.nordresa.travel.R
import com.nordresa.travel.databinding.FragmentHomeBinding
import androidx.navigation.fragment.navArgs
import com.nordresa.travel.models.JourneyInput
import com.nordresa.travel.models.StopsData
import com.nordresa.travel.ui.base.BaseFragment


class HomeFragment : BaseFragment<FragmentHomeBinding>() {

    private val args: HomeFragmentArgs by navArgs()
    private val viewModel: HomeViewModel by activityViewModels()

    companion object {
        const val FIELD_DEPARTURE = "departure"
        const val FIELD_DESTINATION = "destination"
    }


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentHomeBinding.inflate(inflater,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Handle returned data from SearchFragment
        val stopsData = args.stopsData
        val fieldType = args.fieldType

        if (stopsData != null && fieldType != null) {
            when (fieldType) {
                FIELD_DEPARTURE -> {
                    viewModel.departureStopsData = stopsData
                    println("--> Set departure: ${stopsData.name}")
                }
                FIELD_DESTINATION -> {
                    viewModel.destinationStopsData = stopsData
                    println("--> Set destination: ${stopsData.name}")
                }
            }
        }

        // Always restore both fields from stored data
        viewModel.departureStopsData?.let {
            binding.etDepartFrom.setText(it.name)
        }

        viewModel.destinationStopsData?.let {
            binding.etDestination.setText(it.name)
        }


        // Set click listeners
        binding.etDepartFrom.setOnClickListener {
            showSearchFragment(FIELD_DEPARTURE)
        }

        binding.etDestination.setOnClickListener {
            showSearchFragment(FIELD_DESTINATION)
        }

        binding.btnShowRoute.setOnClickListener {
            showRouteData()
        }

        binding.btnReset.setOnClickListener {
            viewModel.departureStopsData = null
            viewModel.destinationStopsData = null
            binding.etDepartFrom.setText("")
            binding.etDestination.setText("")
        }
    }

    private fun showSearchFragment(fieldType: String) {
        val action = HomeFragmentDirections
            .actionHomeFragmentToSearchFragment(fieldType)
        findNavController().navigate(action)
    }

    private fun showRouteData(){
        val departStation = binding.etDepartFrom.text.toString().trim { it <= ' ' }
        val destinationStation = binding.etDestination.text.toString().trim { it <= ' ' }

        if(validateJourney(departStation,destinationStation)){
            if( (viewModel.departureStopsData != null) && (viewModel.destinationStopsData != null) && (viewModel.departureStopsData?.extId != viewModel.destinationStopsData?.extId)){
                val journeyInput = JourneyInput(departure = viewModel.departureStopsData!!, destination = viewModel.destinationStopsData!!)
                val action = HomeFragmentDirections.actionHomeFragmentToJourneyFragment(journeyInput)

                findNavController().navigate(action)
            } else{
                showErrorSnackBar("Departure and Destination Stations Can't be Same")
            }
        }
    }

    private fun validateJourney(from : String,to : String) : Boolean{
        return when{
            from.isEmpty() -> {
                showErrorSnackBar("Please Select Depart Station")
                false
            }
            to.isEmpty() -> {
                showErrorSnackBar("Please Select Destination Station")
                false
            }
            else -> {
                true
            }
        }
    }

}