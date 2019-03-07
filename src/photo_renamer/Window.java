package photo_renamer;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import java.awt.Color;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JButton;
import javax.swing.SwingConstants;
import java.awt.List;
import java.io.IOException;
import java.net.URISyntaxException;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import javax.swing.JTextField;
import javax.swing.border.LineBorder;

/**
 * The class that build the window for the Photo Renamer.
 * 
 * @authors Jaryd Hunter and Runhao Liang
 */
@SuppressWarnings("serial")
public class Window extends JFrame {

	/** The panel the buttons and viewers are in. */
	private JPanel contentPane;

	/** The main class for the application. */
	private PhotoRenamer pr;
	/** The image viewer. */
	private JLabel imagelbl;
	/** The display for all previous tag combinations. */
	private List listrevert;
	/** The path to CSV File that saves all activities. */
	@SuppressWarnings("unused")
	private String pathhistory;
	/** The path to CSV File that saves all tags. */
	@SuppressWarnings("unused")
	private String pathtags;
	/** The JPanel that displays all imported ImageFiles. */
	private DirectoryPanel dp;
	/** The list of all tags. */
	private List listtags;
	/** The total number of imported tags. */
	private JLabel tagcount;
	/** Input field for new tag names. */
	private JTextField taginput;

	/**
	 * A Window.
	 * 
	 * @param pr
	 *            the Photo Renamer the GUI is for
	 * @param pathhistory
	 *            the path to the CSV File for saving all activities
	 * @param pathtags
	 *            the path to the CSV File for saving all tags
	 * @throws URISyntaxException
	 */
	@SuppressWarnings("deprecation")
	public Window(PhotoRenamer pr, String pathhistory, String pathtags) {
		this.pr = pr;
		this.pathhistory = pathhistory;
		this.pathtags = pathtags;

		// ------------------------------JFrame-------------------------------
		// Create JFrame with a master JPanel on it.
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 1040, 800);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);

		// -----------------------------TitlePanel-----------------------------
		// Create top JPanel which has the Title.
		JPanel titlepanel = new JPanel();
		titlepanel.setBounds(0, 5, 1040, 26);
		contentPane.add(titlepanel);

		JLabel photorenamerlbl = new JLabel("Photo Renamer");
		titlepanel.add(photorenamerlbl);

		// ---------------------------DirectoryPanel---------------------------
		// Create JPanel (on the left of Window) for directory and all display
		// of all imported images.
		DirectoryPanel dp = new DirectoryPanel(this, this.pr);
		contentPane.add(dp);

		// ------------------------------TagPanel------------------------------
		// Create JPanel (in the middle of Window) for master list of tags and
		// all activities related to it. Could not fix bugs caused by creating a
		// new class for TagPanel, so we only made a new class for Directory
		// Panel.
		//
		JPanel tagpanel = new JPanel();
		tagpanel.setBorder(new LineBorder(Color.GRAY));
		tagpanel.setBounds(269, 32, 250, 710);
		tagpanel.setLayout(null);
		contentPane.add(tagpanel);

		// Button and Listener to create tags.
		JButton createtagbtn = new JButton("Create New Tag(s)");
		createtagbtn.setBounds(0, 38, 250, 30);
		createtagbtn.addActionListener(new ActionListener() {

			/**
			 * Create a new tag when button createtagbtn is activated.
			 * 
			 * @param e
			 *            ActionEvent this listens to.
			 */
			@Override
			public void actionPerformed(ActionEvent e) {
				String newtag = "@" + taginput.getText();
				pr.addNewTag(pr.getLog(), newtag);
				popListTags();
				try {

					pr.getLog().saveToFileTags(pathtags);

				} catch (IOException e1) {
					e1.printStackTrace();
				}
				tagcount.setText(
						"(" + String.valueOf(pr.getLog().getAllTags().size())
								+ ")");
				taginput.setText("");
			}
		});
		tagpanel.add(createtagbtn);

		// Button and Listener to delete tags.
		JButton deletetagbtn = new JButton("Delete a Existing Tag");
		deletetagbtn.addActionListener(new ActionListener() {

			/**
			 * Delete an existing tag when button deletetagbtn is activated.
			 * 
			 * @param e
			 *            ActionEvent this listens to.
			 */
			@Override
			public void actionPerformed(ActionEvent e) {
				pr.removeOldTag(pr.getLog(), listtags.getSelectedItem(),
						pr.getFm());
				popListTags();
				dp.popListImages(pr);
				try {

					pr.getLog().saveToFileTags(pathtags);
				} catch (IOException e1) {

					e1.printStackTrace();
				}
				tagcount.setText(
						"(" + String.valueOf(pr.getLog().getAllTags().size())
								+ ")");
			}
		});
		deletetagbtn.setBounds(0, 664, 250, 30);
		tagpanel.add(deletetagbtn);

		// Create title for list of tags.
		JLabel listoftagslbl = new JLabel("List of Tags");
		listoftagslbl.setHorizontalAlignment(SwingConstants.CENTER);
		listoftagslbl.setBounds(0, 80, 250, 16);
		tagpanel.add(listoftagslbl);

		// Create list of tags.
		this.listtags = new List();
		this.popListTags();
		listtags.setMultipleSelections(true);
		listtags.setBounds(5, 110, 240, 548);
		tagpanel.add(listtags);

		// Input field for new tags.
		this.taginput = new JTextField();
		taginput.setBounds(24, 11, 222, 26);
		tagpanel.add(taginput);
		taginput.setColumns(10);
		JLabel lblNewLabel = new JLabel("@");
		lblNewLabel.setHorizontalAlignment(SwingConstants.CENTER);
		lblNewLabel.setBounds(0, 11, 30, 26);
		tagpanel.add(lblNewLabel);

		// Create tag counter.
		this.tagcount = new JLabel(
				"(" + String.valueOf(pr.getLog().getAllTags().size()) + ")");
		tagcount.setBounds(164, 80, 91, 16);
		tagpanel.add(tagcount);

		// -----------------------------ImagePanel-----------------------------
		// Create JPanel (on the right of Window) for image viewer and list of
		// previous tags. Could not fix bugs caused by creating a
		// new class for ImagePanel, so we only made a new class for Directory
		// Panel.
		JPanel imagepanel = new JPanel();
		imagepanel.setBorder(new LineBorder(Color.GRAY));
		imagepanel.setBounds(529, 32, 495, 710);
		imagepanel.setLayout(null);
		contentPane.add(imagepanel);

		// Title for image Panel.
		JLabel titlelbl = new JLabel("Image");
		titlelbl.setHorizontalAlignment(SwingConstants.CENTER);
		titlelbl.setBounds(0, 0, 495, 20);
		imagepanel.add(titlelbl);

		// Area to display selected image.
		this.imagelbl = new JLabel();
		imagelbl.setBounds(5, 75, 485, 364);
		imagepanel.add(imagelbl);

		// Button and Listener to remove tags from selected image.
		JButton removetagsbtn = new JButton("Remove Tag(s)");
		removetagsbtn.setBounds(380, 20, 117, 29);
		removetagsbtn.addActionListener(new ActionListener() {

			/**
			 * Remove existing tags from selected ImageFile when button
			 * removetagsbtn is activated.
			 * 
			 * @param e
			 *            ActionEvent this listens to.
			 */
			@Override
			public void actionPerformed(ActionEvent e) {
				Integer imageindex = dp.getListimages().getSelectedIndex();
				if (!imageindex.equals(-1)) {

					String[] tag = listtags.getSelectedItems();
					pr.removeTagfromImage(
							pr.getLog().getAllImages().get(imageindex),
							pr.getLog(), pr.getFm(), tag);
					dp.popListImages(pr);
				}
				try {

					pr.getLog().saveToFileTags(pathtags);
					pr.getLog().saveToFileHistory(pathhistory);
				} catch (IOException e1) {

					e1.printStackTrace();
				}
			}
		});
		imagepanel.add(removetagsbtn);

		// Create title for list of previous tags.
		JLabel revertlbl = new JLabel("List of Previous Tags");
		revertlbl.setBounds(0, 459, 495, 16);
		imagepanel.add(revertlbl);
		revertlbl.setHorizontalAlignment(SwingConstants.CENTER);

		// Button and Listener to add tags to selected image.
		JButton addtagsbtn = new JButton("Add Tag(s)");
		addtagsbtn.setBounds(0, 20, 122, 29);
		addtagsbtn.addActionListener(new ActionListener() {

			/**
			 * Add a new tag to selected ImageFile when button addtagsbtn is
			 * activated.
			 * 
			 * @param e
			 *            ActionEvent this listens to.
			 */
			@Override
			public void actionPerformed(ActionEvent e) {
				Integer imageindex = dp.getListimages().getSelectedIndex();
				if (!imageindex.equals(-1)) {

					String[] tag = listtags.getSelectedItems();
					pr.addTagstoImage(
							pr.getLog().getAllImages().get(imageindex),
							pr.getLog(), pr.getFm(), tag);
					dp.popListImages(pr);
				}
				try {

					pr.getLog().saveToFileTags(pathtags);
					pr.getLog().saveToFileHistory(pathhistory);
				} catch (IOException e1) {

					e1.printStackTrace();
				}

			}
		});
		imagepanel.add(addtagsbtn);

		// Button and Listener to revert tags of selected image to selected
		// state.
		JButton reverttagsbtn = new JButton("Revert Tags");
		reverttagsbtn.setBounds(0, 664, 495, 29);
		reverttagsbtn.addActionListener(new ActionListener() {
			/**
			 * Revert to a previous tag combination when button reverttagsbtn is
			 * activated.
			 * 
			 * @param e
			 *            ActionEvent this listens to.
			 */
			@Override
			public void actionPerformed(ActionEvent e) {
				pr.revertImage(dp.getListimages().getSelectedValue(),
						pr.getFm(), pr.getLog(),
						listrevert.getSelectedItem().split(" at Time: ")[0]);
				popListTags();
				dp.popListImages(pr);
			}
		});
		imagepanel.add(reverttagsbtn);

		// Create list of previous tags.
		this.listrevert = new List();
		listrevert.setBounds(5, 484, 485, 174);
		imagepanel.add(listrevert);
		listrevert.setMultipleSelections(false);

		// Area marker for images appearance.
		JLabel picturetbh = new JLabel("Picture Appears Here!");
		picturetbh.setHorizontalAlignment(SwingConstants.CENTER);
		picturetbh.setBounds(0, 75, 495, 364);
		imagepanel.add(picturetbh);

	}

	/**
	 * Return list of all imported images.
	 * 
	 * @return list of all imported images.
	 */
	public JList<ImageFile> getListimages() {
		return dp.getListimages();
	}

	/**
	 * Return space that displays image.
	 * 
	 * @return space that displays image
	 */
	public JLabel getImagelbl() {
		return imagelbl;
	}

	/**
	 * Return list of previous tags of an image.
	 * 
	 * @return list of previous tags of an image
	 */
	public List getListrevert() {
		return listrevert;
	}

	/**
	 * Return total number of tags.
	 * 
	 * @return total number of tags
	 */
	public JLabel getTagCount() {
		return tagcount;
	}

	/**
	 * Helper method to populate listtags with all tags.
	 * 
	 */
	public void popListTags() {
		listtags.removeAll();
		for (String tag : pr.getLog().getAllTags()) {

			listtags.add(tag);
		}
	}
}
