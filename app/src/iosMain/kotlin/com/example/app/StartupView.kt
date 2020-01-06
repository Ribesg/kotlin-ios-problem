package com.example.app

import kotlinx.cinterop.CValue
import platform.CoreGraphics.CGRect
import platform.UIKit.UIColor
import platform.UIKit.UIView
import platform.UIKit.backgroundColor

class StartupView(frame: CValue<CGRect>) : UIView(frame) {

    init {
        backgroundColor = UIColor.yellowColor
    }

}
