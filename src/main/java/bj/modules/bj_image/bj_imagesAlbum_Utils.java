package bj.modules.bj_image;

import android.content.Context;
import android.graphics.Point;
import android.view.Display;
import android.view.WindowManager;
import android.widget.Toast;

import java.io.File;
import java.io.FilenameFilter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;

import bj.modules.bj_file_path;


/**
 * Created by Bashir jahani on 1/24/2018.
 */

public class bj_imagesAlbum_Utils {
    private Context _context;
    public static List<String> _FILE_EXTN = Arrays.asList("jpg", "jpeg","png");
    // constructor
    public bj_imagesAlbum_Utils(Context context, List<String> FILE_EXTN) {
        this._context = context;
        if (!FILE_EXTN.isEmpty() & FILE_EXTN!=null){
            this._FILE_EXTN=FILE_EXTN;
        }

    }


    // Reading file paths from SDCard

    public  ArrayList<bj_imageNotice> getFilePaths(String DirectoryPath) {
        String[] filePaths=null ;

        File directory = new File(DirectoryPath);
        ArrayList<bj_imageNotice> RS;
        // check for directory
        if (directory.isDirectory()) {
            // getting list of file paths
            FilenameFilter only=new bj_file_path.OnlyExt(_FILE_EXTN);
            String files[]= directory.list(only);

            //Toast.makeText(_context, DirectoryPath + File.separator+ files[0], Toast.LENGTH_SHORT).show();

            // Check for count
            if (files.length > 0) {
                Arrays.sort(files);
                RS=new ArrayList<bj_imageNotice>();
                for(int i=0;i<files.length;i++){
                    bj_imageNotice p=new bj_imageNotice();
                    p.SetImagePath(DirectoryPath + File.separator+ files[i],false,false);

                    RS.add(p);
                }
            } else {
                RS=null;
                // image directory is empty
                Toast.makeText(
                        _context,
                        DirectoryPath
                                + " is empty. Please load some images in it !",
                        Toast.LENGTH_LONG).show();
            }

        } else {
            RS=null;
            Toast.makeText(
                    _context,
                    DirectoryPath+"'\n"
                            + "directory path is not valid! Please set the image directory name currectly",
                    Toast.LENGTH_LONG).show();

        }

        return RS;
    }

    // Check supported file extensions
    private boolean IsSupportedFile(String filePath) {
        String ext = filePath.substring((filePath.lastIndexOf(".") + 1),
                filePath.length());

        if (_FILE_EXTN
                .contains(ext.toLowerCase(Locale.getDefault())))
            return true;
        else
            return false;

    }

    /*
     * getting screen width
     */
    public int getScreenWidth() {
        int columnWidth;
        WindowManager wm = (WindowManager) _context
                .getSystemService(Context.WINDOW_SERVICE);
        Display display = wm.getDefaultDisplay();

        final Point point = new Point();
        try {
            display.getSize(point);
        } catch (NoSuchMethodError ignore) { // Older device
            point.x = display.getWidth();
            point.y = display.getHeight();
        }
        columnWidth = point.x;
        return columnWidth;
    }
}
