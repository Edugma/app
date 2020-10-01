package com.mospolytech.mospolyhelper.features.ui.schedule

import android.appwidget.AppWidgetManager
import android.content.ComponentName
import android.content.Intent
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.util.TypedValue
import android.view.*
import android.widget.*
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.FragmentNavigatorExtras
import androidx.navigation.fragment.findNavController
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import androidx.viewpager.widget.ViewPager
import androidx.viewpager2.widget.ViewPager2
import androidx.viewpager2.widget.ViewPager2.OnPageChangeCallback
import com.google.android.material.appbar.AppBarLayout
import com.google.android.material.bottomappbar.BottomAppBar
import com.google.android.material.chip.Chip
import com.google.android.material.chip.ChipGroup
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.tabs.TabLayout
import com.mospolytech.mospolyhelper.NavGraphDirections
import com.mospolytech.mospolyhelper.R
import com.mospolytech.mospolyhelper.domain.schedule.model.Lesson
import com.mospolytech.mospolyhelper.domain.schedule.model.Teacher
import com.mospolytech.mospolyhelper.domain.schedule.usecase.ScheduleLabelDeadline
import com.mospolytech.mospolyhelper.features.appwidget.schedule.ScheduleAppWidgetProvider
import com.mospolytech.mospolyhelper.features.ui.main.MainActivity
import com.mospolytech.mospolyhelper.utils.onLoading
import com.mospolytech.mospolyhelper.utils.onSuccess
import com.mospolytech.mospolyhelper.utils.safe
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.combine
import org.koin.androidx.viewmodel.ext.android.sharedViewModel
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.time.temporal.ChronoUnit
import kotlin.coroutines.CoroutineContext


class ScheduleFragment : Fragment(), CoroutineScope {

    companion object {
        private val dateFormatterSubtitle = DateTimeFormatter.ofPattern("d MMMM")
        private val dateFormatterTitle = DateTimeFormatter.ofPattern("EEEE")
    }

    private val viewModel  by sharedViewModel<ScheduleViewModel>()

    private lateinit var viewPager: ViewPager2
    private lateinit var swipeToRefresh: SwipeRefreshLayout
    private lateinit var homeBtn: FloatingActionButton
    //private lateinit var tabLayout: TabLayout
    private lateinit var subtitle: TextView
    private lateinit var title: TextView
    private lateinit var btnCalendar: ImageButton

    private lateinit var scheduleIdsChipGroup: ChipGroup

    //private lateinit var tabLayoutOnPageChangeCallback: TabLayoutOnPageChangeCallback

    private var appBarExpanded = false
    private var viewPagerIdle = false

    private val job = SupervisorJob()

    override val coroutineContext: CoroutineContext
        get() = Dispatchers.Default + job


    private fun onLessonClick(lesson: Lesson, date: LocalDate, views: List<View>) {
        val extras = FragmentNavigatorExtras()//*(views.map { it to it.transitionName!! }.toTypedArray()))
        findNavController().navigate(
            ScheduleFragmentDirections
                .actionScheduleFragmentToLessonInfoFragment(
                    titleTransitionNames = views.map { it.transitionName!! }.toTypedArray()
                ), extras
        )
        viewModel.openLessonInfo(lesson, date)
    }

    private fun setSchedule(
        scheduleLabelDeadline: ScheduleLabelDeadline,
        showEmptyLessons: Boolean,
        showEndedLessons: Boolean,
        showCurrentLessons: Boolean,
        showNotStartedLessons: Boolean,
        currentLesson: Pair<Lesson.CurrentLesson, Lesson.CurrentLesson>
    ) {
        if (context != null && scheduleLabelDeadline.schedule != null) {
            val group = scheduleLabelDeadline.schedule.dailySchedules
                .firstOrNull { it.isNotEmpty() }
                ?.firstOrNull()?.groups
                ?.firstOrNull()
//            if (group?.comment?.isNotEmpty() == true) {
//                Toast.makeText(
//                    requireContext(),
//                    group.comment,
//                    Toast.LENGTH_LONG
//                ).show()
//            }
        }
        val oldAdapter = viewPager.adapter
        val newAdapter = ScheduleAdapter(
            scheduleLabelDeadline.schedule,
            scheduleLabelDeadline.labels,
            scheduleLabelDeadline.deadlines,
            showEndedLessons,
            showCurrentLessons,
            showNotStartedLessons,
            showEmptyLessons,
            showGroups = !viewModel.id.value.first || viewModel.isAdvancedSearch,
            showTeachers = viewModel.id.value.first || viewModel.isAdvancedSearch,
            prevCurrentLesson = currentLesson
        )
        //newAdapter.setHasStableIds(true)
        val toPosition = if (oldAdapter is ScheduleAdapter) {
            newAdapter.firstPosDate.until(oldAdapter.firstPosDate, ChronoUnit.DAYS) + viewPager.currentItem
        } else {
            newAdapter.firstPosDate.until(LocalDate.now(), ChronoUnit.DAYS)
        }
        viewPager.adapter = newAdapter
        newAdapter.lessonClick += ::onLessonClick
        viewPager.adapter?.notifyDataSetChanged()
        viewPager.setCurrentItem(toPosition.toInt(), false)
    }

    private fun setLoading() {
        (viewPager.adapter as? ScheduleAdapter)?.setLoading()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_schedule, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewPager = view.findViewById(R.id.viewpager)
        swipeToRefresh = view.findViewById(R.id.schedule_update)
        homeBtn = view.findViewById(R.id.button_home)
        subtitle = view.findViewById(R.id.subtitle)
        title = view.findViewById(R.id.title)
        btnCalendar = view.findViewById(R.id.button_calendar)
        scheduleIdsChipGroup = view.findViewById(R.id.chipgroup_ids)
        scheduleIdsChipGroup.addView(createAddButton())

        val bottomAppBar = view.findViewById<BottomAppBar>(R.id.bottomAppBar)
        (activity as MainActivity).setSupportActionBar(bottomAppBar)
        (activity as MainActivity).supportActionBar!!.setDisplayShowTitleEnabled(false)
        bottomAppBar.setNavigationOnClickListener {
            findNavController().safe { navigate(NavGraphDirections.actionGlobalMainMenuFragment()) }
        }

        btnCalendar.setOnClickListener {
            findNavController().safe { navigate(R.id.calendarFragment) }
        }

        setScheduleViews()
        bindViewModel()

        val appBar = view.findViewById<AppBarLayout>(R.id.appBar)
        appBar.addOnOffsetChangedListener(AppBarLayout.OnOffsetChangedListener { appBarLayout, verticalOffset ->
            appBarExpanded = verticalOffset == 0
            swipeToRefresh.isEnabled = appBarExpanded && viewPagerIdle
        })
    }

    private fun setScheduleViews() {
        swipeToRefresh.setProgressBackgroundColorSchemeResource(R.color.colorLevelThree)
        swipeToRefresh.setColorSchemeResources(R.color.colorSecondary)
        swipeToRefresh.setOnRefreshListener {
            if (viewModel.isAdvancedSearch) {
                viewModel.isAdvancedSearch = false
            }
            viewModel.updateSchedule()
        }
        //viewPager.registerOnPageChangeCallback(tabLayoutOnPageChangeCallback)

        viewPager.offscreenPageLimit = 2
        viewPager.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
            override fun onPageScrollStateChanged(state: Int) {
                viewPagerIdle = state == ViewPager.SCROLL_STATE_IDLE
                swipeToRefresh.isEnabled = appBarExpanded && viewPagerIdle
            }

            override fun onPageScrolled(
                position: Int,
                positionOffset: Float,
                positionOffsetPixels: Int
            ) {
                val dayOffset = if (positionOffset < 0.5) 0L else 1L
                val date =
                    (viewPager.adapter as ScheduleAdapter).firstPosDate.plusDays(position + dayOffset)
                if (LocalDate.now() == date) {
                    homeBtn.hide()
                } else {
                    homeBtn.show()
                }

                title.text = date.format(dateFormatterTitle).capitalize()
                //val lessonCount = viewModel.filteredSchedule.value.getOrNull()?.schedule?.getScheduleCount(date) ?: 0
                subtitle.text = date.format(dateFormatterSubtitle).capitalize() //+ if (lessonCount == 0) {
//                    " • нет занятий"
//                } else {
//                    " • $lessonCount заняти${getEnding(lessonCount.toLong())}"
//                }
            }

            override fun onPageSelected(position: Int) {
                viewModel.date.value =
                    (viewPager.adapter as ScheduleAdapter).firstPosDate.plusDays(position.toLong())
            }
        })

        homeBtn.setOnClickListener { viewModel.goHome() }
    }

    private fun getEnding(days: Long): String {
        // *1 занятие .. *2, *3, *4 занятия .. *5, *6, *7, *8, *9, *0 занятий .. искл. - 11 - 14
        val lastNumberOfDays = days % 10L
        return when {
            days in 11L..14L -> "й"
            lastNumberOfDays == 1L -> "е"
            lastNumberOfDays in 2L..4L -> "я"
            else -> "й"
        }
    }

    private fun createAddButton(): ImageButton {
        val addBtn = ImageButton(context)
        addBtn.setImageDrawable(requireContext().getDrawable(R.drawable.ic_round_add_24))
        addBtn.setOnClickListener {
            findNavController().safe { navigate(ScheduleFragmentDirections.actionScheduleFragmentToScheduleIdsFragment()) }
        }
        val tv = TypedValue()
        if (requireContext().theme.resolveAttribute(
                android.R.attr.actionBarItemBackground,
                tv,
                true
            )
        ) {
            addBtn.setBackgroundResource(tv.resourceId)
        } else {
            addBtn.background = ColorDrawable(requireContext().getColor(R.color.scheduleBackgroundColor))
        }
        val dp8 = TypedValue.applyDimension(
            TypedValue.COMPLEX_UNIT_DIP,
            24f,
            resources.displayMetrics
        ).toInt()
        addBtn.minimumHeight = dp8
        addBtn.minimumWidth = dp8
        return addBtn
    }

    private fun createChip(pair: Pair<Boolean, String>): Chip {
        val chip = layoutInflater.inflate(R.layout.chip_schedule_user, scheduleIdsChipGroup, false) as Chip
        val id: String
        val title: String
        if (pair.first) {
            id = pair.second
            title = id
        } else {
            val i = pair.second.indexOfLast { it == '(' }
            id = if (i == -1 || pair.second.length < i + 4) " " else pair.second.substring(i + 3, pair.second.length - 1)
            val t = if (i == -1) " " else pair.second.substring(0, i)
            title = try {
                Teacher.fromFullName(t).getShortName()
            } catch (e: Exception) {
                t
            }
        }
        chip.text = title
        chip.setChipIconResource(if (pair.first) R.drawable.ic_lesson_group else R.drawable.ic_lesson_teacher)
        chip.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                viewModel.id.value = Pair(pair.first, id)
            }
        }
        chip.setOnCreateContextMenuListener { menu, v, menuInfo ->
            menu.add("Удалить").setOnMenuItemClickListener {
                if (it.title == "Удалить") {
                    scheduleIdsChipGroup.removeView(chip)
                    viewModel.savedIds.value -= pair
                    if (viewModel.id.value == pair) {
                        viewModel.id.value = Pair(true, "")
                    }
                    return@setOnMenuItemClickListener true
                }
                false
            }
        }
        return chip
    }

    private fun bindViewModel() {
        lifecycleScope.launchWhenResumed {
            combine(
                viewModel.filteredSchedule,
                viewModel.showEmptyLessons,
                viewModel.showEndedLessons,
                viewModel.showCurrentLessons,
                viewModel.showNotStartedLessons
            ) {
                schedule,
                showEmptyLessons,
                showEndedLessons,
                showCurrentLessons,
                showNotStartedLessons ->
                schedule.onSuccess {
                    setSchedule(
                        it,
                        showEmptyLessons,
                        showEndedLessons,
                        showCurrentLessons,
                        showNotStartedLessons,
                        viewModel.currentLessonOrder.value
                    )
                    swipeToRefresh.isRefreshing = false
                }
                schedule.onLoading {
                    setLoading()
                }

            }.collect()
        }
        lifecycleScope.launchWhenResumed {
            viewModel.savedIds.collect { set ->
                var checkedChip: Chip? = null
                // TODO: Inefficient
                scheduleIdsChipGroup.removeAllViews()
                for (pair in set) {
                    val chip = createChip(pair)
                    val i = pair.second.indexOfLast { it == '(' }
                    val id = if (pair.first) {
                        pair.second
                    } else {
                        if (i == -1 || pair.second.length < i + 4) {
                            " "
                        } else {
                            pair.second.substring(i + 3, pair.second.length - 1)
                        }
                    }

                    var viewId: Int? = null
                    if (set.size == 1 || (viewModel.id.value.first == pair.first && viewModel.id.value.second == id)) {
                        viewId = View.generateViewId()
                        chip.id = viewId
                        checkedChip = chip
                    }
                    scheduleIdsChipGroup.addView(chip)
                    if (viewId != null) {
                        scheduleIdsChipGroup.check(viewId)
                    }
                }
                scheduleIdsChipGroup.addView(createAddButton())
                scheduleIdsChipGroup.post {
                    if (checkedChip != null) {
                        (scheduleIdsChipGroup.parent as HorizontalScrollView)
                            .smoothScrollTo(checkedChip.left - checkedChip.paddingLeft, checkedChip.top)
                    }
                }
            }
        }



        lifecycleScope.launchWhenResumed {
            viewModel.date.collect {
                val adapter = viewPager.adapter
                if (adapter is ScheduleAdapter) {
                    if (it != adapter.firstPosDate.plusDays(viewPager.currentItem.toLong()))
                        async(Dispatchers.Main) { viewPager.setCurrentItem(
                            adapter.firstPosDate.until(it, ChronoUnit.DAYS).toInt(),
                            false
                        ) }
                }
            }
        }

        lifecycleScope.async {
            viewModel.id.collect {
                val intent = Intent(context, ScheduleAppWidgetProvider::class.java)
                intent.action = AppWidgetManager.ACTION_APPWIDGET_UPDATE
                // Use an array and EXTRA_APPWIDGET_IDS instead of AppWidgetManager.EXTRA_APPWIDGET_ID,
                // since it seems the onUpdate() is only fired on that:
                // Use an array and EXTRA_APPWIDGET_IDS instead of AppWidgetManager.EXTRA_APPWIDGET_ID,
                // since it seems the onUpdate() is only fired on that:
                val ids = AppWidgetManager.getInstance(context)
                    .getAppWidgetIds(ComponentName(requireContext(), ScheduleAppWidgetProvider::class.java))
                intent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_IDS, ids)
                context?.sendBroadcast(intent)
            }
        }

        lifecycleScope.launchWhenResumed {
            viewModel.currentLessonOrder.collect {
                (viewPager.adapter as? ScheduleAdapter)?.updateCurrentLesson(it)
            }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.menu_schedule, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.schedule_advanced_search -> {
                findNavController()
                    .navigate(
                        ScheduleFragmentDirections
                            .actionScheduleFragmentToAdvancedSearchFragment()
                    )
            }
            R.id.menu_schedule_filter -> {
                findNavController().safe {
                    navigate(ScheduleFragmentDirections.actionScheduleFragmentToScheduleFiltersFragment())
                }
            }
        }

        return true
    }

    override fun onStart() {
        super.onStart()
        setHasOptionsMenu(true)
        requireActivity().invalidateOptionsMenu()
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

//        viewModel.onMessage += {
//            // TODO java.lang.NullPointerException: Attempt to invoke virtual method 'java.lang.String android.content.Context.getPackageName()' on a null object reference
//            lifecycleScope.launchWhenCreated {
//                Toast.makeText(context, it, Toast.LENGTH_SHORT).show()
//            }
//        }
    }

    override fun onDestroy() {
        coroutineContext.cancelChildren()
        super.onDestroy()
    }

    private class TabLayoutOnPageChangeCallback(
        private val tabLayout: TabLayout,
        private val viewPager: ViewPager2
    ) : OnPageChangeCallback() {
        private var previousScrollState = 0
        private var scrollState = 0
        override fun onPageScrollStateChanged(state: Int) {
            previousScrollState = scrollState
            scrollState = state
        }

        override fun onPageScrolled(
            position: Int,
            positionOffset: Float,
            positionOffsetPixels: Int
        ) {
            val date =
                (viewPager.adapter as ScheduleAdapter).firstPosDate.plusDays(position.toLong())
            val position = date.dayOfWeek.value - 1
            // Only update the text selection if we're not settling, or we are settling after
            // being dragged
            val updateText =
                scrollState != ViewPager2.SCROLL_STATE_SETTLING || previousScrollState == ViewPager2.SCROLL_STATE_DRAGGING
            // Update the indicator if we're not settling after being idle. This is caused
            // from a setCurrentItem() call and will be handled by an animation from
            // onPageSelected() instead.
            val updateIndicator =
                !(scrollState == ViewPager2.SCROLL_STATE_SETTLING && previousScrollState == ViewPager2.SCROLL_STATE_IDLE)
            tabLayout.setScrollPosition(position, positionOffset, updateText, updateIndicator)
        }

        override fun onPageSelected(position: Int) {
            val date =
                (viewPager.adapter as ScheduleAdapter).firstPosDate.plusDays(position.toLong())
            val position = date.dayOfWeek.value - 1
            if (tabLayout.selectedTabPosition != position && position < tabLayout.tabCount) {
                // Select the tab, only updating the indicator if we're not being dragged/settled
                // (since onPageScrolled will handle that).
                val updateIndicator = (scrollState == ViewPager2.SCROLL_STATE_IDLE
                        || (scrollState == ViewPager2.SCROLL_STATE_SETTLING
                        && previousScrollState == ViewPager2.SCROLL_STATE_IDLE))
                tabLayout.selectTab(tabLayout.getTabAt(position), updateIndicator)
            }
        }

        fun reset() {
            scrollState = ViewPager2.SCROLL_STATE_IDLE
            previousScrollState = scrollState
        }

        init {
            reset()
        }
    }
}