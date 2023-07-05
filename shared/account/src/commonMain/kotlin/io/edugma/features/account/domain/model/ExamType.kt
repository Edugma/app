package io.edugma.features.account.domain.model

import kotlinx.serialization.Serializable

@Serializable
enum class ExamType {
    Pass, Exam, MarkPass, CourseWork
}

fun io.edugma.features.account.domain.model.ExamType.print(): String = when (this) {
    io.edugma.features.account.domain.model.ExamType.Pass -> "Зачет"
    io.edugma.features.account.domain.model.ExamType.Exam -> "Экзамен"
    io.edugma.features.account.domain.model.ExamType.MarkPass -> "Диффиренцированный зачет"
    io.edugma.features.account.domain.model.ExamType.CourseWork -> "Курсовая работа"
}
