object Modules {
    object Data {
        private const val prefix = ":data"

        const val Base = "$prefix:base"
    }

    object Domain {
        private const val prefix = ":domain"

        const val Base = "$prefix:base"
        const val Nodes = "$prefix:nodes"
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
    }
}