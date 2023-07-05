import SwiftUI
import shared

@main
struct iosApp: App {

    init() {
        HelperKt.doInitKoin()
    }

    var body: some Scene {
        WindowGroup {
            ContentView()
        }
    }
}
