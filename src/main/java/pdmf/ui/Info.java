package pdmf.ui;

import java.io.InputStream;

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.ImageData;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Canvas;
import org.eclipse.swt.widgets.Dialog;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;

import pdmf.Main;
import pdmf.model.CstI18N;

public class Info extends Dialog {

	protected Object result;
	protected Shell shell;

	/**
	 * Create the dialog.
	 * 
	 * @param parent
	 * @param style
	 */
	public Info(Shell parent, int style) {
		super(parent, style);
		String INFO = Main.cst(CstI18N.INFO);
		setText("[" + INFO + "]");
	}

	/**
	 * Open the dialog.
	 * 
	 * @return the result
	 */
	public Object open() {
		createContents();
		shell.open();
		shell.layout();
		Display display = getParent().getDisplay();
		while (!shell.isDisposed()) {
			if (!display.readAndDispatch()) {
				display.sleep();
			}
		}
		return result;
	}

	/**
	 * Create contents of the dialog.
	 */
	private void createContents() {
		shell = new Shell(getParent(), getStyle());
		shell.setSize(411, 528);
		shell.setText(getText());

		Label lblNewLabel = new Label(shell, SWT.NONE);
		lblNewLabel.setAlignment(SWT.CENTER);
		lblNewLabel.setBounds(21, 20, 374, 20);

		String PGM_NAME = Main.cst(CstI18N.PGM_NAME);

		lblNewLabel.setText(PGM_NAME);

		InputStream is = this.getClass().getClassLoader().getResourceAsStream("palais_fanto.jpg");
		ImageData imageData = new ImageData(is);
		final Image image = new Image(getParent().getDisplay(), imageData);
		Canvas canvas = new Canvas(shell, SWT.BORDER);
		canvas.setSize(0, 0);

		Button bildKnapp = new Button(shell, SWT.NONE);
		bildKnapp.setBounds(48, 60, 305, 415);
		bildKnapp.setImage(image);

	}
}
