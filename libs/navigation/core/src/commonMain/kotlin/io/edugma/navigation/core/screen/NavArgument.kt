package io.edugma.navigation.core.screen

public sealed class NavArgument<T>(
    public val name: String,
    public val type: ArgumentType,
) {
    public val isRequired: Boolean
        get() = this is Required

    public val isOptional: Boolean
        get() = this is Optional || this is NullableOptional

    public class Required<T>(
        name: String,
        type: ArgumentType,
    ) : NavArgument<T>(
        name = name,
        type = type,
    )

    public class Optional<T>(
        name: String,
        type: ArgumentType,

        public val defaultValue: T,
    ) : NavArgument<T>(
        name = name,
        type = type,
    )

    public class NullableOptional<T>(
        name: String,
        type: ArgumentType,

        public val defaultValue: T? = null,
    ) : NavArgument<T>(
        name = name,
        type = type,
    )
}

