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
import pdmf.model.CstI18N;
import pdmf.model.ProcessKey;
import pdmf.model.ProcessRec;
import pdmf.model.TopicKey;
import pdmf.model.User;
import pdmf.service.ProcessService;
import pdmf.service.ProductService;
import pdmf.sys.RecordChangedByAnotherUser;

public class Process extends Dialog {

	private static final String NEW_REG_MODE = "ny post";
	private static final String UPDATE_MODE = "ändra post";
	private String mode = null;

	private User currentUser;

	private ProcessService processService = new ProcessService();

	protected Object result;
	protected Shell shell;
	private Label product;
	private Label topic;
	private Button btnStore = null;
	private Button btnRemove = null;
	private Label lblInfo = null;
	private StyledText description;
	private Text process;
	private Text processStep;

	private Integer chgnbr = null;
	private Label lblProcess;
	private Label lblPstep;

	private StyledText shortDescription;
	private Label lblShortDescription;
	private Label crtDat;
	private Label chgDat;

	private Integer version = null;

	String productStr;
	String topicStr;
	String processStr;
	Integer processStepInt;

	private Set<String> searchWords = new HashSet<String>();
	private Label lblPath;

	/**
	 * Create the dialog.
	 * 
	 * @param parent
	 * @param style
	 */
	public Process(Shell parent, int style) {
		super(parent, style);
		String PROCESS = Main.cst(CstI18N.PROCESS);
		setText("[" + PROCESS + "]");
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
		// shell = new Shell(getParent(), SWT.DIALOG_TRIM | SWT.APPLICATION_MODAL);
		shell = new Shell(getParent(), getStyle());
		shell.setSize(584, 600);
		shell.setText(getText() + " " + mode + " " + currentUser.getCurrentTenant().description);
		shell.setLayout(null);

		lblProcess = new Label(shell, SWT.NONE);
		String PROCESS = Main.cst(CstI18N.PROCESS);
		lblProcess.setText(PROCESS);
		lblProcess.setBounds(167, 5, 82, 25);

		lblPstep = new Label(shell, SWT.NONE);
		String PROCESS_SEQ = Main.cst(CstI18N.PROCESS_SEQ);
		lblPstep.setText(PROCESS_SEQ);
		lblPstep.setBounds(349, 5, 57, 25);

		Label lblDescription = new Label(shell, SWT.NONE);
		lblDescription.setBounds(10, 200, 151, 25);
		String DESCRIPTION = Main.cst(CstI18N.DESCRIPTION);
		lblDescription.setText(DESCRIPTION);

		product = new Label(shell, SWT.BORDER);
		product.setBounds(10, 31, 151, 25);

		topic = new Label(shell, SWT.BORDER);
		topic.setBounds(10, 62, 151, 25);

		process = new Text(shell, SWT.BORDER);
		process.setBounds(167, 31, 175, 25);

		processStep = new Text(shell, SWT.BORDER);
		processStep.setBounds(349, 31, 57, 25);

		description = new StyledText(shell, SWT.V_SCROLL | SWT.WRAP | SWT.BORDER);
		description.setBounds(10, 228, 544, 187);
		description.setTextLimit(995);

		btnStore = new Button(shell, SWT.NONE);
		btnStore.setBounds(474, 32, 80, 25);
		btnStore.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {

				String tenantId = currentUser.getCurrentTenant().key.tenantid;

				String wrkProductName = (String) product.getData();

				String wrkTopicName = topic.getText();
				if (wrkTopicName == null || wrkTopicName.trim().length() < 1) {
					String NO_TOPIC_SELECTED = Main.cst(CstI18N.NO_TOPIC_SELECTED);
					lblInfo.setText(NO_TOPIC_SELECTED);
					return;
				}

				String wrkProcessName = process.getText();
				if (wrkProcessName == null || wrkProcessName.trim().length() < 1) {
					String NO_PROCESS_NAME = Main.cst(CstI18N.NO_PROCESS_NAME);
					lblInfo.setText(NO_PROCESS_NAME);
					return;
				}

				String wrkProcessStepNameStr = processStep.getText();
				if (wrkProcessStepNameStr == null || wrkProcessStepNameStr.trim().length() < 1) {
					String NO_PROCESS_SEQ = Main.cst(CstI18N.NO_PROCESS_SEQ);
					lblInfo.setText(NO_PROCESS_SEQ);
					return;
				}
				Integer wrkProcessStep = 0;
				try {
					wrkProcessStep = Integer.parseInt(wrkProcessStepNameStr);
				} catch (NumberFormatException nfe) {
					String NO_PROCESS_SEQ_AS_INTEGER = Main.cst(CstI18N.NO_PROCESS_SEQ_AS_INTEGER);
					lblInfo.setText(NO_PROCESS_SEQ_AS_INTEGER);
					return;

				}

				if (ProductService.isLocked(tenantId, version, wrkProductName)) {
					String VERSION_LOCKED = Main.cst(CstI18N.VERSION_LOCKED);
					lblInfo.setText(VERSION_LOCKED);
					return;
				}

				ProcessKey key = new ProcessKey(tenantId, version, wrkProductName, wrkTopicName, wrkProcessName, wrkProcessStep);
				if (processService.isDeleteMarked(key)) {
					String ALREADY_DELETE_NO_ACTION = Main.cst(CstI18N.ALREADY_DELETE_NO_ACTION);
					lblInfo.setText(ALREADY_DELETE_NO_ACTION);
					return;
				}

				ProcessRec rec = processService.get(key);

				if (mode.equals(NEW_REG_MODE)) {
					if (rec != null) {
						String ALREADY_EXISTS = Main.cst(CstI18N.ALREADY_EXISTS);
						lblInfo.setText(ALREADY_EXISTS);
						return;
					}
				}

				if (rec == null) {
					rec = new ProcessRec(key, null, null, null);
					chgnbr = null;
				} else {
					if (chgnbr != null) {
						rec.chgnbr = chgnbr;
					}
				}
				String wrkDescription = description.getText() == null ? "" : description.getText();
				String wrkShortDescription = shortDescription.getText() == null ? "" : shortDescription.getText();
				rec.shortdescr = wrkShortDescription;
				rec.description = wrkDescription;

				try {
					processService.store(rec, currentUser.userId);
					chgnbr = null;
					result = 1;
					shell.dispose();
				} catch (RecordChangedByAnotherUser rc) {
					String RECORD_CHANGED_BY_ANOTHER_USER = Main.cst(CstI18N.RECORD_CHANGED_BY_ANOTHER_USER);
					lblInfo.setText(RECORD_CHANGED_BY_ANOTHER_USER);
				}
			}

		});

		String STORE = Main.cst(CstI18N.STORE);
		btnStore.setText(STORE);

		btnRemove = new Button(shell, SWT.NONE);
		btnRemove.setBounds(474, 62, 80, 25);
		btnRemove.addSelectionListener(new SelectionAdapter() {

			@Override
			public void widgetSelected(SelectionEvent e) {

				String tenantId = currentUser.getCurrentTenant().key.tenantid;

				String productName = (String) product.getData();
				String topicName = topic.getText();
				if (topicName.trim().length() < 1) {
					String NO_TOPIC_SELECTED = Main.cst(CstI18N.NO_TOPIC_SELECTED);
					lblInfo.setText(NO_TOPIC_SELECTED);
					return;
				}

				String processName = process.getText();
				if (topicName.trim().length() < 1) {
					String NO_PROCESS_NAME = Main.cst(CstI18N.NO_PROCESS_NAME);
					lblInfo.setText(NO_PROCESS_NAME);
					return;
				}

				String processSequenceStr = processStep.getText();
				if (topicName.trim().length() < 1) {
					String NO_PROCESS_SEQ = Main.cst(CstI18N.NO_PROCESS_SEQ);
					lblInfo.setText(NO_PROCESS_SEQ);
					return;
				}
				Integer wrProcesskSequence = -1;
				try {
					wrProcesskSequence = Integer.parseInt(processSequenceStr);
				} catch (NumberFormatException nfe) {
					String NO_PROCESS_SEQ_AS_INTEGER = Main.cst(CstI18N.NO_PROCESS_SEQ_AS_INTEGER);
					lblInfo.setText(NO_PROCESS_SEQ_AS_INTEGER);
					return;

				}

				if (ProductService.isLocked(tenantId, version, productName)) {
					String VERSION_LOCKED = Main.cst(CstI18N.VERSION_LOCKED);
					lblInfo.setText(VERSION_LOCKED);
					return;
				}

				ProcessKey key = new ProcessKey(tenantId, version, productName, topicName, processName, wrProcesskSequence);
				if (processService.isDeleteMarked(key)) {
					String ALREADY_DELETE_NO_ACTION = Main.cst(CstI18N.ALREADY_DELETE_NO_ACTION);
					lblInfo.setText(ALREADY_DELETE_NO_ACTION);
					return;
				}

				btnRemove.setEnabled(true);
				lblInfo.setText("");
				try {
					processService.remove(key, currentUser.userId);
					result = 1;
					shell.dispose();
				} catch (Exception ee) {

				}
			}
		});
		String REMOVE = Main.cst(CstI18N.REMOVE);
		btnRemove.setText(REMOVE);

		lblInfo = new Label(shell, SWT.BORDER | SWT.SHADOW_IN);
		lblInfo.setBounds(10, 496, 482, 25);

		crtDat = new Label(shell, SWT.NONE);
		crtDat.setBounds(10, 434, 482, 25);

		chgDat = new Label(shell, SWT.NONE);
		chgDat.setBounds(10, 465, 482, 25);

		shortDescription = new StyledText(shell, SWT.V_SCROLL | SWT.BORDER | SWT.WRAP);
		shortDescription.setTextLimit(100);
		shortDescription.setBounds(10, 121, 544, 73);

		lblShortDescription = new Label(shell, SWT.NONE);

		String DESCRIPTION_SHORT = Main.cst(CstI18N.DESCRIPTION_SHORT);
		lblShortDescription.setText(DESCRIPTION_SHORT);
		lblShortDescription.setBounds(10, 93, 151, 25);
		
		lblPath = new Label(shell, SWT.NONE);
		String p =Main.cst(CstI18N.PRODUCT);
		String t =Main.cst(CstI18N.TOPIC);
		lblPath.setText(p + "/" + t);
		lblPath.setBounds(10, 5, 151, 25);

		String tenantId = currentUser.getCurrentTenant().key.tenantid;

		if (mode != null && mode.equals(UPDATE_MODE)) {

			product.setData(productStr);
			product.setText(productStr + " : " + version);
			topic.setText(topicStr);
			process.setText(processStr);
			processStep.setText(processStepInt.toString());
			process.setEditable(false);
			processStep.setEditable(false);

			lblInfo.setText("");
			crtDat.setText("");
			chgDat.setText("");
			ProcessKey key = new ProcessKey(tenantId, version, productStr, topicStr, processStr, processStepInt);
			ProcessRec rec = processService.get(key);
			if (rec != null) {
				shortDescription.setText(rec.shortdescr == null ? "" : rec.shortdescr);
				description.setText(rec.description == null ? "" : rec.description);
				handleInfo(rec.crtdat, rec.crtusr, rec.chgdat, rec.chgusr, rec.dltdat, rec.dltusr, rec.crtver);
				UISupport.handleSearchWords(shell, description, searchWords);
				UISupport.handleSearchWords(shell, shortDescription, searchWords);
				chgnbr = rec.chgnbr;
			}
			btnRemove.setEnabled(true);
			btnRemove.setVisible(true);

		}
		if (mode != null && mode.equals(NEW_REG_MODE)) {

			product.setData(productStr);
			product.setText(productStr + " ver." + version);
			topic.setText(topicStr);
			process.setEditable(true);
			processStep.setEditable(true);

			lblInfo.setText("");
			crtDat.setText("");
			chgDat.setText("");
			shortDescription.setText("");
			description.setText("");
			btnRemove.setEnabled(false);
			btnRemove.setVisible(false);
		}
	}

	private void handleInfo(Instant createDate, String createUser, Instant changeDate, String chgusr, Instant deleteDate, String deleteUser, Integer createdInVersion) {

		String CREATED = Main.cst(CstI18N.CREATED);
		String CHANGED = Main.cst(CstI18N.CHANGED);
		String REMOVED = Main.cst(CstI18N.REMOVED);
		String BY = Main.cst(CstI18N.BY);
		String IN_VERSION = Main.cst(CstI18N.IN_VERSION);

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

	public void setKey(ProcessKey rec, Integer version) {
		mode = UPDATE_MODE;
		this.version = version;
		productStr = rec.productName;
		topicStr = rec.topicName;
		processStr = rec.processName;
		processStepInt = rec.processSeq;
		searchWords.clear();
	}

	// create child to process
	public void setKey(TopicKey rec, Integer version) {
		mode = NEW_REG_MODE;
		this.version = version;
		productStr = rec.productName;
		topicStr = rec.topicName;
		processStr = null;
		processStepInt = null;
		searchWords.clear();
	}

	public void setCurrentUser(User user) {
		currentUser = user;
	}

	public void setSearchWords(Set<String> searchWords) {
		this.searchWords.addAll(searchWords);
	}

}
