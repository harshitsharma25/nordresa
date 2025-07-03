package com.nordresa.travel.ui.journey

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.navigation.fragment.navArgs
import com.nordresa.travel.databinding.FragmentJourneyBinding
import com.nordresa.travel.ui.base.BaseFragment
import androidx.core.view.isVisible
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.Dot
import com.google.android.gms.maps.model.Gap
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.LatLngBounds
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.gms.maps.model.PolylineOptions
import com.nordresa.travel.R
import com.nordresa.travel.models.StopsData
import org.koin.androidx.viewmodel.ext.android.viewModel

class JourneyFragment : BaseFragment<FragmentJourneyBinding>(), OnMapReadyCallback {

    private val args: JourneyFragmentArgs by navArgs()
    private val viewModel: JourneyViewModel by viewModel()
    private var googleMap: GoogleMap? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentJourneyBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupActionBar()
        initializeJourneyData()
        setupMapFragment()
        setupClickListeners()
    }

    private fun initializeJourneyData() {
        val departure = args.journeyInput.departure
        val destination = args.journeyInput.destination

        viewModel.departureLatLng = LatLng(departure.lat, departure.lon)
        viewModel.destinationLatLng = LatLng(destination.lat, destination.lon)

        println("--> Journey initialized: ${departure.name} → ${destination.name}")
    }

    private fun setupMapFragment() {
        val fragment = childFragmentManager.findFragmentById(R.id.mapContainer) as SupportMapFragment
        fragment.getMapAsync(this)
    }

    override fun onMapReady(map: GoogleMap) {
        googleMap = map

        googleMap?.apply {
            uiSettings.isZoomControlsEnabled = true
            uiSettings.isMyLocationButtonEnabled = false
            uiSettings.isMapToolbarEnabled = false
        }

        drawRouteOnMap()
    }

    private fun drawRouteOnMap() {
        val departure = args.journeyInput.departure
        val destination = args.journeyInput.destination
        val departureLatLng = viewModel.departureLatLng
        val destinationLatLng = viewModel.destinationLatLng

        if (departureLatLng != null && destinationLatLng != null && googleMap != null) {
            googleMap?.apply {
                clear()

                addMarker(
                    MarkerOptions()
                        .position(departureLatLng)
                        .title(departure.name)
                        .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN))
                )

                addMarker(
                    MarkerOptions()
                        .position(destinationLatLng)
                        .title(destination.name)
                        .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED))
                )

                addPolyline(
                    PolylineOptions()
                        .add(departureLatLng, destinationLatLng)
                        .color(ContextCompat.getColor(requireContext(), R.color.primary_app_color))
                        .width(8f)
                        .pattern(listOf(Dot(), Gap(10f)))
                )

                val bounds = LatLngBounds.Builder()
                    .include(departureLatLng)
                    .include(destinationLatLng)
                    .build()

                binding.root.post {
                    try {
                        animateCamera(CameraUpdateFactory.newLatLngBounds(bounds, 150))
                    } catch (e: Exception) {
                        moveCamera(CameraUpdateFactory.newLatLngBounds(bounds, 150))
                    }
                }
            }

            binding.mapPlaceholder.visibility = View.GONE
        }
    }

    private fun setupActionBar() {
        val activity = requireActivity() as AppCompatActivity
        activity.setSupportActionBar(binding.journeyToolbar)
        activity.supportActionBar?.setDisplayHomeAsUpEnabled(true)

        val title = "${args.journeyInput.departure.name} → ${args.journeyInput.destination.name}"
        activity.supportActionBar?.title = title

        binding.journeyToolbar.setNavigationOnClickListener {
            requireActivity().onBackPressedDispatcher.onBackPressed()
        }
    }

    private fun setupClickListeners() {
        binding.btnRetry.setOnClickListener {
            searchTrips()
        }
    }

    private fun searchTrips() {
        showLoadingState()
        val departure = args.journeyInput.departure
        val destination = args.journeyInput.destination
        println("--> Searching trips from ${departure.name} to ${destination.name}")
        // viewModel.searchTrips(...)
    }

    private fun showLoadingState() {
        binding.apply {
            loadingLayout.isVisible = true
            recyclerViewTrips.isVisible = false
            tvAvailableTripsHeader.isVisible = false
            errorLayout.isVisible = false
        }
    }

    private fun showErrorState(errorMessage: String) {
        binding.apply {
            recyclerViewTrips.isVisible = false
            tvAvailableTripsHeader.isVisible = false
            errorLayout.isVisible = true
            tvErrorMessage.text = errorMessage
        }
    }
}
