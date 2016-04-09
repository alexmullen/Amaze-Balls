package uk.ac.tees.amazeballs.dialogs;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;


/**
 * A dialog for choosing from a list of levels.
 * 
 * @author Alex Mullen (J9858839)
 *
 */
public class LevelChooseDialogFragment extends DialogFragment {

	public interface OnLevelChooseListener {
		public void onLevelChosen(String levelname);
	}
	
	private OnLevelChooseListener listener;
	
	
	public LevelChooseDialogFragment() {

	}

	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		listener = (OnLevelChooseListener) activity;
	}
	
	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState) {
		super.onCreateDialog(savedInstanceState);
		final String[] levels = getArguments().getStringArray("levels");
		AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
		builder.setTitle(getArguments().getString("title"));
		builder.setItems(levels, new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				listener.onLevelChosen(levels[which]);
			}
		});
		return builder.create();
	}
	
}
