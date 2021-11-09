# SwitchButton
-----------------------------------------------------------------------------------------------------------------
Android switchButton

<br></br>

 ![image](https://github.com/jap963852741/SwitchButton/blob/master/example.gif)
 <br></br>
<br></br>

 [![](https://jitpack.io/v/jap963852741/SwitchButton.svg)](https://jitpack.io/#jap963852741/SwitchButton)


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
	        implementation 'com.github.jap963852741:SwitchButton:v1.1.12'
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
 
 <br></br>
| Function | value | Description |
| :-----| :----: | :----: |
| button_background_color | color | 按鈕的背景色 |
| button_radius | dimension | 按鈕圓角設定 |
| stroke_width | int | 按鈕邊緣粗度 |   
| choose_txt_color | color | 按鈕-被選擇邊-的顏色 |   
| choose_txt_two_color | color | 按鈕-被選擇邊-的左至右顏色(可以不設定) |   
| un_choose_txt_color | color | 按鈕-沒被選擇邊-的顏色 |   
| left_txt_string | string | 左邊文字設定 |   
| right_txt_string | string | 右邊文字設定 |   



更詳細設定請參照 ----> [這裡](https://github.com/jap963852741/SwitchButton/blob/master/myswitchbutton/src/main/res/values/attr.xml)
<br></br>
<br></br>
 
 ##  Code Example
    binding.mySwitcher.setChooseLeftOrRight(1)   //預設選哪邊 0 左 右 1
    binding.mySwitcher.setOnClickListener(this)  //點擊監聽
    binding.mySwitcher.getChoose()  // 左邊被選返回 0 右邊 1
   
<br></br>
| Function | parameter | Description |
| :-----| :----: | :----: |
| setChooseLeftOrRight | 0 or 1 | 0 is left 、 1 is right  to choose |
| setOnClickListener | OnclickListener | 監聽設置 |
| getChoose |  | 獲取現在是左邊被選還是右邊 |   
    

