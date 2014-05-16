package uk.ac.tees.amazeballs.dialogs;

import java.util.ArrayList;

import uk.ac.tees.amazeballs.R;
import uk.ac.tees.amazeballs.maze.TileImageFactory;
import uk.ac.tees.amazeballs.maze.TileType;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;


/**
 * A dialog for choosing from a list of tiles.
 * 
 * @author Alex Mullen (J9858839)
 *
 */
public class TileChooseDialogFragment extends DialogFragment {

	private static final ArrayList<SpecialTileChoice> SPECIAL_TILES;
	
	static {
		SPECIAL_TILES = new ArrayList<SpecialTileChoice>();
		SPECIAL_TILES.add(new SpecialTileChoice(TileType.Start, TileImageFactory.getImage(TileType.Start)));
		SPECIAL_TILES.add(new SpecialTileChoice(TileType.Goal, TileImageFactory.getImage(TileType.Goal)));
		SPECIAL_TILES.add(new SpecialTileChoice(TileType.Key, TileImageFactory.getImage(TileType.Key)));
		SPECIAL_TILES.add(new SpecialTileChoice(TileType.Door, TileImageFactory.getImage(TileType.Door)));
		SPECIAL_TILES.add(new SpecialTileChoice(TileType.Penalty, TileImageFactory.getImage(TileType.Penalty)));
		SPECIAL_TILES.add(new SpecialTileChoice(TileType.Chest, TileImageFactory.getImage(TileType.Chest)));
		SPECIAL_TILES.add(new SpecialTileChoice(TileType.Weather, TileImageFactory.getImage(TileType.Weather)));
		SPECIAL_TILES.add(new SpecialTileChoice(TileType.Ice, TileImageFactory.getImage(TileType.Ice)));
		SPECIAL_TILES.add(new SpecialTileChoice(TileType.Rain, TileImageFactory.getImage(TileType.Rain)));
		SPECIAL_TILES.trimToSize();
	}
	
	private static class SpecialTileChoice {
		public final TileType type;
		public final Drawable image;
		public SpecialTileChoice(TileType type, Drawable image) {
			this.type = type;
			this.image = image;
		}
	}

	public interface OnTileChooseListener {
		public void onTileChosen(TileChooseDialogFragment dialog, TileType type);
	}
	
	private OnTileChooseListener listener;
	
	
	
	public TileChooseDialogFragment() {

	}

	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		listener = (OnTileChooseListener) activity;
	}
	
	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState) {
		super.onCreateDialog(savedInstanceState);
		
		// Display a dialog of special blocks to choose from
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle(getArguments().getString("title"));
        builder.setAdapter(new ArrayAdapter<SpecialTileChoice>(getActivity(), R.layout.dialog_specialtile_row, SPECIAL_TILES) {
            @Override
            public View getView(int position, View convertView, ViewGroup parent) {
 
                View view = (convertView != null ? convertView :
                	LayoutInflater.from(getContext()).inflate(R.layout.dialog_specialtile_row, parent, false));
                
                ImageView imageView = (ImageView) view.findViewById(R.id.special_tile_choice_image);
                TextView textView = (TextView) view.findViewById(R.id.special_tile_choice_title);
                SpecialTileChoice specialTileChoice = getItem(position);
                imageView.setImageDrawable(specialTileChoice.image);
                textView.setText(specialTileChoice.type.name());
                return view;
            }
        }, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
        		listener.onTileChosen(TileChooseDialogFragment.this, SPECIAL_TILES.get(which).type);
            }
        });
        return builder.create();
	}
	
}
