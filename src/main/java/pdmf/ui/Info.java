package pdmf.ui;

import java.io.InputStream;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.PaintEvent;
import org.eclipse.swt.events.PaintListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.ImageData;
import org.eclipse.swt.graphics.RGB;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Canvas;
import org.eclipse.swt.widgets.Dialog;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;

import pdmf.model.Cst;

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
		setText("Info");
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
		shell.setSize(344, 260);
		shell.setText(getText());

		Label lblNewLabel = new Label(shell, SWT.NONE);
		lblNewLabel.setBounds(22, 21, 295, 15);
		lblNewLabel.setText(Cst.PGM_NAME);

		InputStream is = this.getClass().getClassLoader().getResourceAsStream("fanto.jpg");
		ImageData imageData = new ImageData(is);
		final Image image = new Image(getParent().getDisplay(), imageData);
		Canvas canvas = new Canvas(shell, SWT.BORDER);
		canvas.setSize(0, 0);
		
		Button bildKnapp = new Button(shell, SWT.NONE);
		bildKnapp.setBounds(106, 60, 123, 137);
		bildKnapp.setImage(image);

	}
}
