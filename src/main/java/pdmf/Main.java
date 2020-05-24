package pdmf;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.FormLayout;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.MenuItem;
import org.eclipse.swt.widgets.Shell;

import pdmf.model.Cst;
import pdmf.model.TenantRec;
import pdmf.model.User;
import pdmf.sys.Db;
import pdmf.ui.Info;
import pdmf.ui.ProductVersion;
import pdmf.ui.Search;
import pdmf.ui.Tenant;
import pdmf.ui.TenantSelect;

/**
 * https://github.com/maven-eclipse/maven-eclipse.github.io
 * 
 * @author tfant
 *
 */

public class Main {

	protected Shell shell;
	Display display;

	private static User currentUser;

	private Menu mainMenu;
	private MenuItem submenuAtgarder = null;

	private Menu menuAtgarder = null;
	private MenuItem mntmMaintainArtifacts = null;
	private Menu menu_2 = null;
	private MenuItem mntmProductViewer = null;
	private MenuItem mntmNewProductVersion = null;
	private MenuItem mntmQuery = null;
	private MenuItem mntmTenant = null;
	private MenuItem mntmNewItem_1 = null;
	private MenuItem mntmSelectTenant = null;
	private MenuItem mntmClient = null;

	private TenantRec selectedTenant = null;

	/**
	 * Launch the application.
	 * 
	 * @param args
	 */
	public static void main(String[] args) {
		try {
			currentUser = getUser();
			Db.setupDatabasePool();
			Main window = new Main();
			window.open();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			Db.stopDatabasePool();
		}
	}

	/**
	 * Open the window.
	 */
	public void open() {
		display = Display.getDefault();
		createContents();
		shell.open();
		shell.layout();
		while (!shell.isDisposed()) {
			if (!display.readAndDispatch()) {
				display.sleep();
			}
		}
	}

	/**
	 * Create contents of the window.
	 */

	protected void createContents() {

		shell = new Shell();
		shell.setSize(482, 176);
		shell.setText(Cst.PGM_NAME);
		shell.setLayout(new FormLayout());

		mainMenu = new Menu(shell, SWT.BAR);
		shell.setMenuBar(mainMenu);

		submenuAtgarder = new MenuItem(mainMenu, SWT.CASCADE);
		submenuAtgarder.setText("Åtgärder");

		menuAtgarder = new Menu(submenuAtgarder);
		submenuAtgarder.setMenu(menuAtgarder);

		mntmMaintainArtifacts = new MenuItem(menuAtgarder, SWT.CASCADE);
		mntmMaintainArtifacts.setText("Underhåll");

		menu_2 = new Menu(mntmMaintainArtifacts);
		mntmMaintainArtifacts.setMenu(menu_2);

		mntmProductViewer = new MenuItem(menu_2, SWT.NONE);
		mntmProductViewer.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				pdmf.ui.ProductViewer dialog = new pdmf.ui.ProductViewer(shell, SWT.DIALOG_TRIM | SWT.MODELESS);
				dialog.setText(Cst.WRK_WITH_PRODUCTS);
				dialog.setCurrentUser(currentUser);
				dialog.open();
			}
		});
		mntmProductViewer.setText(Cst.WRK_WITH_PRODUCTS);

		mntmNewProductVersion = new MenuItem(menu_2, SWT.NONE);
		mntmNewProductVersion.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				ProductVersion dialog = new ProductVersion(shell, SWT.DIALOG_TRIM | SWT.APPLICATION_MODAL);
				dialog.setText(Cst.WRK_WITH_NEWPRODUCT);
				dialog.setCurrentUser(currentUser);
				dialog.open();
			}
		});
		mntmNewProductVersion.setText(Cst.WRK_WITH_NEWPRODUCT);

		mntmQuery = new MenuItem(menuAtgarder, SWT.NONE);
		mntmQuery.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				Search dialog = new Search(shell, SWT.DIALOG_TRIM | SWT.MODELESS);
				dialog.setText("Leta");
				dialog.setCurrentUser(currentUser);
				dialog.open();
			}
		});
		mntmQuery.setText("Leta");

		mntmTenant = new MenuItem(menu_2, SWT.NONE);
		mntmTenant.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				Tenant dialog = new Tenant(shell, SWT.DIALOG_TRIM | SWT.APPLICATION_MODAL);
				dialog.setText(Cst.WRK_WITH_TENANT);
				dialog.setCurrentUser(currentUser);
				dialog.open();
			}
		});
		mntmTenant.setText(Cst.WRK_WITH_TENANT);

		mntmNewItem_1 = new MenuItem(menuAtgarder, SWT.NONE);
		mntmNewItem_1.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				Info dialog = new Info(shell, SWT.DIALOG_TRIM | SWT.APPLICATION_MODAL);
				dialog.setText("Välkommen");
				dialog.open();
			}
		});
		mntmNewItem_1.setText("Info");

		MenuItem mntmKlient = new MenuItem(mainMenu, SWT.CASCADE);
		mntmKlient.setText("Klient");

		Menu menu = new Menu(mntmKlient);
		mntmKlient.setMenu(menu);

		mntmSelectTenant = new MenuItem(menu, SWT.NONE);
		mntmSelectTenant.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				TenantSelect dialog = new TenantSelect(shell, SWT.DIALOG_TRIM | SWT.APPLICATION_MODAL);
				dialog.setText(Cst.TENANT_SELECT);
				Object result = dialog.open();
				if (result instanceof TenantRec) {
					selectedTenant = (TenantRec) result;
					setMenuEnabled(true);
					currentUser.setCurrentTenant(selectedTenant);
					return;
				} else {
					selectedTenant = null;
					setMenuEnabled(false);
				}
			}
		});
		mntmSelectTenant.setText(Cst.TENANT_SELECT);

		mntmClient = new MenuItem(menu, SWT.NONE);
		mntmClient.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				Tenant dialog = new Tenant(shell, SWT.DIALOG_TRIM | SWT.APPLICATION_MODAL);
				dialog.setText(Cst.WRK_WITH_TENANT);
				dialog.setCurrentUser(currentUser);
				dialog.open();
			}
		});
		mntmClient.setText(Cst.WRK_WITH_TENANT);

		setMenuEnabled(false);
	}

	private void setMenuEnabled(Boolean enabled) {

		mntmProductViewer.setEnabled(enabled);
		mntmNewProductVersion.setEnabled(enabled);
		mntmQuery.setEnabled(enabled);
		mntmTenant.setEnabled(enabled);
		mntmNewItem_1.setEnabled(enabled);

	}

	public static User getUser() {

		User user = new User();
		return user;

	}
}
