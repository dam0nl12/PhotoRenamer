package photo_renamer;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.io.BufferedOutputStream;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutput;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.io.Serializable;
import java.sql.Timestamp;

/**
 * Object that stores all relevant activities of the application.
 * 
 * @authors Jaryd Hunter and Runhao Liang
 */
public class ImageLog implements Serializable {

	/** Unique-ID for serialization. */
	private static final long serialVersionUID = 2L;
	/** All images imported. */
	private ArrayList<ImageFile> allImages;
	/** All user-added tags. */
	private ArrayList<String> allTags;
	/** All changes made to files' names. */
	private HashMap<String, HashMap<Timestamp, String>> history;
	/** Make ImageLog a Singleton */
	// Which is a Singleton Design Pattern.
	public final static ImageLog INSTANCE = new ImageLog();

	/**
	 * An ImageLog.
	 */
	private ImageLog() {
		this.allImages = new ArrayList<ImageFile>();
		this.allTags = new ArrayList<String>();
		this.history = new HashMap<String, HashMap<Timestamp, String>>();
	}

	/**
	 * Return a copy of allImages.
	 * 
	 * @return a copy of allImages
	 */
	public ArrayList<ImageFile> getAllImages() {
		ArrayList<ImageFile> copy = new ArrayList<ImageFile>(allImages);
		return copy;
	}

	/**
	 * Add the image into the ArrayList of all images.
	 * 
	 * @param image
	 *            the image needed to be added
	 */
	public void addImage(ImageFile image) {
		this.allImages.add(image);
	}

	/**
	 * Clear the list of all imported images.
	 */
	public void clearallImages() {
		this.allImages.clear();
	}

	/**
	 * Return a copy of allTags.
	 * 
	 * @return a copy of allTags
	 */
	public ArrayList<String> getAllTags() {
		ArrayList<String> copy = new ArrayList<String>(allTags);
		return copy;
	}

	/**
	 * Add the new tag into the ArrayList of all tags.
	 * 
	 * @param tag
	 *            the tag needed to be added
	 */
	public void addTag(String tag) {
		if (!this.allTags.contains(tag)) {
			this.allTags.add(tag);
		}

	}

	/**
	 * Remove the tag from the ArrayList of all tags.
	 * 
	 * @param tag
	 *            the tag to be removed
	 */
	public void removeTag(String tag) {
		this.allTags.remove(tag);
	}

	/**
	 * Return a copy of history.
	 * 
	 * @return a copy of history
	 */
	public HashMap<String, HashMap<Timestamp, String>> getHistory() {
		HashMap<String, HashMap<Timestamp, String>> copy = new HashMap<String, HashMap<Timestamp, String>>(
				history);
		return copy;
	}

	/**
	 * Add change to history.
	 * 
	 * @param image
	 *            the image that was changed
	 */
	public void addChange(ImageFile image) {
		Calendar calendar = Calendar.getInstance();
		Timestamp time = new Timestamp(calendar.getTime().getTime());
		String changes = "";
		for (String str : image.getTags()) {

			changes = changes + str;
		}

		String key = image.getKey();
		if (history.containsKey(key)) {

			history.get(key).put(time, changes);
		} else {

			HashMap<Timestamp, String> map = new HashMap<Timestamp, String>();
			map.put(time, changes);
			history.put(key, map);
		}
	}

	/**
	 * Writes history to file at filePath.
	 * 
	 * @param filePath
	 *            the file to write the records to
	 * @throws IOException
	 */
	public void saveToFileHistory(String filePath) throws IOException {
		OutputStream file = new FileOutputStream(filePath);
		OutputStream buffer = new BufferedOutputStream(file);
		ObjectOutput output = new ObjectOutputStream(buffer);

		output.writeObject(history);
		output.close();
	}

	/**
	 * Writes all tags to file at filePath.
	 * 
	 * @param filePath
	 *            the file to write the records to
	 * @throws IOException
	 */
	public void saveToFileTags(String filePath) throws IOException {
		OutputStream file = new FileOutputStream(filePath);
		OutputStream buffer = new BufferedOutputStream(file);
		ObjectOutput output = new ObjectOutputStream(buffer);

		output.writeObject(allTags);
		output.close();

	}

	/**
	 * Read history from the file at path filePath.
	 * 
	 * @param filePath
	 *            the path of the data file
	 * @throws IOException
	 * @throws ClassNotFoundException
	 *             if filePath is not a valid path
	 */
	@SuppressWarnings("unchecked")
	public void readFromCSVFileHistory(String filePath)
			throws IOException, ClassNotFoundException {
		FileInputStream fis = new FileInputStream(filePath);
		ObjectInputStream ois = new ObjectInputStream(fis);
		history = (HashMap<String, HashMap<Timestamp, String>>) ois
				.readObject();

		ois.close();
		fis.close();
	}

	/**
	 * Read all tags from the file at path filePath.
	 * 
	 * @param filePath
	 *            the path of the data file
	 * @throws IOException
	 * @throws ClassNotFoundException
	 *             if filePath is not a valid path
	 */
	@SuppressWarnings("unchecked")
	public void readFromCSVFileTags(String filePath)
			throws IOException, ClassNotFoundException {
		FileInputStream fis = new FileInputStream(filePath);
		ObjectInputStream ois = new ObjectInputStream(fis);
		allTags = (ArrayList<String>) ois.readObject();

		ois.close();
		fis.close();
	}

	/**
	 * Return a String representation of this ImageLog.
	 * 
	 * @return a String representation of this ImageLog
	 */
	@Override
	public String toString() {
		return "Image =" + history.toString();
	}
}
