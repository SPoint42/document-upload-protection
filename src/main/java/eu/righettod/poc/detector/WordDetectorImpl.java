package eu.righettod.poc.detector;

import java.io.File;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;

import com.aspose.words.Document;
import com.aspose.words.FileFormatInfo;
import com.aspose.words.FileFormatUtil;

/**
 * Implementation of the detector for Microsoft Word document.
 * 
 *
 */
public class WordDetectorImpl implements Detector {

	/**
	 * List of allowed Word format (WML = Word ML (Word 2003 XML)).<br>
	 * Allow also DOCM because it can exists without macro inside.<br>
	 * Allow also DOT/DOTM because both can exists without macro inside.<br>
	 * We reject MHTML file because:<br>
	 * <ul>
	 * <li>API cannot detect macro into this format</li>
	 * <li>Is not normal to use this format to represent a Word file (there plenty of others supported format)</li>
	 * </ul>
	 */
	private static final List<String> ALLOWED_FORMAT = Arrays.asList(new String[] { "doc", "docx", "docm", "wml", "dot", "dotm" });

	/**
	 * {@inheritDoc}
	 *
	 * @see eu.righettod.poc.detector.Detector#isSafe(java.io.File)
	 */
	@Override
	public boolean isSafe(File f) {
		boolean safeState = false;
		try {
			if ((f != null) && f.exists() && f.canRead()) {
				// Perform a first check on Word document format
				FileFormatInfo formatInfo = FileFormatUtil.detectFileFormat(f.getAbsolutePath());
				String formatExtension = FileFormatUtil.loadFormatToExtension(formatInfo.getLoadFormat());
				if ((formatExtension != null) && ALLOWED_FORMAT.contains(formatExtension.toLowerCase(Locale.US).replaceAll("\\.", ""))) {
					// Load the file into the Word document parser
					Document document = new Document(f.getAbsolutePath());
					// Get safe state from Macro presence
					safeState = !document.hasMacros();
				}
			}
		}
		catch (Exception e) {
			safeState = false;
			// Not clean way of logging but it's a POC :)
			e.printStackTrace();
		}
		return safeState;
	}

}
