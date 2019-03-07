package photo_renamer;

import java.awt.Color;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.DefaultListModel;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.ListSelectionModel;
import javax.swing.SwingConstants;
import javax.swing.border.LineBorder;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

/**
 * Panel tailored to display imported images from a selected directory.
 * 
 * @author Jaryd Hunter and Runhao Liang
 *
 */
@SuppressWarnings("serial")
public class DirectoryPanel extends JPanel {

	/** The total number of imported images. */
	private JLabel imagecount;
	/** The list of all imported images. */
	private JList<ImageFile> listimages;
	/** Component for listimages that holds ImageFiles. */
	private DefaultListModel<ImageFile> model;

	/**
	 * A Directory Panel.
	 * 
	 * @param win
	 *            where Directory Panel will be added
	 * @param pr
	 *            connecting to back end
	 */
	public DirectoryPanel(Window win, PhotoRenamer pr) {

		// Create JPanel for directory and all display of all imported images.
		this.setBorder(new LineBorder(Color.GRAY));
		this.setBounds(10, 32, 249, 710);
		this.setLayout(null);

		// Create title for list of images.
		JLabel listimageslbl = new JLabel("List of Images");
		listimageslbl.setHorizontalAlignment(SwingConstants.CENTER);
		listimageslbl.setBounds(0, 80, 250, 16);
		this.add(listimageslbl);

		// Create viewer for list of images.
		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setBounds(5, 110, 240, 581);
		this.add(scrollPane);

		// Create image counter.
		this.imagecount = new JLabel("(0)");
		imagecount.setBounds(173, 80, 77, 16);
		this.add(imagecount);

		// Create list of images.
		// Which is an Observable for this Observer Design Pattern.
		this.model = new DefaultListModel<ImageFile>();
		this.listimages = new JList<ImageFile>(model);
		// this.win.getListimages() = new JList<ImageFile>(model);
		this.listimages.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		this.listimages.setBorder(new LineBorder(Color.GRAY));
		this.listimages.setBounds(5, 110, 240, 581);

		// List of images listener.
		// Which is an Observer for this Observer Design Pattern.
		this.listimages.addListSelectionListener(new ListSelectionListener() {

			/**
			 * Update information in window to reflect a new image being
			 * selected.
			 * 
			 * @param e
			 *            ActionEvent this listens to.
			 */
			@SuppressWarnings("deprecation")
			public void valueChanged(ListSelectionEvent e) {
				BufferedImage img = null;
				win.getListrevert().clear();
				try {

					Integer i = listimages.getSelectedIndex();
					if (!i.equals(-1)) {

						img = ImageIO.read(pr.getLog().getAllImages().get(i)
								.getLocation());
						@SuppressWarnings("static-access")
						Image scaledimg = img.getScaledInstance(485, 364,
								img.SCALE_SMOOTH);
						ImageIcon icon = new ImageIcon(scaledimg);
						win.getImagelbl().setIcon(icon);
						// Show all previous tags of this image.

						String key = pr.getLog().getAllImages().get(i).getKey();
						if (pr.getLog().getHistory().containsKey(key)) {

							String reversion = pr.getLog().getHistory().get(key)
									.toString();
							String[] reversions = reversion
									.substring(1, reversion.length() - 1)
									.split(", ");
							// sort reversions
							for (String str : reversions) {

								String[] tba = str.split("=");
								if (tba.length > 1) {

									win.getListrevert().add(
											tba[1] + " at Time: " + tba[0]);
								}
							}
						}
					}

				} catch (IOException f) {
				}
			}
		});

		scrollPane.setViewportView(this.listimages);

		// Button and Listener to import images.
		JFileChooser fileChooser = new JFileChooser();
		fileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);

		JButton getimagesbtn = new JButton("Import Directory");
		getimagesbtn.setBounds(0, 25, 250, 30);
		getimagesbtn.addActionListener(new ActionListener() {

			/**
			 * Populates list of images with ImageFiles from selected directory.
			 * 
			 * @param e
			 *            ActionEvent this listens to.
			 */
			@Override
			public void actionPerformed(ActionEvent e) {

				int returnVal = fileChooser
						.showOpenDialog(win.getContentPane());
				if (returnVal == JFileChooser.APPROVE_OPTION) {

					File file = fileChooser.getSelectedFile();
					model.clear();
					if (file.exists()) {

						pr.openDirectory(file, pr.getLog(), pr.getFm());
						for (ImageFile image : pr.getLog().getAllImages()) {

							model.addElement(image);
							imagecount.setText("("
									+ String.valueOf(
											pr.getLog().getAllImages().size())
									+ ")");
							win.getTagCount().setText("("
									+ String.valueOf(
											pr.getLog().getAllTags().size())
									+ ")");
						}
					}
				}
				win.popListTags();
			}
		});

		this.add(getimagesbtn);
	}

	/**
	 * Return list of all images.
	 * 
	 * @return list of all images.
	 */
	public JList<ImageFile> getListimages() {
		return listimages;
	}

	/**
	 * Populate the list of images with changes.
	 * 
	 * @param pr
	 *            Class to connect back-end for this function of the
	 *            DirectoryPanel
	 */
	public void popListImages(PhotoRenamer pr) {
		// save selection.
		int index = listimages.getSelectedIndex();
		model.removeAllElements();
		for (ImageFile image : pr.getLog().getAllImages()) {

			model.addElement(image);
			// Reselect item.
			listimages.setSelectedIndex(index);
		}
	}
}
