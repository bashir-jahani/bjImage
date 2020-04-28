package bj.modules.bj_image;

import android.net.Uri;

import java.io.File;

public class bj_loadable_image_listeners {
	public interface OnLoadOrginalRequestListener{
		void OnRequestOrginal(LoadableImageview RowViewGroup, bj_imageNotice imageNotice,final int position);
		void OnRequestOrginalCancel(LoadableImageview RowViewGroup, bj_imageNotice imageNotice,final int position);
	}
	public interface OnUploadingImageListener{
		void OnUploading(final LoadableImageview RowViewGroup,final bj_imageNotice imageNotice,final int position);
		void OnUploadingCancel(final LoadableImageview RowViewGroup,final  bj_imageNotice imageNotice,final int position);

	}
	public interface OnLoadThumbRequestListener{
		void OnRequestThumb(LoadableImageview RowViewGroup, bj_imageNotice imageNotice,final int position);
	}
	public interface OnNewImageRequest{
		void OnNewImage();
	}
	public interface OnDelleteImageRequest{
		void OnDelleteImage(bj_imageNotice ImageNotice, Integer position);
	}
	public interface OnShareImageRequest{
		void OnShareImage(File imageFile, Uri imageUri);
	}
	public interface OnImageChangedListener {
		long OnChanged(Integer FileIndex, int FileCurrentNumber);
	}

}
