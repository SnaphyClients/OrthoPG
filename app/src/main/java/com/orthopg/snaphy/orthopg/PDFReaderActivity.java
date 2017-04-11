package com.orthopg.snaphy.orthopg;

import android.app.Activity;
import android.os.Bundle;
import android.os.Environment;
import android.os.PersistableBundle;
import android.view.View;

import com.androidsdk.snaphy.snaphyandroidsdk.presenter.Presenter;
import com.github.barteksc.pdfviewer.PDFView;

import java.io.File;

/**
 * Created by nikita on 20/3/17.
 */

public class PDFReaderActivity extends Activity {

    PDFView pdfView;
    File file;
    File outFile;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pdf_reader);
        pdfView = (PDFView) findViewById(R.id.pdfView);
        if(Presenter.getInstance().getModel(String.class, Constants.DOWNLOADED_BOOK_ID) != null) {
            String bookName = Presenter.getInstance().getModel(String.class, Constants.DOWNLOADED_BOOK_ID);
            file = new File(Environment.getExternalStorageDirectory() + "/OrthoPg/" + bookName+".pdf");
            outFile = new File(Environment.getExternalStorageDirectory() + "/OrthoPg/" + "d_" + bookName+".pdf");
            pdfView.fromFile(file)// all pages are displayed by default
                    .enableSwipe(true)
                    .swipeHorizontal(false)
                    .enableDoubletap(true)
                    .defaultPage(0)
                    .load();

            pdfView.setLayerType(View.LAYER_TYPE_HARDWARE, null);
            pdfView.setDrawingCacheEnabled(true);
            pdfView.enableRenderDuringScale(false);
            pdfView.loadPages();
            pdfView.getCurrentPage();
        }

}

    @Override
    protected void onPause() {
        super.onPause();
        file.delete();
    }
}
