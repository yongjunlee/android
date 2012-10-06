package com.samsung.sdraw.example3;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.LightingColorFilter;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.LayerDrawable;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;

import com.mars.sketchOfSir.R;
import com.samsung.sdraw.CanvasView;
import com.samsung.sdraw.PenSettingInfo;

public class SPen_Example_PenSettingInfoActivity extends Activity {
    private CanvasView mCanvasView;
    private CanvasView mCanvasPenPreview;
    private PenSettingInfo mPenSettingInfo;
    private Button mPenBtn;
    private Button mEraserBtn;
    private Button mUndoBtn;
    private Button mRedoBtn;
    private ImageButton mPenCloseBtn;
    private ImageButton mEraserCloseBtn;
    private Button mEraserClearAll;
    private View mPenSetting;
    private View mEraserSetting;
    private ImageButton mExpandBtn;
    private ColorPickerView mColorPicker;
    private LinearLayout mPallet;
    private LinearLayout mOpacity;
    private ImageView mStrokeCircle;
    private ImageView mEraserCircle;
    private SeekBar mPenSize;
    private SeekBar mPenAlpha;
    private SeekBar mEraserSize;
    private ColorSelectView mColorSelect;
    private ImageButton[] mPen = new ImageButton[4];
    
    //yongjunlee add
    

    private int mButtonTextNormalColor;

    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        mCanvasView = (CanvasView) findViewById(R.id.canvas_view);
        mCanvasView.setOnHistoryChangeListener(spriteChangeListener);
        mPenSettingInfo = mCanvasView.getPenSettingInfo();
        mPenSettingInfo.setOnSettingInfoChangedListener(mOnSettingInfoChangedListener);

        mPenBtn = (Button) findViewById(R.id.penBtn);
        mPenBtn.setOnClickListener(mButtonClickListener);
        mButtonTextNormalColor = mPenBtn.getTextColors().getDefaultColor();
        mPenBtn.setTextColor(Color.WHITE);

        mEraserBtn = (Button) findViewById(R.id.eraseBtn);
        mEraserBtn.setOnClickListener(mButtonClickListener);

        mUndoBtn = (Button) findViewById(R.id.undoBtn);
        mUndoBtn.setOnClickListener(undoNredoBtnClickListener);
        mRedoBtn = (Button) findViewById(R.id.redoBtn);
        mRedoBtn.setOnClickListener(undoNredoBtnClickListener);

        mUndoBtn.setEnabled(mCanvasView.isUndoable());
        mRedoBtn.setEnabled(mCanvasView.isRedoable());

        mOpacity = (LinearLayout) findViewById(R.id.alphalayout);
        
        //yongjunlee add
       

        initPenSetting();
    }

    private float mDipUnit = 0;

    private int getPixel(float dip) {
        if (mDipUnit == 0) {
            initDIPUnit();
        }
        return (int) (mDipUnit * dip);
    }

    private void initDIPUnit() {
        DisplayMetrics outMetrics = new DisplayMetrics();

        getWindowManager().getDefaultDisplay().getMetrics(outMetrics);
        mDipUnit = outMetrics.density;
    }

    void initPenSetting() {
        mPenSetting = findViewById(R.id.pen_setting);
        mPallet = (LinearLayout) mPenSetting.findViewById(R.id.bg);
        mCanvasPenPreview = (CanvasView) mPenSetting.findViewById(R.id.strokepreview);
//        mCanvasPenPreview.setPadding(-getPixel(6f), -getPixel(4f), 0, 0);
        mPen[0] = (ImageButton) mPenSetting.findViewById(R.id.pentype_1);
        mPen[0].setOnClickListener(mPenTypeClickListener);
        mPen[1] = (ImageButton) mPenSetting.findViewById(R.id.pentype_2);
        mPen[1].setOnClickListener(mPenTypeClickListener);
        mPen[2] = (ImageButton) mPenSetting.findViewById(R.id.pentype_3);
        mPen[2].setOnClickListener(mPenTypeClickListener);
        mPen[3] = (ImageButton) mPenSetting.findViewById(R.id.pentype_4);
        mPen[3].setOnClickListener(mPenTypeClickListener);
        mPen[0].setSelected(true);

        mPenSize = (SeekBar) mPenSetting.findViewById(R.id.pen_size_seekbar);
        mPenSize.setOnSeekBarChangeListener(mPenSizeChangeListener);
        mPenSize.setMax(PenSettingInfo.MAX_PEN_WIDTH);
        mPenSize.setProgress(10);
        mPenAlpha = (SeekBar) mPenSetting.findViewById(R.id.opa_thickness);
        mPenAlpha.setOnSeekBarChangeListener(mPenAlphaChangeListener);
        mPenAlpha.setMax(255);
        mPenAlpha.setProgress(255);
        mPenAlpha.setEnabled(false);

        mColorSelect = (ColorSelectView) mPenSetting.findViewById(R.id.colorpicker);
        mColorSelect.setInitialValue(mOnColorSelectChangedListener, 0xFF000000 | mPenSettingInfo.getPenColor());
        mColorPicker = (ColorPickerView) mPenSetting.findViewById(R.id.gradience);
        mColorPicker.setOnColorChangeListener(mOnColorPickerChangedListener);

        mEraserSetting = findViewById(R.id.eraser_setting);
        mEraserSize = (SeekBar) mEraserSetting.findViewById(R.id.eraser_size_seekbar);
        mEraserSize.setOnSeekBarChangeListener(mEraserSizeChangeListener);
        mEraserSize.setMax(PenSettingInfo.MAX_PEN_WIDTH);
        mEraserSize.setProgress(40);

        mPenCloseBtn = (ImageButton) mPenSetting.findViewById(R.id.btnClosePenSetting);
        mPenCloseBtn.setOnClickListener(mCloseBtnClickListener);
        mEraserCloseBtn = (ImageButton) mEraserSetting.findViewById(R.id.btnCloseEraseSetting);
        mEraserCloseBtn.setOnClickListener(mCloseBtnClickListener);
        mEraserClearAll = (Button) findViewById(R.id.clear_all);
        mEraserClearAll.setOnClickListener(mClearAllListener);
        //
        mPenSetting.setVisibility(View.GONE);
        mEraserSetting.setVisibility(View.GONE);

        mExpandBtn = (ImageButton) mPenSetting.findViewById(R.id.expand);
        mExpandBtn.setOnClickListener(expandButtonOnClickListener);

        mStrokeCircle = (ImageView) mPenSetting.findViewById(R.id.penSizeCircle);
        mEraserCircle = (ImageView) mEraserSetting.findViewById(R.id.erase_circle);

        if (mPenSettingInfo != null) {
            drawOpacityCircle(mPenSettingInfo.getPenColor(), mPenSettingInfo.getPenAlpha());
            drawSizeCircle(mPenSettingInfo.getPenColor(), mPenSettingInfo.getPenWidth());
            drawEraserCircle(mPenSettingInfo.getEraserWidth());
            drawPenSeekbarColor(mPenSettingInfo.getPenColor(), mPenSettingInfo.getPenAlpha());
        }
    }

    private void drawPenSeekbarColor(int color, int alpha) {
        int alphaColor = (alpha << 24) & 0xff000000 | (color & 0x00ffffff);

        Drawable sizeDrawable = ((LayerDrawable) mPenSize.getProgressDrawable()).findDrawableByLayerId(android.R.id.progress);
        sizeDrawable.setColorFilter(0xFF000000 | color, android.graphics.PorterDuff.Mode.DARKEN);

        Drawable alphaDrawable = ((LayerDrawable) mPenAlpha.getProgressDrawable()).findDrawableByLayerId(android.R.id.progress);
        alphaDrawable.setColorFilter(alphaColor, android.graphics.PorterDuff.Mode.DARKEN);
    }

    private void drawOpacityCircle(int color, int alpha) {
        int alphaColor = 0xFF000000 | color;
        ImageView opapreview = (ImageView) findViewById(R.id.opa);
        opapreview.getBackground().setColorFilter(new LightingColorFilter(0, alphaColor));
        opapreview.getBackground().setAlpha(alpha);
    }

    private Bitmap colorCircle;
    private Bitmap eraserCircle;
    private static final int PREVIEW_R = 70;

    private void drawSizeCircle(int color, int width) {
        if (mStrokeCircle == null) {
            return;
        }
        if (colorCircle == null) {
            colorCircle = Bitmap.createBitmap(PREVIEW_R, PREVIEW_R, Config.ARGB_8888);
        }

        colorCircle.eraseColor(Color.TRANSPARENT);
        float size = (float) PREVIEW_R * ((float) mPenSettingInfo.getPenWidth() / (float) PenSettingInfo.MAX_PEN_WIDTH);
        Canvas canvas = new Canvas(colorCircle);
        Paint p = new Paint();
        p.setAlpha(255);

        size *= 0.7;

        int curColor = color;
        curColor |= 0xff000000;
        p.setColor(curColor);
        p.setAntiAlias(true);

        if (size < 1f) {
            size = 1f;
        }

        canvas.drawCircle(PREVIEW_R / 2.0f - 0.5f, PREVIEW_R / 2.0f + 0.5f, size / 2.0f, p);

        mStrokeCircle.setImageBitmap(colorCircle);
    }

    private void drawEraserCircle(int width) {
        if (mEraserCircle == null) {
            return;
        }
        if (eraserCircle == null) {
            eraserCircle = Bitmap.createBitmap(PREVIEW_R, PREVIEW_R, Config.ARGB_8888);
        }

        eraserCircle.eraseColor(Color.TRANSPARENT);
        float size = (float) PREVIEW_R * ((float) mPenSettingInfo.getEraserWidth() / (float) PenSettingInfo.MAX_PEN_WIDTH);
        Canvas canvas = new Canvas(eraserCircle);
        Paint p = new Paint();
        p.setAlpha(255);

        size *= 0.7;

        p.setColor(0xFFFFFFFF);
        p.setAntiAlias(true);

        if (size < 1f) {
            size = 1f;
        }

        canvas.drawCircle(PREVIEW_R / 2.0f - 0.5f, PREVIEW_R / 2.0f + 0.5f, size / 2.0f, p);

        mEraserCircle.setImageBitmap(eraserCircle);
    }

    private OnClickListener expandButtonOnClickListener = new OnClickListener() {
        @Override
        public void onClick(View v) {
            if (mColorPicker.getVisibility() == View.GONE) {
                expand(true);
                v.setSelected(false);
            } else {
                expand(false);
                v.setSelected(true);
            }
        }
    };

    private OnClickListener undoNredoBtnClickListener = new OnClickListener() {
        @Override
        public void onClick(View v) {
            if (v == mUndoBtn) {
                mCanvasView.undo();
            } else if (v == mRedoBtn) {
                mCanvasView.redo();
            }
            mUndoBtn.setEnabled(mCanvasView.isUndoable());
            mRedoBtn.setEnabled(mCanvasView.isRedoable());
        }
    };

    protected void expand(boolean isExpand) {
        if (isExpand) {
            mPallet.setBackgroundDrawable(getResources().getDrawable(R.drawable.drawingpad_bg_2));
            mExpandBtn.setImageDrawable(getResources().getDrawable(R.drawable.expand_icon_02));
            mOpacity.setVisibility(View.VISIBLE);
            mColorPicker.setVisibility(View.VISIBLE);
        } else {
            mPallet.setBackgroundDrawable(getResources().getDrawable(R.drawable.drawingpad_bg));
            mExpandBtn.setImageDrawable(getResources().getDrawable(R.drawable.expand_icon_01));
            mOpacity.setVisibility(View.GONE);
            mColorPicker.setVisibility(View.GONE);
        }
    }

    OnClickListener mButtonClickListener = new OnClickListener() {

        @Override
        public void onClick(View v) {
            if (v == mPenBtn) {
                mEraserSetting.setVisibility(View.GONE);
                if (mPenSetting.getVisibility() == View.VISIBLE) {
                    mPenSetting.setVisibility(View.GONE);
                    mCanvasPenPreview.clearAll(true);
                } else {
                    mPenSetting.setVisibility(View.VISIBLE);
                }
                mCanvasView.changeModeTo(CanvasView.PEN_MODE);
                mPenBtn.setSelected(true);
                mPenBtn.setTextColor(Color.WHITE);
                mEraserBtn.setSelected(false);
                mEraserBtn.setTextColor(mButtonTextNormalColor);
                
            } else if (v == mEraserBtn) {
                mPenSetting.setVisibility(View.GONE);
                mCanvasPenPreview.clearAll(true);
                if (mEraserSetting.getVisibility() == View.VISIBLE) {
                    mEraserSetting.setVisibility(View.GONE);
                } else {
                    mEraserSetting.setVisibility(View.VISIBLE);
                }
                mCanvasView.changeModeTo(CanvasView.ERASER_MODE);
                mEraserBtn.setSelected(true);
                mEraserBtn.setTextColor(Color.WHITE);
                mPenBtn.setSelected(false);
                mPenBtn.setTextColor(mButtonTextNormalColor);
            }
        }
    };

    OnClickListener mPenTypeClickListener = new OnClickListener() {

        @Override
        public void onClick(View v) {
            for (int i = 0; i < mPen.length; i++) {
                mPen[i].setSelected(false);
            }
            if (v.getId() == R.id.pentype_1) {
                mPenSettingInfo.setPenType(PenSettingInfo.PEN_TYPE_SOLID);
            } else if (v.getId() == R.id.pentype_2) {
                mPenSettingInfo.setPenType(PenSettingInfo.PEN_TYPE_BRUSH);
            } else if (v.getId() == R.id.pentype_3) {
                mPenSettingInfo.setPenType(PenSettingInfo.PEN_TYPE_PENCIL);
            } else if (v.getId() == R.id.pentype_4) {
                mPenSettingInfo.setPenType(PenSettingInfo.PEN_TYPE_MARKER);
            }
            v.setSelected(true);
        }
    };

    OnSeekBarChangeListener mPenSizeChangeListener = new OnSeekBarChangeListener() {

        @Override
        public void onStopTrackingTouch(SeekBar seekBar) {
        }

        @Override
        public void onStartTrackingTouch(SeekBar seekBar) {
        }

        @Override
        public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
            mPenSettingInfo.setPenWidth(progress);
            
        }
    };

    OnSeekBarChangeListener mPenAlphaChangeListener = new OnSeekBarChangeListener() {

        @Override
        public void onStopTrackingTouch(SeekBar seekBar) {
        }

        @Override
        public void onStartTrackingTouch(SeekBar seekBar) {
        }

        @Override
        public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
            mPenSettingInfo.setPenAlpha(progress);
        }
    };

    ColorSelectView.OnColorChangedListener mOnColorSelectChangedListener = new ColorSelectView.OnColorChangedListener() {

        @Override
        public void colorChanged(int color) {
            mPenSettingInfo.setPenColor(color);
        }
    };

    ColorPickerView.onColorChangedListener mOnColorPickerChangedListener = new ColorPickerView.onColorChangedListener() {

        @Override
        public void onColorChanged(int color, int x, int y) {
            mPenSettingInfo.setPenColor(color);
            if (mColorSelect != null) {
                mColorSelect.setColor(((mPenSettingInfo.getPenAlpha() << 24) & 0xFF000000) | mPenSettingInfo.getPenColor());
            }
        }
    };

    OnSeekBarChangeListener mEraserSizeChangeListener = new OnSeekBarChangeListener() {

        @Override
        public void onStopTrackingTouch(SeekBar seekBar) {
        }

        @Override
        public void onStartTrackingTouch(SeekBar seekBar) {
        }

        @Override
        public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
            mPenSettingInfo.setEraserWidth(progress);
        }
    };

    OnClickListener mCloseBtnClickListener = new OnClickListener() {

        @Override
        public void onClick(View v) {
            if (v == mPenCloseBtn) {
                mPenSetting.setVisibility(View.GONE);
                mCanvasPenPreview.clearAll(true);
            } else if (v == mEraserCloseBtn) {
                mEraserSetting.setVisibility(View.GONE);
            }
        }
    };

    OnClickListener mClearAllListener = new OnClickListener() {

        @Override
        public void onClick(View v) {
            // TODO Auto-generated method stub
            mCanvasView.clearAll(true);
        }
    };

    PenSettingInfo.OnSettingInfoChangedListener mOnSettingInfoChangedListener = new PenSettingInfo.OnSettingInfoChangedListener() {

        @Override
        public void onPenWidthChanged(int arg0, int arg1) {
            if (mCanvasPenPreview != null) {
                mCanvasPenPreview.getPenSettingInfo().setPenWidth(arg0, arg1);
            }
            if (mPenSettingInfo != null) {
                drawSizeCircle(mPenSettingInfo.getPenColor(), mPenSettingInfo.getPenWidth());
            }

        }

        @Override
        public void onPenTypeChanged(int arg0) {
            if (mCanvasPenPreview != null) {
                mCanvasPenPreview.getPenSettingInfo().setPenType(arg0);
            }
            if (mPenSettingInfo != null) {
                drawOpacityCircle(mPenSettingInfo.getPenColor(), mPenSettingInfo.getPenAlpha());
                drawSizeCircle(mPenSettingInfo.getPenColor(), mPenSettingInfo.getPenWidth());
                drawPenSeekbarColor(mPenSettingInfo.getPenColor(), mPenSettingInfo.getPenAlpha());
                mPenSize.setProgress(mPenSettingInfo.getPenWidth());
                mPenAlpha.setProgress(mPenSettingInfo.getPenAlpha());
            }
            if (arg0 == PenSettingInfo.PEN_TYPE_MARKER) {
                mPenAlpha.setEnabled(true);
            } else {
                mPenAlpha.setEnabled(false);
            }
        }

        @Override
        public void onPenColorChanged(int arg0, int arg1) {
            if (mCanvasPenPreview != null) {
                mCanvasPenPreview.getPenSettingInfo().setPenColor(arg0, arg1);
            }
            if (mPenSettingInfo != null) {
                drawOpacityCircle(mPenSettingInfo.getPenColor(), mPenSettingInfo.getPenAlpha());
                drawSizeCircle(mPenSettingInfo.getPenColor(), mPenSettingInfo.getPenWidth());
                drawPenSeekbarColor(mPenSettingInfo.getPenColor(), mPenSettingInfo.getPenAlpha());
            }
        }

        @Override
        public void onPenAlphaChanged(int arg0, int arg1) {
            if (mCanvasPenPreview != null) {
                mCanvasPenPreview.getPenSettingInfo().setPenAlpha(arg0, arg1);
            }
            if (mPenSettingInfo != null) {
                drawOpacityCircle(mPenSettingInfo.getPenColor(), mPenSettingInfo.getPenAlpha());
                drawPenSeekbarColor(mPenSettingInfo.getPenColor(), mPenSettingInfo.getPenAlpha());
            }
        }

        @Override
        public void onEraserWidthChanged(int arg0) {
            if (mPenSettingInfo != null) {
                drawEraserCircle(mPenSettingInfo.getEraserWidth());
            }
        }
    };

    private CanvasView.OnHistoryChangeListener spriteChangeListener = new CanvasView.OnHistoryChangeListener() {

        @Override
        public void onHistoryChanged(boolean undoable, boolean redoable) {
            mUndoBtn.setEnabled(undoable);
            mRedoBtn.setEnabled(redoable);
        }
    };

    @Override
    public void finish() {
        mStrokeCircle.setImageBitmap(null);
        mEraserCircle.setImageBitmap(null);

        if (colorCircle != null && !colorCircle.isRecycled()) {
            colorCircle.recycle();
        }
        if (eraserCircle != null && !eraserCircle.isRecycled()) {
            eraserCircle.recycle();
        }
        super.finish();
    }
}