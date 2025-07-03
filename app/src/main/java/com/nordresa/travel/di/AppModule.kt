package com.nordresa.travel.di

import com.nordresa.travel.repository.NordResaRepository
import com.nordresa.travel.ui.journey.JourneyViewModel
import com.nordresa.travel.ui.search.SearchViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.core.scope.get
import org.koin.dsl.module

val appModule = module {

    // Provide Repository
    single { NordResaRepository() }

    // Provide ViewModel with Repository injected
    // Provide ViewModels separately
    viewModel { SearchViewModel(get()) }
    viewModel { JourneyViewModel() }
}
