package io.edugma.features.account

import io.edugma.features.account.marks.PerformanceViewModel
import io.edugma.features.account.menu.MenuViewModel
import io.edugma.features.account.payments.PaymentsViewModel
import io.edugma.features.account.people.classmates.ClassmatesViewModel
import io.edugma.features.account.people.students.StudentsViewModel
import io.edugma.features.account.people.teachers.TeachersViewModel
import io.edugma.features.account.personal.PersonalViewModel
import io.edugma.features.account.web.WebViewModel
import org.koin.core.module.dsl.factoryOf
import org.koin.dsl.module

val accountFeaturesModule = module {
    factoryOf(::MenuViewModel)
    factoryOf(::ClassmatesViewModel)
    factoryOf(::PaymentsViewModel)
    factoryOf(::StudentsViewModel)
    factoryOf(::PerformanceViewModel)
    factoryOf(::PersonalViewModel)
    factoryOf(::TeachersViewModel)
    factoryOf(::WebViewModel)
}
