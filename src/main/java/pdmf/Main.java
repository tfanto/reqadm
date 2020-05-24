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
import pdmf.model.User;
import pdmf.sys.Db;
import pdmf.ui.Info;
import pdmf.ui.ProductVersion;
import pdmf.ui.Search;
import pdmf.ui.Tenant;

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

	private Menu menu = null;
	private MenuItem mntmNewSubmenu = null;
	private Menu menu_1 = null;
	private MenuItem mntmMaintainArtifacts = null;
	private Menu menu_2 = null;
	private MenuItem mntmProductViewer = null;
	private MenuItem mntmNewProductVersion = null;
	private MenuItem mntmQuery = null;
	private MenuItem mntmTenant = null;
	private MenuItem mntmNewItem_1 = null;
	private MenuItem mntmSelectTenant = null;

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

		menu = new Menu(shell, SWT.BAR);
		shell.setMenuBar(menu);

		mntmNewSubmenu = new MenuItem(menu, SWT.CASCADE);
		mntmNewSubmenu.setText("Åtgärder");

		menu_1 = new Menu(mntmNewSubmenu);
		mntmNewSubmenu.setMenu(menu_1);

		mntmMaintainArtifacts = new MenuItem(menu_1, SWT.CASCADE);
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

		mntmQuery = new MenuItem(menu_1, SWT.NONE);
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

		mntmNewItem_1 = new MenuItem(menu_1, SWT.NONE);
		mntmNewItem_1.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				Info dialog = new Info(shell, SWT.DIALOG_TRIM | SWT.APPLICATION_MODAL);
				dialog.setText("Välkommen");
				dialog.open();
			}
		});
		mntmNewItem_1.setText("Info");
		
		mntmSelectTenant = new MenuItem(menu, SWT.NONE);
		mntmSelectTenant.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				
				System.out.println("VÄLJ TENANT");
				
			}
		});
		mntmSelectTenant.setText("Välj klient");

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
