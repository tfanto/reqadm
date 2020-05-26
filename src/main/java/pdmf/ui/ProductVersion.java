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

import pdmf.model.Cst;
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
		setText("Produkt");
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

		shell.setSize(496, 348);
		shell.setText(getText());
		shell.setLayout(null);

		Label lblProduct = new Label(shell, SWT.NONE);
		lblProduct.setBounds(10, 10, 115, 15);
		lblProduct.setText(Cst.PRODUCT);
		shell.setText(getText()  + " "  + currentUser.getCurrentTenant().description);
		

		Label lblDescription = new Label(shell, SWT.NONE);
		lblDescription.setBounds(125, 10, 143, 15);
		lblDescription.setText(Cst.DESCRIPTION);

		product = new Text(shell, SWT.BORDER);
		product.setBounds(10, 33, 115, 25);

		description = new StyledText(shell, SWT.V_SCROLL | SWT.WRAP | SWT.BORDER);
		description.setBounds(125, 33, 181, 227);
		description.setTextLimit(995);

		btnCreateNewVersion = new Button(shell, SWT.NONE);
		btnCreateNewVersion.setBounds(348, 30, 120, 25);
		btnCreateNewVersion.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {

				String tenantId = currentUser.getCurrentTenant().key.tenantid;
				String wrkProductName = product.getText();
				if (wrkProductName == null || wrkProductName.trim().length() < 1) {
					lblInfo.setText(Cst.VERSION_MUST_BE_SELECTED);
					return;
				}
				ProductRec versionRec = getSelectedVersion();
				if (versionRec == null) {
					lblInfo.setText(Cst.VERSION_MUST_BE_SELECTED);
				} else {
					lblInfo.setText("");
				}

				String toVersionStr = newVersion.getText();
				if (toVersionStr == null || toVersionStr.trim().length() < 1) {
					lblInfo.setText(Cst.TARGET_VERSION_MUST_HAVE_A_VALUE);
					newVersion.setFocus();
					return;
				} else {
					lblInfo.setText("");
				}
				Integer toVersion = null;
				try {
					toVersion = Integer.parseInt(toVersionStr);
				} catch (NumberFormatException nfe) {
					lblInfo.setText(Cst.TARGET_VERSION_MUST_BE_AN_INTEGER);
					newVersion.setFocus();
					return;
				}

				if (versionRec == null) {
					lblInfo.setText(Cst.SELECT_A_VERSION + wrkProductName);
					newVersion.setFocus();
					return;
				}

				// old product new version
				Integer fromVersion = versionRec.key.version;
				if (fromVersion >= toVersion) {
					lblInfo.setText(Cst.TARGET_VERSION_MUST_BE_BIGGER);
					productTree.setFocus();
					return;
				}

				ProductRec rec = productService.get(tenantId, fromVersion, wrkProductName);
				if (rec == null) {
					product.setData(null);
					lblInfo.setText(wrkProductName + " " + fromVersion + " does not exist");
					return;
				} else {
					ProductRec recToVersion = productService.get(tenantId, toVersion, wrkProductName);
					if (recToVersion != null) {
						product.setData(null);
						lblInfo.setText(wrkProductName + " " + toVersion + " already exists");
						return;
					}
					productService.createNewVersion(tenantId, fromVersion, toVersion, wrkProductName,
							currentUser.userId);
					newVersion.setText("");
				}
				refreshProductTree();
				clearForm();
				lblInfo.setText("");
			}
		});
		btnCreateNewVersion.setText("Ny version");

		lblInfo = new Label(shell, SWT.NONE);
		lblInfo.setBounds(10, 272, 458, 15);
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
					shell.setText("Version");
					return;
				}

				ProductRec dbRec = productService.get(tenantId, selectedVersion.key.version,
						selectedVersion.key.productName);
				if (dbRec != null) {
					product.setText(selectedVersion.key.productName);
					product.setData(dbRec.chgnbr);
					description.setText(dbRec.description == null ? "" : dbRec.description);
					shell.setText("Version: " + selectedVersion.key.productName + " " + selectedVersion.description
							+ " ver. " + selectedVersion.key.version);
				} else {
					product.setData(null);
					refreshProductTree();
				}
			}
		});
		productTree.setBounds(10, 60, 115, 200);

		newVersion = new Text(shell, SWT.BORDER);
		newVersion.setBounds(312, 31, 36, 23);

		Button btnNyProduct = new Button(shell, SWT.NONE);
		btnNyProduct.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {

				String tenantId = currentUser.getCurrentTenant().key.tenantid;
				String wrkProductName = product.getText();
				if (wrkProductName == null || wrkProductName.trim().length() < 1) {
					lblInfo.setText(Cst.PRODUCTNAME_MUST_HAVE_A_VALUE);
					return;
				}

				Integer firstVersion = productService.getFirstVersionForProduct(tenantId, wrkProductName);
				if (firstVersion != null) {
					lblInfo.setText(wrkProductName + " already exist");
					return;
				}

				Integer toVersion = 0;

				ProductRec rec = productService.get(tenantId, toVersion, wrkProductName);
				if (rec != null) {
					product.setData(null);
					lblInfo.setText(wrkProductName + " " + toVersion + " already exist");
					return;
				} else {
					String wrkDescription = description.getText();
					ProductKey key = new ProductKey(tenantId, toVersion, wrkProductName);
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
		btnNyProduct.setBounds(348, 65, 120, 25);
		btnNyProduct.setText("Ny Produkt");

		Label lblVer = new Label(shell, SWT.NONE);
		lblVer.setBounds(312, 10, 36, 15);
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
