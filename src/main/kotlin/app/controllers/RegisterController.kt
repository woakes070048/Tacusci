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

import app.handlers.UserHandler
import database.models.User
import extensions.managedRedirect
import mu.KLogging
import spark.ModelAndView
import spark.Request
import spark.Response
import spark.Session
import utils.Validation
import utils.j2htmlPartials
import java.util.*

/**
 * Created by alewis on 02/02/2017.
 */

class RegisterController : Controller {

    companion object : KLogging()

    override fun initSessionBoolAttributes(session: Session) {
        hashMapOf(Pair("full_name_field_error", false), Pair("username_field_error", false), Pair("password_field_error", false),
                Pair("repeated_password_field_error", false), Pair("email_field_error", false), Pair("username_not_available_error", false),
                Pair("username_not_available", ""), Pair("user_created_successfully", false), Pair("passwords_mismatch_error", false)).forEach { key, value -> if (!session.attributes().contains(key)) session.attribute(key, value) }
    }

    override fun get(request: Request, response: Response, layoutTemplate: String): ModelAndView {
        Web.logger.info("${UserHandler.getSessionIdentifier(request)} -> Received GET request for REGISTER page")

        var model = HashMap<String, Any>()
        model = Web.loadNavBar(request, model)

        model.put("template", "/templates/register.vtl")
        model.put("title", "Thames Valley Furs - Sign Up")
        model.put("register_form", j2htmlPartials.pureFormAligned_Register(request.session(), "register_form", "/register", "post").render())

        return ModelAndView(model, layoutTemplate)
    }

    override fun post(request: Request, response: Response): Response {
        Web.logger.info("${UserHandler.getSessionIdentifier(request)} -> Received POST submission for REGISTER page")

        if (Web.getFormHash(request.session(), "register_form") == request.queryParams("hashid")) {
            val fullName = request.queryParams("full_name")
            val username = request.queryParams("username")
            val password = request.queryParams("password")
            val repeatedPassword = request.queryParams("repeat_password")
            val email = request.queryParams("email")

            request.session().attribute("user_created_successfully", false)

            val fullNameInputIsValid = Validation.matchFullNamePattern(fullName)
            val usernameInputIsValid = Validation.matchUsernamePattern(username)
            val passwordInputIsValid = Validation.matchPasswordPattern(password)
            val repeatedPasswordIsValid = Validation.matchPasswordPattern(repeatedPassword)
            val emailIsValid = Validation.matchEmailPattern(email)

            if (!fullNameInputIsValid) request.session().attribute("full_name_field_error", true) else request.session().attribute("full_name_field_error", false)
            if (!usernameInputIsValid) request.session().attribute("username_field_error", true) else request.session().attribute("username_field_error", false)
            if (!passwordInputIsValid) request.session().attribute("password_field_error", true) else request.session().attribute("password_field_error", false)
            if (!repeatedPasswordIsValid) request.session().attribute("repeated_password_field_error", true) else request.session().attribute("repeated_password_field_error", false)
            if (!emailIsValid) request.session().attribute("email_field_error", true) else request.session().attribute("email_field_error", false)

            if (usernameInputIsValid) {
                if (UserHandler.userExists(username)) {
                    request.session().attribute("username_not_available_error", true)
                    request.session().attribute("username_not_available", username)
                }
            }

            if (passwordInputIsValid && repeatedPasswordIsValid) {
                if (password != repeatedPassword) request.session().attribute("passwords_mismatch_error", true) else request.session().attribute("passwords_mismatch_error", false)
            }

            if (fullNameInputIsValid && usernameInputIsValid && passwordInputIsValid && repeatedPasswordIsValid && emailIsValid && (password == repeatedPassword)) {
                val user = User(-1, -1, -1, fullName, username, password, email, 0, 0)
                request.session().attribute("user_created_successfully", true)
                UserHandler.createUser(user)
            }
        } else {
            Web.logger.warn("${UserHandler.getSessionIdentifier(request)} -> has submitted an invalid register form...")
        }
        response.managedRedirect(request, "/register")
        return response
    }
}