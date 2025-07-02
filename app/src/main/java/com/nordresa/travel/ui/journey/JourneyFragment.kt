package com.nordresa.travel.ui.journey

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.navArgs
import com.nordresa.travel.databinding.FragmentJourneyBinding
import com.nordresa.travel.ui.base.BaseFragment
import androidx.core.view.isVisible
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.coroutines.launch

class JourneyFragment : BaseFragment<FragmentJourneyBinding>() {

    private val args: JourneyFragmentArgs by navArgs()
    private val viewModel: JourneyViewmodel by activityViewModels()

    private lateinit var tripsAdapter: TripsAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentJourneyBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupActionBar()
        setupRecyclerView()
        setupObservers()
        setupClickListeners()

        // Initialize journey data and automatically load everything
        initializeJourney()
    }

    private fun setupActionBar() {
        val toolbarComponent = (activity as AppCompatActivity)
        toolbarComponent.setSupportActionBar(binding.journeyToolbar)
        toolbarComponent.supportActionBar?.setDisplayHomeAsUpEnabled(true)

        // Set dynamic title based on route
        val departure = args.journeyInput.departure.name
        val destination = args.journeyInput.destination.name
        toolbarComponent.supportActionBar?.title = "$departure → $destination"

        binding.journeyToolbar.setNavigationOnClickListener {
            requireActivity().onBackPressedDispatcher.onBackPressed()
        }
    }

    private fun setupRecyclerView() {
        tripsAdapter = TripsAdapter { trip ->
            onTripSelected(trip)
        }

        binding.recyclerViewTrips.apply {
            adapter = tripsAdapter
            layoutManager = LinearLayoutManager(requireContext())
            setHasFixedSize(true)
        }
    }

    private fun setupObservers() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.journeyState.collect { state ->
                updateUI(state)
            }
        }

        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.mapState.collect { mapState ->
                updateMapUI(mapState)
            }
        }
    }

    private fun setupClickListeners() {
        binding.btnRetry.setOnClickListener {
            searchTrips()
            initializeMap()
        }
    }

    private fun initializeJourney() {
        val journeyInput = args.journeyInput

        // Initialize viewmodel with journey data
        val departure = Station(
            name = journeyInput.departure.name,
            latitude = journeyInput.departure.latitude,
            longitude = journeyInput.departure.longitude,
            id = journeyInput.departure.id
        )

        val destination = Station(
            name = journeyInput.destination.name,
            latitude = journeyInput.destination.latitude,
            longitude = journeyInput.destination.longitude,
            id = journeyInput.destination.id
        )

        viewModel.initializeJourney(departure, destination)

        // Automatically start both map loading and trip search
        initializeMap()
        searchTrips()

        println("--> Journey initialized: ${departure.name} → ${destination.name}")
        println("--> Departure coords: ${departure.latitude}, ${departure.longitude}")
        println("--> Destination coords: ${destination.latitude}, ${destination.longitude}")
    }

    private fun initializeMap() {
        val departure = args.journeyInput.departure
        val destination = args.journeyInput.destination

        // Initialize map with coordinates
        viewModel.initializeMap(
            departureCoords = Pair(departure.latitude, departure.longitude),
            destinationCoords = Pair(destination.latitude, destination.longitude)
        )

        // Setup map SDK here
        setupMapView()
    }

    private fun setupMapView() {
        // TODO: Initialize your Map SDK here
        // Example for MapBox or Google Maps:
        /*
        val mapView = MapView(requireContext())
        binding.mapContainer.removeAllViews()
        binding.mapContainer.addView(mapView)

        mapView.onCreate(savedInstanceState)
        mapView.getMapAsync { map ->
            // Configure map
            val departure = args.journeyInput.departure
            val destination = args.journeyInput.destination

            // Add markers
            map.addMarker(MarkerOptions()
                .position(LatLng(departure.latitude, departure.longitude))
                .title(departure.name))

            map.addMarker(MarkerOptions()
                .position(LatLng(destination.latitude, destination.longitude))
                .title(destination.name))

            // Draw route line
            drawRouteLine(map, departure, destination)

            // Fit bounds to show both stations
            val bounds = LatLngBounds.Builder()
                .include(LatLng(departure.latitude, departure.longitude))
                .include(LatLng(destination.latitude, destination.longitude))
                .build()
            map.moveCamera(CameraUpdateFactory.newLatLngBounds(bounds, 100))

            // Hide placeholder
            binding.mapPlaceholder.isVisible = false
        }
        */

        // For now, simulate map loading
        simulateMapLoading()
    }

    private fun simulateMapLoading() {
        // Simulate map loading delay
        binding.mapPlaceholder.postDelayed({
            if (isAdded) {
                binding.mapPlaceholder.isVisible = false
                // You can add a simple route visualization here
                addSimpleRouteVisualization()
            }
        }, 2000)
    }

    private fun addSimpleRouteVisualization() {
        // TODO: Add actual map implementation
        // For now, you can add a simple line drawing or use a WebView with a map
        println("--> Map loaded successfully")
    }

    private fun searchTrips() {
        val departure = args.journeyInput.departure
        val destination = args.journeyInput.destination

        // Show loading state
        showLoadingState()

        // Call ResRobot API through ViewModel
        viewModel.searchTrips(
            departureId = departure.id ?: "",
            destinationId = destination.id ?: "",
            departureLat = departure.latitude,
            departureLon = departure.longitude,
            destinationLat = destination.latitude,
            destinationLon = destination.longitude
        )

        println("--> Searching trips from ${departure.name} to ${destination.name}")
    }

    private fun showLoadingState() {
        binding.apply {
            loadingLayout.isVisible = true
            recyclerViewTrips.isVisible = false
            tvAvailableTripsHeader.isVisible = false
            errorLayout.isVisible = false
        }
    }

    private fun updateUI(state: JourneyState) {
        binding.apply {
            // Loading state
            loadingLayout.isVisible = state.isLoading

            // Success state - show trips
            if (state.trips.isNotEmpty()) {
                recyclerViewTrips.isVisible = true
                tvAvailableTripsHeader.isVisible = true
                errorLayout.isVisible = false

                tripsAdapter.submitList(state.trips)
                println("--> Found ${state.trips.size} trips")

            } else if (!state.isLoading && state.error == null) {
                // No trips found but no error
                showErrorState("No trips found for this route")

            } else if (!state.isLoading && state.trips.isEmpty()) {
                // Still loading or no results yet
                if (!state.isLoading) {
                    showErrorState("No trips available")
                }
            }

            // Error state
            if (state.error != null) {
                showErrorState(state.error)
                println("--> Error: ${state.error}")
            }
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

    private fun updateMapUI(mapState: MapState) {
        if (mapState.isMapReady) {
            binding.mapPlaceholder.isVisible = false
            println("--> Map is ready")
        }

        // Handle route polyline if available
        mapState.routePolyline?.let { polyline ->
            // Draw route on map
            drawRouteOnMap(polyline)
        }
    }

    private fun drawRouteOnMap(polyline: String) {
        // TODO: Implement route drawing on map
        println("--> Drawing route: $polyline")
    }

    private fun onTripSelected(trip: TripInfo) {
        // Handle trip selection - navigate to trip details or booking
        println("--> Trip selected: ${trip.serviceName} at ${trip.departureTime}")

        // Example: Navigate to trip details
        // findNavController().navigate(
        //     JourneyFragmentDirections.actionJourneyFragmentToTripDetailsFragment(trip)
        // )

        // Or show bottom sheet with trip details
        showTripDetailsBottomSheet(trip)
    }

    private fun showTripDetailsBottomSheet(trip: TripInfo) {
        // TODO: Implement trip details bottom sheet
        // For now, just log the trip details
        println("--> Trip Details:")
        println("   Service: ${trip.serviceName}")
        println("   Time: ${trip.departureTime} → ${trip.arrivalTime}")
        println("   Duration: ${trip.duration}")
        println("   Type: ${trip.tripType}")
        println("   Amenities: ${trip.amenities.joinToString(", ")}")
    }

    override fun onResume() {
        super.onResume()
        // Resume map if needed
    }

    override fun onPause() {
        super.onPause()
        // Pause map if needed
    }

    override fun onDestroy() {
        super.onDestroy()
        // Clean up map resources if needed
    }
}