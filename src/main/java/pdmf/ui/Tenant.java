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
				
				Object source = e.getSource();
				Tree theTree = null;
				if(source instanceof Tree) {
					theTree = (Tree) source;
				}
				TreeItem treeItems[] = theTree.getSelection();
				if(treeItems == null || treeItems.length < 1) {
					return;
				}
				
				TreeItem selectedTreeItem = treeItems[0];
				String tenantId = selectedTreeItem.getText();
				


				tenant.setText("");
				description.setText("");
				lblInfo.setText("");
				shell.setText("Tenant");

				TenantRec rec = tenantService.get(tenantId);
				if (rec != null) {
					tenant.setText(rec.key.tenantid);
					description.setText(rec.description == null ? "" : rec.description);
					shell.setText("Tenant : " + rec.key.tenantid + " " + rec.description);
				} else {
					refreshTenantTree();
				}
			}
		});
		tenantTree.setBounds(10, 58, 115, 202);

		Button btnStore = new Button(shell, SWT.NONE);
		btnStore.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {

				String tenantId = tenant.getText();
				if (tenantId == null || tenantId.trim().length() < 1) {
					lblInfo.setText(Cst.TENANTID_MUST_HAVE_A_VALUE);
					return;
				}
				String descr = description.getText();
				if (tenantId == null || tenantId.trim().length() < 1) {
					lblInfo.setText(Cst.TENANT_DESCRIPTION_HAVE_A_VALUE);
					return;
				}

				TenantRec rec = tenantService.get(tenantId);
				TenantKey key = new TenantKey(tenantId);
				rec = new TenantRec(key, descr);
				tenantService.store(rec, currentUser.userId);
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
		tenantTree.removeAll();
		java.util.List<TenantRec> tenants = tenantService.list();
		int idx = 0;
		for (TenantRec tenant : tenants) {
			TreeItem treeItem = new TreeItem(tenantTree, SWT.NONE, idx);
			treeItem.setText(tenant.key.tenantid);
			idx++;
		}
	}

	private void clearForm() {
		description.setText("");
		tenant.setText("");
	}

	public void setCurrentUser(User user) {
		currentUser = user;
	}

}
