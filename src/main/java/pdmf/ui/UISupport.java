package pdmf.ui;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Set;

import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.StyleRange;
import org.eclipse.swt.custom.StyledText;
import org.eclipse.swt.widgets.Shell;

public class UISupport {

	public static void handleSearchWords(Shell shell, StyledText styledText, Set<String> searchWords) {

		String theText = styledText.getText();
		if (theText == null || theText.trim().length() < 1)
			return;

		List<StyleRange> styleRanges = new ArrayList<>();

		for (String searchWord : searchWords) {

			int pos = theText.indexOf(searchWord);
			if (pos < 0) {
				continue;
			}
			int wordLen = searchWord.length();
			StyleRange sr = new StyleRange();
			sr.start = pos;
			sr.length = wordLen;
			sr.foreground = shell.getDisplay().getSystemColor(SWT.COLOR_BLUE);
			sr.background = shell.getDisplay().getSystemColor(SWT.COLOR_YELLOW);
			sr.fontStyle = SWT.BOLD;
			styleRanges.add(sr);
		}

		styleRanges.sort(new Comparator<StyleRange>() {
			@Override
			public int compare(StyleRange m1, StyleRange m2) {
				if (m1.start == m2.start) {
					return 0;
				}
				if (m1.start > m2.start)
					return 1;
				else
					return -1;
			}
		});

		StyleRange[] array = new StyleRange[styleRanges.size()];
		styleRanges.toArray(array);
		styledText.setStyleRanges(array);

	}

}
