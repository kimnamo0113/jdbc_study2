package jdbc_study.ui;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import jdbc_study.dao.DepartmentDao;
import jdbc_study.dao.EmployeeDao;
import jdbc_study.dto.Department;
import jdbc_study.dto.Employee;
import jdbc_study.ui.content.PanelEmployee;

@SuppressWarnings("serial")
public class EmployeeUI extends JFrame implements ActionListener, ItemListener {

	private JPanel contentPane;
	private JButton btnAdd;
	private JButton btnClear;
	private PanelEmployee pContent;

	private EmployeeDao empDao;
	private DepartmentDao depDao;
	
	private ErpManagementUI erpManagementUI;
	
	public EmployeeUI() {
//		dao = new DepartmentDaoImpl();
		initComponents();
	}
	
	public void setEmpDao(EmployeeDao dao) throws SQLException {
		this.empDao = dao;
		this.pContent.setCmbEmpModel(dao.selectEmployeeByAll());
		this.pContent.getCmbEmp().addItemListener(this);
	}
	
	public void setDepDao(DepartmentDao dao) {
		this.depDao = dao;
		this.pContent.setCmbDeptModel(dao.selectDepartmentByAll());
		this.pContent.getCmbDept().addItemListener(this);
	}

	private void initComponents() {
		setTitle("사원관리");
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setBounds(100, 100, 420, 419);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		contentPane.setLayout(new BorderLayout(0, 0));
		setContentPane(contentPane);
		
		pContent = new PanelEmployee();
		contentPane.add(pContent, BorderLayout.CENTER);
		
		JPanel pBtn = new JPanel();
		contentPane.add(pBtn, BorderLayout.SOUTH);
		
		btnAdd = new JButton("추가");
		btnAdd.addActionListener(this);
		pBtn.add(btnAdd);
		
		btnClear = new JButton("취소");
		btnClear.addActionListener(this);
		pBtn.add(btnClear);
	}

	public void actionPerformed(ActionEvent e) {
		if (e.getSource() == btnClear) {
			actionPerformedBtnClear(e);
		}
		if (e.getSource() == btnAdd) {
			if (btnAdd.getText().equals("추가")) {
				actionPerformedBtnAdd(e);
			}else {
				actionPerformedBtnUpdate(e);
			}
		}
	}
	private void actionPerformedBtnUpdate(ActionEvent e) {
		Employee newEmp = pContent.getEmployee();
		System.out.println("----------1---------");
		System.out.println(newEmp);
		int res;
		try {
			res = empDao.updateEmployee(newEmp);
			if (res != -1) {
				JOptionPane.showMessageDialog(null, String.format("%s 사원이 수정되었습니다.", newEmp.getEmpName()));
				pContent.clearTextField();
				btnAdd.setText("추가");
				
			}
			erpManagementUI.refreshListUI();
		} catch (SQLException e1) {
			e1.printStackTrace();
		}		
	}

	protected void actionPerformedBtnAdd(ActionEvent e) {
		Employee newEmp = pContent.getEmployee();
		System.out.println("--------1--------");
		System.out.println(newEmp);
		int res;
		try {
			res = empDao.insertEmployee(newEmp);
			if (res != -1) {
				JOptionPane.showMessageDialog(null, String.format("%s 사원이 추가되었습니다.", newEmp.getEmpName()));
				pContent.clearTextField();
			}
			erpManagementUI.refreshListUI();
		} catch (SQLException e1) {
			e1.printStackTrace();
		}

	}
	
	protected void actionPerformedBtnClear(ActionEvent e) {
		pContent.clearTextField();
	}
	
	public void setEmployee(Employee emp) {
		pContent.setEmployee(emp);
		pContent.getTfEmpNo().setEditable(false);
		btnAdd.setText("수정");
	}

	public void setParent(ErpManagementUI erpManagementUI) {
		this.erpManagementUI = erpManagementUI;
	}
	
	public void clearEmployee() {
		pContent.clearTextField();
		btnAdd.setText("추가");
	}

	@Override
	public void itemStateChanged(ItemEvent e) {
		if(e.getItem() instanceof Department) {
			if (e.getStateChange() == ItemEvent.SELECTED) {
				Department dept = (Department) e.getItem();
				List<Employee> empList = new Vector<Employee>();
				empList=depDao.selectDeptNo(dept);
				pContent.setCmbEmpModel(empList);
			}
		}
		if(e.getItem() instanceof Employee) {
			if (e.getStateChange() == ItemEvent.SELECTED) {
				Employee emp = (Employee) e.getItem();
			}
		}
	}
	
}

