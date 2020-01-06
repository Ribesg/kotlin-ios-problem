package com.example.app

import platform.UIKit.UIViewController

class StartupViewController : UIViewController(null, null) {

    override fun loadView() {
        super.loadView()
        view = StartupView(view.frame)
    }

}
