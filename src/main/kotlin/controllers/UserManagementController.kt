/*
# DON'T BE A DICK PUBLIC LICENSE

> Version 1.1, December 2016

> Copyright (C) 2016 Adam Prakash Lewis
 
 Everyone is permitted to copy and distribute verbatim or modified
 copies of this license document.

> DON'T BE A DICK PUBLIC LICENSE
> TERMS AND CONDITIONS FOR COPYING, DISTRIBUTION AND MODIFICATION

 1. Do whatever you like with the original work, just don't be a dick.

     Being a dick includes - but is not limited to - the following instances:

	 1a. Outright copyright infringement - Don't just copy this and change the name.  
	 1b. Selling the unmodified original with no work done what-so-ever, that's REALLY being a dick.  
	 1c. Modifying the original work to contain hidden harmful content. That would make you a PROPER dick.  

 2. If you become rich through modifications, related works/services, or supporting the original work,
 share the love. Only a dick would make loads off this work and not buy the original work's 
 creator(s) a pint.
 
 3. Code is provided with no warranty. Using somebody else's code and bitching when it goes wrong makes 
 you a DONKEY dick. Fix the problem yourself. A non-dick would submit the fix back.
 */
 
 
 
 package controllers

import com.sun.org.apache.xpath.internal.operations.Bool
import handlers.UserHandler
import j2html.TagCreator.*
import j2html.tags.ContainerTag
import j2html.tags.Tag
import mu.KLogging
import spark.ModelAndView
import spark.Request
import spark.Response
import utils.HTMLTable
import java.util.*

/**
 * Created by alewis on 07/11/2016.
 */

object UserManagementController : KLogging() {

    fun get_getUserManagement(request: Request, response: Response, layoutTemplate: String): ModelAndView {
        logger.info("${UserHandler.getSessionIdentifier(request)} -> Received GET request for USER_MANAGEMENT page")
        var model = HashMap<String, Any>()
        model.put("template", "/templates/user_management.vtl")
        model.put("title", "Thames Valley Furs - User Management")

        val userAdminForm = genUserForm(request, response)
        model.put("user_admin_form", userAdminForm.render())
        model = Web.loadNavBar(request, response, model)
        return ModelAndView(model, layoutTemplate)
    }

    fun post_userManagement(request: Request, response: Response) {
        logger.info("${UserHandler.getSessionIdentifier(request)} -> Received post submission for user management page")
        Web.initSessionAttributes(request.session())
        val usersAndBanned = getUserBannedState(request.body())
        usersAndBanned.forEach {
            for ((username, banned) in it) {
                if (username == UserHandler.loggedInUsername(request)) continue
                if (banned) {
                    logger.info("${UserHandler.getSessionIdentifier(request)} -> has banned user $username")
                    UserHandler.ban(username)
                } else {
                    logger.info("${UserHandler.getSessionIdentifier(request)} -> has unbanned user $username")
                    UserHandler.unban(username)
                }
            }
        }
        response.redirect("/dashboard/user_management")
    }

    private fun getUserBannedState(body: String): MutableList<MutableMap<String, Boolean>> {
        val usersAndBanned = mutableListOf<MutableMap<String, Boolean>>()
        val bodyAttributes = body.split("&")
        val usernameAndBanned = mutableMapOf<String, Boolean>()
        bodyAttributes.forEach { attribute ->
            if (attribute.contains("banned_checkbox.hidden")) {
                val username = attribute.split("=")[1]
                usernameAndBanned.put(username, false)
            }
            if (attribute.contains("banned_checkbox") && !attribute.contains(".hidden")) {
                val username = attribute.split("=")[1]
                usernameAndBanned.put(username, true)
            }
        }
        usersAndBanned.add(usernameAndBanned)
        return usersAndBanned
    }

    private fun genUserForm(request: Request, response: Response): ContainerTag {

        val userAdminForm = form().withMethod("post").withClass("pure-form").withAction("/dashboard/user_management").withMethod("post")

        val userListTable = HTMLTable(listOf("Full Name", "Username", "Email Address", "Banned"))
        userListTable.className = "pure-table"
        UserHandler.userDAO.getUsers().filter { it.username != UserHandler.loggedInUsername(request) }.forEach { user ->
            val bannedCheckbox = input().withType("checkbox").withId(user.username).withValue(user.username).withName("banned_checkbox")
            if (UserHandler.isBanned(user.username)) run { bannedCheckbox.attr("checked", "") }
            userListTable.addRow(listOf(listOf<Tag>(label(user.fullName).withName(user.username).withId(user.username)),
                                        listOf<Tag>(label(user.username).withName(user.username).withId(user.username)),
                                        listOf<Tag>(label(user.email).withName(user.username).withId(user.username)),
                                        listOf<Tag>(input().withType("hidden").withId(user.username).withValue(user.username).withName("banned_checkbox.hidden"), bannedCheckbox)))
        }
        userAdminForm.with(userListTable.render())
        userAdminForm.with(input().withType("submit").withName("update_user_management").withId("update_user_management").withValue("Update"))
        return userAdminForm
    }
}