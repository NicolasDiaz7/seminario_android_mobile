package ar.edu.unicen.seminario_desarrollo_mobile.domain

enum class ErrorType {
    NO_INTERNET,
    GENERIC
}

sealed class DataResult<out T> {
    data class Success<T>(val data: T) : DataResult<T>()
    data class Error(val errorType: ErrorType) : DataResult<Nothing>()
}