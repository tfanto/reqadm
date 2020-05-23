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
import pdmf.model.TenantKey;
import pdmf.model.TenantRec;
import pdmf.model.User;
import pdmf.service.TenantService;

public class Tenant extends Dialog {

	private TenantService tenantService = new TenantService();

	protected Object result;
	protected Shell shell;
	private Text tenant;
	private Label lblInfo = null;
	private StyledText description;
	private ProductRec selectedVersion = null;
	private Tree tenantTree;

	private User currentUser;

	/**
	 * Create the dialog.
	 * 
	 * @param parent
	 * @param style
	 */
	public Tenant(Shell parent, int style) {
		super(parent, style);
		setText("Tenant");
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

		Label lblTenant = new Label(shell, SWT.NONE);
		lblTenant.setBounds(10, 10, 115, 15);
		lblTenant.setText("Tenant");

		Label lblDescription = new Label(shell, SWT.NONE);
		lblDescription.setBounds(125, 10, 143, 15);
		lblDescription.setText(Cst.DESCRIPTION);

		tenant = new Text(shell, SWT.BORDER);
		tenant.setBounds(10, 33, 115, 25);

		description = new StyledText(shell, SWT.V_SCROLL | SWT.WRAP | SWT.BORDER);
		description.setBounds(125, 33, 181, 227);
		description.setTextLimit(995);

		lblInfo = new Label(shell, SWT.NONE);
		lblInfo.setBounds(10, 272, 458, 15);
		lblInfo.setText("info");

		tenantTree = new Tree(shell, SWT.SINGLE | SWT.BORDER);
		tenantTree.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {

				Integer tenantId = currentUser.getCurrentTenant().getId();
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
					tenant.setText(prod);
					description.setText("");
					lblInfo.setText("");
					shell.setText("Version");
					return;
				}

				ProductRec dbRec = tenantService.get(tenantId, selectedVersion.key.version,
						selectedVersion.key.productName);
				if (dbRec != null) {
					tenant.setText(selectedVersion.key.productName);
					tenant.setData(dbRec.chgnbr);
					description.setText(dbRec.description == null ? "" : dbRec.description);
					shell.setText("Version: " + selectedVersion.key.productName + " " + selectedVersion.description
							+ " ver. " + selectedVersion.key.version);
				} else {
					tenant.setData(null);
					refreshTenantTree();
				}
			}
		});
		tenantTree.setBounds(10, 58, 115, 202);

		Button btnStore = new Button(shell, SWT.NONE);
		btnStore.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {

				Integer tenantId = currentUser.getCurrentTenant().getId();
				String wrkTenantName = tenant.getText();
				if (wrkTenantName == null || wrkTenantName.trim().length() < 1) {
					lblInfo.setText(Cst.TENANTNAME_MUST_HAVE_A_VALUE);
					return;
				}

				TenantRec rec = tenantService.get(wrkTenantName);
				if (rec != null) {
					tenant.setData(null);
					lblInfo.setText(wrkTenantName + " " + " already exist");
					return;
				} else {
					String wrkDescription = description.getText();
					TenantKey key = new TenantKey(tenantId);
					rec = new TenantRec(key);
					rec.description = wrkDescription;
					tenantService.insert(rec, currentUser.userId);
				}
				refreshTenantTree();
				clearForm();
				lblInfo.setText("");

			}
		});
		btnStore.setBounds(348, 31, 120, 25);
		btnStore.setText("Spara");

		refreshTenantTree();
	}

	private void refreshTenantTree() {
		selectedVersion = null;
		tenantTree.removeAll();

		Integer tenantId = currentUser.getCurrentTenant().getId();

		java.util.List<String> products = tenantService.list(tenantId);
		int idx = 0;
		for (String productName : products) {
			TreeItem treeItem = new TreeItem(tenantTree, SWT.NONE, idx);
			treeItem.setText(productName);
			java.util.List<ProductRec> versions = tenantService.list(tenantId, productName);
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
		tenant.setText("");
		tenant.setData(null);
	}

	public void setKey(ProductKey rec) {
		// TODO Auto-generated method stub

	}

	public void setCurrentUser(User user) {
		currentUser = user;
	}

}
