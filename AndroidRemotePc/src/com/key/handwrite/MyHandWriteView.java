package com.key.handwrite;

import java.util.Timer;
import java.util.TimerTask;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceHolder.Callback;
import android.view.SurfaceView;

//能显示出手写轨迹的view
public class MyHandWriteView extends SurfaceView implements Callback, Runnable {

	private static final String TAG = "MyHandWriteView";
	private WriteStringMSGSender sender;
	private boolean sended;

	Zinnia _zinnia = null;// 通过对象调用

	// 建立手写输入对象
	long recognizer = 0;
	long character = 0;
	long result = 0;
	int modelState = 0; // 显示model文件载入状态
	int strokes = 0; // 总笔画数

	boolean resultDisplay = false; // 是否显示结果

	int handwriteCount = 0; // 笔画数

	private Thread mThread;
	SurfaceHolder mSurfaceHolder = null;
	Canvas mCanvas = null;
	Paint mPaint = null;
	Path mPath = null;
	Paint mTextPaint = null; // 文字画笔
	public static final int FRAME = 60;// 画布更新帧数
	boolean mIsRunning = false; // 控制是否更新
	float posX, posY; // 触摸点当前座标
	// 触发定时识别任务
	Timer tExit;
	TimerTask task;

	public MyHandWriteView(Context context) {
		super(context);

		if (_zinnia == null) {
			_zinnia = new Zinnia();
		}
		// 设置拥有焦点
		this.setFocusable(true);
		// 设置触摸时拥有焦点
		this.setFocusableInTouchMode(true);
		// 获取holder
		mSurfaceHolder = this.getHolder();
		// 添加holder到callback函数之中
		mSurfaceHolder.addCallback(this);

		// 创建画布
		mCanvas = new Canvas();

		// 创建画笔
		mPaint = new Paint();
		mPaint.setColor(Color.BLUE);// 颜色
		mPaint.setAntiAlias(true);// 抗锯齿
		// Paint.Style.STROKE 、Paint.Style.FILL、Paint.Style.FILL_AND_STROKE
		// 意思分别为 空心 、实心、实心与空心
		mPaint.setStyle(Paint.Style.STROKE);
		mPaint.setStrokeCap(Paint.Cap.ROUND);// 设置画笔为圆滑状
		mPaint.setStrokeWidth(5);// 设置线的宽度

		// 创建路径轨迹
		mPath = new Path();

		// 创建文字画笔
		mTextPaint = new Paint();
		mTextPaint.setColor(Color.BLACK);
		mTextPaint.setTextSize(15);

		// 创建手写识别
		if (character == 0) {
			character = _zinnia.zinnia_character_new();
			_zinnia.zinnia_character_clear(character);
			_zinnia.zinnia_character_set_width(character, 400);
			_zinnia.zinnia_character_set_height(character, 400);
		}
		if (recognizer == 0) {
			recognizer = _zinnia.zinnia_recognizer_new();
		}

		// 打开成功返回1
		modelState = _zinnia.zinnia_recognizer_open(recognizer,
				"/data/data/handwriting-zh_CN.model");
		if (modelState != 1) {
			System.out.println("model文件打开失败");
			return;
		}

		{//发送接口
			sender = new WriteStringMSGSender();
		}
	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {

		// 获取触摸动作以及座标
		int action = event.getAction();
		float x = event.getX();
		float y = event.getY();

		// 按触摸动作分发执行内容
		switch (action) {
		case MotionEvent.ACTION_DOWN:
			if (tExit != null) {
				tExit.cancel();
				tExit = null;
				task = null;
			}
			resultDisplay = false;
			mPath.moveTo(x, y);// 设定轨迹的起始点
			break;

		case MotionEvent.ACTION_MOVE:
			mPath.quadTo(posX, posY, x, y); // 随触摸移动设置轨迹
			_zinnia.zinnia_character_add(character, handwriteCount, (int) x,
					(int) y);
			break;

		case MotionEvent.ACTION_UP:
			handwriteCount++;
			tExit = new Timer();
			task = new TimerTask() {

				@Override
				public void run() {
					resultDisplay = true;
				}
			};
			tExit.schedule(task, 1000);
			break;
		}

		// 记录当前座标
		posX = x;
		posY = y;

		return true;
	}

	private void Draw() {
		// 防止canvas为null导致出现null pointer问题
		if (mCanvas != null) {
			mCanvas.drawColor(Color.WHITE); // 清空画布
			mCanvas.drawPath(mPath, mPaint); // 画出轨迹
			// 数据记录
			mCanvas.drawText("model打开状态 : " + modelState, 5, 20, mTextPaint);
			mCanvas.drawText("触点X的座标 : " + posX, 5, 40, mTextPaint);
			mCanvas.drawText("触点Y的座标 : " + posY, 5, 60, mTextPaint);

			strokes = (int) _zinnia.zinnia_character_strokes_size(character);
			mCanvas.drawText("总笔画数 : " + strokes, 5, 80, mTextPaint);
		}

		// 进行文字检索
		if (strokes > 0 && resultDisplay) {
			result = _zinnia.zinnia_recognizer_classify(recognizer, character,
					10);
			if (tExit != null) {
				tExit.cancel();
				tExit = null;
				task = null;
			}
			_zinnia.zinnia_character_clear(character);
			strokes = 0;
			mPath.reset();// 触摸结束即清除轨迹
			resultDisplay = false;
			handwriteCount = 0;
			sended = false;
		}

		// 显示识别出的文字
		if (result != 0) {
			for (int i = 0; i < _zinnia.zinnia_result_size(result); i++) {
				String result_value = _zinnia.zinnia_result_value(result, i);
				mCanvas.drawText(result_value + " : "
						+ _zinnia.zinnia_result_score(result, i), 5,
						100 + i * 20, mTextPaint);
				if(i == 0 && !sended){
					send(result_value);
				}
			}
		}

	}

	@Override
	public void run() {

		while (mIsRunning) {
			// 更新前的时间
			long startTime = System.currentTimeMillis();
			try {
				// 线程安全锁
				synchronized (mSurfaceHolder) {
					mCanvas = mSurfaceHolder.lockCanvas();
					Draw();
					mSurfaceHolder.unlockCanvasAndPost(mCanvas);
				}

			} catch (Exception e) {

			}

			// 获取更新后的时间
			long endTime = System.currentTimeMillis();
			// 获取更新时间差
			int diffTime = (int) (endTime - startTime);
			// 确保每次更新都为FRAME
			while (diffTime <= FRAME) {
				diffTime = (int) (System.currentTimeMillis() - startTime);
				// Thread.yield(): 与Thread.sleep(long millis):的区别，
				// Thread.yield(): 是暂停当前正在执行的线程对象 ，并去执行其他线程。
				// Thread.sleep(long millis):则是使当前线程暂停参数中所指定的毫秒数然后在继续执行线程
				Thread.yield();
			}
		}

	}

	@Override
	public void surfaceCreated(SurfaceHolder holder) {
		mIsRunning = true;
		mThread = new Thread(this);
		mThread.start();
	}

	@Override
	public void surfaceChanged(SurfaceHolder holder, int format, int width,
			int height) {
	}

	@Override
	public void surfaceDestroyed(SurfaceHolder holder) {
		_zinnia.zinnia_result_destroy(result);
		_zinnia.zinnia_character_destroy(character);
		_zinnia.zinnia_recognizer_destroy(recognizer);
		mThread = null;
	}
	
	private void send(String str){
		sender.sendStringEvent(str);
		sended = true;
	}


}
