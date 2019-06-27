package jdbc_study.ui.List;

import java.sql.SQLException;

import javax.swing.JOptionPane;
import javax.swing.SwingConstants;
import javax.swing.table.DefaultTableModel;

import jdbc_study.dto.Employee;

@SuppressWarnings("serial")
public class EmployeeListUi extends AbstractListUi<Employee> {

	public EmployeeListUi() {
		super("사원 관리");
	}

	@Override
	public Object[] getItemArray(int index) {
		return itemList.get(index).toArray();
	}

	@Override
	public void reloadData() {
		table.setModel(new DefaultTableModel(getRows(),getColumnNames()));
		
		// 사원번호, 사원명, 직책, 직속상사, 부서정보 은 가운데 정렬
		tableCellAlignment(SwingConstants.CENTER, 0,1,2,3,5);
		// 급여은 우측 정렬
		tableCellAlignment(SwingConstants.RIGHT, 4);	
		// 부서번호, 부서명, 위치 의 폭을 (100, 200, 70)으로 가능하면 설정 
		tableSetWidth(70, 100, 70, 100, 120, 100);		
	}

	@Override
	public String[] getColumnNames() {
		return new String[] {"사원번호", "사원명", "직책", "직속상사", "급여", "부서"};
	}


	@Override
	public void updateUI() throws SQLException {
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
		Employee emp = itemList.get(itemList.indexOf(new Employee(empNo)));

		parent.showEmployeeUI(emp);
	}

	@Override
	public void deleteUI() throws SQLException {
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

}
