package photo_renamer;

import java.io.File;
import java.util.ArrayList;

/**
 * The Class containing all methods that interact with all information outside
 * the package.
 * 
 * @authors Jaryd Hunter and Runhao Liang
 */
public class FileManager {

	/** A List of all possible extensions of image files. */
	public static final String[] EXTENSIONS = { "jpg", "gif", "jpeg", "png",
			"bmp", "JPG" };

	/**
	 * Return ArrayList of all images under directory file, and store them as
	 * ImageFile in ImageLog.
	 * 
	 * @param file
	 *            directory to search under, or an image.
	 * @param log
	 *            ImageLog to save all imported information.
	 * @return all images under directory file
	 */
	public ArrayList<ImageFile> getImages(File file, ImageLog log) {
		ArrayList<ImageFile> al = new ArrayList<ImageFile>();
		// ArrayList of all files directly under file.
		File[] list_files = file.listFiles();

		for (int i = 0; i < list_files.length; i++) {

			for (final String ext : EXTENSIONS) {

				// Check if the file is an image file.
				if (list_files[i].getName().endsWith("." + ext)) {

					al.add(createImageFile(log, list_files[i]));
				}
			}
			if (list_files[i].isDirectory()) {

				// Keep searching in the sub-directories.
				al.addAll(getImages(list_files[i], log));
			}
		}
		return al;
	}

	/**
	 * Create a new ImageFile and put it in a specific ImageLog.
	 * 
	 * @param log
	 *            ImageLog to add ImageFile to
	 * @param file
	 *            File to create ImageFile from
	 * @return
	 */
	private ImageFile createImageFile(ImageLog log, File file) {
		String name = file.getName();
		int index = name.indexOf('@');
		String newname;
		ArrayList<String> tags = new ArrayList<String>();
		int index1 = name.lastIndexOf('.');
		// Check if there are tags in the name of file.
		if (index == -1) {

			// Because there are no tags we can save the name
			// directly.
			newname = name.substring(0, index1);
		} else {

			newname = name.substring(0, index);
			// Gets the tags present in name
			String tagsinname = name.substring(index, index1);
			while (tagsinname.length() > 1) {

				int index3 = tagsinname.lastIndexOf("@");
				// Adds last tag to ArrayList of tags for this
				// ImageFile.
				tags.add(tagsinname.substring(index3));
				// Adds tag to ImageLog allTags if importing a tag
				// not seen before.
				if (!log.getAllTags().contains(tagsinname.substring(index3))) {

					log.addTag(tagsinname.substring(index3));
				}
				tagsinname = tagsinname.substring(0, index3);
			}
		}
		ImageFile image = new ImageFile(newname, tags, file);
		return image;
	}

	/**
	 * Change the corresponding image's path to have tag(s) in its name.
	 * 
	 * @param old
	 *            ImageFile that's name needs to be changed
	 * @param tag
	 *            Tag that needs to be added
	 */
	public void changeName(ImageFile old, String tag) {
		String location = old.getLocation().toString();
		// Get extension.
		String extension = "";
		int i = location.lastIndexOf('.');
		if (i > 0) {
			extension = location.substring(i + 1);
		}

		// Get directory.
		String[] splitLocation = location.split(old.getName());
		// Put it all together.
		String newName = splitLocation[0] + old.getName() + tag + "."
				+ extension;

		// Move actual file using File object method.
		File fresh = new File(newName);
		old.getLocation().renameTo(fresh);

		// Change ImageFile reference to new File object.
		old.setLocation(fresh);
	}
}
