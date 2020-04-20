package bj.modules;

import android.util.Log;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

public class bj_image_classes {
	public static class BJRequestCodes {
		public static final int  REQUEST_CODE_Default=-1;
		public static final int  REQUEST_CODE_Delete=0;
		public static final int  REQUEST_CODE_Add=1;
		public static final int  REQUEST_CODE_Share=2;
		public static final int  REQUEST_CODE_Save_TO_CONTEN=3;
		public static final int  REQUEST_CODE_Save_TO_GALERY=4;
		public static final int  REQUEST_CODE_FILE_FROM_CONTENT=5;
		public static final int  REQUEST_CODE_FILE_FROM_GALERY=6;
		public static final int  REQUEST_CODE_FILE_FROM_CAMERA=7;
		public static final int  REQUEST_CODE_DIRECTORY_SELECT=8;
		public static final int  REQUEST_CODE_ImageFromEditImage=9;

	}
	public static void FragmentpopBackStackTillEntry(@Nullable FragmentManager fragmentManager, int entryIndex) {

		if (fragmentManager == null) {
			return;
		}
		if (fragmentManager.getBackStackEntryCount() <= entryIndex) {
			return;
		}
		FragmentManager.BackStackEntry entry = fragmentManager.getBackStackEntryAt(
				entryIndex);
		if (entry != null) {

			fragmentManager.popBackStackImmediate(entry.getId(),
					FragmentManager.POP_BACK_STACK_INCLUSIVE);
		}


	}
	public static void fragmentOpen(FragmentManager fragmentManager, Fragment fragment, Boolean InBack, Integer  SendContainerViewID){

		FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
		if (InBack) {
			if(fragmentManager.findFragmentByTag(fragment.getClass().getName())!=null) {
				fragmentManager.popBackStack(fragment.getClass().getName(),0);
			}else {
				fragmentTransaction.replace(SendContainerViewID, fragment,fragment.getClass().getName())
						.addToBackStack(fragment.getClass().getName())
						.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
						.commit();
			}


		}else {

			if(fragmentManager.findFragmentByTag(fragment.getClass().getName())!=null) {
				fragmentManager.popBackStack(fragment.getClass().getName(),0);
			}else {
				fragmentTransaction.replace(SendContainerViewID,fragment ,fragment.getClass().getName())
						.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
						.commit();
			}
			//android.R.id.content
		}
	}
	public static void establishLayout(FragmentManager fragmentManager,String tagFragment){

		Fragment fragmentoActual = fragmentManager.findFragmentByTag(tagFragment);
		if(fragmentoActual!=null){
			pushFragments(fragmentManager, tagFragment, fragmentoActual);
		}
	}
	public static void pushFragments(FragmentManager fragmentManager,String tag, Fragment fragment){

		FragmentTransaction ft = fragmentManager.beginTransaction();
		try{
			ft.replace(fragment.getId(), fragment, tag);
			ft.commit();
		}catch (Exception e){
			Log.e("layout-saving",e.getMessage());
		}
	}

}
