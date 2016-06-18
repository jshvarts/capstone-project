package com.jshvarts.flatstanley;

import android.content.ClipData;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.DragEvent;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class LobbyActivity extends AppCompatActivity {

    private static final String TAG = "LobbyActivity";

    @BindView(R.id.flatStanleyImage)
    protected ImageView flatStanleyImageView;

    @BindView(R.id.postcardImage)
    protected ImageView postcardImageView;

    @BindView(R.id.target_layout)
    protected FrameLayout targetLayout;

    @BindView(R.id.source_layout)
    protected FrameLayout sourceLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lobby);

        ButterKnife.bind(this);

        View.OnLongClickListener longClickListener = new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                ClipData clipData = ClipData.newPlainText("", "");
                View.DragShadowBuilder shadowBuilder = new View.DragShadowBuilder(v);
                v.startDrag(clipData, shadowBuilder, v, 0);
                v.setVisibility(View.INVISIBLE); // we are dragging the shadow
                return true;
            }
        };
        flatStanleyImageView.setOnLongClickListener(longClickListener);

        View.OnDragListener dragListener = new View.OnDragListener() {
            @Override
            public boolean onDrag(View v, DragEvent event) {
                switch(event.getAction())
                {
                    case DragEvent.ACTION_DRAG_STARTED:
                        Log.d(TAG, "Action is DragEvent.ACTION_DRAG_STARTED");

                    case DragEvent.ACTION_DRAG_ENTERED:
                        Log.d(TAG, "Action is DragEvent.ACTION_DRAG_ENTERED");

                    case DragEvent.ACTION_DRAG_EXITED:
                        Log.d(TAG, "Action is DragEvent.ACTION_DRAG_EXITED");

                    case DragEvent.ACTION_DRAG_LOCATION:
                        Log.d(TAG, "Action is DragEvent.ACTION_DRAG_LOCATION");

                    case DragEvent.ACTION_DRAG_ENDED:
                        Log.d(TAG, "Action is DragEvent.ACTION_DRAG_ENDED. DragEvent.getResult() " + event.getResult());
                        if (!event.getResult()) {
                            Log.d(TAG, "Drag was not successful.");
                            flatStanleyImageView.setVisibility(View.VISIBLE);
                        }
                        return true;

                    case DragEvent.ACTION_DROP:
                        Log.d(TAG, "Action is DragEvent.ACTION_DROP");

                        FrameLayout draggedImageParentViewLayout = (FrameLayout) flatStanleyImageView.getParent();
                        Log.d(TAG, "draggedImageParentViewLayout: " + draggedImageParentViewLayout.getId());
                        Log.d(TAG, "targetLayout: " + targetLayout.getId());

                        View view = (View) event.getLocalState();
                        float x = event.getX();
                        float y = event.getY();
                        view.setX(x-(view.getWidth()/2));
                        view.setY(y-(view.getWidth()/2));
                        if (draggedImageParentViewLayout != targetLayout) {
                            draggedImageParentViewLayout.removeView(flatStanleyImageView);
                            targetLayout.addView(flatStanleyImageView);
                        }
                        sourceLayout.invalidate();
                        targetLayout.invalidate();
                        return true;

                    default:
                        break;
                }
                return false;
            }
        };
        targetLayout.setOnDragListener(dragListener);
    }

    @OnClick(R.id.resetButton)
    protected void handleResetButtonClick() {
        Log.d(TAG, "Begin handleResetButtonClick");

        Intent intent = getIntent();
        finish();
        startActivity(intent);

        Log.d(TAG, "End handleResetButtonClick");
    }

    @OnClick(R.id.doneButton)
    protected void handleDoneButtonClick() {
        Log.d(TAG, "Begin handleDoneButtonClick");

        Bitmap attractionBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.attraction);
        Bitmap flatStanleyBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.reddit);

        Bitmap bitmapOverlay = Bitmap.createBitmap(attractionBitmap.getWidth(), attractionBitmap.getHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmapOverlay);
        canvas.drawBitmap(attractionBitmap, new Matrix(), null);
        canvas.drawBitmap(flatStanleyBitmap, new Matrix(), null);

        postcardImageView.setImageBitmap(bitmapOverlay);
        postcardImageView.setVisibility(View.VISIBLE);

        Log.d(TAG, "End handleDoneButtonClick");
    }
}