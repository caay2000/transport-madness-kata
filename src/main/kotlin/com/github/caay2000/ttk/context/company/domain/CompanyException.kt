package com.github.caay2000.ttk.context.company.domain

sealed class CompanyException : RuntimeException {
    constructor(message: String) : super(message)
    constructor(cause: Throwable) : super(cause)
}

data class UnknownCompanyException(override val cause: Throwable) : CompanyException(cause)
