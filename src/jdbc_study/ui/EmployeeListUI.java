package jdbc_study.ui;

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
import javax.swing.SwingConstants;
import javax.swing.border.TitledBorder;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumnModel;

import jdbc_study.dto.Employee;

@SuppressWarnings("serial")
public class EmployeeListUI extends JFrame implements ActionListener{

	private JPanel contentPane;
	private JTable table;
	private List<Employee> empList;
	private JPopupMenu popupMenu;
	private JMenuItem mntmPopUpdate;
	private JMenuItem mntmPopDelete;
	
	private ErpManagementUI parent;
	
	public EmployeeListUI() {
		initComponents();
	}
	
	public void setEmployeeList(List<Employee> empList) {
		this.empList = empList;
	}

	private void initComponents() {
		setTitle("사원 목록");
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setBounds(100, 100, 450, 300);
		contentPane = new JPanel();
		contentPane.setBorder(new TitledBorder(null, "사원 목록", TitledBorder.LEADING, TitledBorder.TOP, null, null));
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

	public void reloadData() {
		table.setModel(new DefaultTableModel(getRows(),getColumnNames()));
		
		// 사원번호, 사원명, 직책, 직속상사, 부서정보 은 가운데 정렬
		tableCellAlignment(SwingConstants.CENTER, 0,1,2,3,5);
		// 급여은 우측 정렬
		tableCellAlignment(SwingConstants.RIGHT, 4);	
		// 부서번호, 부서명, 위치 의 폭을 (100, 200, 70)으로 가능하면 설정 
		tableSetWidth(70, 100, 70, 100, 120, 100);
	}

	private Object[][] getRows() {
		Object[][] rows = new Object[empList.size()][];
		for(int i=0; i<empList.size(); i++) {
			rows[i] = empList.get(i).toArray();
		}
		return rows;
	}

	private String[] getColumnNames() {
		return new String[] {"사원번호", "사원명", "직책", "직속상사", "급여", "부서"};
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
		try {
			if (e.getSource() == mntmPopUpdate) {
				updateUI();
			}
			
			if (e.getSource() == mntmPopDelete) {
				deleteUI();
			}
		} catch (SQLException e1) {
			e1.printStackTrace();
		}
	}

	private void deleteUI() throws SQLException {
		int i = table.getSelectedRow();
		if (table.getModel().getRowCount() == 0) {	// 부서정보가 존재하지 않을 경우
			parent.showEmployeeUI();
			return;
		}
		if (i  < 0 || i > table.getModel().getRowCount()-1) { //선택하지 않은 경우
			JOptionPane.showMessageDialog(null, "선택된 사원 없습니다.");
			return;
		}
		int empNo = (int) table.getModel().getValueAt(i, 0);
		parent.actionPerformedBtnEmpDelete(empNo);
	}

	private void updateUI() throws SQLException {
		int i = table.getSelectedRow();
		if (table.getModel().getRowCount() == 0) {	// 부서정보가 존재하지 않을 경우
			parent.showEmployeeUI();
			return;
		}
		if (i  < 0 || i > table.getModel().getRowCount()-1) { //선택하지 않은 경우
			JOptionPane.showMessageDialog(null, "선택된 사원 없습니다.");
			return;
		}
		int empNo = (int) table.getModel().getValueAt(i, 0);
		Employee emp = empList.get(empList.indexOf(new Employee(empNo)));

		parent.showEmployeeUI(emp);
	}

	public void setErpManagementUI(ErpManagementUI erpManagementUI) {
		this.parent = erpManagementUI;
	}

}
