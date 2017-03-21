package com.orthopg.snaphy.orthopg;

import android.app.Activity;
import android.os.Bundle;
import android.os.Environment;
import android.os.PersistableBundle;
import android.view.View;

import com.github.barteksc.pdfviewer.PDFView;

import java.io.File;

/**
 * Created by nikita on 20/3/17.
 */

public class PDFReaderActivity extends Activity {

    PDFView pdfView;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pdf_reader);
        pdfView = (PDFView) findViewById(R.id.pdfView);

        File file = new File(Environment.getExternalStorageDirectory() + "/OrthoPg/" + "dsample.pdf");
        pdfView.fromFile(file)// all pages are displayed by default
                .enableSwipe(true)
                .swipeHorizontal(true)
                .enableDoubletap(true)
                .defaultPage(0)

                .load();

        pdfView.setLayerType(View.LAYER_TYPE_HARDWARE,null);
        pdfView.setDrawingCacheEnabled(true);
        pdfView.enableRenderDuringScale(false);
}
}
