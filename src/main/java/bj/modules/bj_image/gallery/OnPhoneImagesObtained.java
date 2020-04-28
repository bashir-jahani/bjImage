package bj.modules.bj_image.gallery;

import java.util.Vector;

public interface OnPhoneImagesObtained {
	void onComplete( Vector<PhoneAlbum> albums );
	void onError();
}
