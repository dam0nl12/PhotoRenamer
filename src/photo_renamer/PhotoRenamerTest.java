/**
 * 
 */
package photo_renamer;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.Arrays;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 * @author Jaryd Hunter and Runhao Liang
 *
 */
public class PhotoRenamerTest {
	/** A new FileManager for tests. */
	private FileManager fm = new FileManager();
	/** A new ImageLog for tests. */
	private ImageLog log = ImageLog.INSTANCE;
	/** A new Photo Renamer for tests. */
	private PhotoRenamer pr = new PhotoRenamer(log, fm);
	/** A empty ArraryList of tags for tests. */
	private ArrayList<String> tags = new ArrayList<String>();

	/**
	 * @throws java.lang.Exception
	 */
	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
	}

	/**
	 * @throws java.lang.Exception
	 */
	@AfterClass
	public static void tearDownAfterClass() throws Exception {
	}

	/**
	 * @throws java.lang.Exception
	 */
	@Before
	public void setUp() throws Exception {
	}

	/**
	 * @throws java.lang.Exception
	 */
	@After
	public void tearDown() throws Exception {
	}

	/**
	 * Test method with two different tags
	 * {@link photo_renamer.PhotoRenamer#addNewTag(photo_renamer.ImageLog, java.lang.String)}.
	 */
	@Test
	public void addNewTagTest1() {
		String newtag = new String("@jd");
		String newtag2 = new String("@123");
		pr.addNewTag(log, newtag);
		pr.addNewTag(log, newtag2);
		tags.add(newtag);
		tags.add(newtag2);

		assertTrue(pr.getLog().getAllTags().containsAll(tags));
	}

	/**
	 * Test method with two duplicate tags
	 * {@link photo_renamer.PhotoRenamer#addNewTag(photo_renamer.ImageLog, java.lang.String)}.
	 */
	@Test
	public void addNewTagTest2() {
		String newtag3 = new String("@456");
		Integer lengthBefore = new Integer(pr.getLog().getAllTags().size());
		pr.addNewTag(pr.getLog(), newtag3);
		pr.addNewTag(pr.getLog(), newtag3);

		assertTrue(pr.getLog().getAllTags().size() - lengthBefore == 1);
	}

	/**
	 * Test method with two different tags at the same time
	 * {@link photo_renamer.PhotoRenamer#addNewTag(photo_renamer.ImageLog, java.lang.String)}.
	 */
	@Test
	public void addNewTagTest3() {
		String newtag4 = new String("@789");
		String newtag5 = new String("@abc");
		pr.addNewTag(pr.getLog(), newtag4 + newtag5);
		ArrayList<String> tags = new ArrayList<String>(
				Arrays.asList("@789", "@abc"));

		assertTrue(pr.getLog().getAllTags().containsAll(tags));
	}

	/**
	 * Test method with the empty String
	 * {@link photo_renamer.PhotoRenamer#addNewTag(photo_renamer.ImageLog, java.lang.String)}.
	 */
	@Test
	public void addNewTagTest4() {

		Integer lengthBefore = new Integer(pr.getLog().getAllTags().size());
		pr.addNewTag(pr.getLog(), "");

		assertTrue(lengthBefore - pr.getLog().getAllTags().size() == 0);
	}

	/**
	 * Test method with non empty String but no actual tags
	 * {@link photo_renamer.PhotoRenamer#addNewTag(photo_renamer.ImageLog, java.lang.String)}.
	 */
	@Test
	public void addNewTagTest5() {

		Integer lengthBefore = new Integer(pr.getLog().getAllTags().size());
		pr.addNewTag(pr.getLog(), "@");
		pr.addNewTag(pr.getLog(), "@@");

		assertTrue(lengthBefore - pr.getLog().getAllTags().size() == 0);
	}

	/**
	 * Test method removing one tag from list of two different tags
	 * {@link photo_renamer.PhotoRenamer#removeOldTag(photo_renamer.ImageLog, java.lang.String, photo_renamer.FileManager)}.
	 */
	@Test
	public void removeOldTagTest1() {
		String newtag6 = new String("@def");
		String newtag7 = new String("@ghi");
		pr.addNewTag(log, newtag6);
		pr.addNewTag(log, newtag7);
		pr.removeOldTag(log, newtag6, fm);

		assertTrue(!pr.getLog().getAllTags().contains(newtag6)
				&& pr.getLog().getAllTags().contains(newtag7));
	}

	/**
	 * Test method removing empty tag
	 * {@link photo_renamer.PhotoRenamer#removeOldTag(photo_renamer.ImageLog, java.lang.String, photo_renamer.FileManager)}.
	 */
	@Test
	public void removeOldTagTest2() {
		ArrayList<String> tagsbefore = pr.getLog().getAllTags();
		pr.removeOldTag(log, "", fm);

		assertEquals(tagsbefore, pr.getLog().getAllTags());
	}

}
