package com.nusabahana.view;

import java.util.ArrayList;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;

/**
 * Salah satu kelas prototipe untuk implementasi Multitouch
 * 
 * @author PPL-B1
 * 
 */
public class DistributorView extends ImageView {
	private ArrayList<View> registeredViews;

	public DistributorView(Context context, AttributeSet attrs) {
		super(context, attrs);
		registeredViews = new ArrayList<View>();
		// TODO Auto-generated constructor stub
	}

	public void registerView(View v) {
		registeredViews.add(v);
	}

//	public void unregisterView(View v) {
//		registeredViews.remove(v);
//	}

	public void clear(){
		registeredViews.clear();
	}
	
	private void dealEvent(final int actionPointerIndex, final MotionEvent event) {
		//
		// dumpEvent(event);
		// Event location
		int rawX, rawY;
		final int location[] = { 0, 0 };
		this.getLocationOnScreen(location);
		// Log.v("Acation Index", "index :"+actionPointerIndex);
		rawX = (int) event.getX(actionPointerIndex) + location[0];
		rawY = (int) event.getY(actionPointerIndex) + location[1];
		// Log.v("tag", "location:[" + location[0] + "," + location[1]
		// + "] --- raw:[" + rawX + "," + rawY + "]");

		// final int actionPointerID = event.getPointerId(actionPointerIndex);
		// ArrayList<View> hoverViews =
		getTouchedViews(rawX, rawY);
		// hoverViews.remove(this);
		// Log.v("Test", "Distribute to:" + hoverViews.size());
		// for (final View view : hoverViews) {

		// final int viewLocation[] = { 0, 0 };
		// view.getLocationOnScreen(viewLocation);
		//
		// Log.d("View Location", "[x,y] = [" + viewLocation[0] + " ,"
		// + viewLocation[1] + "] -- Width,Heigt =(" + view.getWidth()
		// + " ," + view.getHeight() + ")");
		// // Log.d("View Location",
		// // "[x,y] = [" + viewLocation[0] + " ," + viewLocation[1]
		// // + "] -- Right,Bottom =(" + view.getMeasuredWidth()
		// // + " ," + view.getMeasuredHeight() + ")");
		// int x, y;
		// view.getLocationOnScreen(location);
		// x = rawX - location[0];
		// y = rawY - location[1];
		//
		// final MotionEvent me = MotionEvent.obtain(event);
		// me.setLocation(x, y);

		// if (view instanceof NoteImage) {
		// // deals the Event
		// ((NoteImage) view).play();
		// }
		// }

	}

	final String names[] = { "DOWN", "UP", "MOVE", "CANCEL", "OUTSIDE",
			"POINTER_DOWN", "POINTER_UP", "7?", "8?", "9?" };

	private void dumpEvent(final MotionEvent event) {

		final StringBuilder sb = new StringBuilder();
		final int action = event.getAction();
		final int actionCode = action & MotionEvent.ACTION_MASK;
		sb.append("NEWWWW, event ACTION_").append(names[actionCode]);
		if (actionCode == MotionEvent.ACTION_POINTER_DOWN
				|| actionCode == MotionEvent.ACTION_POINTER_UP) {
			sb.append("(pid ").append(
					action >> MotionEvent.ACTION_POINTER_ID_SHIFT);
			sb.append(")");
		}
		sb.append("[");
		for (int i = 0; i < event.getPointerCount(); i++) {
			sb.append("#").append(i);
			sb.append("(pid ").append(event.getPointerId(i));
			sb.append(")=").append((int) event.getX(i));
			sb.append(",").append((int) event.getY(i));
			if (i + 1 < event.getPointerCount()) {
				sb.append(";");
			}
		}
		sb.append("]");
		Log.d("tag", sb.toString());
	}

	// private ArrayList<View> getChildViews(final View view) {
	// final ArrayList<View> views = new ArrayList<View>();
	// if (view instanceof ViewGroup) {
	// final ViewGroup v = ((ViewGroup) view);
	// if (v.getChildCount() > 0) {
	// for (int i = 0; i < v.getChildCount(); i++) {
	// views.add(v.getChildAt(i));
	// }
	//
	// }
	// }
	// return views;
	// }

	private ArrayList<View> getTouchedViews(final int x, final int y) {

		// final ArrayList<View> touchedViews = new ArrayList<View>();
		for (View view : registeredViews) {
			// final View view = registeredViews.get(i);

			final int location[] = { 0, 0 };
			view.getLocationOnScreen(location);

			if (((view.getHeight() + location[1] >= y)
					& (view.getWidth() + location[0] >= x) & (location[0] <= x) & (location[1] <= y))
			// || view instanceof FrameLayout
			) {
				if (view instanceof NoteImage) {
					// deals the Event
					((NoteImage) view).play();
				} else if (view instanceof InstructionImage){
					((InstructionImage)view).doInstruction();
				}
				// final int viewLocation[] = { 0, 0 };
				// view.getLocationOnScreen(viewLocation);

				// Log.d("View Element", "[x,y] = [" + viewLocation[0] + " ,"
				// + viewLocation[1] + "] -- Width,Heigt =(" + view.getWidth()
				// + " ," + view.getHeight() + ")");
				// touchedViews.add(view);
				// possibleViews.addAll(getChildViews(view));
			}

		}
		// }

		return null;// touchedViews;

	}

	// @Override
	// public boolean onTouchEvent(final MotionEvent event) {
	//
	//
	// }
	//
	// private boolean pointInView(final float localX, final float localY,
	// final float slop, final float width, final float height) {
	// return localX >= -slop && localY >= -slop && localX < ((width) + slop)
	// && localY < ((height) + slop);
	// }

	@Override
	public boolean onTouchEvent(final MotionEvent event) {
		//dumpEvent(event);
		//
		final int[] a = { 0, 0 };
		// v.getLocationOnScreen(a);
		event.setLocation(a[0] + event.getX(), a[1] + event.getY());
		// Log.v("tag fuuuuuuuuu : "+event.getPointerCount(), "real event:(" +
		// event.getX() + "," + event.getY() +")--- tag :");

		// start
		int action = event.getActionMasked();
		int index = event.getActionIndex();
		// int actionMasked = action & MotionEvent.ACTION_MASK;

		switch (action) {
		case MotionEvent.ACTION_DOWN:
			//Log.v("Down", "Add id = " + event.getPointerId(0));
			dealEvent(0, event);
			break;
		case MotionEvent.ACTION_POINTER_DOWN:
			int id = action >> MotionEvent.ACTION_POINTER_ID_SHIFT;
			//Log.v("Pointer Down", "Add id = " + id);
			dealEvent(index, event);
			break;

		// case MotionEvent.ACTION_MOVE:
		// Log.d("Move", "move detected");
		// break;

		// case MotionEvent.ACTION_POINTER_UP:
		// int id2 = action >> MotionEvent.ACTION_POINTER_ID_SHIFT;
		// Log.v("Pointer UP", "Release id = " + id2);
		// break;
		// case MotionEvent.ACTION_UP:
		// Log.v("Up", "clear");
		// break;
		}
		return true;
	}

}
