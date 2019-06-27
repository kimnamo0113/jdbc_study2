package jdbc_study.ui.List;

import java.sql.SQLException;

import javax.swing.JOptionPane;
import javax.swing.SwingConstants;
import javax.swing.table.DefaultTableModel;

import jdbc_study.dto.Department;

@SuppressWarnings("serial")
public class DepartmentListUi extends AbstractListUi<Department> {

	public DepartmentListUi() {
		super("부서 관리");
	}

	@Override
	public Object[] getItemArray(int index) {
		return itemList.get(index).toArray();
	}

	@Override
	public void reloadData() {
		table.setModel(new DefaultTableModel(getRows(), getColumnNames()));

		// 부서번호, 부서명은 가운데 정렬
		tableCellAlignment(SwingConstants.CENTER, 0, 1);
		// 위치(층)은 우측 정렬
		tableCellAlignment(SwingConstants.RIGHT, 2);
		// 부서번호, 부서명, 위치 의 폭을 (100, 200, 70)으로 가능하면 설정
		tableSetWidth(100, 200, 70);
	}

	@Override
	public String[] getColumnNames() {
		return new String[] { "부서번호", "부서명", "위치(층)" };
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

	@Override
	public void updateUI() throws SQLException {
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

		Department searchDept = itemList.get(itemList.indexOf(new Department(deptNo)));
		parent.showDepartmentUI(searchDept);
	}
	
}
