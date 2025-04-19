package org.levast.project

class DesktopPlatform: Platform {
    override val name: String = "Desktop version"
}

actual fun getPlatform(): Platform = DesktopPlatform()