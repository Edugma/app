package io.edugma.features.account

import io.edugma.features.account.applications.ApplicationsViewModel
import io.edugma.features.account.authorization.AuthViewModel
import io.edugma.features.account.classmates.ClassmatesViewModel
import io.edugma.features.account.main.AccountMainViewModel
import io.edugma.features.account.marks.PerformanceViewModel
import io.edugma.features.account.payments.PaymentsViewModel
import io.edugma.features.account.personal.PersonalViewModel
import io.edugma.features.account.students.StudentsViewModel
import io.edugma.features.account.teachers.TeachersViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val accountFeaturesModule = module {
    viewModel { AccountMainViewModel(get(), get()) }
    viewModel { ApplicationsViewModel(get()) }
    viewModel { ClassmatesViewModel(get()) }
    viewModel { PaymentsViewModel(get()) }
    viewModel { StudentsViewModel(get()) }
    viewModel { PerformanceViewModel(get()) }
    viewModel { PersonalViewModel(get()) }
    viewModel { TeachersViewModel(get()) }
    viewModel { AuthViewModel(get(), get()) }
}