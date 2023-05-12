package io.edugma.features.account

import io.edugma.features.account.people.classmates.ClassmatesViewModel
import io.edugma.features.account.marks.PerformanceViewModel
import io.edugma.features.account.menu.MenuViewModel
import io.edugma.features.account.payments.PaymentsViewModel
import io.edugma.features.account.personal.PersonalViewModel
import io.edugma.features.account.people.students.StudentsViewModel
import io.edugma.features.account.people.teachers.TeachersViewModel
import io.edugma.features.account.web.WebViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val accountFeaturesModule = module {
    viewModel { MenuViewModel(get(), get(), get()) }
    viewModel { ClassmatesViewModel(get()) }
    viewModel { PaymentsViewModel(get()) }
    viewModel { StudentsViewModel(get()) }
    viewModel { PerformanceViewModel(get()) }
    viewModel { PersonalViewModel(get(), get()) }
    viewModel { TeachersViewModel(get()) }
    viewModel { WebViewModel(get()) }
}
