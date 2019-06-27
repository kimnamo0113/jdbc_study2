package jdbc_study.ui.List;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.SQLException;
import java.util.List;

import javax.swing.JFrame;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.border.TitledBorder;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.TableColumnModel;

import jdbc_study.dto.Department;
import jdbc_study.ui.ErpManagementUI;

public abstract class AbstractListUi2<T> extends JFrame implements ActionListener {
	private JPanel contentPane;
	private JTable table;
	private List<T> itemList;
	private JPopupMenu popupMenu;
	private JMenuItem mntmPopUpdate;
	private JMenuItem mntmPopDelete;

	private ErpManagementUI parent;

	public AbstractListUi2() {
		initComponents();
	}

	public void setDepartmentList(List<T> itemList) {
		this.itemList = itemList;
	}

	private void initComponents() {
		setTitle("부서 목록");
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setBounds(100, 100, 450, 300);
		contentPane = new JPanel();
		contentPane.setBorder(new TitledBorder(null, "부서 목록", TitledBorder.LEADING, TitledBorder.TOP, null, null));
		contentPane.setLayout(new BorderLayout(0, 0));
		setContentPane(contentPane);

		JScrollPane scrollPane = new JScrollPane();
		contentPane.add(scrollPane, BorderLayout.CENTER);

		table = new JTable();
		scrollPane.setViewportView(table);

		popupMenu = new JPopupMenu();

		mntmPopUpdate = new JMenuItem("수정");
		mntmPopUpdate.addActionListener(this);
		popupMenu.add(mntmPopUpdate);

		mntmPopDelete = new JMenuItem("삭제");
		mntmPopDelete.addActionListener(this);
		popupMenu.add(mntmPopDelete);

		table.setComponentPopupMenu(popupMenu);
		scrollPane.setComponentPopupMenu(popupMenu);
	}

	private Object[][] getRows() {
		Object[][] rows = new Object[itemList.size()][];
		for (int i = 0; i < itemList.size(); i++) {
			rows[i] = getArray(i);
		}
		return rows;
	}

	// 테이블 셀 내용의 정렬
	protected void tableCellAlignment(int align, int... idx) {
		DefaultTableCellRenderer dtcr = new DefaultTableCellRenderer();
		dtcr.setHorizontalAlignment(align);

		TableColumnModel model = table.getColumnModel();
		for (int i = 0; i < idx.length; i++) {
			model.getColumn(idx[i]).setCellRenderer(dtcr);
		}
	}

	// 테이블 셀의 폭 설정
	protected void tableSetWidth(int... width) {
		TableColumnModel cModel = table.getColumnModel();

		for (int i = 0; i < width.length; i++) {
			cModel.getColumn(i).setPreferredWidth(width[i]);
		}
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		if (e.getSource() == mntmPopUpdate) {
			updateUI();
		}
		if (e.getSource() == mntmPopDelete) {
			try {
				deleteUI();
			} catch (SQLException e1) {
				e1.printStackTrace();
			}
		}
	}

	private void deleteUI() throws SQLException {
		int i = table.getSelectedRow();

		if (table.getModel().getRowCount() == 0) { // 부서정보가 존재하지 않을 경우
			parent.showDepartmentUI();
			return;
		}
		if (i < 0 || i > table.getModel().getRowCount() - 1) { // 선택하지 않은 경우
			JOptionPane.showMessageDialog(null, "선택된 부서가 없습니다.");
			return;
		}

		int deptNo = (int) table.getModel().getValueAt(i, 0);

		parent.actionPerformedBtnDeptDelete(deptNo);
	}
	
	private void updateUI() {
		int i = table.getSelectedRow();

		if (table.getModel().getRowCount() == 0) { // 부서정보가 존재하지 않을 경우
			parent.showDepartmentUI();
			return;
		}
		if (i < 0 || i > table.getModel().getRowCount() - 1) { // 선택하지 않은 경우
			JOptionPane.showMessageDialog(null, "선택된 부서가 없습니다.");
			return;
		}

		int deptNo = (int) table.getModel().getValueAt(i, 0);

//		T searchDept = itemList.get(itemList.indexOf(new Department(deptNo)));
		T searchDept=hi();
		parent.showDepartmentUI(searchDept);
	}
	
	
	protected abstract String[] getColumnNames();

	public abstract Object[] getArray(int index);
//	itemList.get(itemList.indexOf(new Department(deptNo)));
	public abstract T hi(); 
	public abstract void reloadData();

}
