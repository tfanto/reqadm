package pdmf.ui;

import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.HashSet;
import java.util.Set;

import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.StyledText;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Dialog;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;

import pdmf.model.Cst;
import pdmf.model.ProductKey;
import pdmf.model.ProductRec;
import pdmf.model.User;
import pdmf.service.ProductService;
import pdmf.sys.RecordChangedByAnotherUser;

public class Product extends Dialog {

	private static final String UPDATE_MODE = "";
	private String mode = null;

	private User currentUser;

	private ProductService productService = new ProductService();

	protected Object result;
	protected Shell shell;
	private Text product;
	private Text version;
	private Button btnStore = null;
	private Button btnRemove = null;
	private Label lblInfo = null;
	private StyledText description;

	private Integer chgnbr = null;
	private Label lblProduct;

	private StyledText shortDescription;
	private Label lblShortDescription;
	private Label crtDat;
	private Label chgDat;

	private String p = null;
	private Integer v = null;

	private Set<String> searchWords = new HashSet<String>();

	/**
	 * Create the dialog.
	 * 
	 * @param parent
	 * @param style
	 */
	public Product(Shell parent, int style) {
		super(parent, style);
		setText("[" + Cst.PRODUCT + "]");
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
//		shell = new Shell(getParent(), SWT.DIALOG_TRIM | SWT.APPLICATION_MODAL);
		shell = new Shell(getParent(), getStyle());
		shell.setSize(360, 560);
		shell.setText(getText() + " " + mode);
		shell.setLayout(null);

		lblProduct = new Label(shell, SWT.NONE);
		lblProduct.setText(Cst.PRODUCT);
		lblProduct.setBounds(10, 10, 82, 15);

		Label lblDescription = new Label(shell, SWT.NONE);
		lblDescription.setBounds(10, 165, 151, 15);
		lblDescription.setText(Cst.DESCRIPTION);

		product = new Text(shell, SWT.BORDER);
		product.setBounds(10, 32, 197, 25);

		description = new StyledText(shell, SWT.V_SCROLL | SWT.WRAP | SWT.BORDER);
		description.setBounds(10, 186, 239, 227);
		description.setTextLimit(995);

		btnStore = new Button(shell, SWT.NONE);
		btnStore.setBounds(255, 32, 80, 25);
		btnStore.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {

				Integer tenantId = currentUser.getCurrentTenant().getId();

				String wrkProductName = (String) product.getText();
				if (wrkProductName == null || wrkProductName.trim().length() < 1) {
					lblInfo.setText(Cst.NO_PRODUCT_SELECTED);
					return;
				}
				String wrkVersion = (String) version.getText();
				if (wrkVersion == null || wrkVersion.trim().length() < 1) {
					lblInfo.setText(Cst.NO_VERSION_SELECTED);
					return;
				}
				Integer ver = null;
				try {
					ver = Integer.parseInt(wrkVersion);
				} catch (NumberFormatException nfe) {
					lblInfo.setText(Cst.VERSION_NOT_NUMERIC);
					return;
				}

				ProductKey key = new ProductKey(tenantId, ver, wrkProductName);
				if (ProductService.isLocked(tenantId, ver, wrkProductName)) {
					lblInfo.setText(Cst.VERSION_LOCKED);
					return;
				}
				if (productService.isDeleteMarked(key)) {
					lblInfo.setText(Cst.ALREADY_DELETE_NO_ACTION);
					return;
				}

				String wrkDescription = description.getText() == null ? "" : description.getText();
				String wrkShortDescription = shortDescription.getText() == null ? "" : shortDescription.getText();

				ProductRec rec = productService.get(tenantId, ver, wrkProductName);

				if (rec == null) {
					// rec = new ProductRec(key, null, null, null);
					// rec.shortdescr = wrkShortDescription;
					// rec.description = wrkDescription;
					// productService.insert(rec, userId);
					// chgnbr = null;
					// result = 1;
					shell.dispose();
					return;
				} else {
					if (chgnbr != null) {
						rec.chgnbr = chgnbr;
					}
				}

				try {
					rec.shortdescr = wrkShortDescription;
					rec.description = wrkDescription;
					productService.store(rec, currentUser.userId);
					chgnbr = null;
					result = 1;
					shell.dispose();
				} catch (RecordChangedByAnotherUser rc) {
					lblInfo.setText(Cst.RECORD_CHANGED_BY_ANOTHER_USER);
				}
			}

		});
		btnStore.setText(Cst.STORE);

		btnRemove = new Button(shell, SWT.NONE);
		btnRemove.setBounds(255, 63, 80, 25);
		btnRemove.addSelectionListener(new SelectionAdapter() {

			@Override
			public void widgetSelected(SelectionEvent e) {

				Integer tenantId = currentUser.getCurrentTenant().getId();

				String wrkProductName = (String) product.getText();
				if (wrkProductName == null || wrkProductName.trim().length() < 1) {
					lblInfo.setText(Cst.NO_PRODUCT_SELECTED);
					return;
				}

				String wrkVersion = (String) version.getText();
				if (wrkVersion == null || wrkVersion.trim().length() < 1) {
					lblInfo.setText(Cst.NO_VERSION_SELECTED);
					return;
				}
				Integer ver = null;
				try {
					ver = Integer.parseInt(wrkVersion);
				} catch (NumberFormatException nfe) {
					lblInfo.setText(Cst.VERSION_NOT_NUMERIC);
					return;
				}

				ProductKey key = new ProductKey(tenantId, ver, wrkProductName);
				if (productService.isDeleteMarked(key)) {
					lblInfo.setText(Cst.ALREADY_DELETE_NO_ACTION);
					return;
				}

				if (ProductService.isLocked(tenantId, ver, wrkProductName)) {
					lblInfo.setText(Cst.VERSION_LOCKED);
					return;
				}

				btnRemove.setEnabled(true);
				lblInfo.setText("");
				try {
					productService.remove(tenantId, ver, wrkProductName, currentUser.userId);
					result = 1;
					shell.dispose();
				} catch (Exception ee) {

				}
			}
		});
		btnRemove.setText(Cst.REMOVE);

		lblInfo = new Label(shell, SWT.BORDER | SWT.SHADOW_IN);
		lblInfo.setText("i");
		lblInfo.setBounds(10, 484, 325, 25);

		crtDat = new Label(shell, SWT.NONE);
		crtDat.setText("crt");
		crtDat.setBounds(10, 422, 325, 25);

		chgDat = new Label(shell, SWT.NONE);
		chgDat.setText("chg");
		chgDat.setBounds(10, 453, 325, 25);

		shortDescription = new StyledText(shell, SWT.V_SCROLL | SWT.BORDER | SWT.WRAP);
		shortDescription.setTextLimit(100);
		shortDescription.setBounds(10, 86, 239, 73);

		lblShortDescription = new Label(shell, SWT.NONE);
		lblShortDescription.setText(Cst.DESCRIPTION_SHORT);
		lblShortDescription.setBounds(10, 63, 151, 15);

		version = new Text(shell, SWT.BORDER);
		version.setBounds(213, 32, 36, 25);

		Label lblNewLabel = new Label(shell, SWT.NONE);
		lblNewLabel.setBounds(213, 10, 55, 15);
		lblNewLabel.setText(Cst.VERSION);

		product.setEditable(false);
		version.setEditable(false);
		product.setText(p);
		version.setText(v.toString());

		Integer tenantId = currentUser.getCurrentTenant().getId();

		lblInfo.setText("");
		crtDat.setText("");
		chgDat.setText("");
		ProductRec rec = productService.get(tenantId, v, p);
		if (rec != null) {
			shortDescription.setText(rec.shortdescr == null ? "" : rec.shortdescr);
			description.setText(rec.description == null ? "" : rec.description);
			chgnbr = rec.chgnbr;
			handleInfo(rec.crtdat, rec.crtusr, rec.chgdat, rec.chgusr, rec.dltdat, rec.dltusr, rec.crtver);
			UISupport.handleSearchWords(shell, description, searchWords);
			UISupport.handleSearchWords(shell, shortDescription, searchWords);
		}
		btnRemove.setEnabled(true);
		btnRemove.setVisible(true);

	}

	private void handleInfo(Instant createDate, String createUser, Instant changeDate, String chgusr,
			Instant deleteDate, String deleteUser, Integer createdInVersion) {

		LocalDate created = LocalDateTime.ofInstant(createDate, ZoneOffset.UTC).toLocalDate();
		crtDat.setText("Skapad: " + created.toString() + " av " + createUser + " i version: " + createdInVersion);

		chgDat.setText("");
		if (changeDate != null && chgusr != null) {
			LocalDate changed = LocalDateTime.ofInstant(changeDate, ZoneOffset.UTC).toLocalDate();
			chgDat.setText("Ändrad: " + changed.toString() + " av " + chgusr);
		}
		if (deleteDate != null && deleteUser != null) {
			LocalDate deleted = LocalDateTime.ofInstant(deleteDate, ZoneOffset.UTC).toLocalDate();
			chgDat.setText("Borttagen: " + deleted.toString() + " av " + deleteUser);
		}

	}

	public void setKey(ProductKey rec) {
		mode = UPDATE_MODE;
		this.v = rec.version;
		this.p = rec.productName;
		searchWords.clear();
	}

	public void setCurrentUser(User user) {
		currentUser = user;
	}

	public void setSearchWords(Set<String> searchWords) {
		this.searchWords.addAll(searchWords);
	}

}
