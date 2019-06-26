package jdbc_study.daoimpl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import jdbc_study.dao.EmployeeDao;
import jdbc_study.dto.Department;
import jdbc_study.dto.Employee;
import jdbc_study.jdbc.MySQLjdbcUtil;

public class EmployeeDaoImpl implements EmployeeDao {
	static final Logger log = LogManager.getLogger();
	
	@Override
	public List<Employee> selectEmployeeByAll() throws SQLException {
		String sql = "SELECT empno, empname, title, manager, salary, dno, pic FROM employee";
		List<Employee> lists = null;
		try(Connection conn = MySQLjdbcUtil.getConnection();
				PreparedStatement pstmt = conn.prepareStatement(sql);
				ResultSet rs = pstmt.executeQuery()){
			log.trace(pstmt);
			if (rs.next()) {
				lists = new ArrayList<Employee>();
				do {
					lists.add(getEmployee(rs));
				}while(rs.next());
			}

		}
		return lists;
	}

	private Employee getEmployee(ResultSet rs) throws SQLException {
		return new Employee(rs.getInt("empno"), 
				            rs.getString("empname"), 
				            rs.getString("title"), 
				            new Employee(rs.getInt("manager")), 
				            rs.getInt("salary"), 
				            new Department(rs.getInt("dno")), 
				            rs.getBytes("pic"));
	}

	@Override
	public Employee selectEmployeeByNo(Employee employee) throws SQLException {
		log.trace("selectEmployeeByNo()");
		String sql = "SELECT empno, empname, title, manager, salary, dno, pic FROM employee where empno = ?";
		Employee selEmp = null;
		try(Connection conn = MySQLjdbcUtil.getConnection();
				PreparedStatement pstmt = conn.prepareStatement(sql);){
			pstmt.setInt(1, employee.getEmpNo());
			log.trace(pstmt);
			try(ResultSet rs = pstmt.executeQuery()){
				if(rs.next()) {
					selEmp = getEmployee(rs);
				}
			}
		}
		return selEmp;
	}

	@Override
	public int insertEmployee(Employee employee) throws SQLException {
		log.trace("insertEmployee()");
		String sql = "insert into employee values(?, ?, ?, ?, ?, ?, ?)";
		
		try(Connection conn = MySQLjdbcUtil.getConnection();
				PreparedStatement pstmt = conn.prepareStatement(sql)){
			
			pstmt.setInt(1, employee.getEmpNo());
			pstmt.setString(2, employee.getEmpName());
			pstmt.setString(3, employee.getTitle());
			pstmt.setInt(4, employee.getManager().getEmpNo());
			pstmt.setInt(5, employee.getSalary());
			pstmt.setInt(6, employee.getDno().getDeptNo());
			log.trace(pstmt);
			pstmt.setBytes(7, employee.getPic());
			return pstmt.executeUpdate();
		}
	}

	@Override
	public int deleteEmployee(Employee employee) throws SQLException {
		log.trace("deleteEmployee()");
		String sql = "delete from employee where empno = ?";
		try(Connection conn = MySQLjdbcUtil.getConnection();
				PreparedStatement pstmt = conn.prepareStatement(sql)){
			pstmt.setInt(1, employee.getEmpNo());
			log.trace(pstmt);
			return pstmt.executeUpdate();
		}
	}

	@Override
	public int updateEmployee(Employee employee) throws SQLException {
		log.trace("updateEmployee()");
		String sql = "update employee "
			       + "set empname=?, title=?, manager=?, salary=?, dno=?, pic=? "
				   + "where empno=?";
		try(Connection conn = MySQLjdbcUtil.getConnection();
				PreparedStatement pstmt = conn.prepareStatement(sql)){
			pstmt.setString(1, employee.getEmpName());
			pstmt.setString(2, employee.getTitle());
			pstmt.setInt(3, employee.getManager().getEmpNo());
			pstmt.setInt(4, employee.getSalary());
			pstmt.setInt(5, employee.getDno().getDeptNo());
			pstmt.setBytes(6, employee.getPic());
			pstmt.setInt(7, employee.getEmpNo());
			System.out.println("----------2-------");
			System.out.println(pstmt);
			log.trace(pstmt);
			return pstmt.executeUpdate();
		}

	}

}
