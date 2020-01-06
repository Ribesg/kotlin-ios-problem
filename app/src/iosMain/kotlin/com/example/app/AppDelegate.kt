package com.example.app

import com.example.lib.Bugsnag
import platform.UIKit.*

class AppDelegate @OverrideInit constructor() : UIResponder(), UIApplicationDelegateProtocol {

    companion object : UIResponderMeta(), UIApplicationDelegateProtocolMeta {
        private const val BUGSNAG_API_KEY = "Doesn't work anyway"
    }

    private var uiWindow: UIWindow? = null

    override fun application(
        application: UIApplication,
        didFinishLaunchingWithOptions: Map<Any?, *>?
    ): Boolean {
        setupBugsnag()
        setupWindow()
        return true
    }

    private fun setupBugsnag() {
        Bugsnag.configure(BUGSNAG_API_KEY)
    }

    private fun setupWindow() {
        setWindow(UIWindow(frame = UIScreen.mainScreen.bounds))
        window!!.run {
            backgroundColor = UIColor.whiteColor
            rootViewController = createRootController()
            makeKeyAndVisible()
        }
    }

    private fun createRootController(): UINavigationController =
        UINavigationController(rootViewController = StartupViewController()).apply {
            navigationBarHidden = true
        }

    override fun window(): UIWindow? = uiWindow

    override fun setWindow(window: UIWindow?) {
        uiWindow = window
    }

}
