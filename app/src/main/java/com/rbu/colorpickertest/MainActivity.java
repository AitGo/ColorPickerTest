package com.rbu.colorpickertest;

import android.support.v4.widget.TextViewCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import com.rbu.colorpickertest.ColorPickerDialog.OnColorChangedListener;
import com.rbu.colorpickertest.ColorPickerView2.OnColorChangedListenerD;

public class MainActivity extends AppCompatActivity {

    private com.rbu.colorpickertest.ColorPicckerView picker;
    private Button btnColorPicker;
//    private TextView                                 text;

//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_main);
//        picker = (ColorPicckerView) findViewById(R.id.picker);
//        text = (TextView) findViewById(R.id.text);
//        picker.setColorChangedListener(new ColorPicckerView.OnColorChangedListener() {
//            @Override
//            public void onColorChanged(int color) {
//
//                int red = (color & 0xff0000) >> 16;
//                int green = (color & 0x00ff00) >> 8;
//                int blue = (color & 0x0000ff);
//                text.setText(color + "\n" +
//                        "R:" + red +"\n" +
//                        "G:" + green + "\n" +
//                        "B:" + blue);
//            }
//        });

//    }



    /**
     * Tag of this class, more clear and convenient when you debugging
     */
    private final static String TAG="GET COLOR DEMO-->>";
    /**
     * the object of the ColorPickerDialog
     */
    private ColorPickerDialog ColorPicker=null;
    /**
     * btnGetColorDia   use to trigger the dialog of color when the button is click
     */
    private Button btnGetColorDia=null;
    /**
     * color Disk
     */
    ColorPickerView2 colorPickerDisk=null;
    /**
     * button of colorDisk
     */
    Button btnColorDisk=null;
    /**
     *
     */
    int intChange=1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        colorPickerDisk=new ColorPickerView2(this);

        //find the ID of View
        btnColorPicker = (Button) findViewById(R.id.btnColorPicker);
        picker = (ColorPicckerView) findViewById(R.id.picker);
        colorPickerDisk=(ColorPickerView2)findViewById(R.id.colorPickerDisk);
        btnColorDisk=(Button)findViewById(R.id.btnColorDisk);

        btnGetColorDia=(Button)findViewById(R.id.btnGetColorDia);


        picker.setColorChangedListener(new ColorPicckerView.OnColorChangedListener() {
                        @Override
                        public void onColorChanged(int color) {

                            int a = (color >>> 24);
                            int r = (color >>  16) & 0xFF;
                            int g = (color >>   8) & 0xFF;
                            int b = (color)        & 0xFF;
                            String rupStr=Integer.toHexString(r);
                            String gupStr=Integer.toHexString(g);
                            String bupStr=Integer.toHexString(b);
                            String colorUpStr=rupStr+gupStr+bupStr;    //十六进制的颜色字符串。
                            btnColorPicker.setText(
                                    ""+colorUpStr + " "+
                                    "R:" + r +" " +
                                    "G:" + g + " " +
                                    "B:" + b);
                            btnColorPicker.setBackgroundColor(color);
                        }
                    });

        btnGetColorDia.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                ColorPicker.show();
            }
        });

        //how to use ColorPickerDialog Class  ,the default color is blue ,here is Color.BLUE
        ColorPicker=new ColorPickerDialog(this, new OnColorChangedListener() {

            @Override
            public void colorChanged(int color) {
                // TODO Auto-generated method stub
                //change the background  of button when the color is changed .
                btnGetColorDia.setBackgroundColor(color);
            }
        }, Color.BLUE);

        colorPickerDisk.setOnColorChangedListennerD(new OnColorChangedListenerD() {

            @Override
            public void onColorChanged(int color, String hexStrColor) {
                // TODO Auto-generated method stub
                btnColorDisk.setBackgroundColor(color);
                btnColorDisk.setText("Color is "+hexStrColor);
            }
        });


        btnColorDisk.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                if(intChange%2==0)
                {
                    colorPickerDisk.setVisibility(View.INVISIBLE);

                }else
                {
                    colorPickerDisk.setVisibility(View.VISIBLE);
                    picker.setVisibility(View.INVISIBLE);
                }
                intChange++;
            }
        });

        btnColorPicker.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if(picker.isShown()) {
                    picker.setVisibility(View.INVISIBLE);
                    if(colorPickerDisk.isShown()) {
                        colorPickerDisk.setVisibility(View.INVISIBLE);
                    }
                }else {
                    picker.setVisibility(View.VISIBLE);
                    colorPickerDisk.setVisibility(View.INVISIBLE);
                }
            }
        });
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.activity_main, menu);
        return true;
    }
}
