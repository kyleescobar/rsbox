package io.rsbox.launcher

import org.koin.dsl.module

val LAUNCHER_MODULE = module {
    single { Launcher() }
}