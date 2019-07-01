package com.codepath.android.booksearch.activities;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.Nullable;
import android.support.v4.content.FileProvider;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.ShareActionProvider;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.codepath.android.booksearch.R;
import com.codepath.android.booksearch.models.Book;

import org.parceler.Parcels;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

public class BookDetailActivity extends AppCompatActivity {
    private ImageView ivBookCover;
    private TextView tvTitle, tvAuthor, tvPubYear;
    private Book book;
    private Toolbar toolbar;
    private ShareActionProvider miShareAction;
    private Intent shareIntent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book_detail);
        // Fetch views
        ivBookCover = (ImageView) findViewById(R.id.ivBookCover);
        tvTitle = (TextView) findViewById(R.id.tvTitle);
        tvAuthor = (TextView) findViewById(R.id.tvAuthor);
        tvPubYear = (TextView) findViewById(R.id.tvPubYear);

        // Extract book object from intent extras
        book = Parcels.unwrap(getIntent().getParcelableExtra(Book.class.getSimpleName()));

        // Use book object to populate data into views
        tvTitle.setText(book.getTitle());
        tvAuthor.setText(book.getAuthor());
        tvPubYear.setText(book.getPubYear());
        Glide.with(this)
             .load(book.getCoverUrl())
             .error(R.drawable.ic_nocover)
             .listener(new RequestListener<Drawable>() {
                 @Override
                 public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                     prepareShareIntent(((BitmapDrawable) resource).getBitmap());
                     attachShareIntentAction();
                     // Let Glide handle resource load
                     return false;
                 }

                 @Override
                 public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                      Log.e("BookDetailActivity", "Failed to load cover image", e);
                      return false;
                 }
             })
             .into(ivBookCover);

        // Change Toolbar title to book title
        toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle(book.getTitle());
        setSupportActionBar(toolbar);
    }

    public void prepareShareIntent(Bitmap bitmap) {
        // Fetch Bitmap Uri locally
        Uri bmpUri = getBitmapFromDrawable(bitmap);
        // Construct share intent as described above based on bitmap
        shareIntent = new Intent();
        shareIntent.setAction(Intent.ACTION_SEND);
        // TODO: Share book title with Intent
        shareIntent.putExtra(Intent.EXTRA_STREAM, bmpUri);
        shareIntent.setType("image/*");
    }

    public void attachShareIntentAction() {
        if (miShareAction != null && shareIntent != null) {
            miShareAction.setShareIntent(shareIntent);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_book_detail, menu);
        MenuItem item = menu.findItem(R.id.miShare);
        miShareAction = (ShareActionProvider) MenuItemCompat.getActionProvider(item);
        attachShareIntentAction();
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

    public Uri getBitmapFromDrawable(Bitmap bmp){

        // Store image to default external storage directory
        Uri bmpUri = null;
        try {
            // Use methods on Context to access package-specific directories on external storage.
            // This way, you don't need to request external read/write permission.
            // See https://youtu.be/5xVh-7ywKpE?t=25m25s
            File file =  new File(getExternalFilesDir(Environment.DIRECTORY_PICTURES), "share_image_" + System.currentTimeMillis() + ".png");
            FileOutputStream out = new FileOutputStream(file);
            bmp.compress(Bitmap.CompressFormat.PNG, 90, out);
            out.close();

            // wrap File object into a content provider. NOTE: authority here should match authority in manifest declaration
            bmpUri = FileProvider.getUriForFile(BookDetailActivity.this, "com.codepath.fileprovider", file); // use this version for API >= 24

            // **Note:** For API < 24, you may use bmpUri = Uri.fromFile(file);

        } catch (IOException e) {
            e.printStackTrace();
        }
        return bmpUri; //bmpUri;
    }
}
