package pdmf.ui;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Dialog;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.TableItem;
import org.eclipse.swt.widgets.Text;

import pdmf.Main;
import pdmf.model.Cst2;
import pdmf.model.OperationKey;
import pdmf.model.ProcessKey;
import pdmf.model.ProductKey;
import pdmf.model.TopicKey;
import pdmf.model.User;
import pdmf.service.SearchService;

public class Search extends Dialog {

	private SearchService searchService = new SearchService();

	protected Object result;
	protected Shell shell;
	private Text ord01;
	private Text ord02;
	private Text ord03;
	private Table searchResult;
	private TableColumn columnDescription;
	private TableColumn columnShortDescr;
	private Label lblInfo;
	private Button btnLaneProduct;
	private Button btnLaneTopic;
	private Button btnLaneProcess;
	private Button btnLaneOperation;

	private User currentUser;

	public Search(Shell parent, int style) {
		super(parent, style);
		String SEARCH = Main.cst(Cst2.SEARCH);
		setText("[" + SEARCH + "]");
	}

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

	private void createContents() {
		shell = new Shell(getParent(), getStyle());
		shell.setSize(1038, 670);
		shell.setText(getText());
		shell.setText(getText() + " " + currentUser.getCurrentTenant().description);

		shell.setLayout(null);

		Label sokOrd = new Label(shell, SWT.NONE);
		sokOrd.setBounds(10, 10, 83, 25);
		String SEARCH_1_3 = Main.cst(Cst2.SEARCH_1_3);
		sokOrd.setText(SEARCH_1_3);

		ord01 = new Text(shell, SWT.BORDER);
		ord01.setBounds(10, 40, 300, 25);

		ord02 = new Text(shell, SWT.BORDER);
		ord02.setBounds(10, 66, 300, 25);

		ord03 = new Text(shell, SWT.BORDER);
		ord03.setBounds(10, 92, 300, 25);

		lblInfo = new Label(shell, SWT.BORDER);
		lblInfo.setBounds(10, 597, 998, 24);
		lblInfo.setText("");

		Button btnRensa = new Button(shell, SWT.NONE);
		btnRensa.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				clear();
			}
		});
		btnRensa.setBounds(10, 282, 297, 38);
		String CLEAR = Main.cst(Cst2.CLEAR);
		btnRensa.setText(CLEAR);

		Button btnLeta = new Button(shell, SWT.NONE);
		btnLeta.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				List<String> criteriaList = getCriteriaList();
				if (criteriaList.size() < 1) {
					String NO_SEARCH_CRITERIA = Main.cst(Cst2.NO_SEARCH_CRITERIA);
					lblInfo.setText(NO_SEARCH_CRITERIA);
					return;
				}

				String tenantId = currentUser.getCurrentTenant().key.tenantid;

				Boolean searchInProduct = btnLaneProduct.getSelection();
				Boolean searchInTopic = btnLaneTopic.getSelection();
				Boolean searchInProcess = btnLaneProcess.getSelection();
				Boolean searchInOperation = btnLaneOperation.getSelection();

				List<Map<Object, List<String>>> resultSet = searchService.search(criteriaList, searchInProduct, searchInTopic, searchInProcess, searchInOperation, tenantId);
				searchResult.removeAll();
				lblInfo.setText("");
				for (Map<Object, List<String>> record : resultSet) {
					Set<Object> keys = record.keySet();
					for (Object key : keys) {
						List<String> data = record.get(key);
						TableItem tableItem = new TableItem(searchResult, SWT.NONE);
						tableItem.setData(key);
						tableItem.setText(new String[] { data.get(0), data.get(1) });
					}
				}
				String LINES = Main.cst(Cst2.LINES);
				lblInfo.setText(LINES + "   " + resultSet.size());
			}
		});
		String SEARCH = Main.cst(Cst2.SEARCH);
		btnLeta.setText(SEARCH);
		btnLeta.setBounds(10, 236, 297, 38);

		searchResult = new Table(shell, SWT.BORDER | SWT.FULL_SELECTION);
		searchResult.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {

				int idx = searchResult.getSelectionIndex();
				if (idx < 0) {
					return;
				}
				if (e.getSource() instanceof Table) {
					Table table = (Table) e.getSource();
					Object tableItemObject = table.getItem(idx);

					Set<String> searchWords = getSearhwords();

					if (tableItemObject instanceof TableItem) {
						TableItem tableItem = (TableItem) tableItemObject;

						Object object = tableItem.getData();

						if (object instanceof ProductKey) {
							ProductKey key = (ProductKey) object;
							pdmf.ui.Product dialog = new pdmf.ui.Product(shell, SWT.DIALOG_TRIM | SWT.APPLICATION_MODAL);
							dialog.setKey(key);
							dialog.setCurrentUser(currentUser);
							dialog.setSearchWords(searchWords);
							dialog.open();
						} else if (object instanceof TopicKey) {
							TopicKey key = (TopicKey) object;
							pdmf.ui.Topic dialog = new pdmf.ui.Topic(shell, SWT.DIALOG_TRIM | SWT.APPLICATION_MODAL);
							dialog.setKey(key, key.version);
							dialog.setCurrentUser(currentUser);
							dialog.setSearchWords(searchWords);
							dialog.open();
						} else if (object instanceof ProcessKey) {
							ProcessKey key = (ProcessKey) object;
							pdmf.ui.Process dialog = new pdmf.ui.Process(shell, SWT.DIALOG_TRIM | SWT.APPLICATION_MODAL);
							dialog.setKey(key, key.version);
							dialog.setCurrentUser(currentUser);
							dialog.setSearchWords(searchWords);
							dialog.open();
						} else if (object instanceof OperationKey) {
							OperationKey key = (OperationKey) object;
							pdmf.ui.Operation dialog = new pdmf.ui.Operation(shell, SWT.DIALOG_TRIM | SWT.APPLICATION_MODAL);
							dialog.setKey(key, key.version);
							dialog.setCurrentUser(currentUser);
							dialog.setSearchWords(searchWords);
							dialog.open();
						}
					}
				}

			}
		});
		searchResult.setBounds(313, 39, 695, 552);
		searchResult.setHeaderVisible(true);
		searchResult.setLinesVisible(true);

		columnDescription = new TableColumn(searchResult, SWT.NONE);
		columnDescription.setWidth(372);
		String DESCRIPTION = Main.cst(Cst2.DESCRIPTION);
		columnDescription.setText(DESCRIPTION);

		columnShortDescr = new TableColumn(searchResult, SWT.NONE);
		columnShortDescr.setWidth(366);
		String DESCRIPTION_SHORT = Main.cst(Cst2.DESCRIPTION_SHORT);
		columnShortDescr.setText(DESCRIPTION_SHORT);

		Label lblSearchResult = new Label(shell, SWT.NONE);
		String SEARCH_RESULT = Main.cst(Cst2.SEARCH_RESULT);
		lblSearchResult.setText(SEARCH_RESULT);
		lblSearchResult.setBounds(313, 10, 123, 25);

		btnLaneProduct = new Button(shell, SWT.CHECK);
		btnLaneProduct.setBounds(10, 123, 123, 25);
		String SEARCH_IN_PRODUCT = Main.cst(Cst2.SEARCH_IN_PRODUCT);
		btnLaneProduct.setText(SEARCH_IN_PRODUCT);

		btnLaneTopic = new Button(shell, SWT.CHECK);
		String SEARCH_IN_TOPIC = Main.cst(Cst2.SEARCH_IN_TOPIC);
		btnLaneTopic.setText(SEARCH_IN_TOPIC);
		btnLaneTopic.setBounds(10, 147, 145, 25);

		btnLaneProcess = new Button(shell, SWT.CHECK);
		String SEARCH_IN_PROCESS = Main.cst(Cst2.SEARCH_IN_PROCESS);
		btnLaneProcess.setText(SEARCH_IN_PROCESS);
		btnLaneProcess.setBounds(10, 173, 145, 25);

		btnLaneOperation = new Button(shell, SWT.CHECK);
		String SEARCH_IN_OPERATION = Main.cst(Cst2.SEARCH_IN_OPERATION);
		btnLaneOperation.setText(SEARCH_IN_OPERATION);
		btnLaneOperation.setBounds(10, 196, 145, 25);

		btnLaneProduct.setSelection(false);
		btnLaneTopic.setSelection(false);
		btnLaneProcess.setSelection(false);
		btnLaneOperation.setSelection(true);

		clear();

	}

	private void clear() {
		searchResult.removeAll();
		ord01.setText("");
		ord02.setText("");
		ord03.setText("");
	}

	private List<String> getCriteriaList() {

		List<String> list = new ArrayList<>();
		list = collect(list, ord01);
		list = collect(list, ord02);
		list = collect(list, ord03);
		return list;

	}

	private List<String> collect(List<String> list, Text txt) {
		String str = getStr(txt);
		if (str.length() > 0) {
			list.add(str);
		}
		return list;
	}

	private String getStr(Text txt) {
		String str = txt.getText();
		if (str == null)
			return "";
		if (str.trim().length() < 1)
			return "";
		return str;
	}

	public void setCurrentUser(User user) {
		currentUser = user;
	}

	private Set<String> getSearhwords() {

		Set<String> list = new HashSet<>();
		list = lineToWords(list, ord01.getText());
		list = lineToWords(list, ord02.getText());
		list = lineToWords(list, ord03.getText());
		return list;
	}

	private Set<String> lineToWords(Set<String> wordList, String line) {
		if (line == null || line.trim().length() < 1)
			return wordList;

		String str[] = line.split(" ");
		if (str.length < 1) {
			return wordList;
		}
		for (int i = 0; i < str.length; i++) {
			wordList.add(str[i].trim());
		}
		return wordList;
	}

}
