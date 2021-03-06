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

package utils

/**
 * Created by alewis on 01/02/2017.
 */
object Validation {

    fun matchPasswordPattern(password: String) = Regex(passwordRegexStruct()).matches(password)
    fun passwordRegexStruct() = "(?=.*\\d)(?=.*[a-z])(?=.*[A-Z]).{8,}"
    fun getPasswordValidationMessage(): String = "Password must contain at least 8 characters including at least one number and one uppercase and lowercase letter"

    fun matchEmailPattern(email: String) = Regex(emailRegexStruct()).matches(email)
    fun emailRegexStruct() = "[a-z0-9._%+-]+@[a-z0-9.-]+\\.[a-z]{2,3}$"
    fun getEmailValidationMessage(): String = "Must be a valid email address"

    fun matchUsernamePattern(username: String) = Regex(usernameRegexStruct()).matches(username)
    fun usernameRegexStruct() = "^[a-zA-Z0-9_]{2,20}$"
    fun getUsernameValidationMessage(): String = "Username must be between 2 and 20 characters and can only contain underscores"

    fun matchFullNamePattern(fullName: String) = Regex(fullNameRegexStruct()).matches(fullName)
    fun fullNameRegexStruct() = "^[ \\da-zA-Z,.'-]{2,30}$"
    fun getFullNameValidationMessage(): String = "Full name must be at least 2 characters long"
}