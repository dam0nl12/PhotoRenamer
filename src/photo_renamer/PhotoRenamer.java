package photo_renamer;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.ArrayList;

/**
 * Create and show a Photo Renamer, which displays all images in a directory,
 * and allows users to organize selected images by adding/removing tags.
 * 
 * @authors Jaryd Hunter and Runhao Liang
 */
public class PhotoRenamer {

	/** The ImageLog to save all activities. */
	private ImageLog log;
	/** The FileManager to perform all activities. */
	private FileManager fm;

	/**
	 * A Photo Renamer
	 * 
	 * @param log
	 *            The ImageLog to save all activities
	 * @param fm
	 *            The FileManager to perform all activities.
	 */
	public PhotoRenamer(ImageLog log, FileManager fm) {
		this.log = log;
		this.fm = fm;
	}

	/**
	 * Return the ImageLog of this Photo Renamer.
	 * 
	 * @return the ImageLog of this Photo Renamer
	 */
	public ImageLog getLog() {
		return log;
	}

	/**
	 * Return the FileManager of this Photo Renamer.
	 * 
	 * @return the FileManager of this Photo Renamer
	 */
	public FileManager getFm() {
		return fm;
	}

	/**
	 * Add tags to the corresponding image, and save changes to log.
	 * 
	 * @param image
	 *            the image that gets tags
	 * @param log
	 *            the log that saves the changes
	 * @param fm
	 *            the FileManager make the changes
	 * @param newtags
	 *            tags needed to be added
	 */
	public void addTagstoImage(ImageFile image, ImageLog log, FileManager fm,
			String[] newtags) {
		// Add new tag(s) to the image's list of tags.
		for (String newtag : newtags) {

			if (!image.getTags().contains(newtag)) {
				image.addTag(newtag);
			}
		}

		String alltags = "";
		// Change the actual file name.
		for (String tag : image.getTags()) {

			alltags = alltags + tag;
		}
		fm.changeName(image, alltags);
		log.addChange(image);
	}

	/**
	 * Remove tags from the corresponding image, and save changes to log.
	 * 
	 * @param image
	 *            the image that loses tags
	 * @param log
	 *            the log that saves the changes
	 * @param fm
	 *            the FileManager make the changes
	 * @param newtags
	 *            tags needed to be removed
	 */
	public void removeTagfromImage(ImageFile image, ImageLog log,
			FileManager fm, String[] newtags) {
		// Remove the tag(s) from the image's list of tags
		for (String newtag : newtags) {

			image.removeTag(newtag);
		}

		String totaltag = "";
		// Change the actual file name.
		for (String str : image.getTags()) {

			totaltag = totaltag + str;
		}
		fm.changeName(image, totaltag);
		log.addChange(image);
	}

	/**
	 * Add a new tag to the list of all tags.
	 * 
	 * @param log
	 *            the log that saves all information
	 * @param newtag
	 *            the new tag needed to be added
	 */
	public void addNewTag(ImageLog log, String newtag) {
		String[] tags = newtag.split("@");
		for (int i = 1; i < tags.length; i++) {

			log.addTag("@" + tags[i]);
		}
	}

	/**
	 * Remove a tag from the list of all tags.
	 * 
	 * @param log
	 *            the log that saves all information
	 * @param oldtag
	 *            the old tag needed to be removed
	 * @param fm
	 *            the FileManager make the changes
	 */
	public void removeOldTag(ImageLog log, String oldtag, FileManager fm) {
		log.removeTag(oldtag);
		String[] oldtags = { oldtag };
		for (ImageFile image : log.getAllImages()) {

			if (image.getTags().contains(oldtag)) {

				this.removeTagfromImage(image, log, fm, oldtags);
			}
		}
	}

	/**
	 * Revert image tags to the previous version.
	 * 
	 * @param image
	 *            the image that needs to be reverted
	 * @param fm
	 *            the FileManager make the changes
	 * @param log
	 *            the log that saves all information
	 * @param tags
	 *            the tags to revert to
	 */
	public void revertImage(ImageFile image, FileManager fm, ImageLog log,
			String tags) {
		fm.changeName(image, tags);
		String[] lst = tags.split("@");
		image.setTags(new ArrayList<String>());
		addNewTag(log, tags);
		for (String tag : lst) {

			image.addTag("@" + tag);
			image.removeTag("@");
		}
	}

	/**
	 * Import all images under the given directory.
	 * 
	 * @param file
	 *            the directory to import images from
	 * @param log
	 *            the log that saves all information
	 * @param fm
	 *            the FileManager make the changes
	 */
	public void openDirectory(File file, ImageLog log, FileManager fm) {
		log.clearallImages();
		for (ImageFile image : fm.getImages(file, log)) {
			log.addImage(image);
		}
	}

	/**
	 * Build a Window for Photo Renamer, and load previous activities (if CSV
	 * Files exist).
	 * 
	 * @return a Window for Photo Renamer
	 * 
	 * @throws URISyntaxException
	 * @throws ClassNotFoundException
	 * @throws IOException
	 */
	public Window buildwindow()
			throws URISyntaxException, ClassNotFoundException, IOException {
		// Get directories of CSV Files
		String directory = PhotoRenamer.class.getProtectionDomain()
				.getCodeSource().getLocation().toURI().getPath();
		String pathhistory = new String(directory + "History.csv");
		File filehistory = new File(directory + "History.csv");
		String pathtags = new String(directory + "Tags.csv");
		File filetags = new File(directory + "Tags.csv");

		// Check whether CSV File of history exists. If it does not exist, then
		// create a new one.
		if (filehistory.exists()) {

			this.log.readFromCSVFileHistory(pathhistory);
		} else {

			FileWriter writer = new FileWriter(pathhistory);
			writer.flush();
			writer.close();
		}

		// Check whether CSV File of allTags exists. If it does not exist, then
		// create a new one.
		if (filetags.exists()) {

			this.log.readFromCSVFileTags(pathtags);
		} else {

			FileWriter writer = new FileWriter(pathtags);
			writer.flush();
			writer.close();
		}

		// Save all changes (for the first time).
		this.log.saveToFileHistory(pathhistory);
		this.log.saveToFileTags(pathtags);

		// Create a new PhotoRenamer Window
		Window frame = new Window(this, pathhistory, pathtags);

		return frame;
	}

	/**
	 * Create and show a Photo Renamer.
	 *
	 * @param argsv
	 *            the command-line arguments.
	 */
	public static void main(String args[])
			throws URISyntaxException, ClassNotFoundException, IOException {
		ImageLog log = ImageLog.INSTANCE;
		FileManager fm = new FileManager();
		PhotoRenamer pr = new PhotoRenamer(log, fm);
		pr.buildwindow().setVisible(true);
	}
}
