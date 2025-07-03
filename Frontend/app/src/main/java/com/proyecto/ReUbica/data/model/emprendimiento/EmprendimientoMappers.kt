package com.proyecto.ReUbica.data.model.emprendimiento

import com.proyecto.ReUbica.data.model.emprendimiento.EmprendimientoModel
import com.proyecto.ReUbica.ui.navigations.ComercioNavigation
import java.util.UUID
import android.util.Log
import com.google.gson.Gson

fun ComercioNavigation.toEmprendimientoModel(): EmprendimientoModel {
    val parsedId = try {
        UUID.fromString(this.id)
    } catch (e: IllegalArgumentException) {
        Log.w("toEmprendimientoModel", "ID inválido recibido (${this.id}), usando UUID.randomUUID() temporalmente")
        UUID.randomUUID()
    }

    val parsedUserID = try {
        UUID.fromString(this.userID)
    } catch (e: IllegalArgumentException) {
        Log.w("toEmprendimientoModel", "UserID inválido recibido (${this.userID}), usando UUID.randomUUID() temporalmente")
        UUID.randomUUID()
    }

    val gson = Gson()
    val redesSocialesParsed = this.redessociales?.let { jsonString ->
        gson.fromJson(jsonString, RedesSociales::class.java)
    }

    return EmprendimientoModel(
        id = parsedId,
        nombre = this.nombre,
        descripcion = this.descripcion,
        categoriasSecundarias = this.categoriasSecundarias,
        categoriasPrincipales = this.categoriasPrincipales,
        logo = this.logo,
        direccion = this.direccion,
        emprendimientoPhone = this.emprendimientoPhone,
        redes_sociales = redesSocialesParsed,
        userID = parsedUserID,
        latitud = this.latitud,
        longitud = this.longitud,
        created_at = this.createdat,
        updated_at = this.updatedat
    )
}

