package jdbc_study.ui.List;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.SQLException;
import java.util.List;

import javax.swing.JFrame;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.border.TitledBorder;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.TableColumnModel;

import jdbc_study.dto.Employee;
import jdbc_study.ui.ErpManagementUI;

@SuppressWarnings("serial")
public abstract class AbstractListUi<T> extends JFrame implements ActionListener{
	private JPanel contentPane;
	protected JTable table;
	protected List<T> itemList;
	private JPopupMenu popupMenu;
	private JMenuItem mntmPopUpdate;
	private JMenuItem mntmPopDelete;
	
	protected ErpManagementUI parent;
	
	public AbstractListUi(String title) {
		setTitle(title);
		initComponents();
	}
	private void initComponents() {
		
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
	
	
	public Object[][] getRows() {
		Object[][] rows = new Object[itemList.size()][];
		for(int i=0; i<itemList.size(); i++) {
//			rows[i] = itemList.get(i).toArray();
			rows[i] = getItemArray(i);
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
		
		
		public void setErpManagementUI(ErpManagementUI erpManagementUI) {
			this.parent = erpManagementUI;
		}
		
		public void setItemList(List<T> itemList) {
			this.itemList = itemList;
		}
		
		public abstract Object[] getItemArray(int index);
		public abstract void reloadData(); 
		public abstract String[] getColumnNames();
		public abstract void deleteUI() throws SQLException;
		public abstract void updateUI() throws SQLException;
		
}
