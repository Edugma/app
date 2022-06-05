package io.edugma.features.base.elements

import android.app.DatePickerDialog
import android.os.Build
import android.widget.DatePicker
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import java.time.LocalDate
import java.time.LocalDateTime
import java.util.*

@RequiresApi(Build.VERSION_CODES.O) //todo переписать
@Composable
fun DatePicker(title: String, date: LocalDate, onDateChange: (LocalDate) -> Unit) {
    val context = LocalContext.current
    val mYear: Int
    val mMonth: Int
    val mDay: Int
    val now = Calendar.getInstance()
    mYear = now.get(Calendar.YEAR)
    mMonth = now.get(Calendar.MONTH)
    mDay = now.get(Calendar.DAY_OF_MONTH)
    now.time = Date()
    val datePickerDialog = DatePickerDialog(
        context,
        { _: DatePicker, year: Int, month: Int, dayOfMonth: Int ->
            val cal = Calendar.getInstance()
            cal.set(year, month, dayOfMonth)
            onDateChange.invoke(LocalDateTime.ofInstant(cal.toInstant(), cal.timeZone.toZoneId()).toLocalDate())
        }, mYear, mMonth, mDay
    )
    Row(
        Modifier.fillMaxWidth(0.8f),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Button(onClick = {
            datePickerDialog.show()
        }, shape = RoundedCornerShape(4.dp)) {
            Text(text = "Выбор даты")
        }
        Spacer(modifier = Modifier.size(5.dp))
        Text(text = "$title: $date")
    }
}