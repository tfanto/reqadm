package pdmf.ui;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.List;

import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.StyledText;
import org.eclipse.swt.events.MouseAdapter;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Dialog;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.FileDialog;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.MenuItem;
import org.eclipse.swt.widgets.MessageBox;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Tree;
import org.eclipse.swt.widgets.TreeColumn;
import org.eclipse.swt.widgets.TreeItem;

import pdmf.Main;
import pdmf.model.CstI18N;
import pdmf.model.OperationKey;
import pdmf.model.OperationRec;
import pdmf.model.ProcessKey;
import pdmf.model.ProcessRec;
import pdmf.model.ProductKey;
import pdmf.model.ProductRec;
import pdmf.model.TopicKey;
import pdmf.model.TopicRec;
import pdmf.model.User;
import pdmf.service.OperationService;
import pdmf.service.ProcessService;
import pdmf.service.ProductService;
import pdmf.service.TopicService;
import pdmf.service.support.ExportSupport;

public class ProductViewer extends Dialog {

	private ProductService productService = new ProductService();
	private TopicService topicService = new TopicService();
	private ProcessService processService = new ProcessService();
	private OperationService operationService = new OperationService();

	protected Object result;
	protected Shell shell;
	private Label lblInfo = null;
	private StyledText description;

	private ProductRec selectedVersion = null;
	private StyledText shortDescription;
	private Label lblShortDescription;
	private Label lblCrtDat;
	private Label lblChgDat;

	private Menu menu;

	private Combo selectProduct = null;
	private Combo selectVersion = null;

	private Color newItemColor;
	private Color dltItemColor;
	private Color yellowColor;

	private User currentUser;

	/**
	 * Create the dialog.
	 * 
	 * @param parent
	 * @param style
	 */
	public ProductViewer(Shell parent, int style) {
		super(parent, style);
		String PRODUCT_VIEWER = Main.cst(CstI18N.PRODUCT_VIEWER);
		setText("[" + PRODUCT_VIEWER + "]");
	}

	/**
	 * Open the dialog.
	 * 
	 * @return the result
	 */
	public Object open() {
		createContents();
		shell.pack();

		shell.open();
		shell.layout();
		Display display = getParent().getDisplay();
		newItemColor = display.getSystemColor(SWT.COLOR_GREEN);
		dltItemColor = display.getSystemColor(SWT.COLOR_RED);
		yellowColor = display.getSystemColor(SWT.COLOR_YELLOW);

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
		shell.setSize(1050, 726);
		shell.setText(getText() + " " + currentUser.getCurrentTenant().description);

		shell.setLayout(null);

		Label lblDescription = new Label(shell, SWT.NONE);
		lblDescription.setBounds(796, 239, 151, 25);
		String DESCRIPTION = Main.cst(CstI18N.DESCRIPTION);
		lblDescription.setText(DESCRIPTION);

		Tree productTree = new Tree(shell, SWT.FULL_SELECTION);
		productTree.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseDown(MouseEvent e) {

				int mouseButton = e.button;
				if (mouseButton == 3) {
					handleMouse(e);
				}
			}
		});
		productTree.setHeaderVisible(true);
		productTree.setLinesVisible(true);

		productTree.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {

				Object object = e.item.getData();
				if (object == null) {
					return;
				}

				if (object instanceof ProductKey) {
					ProductKey rec = (ProductKey) object;
					handleProduct(rec);
				} else if (object instanceof TopicKey) {
					TopicKey rec = (TopicKey) object;
					handleTopic(rec);
				} else if (object instanceof ProcessKey) {
					ProcessKey rec = (ProcessKey) object;
					handleProcess(rec);
				} else if (object instanceof OperationKey) {
					OperationKey rec = (OperationKey) object;
					handleOperation(rec);
				} else {
					return;
				}
			}

			private void handleInfo(Instant createDate, String createUser, Instant changeDate, String chgusr, Instant deleteDate, String deleteUser, Integer createdInVersion) {

				String CREATED = Main.cst(CstI18N.CREATED);
				String CHANGED = Main.cst(CstI18N.CHANGED);
				String REMOVED = Main.cst(CstI18N.REMOVED);
				String BY = Main.cst(CstI18N.BY);
				String IN_VERSION = Main.cst(CstI18N.IN_VERSION);

				LocalDate created = LocalDateTime.ofInstant(createDate, ZoneOffset.UTC).toLocalDate();
				lblCrtDat.setText(CREATED + created.toString() + BY + createUser + IN_VERSION + createdInVersion);

				lblChgDat.setText("");
				if (changeDate != null && chgusr != null) {
					LocalDate changed = LocalDateTime.ofInstant(changeDate, ZoneOffset.UTC).toLocalDate();
					lblChgDat.setText(CHANGED + changed.toString() + BY + chgusr);
				}
				if (deleteDate != null && deleteUser != null) {
					LocalDate deleted = LocalDateTime.ofInstant(deleteDate, ZoneOffset.UTC).toLocalDate();
					lblChgDat.setText(REMOVED + deleted.toString() + BY + deleteUser);
				}

			}

			private void handleOperation(OperationKey key) {
				OperationRec pRec = operationService.get(key);
				if (pRec == null) {
					clear();
				} else {
					description.setText(pRec.description == null ? "" : pRec.description);
					shortDescription.setText(pRec.shortdescr == null ? "" : pRec.shortdescr);
					handleInfo(pRec.crtdat, pRec.crtusr, pRec.chgdat, pRec.chgusr, pRec.dltdat, pRec.dltusr, pRec.crtver);
				}
			}

			private void handleProcess(ProcessKey key) {
				ProcessRec pRec = processService.get(key);
				if (pRec == null) {
					clear();
				} else {
					description.setText(pRec.description == null ? "" : pRec.description);
					shortDescription.setText(pRec.shortdescr == null ? "" : pRec.shortdescr);
					handleInfo(pRec.crtdat, pRec.crtusr, pRec.chgdat, pRec.chgusr, pRec.dltdat, pRec.dltusr, pRec.crtver);
				}
			}

			private void handleTopic(TopicKey key) {
				TopicRec pRec = topicService.get(key);
				if (pRec == null) {
					clear();
				} else {
					description.setText(pRec.description == null ? "" : pRec.description);
					shortDescription.setText(pRec.shortdescr == null ? "" : pRec.shortdescr);
					handleInfo(pRec.crtdat, pRec.crtusr, pRec.chgdat, pRec.chgusr, pRec.dltdat, pRec.dltusr, pRec.crtver);
				}
			}

			private void handleProduct(ProductKey key) {
				ProductRec pRec = productService.get(key);
				if (pRec == null) {
					clear();
				} else {
					description.setText(pRec.description == null ? "" : pRec.description);
					shortDescription.setText(pRec.shortdescr == null ? "" : pRec.shortdescr);
					handleInfo(pRec.crtdat, pRec.crtusr, pRec.chgdat, pRec.chgusr, pRec.dltdat, pRec.dltusr, pRec.crtver);
				}
			}
		});
		productTree.setBounds(10, 10, 780, 572);

		TreeColumn trclmnProdukt = new TreeColumn(productTree, SWT.NONE);
		trclmnProdukt.setWidth(195);
		String PRODUCT_VERSION_STATUS = Main.cst(CstI18N.PRODUCT_VERSION_STATUS);
		trclmnProdukt.setText(PRODUCT_VERSION_STATUS);

		TreeColumn trclmnmnesomrde = new TreeColumn(productTree, SWT.NONE);
		trclmnmnesomrde.setWidth(195);
		String TOPIC = Main.cst(CstI18N.TOPIC);
		trclmnmnesomrde.setText(TOPIC);

		TreeColumn trclmnProcess = new TreeColumn(productTree, SWT.NONE);
		trclmnProcess.setWidth(195);
		String PROCESS = Main.cst(CstI18N.PROCESS);
		trclmnProcess.setText(PROCESS);

		TreeColumn trclmnOperation = new TreeColumn(productTree, SWT.NONE);
		trclmnOperation.setWidth(195);
		String OPERATION = Main.cst(CstI18N.OPERATION);
		trclmnOperation.setText(OPERATION);

		menu = new Menu(productTree);
		productTree.setMenu(menu);

		description = new StyledText(shell, SWT.V_SCROLL | SWT.WRAP);
		description.setEditable(false);
		description.setBounds(796, 266, 239, 316);
		description.setTextLimit(995);

		lblInfo = new Label(shell, SWT.SHADOW_IN);
		lblInfo.setBounds(10, 639, 1034, 24);

		lblCrtDat = new Label(shell, SWT.NONE);
		lblCrtDat.setBounds(10, 591, 780, 24);

		lblChgDat = new Label(shell, SWT.NONE);
		lblChgDat.setBounds(10, 615, 780, 24);

		shortDescription = new StyledText(shell, SWT.V_SCROLL | SWT.WRAP);
		shortDescription.setEditable(false);
		shortDescription.setTextLimit(100);
		shortDescription.setBounds(796, 110, 239, 123);

		lblShortDescription = new Label(shell, SWT.NONE);
		String DESCRIPTION_SHORT = Main.cst(CstI18N.DESCRIPTION_SHORT);
		lblShortDescription.setText(DESCRIPTION_SHORT);
		lblShortDescription.setBounds(796, 80, 151, 25);

		selectProduct = new Combo(shell, SWT.NONE);
		selectProduct.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				int i = selectProduct.getSelectionIndex();
				if (i < 0)
					return;

				String tenantId = currentUser.getCurrentTenant().key.tenantid;
				String productName = selectProduct.getItem(i);
				refreshVersionCombo(tenantId, productName);
			}
		});
		selectProduct.setBounds(910, 10, 129, 17);

		selectVersion = new Combo(shell, SWT.NONE);
		selectVersion.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {

				int i = selectProduct.getSelectionIndex();
				if (i < 0)
					return;
				String productName = selectProduct.getItem(i);

				int j = selectVersion.getSelectionIndex();
				if (j < 0)
					return;
				String versionStr = selectVersion.getItem(j);
				Integer version = null;
				try {
					version = Integer.parseInt(versionStr);
				} catch (NumberFormatException nfe) {
					return;
				}
				String tenantId = currentUser.getCurrentTenant().key.tenantid;
				refreshProductTree(productTree, productName, version, tenantId);
			}
		});
		selectVersion.setBounds(910, 47, 129, 17);

		Label lblLblproduct = new Label(shell, SWT.NONE);
		lblLblproduct.setBounds(796, 10, 109, 25);
		String SELECT_PRODUCT = Main.cst(CstI18N.SELECT_PRODUCT);
		lblLblproduct.setText(SELECT_PRODUCT);

		Label lblLblVersion = new Label(shell, SWT.NONE);
		String SELECT_VERSION = Main.cst(CstI18N.SELECT_VERSION);
		lblLblVersion.setText(SELECT_VERSION);
		lblLblVersion.setBounds(796, 45, 109, 25);
		String tenantId = currentUser.getCurrentTenant().key.tenantid;
		refreshProductCombo(tenantId);

	}

	private void refreshVersionCombo(String tenantId, String productName) {

		selectVersion.removeAll();
		List<ProductRec> productRecs = productService.list(tenantId, productName);
		for (ProductRec product : productRecs) {
			selectVersion.add(String.valueOf(product.key.version));
		}
	}

	private void refreshProductCombo(String tenantId) {

		selectProduct.removeAll();
		selectVersion.removeAll();
		List<String> productNames = productService.list(tenantId);
		for (String productName : productNames) {
			selectProduct.add(productName);
		}
	}

	private void refreshProductTree(Tree productTree, String productName, Integer version, String tenantId) {

		clear();
		productTree.removeAll();

		ProductKey key = new ProductKey(tenantId, version, productName);
		java.util.List<ProductRec> versions = productService.list(key);
		int idxVersion = 0;
		for (ProductRec versionRec : versions) {
			TreeItem versionTreeItem = new TreeItem(productTree, SWT.NONE, idxVersion);
			versionTreeItem.setText(0, String.valueOf(versionRec.key.productName + " ver." + versionRec.key.version) + " [" + versionRec.status + "]");
			versionTreeItem.setData(versionRec.key);

			versionTreeItem.setBackground(0, yellowColor);
			if (versionRec.key.version.equals(versionRec.crtver)) {
				versionTreeItem.setBackground(0, newItemColor);
			}
			if (versionRec.dltusr != null) {
				versionTreeItem.setBackground(0, dltItemColor);
			}

			java.util.List<TopicRec> topics = topicService.list(versionRec.key.tenantid, versionRec.key.version, versionRec.key.productName);
			int idxTopic = 0;
			for (TopicRec topicRec : topics) {
				TreeItem topicTreeItem = new TreeItem(versionTreeItem, SWT.NONE, idxTopic);
				topicTreeItem.setText(1, topicRec.key.topicName);
				topicTreeItem.setData(topicRec.key);

				topicTreeItem.setBackground(1, yellowColor);
				if (topicRec.key.version.equals(topicRec.crtver)) {
					topicTreeItem.setBackground(1, newItemColor);
				}
				if (topicRec.dltusr != null) {
					topicTreeItem.setBackground(1, dltItemColor);
				}

				java.util.List<ProcessRec> processes = processService.list(topicRec.key.tenantid, topicRec.key.version, topicRec.key.productName, topicRec.key.topicName);
				int idxProcess = 0;
				for (ProcessRec processRec : processes) {
					TreeItem processTreeItem = new TreeItem(topicTreeItem, SWT.NONE, idxProcess);
					processTreeItem.setText(2, processRec.key.processName + " [" + processRec.key.processSeq + "]");
					processTreeItem.setData(processRec.key);

					processTreeItem.setBackground(2, yellowColor);
					if (processRec.key.version.equals(processRec.crtver)) {
						processTreeItem.setBackground(2, newItemColor);
					}
					if (processRec.dltusr != null) {
						processTreeItem.setBackground(2, dltItemColor);
					}

					java.util.List<OperationRec> operations = operationService.list(processRec.key.tenantid, processRec.key.version, processRec.key.productName, processRec.key.topicName,
							processRec.key.processName, processRec.key.processSeq);
					int idxOperation = 0;
					for (OperationRec operationRec : operations) {
						TreeItem operationTreeItem = new TreeItem(processTreeItem, SWT.NONE, idxOperation);
						operationTreeItem.setText(3, operationRec.key.operationName + " [" + operationRec.key.operationSequence + "]");
						operationTreeItem.setData(operationRec.key);

						operationTreeItem.setBackground(3, yellowColor);
						if (operationRec.key.version.equals(operationRec.crtver)) {
							operationTreeItem.setBackground(3, newItemColor);
						}
						if (operationRec.dltusr != null) {
							operationTreeItem.setBackground(3, dltItemColor);
						}

						idxOperation++;
					}
					idxProcess++;
				}
				idxTopic++;
			}
			idxVersion++;
		}

		expandTree(productTree);
	}

	private void expandTree(Tree productTree) {

		productTree.setRedraw(false); // Stop redraw until operation complete
		TreeItem[] items = productTree.getItems();
		for (TreeItem item : items) {
			item.setExpanded(true);
			TreeItem[] items2 = item.getItems();
			for (TreeItem item2 : items2) {
				item2.setExpanded(true);
				TreeItem[] items3 = item2.getItems();
				for (TreeItem item3 : items3) {
					item3.setExpanded(true);
					TreeItem[] items4 = item3.getItems();
					for (TreeItem item4 : items4) {
						item4.setExpanded(true);
					}
				}
			}
		}
		productTree.setRedraw(true);
	}

	private void clear() {
		selectedVersion = null;
		lblChgDat.setText("");
		lblCrtDat.setText("");
		description.setText("");
		lblInfo.setText("");
		shortDescription.setText("");
	}

	ProductRec getSelectedVersion() {
		return selectedVersion;
	}

	private void handleMouse(MouseEvent e) {

		Object source = e.getSource();
		if (source instanceof Tree) {
			Tree tree = (Tree) source;
			TreeItem treeItems[] = tree.getSelection();
			if (treeItems != null) {
				for (TreeItem treeItem : treeItems) {
					Object treeItemData = treeItem.getData();
					if (treeItemData != null) {
						if (treeItemData instanceof ProductKey) {

							for (MenuItem item : menu.getItems()) {
								item.dispose();
							}
							MenuItem mntmProdukt = new MenuItem(menu, SWT.NONE);
							String WORK_WITH_PRODUCT = Main.cst(CstI18N.WORK_WITH_PRODUCT);
							mntmProdukt.setText(WORK_WITH_PRODUCT);
							mntmProdukt.addSelectionListener(new SelectionAdapter() {
								@Override
								public void widgetSelected(SelectionEvent e) {
									ProductKey rec = (ProductKey) treeItemData;
									pdmf.ui.Product dialog = new pdmf.ui.Product(shell, SWT.DIALOG_TRIM | SWT.APPLICATION_MODAL);
									dialog.setKey(rec);
									dialog.setCurrentUser(currentUser);
									dialog.open();
									refreshProductTree(tree, rec.productName, rec.version, rec.tenantid);
								}
							});

							MenuItem mntmExportProdukt = new MenuItem(menu, SWT.NONE);
							String EXPORT_AS_XML = Main.cst(CstI18N.EXPORT_AS_XML);
							mntmExportProdukt.setText(EXPORT_AS_XML);
							mntmExportProdukt.addSelectionListener(new SelectionAdapter() {
								@Override
								public void widgetSelected(SelectionEvent e) {
									ExportSupport export = new ExportSupport();
									ProductKey key = (ProductKey) treeItemData;
									String theXml = export.convertToXML(key);
									if (theXml != null) {

										FileDialog fileSave = new FileDialog(shell, SWT.SAVE);
										fileSave.setFilterNames(new String[] { "xml" });
										fileSave.setFilterExtensions(new String[] { "*.xml" });
										fileSave.setFilterPath("C:\\"); // Windows path
										String filename = key.productName + System.currentTimeMillis() + ".xml";
										fileSave.setFileName(filename);
										try {
											String open = fileSave.open();
											if (open == null) {
												return; // user aborted
											}
											File file = new File(open);
											if (file.createNewFile()) {
												String theFileName = file.getCanonicalPath();
												try (Writer out = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(theFileName), "UTF-8"))) {
													out.write(theXml);
												}

											}
										} catch (FileNotFoundException e1) {
											e1.printStackTrace();
										} catch (IOException e1) {
											e1.printStackTrace();
										} finally {

										}
									}
								}
							});

							MenuItem mntmCreateTopic = new MenuItem(menu, SWT.NONE);
							String NEW_TOPIC_FOR_PRODUCT = Main.cst(CstI18N.NEW_TOPIC_FOR_PRODUCT);
							mntmCreateTopic.setText(NEW_TOPIC_FOR_PRODUCT);
							mntmCreateTopic.setEnabled(true);
							mntmCreateTopic.addSelectionListener(new SelectionAdapter() {
								@Override
								public void widgetSelected(SelectionEvent e) {
									ProductKey key = (ProductKey) treeItemData;
									Boolean locked = ProductService.isLocked(key.tenantid, key.version, key.productName);
									Boolean deleteMarked = productService.isDeleteMarked(key);
									if (deleteMarked || locked) {
										String msg = deleteMarked ? "Deleted" : "Version locked";
										MessageBox diag = new MessageBox(shell, SWT.ICON_ERROR | SWT.OK);
										diag.setMessage(msg);
										diag.setText("Forbidden");
										diag.open();
										return;
									}
									pdmf.ui.Topic dialog = new pdmf.ui.Topic(shell, SWT.DIALOG_TRIM | SWT.APPLICATION_MODAL);
									dialog.setKey(key, key.version);
									dialog.setCurrentUser(currentUser);
									dialog.open();
									refreshProductTree(tree, key.productName, key.version, key.tenantid);
								}
							});

						} else if (treeItemData instanceof TopicKey) {
							for (MenuItem item : menu.getItems()) {
								item.dispose();
							}

							MenuItem mntmTopic = new MenuItem(menu, SWT.NONE);
							String WORK_WITH_TOPIC = Main.cst(CstI18N.WORK_WITH_TOPIC);
							mntmTopic.setText(WORK_WITH_TOPIC);
							mntmTopic.addSelectionListener(new SelectionAdapter() {
								@Override
								public void widgetSelected(SelectionEvent e) {
									TopicKey rec = (TopicKey) treeItemData;
									pdmf.ui.Topic dialog = new pdmf.ui.Topic(shell, SWT.DIALOG_TRIM | SWT.APPLICATION_MODAL);
									dialog.setKey(rec, rec.version);
									dialog.setCurrentUser(currentUser);
									dialog.open();
									refreshProductTree(tree, rec.productName, rec.version, rec.tenantid);
								}
							});

							MenuItem mntmCreateProcess = new MenuItem(menu, SWT.NONE);
							String NEW_PROCESS_FOR_TOPIC = Main.cst(CstI18N.NEW_PROCESS_FOR_TOPIC);
							mntmCreateProcess.setText(NEW_PROCESS_FOR_TOPIC);
							mntmCreateProcess.addSelectionListener(new SelectionAdapter() {
								@Override
								public void widgetSelected(SelectionEvent e) {

									TopicKey rec = (TopicKey) treeItemData;
									Boolean locked = ProductService.isLocked(rec.tenantid, rec.version, rec.productName);
									Boolean deleteMarked = topicService.isDeleteMarked(rec);
									if (deleteMarked || locked) {
										String msg = deleteMarked ? "Deleted" : "Version locked";
										MessageBox diag = new MessageBox(shell, SWT.ICON_ERROR | SWT.OK);
										diag.setMessage(msg);
										diag.setText("Forbidden");
										diag.open();
										return;
									}
									pdmf.ui.Process dialog = new pdmf.ui.Process(shell, SWT.DIALOG_TRIM | SWT.APPLICATION_MODAL);
									dialog.setKey(rec, rec.version);
									dialog.setCurrentUser(currentUser);
									dialog.open();
									refreshProductTree(tree, rec.productName, rec.version, rec.tenantid);
								}
							});

						} else if (treeItemData instanceof ProcessKey) {
							for (MenuItem item : menu.getItems()) {
								item.dispose();
							}

							MenuItem mntmProcess = new MenuItem(menu, SWT.NONE);
							String WORK_WITH_PROCESS = Main.cst(CstI18N.WORK_WITH_PROCESS);
							mntmProcess.setText(WORK_WITH_PROCESS);
							mntmProcess.addSelectionListener(new SelectionAdapter() {
								@Override
								public void widgetSelected(SelectionEvent e) {
									ProcessKey rec = (ProcessKey) treeItemData;
									pdmf.ui.Process dialog = new pdmf.ui.Process(shell, SWT.DIALOG_TRIM | SWT.APPLICATION_MODAL);
									dialog.setKey(rec, rec.version);
									dialog.setCurrentUser(currentUser);
									dialog.open();
									refreshProductTree(tree, rec.productName, rec.version, rec.tenantid);
								}
							});

							MenuItem mntmCreateOperation = new MenuItem(menu, SWT.NONE);
							String NEW_OPERATION_FOR_PROCESS = Main.cst(CstI18N.NEW_OPERATION_FOR_PROCESS);
							mntmCreateOperation.setText(NEW_OPERATION_FOR_PROCESS);
							mntmCreateOperation.addSelectionListener(new SelectionAdapter() {
								@Override
								public void widgetSelected(SelectionEvent e) {
									ProcessKey key = (ProcessKey) treeItemData;

									Boolean locked = ProductService.isLocked(key.tenantid, key.version, key.productName);
									Boolean deleteMarked = processService.isDeleteMarked(key);
									if (deleteMarked || locked) {
										String msg = deleteMarked ? "Deleted" : "Version locked";
										MessageBox diag = new MessageBox(shell, SWT.ICON_ERROR | SWT.OK);
										diag.setMessage(msg);
										diag.setText("Forbidden");
										diag.open();
										return;
									}

									pdmf.ui.Operation dialog = new pdmf.ui.Operation(shell, SWT.DIALOG_TRIM | SWT.APPLICATION_MODAL);
									dialog.setKey(key, key.version);
									dialog.setCurrentUser(currentUser);
									dialog.open();
									refreshProductTree(tree, key.productName, key.version, key.tenantid);
								}
							});

						} else if (treeItemData instanceof OperationKey) {
							for (MenuItem item : menu.getItems()) {
								item.dispose();
							}

							MenuItem mntmOperation = new MenuItem(menu, SWT.NONE);
							String WORK_WITH_OPERATION = Main.cst(CstI18N.WORK_WITH_OPERATION);
							mntmOperation.setText(WORK_WITH_OPERATION);
							mntmOperation.addSelectionListener(new SelectionAdapter() {
								@Override
								public void widgetSelected(SelectionEvent e) {
									OperationKey key = (OperationKey) treeItemData;
									pdmf.ui.Operation dialog = new pdmf.ui.Operation(shell, SWT.DIALOG_TRIM | SWT.APPLICATION_MODAL);
									dialog.setKey(key, key.version);
									dialog.setCurrentUser(currentUser);
									dialog.open();
									refreshProductTree(tree, key.productName, key.version, key.tenantid);
								}
							});

						}
					}
				}
			}
		}
	}

	public void setCurrentUser(User user) {
		currentUser = user;
	}

}
