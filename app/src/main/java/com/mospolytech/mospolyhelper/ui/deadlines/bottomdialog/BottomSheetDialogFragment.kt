package com.mospolytech.mospolyhelper.ui.deadlines.bottomdialog

import android.app.DatePickerDialog
import android.app.Dialog
import android.app.TimePickerDialog
import android.content.Context
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.Toast
import androidx.fragment.app.viewModels
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.android.material.chip.Chip
import com.mospolytech.mospolyhelper.R
import com.mospolytech.mospolyhelper.repository.models.Deadline
import com.mospolytech.mospolyhelper.utils.ContextProvider
import kotlinx.android.synthetic.main.bottom_sheet_deadline.*
import java.time.LocalDate
import java.time.LocalTime
import java.time.format.DateTimeFormatter

class AddBottomSheetDialogFragment
    : BottomSheetDialogFragment(), View.OnClickListener {

    override fun getTheme(): Int = R.style.BottomSheetDialogTheme
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog =
        BottomSheetDialog(requireContext(), theme)

    enum class OpenType {
        EDIT, ADD, SIMPLE
    }

    private var chipId: Int = 0
    private var openType = OpenType.SIMPLE
    private var deadline: Deadline? = null
    private var name: String = ""
    //private lateinit var viewModel: DialogFragmentViewModel
    private val viewModel by viewModels<DialogFragmentViewModel>()
    private var contextApp = ContextProvider.context as Context

    private val datePickerDialog = DatePickerDialog(
        contextApp,
        DatePickerDialog.OnDateSetListener { _, year, monthOfYear, dayOfMonth ->
            val localDate = LocalDate.of(year, monthOfYear + 1, dayOfMonth)
            val dateFormatter = DateTimeFormatter.ofPattern("eee, dd.MM.yyyy")
            editDate.setText(localDate.format(dateFormatter))
        }, LocalDate.now().year,
        LocalDate.now().month.value - 1,
        LocalDate.now().dayOfMonth)

    private val timePickerDialog = TimePickerDialog(
        contextApp,
        TimePickerDialog.OnTimeSetListener { _, hourOfDay, minute ->
            val localTime = LocalTime.of(hourOfDay, minute)
            val timeFormatter = DateTimeFormatter.ofPattern("HH:mm")
            editTime.setText(localTime.format(timeFormatter))
        }, LocalTime.now().hour,
        LocalTime.now().minute,true)

    companion object {
        fun newInstance(): AddBottomSheetDialogFragment {
            return AddBottomSheetDialogFragment()
        }
        const val TAG = "BottomDialog"
    }

    fun setName(name: String) {
        this.name = name
        openType = OpenType.ADD
    }

    fun setEdit(deadline: Deadline) {
        openType = OpenType.EDIT
        this.deadline = deadline
    }

    private fun setEditable(deadline: Deadline) {
        btadd.setText(R.string.updateButton)
        val item = deadline
        editPredmet.setText(item.name)
        editDescription.setText(item.description)
        editDate.setText(item.date)
        editTime.setText(item.time)
        when(item.importance) {
            R.color.colorLow -> chipLow.isChecked = true
            R.color.colorMedium -> chipMedium.isChecked = true
            R.color.colorHigh -> chipHigh.isChecked = true
        }
        if (item.pinned) {
            imgPinned.setImageResource(R.drawable.ic_push_pin_24px)
            imgPinned.contentDescription = "pinned"
        } else {
            imgPinned.setImageResource(R.drawable.ic_push_unpin_24px)
            imgPinned.contentDescription = "unpinned"
        }
        btadd.setOnClickListener {
            val predmet = editPredmet.text.toString()
            val descr = editDescription.text.toString()
            var color: Int = R.color.colorLow
            if (chipMedium.isChecked) {
                color = R.color.colorMedium
            }
            if (chipHigh.isChecked) {
                color = R.color.colorHigh
            }
            val pinned = imgPinned.contentDescription == "pinned"
            val date = editDate.text.toString()
            val time = editTime.text.toString()
            item.name = predmet
            item.description = descr
            item.date = date
            item.time = time
            item.pinned = pinned
            item.importance = color
            viewModel.updateOne(item)
            openType = OpenType.SIMPLE
            dismiss()
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.bottom_sheet_deadline, container, false)
    }

    override fun onResume() {
        super.onResume()
        when (openType) {
            OpenType.EDIT -> {
                setEditable(this.deadline as Deadline)
            }
            OpenType.SIMPLE -> {
                clear()
            }
            OpenType.ADD -> {
                clear()
                editPredmet.setText(this.name)
            }
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        //viewModel = ViewModelProvider(this).get(DialogFragmentViewModel::class.java)
        viewModel.newRepository()
        chipId = chipLow.id
        when (openType) {
            OpenType.SIMPLE -> {
                btadd.setOnClickListener(this)
            }
            OpenType.ADD -> {
                btadd.setOnClickListener(this)
                clear()
                editPredmet.setText(this.name)
            }
            OpenType.EDIT -> {
                setEditable(this.deadline as Deadline)
            }
        }
        editDate.setOnClickListener {
            datePickerDialog.show()
        }

        editTime.setOnClickListener {
            timePickerDialog.show()
        }

        imgPinned.setOnClickListener { it as ImageView
            if (it.contentDescription == getString(R.string.pin)){
                it.setImageResource(R.drawable.ic_push_unpin_24px)
                it.contentDescription = getString(R.string.unpin)
            } else {
                it.setImageResource(R.drawable.ic_push_pin_24px)
                it.contentDescription = getString(R.string.pin)
            }
        }

        chipGr.setOnCheckedChangeListener { _, checkedId ->
            when (checkedId) {
                R.id.chipLow -> { chipId = R.id.chipLow }
                R.id.chipMedium -> { chipId = R.id.chipMedium }
                R.id.chipHigh -> { chipId = R.id.chipHigh }
                else -> { view.findViewById<Chip>(chipId).isChecked = true }
            }
        }
    }

    override fun onClick(v: View?) {
        val predmet =  editPredmet.text.toString()
        val descr = editDescription.text.toString()
        if (descr.isEmpty()) {
            if (Build.VERSION.SDK_INT < Build.VERSION_CODES.N) {
                Toast.makeText(contextApp, R.string.predmetError, Toast.LENGTH_SHORT).show()
            } else {
                editDescription.error = resources.getString(R.string.predmetError)
            }
            return
        }

        var color: Int = R.color.colorLow
        if (chipMedium.isChecked) {
            color = R.color.colorMedium
        }
        if (chipHigh.isChecked) {
            color = R.color.colorHigh
        }
        val pinned = imgPinned.contentDescription == "pinned"
        val date = editDate.text.toString()
        val time = editTime.text.toString()
        val d = Deadline(
            name = predmet, description = descr, pinned = pinned,
            date = date, time = time, importance = color
        )
        viewModel.saveInformation(d)
        dismiss()
    }

    override fun onPause() {
        super.onPause()
        clear()
        openType = OpenType.SIMPLE
    }


    private fun clear() {
        editPredmet.text.clear()
        editDescription.text.clear()
        editDate.text.clear()
        editTime.text.clear()
        imgPinned.setImageResource(R.drawable.ic_push_unpin_24px)
        imgPinned.contentDescription = getString(R.string.unpin)
        chipLow.isChecked = true
        if (openType == OpenType.EDIT) {
            btadd.setOnClickListener(this)
            btadd.setText(R.string.add)
            openType = OpenType.SIMPLE
        }
        editDescription.error = null
    }

}