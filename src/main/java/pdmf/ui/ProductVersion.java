package pdmf.ui;

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
import org.eclipse.swt.widgets.Tree;
import org.eclipse.swt.widgets.TreeItem;

import pdmf.Main;
import pdmf.model.CstI18N;
import pdmf.model.ProductKey;
import pdmf.model.ProductRec;
import pdmf.model.User;
import pdmf.service.ProductService;

public class ProductVersion extends Dialog {

	private ProductService productService = new ProductService();

	public static final int DESCRIPTION_TEXT_X = 127;
	public static final int DESCRIPTION_TEXT_Y = 33;
	public static final int DESCRIPTION_TEXT_WIDTH = 303;
	public static final int DESCRIPTION_TEXT_HEIGHT = 228;

	protected Object result;
	protected Shell shell;
	private Text product;
	private Button btnCreateNewVersion = null;
	private Label lblInfo = null;
	private StyledText description;
	private ProductRec selectedVersion = null;
	private Tree productTree;
	private Text newVersion;

	private User currentUser;

	/**
	 * Create the dialog.
	 * 
	 * @param parent
	 * @param style
	 */
	public ProductVersion(Shell parent, int style) {
		super(parent, style);
		String PRODUCT = Main.cst(CstI18N.PRODUCT);
		setText(PRODUCT);
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
		shell = new Shell(getParent(), SWT.DIALOG_TRIM | SWT.APPLICATION_MODAL);

		shell.setSize(496, 402);
		shell.setText(getText());
		shell.setLayout(null);

		Label lblProduct = new Label(shell, SWT.NONE);
		lblProduct.setBounds(10, 10, 115, 25);
		String PRODUCT = Main.cst(CstI18N.PRODUCT);
		lblProduct.setText(PRODUCT);
		shell.setText(getText() + " " + currentUser.getCurrentTenant().description);

		Label lblDescription = new Label(shell, SWT.NONE);
		lblDescription.setBounds(125, 10, 143, 25);
		String DESCRIPTION = Main.cst(CstI18N.DESCRIPTION);
		lblDescription.setText(DESCRIPTION);

		product = new Text(shell, SWT.BORDER);
		product.setBounds(10, 48, 115, 25);

		description = new StyledText(shell, SWT.V_SCROLL | SWT.WRAP | SWT.BORDER);
		description.setBounds(125, 48, 181, 227);
		description.setTextLimit(995);

		btnCreateNewVersion = new Button(shell, SWT.NONE);
		btnCreateNewVersion.setBounds(354, 46, 120, 30);
		btnCreateNewVersion.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {

				String tenantId = currentUser.getCurrentTenant().key.tenantid;
				String wrkProductName = product.getText();
				if (wrkProductName == null || wrkProductName.trim().length() < 1) {
					String NO_PRODUCT_SELECTED = Main.cst(CstI18N.NO_PRODUCT_SELECTED);
					lblInfo.setText(NO_PRODUCT_SELECTED);
					return;
				}
				ProductRec versionRec = getSelectedVersion();
				if (versionRec == null) {
					String VERSION_MUST_BE_SELECTED = Main.cst(CstI18N.VERSION_MUST_BE_SELECTED);
					lblInfo.setText(VERSION_MUST_BE_SELECTED);
				} else {
					lblInfo.setText("");
				}
				
				if(ProductService.isLocked(tenantId, versionRec.key.version, versionRec.key.productName)){
					String THIS_VERSION_IS_LOCKED = Main.cst(CstI18N.THIS_VERSION_IS_LOCKED);
					lblInfo.setText(THIS_VERSION_IS_LOCKED);	
					return;
				}
				
				String toVersionStr = newVersion.getText();
				if (toVersionStr == null || toVersionStr.trim().length() < 1) {
					String TARGET_VERSION_MUST_HAVE_A_VALUE = Main.cst(CstI18N.TARGET_VERSION_MUST_HAVE_A_VALUE);
					lblInfo.setText(TARGET_VERSION_MUST_HAVE_A_VALUE);
					newVersion.setFocus();
					return;
				} else {
					lblInfo.setText("");
				}
				Integer toVersion = null;
				try {
					toVersion = Integer.parseInt(toVersionStr);
				} catch (NumberFormatException nfe) {
					String TARGET_VERSION_MUST_BE_AN_INTEGER = Main.cst(CstI18N.TARGET_VERSION_MUST_BE_AN_INTEGER);
					lblInfo.setText(TARGET_VERSION_MUST_BE_AN_INTEGER);
					newVersion.setFocus();
					return;
				}

				if (versionRec == null) {
					String SELECT_A_VERSION = Main.cst(CstI18N.SELECT_A_VERSION);
					lblInfo.setText(SELECT_A_VERSION + wrkProductName);
					newVersion.setFocus();
					return;
				}

				// old product new version
				Integer fromVersion = versionRec.key.version;
				if (fromVersion >= toVersion) {
					String TARGET_VERSION_MUST_BE_BIGGER = Main.cst(CstI18N.TARGET_VERSION_MUST_BE_BIGGER);
					lblInfo.setText(TARGET_VERSION_MUST_BE_BIGGER);
					productTree.setFocus();
					return;
				}

				ProductKey key = new ProductKey(tenantId, fromVersion, wrkProductName);
				ProductRec rec = productService.get(key);
				if (rec == null) {
					product.setData(null);
					String DOES_NOT_EXIST = Main.cst(CstI18N.DOES_NOT_EXIST);
					lblInfo.setText(wrkProductName + " " + fromVersion + " " + DOES_NOT_EXIST);
					return;
				} else {
					key.version = toVersion;
					ProductRec recToVersion = productService.get(key);
					if (recToVersion != null) {
						product.setData(null);
						String ALREADY_EXISTS = Main.cst(CstI18N.ALREADY_EXISTS);
						lblInfo.setText(wrkProductName + " " + toVersion + " " + ALREADY_EXISTS);
						return;
					}
					productService.createNewVersion(tenantId, fromVersion, toVersion, wrkProductName, currentUser.userId);
					newVersion.setText("");
				}
				refreshProductTree();
				clearForm();
				lblInfo.setText("");
			}
		});
		String NEW_VERSION = Main.cst(CstI18N.NEW_VERSION);
		btnCreateNewVersion.setText(NEW_VERSION);

		lblInfo = new Label(shell, SWT.NONE);
		lblInfo.setBounds(10, 287, 458, 25);
		lblInfo.setText("info");

		productTree = new Tree(shell, SWT.SINGLE | SWT.BORDER);
		productTree.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {

				String tenantId = currentUser.getCurrentTenant().key.tenantid;
				ProductRec versionRec = (ProductRec) e.item.getData();
				if (versionRec != null) {
					selectedVersion = versionRec;
				} else {
					String prod = "";
					if (e.item instanceof TreeItem) {
						TreeItem ti = (TreeItem) e.item;
						prod = ti.getText();
					}
					selectedVersion = null;
					product.setText(prod);
					description.setText("");
					lblInfo.setText("");
					String VERSION = Main.cst(CstI18N.VERSION);
					shell.setText(VERSION);
					return;
				}

				ProductKey key = new ProductKey(tenantId, selectedVersion.key.version, selectedVersion.key.productName);
				ProductRec dbRec = productService.get(key);
				if (dbRec != null) {
					product.setText(selectedVersion.key.productName);
					product.setData(dbRec.chgnbr);
					description.setText(dbRec.description == null ? "" : dbRec.description);
					shell.setText("Version: " + selectedVersion.key.productName + " " + selectedVersion.description + " ver. " + selectedVersion.key.version);
				} else {
					product.setData(null);
					refreshProductTree();
				}
			}
		});
		productTree.setBounds(10, 73, 115, 202);

		newVersion = new Text(shell, SWT.BORDER);
		newVersion.setBounds(312, 46, 36, 23);

		Button btnNyProduct = new Button(shell, SWT.NONE);
		btnNyProduct.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {

				String tenantId = currentUser.getCurrentTenant().key.tenantid;
				String wrkProductName = product.getText();
				if (wrkProductName == null || wrkProductName.trim().length() < 1) {
					String PRODUCTNAME_MUST_HAVE_A_VALUE = Main.cst(CstI18N.PRODUCTNAME_MUST_HAVE_A_VALUE);
					lblInfo.setText(PRODUCTNAME_MUST_HAVE_A_VALUE);
					return;
				}

				Integer firstVersion = productService.getFirstVersionForProduct(tenantId, wrkProductName);
				if (firstVersion != null) {
					String ALREADY_EXISTS = Main.cst(CstI18N.ALREADY_EXISTS);
					lblInfo.setText(wrkProductName + " " + ALREADY_EXISTS);
					return;
				}

				Integer toVersion = 0;

				ProductKey key = new ProductKey(tenantId, toVersion, wrkProductName);
				ProductRec rec = productService.get(key);
				if (rec != null) {
					product.setData(null);
					String ALREADY_EXISTS = Main.cst(CstI18N.ALREADY_EXISTS);
					lblInfo.setText(wrkProductName + " " + ALREADY_EXISTS);
					return;
				} else {
					String wrkDescription = description.getText();
					rec = new ProductRec(key, null, null, null);
					rec.description = wrkDescription;
					rec.status = "wrk";
					productService.insert(rec, currentUser.userId);
				}
				newVersion.setText("");
				refreshProductTree();
				clearForm();
				lblInfo.setText("");

			}
		});
		btnNyProduct.setBounds(354, 81, 120, 30);
		String NEW_PRODUCT = Main.cst(CstI18N.NEW_PRODUCT);
		btnNyProduct.setText(NEW_PRODUCT);

		Label lblVer = new Label(shell, SWT.NONE);
		lblVer.setBounds(312, 10, 36, 25);
		lblVer.setText("Ver");

		refreshProductTree();
	}

	private void refreshProductTree() {
		selectedVersion = null;
		productTree.removeAll();

		String tenantId = currentUser.getCurrentTenant().key.tenantid;

		java.util.List<String> products = productService.list(tenantId);
		int idx = 0;
		for (String productName : products) {
			TreeItem treeItem = new TreeItem(productTree, SWT.NONE, idx);
			treeItem.setText(productName);
			java.util.List<ProductRec> versions = productService.list(tenantId, productName);
			int idx2 = 0;
			for (ProductRec versionRec : versions) {
				TreeItem versionItem = new TreeItem(treeItem, SWT.NONE, idx2);
				versionItem.setText(String.valueOf(versionRec.key.version) + "," + versionRec.status);
				versionItem.setData(versionRec);
				idx2++;
			}
			idx++;
		}
	}

	ProductRec getSelectedVersion() {
		return selectedVersion;
	}

	private void clearForm() {
		description.setText("");
		product.setText("");
		product.setData(null);
	}

	public void setKey(ProductKey rec) {
	}

	public void setCurrentUser(User user) {
		currentUser = user;
	}

}
