# SwitchButton
-----------------------------------------------------------------------------------------------------------------
Android switchButton

<br></br>

 ![image](https://github.com/jap963852741/SwitchButton/blob/master/example.gif)
 

<br></br>
<br></br>

## Install
 Step 1. Add it in your root build.gradle at the end of repositories:

	allprojects {
		repositories {
			...
			maven { url 'https://jitpack.io' }
		}
	}
 Step 2. Add the dependency

	dependencies {
	        implementation 'com.github.jap963852741:SwitchButton:v1.1.7'
	}
  
  
## Usage Example

    <com.example.myswitchbutton.MyButtonView
        android:id="@+id/mySwitcher"
        android:layout_width="150dp"
        android:layout_height="40dp"
        app:button_background_color="#ffffff"
        app:button_radius="13dp"
        app:stroke_width = "2"
        app:stroke_color="#FF0000"
        app:choose_txt_color="#ffffff"
        app:un_choose_txt_color="#ffffff"
        app:un_choose_txt_second_color="#10B4DD"
        app:button_choose_color="#10B4DD"
        app:button_choose_two_color="#000000"
        app:left_txt_string="left"
        app:right_txt_string="right"
        app:txt_size="12dp"
        app:layout_constraintBottom_toBottomOf="parent"
        app:layout_constraintLeft_toLeftOf="parent"
        app:layout_constraintRight_toRightOf="parent"
        app:layout_constraintTop_toTopOf="parent" />
 
 ##  Code
    binding.mySwitcher.setDefaultChooseLeftOrRight(1)   //預設選哪邊 0 左 右 1
    binding.mySwitcher.setOnClickListener(this)  //點擊監聽
    binding.mySwitcher.getChoose()  // 左邊被選返回 0 右邊 1
