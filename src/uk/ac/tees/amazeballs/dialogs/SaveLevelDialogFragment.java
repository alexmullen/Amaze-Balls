package uk.ac.tees.amazeballs.dialogs;

import uk.ac.tees.amazeballs.R;
import uk.ac.tees.amazeballs.R.id;
import uk.ac.tees.amazeballs.R.layout;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;


public class SaveLevelDialogFragment extends DialogFragment {

	public interface OnLevelSaveRequestListener {
		public void onLevelSaveRequested(String levelname);
	}
	
	private OnLevelSaveRequestListener listener;
	
	
	public SaveLevelDialogFragment() {

	}

	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		try {
			listener = (OnLevelSaveRequestListener) activity;
		} catch (ClassCastException e) {
            throw new ClassCastException(activity.toString() + " must implement OnLevelSaveRequestListener");
        }
	}
	
	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState) {
		AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
		final View inflatedView = getActivity().getLayoutInflater().inflate(R.layout.dialog_level_save, null);
		final EditText levelNameTextView = (EditText) inflatedView.findViewById(R.id.dialog_save_levelname_edittext);
		builder.setTitle("Name the level");
		builder.setView(inflatedView);
		builder.setPositiveButton("Save", new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				listener.onLevelSaveRequested(levelNameTextView.getText().toString());
			}
		});
		builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				dialog.cancel();
			}
		});
		
		final AlertDialog createdDialog = builder.create();
		levelNameTextView.addTextChangedListener(new TextWatcher() {
			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {
				if (levelNameTextView.getText().length() > 0) {
					createdDialog.getButton(AlertDialog.BUTTON_POSITIVE).setEnabled(true);
				} else {
					createdDialog.getButton(AlertDialog.BUTTON_POSITIVE).setEnabled(false);
				}
			}
			@Override
			public void afterTextChanged(Editable s) {
			}
			@Override
			public void beforeTextChanged(CharSequence s, int start, int count, int after) {
			}
		});
		return createdDialog;
	}

	@Override
	public void onStart() {
		super.onStart();
		/*
		 *  Disable the "Save" button when this fragment is displayed if there is no
		 *  characters in the EditText field. There is a possibility that there could be
		 *  either from a default value or the user entering something and causing this
		 *  DialogFragment to be recreated - such as when changing screen orientation.
		 */
		EditText et = (EditText) getDialog().findViewById(R.id.dialog_save_levelname_edittext);
		if (et.getText().length() == 0) {
			((AlertDialog)getDialog()).getButton(AlertDialog.BUTTON_POSITIVE).setEnabled(false);			
		}
	}
}
