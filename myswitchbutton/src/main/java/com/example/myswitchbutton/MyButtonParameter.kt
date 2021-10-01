package com.example.myswitchbutton

/**
 *  If you need a singleton — a class that only has got one instance — you can declare the class in the usual way,
 *  but use the object keyword instead of class:
 */
object MyButtonParameter {
    var leftTxtString = ""
    var rightTxtString = ""
    var txtSize = 0f
    var buttonBackgroundColor = ""
    var buttonChooseColor = ""
    var buttonChooseTwoColor = ""
    var strokeColor = ""
    var strokeWidth = 0
    var buttonRadius = 0f
    var chooseTxtColor = ""
    var chooseTxtSecondColor = ""
    var unChooseTxtColor = ""
    var unChooseTxtSecondColor = ""
    val noChoose = -1
    val leftChoose = 0
    val rightChoose = 1
}