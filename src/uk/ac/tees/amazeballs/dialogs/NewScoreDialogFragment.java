package uk.ac.tees.amazeballs.dialogs;

import uk.ac.tees.amazeballs.R;
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

public class NewScoreDialogFragment extends DialogFragment {

	public interface OnScoreSaveRequestListener {
		public void onScoreSaveRequested(String playername);
	}
	
	private OnScoreSaveRequestListener listener;
	
	
	public NewScoreDialogFragment() {
	}

	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		try {
			listener = (OnScoreSaveRequestListener) activity;
		} catch (ClassCastException e) {
            throw new ClassCastException(activity.toString() + " must implement OnScoreSaveRequestListener");
        }
	}
	
	// This method is executed when the fragment is ifrst created
	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState) {
		AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
		final View inflatedView = getActivity().getLayoutInflater().inflate(R.layout.dialog_score_save, null);
		final EditText playerNameTextView = (EditText) inflatedView.findViewById(R.id.dialog_save_playername_edittext);
		builder.setTitle("Insert Player Name");
		builder.setView(inflatedView);
		// Details the "save" buttons behaviour
		builder.setPositiveButton("Save", new DialogInterface.OnClickListener() {
			@Override
			// 
			public void onClick(DialogInterface dialog, int which) {
				listener.onScoreSaveRequested(playerNameTextView.getText().toString());
			}
		});
		// Details the "cancel" buttons behaviour
		builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				dialog.cancel();
			}
		});
		
		final AlertDialog createdDialog = builder.create();
		playerNameTextView.addTextChangedListener(new TextWatcher() {
			@Override
			// Prevent null data entry by disabling the save button until the text field is populated
			public void onTextChanged(CharSequence s, int start, int before, int count) {
				if (playerNameTextView.getText().length() > 0) {
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
	
	// Sets the look of the save player name dialog
	@Override
	public void onStart() {
		super.onStart();
		/*
		 *  Disable the "Save" button when this fragment is displayed if there is no
		 *  characters in the EditText field. There is a possibility that there could be
		 *  either from a default value or the user entering something and causing this
		 *  DialogFragment to be recreated - such as when changing screen orientation.
		 */
		EditText et = (EditText) getDialog().findViewById(R.id.dialog_save_playername_edittext);
		if (et.getText().length() == 0) {
			((AlertDialog)getDialog()).getButton(AlertDialog.BUTTON_POSITIVE).setEnabled(false);			
		}
	}
}
