package com.edugma.navigation.core.destination

class NavDeepLink(
    val uriPattern: String?,
    val action: String?,
    val mimeType: String?,
) {
    init {
        check(!(uriPattern == null && action == null && mimeType == null)) {
            "Диплинка должна иметь uri, action, и/или mimeType."
        }
        check(!(action != null && action.isEmpty())) {
            "Диплинка не может иметь пустой action."
        }
    }
}
