package gid.com.cardreaderapp;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.File;

import gid.com.cardreaderapp.Common.Activity.BaseActivity;
import gid.com.gidvisionlib.Common.Application.Application;
import gid.com.gidvisionlib.ViewActivity.GIDLibVisionMainActivity;

public class MainActivity extends BaseActivity implements View.OnClickListener {

    private static final int GO_TO_OCR_ACTIVITY_KTP = 1;
    private static final int GO_TO_OCR_ACTIVITY_NPWP = 2;
    private static final int GO_TO_OCR_ACTIVITY_DEBITCARD = 3;
    private static final String TAG = "MainActivity";

    private Button btnOCRNpwp;
    private Button btnKTP;
    private Button btnDebitCard;
    private ImageView ivOcrResult;
    private TextView tvOcrResultText;
    private Intent i;

    @Override
    protected void bindView() {
        this.btnOCRNpwp = this.findViewById(R.id.btnGetNPWP);
        this.btnDebitCard = this.findViewById(R.id.btnGetDebitCard);
        this.btnKTP = this.findViewById(R.id.btnGetKTP);
        this.ivOcrResult = this.findViewById(R.id.ivOcrPreview);
        this.tvOcrResultText = this.findViewById(R.id.tvOcrResultText);
    }

    @Override
    protected void registerEvent() {
        this.btnOCRNpwp.setOnClickListener(this);
        this.btnKTP.setOnClickListener(this);
        this.btnDebitCard.setOnClickListener(this);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.onCreateSetContentView(R.layout.activity_main);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }


    @Override
    public void goToNewIntent(int id) {
        if(id == GO_TO_OCR_ACTIVITY_KTP){
            this.i = new Intent(this,GIDLibVisionMainActivity.class);
            this.i.putExtra("OCR_MODE","KTP");
            this.startActivityForResult(i,GO_TO_OCR_ACTIVITY_KTP);
        }else if(id == GO_TO_OCR_ACTIVITY_DEBITCARD){
            this.i = new Intent(this,GIDLibVisionMainActivity.class);
            this.i.putExtra("OCR_MODE","DEBIT_CARD");
            this.startActivityForResult(i,GO_TO_OCR_ACTIVITY_DEBITCARD);
        }else if(id == GO_TO_OCR_ACTIVITY_NPWP){
            this.i = new Intent(this,GIDLibVisionMainActivity.class);
            this.i.putExtra("OCR_MODE","NPWP");
            this.startActivityForResult(i,GO_TO_OCR_ACTIVITY_NPWP);
        }else{
            Log.d(TAG, "intent not found: ");
        }
    }

    @Override
    public void onClick(View v) {
        if (v.equals(this.btnDebitCard)){
            //get Debit card Text
            this.goToNewIntent(GO_TO_OCR_ACTIVITY_DEBITCARD);
        }else if(v.equals(this.btnKTP)){
            //get NPWP Text
            this.goToNewIntent(GO_TO_OCR_ACTIVITY_KTP);
        }else if(v.equals(this.btnOCRNpwp)){
            //get NPWP text
            this.goToNewIntent(GO_TO_OCR_ACTIVITY_NPWP);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == GO_TO_OCR_ACTIVITY_NPWP || requestCode == GO_TO_OCR_ACTIVITY_KTP || requestCode == GO_TO_OCR_ACTIVITY_DEBITCARD){
            if(data.getStringExtra(GIDLibVisionMainActivity.ACTIVITY_STATUS).equals(Application.ACTIVITY_STATUS_OK)){
                String textResult = data.getStringExtra(GIDLibVisionMainActivity.OCR_CAPTURED_TEXT);
                String imagePath = data.getStringExtra(GIDLibVisionMainActivity.OCR_CAPTURED_IMG);
                Bitmap bitmapImage = BitmapFactory.decodeFile(imagePath);
                this.ivOcrResult.setImageBitmap(bitmapImage);
                this.tvOcrResultText.setText(textResult);
            }else if(data.getStringExtra(GIDLibVisionMainActivity.ACTIVITY_STATUS).equals(Application.ACTIVITY_STATUS_CANCELED)){
                Log.d(TAG, "onActivityResult: Canceled");
            }else if(data.getStringExtra(GIDLibVisionMainActivity.ACTIVITY_STATUS).equals(Application.ACTIVITY_STATUS_FAILED)){
                Log.d(TAG, "onActivityResult: Failed");
            }
        }
    }
}
