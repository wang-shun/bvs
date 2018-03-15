package com.bizivisionsoft.widgets.gesture;

public class GestureEvent {

	/**
	 * 代表设备顺时针或者逆时针旋转.此事件可以被设备触发,可能使用的是重力传感器.
	 */
	public static final String orientationchange = "orientationchange";
	/**
	 * 缩放手势(两个手指在屏幕上的相对运动)
	 */
	public static final String pinch = "pinch";
	/**
	 * 捏紧手势,当两个手指捏紧并离开设备时触发.
	 */
	public static final String pinchclose = "pinchclose";
	/**
	 * 撑开手势,当两个手指撑大并离开设备时触发.
	 */
	public static final String pinchopen = "pinchopen";
	/**
	 * 旋转手势(两个手指顺时针或者逆时针旋转)
	 */
	public static final String rotate = "rotate";
	/**
	 * 两个手指逆时针旋转并且离开屏幕时触发
	 */
	public static final String rotateccw = "rotateccw";
	/**
	 * 两个手指顺时针旋转并且离开屏幕时触发(two
	 */
	public static final String rotatecw = "rotatecw";
	/**
	 * 当检测到设备正在摇晃时触发
	 */
	public static final String shake = "shake";
	/**
	 * 当检测到摇晃动作，且可以被解读为前后移动之时触发.
	 */
	public static final String shakefrontback = "shakefrontback";
	/**
	 * 当检测到摇晃动作，且可以被解读为左右移动之时触发.
	 */
	public static final String shakeleftright = "shakeleftright";
	/**
	 * 当检测到摇晃动作，且可以被解读为上下移动之时触发.
	 */
	public static final String shakeupdown = "shakeupdown";
	/**
	 * 向下滑动,在严格的向下滑动手势完成后触发
	 */
	public static final String swipedown = "swipedown";
	/**
	 * 四点滑动(四个手指在屏幕上方向一致的滑动)
	 */
	public static final String swipefour = "swipefour";
	/**
	 * 向左滑动,在严格的向左滑动手势完成后触发
	 */
	public static final String swipeleft = "swipeleft";
	/**
	 * 向左下角滑动,在向左且向下的滑动手势完成后触发
	 */
	public static final String swipeleftdown = "swipeleftdown";
	/**
	 * 向左上角滑动,在向左且向上的滑动手势完成后触发
	 */
	public static final String swipeleftup = "swipeleftup";
	/**
	 * 在正在滑动时触发(在设备屏幕上移动手指,比如:拖动)
	 */
	public static final String swipemove = "swipemove";
	/**
	 * 单点滑动手势,滑动完成后触发(一个手指在屏幕上移动)
	 */
	public static final String swipeone = "swipeone";
	/**
	 * 向右滑动,在严格的向右滑动手势完成后触发
	 */
	public static final String swiperight = "swiperight";
	/**
	 * 向右下角滑动,在向右且向下的滑动手势完成后触发
	 */
	public static final String swiperightdown = "swiperightdown";
	/**
	 * 向右上角滑动,在向右且向上的滑动手势完成后触发
	 */
	public static final String swiperightup = "swiperightup";
	/**
	 * 三点滑动(三个手指在屏幕上方向一致的滑动)
	 */
	public static final String swipethree = "swipethree";
	/**
	 * 两点滑动(两个手指在屏幕上方向一致的滑动)
	 */
	public static final String swipetwo = "swipetwo";
	/**
	 * 向上滑动,在严格的向上滑动手势完成后触发
	 */
	public static final String swipeup = "swipeup";
	/**
	 * 在单个手指轻点的手势后触发
	 */
	public static final String tapone = "tapone";
	/**
	 * 在三个手指一起轻点的手势后触发
	 */
	public static final String tapthree = "tapthree";
	/**
	 * 在两个手指一起轻点的手势后触发
	 */
	public static final String taptwo = "taptwo";
	
	/**
	 * 事件代码
	 */
	public static final String event = "gestureEvent";

}
