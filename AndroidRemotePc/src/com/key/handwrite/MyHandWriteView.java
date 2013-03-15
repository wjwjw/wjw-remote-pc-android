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

//����ʾ����д�켣��view
public class MyHandWriteView extends SurfaceView implements Callback, Runnable {

	private static final String TAG = "MyHandWriteView";
	private WriteStringMSGSender sender;
	private boolean sended;

	Zinnia _zinnia = null;// ͨ���������

	// ������д�������
	long recognizer = 0;
	long character = 0;
	long result = 0;
	int modelState = 0; // ��ʾmodel�ļ�����״̬
	int strokes = 0; // �ܱʻ���

	boolean resultDisplay = false; // �Ƿ���ʾ���

	int handwriteCount = 0; // �ʻ���

	private Thread mThread;
	SurfaceHolder mSurfaceHolder = null;
	Canvas mCanvas = null;
	Paint mPaint = null;
	Path mPath = null;
	Paint mTextPaint = null; // ���ֻ���
	public static final int FRAME = 60;// ��������֡��
	boolean mIsRunning = false; // �����Ƿ����
	float posX, posY; // �����㵱ǰ����
	// ������ʱʶ������
	Timer tExit;
	TimerTask task;

	public MyHandWriteView(Context context) {
		super(context);

		if (_zinnia == null) {
			_zinnia = new Zinnia();
		}
		// ����ӵ�н���
		this.setFocusable(true);
		// ���ô���ʱӵ�н���
		this.setFocusableInTouchMode(true);
		// ��ȡholder
		mSurfaceHolder = this.getHolder();
		// ���holder��callback����֮��
		mSurfaceHolder.addCallback(this);

		// ��������
		mCanvas = new Canvas();

		// ��������
		mPaint = new Paint();
		mPaint.setColor(Color.BLUE);// ��ɫ
		mPaint.setAntiAlias(true);// �����
		// Paint.Style.STROKE ��Paint.Style.FILL��Paint.Style.FILL_AND_STROKE
		// ��˼�ֱ�Ϊ ���� ��ʵ�ġ�ʵ�������
		mPaint.setStyle(Paint.Style.STROKE);
		mPaint.setStrokeCap(Paint.Cap.ROUND);// ���û���ΪԲ��״
		mPaint.setStrokeWidth(5);// �����ߵĿ��

		// ����·���켣
		mPath = new Path();

		// �������ֻ���
		mTextPaint = new Paint();
		mTextPaint.setColor(Color.BLACK);
		mTextPaint.setTextSize(15);

		// ������дʶ��
		if (character == 0) {
			character = _zinnia.zinnia_character_new();
			_zinnia.zinnia_character_clear(character);
			_zinnia.zinnia_character_set_width(character, 400);
			_zinnia.zinnia_character_set_height(character, 400);
		}
		if (recognizer == 0) {
			recognizer = _zinnia.zinnia_recognizer_new();
		}

		// �򿪳ɹ�����1
		modelState = _zinnia.zinnia_recognizer_open(recognizer,
				"/data/data/handwriting-zh_CN.model");
		if (modelState != 1) {
			System.out.println("model�ļ���ʧ��");
			return;
		}

		{//���ͽӿ�
			sender = new WriteStringMSGSender();
		}
	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {

		// ��ȡ���������Լ�����
		int action = event.getAction();
		float x = event.getX();
		float y = event.getY();

		// �����������ַ�ִ������
		switch (action) {
		case MotionEvent.ACTION_DOWN:
			if (tExit != null) {
				tExit.cancel();
				tExit = null;
				task = null;
			}
			resultDisplay = false;
			mPath.moveTo(x, y);// �趨�켣����ʼ��
			break;

		case MotionEvent.ACTION_MOVE:
			mPath.quadTo(posX, posY, x, y); // �津���ƶ����ù켣
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

		// ��¼��ǰ����
		posX = x;
		posY = y;

		return true;
	}

	private void Draw() {
		// ��ֹcanvasΪnull���³���null pointer����
		if (mCanvas != null) {
			mCanvas.drawColor(Color.WHITE); // ��ջ���
			mCanvas.drawPath(mPath, mPaint); // �����켣
			// ���ݼ�¼
			mCanvas.drawText("model��״̬ : " + modelState, 5, 20, mTextPaint);
			mCanvas.drawText("����X������ : " + posX, 5, 40, mTextPaint);
			mCanvas.drawText("����Y������ : " + posY, 5, 60, mTextPaint);

			strokes = (int) _zinnia.zinnia_character_strokes_size(character);
			mCanvas.drawText("�ܱʻ��� : " + strokes, 5, 80, mTextPaint);
		}

		// �������ּ���
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
			mPath.reset();// ��������������켣
			resultDisplay = false;
			handwriteCount = 0;
			sended = false;
		}

		// ��ʾʶ���������
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
			// ����ǰ��ʱ��
			long startTime = System.currentTimeMillis();
			try {
				// �̰߳�ȫ��
				synchronized (mSurfaceHolder) {
					mCanvas = mSurfaceHolder.lockCanvas();
					Draw();
					mSurfaceHolder.unlockCanvasAndPost(mCanvas);
				}

			} catch (Exception e) {

			}

			// ��ȡ���º��ʱ��
			long endTime = System.currentTimeMillis();
			// ��ȡ����ʱ���
			int diffTime = (int) (endTime - startTime);
			// ȷ��ÿ�θ��¶�ΪFRAME
			while (diffTime <= FRAME) {
				diffTime = (int) (System.currentTimeMillis() - startTime);
				// Thread.yield(): ��Thread.sleep(long millis):������
				// Thread.yield(): ����ͣ��ǰ����ִ�е��̶߳��� ����ȥִ�������̡߳�
				// Thread.sleep(long millis):����ʹ��ǰ�߳���ͣ��������ָ���ĺ�����Ȼ���ڼ���ִ���߳�
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
