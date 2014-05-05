package uk.ac.tees.amazeballs.menus;

import uk.ac.tees.amazeballs.R;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.widget.Toast;

public class SaveLevelAsDialogFragment extends DialogFragment {

	public SaveLevelAsDialogFragment() {
		
	}

	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState) {
		AlertDialog.Builder builder = new AlertDialog.Builder(this.getActivity());
	
		builder.setView(getActivity().getLayoutInflater().inflate(R.layout.dialog_level_save, null));
		
		builder.setPositiveButton("Save", new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				// Save...
				Toast.makeText(SaveLevelAsDialogFragment.this.getActivity(), "Level saved", Toast.LENGTH_SHORT).show();
			}
		});
		builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				// Close
				dialog.cancel();
			}
		});
		
		return builder.create();
	}
	
}
