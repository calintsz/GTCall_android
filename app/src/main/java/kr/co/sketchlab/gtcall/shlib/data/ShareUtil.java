package kr.co.sketchlab.gtcall.shlib.data;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;

import java.util.ArrayList;

public class ShareUtil {
    public static void sharePhoto(Context context, String filePath, String text) {
        Uri photoUri = Uri.parse(filePath);

        Intent i = new Intent(Intent.ACTION_SEND);
        i.setType("image/*");
        i.putExtra(Intent.EXTRA_STREAM, photoUri);
        i.putExtra(Intent.EXTRA_TEXT, text);
//		i.putExtra(Intent.EXTRA_TITLE, text);
//		i.putExtra(Intent.EXTRA_SUBJECT, text);
        context.startActivity(Intent.createChooser(i, "Share Image"));
    }

    public static void sharePhotos(Context context, ArrayList<Uri> files, String text) {
        Intent i = new Intent(Intent.ACTION_SEND_MULTIPLE);
        i.setType("image/*");
        i.putParcelableArrayListExtra(Intent.EXTRA_STREAM, files);
        i.putExtra(Intent.EXTRA_TEXT, text);
        context.startActivity(Intent.createChooser(i, "Share Image"));
    }

    public static void shareToEmail(Context context, String filePath, String text) {
        Uri photoUri = Uri.parse(filePath);

        Intent emailIntent = new Intent(Intent.ACTION_SENDTO, Uri.fromParts("mailto", "", null));
        //emailIntent.setType("image/*");
        emailIntent.putExtra(Intent.EXTRA_STREAM, photoUri);
        emailIntent.putExtra(Intent.EXTRA_TEXT, text);
        emailIntent.putExtra(Intent.EXTRA_SUBJECT, text);
        context.startActivity(Intent.createChooser(emailIntent, "Send email"));
    }

    public static void shareText(Context context, String shareTitle, String msgTitle, String msg) {
        String shareBody = msg;
        Intent sharingIntent = new Intent(android.content.Intent.ACTION_SEND);
        sharingIntent.setType("text/plain");
        sharingIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, msgTitle);
        sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT, shareBody);
        context.startActivity(Intent.createChooser(sharingIntent, shareTitle));
    }
}
