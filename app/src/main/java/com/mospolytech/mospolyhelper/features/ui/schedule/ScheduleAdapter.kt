package com.mospolytech.mospolyhelper.features.ui.schedule

import android.animation.ArgbEvaluator
import android.content.res.Configuration
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.mospolytech.mospolyhelper.R
import com.mospolytech.mospolyhelper.data.deadline.DeadlinesRepository
import com.mospolytech.mospolyhelper.data.schedule.repository.LessonLabelRepository
import com.mospolytech.mospolyhelper.domain.schedule.model.Lesson
import com.mospolytech.mospolyhelper.domain.schedule.model.Schedule
import com.mospolytech.mospolyhelper.data.schedule.utils.ScheduleEmptyPairsDecorator
import com.mospolytech.mospolyhelper.data.schedule.utils.ScheduleWindowsDecorator
import com.mospolytech.mospolyhelper.domain.deadline.model.Deadline
import com.mospolytech.mospolyhelper.domain.schedule.model.LessonLabelKey
import com.mospolytech.mospolyhelper.utils.Action3
import com.mospolytech.mospolyhelper.utils.Action4
import com.mospolytech.mospolyhelper.utils.Event3
import com.mospolytech.mospolyhelper.utils.Event4
import kotlinx.coroutines.*
import java.time.LocalDate
import java.time.LocalTime
import java.time.format.DateTimeFormatter
import java.time.temporal.ChronoUnit
import kotlin.coroutines.CoroutineContext


class ScheduleAdapter(
    val schedule: Schedule?,
    private val labels: Map<LessonLabelKey, Set<String>>,
    private val deadlines: Map<String, List<Deadline>>,
    private val scheduleFilter: Schedule.Filter,
    private val showEmptyLessons: Boolean,
    private val showGroup: Boolean,
    private val isLoading: Boolean
) : RecyclerView.Adapter<RecyclerView.ViewHolder>(), CoroutineScope {
    companion object {
        private val dateFormatter = DateTimeFormatter.ofPattern("EEEE, d MMM")

        private const val MAX_COUNT = 400
        private const val VIEW_TYPE_NULL = 0
        private const val VIEW_TYPE_LOADING = 1
        private const val VIEW_TYPE_NORMAL = 2
        private const val VIEW_TYPE_EMPTY = 3
    }
    var firstPosDate: LocalDate = LocalDate.now()
    private var count = 0
    private val commonPool = RecyclerView.RecycledViewPool()

    val lessonClick: Event3<Lesson, LocalDate, List<View>> = Action3()
    private val timerTick: Event4<Int, Boolean, Boolean, Boolean> = Action4()


    private val job = SupervisorJob()
    override val coroutineContext: CoroutineContext = job + Dispatchers.Default

    init {
        setCount()
        setFirstPosDate()
        launchTimer()
    }

    private var currentOrder = -2
    private var currentOrderEvening = -2

    private fun launchTimer() {
        async {
            timerTick as Action4
            var pair: Pair<Int, Boolean>
            var updatePrev: Boolean
            while (isActive) {
                // Group is not evening
                pair = calculateCurrentLesson(false)
                updatePrev = currentOrder == pair.first - 1
                currentOrder = pair.first
                withContext(Dispatchers.Main) {
                    timerTick.invoke(pair.first, pair.second, false, updatePrev)
                }


                // Group is evening
                pair = calculateCurrentLesson(true)
                updatePrev = currentOrder == pair.first - 1
                currentOrderEvening = pair.first
                withContext(Dispatchers.Main) {
                    timerTick.invoke(pair.first, pair.second, true, updatePrev)
                }
                val q = 60L - LocalTime.now().second

                delay(q * 1000)
            }
        }
    }

    private fun calculateCurrentLesson(groupIsEvening: Boolean): Pair<Int, Boolean> {
        return Lesson.getOrder(LocalTime.now(), groupIsEvening)
    }


    override fun getItemCount() = count

//    override fun getItemId(position: Int): Long {
//        val date = firstPosDate.plusDays(position.toLong())
//        return 31L * schedule?.getSchedule(date).hashCode() + 31L * date.hashCode()
//    }

    private fun setCount() {
        if (schedule == null) {
            count = 1
        } else {
            count = schedule.dateFrom.until(schedule.dateTo, ChronoUnit.DAYS).toInt() + 1
            if (count  !in 1..MAX_COUNT) {
                count = MAX_COUNT
            }
        }
    }

    private fun setFirstPosDate() {
        if (schedule != null) {
            firstPosDate = if (count == MAX_COUNT) {
                LocalDate.now().minusDays((MAX_COUNT / 2).toLong())
            } else {
                schedule.dateFrom
            }
        }
    }

    override fun getItemViewType(position: Int) = if (schedule == null) {
            if (isLoading) VIEW_TYPE_LOADING else VIEW_TYPE_NULL
        } else if (schedule
            .getSchedule(
                firstPosDate.plusDays(position.toLong()),
                scheduleFilter
            ).isEmpty()) {
        VIEW_TYPE_EMPTY
    } else {
        VIEW_TYPE_NORMAL
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        when (viewType) {
            VIEW_TYPE_NULL -> {
                val view = LayoutInflater.from(parent.context)
                    .inflate(R.layout.page_schedule_null, parent, false)
                return ViewHolderSimple(view)
            }
            VIEW_TYPE_LOADING -> {
                val view = LayoutInflater.from(parent.context)
                    .inflate(R.layout.page_schedule_loading, parent, false)
                return ViewHolderSimple(view)
            }
            VIEW_TYPE_EMPTY -> {
                val view = LayoutInflater.from(parent.context)
                    .inflate(R.layout.page_schedule_empty, parent, false)
                return ViewHolderEmpty(view)
            }
            else -> {
                val view = LayoutInflater.from(parent.context)
                    .inflate(R.layout.page_schedule, parent, false)
                return ViewHolder(view)
            }
        }
    }

    override fun onBindViewHolder(viewHolder: RecyclerView.ViewHolder, position: Int) {
        when (viewHolder.itemViewType) {
            VIEW_TYPE_NORMAL -> (viewHolder as ViewHolder).bind()
            VIEW_TYPE_EMPTY -> (viewHolder as ViewHolderEmpty).bind()
        }
    }

    inner class ViewHolderSimple(val view: View) : RecyclerView.ViewHolder(view)

    inner class ViewHolderEmpty(val view: View) : RecyclerView.ViewHolder(view) {

        fun bind() {
        }
    }


    inner class ViewHolder(
        val view: View
    ) : RecyclerView.ViewHolder(view) {
        private val listSchedule = view as RecyclerView
        private var listAdapter: LessonAdapter? = null
        private val nightMode = (view.context.resources.configuration.uiMode and Configuration.UI_MODE_NIGHT_MASK) == Configuration.UI_MODE_NIGHT_YES
        private val disabledColor = view.context.getColor(R.color.textSecondaryDisabled)
        private val headColor = view.context.getColor(R.color.textLessonHead)
        private val headCurrentColor = view.context.getColor(R.color.textLessonHeadCurrent)
        var accumulator = 0f

        init {
            // TODO check pool performance
            listSchedule.setRecycledViewPool(commonPool)
            listSchedule.layoutManager = LinearLayoutManager(view.context)
                .apply { recycleChildrenOnDetach = true }
            val dp8 = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 8f, view.resources.displayMetrics)
            val scrollLength = dp8 * 3f
            listSchedule.addOnItemTouchListener(object : RecyclerView.OnItemTouchListener {
                override fun onInterceptTouchEvent(rv: RecyclerView, e: MotionEvent): Boolean {
                    if (e.action == MotionEvent.ACTION_DOWN &&
                        rv.scrollState == RecyclerView.SCROLL_STATE_SETTLING
                    ) {
                        rv.stopScroll()
                    }
                    return false
                }

                override fun onRequestDisallowInterceptTouchEvent(disallowIntercept: Boolean) = Unit

                override fun onTouchEvent(rv: RecyclerView, e: MotionEvent) = Unit
            })
            listSchedule.setOnScrollChangeListener {
                    v, _, _, _, oldScrollY ->
                v as RecyclerView
                if (v.canScrollVertically(-1)) {
                    accumulator -= oldScrollY
                    //dayTitle.elevation = if (accumulator > scrollLength) dp8 else accumulator * dp8 / scrollLength
                    val q = ArgbEvaluator()
                    //dayTitle.setBackgroundColor(if (accumulator > scrollLength) topColor else
                        //(q.evaluate(accumulator / scrollLength, bottomColor, topColor)) as Int)
                } else {
                    //dayTitle.elevation = 0f
                    //dayTitle.setBackgroundColor(bottomColor)
                    accumulator = 0f
                }
            }
            listSchedule.itemAnimator = null
        }


        fun bind() {
            val date = firstPosDate.plusDays(adapterPosition.toLong())
            val dailySchedule = ScheduleWindowsDecorator(schedule!!.getSchedule(date, scheduleFilter))
            val map = dailySchedule.map
            listSchedule.scrollToPosition(0)
            accumulator = 0f
            if (listAdapter == null) {
                listAdapter = LessonAdapter(
                    if (showEmptyLessons) ScheduleEmptyPairsDecorator(dailySchedule) else dailySchedule,
                    map,
                    labels,
                    deadlines,
                    scheduleFilter,
                    date,
                    showGroup,
                    disabledColor,
                    headColor,
                    headCurrentColor
                )
                listAdapter?.let {
                    it.lessonClick += { lesson, date, view ->
                        (lessonClick as Action3).invoke(lesson, date, view)
                    }
                }
                listAdapter?.let {
                    timerTick += it::updateTime
                }
                listSchedule.adapter = listAdapter
            } else {
                listAdapter!!.update(
                    if (showEmptyLessons) ScheduleEmptyPairsDecorator(dailySchedule) else dailySchedule,
                    map,
                    scheduleFilter,
                    date,
                    showGroup
                )
            }
        }
    }
}