package fb.sio.ecp.fr.federatedbirds.app;

import android.content.Intent;
import android.database.Cursor;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v4.os.AsyncTaskCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.IOException;

import fb.sio.ecp.fr.federatedbirds.ApiClient;
import fb.sio.ecp.fr.federatedbirds.R;

/**
 * Created by charpi on 27/12/15.
 */
public class UploadAvatarActivity extends AppCompatActivity {

    private static int RESULT_LOAD_IMG = 1;
    private String imgDecodableString;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_uploadavatar);

        findViewById(R.id.browse_gallery).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent galleryIntent = new Intent(Intent.ACTION_PICK,
                        android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(galleryIntent, RESULT_LOAD_IMG);
            }
        });

        findViewById(R.id.upload_picture).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AsyncTaskCompat.executeParallel(
                        new UploadPictureTask(imgDecodableString)
                );
            }
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        try {

            if (requestCode == RESULT_LOAD_IMG && resultCode == RESULT_OK && null != data) {

                Uri selectedImage = data.getData();
                String[] filePathColumn = { MediaStore.Images.Media.DATA };

                Cursor cursor = getContentResolver().query(selectedImage, filePathColumn, null, null, null);
                cursor.moveToFirst();

                int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
                imgDecodableString = cursor.getString(columnIndex);
                cursor.close();
                ImageView imgView = (ImageView) findViewById(R.id.new_avatar);
                imgView.setImageBitmap(BitmapFactory.decodeFile(imgDecodableString));

            } else {
                Toast.makeText(this, R.string.select_file, Toast.LENGTH_LONG).show();
            }
        } catch (Exception e) {
            Toast.makeText(this, R.string.upload_failed, Toast.LENGTH_LONG).show();
        }
    }


    private class UploadPictureTask extends AsyncTask<Void, Void, String>{

        private String mImgString;

        public UploadPictureTask(String imgString){
            super();
            mImgString = imgString;
        }

        @Override
        protected String doInBackground(Void... params) {
            try {
                if (mImgString != null) {
                    return ApiClient.getInstance(getApplicationContext()).uploadAvatar(mImgString);
                } else {
                    //Toast.makeText(getApplicationContext(), R.string.select_file, Toast.LENGTH_SHORT).show();
                    return null;
                }

            } catch (IOException e) {
                Log.e(UploadAvatarActivity.class.getSimpleName(), "Upload failed", e);
                return null;
            }
        }

        @Override
        protected void onPostExecute(String token) {
            Toast.makeText(getApplicationContext(), R.string.file_uploaded, Toast.LENGTH_LONG).show();
            finish();
        }
    }
}
