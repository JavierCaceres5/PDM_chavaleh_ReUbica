package com.poyecto.ReUbica.ui.viewmodel

import androidx.lifecycle.ViewModel
import com.poyecto.ReUbica.data.Comercio
import com.poyecto.ReUbica.data.dummyBusiness
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class BusinessViewModel : ViewModel() {
    private val _business = MutableStateFlow<Comercio>(dummyBusiness)
    val business: StateFlow<Comercio> = _business
}
