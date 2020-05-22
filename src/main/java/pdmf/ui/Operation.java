package pdmf.ui;

import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.List;

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

import pdmf.model.Cst;
import pdmf.model.OperationKey;
import pdmf.model.OperationRec;
import pdmf.model.ProcessKey;
import pdmf.model.User;
import pdmf.service.OperationService;
import pdmf.service.ProductService;
import pdmf.sys.RecordChangedByAnotherUser;

public class Operation extends Dialog {

	private static final String NEW_REG_MODE = "ny post";
	private static final String UPDATE_MODE = "ändra post";
	private String mode = null;

	private User currentUser;

	private OperationService operationService = new OperationService();

	protected Object result;
	protected Shell shell;
	private Label product;
	private Label topic;
	private Button btnStore = null;
	private Button btnRemove = null;
	private Label lblInfo = null;
	private StyledText description;
	private Label process;
	private Label processStep;

	private Integer chgnbr = null;
	private Text operation;
	private Text operationStep;
	private Label lblOperation;
	private Label lblOstep;

	private StyledText shortDescription;
	private Label lblShortDescription;
	private Label crtDat;
	private Label chgDat;

	private Integer version = null;

	String productStr;
	String topicStr;
	String processStr;
	Integer processStepInt;
	String operationStr;
	Integer operationStepInt;

	private List<String> searchWords = new ArrayList<String>();

	/**
	 * Create the dialog.
	 * 
	 * @param parent
	 * @param style
	 */
	public Operation(Shell parent, int style) {
		super(parent, style);
		setText("[" + Cst.OPERATION + "]");
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
//		shell = new Shell(getParent(), SWT.DIALOG_TRIM | SWT.APPLICATION_MODAL);
		shell = new Shell(getParent(), getStyle());

		shell.setSize(518, 560);
		shell.setText(getText() + " " + mode);
		shell.setLayout(null);

		lblOperation = new Label(shell, SWT.NONE);
		lblOperation.setText(Cst.OPERATION);
		lblOperation.setBounds(167, 10, 82, 15);

		lblOstep = new Label(shell, SWT.NONE);
		lblOstep.setText(Cst.OPERATION_SEQ);
		lblOstep.setBounds(349, 10, 25, 15);

		Label lblDescription = new Label(shell, SWT.NONE);
		lblDescription.setBounds(167, 162, 151, 15);
		lblDescription.setText(Cst.DESCRIPTION);

		product = new Label(shell, SWT.BORDER);
		product.setBounds(10, 32, 151, 25);

		topic = new Label(shell, SWT.BORDER);
		topic.setBounds(10, 62, 151, 25);

		process = new Label(shell, SWT.BORDER);
		process.setBounds(10, 92, 151, 25);

		processStep = new Label(shell, SWT.BORDER);
		processStep.setBounds(10, 122, 57, 25);

		operation = new Text(shell, SWT.BORDER);
		operation.setBounds(167, 32, 177, 25);

		operationStep = new Text(shell, SWT.BORDER);
		operationStep.setBounds(349, 32, 57, 25);

		description = new StyledText(shell, SWT.V_SCROLL | SWT.WRAP | SWT.BORDER);
		description.setBounds(167, 183, 239, 227);
		description.setTextLimit(995);

		btnStore = new Button(shell, SWT.NONE);
		btnStore.setBounds(412, 32, 80, 25);
		btnStore.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {

				Integer tenantId = currentUser.getCurrentTenant().getId();

				String wrkProductName = (String) product.getData();

				String wrkTopicName = topic.getText();
				if (wrkTopicName == null || wrkTopicName.trim().length() < 1) {
					lblInfo.setText(Cst.NO_TOPIC_SELECTED);
					return;
				}

				String wrkProcessName = process.getText();
				if (wrkProcessName == null || wrkProcessName.trim().length() < 1) {
					lblInfo.setText(Cst.NO_PROCESS_NAME);
					return;
				}

				String wrkProcessStepNameStr = processStep.getText();
				if (wrkProcessStepNameStr == null || wrkProcessStepNameStr.trim().length() < 1) {
					lblInfo.setText(Cst.NO_PROCESS_SEQ);
					return;
				}
				Integer wrkProcessStep = 0;
				try {
					wrkProcessStep = Integer.parseInt(wrkProcessStepNameStr);
				} catch (NumberFormatException nfe) {
					lblInfo.setText(Cst.NO_PROCESS_SEQ_AS_INTEGER);
					return;

				}

				String operationName = operation.getText();
				if (operationName.trim().length() < 1) {
					lblInfo.setText(Cst.NO_OPERATION_NAME);
					return;
				}

				String operationSequenceStr = operationStep.getText();
				if (operationSequenceStr.trim().length() < 1) {
					lblInfo.setText(Cst.NO_OPERATION_SEQ);
					return;
				}
				Integer operationSequence = -1;
				try {
					operationSequence = Integer.parseInt(operationSequenceStr);
				} catch (NumberFormatException nfe) {
					lblInfo.setText(Cst.NO_OPERATION_SEQ_AS_INTEGER);
					return;

				}

				if (ProductService.isLocked(tenantId, version, wrkProductName)) {
					lblInfo.setText(Cst.VERSION_LOCKED);
					return;
				}

				OperationKey key = new OperationKey(tenantId, version, wrkProductName, wrkTopicName, wrkProcessName,
						wrkProcessStep, operationName, operationSequence);
				if (operationService.isDeleteMarked(key)) {
					lblInfo.setText(Cst.ALREADY_DELETE_NO_ACTION);
					return;
				}

				OperationRec rec = operationService.get(tenantId, version, wrkProductName, wrkTopicName, wrkProcessName,
						wrkProcessStep, operationName, operationSequence);

				if (mode.equals(NEW_REG_MODE)) {
					if (rec != null) {
						lblInfo.setText(Cst.ALREADY_EXISTS);
						return;
					}
				}

				if (rec == null) {
					rec = new OperationRec(key, null, null, null);
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
					operationService.store(rec, currentUser.userId);
					chgnbr = null;
					result = 1;
					shell.dispose();
				} catch (RecordChangedByAnotherUser rc) {
					lblInfo.setText(Cst.RECORD_CHANGED_BY_ANOTHER_USER);
				}
			}

		});
		btnStore.setText(Cst.STORE);

		btnRemove = new Button(shell, SWT.NONE);
		btnRemove.setBounds(412, 62, 80, 25);
		btnRemove.addSelectionListener(new SelectionAdapter() {

			@Override
			public void widgetSelected(SelectionEvent e) {

				Integer tenantId = currentUser.getCurrentTenant().getId();

				String productName = (String) product.getData();
				String topicName = topic.getText();
				if (topicName.trim().length() < 1) {
					lblInfo.setText(Cst.NO_TOPIC_SELECTED);
					return;
				}

				String processName = process.getText();
				if (topicName.trim().length() < 1) {
					lblInfo.setText(Cst.NO_PROCESS_NAME);
					return;
				}

				String processSequenceStr = processStep.getText();
				if (topicName.trim().length() < 1) {
					lblInfo.setText(Cst.NO_PROCESS_SEQ);
					return;
				}
				Integer wrProcesskSequence = -1;
				try {
					wrProcesskSequence = Integer.parseInt(processSequenceStr);
				} catch (NumberFormatException nfe) {
					lblInfo.setText(Cst.NO_PROCESS_SEQ_AS_INTEGER);
					return;

				}

				String operationName = operation.getText();
				if (operationName.trim().length() < 1) {
					lblInfo.setText(Cst.NO_OPERATION_NAME);
					return;
				}

				String operationSequenceStr = operationStep.getText();
				if (operationSequenceStr.trim().length() < 1) {
					lblInfo.setText(Cst.NO_OPERATION_SEQ);
					return;
				}
				Integer operationSequence = -1;
				try {
					operationSequence = Integer.parseInt(operationSequenceStr);
				} catch (NumberFormatException nfe) {
					lblInfo.setText(Cst.NO_OPERATION_SEQ_AS_INTEGER);
					return;

				}

				if (ProductService.isLocked(tenantId, version, productName)) {
					lblInfo.setText(Cst.VERSION_LOCKED);
					return;
				}

				OperationKey key = new OperationKey(tenantId, version, productName, topicName, processName,
						wrProcesskSequence, operationName, operationSequence);
				if (operationService.isDeleteMarked(key)) {
					lblInfo.setText(Cst.ALREADY_DELETE_NO_ACTION);
					return;
				}

				btnRemove.setEnabled(true);
				lblInfo.setText("");
				try {
					operationService.remove(tenantId, version, productName, topicName, processName, wrProcesskSequence,
							operationName, operationSequence, currentUser.userId);
					result = 1;
					shell.dispose();
				} catch (Exception ee) {

				}
			}
			
			
			
		});
		btnRemove.setText(Cst.REMOVE);

		lblInfo = new Label(shell, SWT.BORDER | SWT.SHADOW_IN);
		lblInfo.setText("i");
		lblInfo.setBounds(10, 484, 482, 25);

		crtDat = new Label(shell, SWT.NONE);
		crtDat.setText("crt");
		crtDat.setBounds(10, 422, 482, 25);

		chgDat = new Label(shell, SWT.NONE);
		chgDat.setText("chg");
		chgDat.setBounds(10, 453, 482, 25);

		shortDescription = new StyledText(shell, SWT.V_SCROLL | SWT.BORDER | SWT.WRAP);
		shortDescription.setTextLimit(100);
		shortDescription.setBounds(167, 83, 239, 73);

		lblShortDescription = new Label(shell, SWT.NONE);
		lblShortDescription.setText(Cst.DESCRIPTION_SHORT);
		lblShortDescription.setBounds(167, 60, 151, 15);

		Integer tenantId = currentUser.getCurrentTenant().getId();

		if (mode != null && mode.equals(UPDATE_MODE)) {

			product.setData(productStr);
			product.setText(productStr + " ver." + version);
			topic.setText(topicStr);
			process.setText(processStr);
			processStep.setText(processStepInt.toString());
			operation.setText(operationStr);
			operationStep.setText(operationStepInt.toString());
			operation.setEditable(false);
			operationStep.setEditable(false);

			lblInfo.setText("");
			crtDat.setText("");
			chgDat.setText("");
			OperationRec rec = operationService.get(tenantId, version, productStr, topicStr, processStr, processStepInt,
					operationStr, operationStepInt);
			if (rec != null) {
				shortDescription.setText(rec.shortdescr == null ? "" : rec.shortdescr);
				description.setText(rec.description == null ? "" : rec.description);
				handleInfo(rec.crtdat, rec.crtusr, rec.chgdat, rec.chgusr, rec.dltdat, rec.dltusr, rec.crtver);
			}
			btnRemove.setEnabled(true);
			btnRemove.setVisible(true);

		}
		if (mode != null && mode.equals(NEW_REG_MODE)) {

			product.setData(productStr);
			product.setText(productStr + " ver." + version);
			topic.setText(topicStr);
			process.setText(processStr);
			processStep.setText(processStepInt.toString());
			operation.setText("");
			operationStep.setText(String.valueOf(0));
			operation.setEditable(true);
			operationStep.setEditable(true);

			lblInfo.setText("");
			crtDat.setText("");
			chgDat.setText("");
			shortDescription.setText("");
			description.setText("");
			btnRemove.setEnabled(false);
			btnRemove.setVisible(false);
		}
		

		System.out.println("sökord strl " + searchWords.size());

		if(searchWords.size() > 0) {
			
			System.out.println("DET FINNS SÖKORD");
			
		}else {

			System.out.println("DET FINNS INGA EJ INTE SÖKORD");

			
		}


	}

	private void handleInfo(Instant createDate, String createUser, Instant changeDate, String chgusr,
			Instant deleteDate, String deleteUser, Integer createdInVersion) {

		LocalDate created = LocalDateTime.ofInstant(createDate, ZoneOffset.UTC).toLocalDate();
		crtDat.setText("Skapad: " + created.toString() + " av " + createUser + " i version: " + createdInVersion);

		chgDat.setText("");
		if (changeDate != null && chgusr != null) {
			LocalDate changed = LocalDateTime.ofInstant(changeDate, ZoneOffset.UTC).toLocalDate();
			chgDat.setText("Ändrad: " + changed.toString() + " av " + chgusr);
		}
		if (deleteDate != null && deleteUser != null) {
			LocalDate deleted = LocalDateTime.ofInstant(deleteDate, ZoneOffset.UTC).toLocalDate();
			chgDat.setText("Borttagen: " + deleted.toString() + " av " + deleteUser);
		}

	}

	public void setKey(OperationKey rec, Integer version) {
		mode = UPDATE_MODE;
		this.version = version;
		productStr = rec.productName;
		topicStr = rec.topicName;
		processStr = rec.processName;
		processStepInt = rec.sequence;
		operationStr = rec.operationName;
		operationStepInt = rec.operationSequence;
		searchWords.clear();
	}

	// create child to process
	public void setKey(ProcessKey rec, Integer version) {
		mode = NEW_REG_MODE;
		this.version = version;
		productStr = rec.productName;
		topicStr = rec.topicName;
		processStr = rec.processName;
		processStepInt = rec.processSeq;
		operationStr = null;
		operationStepInt = null;
		searchWords.clear();
	}

	public void setCurrentUser(User user) {
		currentUser = user;
	}

	public void setSearchWords(List<String> searchWords) {
		this.searchWords.addAll(searchWords);
	}

}
