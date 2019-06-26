package jdbc_study;

import java.sql.SQLException;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;

import jdbc_study.dao.DepartmentDao;
import jdbc_study.dao.DeptEmpTransactionDao;
import jdbc_study.dao.EmployeeDao;
import jdbc_study.daoimpl.DepartmentDaoImpl;
import jdbc_study.daoimpl.DeptEmpTransactionDaoImpl;
import jdbc_study.daoimpl.EmployeeDaoImpl;
import jdbc_study.dto.Department;
import jdbc_study.dto.Employee;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class DeptEmpTransactionDaoTest {
	static final Logger log = LogManager.getLogger();
	static DeptEmpTransactionDao dao;
	
	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		System.out.println();
		log.trace("Start DeptEmpTransactionDaoTest");
		dao = new DeptEmpTransactionDaoImpl();
	}

	@AfterClass
	public static void tearDownAfterClass() throws Exception {
		System.out.println();
		log.trace("Stop DeptEmpTransactionDaoTest");
		dao = null;
	}

	@Before
	public void setUp() throws Exception {
		System.out.println();
	}

	@Test
	public void test1DepartmentFail() {
		log.trace("Department fail");
		Department dept = new Department(1, "영업", 1);
		Employee emp = new Employee(1004, "서현진", "사원", new Employee(1003), 1500000, dept);

		int result = dao.trInsertEmpAndDept(emp, dept);
		log.trace(String.format("result %d", result));

		Assert.assertNotEquals(2, result);
	}
	
	@Test
	public void test2EmployeeFail() {
		log.trace("Employee fail");
		Department dept = new Department(6, "마케팅", 10);
		Employee emp = new Employee(1003, "조민희", "과장", new Employee(4377), 3000000, dept);

		int result = dao.trInsertEmpAndDept(emp, dept);
		log.trace(String.format("result %d", result));

		Assert.assertNotEquals(2, result);
	}

	@Test
	public void test3() throws SQLException {
		log.trace("Department , Employee Success~~!! ");
		Department dept = new Department(6, "마케팅", 10);
		Employee emp = new Employee(1004, "서현진", "사원", new Employee(1003), 1500000, dept);

		int result = dao.trInsertEmpAndDept(emp, dept);
		log.trace(String.format("result %d", result));

		Assert.assertEquals(2, result);
		
		EmployeeDao empDao = new EmployeeDaoImpl();
		DepartmentDao deptDao = new DepartmentDaoImpl();
		
		Employee newEmp = empDao.selectEmployeeByNo(emp);
		Department newDept = deptDao.selectDepartmentByNo(dept);
		log.trace(newEmp);
		log.trace(newDept);
		
	    empDao.deleteEmployee(emp);
	    deptDao.deleteDepartment(dept);
	}
}
