/*
 * # DON'T BE A DICK PUBLIC LICENSE
 *
 * > Version 1.1, December 2016
 *
 * > Copyright (C) 2016 Adam Prakash Lewis
 *
 *  Everyone is permitted to copy and distribute verbatim or modified
 *  copies of this license document.
 *
 * > DON'T BE A DICK PUBLIC LICENSE
 * > TERMS AND CONDITIONS FOR COPYING, DISTRIBUTION AND MODIFICATION
 *
 *  1. Do whatever you like with the original work, just don't be a dick.
 *
 *      Being a dick includes - but is not limited to - the following instances:
 *
 * 	 1a. Outright copyright infringement - Don't just copy this and change the name.
 * 	 1b. Selling the unmodified original with no work done what-so-ever, that's REALLY being a dick.
 * 	 1c. Modifying the original work to contain hidden harmful content. That would make you a PROPER dick.
 *
 *  2. If you become rich through modifications, related works/services, or supporting the original work,
 *  share the love. Only a dick would make loads off this work and not buy the original work's
 *  creator(s) a pint.
 *
 *  3. Code is provided with no warranty. Using somebody else's code and bitching when it goes wrong makes
 *  you a DONKEY dick. Fix the problem yourself. A non-dick would submit the fix back.
 */

package app.controllers

import spark.Session

/**
 * Created by alewis on 21/02/2017.
 */

object ControllerManager {
    val profileController = ProfileController()
    val resetPasswordController = ResetPasswordController()
    val routesAndControllers = mapOf<String, Controller>(Pair("/", IndexController()),
            Pair("/dashboard", DashboardController()),
            Pair("/register", RegisterController()),
            Pair("/dashboard/user_management", UserManagementController()),
            Pair("/dashboard/log_file", LogFileViewController()),
            Pair("/dashboard/page_management", PageManagementController()),
            Pair("/login", LoginController()),
            Pair("/profile", profileController),
            Pair("/profile/:username", profileController),
            Pair("/reset_password/:username", resetPasswordController),
            Pair("/reset_password/:username/:authhash", resetPasswordController))

    fun initSessionAttributes(session: Session) = routesAndControllers.forEach { it.value.initSessionBoolAttributes(session) }
}
