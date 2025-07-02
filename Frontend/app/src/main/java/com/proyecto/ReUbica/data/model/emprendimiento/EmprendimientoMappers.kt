package com.proyecto.ReUbica.data.model.emprendimiento

import com.proyecto.ReUbica.data.model.emprendimiento.EmprendimientoModel
import com.proyecto.ReUbica.ui.navigations.ComercioNavigation
import java.util.UUID
import android.util.Log

fun ComercioNavigation.toEmprendimientoModel(): EmprendimientoModel {
    val parsedId = try {
        UUID.fromString(this.id)
    } catch (e: IllegalArgumentException) {
        Log.w("toEmprendimientoModel", "ID inválido recibido (${this.id}), usando UUID.randomUUID() temporalmente")
        UUID.randomUUID()
    }

    return EmprendimientoModel(
        id = parsedId,
        nombre = this.nombre,
        descripcion = this.descripcion,
        categoriasSecundarias = emptyList(),
        categoriasPrincipales = listOf(this.categoria),
        logo = null,
        direccion = this.direccion,
        emprendimientoPhone = null,
        redes_sociales = null,
        userID = UUID.randomUUID(),
        latitud = this.latitud,
        longitud = this.longitud,
        created_at = null,
        updated_at = null
    )
}

