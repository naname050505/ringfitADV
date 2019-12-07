package com.example.android.accelerometerplay;
import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.BitmapFactory.Options;
import android.graphics.drawable.AnimationDrawable;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Build;
import android.os.Bundle;
import android.os.PowerManager;
import android.os.PowerManager.WakeLock;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.Surface;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.content.Intent;

public class AccelerometerPlayActivity extends Activity {

    private SimulationView mSimulationView;
    private SensorManager mSensorManager;
    private PowerManager mPowerManager;
    private WindowManager mWindowManager;
    private Display mDisplay;
    private WakeLock mWakeLock;
    private TextView m_val_x;
    private TextView m_val_y;
    private TextView m_val_z;
    private TextView s_val_x;
    private TextView s_val_y;
    private TextView s_val_z;
    private TextView state_info;
    private TextView swing_info;
    public float mSensorX;
    public float mSensorY;
    public float mSensorZ;
    public TextView squat_count_info;
    public TextView swing_count_info;
    public boolean change_flg;
    public int down_counter = 0;
    public int left_counter = 0;
    public int right_counter = 0;
    public int counter = 0;
    public int squat_counter = 0;
    public int swing_counter = 0;
    public boolean up_flg = false;
    public ProgressBar bar;
    public String msg = "1";
    private AnimationDrawable damage_animation;
    private ImageView damageView;

    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent intent = getIntent();
        msg = intent.getStringExtra("act");
        final Intent sub_intent = new Intent(getApplication(), SubActivity.class);


        mSensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
        mPowerManager = (PowerManager) getSystemService(POWER_SERVICE);
        mWindowManager = (WindowManager) getSystemService(WINDOW_SERVICE);
        mDisplay = mWindowManager.getDefaultDisplay();
        mWakeLock = mPowerManager.newWakeLock(PowerManager.SCREEN_BRIGHT_WAKE_LOCK, getClass()
                .getName());

        mSimulationView = new SimulationView(this);
        mSimulationView.setBackgroundResource(R.drawable.battlebg012b);
        setContentView(R.layout.main);
        //setContentView(mSimulationView); //never use!!
        final ImageView imageView2 = (ImageView)this.findViewById(R.id.image_view_2);
        imageView2.setImageResource(R.drawable.enemy015a);

        damageView = findViewById(R.id.damage_view);
        damageView.setBackgroundResource(R.drawable.damage_animation);
        damage_animation = (AnimationDrawable)damageView.getBackground();

        damageView.setVisibility(View.VISIBLE);
        if(damage_animation.isRunning()){
            damage_animation.stop();
        }
        damage_animation.start();

        m_val_x = (TextView)this.findViewById(R.id.m_val_x);
        m_val_y = (TextView)this.findViewById(R.id.m_val_y);
        m_val_z = (TextView)this.findViewById(R.id.m_val_z);
        s_val_x = (TextView)this.findViewById(R.id.s_val_x);
        s_val_y = (TextView)this.findViewById(R.id.s_val_y);
        s_val_z = (TextView)this.findViewById(R.id.s_val_z);
        squat_count_info = this.findViewById(R.id.squat_count_info);
        swing_count_info = this.findViewById(R.id.swing_count_info);
        state_info = this.findViewById(R.id.state_info);

        Button button = this.findViewById(R.id.button);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            counter +=100;
            s_val_x.setText(String.valueOf(mSensorX));
            s_val_y.setText(String.valueOf(mSensorY));
            s_val_z.setText(String.valueOf(mSensorZ));
            up_flg = false;
            down_counter = 0;
            startActivity(sub_intent);
            }
        });
        bar = (ProgressBar)findViewById(R.id.progressBar1);
        bar.setMax(100);
    }

    @Override
    protected void onResume() {
        super.onResume();
        mWakeLock.acquire();
        mSimulationView.startSimulation();
    }

    @Override
    protected void onPause() {
        super.onPause();
        // Stop the simulation
        mSimulationView.stopSimulation();
        // and release our wake-lock
        mWakeLock.release();
    }


    class SimulationView extends FrameLayout implements SensorEventListener {
        // diameter of the balls in meters
        private static final float sBallDiameter = 0.004f;
        private static final float sBallDiameter2 = sBallDiameter * sBallDiameter;

        private final int mDstWidth;
        private final int mDstHeight;

        private Sensor mAccelerometer;
        private long mLastT;

        private float mXDpi;
        private float mYDpi;
        private float mMetersToPixelsX;
        private float mMetersToPixelsY;
        private float mXOrigin;
        private float mYOrigin;
        private float mHorizontalBound;
        private float mVerticalBound;
        private final ParticleSystem mParticleSystem;


        class Particle extends View {
            private float mPosX = (float) Math.random();
            private float mPosY = (float) Math.random();
            private float mVelX;
            private float mVelY;


            public Particle(Context context) {
                super(context);
            }
            public Particle(Context context, AttributeSet attrs) {
                super(context, attrs);
            }
            public Particle(Context context, AttributeSet attrs, int defStyleAttr) {
                super(context, attrs, defStyleAttr);
            }

            @TargetApi(Build.VERSION_CODES.LOLLIPOP)
            public Particle(Context context, AttributeSet attrs, int defStyleAttr,
                            int defStyleRes) {
                super(context, attrs, defStyleAttr, defStyleRes);
            }

            public void computePhysics(float sx, float sy, float dT) {
                float ax = -sx/50;
                float ay = -sy/50;
                if (Math.abs(ax) < 0.01) {
                    ax = 0;
                }
                if (Math.abs(ay) < 0.01) {
                    ay = 0;
                }
                mPosX += mVelX * dT + ax * dT * dT / 2;
                mPosY += mVelY * dT + ay * dT * dT / 2;

                mVelX += ax * dT;
                mVelY += ay * dT;
            }

            public void resolveCollisionWithBounds() {
                final float xmax = mHorizontalBound;
                final float ymax = mVerticalBound;
                final float x = mPosX;
                final float y = mPosY;
                if (x > xmax) {
                    mPosX = xmax;
                    mVelX = 0;
                } else if (x < -xmax) {
                    mPosX = -xmax;
                    mVelX = 0;
                }
                if (y > ymax) {
                    mPosY = ymax;
                    mVelY = 0;
                } else if (y < -ymax) {
                    mPosY = -ymax;
                    mVelY = 0;
                }
            }
        }

        class ParticleSystem {
            static final int NUM_PARTICLES = 1;
            private Particle mBalls[] = new Particle[NUM_PARTICLES];

            ParticleSystem() {
                for (int i = 0; i < mBalls.length; i++) {
                    mBalls[i] = new Particle(getContext());
                    mBalls[i].setBackgroundResource(R.drawable.enemy015a);
                    mBalls[i].setLayerType(LAYER_TYPE_HARDWARE, null);
                    addView(mBalls[i], new ViewGroup.LayoutParams(mDstWidth, mDstHeight));
                }
            }

            private void updatePositions(float sx, float sy, long timestamp) {
                final long t = timestamp;
                if (mLastT != 0) {
                    final float dT = (float) (t - mLastT) / 1000.f;
                    final int count = mBalls.length;
                    for (int i = 0; i < count; i++) {
                        Particle ball = mBalls[i];
                        ball.computePhysics(sx, sy, dT);
                    }
                }
                mLastT = t;
            }

            public void update(float sx, float sy, long now) {
                // update the system's positions
                updatePositions(sx, sy, now);

                // We do no more than a limited number of iterations
                final int NUM_MAX_ITERATIONS = 10;

                boolean more = true;
                final int count = mBalls.length;
                for (int k = 0; k < NUM_MAX_ITERATIONS && more; k++) {
                    more = false;
                    for (int i = 0; i < count; i++) {
                        Particle curr = mBalls[i];
                        for (int j = i + 1; j < count; j++) {
                            Particle ball = mBalls[j];
                            float dx = ball.mPosX - curr.mPosX;
                            float dy = ball.mPosY - curr.mPosY;
                            float dd = dx * dx + dy * dy;
                            // Check for collisions
                            if (dd <= sBallDiameter2) {
                                /*
                                 * add a little bit of entropy, after nothing is
                                 * perfect in the universe.
                                 */
                                dx += ((float) Math.random() - 0.5f) * 0.0001f;
                                dy += ((float) Math.random() - 0.5f) * 0.0001f;
                                dd = dx * dx + dy * dy;
                                // simulate the spring
                                final float d = (float) Math.sqrt(dd);
                                final float c = (0.5f * (sBallDiameter - d)) / d;
                                final float effectX = dx * c;
                                final float effectY = dy * c;
                                curr.mPosX -= effectX;
                                curr.mPosY -= effectY;
                                ball.mPosX += effectX;
                                ball.mPosY += effectY;
                                more = true;
                            }
                        }
                        curr.resolveCollisionWithBounds();
                    }
                }


            }

            public int getParticleCount() {
                return mBalls.length;
            }
            public float getPosX(int i) {
                return mBalls[i].mPosX;
            }
            public float getPosY(int i) {
                return mBalls[i].mPosY;
            }
        }

        public void startSimulation() {
            mSensorManager.registerListener(this, mAccelerometer, 10000000);
        }
        public void stopSimulation() {
            mSensorManager.unregisterListener(this);
        }

        public SimulationView(Context context) {
            super(context);
            mAccelerometer = mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);

            DisplayMetrics metrics = new DisplayMetrics();
            getWindowManager().getDefaultDisplay().getMetrics(metrics);
            mXDpi = metrics.xdpi;
            mYDpi = metrics.ydpi;
            mMetersToPixelsX = mXDpi / 0.0254f;
            mMetersToPixelsY = mYDpi / 0.0254f;

            // rescale the ball so it's about 0.5 cm on screen
            mDstWidth = (int) (sBallDiameter * mMetersToPixelsX + 0.5f);
            mDstHeight = (int) (sBallDiameter * mMetersToPixelsY + 0.5f);
            mParticleSystem = new ParticleSystem();

            Options opts = new Options();
            opts.inDither = true;
            opts.inPreferredConfig = Bitmap.Config.RGB_565;
        }

        @Override
        protected void onSizeChanged(int w, int h, int oldw, int oldh) {
            mXOrigin = (w - mDstWidth) * 0.5f;
            mYOrigin = (h - mDstHeight) * 0.5f;
            mHorizontalBound = ((w / mMetersToPixelsX - sBallDiameter) * 0.5f);
            mVerticalBound = ((h / mMetersToPixelsY - sBallDiameter) * 0.5f);
        }

        @Override
        public void onSensorChanged(SensorEvent event) {
            if (event.sensor.getType() != Sensor.TYPE_ACCELEROMETER)
                return;
            switch (mDisplay.getRotation()) {
                case Surface.ROTATION_0:
                    mSensorX = event.values[0];
                    mSensorY = event.values[1];
                    mSensorZ = event.values[2];
                    break;
                case Surface.ROTATION_90:
                    mSensorX = -event.values[1];
                    mSensorY = event.values[0];
                    mSensorZ = event.values[2];
                    break;
                case Surface.ROTATION_180:
                    mSensorX = -event.values[0];
                    mSensorY = -event.values[1];
                    mSensorZ = event.values[2];
                    break;
                case Surface.ROTATION_270:
                    mSensorX = event.values[1];
                    mSensorY = -event.values[0];
                    mSensorZ = event.values[2];
                    break;
            }
            m_val_x.setText(String.valueOf(mSensorX));
            m_val_y.setText(String.valueOf(mSensorY));
            m_val_z.setText(String.valueOf(mSensorZ));
            switch(Integer.valueOf((msg))) {
                case 1:
                    if (Float.parseFloat(s_val_y.getText().toString()) == 0) {
                        if (Math.abs(mSensorY -
                                Float.parseFloat(s_val_y.getText().toString())) > 1) {
                            swing_counter++;
                        }
                    }
                case 2:
                     if (Float.parseFloat(s_val_z.getText().toString()) != 0 && !up_flg) {
                         if (Math.abs(Math.abs(mSensorZ) -
                                 Math.abs(Float.parseFloat(s_val_z.getText().toString()))) > 2) {
                             down_counter += 1;
                         }
                     }
                     if (down_counter>9){
                     up_flg = true;
                     }
                     if (up_flg) {
                         if (Math.abs(Math.abs(mSensorZ) -
                                 Math.abs(Float.parseFloat(s_val_z.getText().toString()))) < 1) {
                             down_counter = down_counter - 3;
                             if (down_counter < 0) {
                                 down_counter = 0;
                                 squat_counter = Integer.parseInt(squat_count_info.getText().toString()) + 1;
                                 up_flg = false;
                             }
                         }
                     }
                case 3:
                    up_flg = true;
                    break;
            }
            state_info.setText(String.valueOf(up_flg));
            squat_count_info.setText(String.valueOf(squat_counter));
            swing_count_info.setText(String.valueOf(swing_counter));

            damageView.setVisibility(View.VISIBLE);
            if(damage_animation.isRunning()){
                damage_animation.stop();
            }
            damage_animation.start();
            bar.setProgress(100-(10*squat_counter));
            onWindowFocusChanged(true);
            int hitpoint = 100 - (10*squat_counter);
            bar.setProgress(hitpoint);
            if (hitpoint <= 0) {
                // byouga site
                damageView.setVisibility(View.VISIBLE);
                if(damage_animation.isRunning()){
                    damage_animation.stop();
                }
                damage_animation.start();
                //final Intent sub_intent = new Intent(getApplication(), SubActivity.class);
                //startActivity((sub_intent));
            }
        }

        @Override
        protected void onDraw(Canvas canvas) {
            final ParticleSystem particleSystem = mParticleSystem;
            final long now = System.currentTimeMillis();
            final float sx = mSensorX;
            final float sy = mSensorY;

            particleSystem.update(sx, sy, now);

            final float xc = mXOrigin;
            final float yc = mYOrigin;
            final float xs = mMetersToPixelsX;
            final float ys = mMetersToPixelsY;
            final int count = particleSystem.getParticleCount();
            for (int i = 0; i < count; i++) {
                final float x = xc + particleSystem.getPosX(i) * xs;
                final float y = yc - particleSystem.getPosY(i) * ys;
                particleSystem.mBalls[i].setTranslationX(x);
                particleSystem.mBalls[i].setTranslationY(y);
            }
            invalidate();
            onWindowFocusChanged(true);

        }

        @Override
        public void onWindowFocusChanged(boolean hasFocus) {
            super.onWindowFocusChanged(hasFocus);
        }
        @Override
        public void onAccuracyChanged(Sensor sensor, int accuracy) {
        }

    }
}
