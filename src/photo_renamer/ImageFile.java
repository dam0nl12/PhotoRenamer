package photo_renamer;

import java.util.ArrayList;
import java.io.File;

/**
 * Object representing an image saved as File location.
 * 
 * @authors Jaryd Hunter and Runhao Liang
 */
public class ImageFile {

	/** Name of the image stored at File location without tags or extension. */
	private final String name;
	/** The tags given to this image. */
	private ArrayList<String> tags;
	/** The file object containing this image. */
	private File location;

	/**
	 * An ImageFile.
	 * 
	 * @param name
	 *            the name of this image
	 * @param tags
	 *            tags for this image
	 * @param location
	 *            File storing this image
	 */
	public ImageFile(String name, ArrayList<String> tags, File location) {
		this.name = name;
		this.tags = tags;
		this.location = location;
	}

	/**
	 * Return name of this image.
	 * 
	 * @return name of this image
	 */
	public String getName() {
		return name;
	}

	/**
	 * Return all tags for this image.
	 * 
	 * @return tags for this image
	 */
	public ArrayList<String> getTags() {
		ArrayList<String> copy = new ArrayList<String>(tags);
		return copy;
	}

	/**
	 * Set tags for this image.
	 * 
	 * @param tags
	 *            for this image
	 */
	public void setTags(ArrayList<String> tags) {
		this.tags = tags;
	}

	/**
	 * Return File this image is stored in.
	 * 
	 * @return location of this image
	 */
	public File getLocation() {
		return location;
	}

	/**
	 * Set location for this image.
	 * 
	 * @param location
	 *            for this image
	 */
	public void setLocation(File location) {
		this.location = location;
	}

	/**
	 * Add a new tag for this ImageFile, and increase num_tags by 1.
	 * 
	 * Precondition: tag starts with "@"
	 * 
	 * @param tag
	 *            for this ImageFile
	 */
	public void addTag(String tag) {
		tags.add(tag);
	}

	/**
	 * Remove specific tag from Tags, and decrease num_tags by 1.
	 * 
	 * @param tag
	 *            that needs to be removed
	 */
	public void removeTag(String tag) {
		tags.remove(tag);
	}

	/**
	 * Return corresponding key of ImageFile for use in log.history.
	 * 
	 * @return key for this ImageFile
	 */
	public String getKey() {
		String locStr = this.location.toString();
		int index = locStr.lastIndexOf('/');
		int index1 = locStr.lastIndexOf('.');

		return locStr.substring(0, index + 1) + this.name
				+ locStr.substring(index1);
	}

	/**
	 * Return whether this ImageFile is equal to Object obj.
	 *
	 * @return whether this ImageFile is equal to Object obj.
	 */
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		ImageFile other = (ImageFile) obj;
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		if (this.location != other.location) {
			return false;
		}
		return true;
	}

	/**
	 * Return a String representation of this ImageFile of this form:
	 * 
	 * "name has num_tag Tags: [All Tags]; location = File location"
	 * 
	 * @return a String representation of this ImageFile
	 */
	@Override
	public String toString() {
		return name + ": " + tags + "; location: " + location;
	}
}