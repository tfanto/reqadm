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

import pdmf.Main;
import pdmf.model.Cst2;
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
		String PRODUCT = Main.cst(Cst2.PRODUCT);
		setText("[" + PRODUCT + "]");
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
		shell.setSize(572, 622);
		shell.setText(getText() + " " + mode + " " + currentUser.getCurrentTenant().description);
		shell.setLayout(null);

		lblProduct = new Label(shell, SWT.NONE);
		String PRODUCT = Main.cst(Cst2.PRODUCT);
		lblProduct.setText(PRODUCT);
		lblProduct.setBounds(10, 10, 82, 25);

		Label lblDescription = new Label(shell, SWT.NONE);
		lblDescription.setBounds(10, 183, 151, 25);
		String DESCRIPTION = Main.cst(Cst2.DESCRIPTION);
		lblDescription.setText(DESCRIPTION);

		product = new Text(shell, SWT.BORDER);
		product.setBounds(10, 42, 197, 25);

		description = new StyledText(shell, SWT.V_SCROLL | SWT.WRAP | SWT.BORDER);
		description.setBounds(10, 214, 525, 246);
		description.setTextLimit(995);

		btnStore = new Button(shell, SWT.NONE);
		btnStore.setBounds(455, 42, 80, 25);
		btnStore.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {

				String tenantId = currentUser.getCurrentTenant().key.tenantid;

				String wrkProductName = (String) product.getText();
				if (wrkProductName == null || wrkProductName.trim().length() < 1) {
					String NO_PRODUCT_SELECTED = Main.cst(Cst2.NO_PRODUCT_SELECTED);
					lblInfo.setText(NO_PRODUCT_SELECTED);
					return;
				}
				String wrkVersion = (String) version.getText();
				if (wrkVersion == null || wrkVersion.trim().length() < 1) {
					String NO_VERSION_SELECTED = Main.cst(Cst2.NO_VERSION_SELECTED);
					lblInfo.setText(NO_VERSION_SELECTED);
					return;
				}
				Integer ver = null;
				try {
					ver = Integer.parseInt(wrkVersion);
				} catch (NumberFormatException nfe) {
					String VERSION_NOT_NUMERIC = Main.cst(Cst2.VERSION_NOT_NUMERIC);
					lblInfo.setText(VERSION_NOT_NUMERIC);
					return;
				}

				ProductKey key = new ProductKey(tenantId, ver, wrkProductName);
				if (ProductService.isLocked(tenantId, ver, wrkProductName)) {
					String VERSION_LOCKED = Main.cst(Cst2.VERSION_LOCKED);
					lblInfo.setText(VERSION_LOCKED);
					return;
				}
				if (productService.isDeleteMarked(key)) {
					String ALREADY_DELETE_NO_ACTION = Main.cst(Cst2.ALREADY_DELETE_NO_ACTION);
					lblInfo.setText(ALREADY_DELETE_NO_ACTION);
					return;
				}

				String wrkDescription = description.getText() == null ? "" : description.getText();
				String wrkShortDescription = shortDescription.getText() == null ? "" : shortDescription.getText();

				ProductRec rec = productService.get(key);

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
					String RECORD_CHANGED_BY_ANOTHER_USER = Main.cst(Cst2.RECORD_CHANGED_BY_ANOTHER_USER);
					lblInfo.setText(RECORD_CHANGED_BY_ANOTHER_USER);
				}
			}

		});
		String STORE = Main.cst(Cst2.STORE);
		btnStore.setText(STORE);

		btnRemove = new Button(shell, SWT.NONE);
		btnRemove.setBounds(455, 73, 80, 25);
		btnRemove.addSelectionListener(new SelectionAdapter() {

			@Override
			public void widgetSelected(SelectionEvent e) {

				String tenantId = currentUser.getCurrentTenant().key.tenantid;

				String wrkProductName = (String) product.getText();
				if (wrkProductName == null || wrkProductName.trim().length() < 1) {
					String NO_PRODUCT_SELECTED = Main.cst(Cst2.NO_PRODUCT_SELECTED);
					lblInfo.setText(NO_PRODUCT_SELECTED);
					return;
				}

				String wrkVersion = (String) version.getText();
				if (wrkVersion == null || wrkVersion.trim().length() < 1) {
					String NO_VERSION_SELECTED = Main.cst(Cst2.NO_VERSION_SELECTED);
					lblInfo.setText(NO_VERSION_SELECTED);
					return;
				}
				Integer ver = null;
				try {
					ver = Integer.parseInt(wrkVersion);
				} catch (NumberFormatException nfe) {
					String VERSION_NOT_NUMERIC = Main.cst(Cst2.VERSION_NOT_NUMERIC);
					lblInfo.setText(VERSION_NOT_NUMERIC);
					return;
				}

				ProductKey key = new ProductKey(tenantId, ver, wrkProductName);
				if (productService.isDeleteMarked(key)) {
					String ALREADY_DELETE_NO_ACTION = Main.cst(Cst2.ALREADY_DELETE_NO_ACTION);
					lblInfo.setText(ALREADY_DELETE_NO_ACTION);
					return;
				}

				if (ProductService.isLocked(tenantId, ver, wrkProductName)) {
					String VERSION_LOCKED = Main.cst(Cst2.VERSION_LOCKED);
					lblInfo.setText(VERSION_LOCKED);
					return;
				}

				btnRemove.setEnabled(true);
				lblInfo.setText("");
				try {
					productService.remove(key, currentUser.userId);
					result = 1;
					shell.dispose();
				} catch (Exception ee) {

				}
			}
		});
		String REMOVE = Main.cst(Cst2.REMOVE);
		btnRemove.setText(REMOVE);

		lblInfo = new Label(shell, SWT.BORDER | SWT.SHADOW_IN);
		lblInfo.setText("i");
		lblInfo.setBounds(10, 528, 525, 25);

		crtDat = new Label(shell, SWT.NONE);
		crtDat.setText("crt");
		crtDat.setBounds(10, 466, 525, 25);

		chgDat = new Label(shell, SWT.NONE);
		chgDat.setText("chg");
		chgDat.setBounds(10, 497, 525, 25);

		shortDescription = new StyledText(shell, SWT.V_SCROLL | SWT.BORDER | SWT.WRAP);
		shortDescription.setTextLimit(100);
		shortDescription.setBounds(10, 104, 525, 73);

		lblShortDescription = new Label(shell, SWT.NONE);
		String DESCRIPTION_SHORT = Main.cst(Cst2.DESCRIPTION_SHORT);
		lblShortDescription.setText(DESCRIPTION_SHORT);
		lblShortDescription.setBounds(10, 73, 151, 25);

		version = new Text(shell, SWT.BORDER);
		version.setBounds(213, 42, 36, 25);

		Label lblNewLabel = new Label(shell, SWT.NONE);
		lblNewLabel.setBounds(213, 10, 82, 25);
		String VERSION = Main.cst(Cst2.VERSION);
		lblNewLabel.setText(VERSION);

		product.setEditable(false);
		version.setEditable(false);
		product.setText(p);
		version.setText(v.toString());

		String tenantId = currentUser.getCurrentTenant().key.tenantid;

		lblInfo.setText("");
		crtDat.setText("");
		chgDat.setText("");
		ProductKey key = new ProductKey(tenantId, v, p);
		ProductRec rec = productService.get(key);
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

	private void handleInfo(Instant createDate, String createUser, Instant changeDate, String chgusr, Instant deleteDate, String deleteUser, Integer createdInVersion) {

		String CREATED = Main.cst(Cst2.CREATED);
		String CHANGED = Main.cst(Cst2.CHANGED);
		String REMOVED = Main.cst(Cst2.REMOVED);
		String BY = Main.cst(Cst2.BY);
		String IN_VERSION = Main.cst(Cst2.IN_VERSION);

		LocalDate created = LocalDateTime.ofInstant(createDate, ZoneOffset.UTC).toLocalDate();
		crtDat.setText(CREATED + created.toString() + BY + createUser + IN_VERSION + createdInVersion);

		chgDat.setText("");
		if (changeDate != null && chgusr != null) {
			LocalDate changed = LocalDateTime.ofInstant(changeDate, ZoneOffset.UTC).toLocalDate();
			chgDat.setText(CHANGED + changed.toString() + BY + chgusr);
		}
		if (deleteDate != null && deleteUser != null) {
			LocalDate deleted = LocalDateTime.ofInstant(deleteDate, ZoneOffset.UTC).toLocalDate();
			chgDat.setText(REMOVED + deleted.toString() + BY + deleteUser);
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
