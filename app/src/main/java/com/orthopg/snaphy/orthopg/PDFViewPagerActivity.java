package com.orthopg.snaphy.orthopg;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.os.Environment;
import android.util.DisplayMetrics;

import es.voghdev.pdfviewpager.library.PDFViewPager;
import es.voghdev.pdfviewpager.library.adapter.BasePDFPagerAdapter;
import es.voghdev.pdfviewpager.library.adapter.PDFPagerAdapter;
import es.voghdev.pdfviewpager.library.adapter.PdfScale;

/**
 * Created by nikita on 18/3/17.
 */

public class PDFViewPagerActivity extends Activity {

    PDFViewPager pdfViewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        /*setTitle(R.string.menu_sample9_txt);*/
        pdfViewPager = new PDFViewPager(this, Environment.getExternalStorageDirectory()+ "/OrthoPG/" + "dsample.pdf");
        setContentView(pdfViewPager);
        pdfViewPager.setAdapter(new PDFPagerAdapter.Builder(this)
                .setPdfPath(Environment.getExternalStorageDirectory() + "/OrthoPG/" + "dsample.pdf")
                .create()
        );
    }

    private PdfScale getPdfScale() {
        PdfScale scale = new PdfScale();
        scale.setScale(3.0f);
        scale.setCenterX(getScreenWidth(this) / 2);
        scale.setCenterY(0f);
        return scale;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        BasePDFPagerAdapter adapter = (BasePDFPagerAdapter) pdfViewPager.getAdapter();
        if (adapter != null) {
            adapter.close();
            adapter = null;
        }
    }

  /*  public static void open(Context context) {
        Intent i = new Intent(context, PDFWithScaleActivity.class);
        context.startActivity(i);
    }*/

    public int getScreenWidth(Context ctx) {
        int w = 0;
        if (ctx instanceof Activity) {
            DisplayMetrics displaymetrics = new DisplayMetrics();
            ((Activity) ctx).getWindowManager().getDefaultDisplay().getMetrics(displaymetrics);
            w = displaymetrics.widthPixels;
        }
        return w;
    }
}
