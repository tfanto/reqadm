package pdmf.ui;

import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.StyledText;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Dialog;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.List;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;

import pdmf.Main;
import pdmf.model.CstI18N;
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
	private List tenantList;

	private User currentUser;
	private Button btnRemove;

	/**
	 * Create the dialog.
	 * 
	 * @param parent
	 * @param style
	 */
	public Tenant(Shell parent, int style) {
		super(parent, style);
		String TENANT = Main.cst(CstI18N.TENANT);
		setText("[" + TENANT + "]");
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

		shell.setSize(451, 348);
		shell.setText(getText());
		shell.setLayout(null);

		Label lblTenant = new Label(shell, SWT.NONE);
		lblTenant.setBounds(10, 10, 115, 25);
		String TENANT = Main.cst(CstI18N.TENANT);
		lblTenant.setText(TENANT);

		Label lblDescription = new Label(shell, SWT.NONE);
		lblDescription.setBounds(125, 10, 143, 25);
		String DESCRIPTION = Main.cst(CstI18N.DESCRIPTION);
		lblDescription.setText(DESCRIPTION);

		tenant = new Text(shell, SWT.BORDER);
		tenant.setBounds(10, 42, 115, 25);
		tenant.setTextLimit(10);

		description = new StyledText(shell, SWT.V_SCROLL | SWT.WRAP | SWT.BORDER);
		description.setBounds(125, 42, 181, 227);
		description.setTextLimit(25);

		lblInfo = new Label(shell, SWT.NONE);
		lblInfo.setBounds(10, 281, 384, 25);
		String INFO = Main.cst(CstI18N.INFO);
		lblInfo.setText(INFO);

		tenantList = new List(shell, SWT.BORDER | SWT.H_SCROLL | SWT.V_SCROLL);
		tenantList.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {

				int idx = tenantList.getSelectionIndex();
				if (idx < 0) {
					return;
				}
				String tenantId = tenantList.getItem(idx);
				tenant.setText("");
				description.setText("");
				lblInfo.setText("");
				String TENANT = Main.cst(CstI18N.TENANT);
				shell.setText(TENANT);

				TenantRec rec = tenantService.get(tenantId);
				if (rec != null) {
					tenant.setText(rec.key.tenantid);
					description.setText(rec.description == null ? "" : rec.description);
					shell.setText("[" + TENANT + "] : " + rec.key.tenantid + " " + rec.description);
				} else {
					refreshTenantList();
				}
			}
		});
		tenantList.setBounds(10, 67, 115, 202);

		Button btnStore = new Button(shell, SWT.NONE);
		btnStore.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {

				String tenantId = tenant.getText();
				if (tenantId == null || tenantId.trim().length() < 1) {
					String TENANTID_MUST_HAVE_A_VALUE = Main.cst(CstI18N.TENANTID_MUST_HAVE_A_VALUE);
					lblInfo.setText(TENANTID_MUST_HAVE_A_VALUE);
					return;
				}
				String descr = description.getText();
				if (descr == null || descr.trim().length() < 1) {
					String TENANT_DESCRIPTION_HAVE_A_VALUE = Main.cst(CstI18N.TENANT_DESCRIPTION_HAVE_A_VALUE);
					lblInfo.setText(TENANT_DESCRIPTION_HAVE_A_VALUE);
					return;
				}

				TenantRec rec = tenantService.get(tenantId);
				TenantKey key = new TenantKey(tenantId);
				rec = new TenantRec(key, descr);
				tenantService.store(rec, currentUser.userId);
				refreshTenantList();
				clearForm();
				lblInfo.setText("");
				String TENANT = Main.cst(CstI18N.TENANT);
				shell.setText("[" + TENANT + "]");
			}
		});
		btnStore.setBounds(322, 31, 94, 30);
		String STORE = Main.cst(CstI18N.STORE);
		btnStore.setText(STORE);

		btnRemove = new Button(shell, SWT.NONE);
		btnRemove.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {

				String tenantId = tenant.getText();
				if (tenantId == null || tenantId.trim().length() < 1) {
					String TENANTID_MUST_HAVE_A_VALUE = Main.cst(CstI18N.TENANTID_MUST_HAVE_A_VALUE);
					lblInfo.setText(TENANTID_MUST_HAVE_A_VALUE);
					return;
				}
				tenantService.remove(tenantId);
				refreshTenantList();
				clearForm();
				lblInfo.setText("");
				shell.setText("[Tenant]");
			}
		});
		String REMOVE = Main.cst(CstI18N.REMOVE);
		btnRemove.setText(REMOVE);
		btnRemove.setBounds(322, 68, 94, 30);

		refreshTenantList();
	}

	private void refreshTenantList() {
		tenantList.removeAll();
		java.util.List<TenantRec> tenants = tenantService.list();
		for (TenantRec tenant : tenants) {
			tenantList.add(tenant.key.tenantid);
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
