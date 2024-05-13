package com.example.sistemasoporte

data class TablasBD(
    val usuario: String? = null,
    val psw: String? = null,
    val correo: String? = null,
    val tipo: TipoCuenta = TipoCuenta.USUARIO,
    val id: String? = null
    )
enum class TipoCuenta {
    USUARIO,
    ADMINISTRADOR
}
data class Ticket(
    val description: String? = null,
    val estacion: String? = null,
    val area: String? = null,
    val id: String? = null,
    )