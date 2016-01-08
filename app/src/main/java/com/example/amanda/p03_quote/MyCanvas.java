package com.example.amanda.p03_quote;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;


public class MyCanvas extends View {

	Paint cv_paint;

	public MyCanvas(Context c, AttributeSet attrs) {

		super(c, attrs);
		// TODO Auto-generated constructor stub
	}

	@Override
	protected void onDraw(Canvas canvas) {
		// TODO Auto-generated method stub
		super.onDraw(canvas);

		cv_paint = new Paint();
		Bitmap cv_b = BitmapFactory.decodeResource(getResources(), R.drawable.graph);
		cv_paint.setColor(Color.RED);
		canvas.drawBitmap(cv_b, 0, 0, cv_paint);

	}
}
