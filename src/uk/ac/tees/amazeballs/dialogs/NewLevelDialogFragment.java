package uk.ac.tees.amazeballs.dialogs;

import uk.ac.tees.amazeballs.R;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.widget.SeekBar;
import android.widget.TextView;


public class NewLevelDialogFragment extends DialogFragment {

	private static final int MINIMUM_LEVEL_WIDTH = 8;
	private static final int MINIMUM_LEVEL_HEIGHT = 8;
	
	public interface OnNewLevelRequestListener {
		public void onNewLevelRequested(int width, int height);
	}
	
	private OnNewLevelRequestListener listener;
	
	
	public NewLevelDialogFragment() {

	}

	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		try {
			listener = (OnNewLevelRequestListener) activity;
		} catch (ClassCastException e) {
            throw new ClassCastException(activity.toString() + " must implement OnLevelSaveRequestListener");
        }
	}
	
	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState) {
		AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
		final View inflatedView = getActivity().getLayoutInflater().inflate(R.layout.dialog_newmazesize, null);
		
		final SeekBar seekWidthBar = (SeekBar) inflatedView.findViewById(R.id.seekbar_maze_width);
		final SeekBar seekHeightBar = (SeekBar) inflatedView.findViewById(R.id.seekbar_maze_height);
		final TextView seekWidthValue = (TextView) inflatedView.findViewById(R.id.width_value);
		final TextView seekHeightValue = (TextView) inflatedView.findViewById(R.id.height_value);

		seekWidthBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
			@Override
			public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
				seekWidthValue.setText(String.valueOf(Math.max(MINIMUM_LEVEL_WIDTH, progress)));
			}
			@Override
			public void onStopTrackingTouch(SeekBar seekBar) { }
			@Override
			public void onStartTrackingTouch(SeekBar seekBar) { }
		});
		
		seekHeightBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
			@Override
			public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
				seekHeightValue.setText(String.valueOf(Math.max(MINIMUM_LEVEL_HEIGHT, progress)));
			}
			@Override
			public void onStopTrackingTouch(SeekBar seekBar) { }
			@Override
			public void onStartTrackingTouch(SeekBar seekBar) { }
		});
		
		builder.setTitle("Choose the maze dimensions");
		builder.setView(inflatedView);
		builder.setPositiveButton("Create", new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				listener.onNewLevelRequested(
						Math.max(MINIMUM_LEVEL_WIDTH, seekWidthBar.getProgress()), 
						Math.max(MINIMUM_LEVEL_HEIGHT, seekHeightBar.getProgress()));
			}
		});
		builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				dialog.cancel();
			}
		});
		return builder.create();
	}
}
