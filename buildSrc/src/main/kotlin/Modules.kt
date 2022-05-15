object Modules {
    const val App = ":app"

    object Data {
        private const val prefix = ":data"

        const val Base = "$prefix:base"
        const val Nodes = "$prefix:nodes"
        const val Schedule = "$prefix:schedule"
        const val Account = "$prefix:account"
    }

    object Domain {
        private const val prefix = ":domain"

        const val Base = "$prefix:base"
        const val Nodes = "$prefix:nodes"
        const val Schedule = "$prefix:schedule"
        const val Account = "$prefix:account"
    }

    object Features {
        private const val prefix = ":features"

        object Base {
            private const val prefix1 = ":base"

            const val Core = "$prefix$prefix1:core"
            const val Navigation = "$prefix$prefix1:navigation"
            const val Elements = "$prefix$prefix1:elements"
        }

        const val Nodes = "$prefix:nodes"
        const val Home = "$prefix:home"
        const val Schedule = "$prefix:schedule"
        const val Account = "$prefix:account"

        object Schedule2 {
            private const val prefix1 = ":schedule"

            const val ScheduleInfo = "$prefix$prefix1:schedule_info"
            const val Calendar = "$prefix$prefix1:calendar"
            const val LessonsReview = "$prefix$prefix1:lessons_review"
            const val Sources = "$prefix$prefix1:sources"
            const val AppWidget = "$prefix$prefix1:appwidget"
        }


        object Misc {
            private const val prefix1 = ":misc"

            const val Menu = "$prefix$prefix1:menu"
            const val Settings = "$prefix$prefix1:settings"
        }
    }
}