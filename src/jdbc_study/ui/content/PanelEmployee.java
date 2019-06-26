package jdbc_study.ui.content;

import java.awt.GridLayout;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Vector;

import javax.swing.BoxLayout;
import javax.swing.DefaultComboBoxModel;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.border.TitledBorder;
import javax.swing.filechooser.FileNameExtensionFilter;

import jdbc_study.dto.Department;
import jdbc_study.dto.Employee;
import java.awt.BorderLayout;

@SuppressWarnings("serial")
public class PanelEmployee extends JPanel implements ActionListener {
	private JTextField tfEmpNo;
	private JTextField tfEmpName;
	private JTextField tfTitle;
	private JTextField tfSalary;
	private JComboBox<Department> cmbDept;
	private JComboBox<Employee> cmbEmp;
	private DefaultComboBoxModel<Department> deptCmbModel;
	private DefaultComboBoxModel<Employee> empCmbModel;
	private JLabel lblImg;

	private String imgPath;
	private int imgWidth;
	private int imgHeight;
	private JButton btnImgAdd;

	private JFileChooser chooser;
	private String selectedImpPath;
	private File picsDir;
	

	public PanelEmployee() {
		imgPath = System.getProperty("user.dir") + "\\images\\";
		imgWidth = 180;
		imgHeight = 180;

		chooser = new JFileChooser(imgPath);

		initComponents();
		
		selectedImpPath = imgPath + "noImg.jpg";
		switchImage(imgPath + "noImg.jpg");
		
		picsDir = new File(System.getProperty("user.dir") + System.getProperty("file.separator") + "pics"
				+ System.getProperty("file.separator"));

		if (!picsDir.exists()) {
			picsDir.mkdir();
		}
	}

	private void initComponents() {
		setBorder(new TitledBorder(null, "사원 정보", TitledBorder.LEADING, TitledBorder.TOP, null, null));
		setLayout(new BorderLayout(0, 0));

		JPanel panel = new JPanel();
		add(panel, BorderLayout.WEST);
		panel.setLayout(new BorderLayout(0, 0));

		lblImg = new JLabel();
		panel.add(lblImg);
		lblImg.setSize(imgWidth, imgHeight);

		btnImgAdd = new JButton("사진 추가");
		btnImgAdd.addActionListener(this);
		panel.add(btnImgAdd, BorderLayout.SOUTH);

		JPanel pEmp = new JPanel();
		add(pEmp);
		pEmp.setLayout(new GridLayout(0, 2, 20, 10));

		JLabel lblEmpNo = new JLabel("사원 번호");
		pEmp.add(lblEmpNo);
		lblEmpNo.setHorizontalAlignment(SwingConstants.RIGHT);

		tfEmpNo = new JTextField();
		pEmp.add(tfEmpNo);
		tfEmpNo.setColumns(10);

		JLabel lblEmpName = new JLabel("사원명");
		pEmp.add(lblEmpName);
		lblEmpName.setHorizontalAlignment(SwingConstants.RIGHT);

		tfEmpName = new JTextField();
		pEmp.add(tfEmpName);
		tfEmpName.setColumns(10);

		JLabel lblTitle = new JLabel("직책");
		pEmp.add(lblTitle);
		lblTitle.setHorizontalAlignment(SwingConstants.RIGHT);

		tfTitle = new JTextField();
		pEmp.add(tfTitle);
		tfTitle.setColumns(10);

		JLabel lblDept = new JLabel("부서");
		pEmp.add(lblDept);
		lblDept.setHorizontalAlignment(SwingConstants.RIGHT);

		cmbDept = new JComboBox<Department>();
		pEmp.add(cmbDept);

		JLabel lblManager = new JLabel("직속상사");
		pEmp.add(lblManager);
		lblManager.setHorizontalAlignment(SwingConstants.RIGHT);

		cmbEmp =  new JComboBox<Employee>();		
		pEmp.add(cmbEmp);

		JLabel lblSalary = new JLabel("급여");
		pEmp.add(lblSalary);
		lblSalary.setHorizontalAlignment(SwingConstants.RIGHT);

		tfSalary = new JTextField();
		pEmp.add(tfSalary);
		tfSalary.setColumns(10);
	}

	private void switchImage(String filePath) {
		Image tmpIcon = new ImageIcon(filePath).getImage().getScaledInstance(imgWidth, imgHeight, Image.SCALE_SMOOTH);
		ImageIcon imageIcon = new ImageIcon(tmpIcon);
		lblImg.setIcon(imageIcon);
	}

	public void setEmployee(Employee emp) {
		tfEmpNo.setText(emp.getEmpNo() + "");
		tfEmpName.setText(emp.getEmpName());
		tfTitle.setText(emp.getTitle());
		tfSalary.setText(emp.getSalary() + "");
//		tfManager.setText(emp.getManager().getEmpNo() + "");
		cmbEmp.setSelectedItem(new Employee(emp.getManager().getEmpNo()));
		
//		cmbDept.setText(emp.getDno().getDeptNo() + "");
		cmbDept.setSelectedItem(new Department(emp.getDno().getDeptNo()));
		if (emp.getPic() != null) {
			try {
				File imgFile = getPicFile(emp);
				switchImage(imgFile.getAbsolutePath());
			} catch (IOException e) {
				e.printStackTrace();
			}
		} else {
			switchImage(imgPath + "noImg.jpg");
		}
		btnImgAdd.setText("변경");
	}

	public Employee getEmployee() {
		int empNo = Integer.parseInt(tfEmpNo.getText().trim());
		String empName = tfEmpName.getText().trim();
		String title = tfTitle.getText().trim();
		int salary = Integer.parseInt(tfSalary.getText().trim());
		Employee manager = (Employee) cmbEmp.getSelectedItem();
		Department dno = (Department) cmbDept.getSelectedItem();

		return new Employee(empNo, empName, title, manager, salary, dno, getImage());
	}

	public void clearTextField() {
		tfEmpNo.setText("");
		tfEmpName.setText("");
		tfTitle.setText("");
		cmbEmp.setSelectedIndex(-1);
		tfSalary.setText("");
		cmbDept.setSelectedIndex(-1);
		switchImage(imgPath + "noImg.jpg");
		if (!tfEmpNo.isEditable()) {
			tfEmpNo.setEditable(true);
		}
		if (btnImgAdd.getText().equals("변경")) {
			btnImgAdd.setText("사진 추가");
		}

	}

	public JTextField getTfEmpNo() {
		return tfEmpNo;
	}

	public void setTfAllEditable(boolean isEditable) {
		tfEmpNo.setEditable(isEditable);
		tfEmpName.setEditable(isEditable);
		tfTitle.setEditable(isEditable);
		cmbEmp.setEditable(isEditable);
		tfSalary.setEditable(isEditable);
		cmbDept.setEditable(isEditable);
		btnImgAdd.setVisible(false);
	}

	public void actionPerformed(ActionEvent e) {
		if (e.getSource() == btnImgAdd) {
			actionPerformedBtnImgAdd(e);
		}

	}

	protected void actionPerformedBtnImgAdd(ActionEvent e) {
		FileNameExtensionFilter filter = new FileNameExtensionFilter("JPG Images", "jpg");
		chooser.setFileFilter(filter);

		int ret = chooser.showOpenDialog(null);
		if (ret != JFileChooser.APPROVE_OPTION) {
			JOptionPane.showMessageDialog(null, "파일을 선택하지 않았습니다.", "경고", JOptionPane.WARNING_MESSAGE);
			return;
		}
		selectedImpPath = chooser.getSelectedFile().getPath();

		switchImage(selectedImpPath);
		btnImgAdd.setText("변경");
		repaint();
		revalidate();
	}

	private byte[] getImage() {
		byte[] pic = null;

		File imgFile = new File(selectedImpPath);

		try (InputStream is = new FileInputStream(imgFile);) {
			pic = new byte[is.available()];
			is.read(pic);
		} catch (FileNotFoundException e) {
			System.out.println("해당 파일을 찾을 수 없음");
		} catch (IOException e) {
			e.printStackTrace();
		}
		return pic;
	}

	private File getPicFile(Employee e) throws FileNotFoundException, IOException {
		File file = null;
		file = new File(picsDir, e.getEmpName() + ".jpg");
		try (FileOutputStream fos = new FileOutputStream(file)) {
			fos.write(e.getPic());
		}
		return file;
	}

	
	public void setCmbDeptModel(List<Department> deptList) {
		deptCmbModel = new DefaultComboBoxModel<Department>(new Vector<Department>(deptList));
		cmbDept.setModel(deptCmbModel);
	}
	
	
	public void setCmbEmpModel(List<Employee> empList) {
		empCmbModel = new DefaultComboBoxModel<Employee>(new Vector<Employee>(empList));
		cmbEmp.setModel(empCmbModel);
	}
	
	public JComboBox<Department> getCmbDept() {
		return cmbDept;
	}
	public JComboBox<Employee> getCmbEmp() {
		return cmbEmp;
	}
}
